package by.it.group410901.zaverach.lesson11;

import java.util.Set;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyHashSet<E> implements Set<E> {

    private Node<E>[] table;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    private static class Node<E> {
        E value;
        Node<E> next;

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    public MyHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    private int index(Object o) {
        return Math.abs(o.hashCode() % table.length);
    }

    @Override
    public boolean add(E e) {
        int idx = index(e);
        Node<E> current = table[idx];

        while (current != null) {
            if (current.value.equals(e))
                return false;
            current = current.next;
        }

        table[idx] = new Node<>(e, table[idx]);
        size++;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        int idx = index(o);
        Node<E> current = table[idx];
        while (current != null) {
            if (current.value.equals(o))
                return true;
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        int idx = index(o);
        Node<E> current = table[idx];
        Node<E> prev = null;

        while (current != null) {
            if (current.value.equals(o)) {
                if (prev == null)
                    table[idx] = current.next;
                else
                    prev.next = current.next;

                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++)
            table[i] = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        boolean first = true;
        for (Node<E> head : table) {
            Node<E> current = head;
            while (current != null) {
                if (!first) sb.append(", ");
                sb.append(current.value);
                first = false;
                current = current.next;
            }
        }

        sb.append("]");
        return sb.toString();
    }

    // Необязательные методы
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
}
