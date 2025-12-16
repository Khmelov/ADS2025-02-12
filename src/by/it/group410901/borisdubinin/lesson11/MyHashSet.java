package by.it.group410901.borisdubinin.lesson11;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

public class MyHashSet<E> implements Set<E> {

    // Внутренний статический класс для узла связного списка
    // Используется для разрешения коллизий методом цепочек
    private static class Node<E> {
        final E value;        // Хранимое значение (final для неизменности)
        Node<E> next;         // Ссылка на следующий узел в цепочке
        // Конструктор узла
        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    // Основная хеш-таблица - массив связных списков (цепочки)
    private Node<E>[] table;

    // Количество ненулевых элементов в таблице (для отслеживания загрузки)
    private int nonNullCount;

    // Общий размер множества (включая null элемент если есть)
    private int size;

    // Флаг наличия null элемента (null хранится отдельно)
    private boolean hasNull;

    // Начальная емкость таблицы по умолчанию
    private static final int DEFAULT_CAPACITY = 16;

    // Коэффициент загрузки для определения момента ресайза
    // Когда таблица заполнена на 75%, происходит увеличение размера
    private static final float LOAD_FACTOR = 0.75f;

    // Конструктор с заданной начальной емкостью
    @SuppressWarnings("unchecked")
    public MyHashSet(int capacity) {
        // Проверка валидности емкости
        if (capacity < 1) capacity = DEFAULT_CAPACITY;

        // Создание массива узлов (подавление предупреждения о неконтролируемом приведении)
        table = (Node<E>[]) new Node[capacity];

        // Инициализация счетчиков
        nonNullCount = 0;
        size = 0;
        hasNull = false;
    }

    // Конструктор по умолчанию - использует стандартную емкость
    public MyHashSet() {
        this(DEFAULT_CAPACITY);
    }

    // Вспомогательный метод для получения текущей емкости таблицы
    private int cap() {
        return table.length;
    }

    // Метод для вычисления индекса в таблице для заданного объекта
    private int indexFor(Object o, int len) {
        // Для null всегда возвращаем 0, для других объектов используем hashCode()
        int h = (o == null) ? 0 : o.hashCode();

        // Убираем знаковый бит (0x7fffffff = 0111 1111 1111 1111 1111 1111 1111 1111)
        // Это гарантирует неотрицательный индекс и равномерное распределение
        return (h & 0x7fffffff) % len;
    }

    // Метод для увеличения размера таблицы при превышении коэффициента загрузки
    @SuppressWarnings("unchecked")
    private void resize() {
        // Удваиваем емкость таблицы (битовый сдвиг влево на 1 эквивалентен умножению на 2)
        int newCap = cap() << 1;

        // Создание новой таблицы увеличенного размера
        Node<E>[] newTable = (Node<E>[]) new Node[newCap];

        // Перехеширование всех элементов из старой таблицы в новую
        for (int i = 0; i < cap(); i++) {
            Node<E> n = table[i];  // Получаем голову цепочки из старой таблицы

            // Проходим по всей цепочке
            while (n != null) {
                // Сохраняем ссылку на следующий узел до изменения
                Node<E> next = n.next;

                // Вычисляем новый индекс для текущего значения
                int idx = indexFor(n.value, newCap);

                // Вставляем узел в начало цепочки новой таблицы
                n.next = newTable[idx];  // Старая голова становится следующим узлом
                newTable[idx] = n;       // Текущий узел становится новой головой

                // Переходим к следующему узлу в старой цепочке
                n = next;
            }
        }

        // Заменяем старую таблицу новой
        table = newTable;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        // Очищаем все ячейки таблицы (устанавливаем в null)
        for (int i = 0; i < cap(); i++) {
            table[i] = null;
        }

        // Сбрасываем все счетчики и флаги
        nonNullCount = 0;
        size = 0;
        hasNull = false;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        // Обработка добавления null элемента
        if (e == null) {
            // Если null уже есть в множестве, возвращаем false
            if (hasNull) return false;

            // Иначе устанавливаем флаг и увеличиваем счетчик
            hasNull = true;
            size++;
            return true;
        }

        // Вычисляем индекс для ненулевого элемента
        int idx = indexFor(e, cap());

        // Проверяем цепочку на наличие дубликата
        for (Node<E> n = table[idx]; n != null; n = n.next) {
            // Используем equals() для сравнения значений
            if (e.equals(n.value)) return false;  // Элемент уже существует
        }

        // Создаем новый узел и добавляем его в начало цепочки
        Node<E> node = new Node<>(e, table[idx]);  // Новая голова цепочки
        table[idx] = node;

        // Обновляем счетчики
        nonNullCount++;
        size++;

        // Проверяем необходимость ресайза (только для ненулевых элементов)
        if (nonNullCount > (int)(cap() * LOAD_FACTOR)) {
            resize();
        }

        return true;
    }

