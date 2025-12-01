package by.it.group451003.klimintsionak.lesson11;

import java.util.Collection;

public class MyLinkedHashSet<E> implements java.util.Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private static class Node<E> {
        E data;
        Node<E> hashNext;
        Node<E> prev;
        Node<E> next;

        Node(E data) {
            this.data = data;
        }
    }

    private Node<E>[] buckets;
    private Node<E> head;
    private Node<E> tail;
    private int size;

    public MyLinkedHashSet() {
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
                Node<E> next = node.hashNext;
                node.hashNext = newBuckets[newIndex];
                newBuckets[newIndex] = node;
                node = next;
            }
        }
        buckets = newBuckets;
    }

    private void linkLast(Node<E> node) {
        if (tail == null) {
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
    }

    private void unlink(Node<E> node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
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
        int index = getIndex(o, buckets.length);
        Node<E> node = buckets[index];
        while (node != null) {
            if (node.data == null ? o == null : node.data.equals(o)) {
                return true;
            }
            node = node.hashNext;
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
        newNode.hashNext = buckets[index];
        buckets[index] = newNode;
        linkLast(newNode);
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
                    buckets[index] = node.hashNext;
                } else {
                    prev.hashNext = node.hashNext;
                }
                unlink(node);
                size--;
                return true;
            }
            prev = node;
            node = node.hashNext;
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = null;
        }
        head = tail = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> node = head;
        while (node != null) {
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append(node.data);
            node = node.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            if (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> node = head;
        while (node != null) {
            Node<E> next = node.next;
            if (!c.contains(node.data)) {
                remove(node.data);
                modified = true;
            }
            node = next;
        }
        return modified;
    }

    // Unsupported methods
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
}