package by.it.group451003.halubionak.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;
        Node(E value) { this.value = value; }
    }

    private Node<E>[] table;
    private int size = 0;
    private static final int INITIAL_CAPACITY = 16;

    public MyHashSet() {
        table = (Node<E>[]) new Node[INITIAL_CAPACITY];
    }

    private int index(Object o) {
        return (o == null ? 0 : o.hashCode() & 0x7fffffff) % table.length;
    }

    private void resizeIfNeeded() {
        if (size > table.length * 0.75) {
            Node<E>[] oldTable = table;
            table = (Node<E>[]) new Node[table.length * 2];
            size = 0;
            for (Node<E> head : oldTable) {
                while (head != null) {
                    add(head.value);
                    head = head.next;
                }
            }
        }
    }

    @Override
    public boolean add(E e) {
        resizeIfNeeded();
        int idx = index(e);
        Node<E> curr = table[idx];
        while (curr != null) {
            if ((e == null && curr.value == null) || (e != null && e.equals(curr.value))) {
                return false; // уже есть
            }
            curr = curr.next;
        }
        Node<E> newNode = new Node<>(e);
        newNode.next = table[idx];
        table[idx] = newNode;
        size++;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        int idx = index(o);
        Node<E> curr = table[idx];
        while (curr != null) {
            if ((o == null && curr.value == null) || (o != null && o.equals(curr.value))) {
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        int idx = index(o);
        Node<E> curr = table[idx];
        Node<E> prev = null;
        while (curr != null) {
            if ((o == null && curr.value == null) || (o != null && o.equals(curr.value))) {
                if (prev == null) table[idx] = curr.next;
                else prev.next = curr.next;
                size--;
                return true;
            }
            prev = curr;
            curr = curr.next;
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
        for (Node<E> head : table) {
            Node<E> curr = head;
            while (curr != null) {
                if (!first) sb.append(", ");
                sb.append(curr.value);
                first = false;
                curr = curr.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // =========== Заглушки для Collection/Set ===============
    @Override
    public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override
    public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override
    public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override
    public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override
    public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override
    public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override
    public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}
