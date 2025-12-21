package by.it.group451004.volynets.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<E>[] table;
    private Node<E> head;
    private Node<E> tail;
    private int size;

    // узел для хеш-таблицы с ссылками для поддержания порядка добавления
    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> after;
        Node<E> before;
        int hash;

        Node(int hash, E data, Node<E> next) {
            this.hash = hash;
            this.data = data;
            this.next = next;
        }
    }

    public MyLinkedHashSet() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    public MyLinkedHashSet(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        table = new Node[initialCapacity];
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    // строковое представление в порядке добавления
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<E> current = head;
        int count = 0;

        while (current != null) {
            sb.append(current.data);
            if (++count < size) {
                sb.append(", ");
            }
            current = current.after;
        }

        sb.append("]");
        return sb.toString();
    }

    // количество элементов в множестве
    @Override
    public int size() {
        return size;
    }

    // очистка множества
    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        head = tail = null;
        size = 0;
    }

    // проверка пустоты множества
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // добавление элемента в множество с сохранением порядка
    @Override
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException();
        }

        // проверяем необходимость расширения
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }

        int hash = hash(e);
        int index = indexFor(hash, table.length);

        // проверяем наличие элемента в цепочке
        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash && (e == current.data || e.equals(current.data))) {
                return false; // элемент уже существует
            }
            current = current.next;
        }

        // создаем новый узел
        Node<E> newNode = new Node<>(hash, e, table[index]);
        table[index] = newNode;

        // добавляем в конец списка порядка добавления
        linkLast(newNode);
        size++;
        return true;
    }

    // удаление элемента из множества
    @Override
    public boolean remove(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }

        int hash = hash(o);
        int index = indexFor(hash, table.length);

        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if (current.hash == hash && (o == current.data || o.equals(current.data))) {
                // удаляем из хеш-таблицы
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }

                // удаляем из списка порядка добавления
                unlink(current);
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }

        return false;
    }

    // проверка наличия элемента в множестве
    @Override
    public boolean contains(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }

        int hash = hash(o);
        int index = indexFor(hash, table.length);

        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash && (o == current.data || o.equals(current.data))) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    // содержит ли множество все элементы коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    // добавление всех элементов коллекции
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    // удаление всех элементов, которые есть в коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        // Создаем временный массив для элементов, которые нужно сохранить
        Object[] temp = new Object[size];
        int newSize = 0;

        // Собираем элементы, которые НЕ нужно удалять
        Node<E> current = head;
        while (current != null) {
            if (!c.contains(current.data)) {
                temp[newSize++] = current.data;
            }
            current = current.after;
        }

        // Если размер изменился - перестраиваем множество
        if (newSize != size) {
            modified = true;
            clear();
            for (int i = 0; i < newSize; i++) {
                @SuppressWarnings("unchecked")
                E element = (E) temp[i];
                add(element);
            }
        }
        return modified;
    }

    // оставить только элементы, которые есть в коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        // Создаем временный массив для элементов, которые нужно сохранить
        Object[] temp = new Object[size];
        int newSize = 0;

        // Собираем элементы, которые нужно сохранить
        Node<E> current = head;
        while (current != null) {
            if (c.contains(current.data)) {
                temp[newSize++] = current.data;
            }
            current = current.after;
        }

        // Если размер изменился - перестраиваем множество
        if (newSize != size) {
            modified = true;
            clear();
            for (int i = 0; i < newSize; i++) {
                @SuppressWarnings("unchecked")
                E element = (E) temp[i];
                add(element);
            }
        }
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы                     ///////
    /////////////////////////////////////////////////////////////////////////

    // добавление узла в конец списка порядка добавления
    private void linkLast(Node<E> node) {
        if (tail == null) {
            // первый элемент
            head = tail = node;
        } else {
            // добавляем после tail
            tail.after = node;
            node.before = tail;
            tail = node;
        }
    }

    // удаление узла из списка порядка добавления
    private void unlink(Node<E> node) {
        Node<E> before = node.before;
        Node<E> after = node.after;

        if (before == null) {
            // удаляем первый элемент
            head = after;
        } else {
            before.after = after;
            node.before = null;
        }

        if (after == null) {
            // удаляем последний элемент
            tail = before;
        } else {
            after.before = before;
            node.after = null;
        }
    }

    // вычисление хеш-кода объекта
    private int hash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    // вычисление индекса в таблице по хеш-коду
    private int indexFor(int hash, int length) {
        return (hash & 0x7FFFFFFF) % length;
    }

    // увеличение размера таблицы при переполнении
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * 2;
        Node<E>[] newTable = new Node[newCapacity];

        // перераспределяем элементы в новую таблицу
        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            while (current != null) {
                Node<E> next = current.next;
                int newIndex = indexFor(current.hash, newCapacity);
                current.next = newTable[newIndex];
                newTable[newIndex] = current;
                current = next;
            }
        }

        table = newTable;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы Set - заглушки          ///////
    /////////////////////////////////////////////////////////////////////////

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
}