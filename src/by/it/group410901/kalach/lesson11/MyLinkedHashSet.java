package by.it.group410901.kalach.lesson11;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

public class MyLinkedHashSet<E> implements Set<E> {

    // Внутренний статический класс для узла двусвязного списка
    // Отличается от обычного HashSet наличием ссылок before/after для поддержания порядка
    private static class Node<E> {
        final E value;        // Хранимое значение (final для неизменности)
        Node<E> next;         // Ссылка на следующий узел в хеш-цепочке (для разрешения коллизий)
        Node<E> before, after; // Ссылки для поддержания порядка добавления в двусвязном списке
        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    // Основная хеш-таблица - массив связных списков (цепочки для разрешения коллизий)
    private Node<E>[] table;

    // Указатели на начало и конец двусвязного списка для поддержания порядка итерации
    private Node<E> head, tail;

    // Общий размер множества (включая null элемент если есть)
    private int size;

    // Флаг наличия null элемента (null хранится отдельно)
    private boolean hasNull;

    // Начальная емкость таблицы по умолчанию
    private static final int DEFAULT_CAP = 16;

    // Коэффициент загрузки для определения момента ресайза
    private static final float LOAD_FACTOR = 0.75f;

    // Конструктор с заданной начальной емкостью
    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int capacity) {
        // Проверка валидности емкости
        if (capacity < 1) capacity = DEFAULT_CAP;

        // Создание массива узлов
        table = (Node<E>[]) new Node[capacity];

        // Инициализация указателей двусвязного списка
        head = tail = null;
        size = 0;
        hasNull = false;
    }

    // Конструктор по умолчанию - использует стандартную емкость
    public MyLinkedHashSet() {
        this(DEFAULT_CAP);
    }

    // Вспомогательный метод для получения текущей емкости таблицы
    private int cap() {
        return table.length;
    }

    // Метод для вычисления индекса в таблице для заданного объекта
    private int indexFor(Object o, int len) {
        // Для null всегда возвращаем 0, для других объектов используем hashCode()
        int h = (o == null) ? 0 : o.hashCode();

        // Убираем знаковый бит для неотрицательного индекса
        return (h & 0x7fffffff) % len;
    }

    // Метод для увеличения размера таблицы при превышении коэффициента загрузки
    @SuppressWarnings("unchecked")
    private void resize() {
        // Удваиваем емкость таблицы
        int newCap = cap() << 1;

        // Создание новой таблицы увеличенного размера
        Node<E>[] newTable = (Node<E>[]) new Node[newCap];

        // Перехеширование всех элементов из старой таблицы в новую
        // Важно: порядок в двусвязном списке не меняется при ресайзе
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
        // Очищаем все ячейки таблицы
        for (int i = 0; i < cap(); i++) {
            table[i] = null;
        }

        // Сбрасываем указатели двусвязного списка и все счетчики
        head = tail = null;
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

            // Иначе устанавливаем флаг, добавляем в двусвязный список и увеличиваем счетчик
            hasNull = true;
            linkNode(null);  // Специальная обработка для null
            size++;
            return true;
        }

        // Вычисляем индекс для ненулевого элемента
        int idx = indexFor(e, cap());

        // Проверяем цепочку на наличие дубликата
        for (Node<E> n = table[idx]; n != null; n = n.next) {
            if (e.equals(n.value)) return false;  // Элемент уже существует
        }

        // Создаем новый узел и добавляем его в начало хеш-цепочки
        Node<E> node = new Node<>(e, table[idx]);
        table[idx] = node;

        // Добавляем узел в двусвязный список (в конец для сохранения порядка добавления)
        linkNode(node);

        // Обновляем счетчик
        size++;

        // Проверяем необходимость ресайза (учитываем все элементы, включая null)
        if (size > cap() * LOAD_FACTOR) {
            resize();
        }

