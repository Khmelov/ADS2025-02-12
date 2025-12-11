package by.it.group451001.romeyko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    // Узел для хранения элемента и связей
    private static class Node<E> {
        final E value;
        Node<E> next; // для коллизий
        Node<E> prevOrder; // для порядка вставки
        Node<E> nextOrder; // для порядка вставки

        Node(E value) {
            this.value = value;
        }
    }

    private Node<E>[] table;
    private int size;
    private int threshold;
    private final float loadFactor;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<E> head; // первый добавленный элемент
    private Node<E> tail; // последний добавленный элемент

    public MyLinkedHashSet() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyLinkedHashSet(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) initialCapacity = DEFAULT_INITIAL_CAPACITY;
        if (loadFactor <= 0) loadFactor = DEFAULT_LOAD_FACTOR;
        this.loadFactor = loadFactor;
        table = (Node<E>[]) new Node[initialCapacity];
        threshold = (int) (initialCapacity * loadFactor);
    }

    private int indexFor(Object o, int length) {
        int h = (o == null) ? 0 : o.hashCode();
        return (h & 0x7FFFFFFF) % length;
    }

    private boolean equalsElement(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    private void resizeIfNeeded() {
        if (size >= threshold) {
            int newCapacity = table.length * 2;
            Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];
            for (int i = 0; i < table.length; i++) {
                Node<E> node = table[i];
                while (node != null) {
                    Node<E> next = node.next;
                    int idx = indexFor(node.value, newCapacity);
                    node.next = newTable[idx];
                    newTable[idx] = node;
                    node = next;
                }
            }
            table = newTable;
            threshold = (int) (newCapacity * loadFactor);
        }
    }

    @Override
    public boolean add(E e) {
        int idx = indexFor(e, table.length);
        Node<E> node = table[idx];
        while (node != null) {
            if (equalsElement(node.value, e)) return false; // уже есть
            node = node.next;
        }
        // создаём новый узел
        Node<E> newNode = new Node<>(e);
        // вставляем в бакет (в голову)
        newNode.next = table[idx];
        table[idx] = newNode;

        // добавляем в конец порядка вставки
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.nextOrder = newNode;
            newNode.prevOrder = tail;
            tail = newNode;
        }

        size++;
        resizeIfNeeded();
        return true;
    }

    @Override
    public boolean contains(Object o) {
        int idx = indexFor(o, table.length);
        Node<E> node = table[idx];
        while (node != null) {
            if (equalsElement(node.value, o)) return true;
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexFor(o, table.length);
        Node<E> node = table[idx];
        Node<E> prev = null;
        while (node != null) {
            if (equalsElement(node.value, o)) {
                // удаляем из бакета
                if (prev == null) {
                    table[idx] = node.next;
                } else {
                    prev.next = node.next;
                }

                // удаляем из порядка вставки
                if (node.prevOrder != null) node.prevOrder.nextOrder = node.nextOrder;
                else head = node.nextOrder;

                if (node.nextOrder != null) node.nextOrder.prevOrder = node.prevOrder;
                else tail = node.prevOrder;

                size--;
                return true;
            }
            prev = node;
            node = node.next;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) table[i] = null;
        head = tail = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node<E> current = head;
        boolean first = true;
        while (current != null) {
            if (!first) sb.append(", ");
            sb.append(current.value);
            first = false;
            current = current.nextOrder;
        }
        sb.append(']');
        return sb.toString();
    }

    // --- Методы с коллекциями ---

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c)
            if (add(e)) modified = true;
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c)
            if (remove(o)) modified = true;
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.nextOrder;
            if (!c.contains(current.value)) {
                remove(current.value);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    // --- Методы Set, не требуемые заданием ---
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean equals(Object o) { throw new UnsupportedOperationException(); }
    @Override public int hashCode() { throw new UnsupportedOperationException(); }
}
