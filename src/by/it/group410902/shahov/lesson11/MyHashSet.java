package by.it.group410902.shahov.lesson11;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyHashSet<E> implements Set<E> {

    // Внутренний класс для узла связного списка (разрешение коллизий методом цепочек)
    private static class Node<E> {
        final E value;        // Хранимое значение
        Node<E> next;         // Ссылка на следующий узел в цепочке
        Node(E value, Node<E> next) { this.value = value; this.next = next; }
    }

    private Node<E>[] table;      // Массив бакетов (корзин) для хранения элементов
    private int nonNullCount;     // Количество не-null бакетов (для определения необходимости resize)
    private int size;             // Общее количество элементов в множестве
    private boolean hasNull;      // Флаг наличия null элемента (null хранится отдельно)
    private static final int DEFAULT_CAPACITY = 16;   // Начальная емкость хэш-таблицы
    private static final float LOAD_FACTOR = 0.75f;   // Коэффициент загрузки для resize

    // Конструктор с заданной начальной емкостью
    @SuppressWarnings("unchecked")
    public MyHashSet(int capacity) {
        if (capacity < 1) capacity = DEFAULT_CAPACITY;
        table = (Node<E>[]) new Node[capacity];
        nonNullCount = 0;
        size = 0;
        hasNull = false;
    }

    // Конструктор по умолчанию
    public MyHashSet() {
        this(DEFAULT_CAPACITY);
    }

    // Вычисление индекса бакета для объекта
    private int indexFor(Object o, int length) {
        int h = (o == null) ? 0 : o.hashCode();  // Для null используем хэш 0
        return (h & 0x7fffffff) % length;        // Убираем знак и берем по модулю
    }

    // Увеличение размера хэш-таблицы при превышении коэффициента загрузки
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * 2;      // Удваиваем емкость
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];

        // Перехеширование всех элементов в новую таблицу
        for (int i = 0; i < table.length; i++) {
            Node<E> n = table[i];
            while (n != null) {
                Node<E> next = n.next;                    // Сохраняем ссылку на следующий
                int idx = indexFor(n.value, newCapacity); // Новый индекс
                n.next = newTable[idx];                   // Вставляем в начало цепочки
                newTable[idx] = n;
                n = next;
            }
        }
        table = newTable;
    }

    // Возвращает количество элементов в множестве
    @Override
    public int size() {
        return size;
    }

    // Очищает множество, удаляя все элементы
    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;  // Очищаем все бакеты
        }
        nonNullCount = 0;
        size = 0;
        hasNull = false;      // Сбрасываем флаг null
    }

    // Проверяет, пусто ли множество
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Добавляет элемент в множество
    @Override
    public boolean add(E e) {
        if (e == null) {
            if (hasNull) return false;  // null уже есть
            hasNull = true;
            size++;
            return true;
        }

        int idx = indexFor(e, table.length);
        // Проверяем наличие элемента в цепочке
        for (Node<E> n = table[idx]; n != null; n = n.next) {
            if (e.equals(n.value)) return false;  // Элемент уже существует
        }

        // Добавляем новый элемент в начало цепочки
        table[idx] = new Node<>(e, table[idx]);
        nonNullCount++;
        size++;

        // Проверяем необходимость увеличения размера таблицы
        if (nonNullCount > (int)(table.length * LOAD_FACTOR)) {
            resize();
        }
        return true;
    }

    // Удаляет элемент из множества
    @Override
    public boolean remove(Object o) {
        if (o == null) {
            if (!hasNull) return false;  // null нет в множестве
            hasNull = false;
            size--;
            return true;
        }

        int idx = indexFor(o, table.length);
        Node<E> prev = null;
        Node<E> cur = table[idx];

        // Поиск элемента в цепочке
        while (cur != null) {
            if (o.equals(cur.value)) {
                // Удаление элемента из цепочки
                if (prev == null) {
                    table[idx] = cur.next;  // Удаляем первый элемент
                } else {
                    prev.next = cur.next;   // Удаляем из середины/конца
                }
                nonNullCount--;
                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;  // Элемент не найден
    }

    // Проверяет наличие элемента в множестве
    @Override
    public boolean contains(Object o) {
        if (o == null) return hasNull;  // Проверяем отдельно хранимый null

        int idx = indexFor(o, table.length);
        // Линейный поиск в цепочке
        for (Node<E> n = table[idx]; n != null; n = n.next) {
            if (o.equals(n.value)) return true;
        }
        return false;
    }

    // Возвращает строковое представление множества
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;

        // Добавляем null если есть
        if (hasNull) {
            sb.append("null");
            first = false;
        }

        // Добавляем все элементы из бакетов
        for (int i = 0; i < table.length; i++) {
            for (Node<E> n = table[i]; n != null; n = n.next) {
                if (!first) sb.append(", ");
                sb.append(String.valueOf(n.value));
                first = false;
            }
        }
        sb.append(']');
        return sb.toString();
    }

    // Проверяет, содержатся ли все элементы коллекции в множестве
    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    // Добавляет все элементы из коллекции в множество
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;  // Хотя бы один элемент добавлен
        }
        return modified;
    }

    // Удаляет все элементы, содержащиеся в указанной коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        for (Object o : c) {
            if (remove(o)) modified = true;  // Хотя бы один элемент удален
        }
        return modified;
    }

    // Оставляет только элементы, содержащиеся в указанной коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;

        // Обрабатываем null отдельно
        if (hasNull && !c.contains(null)) {
            hasNull = false;
            size--;
            changed = true;
        }

        // Проходим по всем бакетам и удаляем элементы не из коллекции c
        for (int i = 0; i < table.length; i++) {
            Node<E> prev = null;
            Node<E> cur = table[i];
            while (cur != null) {
                if (!c.contains(cur.value)) {
                    // Удаляем элемент
                    if (prev == null) {
                        table[i] = cur.next;
                    } else {
                        prev.next = cur.next;
                    }
                    cur = (prev == null) ? table[i] : prev.next;
                    nonNullCount--;
                    size--;
                    changed = true;
                } else {
                    prev = cur;
                    cur = cur.next;
                }
            }
        }
        return changed;
    }

    // Не реализованные методы интерфейса Set
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

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}