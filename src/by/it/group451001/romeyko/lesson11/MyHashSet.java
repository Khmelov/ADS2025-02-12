package by.it.group451001.romeyko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        final E value;
        Node<E> next;
        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size;
    private int threshold;
    private final float loadFactor;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    public MyHashSet() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashSet(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) initialCapacity = DEFAULT_INITIAL_CAPACITY;
        if (loadFactor <= 0) loadFactor = DEFAULT_LOAD_FACTOR;
        this.loadFactor = loadFactor;
        table = (Node<E>[]) new Node[initialCapacity];
        threshold = (int) (initialCapacity * loadFactor);
        size = 0;
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
            // rehash all elements
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
        // вставка в голову списка бакета
        table[idx] = new Node<>(e, table[idx]);
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
                if (prev == null) {
                    table[idx] = node.next;
                } else {
                    prev.next = node.next;
                }
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
        boolean first = true;
        for (int i = 0; i < table.length; i++) {
            Node<E> node = table[i];
            while (node != null) {
                if (!first) sb.append(", ");
                sb.append(node.value);
                first = false;
                node = node.next;
            }
        }
        sb.append(']');
        return sb.toString();
    }

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }
    @Override public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) if (add(e)) modified = true;
        return modified;
    }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) if (remove(o)) modified = true;
        return modified;
    }
    @Override public boolean equals(Object o) { throw new UnsupportedOperationException(); }
    @Override public int hashCode() { throw new UnsupportedOperationException(); }

}
