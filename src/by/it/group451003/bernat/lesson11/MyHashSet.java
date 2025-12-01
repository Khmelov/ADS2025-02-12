package by.it.group451003.bernat.lesson11;

import java.util.Collection;

public class MyHashSet<E> implements java.util.Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
        }
    }

    private Node<E>[] buckets;
    private int size;

    public MyHashSet() {
        buckets = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    private int getHash(Object o) {
        return o == null ? 0 : o.hashCode();
    }

    private int getIndex(Object o, int length) {
        int hash = getHash(o);
        return (hash & 0x7FFFFFFF) % length;
    }

    private void resize() {
        int newCapacity = buckets.length * 2;
        Node<E>[] newBuckets = new Node[newCapacity];
        for (Node<E> node : buckets) {
            while (node != null) {
                int newIndex = getIndex(node.data, newCapacity);
                Node<E> next = node.next;
                node.next = newBuckets[newIndex];
                newBuckets[newIndex] = node;
                node = next;
            }
        }
        buckets = newBuckets;
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
        int index = getIndex(o, buckets.length);
        Node<E> node = buckets[index];
        while (node != null) {
            if (node.data == null ? o == null : node.data.equals(o)) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) {
            return false;
        }
        int index = getIndex(e, buckets.length);
        Node<E> newNode = new Node<>(e);
        newNode.next = buckets[index];
        buckets[index] = newNode;
        size++;
        if (size > LOAD_FACTOR * buckets.length) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = getIndex(o, buckets.length);
        Node<E> prev = null;
        Node<E> node = buckets[index];
        while (node != null) {
            if (node.data == null ? o == null : node.data.equals(o)) {
                if (prev == null) {
                    buckets[index] = node.next;
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
    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = null;
        }
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> node : buckets) {
            while (node != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(node.data);
                first = false;
                node = node.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // Unsupported methods (as per task, only specified ones are mandatory)
    @Override
    public java.util.Iterator<E> iterator() {
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
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
}