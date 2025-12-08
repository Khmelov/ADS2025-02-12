package by.it.group451003.platonova.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;
        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 16;

    public MyHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
    }

    private int index(Object o) {
        return (o == null ? 0 : o.hashCode() & 0x7FFFFFFF) % table.length;
    }

    @Override
    public boolean add(E e) {
        int idx = index(e);
        Node<E> cur = table[idx];
        while (cur != null) {
            if ((e == null && cur.value == null) || (e != null && e.equals(cur.value))) {
                return false; // уже есть
            }
            cur = cur.next;
        }
        table[idx] = new Node<>(e, table[idx]);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = index(o);
        Node<E> cur = table[idx];
        Node<E> prev = null;
        while (cur != null) {
            if ((o == null && cur.value == null) || (o != null && o.equals(cur.value))) {
                if (prev == null) table[idx] = cur.next;
                else prev.next = cur.next;
                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        int idx = index(o);
        Node<E> cur = table[idx];
        while (cur != null) {
            if ((o == null && cur.value == null) || (o != null && o.equals(cur.value))) {
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) table[i] = null;
        size = 0;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> bucket : table) {
            Node<E> cur = bucket;
            while (cur != null) {
                if (!first) sb.append(", ");
                sb.append(cur.value);
                first = false;
                cur = cur.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // ===== Заглушки для Set / Collection =====
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}

