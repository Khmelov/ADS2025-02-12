package by.it.group410901.zubchonak.lesson11;

import java.util.*;

public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;
        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private static final int INITIAL_CAPACITY = 16;
    private Node<E>[] buckets;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        buckets = new Node[INITIAL_CAPACITY];
    }

    private int hash(Object key) {
        int h = key.hashCode();
        return (h ^ (h >>> 16)) & (buckets.length - 1);
    }

    private void resize() {
        Node<E>[] oldBuckets = buckets;
        @SuppressWarnings("unchecked")
        Node<E>[] newBuckets = new Node[oldBuckets.length * 2];
        buckets = newBuckets;
        size = 0;

        for (Node<E> head : oldBuckets) {
            while (head != null) {
                add(head.value);
                head = head.next;
            }
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        int index = hash(o);
        Node<E> current = buckets[index];
        while (current != null) {
            if (current.value.equals(o)) return true;
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if (e == null) return false;
        if (contains(e)) return false;

        if (size >= buckets.length * 0.75) {
            resize();
        }

        int index = hash(e);
        buckets[index] = new Node<>(e, buckets[index]);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        int index = hash(o);
        Node<E> current = buckets[index];
        Node<E> prev = null;

        while (current != null) {
            if (current.value.equals(o)) {
                if (prev == null) {
                    buckets[index] = current.next;
                } else {
                    prev.next = current.next;
                }
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
        buckets = new Node[buckets.length];
        size = 0;
    }

    // === toString — порядок произвольный ===
    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> head : buckets) {
            Node<E> current = head;
            while (current != null) {
                if (!first) sb.append(", ");
                sb.append(current.value);
                first = false;
                current = current.next;
            }
        }
        return sb.append("]").toString();
    }

    // === Остальные методы Set — не используются в тесте, но нужны для компиляции ===
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
}