        return true;
    }

    // Метод для добавления узла в двусвязный список (поддержание порядка итерации)
    private void linkNode(Node<E> node) {
        if (node == null) {
            // Специальная обработка для null элемента - создаем фиктивный узел
            Node<E> fake = new Node<>(null, null);
            fake.before = tail;

            // Обновляем ссылки в двусвязном списке
            if (tail != null) {
                tail.after = fake;
            }
            tail = fake;

            // Если список был пуст, устанавливаем голову
            if (head == null) {
                head = fake;
            }
            return;
        }

        // Стандартная процедура добавления узла в конец двусвязного списка
        node.before = tail;  // Предыдущим для нового узла становится старый хвост

        if (tail != null) {
            tail.after = node;  // Следующим для старого хвоста становится новый узел
        }
        tail = node;  // Новый узел становится новым хвостом

        // Если список был пуст, новый узел также становится головой
        if (head == null) {
            head = node;
        }
    }

    // Метод для удаления узла из двусвязного списка
    private void unlinkNode(Node<E> node) {
        // Обновляем ссылку у предыдущего узла (если он есть)
        if (node.before != null) {
            node.before.after = node.after;  // Пропускаем удаляемый узел
        } else {
            head = node.after;  // Если удаляем голову, обновляем указатель head
        }

        // Обновляем ссылку у следующего узла (если он есть)
        if (node.after != null) {
            node.after.before = node.before;  // Пропускаем удаляемый узел
        } else {
            tail = node.before;  // Если удаляем хвост, обновляем указатель tail
        }
    }

    @Override
    public boolean remove(Object o) {
        // Обработка удаления null элемента
        if (o == null) {
            if (!hasNull) return false;  // Если null нет, возвращаем false

            hasNull = false;

            // Поиск фиктивного узла для null в двусвязном списке и его удаление
            Node<E> cur = head;
            while (cur != null) {
                if (cur.value == null) {
                    unlinkNode(cur);
                    break;
                }
                cur = cur.after;
            }

            size--;
            return true;
        }

        // Удаление ненулевого элемента
        int idx = indexFor(o, cap());
        Node<E> prev = null;
        Node<E> cur = table[idx];

        // Поиск элемента в хеш-цепочке
        while (cur != null) {
            if (o.equals(cur.value)) {
                // Найден элемент для удаления - удаляем из хеш-цепочки
                if (prev == null) {
                    table[idx] = cur.next;  // Удаляем голову цепочки
                } else {
                    prev.next = cur.next;   // Удаляем из середины/конца цепочки
                }

                // Удаляем узел из двусвязного списка
                unlinkNode(cur);

                size--;
                return true;
            }

            // Переходим к следующему узлу в цепочке
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

        // Поиск ненулевого элемента в хеш-таблице
        int idx = indexFor(o, cap());
        for (Node<E> n = table[idx]; n != null; n = n.next) {
            if (o.equals(n.value)) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;

        // Обход в порядке добавления (используя двусвязный список)
        Node<E> cur = head;
        while (cur != null) {
            if (!first) {
                sb.append(", ");
            }
            // Для фиктивного узла null выводим "null", для остальных - их значение
            sb.append(String.valueOf(cur.value));
            first = false;
            cur = cur.after;  // Переход к следующему узлу в порядке добавления
        }

        sb.append(']');
        return sb.toString();
    }

    // Стандартные реализации методов интерфейса Set

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        boolean mod = false;
        for (E e : c) {
            if (add(e)) mod = true;
        }
        return mod;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean mod = false;
        for (Object o : c) {
            if (remove(o)) mod = true;
        }
        return mod;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;

        // Обход в порядке добавления через двусвязный список
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.after;  // Сохраняем ссылку на следующий узел до возможного удаления
            if (!c.contains(cur.value)) {
                remove(cur.value);  // Удаляем элемент, если его нет в коллекции c
                changed = true;
            }
            cur = next;  // Переходим к следующему узлу
        }
        return changed;
    }

    // Не реализованные методы (требуются для полной реализации интерфейса Set)

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
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