    @Override
    public boolean remove(Object o) {
        // Обработка удаления null элемента
        if (o == null) {
            // Если null нет в множестве, возвращаем false
            if (!hasNull) return false;

            // Иначе сбрасываем флаг и уменьшаем счетчик
            hasNull = false;
            size--;
            return true;
        }

        // Вычисляем индекс для ненулевого элемента
        int idx = indexFor(o, cap());

        // Поиск элемента в цепочке для удаления
        Node<E> prev = null;      // Предыдущий узел (нужен для перелинковки)
        Node<E> cur = table[idx]; // Текущий узел (начинаем с головы цепочки)

        while (cur != null) {
            // Проверяем совпадение значений
            if (o.equals(cur.value)) {
                // Найден элемент для удаления
                if (prev == null) {
                    // Удаляем голову цепочки
                    table[idx] = cur.next;
                } else {
                    // Удаляем узел из середины/конца цепочки
                    prev.next = cur.next;
                }

                // Обновляем счетчики
                nonNullCount--;
                size--;
                return true;
            }

            // Переходим к следующему узлу
            prev = cur;
            cur = cur.next;
        }

        // Элемент не найден
        return false;
    }

    @Override
    public boolean contains(Object o) {
        // Проверка наличия null элемента
        if (o == null) return hasNull;

        // Вычисляем индекс для ненулевого элемента
        int idx = indexFor(o, cap());

        // Поиск элемента в цепочке
        for (Node<E> n = table[idx]; n != null; n = n.next) {
            if (o.equals(n.value)) return true;  // Элемент найден
        }

        // Элемент не найден
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;  // Флаг для определения первого элемента (чтобы не ставить лишнюю запятую)

        // Добавляем null элемент если он есть
        if (hasNull) {
            sb.append("null");
            first = false;
        }

        // Проходим по всем ячейкам таблицы
        for (int i = 0; i < cap(); i++) {
            // Проходим по всем узлам в цепочке
            for (Node<E> n = table[i]; n != null; n = n.next) {
                if (!first) {
                    sb.append(", ");  // Добавляем разделитель для всех элементов кроме первого
                }
                sb.append(String.valueOf(n.value));  // Безопасное преобразование в строку (работает с null)
                first = false;
            }
        }

        sb.append(']');
        return sb.toString();
    }

    // Реализация проверки содержания всех элементов коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        // Проверка на null коллекцию
        if (c == null) throw new NullPointerException();

        // Проверяем каждый элемент коллекции на наличие в множестве
        for (Object o : c) {
            if (!contains(o)) return false;  // Если хотя бы одного элемента нет, возвращаем false
        }
        return true;  // Все элементы найдены
    }

    // Реализация добавления всех элементов коллекции
    @Override
    public boolean addAll(Collection<? extends E> c) {
        // Проверка на null коллекцию
        if (c == null) throw new NullPointerException();

        boolean mod = false;  // Флаг изменения множества
        for (E e : c) {
            if (add(e)) mod = true;  // Если хотя бы один элемент был добавлен, устанавливаем флаг
        }
        return mod;
    }

    // Реализация удаления всех элементов коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        // Проверка на null коллекцию
        if (c == null) throw new NullPointerException();

        boolean mod = false;  // Флаг изменения множества
        for (Object o : c) {
            if (remove(o)) mod = true;  // Если хотя бы один элемент был удален, устанавливаем флаг
        }
        return mod;
    }

    // Реализация операции пересечения - оставить только элементы, содержащиеся в коллекции c
    @Override
    public boolean retainAll(Collection<?> c) {
        // Проверка на null коллекцию
        if (c == null) throw new NullPointerException();

        boolean changed = false;  // Флаг изменения множества

        // Обработка null элемента
        if (hasNull && !c.contains(null)) {
            hasNull = false;
            size--;
            changed = true;
        }

        // Проходим по всем цепочкам в таблице
        for (int i = 0; i < cap(); i++) {
            Node<E> prev = null;      // Предыдущий узел
            Node<E> cur = table[i];   // Текущий узел

            while (cur != null) {
                if (!c.contains(cur.value)) {
                    // Элемент не содержится в коллекции c - удаляем его
                    if (prev == null) {
                        // Удаляем голову цепочки
                        table[i] = cur.next;
                    } else {
                        // Удаляем узел из середины/конца цепочки
                        prev.next = cur.next;
                    }

                    // Обновляем текущий указатель (остаемся на месте для проверки следующего узла)
                    cur = (prev == null) ? table[i] : prev.next;

                    // Обновляем счетчики
                    nonNullCount--;
                    size--;
                    changed = true;
                } else {
                    // Элемент содержится в коллекции c - переходим к следующему
                    prev = cur;
                    cur = cur.next;
                }
            }
        }
        return changed;
    }

    // Не реализованные методы (требуются для полной реализации интерфейса Set)

    @Override
    public Iterator<E> iterator() {
        // Итератор не реализован - выбрасываем исключение
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        // Преобразование в массив Object не реализовано
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        // Преобразование в типизированный массив не реализовано
        throw new UnsupportedOperationException();
    }

    // Стандартные реализации от Object
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}