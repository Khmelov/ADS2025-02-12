package by.it.group451001.drzhevetskiy.lesson11;

import java.util.Collection;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> prevInsertion;
        Node<E> nextInsertion;

        Node(E value) {
            this.value = value;
        }
    }

    private Node<E>[] table;
    private int capacity;
    private int size;
    private int threshold;
    private final float loadFactor = 0.75f;

    private static final int DEFAULT_CAPACITY = 16;

    private Node<E> head;
    private Node<E> tail;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        this.capacity = DEFAULT_CAPACITY;
        this.table = (Node<E>[]) new Node[capacity];
        this.threshold = (int) (capacity * loadFactor);
        this.size = 0;
    }

    private int hash(Object o) {
        int h = (o == null) ? 0 : o.hashCode();
        return h & 0x7FFFFFFF;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = capacity * 2;
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];

        Node<E> node = head;
        while (node != null) {
            int idx = hash(node.value) % newCapacity;
            node.next = newTable[idx];
            newTable[idx] = node;
            node = node.nextInsertion;
        }

        table = newTable;
        capacity = newCapacity;
        threshold = (int) (capacity * loadFactor);
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
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            table[i] = null;
        }
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        int idx = hash(o) % capacity;
        Node<E> node = table[idx];
        while (node != null) {
            if (o == null ? node.value == null : o.equals(node.value))
                return true;
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        int idx = hash(e) % capacity;

        Node<E> current = table[idx];
        while (current != null) {
            if (e == null ? current.value == null : e.equals(current.value)) {
                return false;
            }
            current = current.next;
        }

        Node<E> newNode = new Node<>(e);

        newNode.next = table[idx];
        table[idx] = newNode;

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.nextInsertion = newNode;
            newNode.prevInsertion = tail;
            tail = newNode;
        }

        size++;
        if (size > threshold) resize();

        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = hash(o) % capacity;
        Node<E> node = table[idx];
        Node<E> prev = null;

        while (node != null) {
            if (o == null ? node.value == null : o.equals(node.value)) {

                if (prev == null)
                    table[idx] = node.next;
                else
                    prev.next = node.next;

                if (node.prevInsertion == null) {
                    head = node.nextInsertion;
                    if (head != null) head.prevInsertion = null;
                } else {
                    node.prevInsertion.nextInsertion = node.nextInsertion;
                }

                if (node.nextInsertion == null) {
                    tail = node.prevInsertion;
                    if (tail != null) tail.nextInsertion = null;
                } else {
                    node.nextInsertion.prevInsertion = node.prevInsertion;
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<E> node = head;
        boolean first = true;
        while (node != null) {
            if (!first) sb.append(", ");
            sb.append(node.value);
            first = false;
            node = node.nextInsertion;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object el : c)
            if (!contains(el)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E el : c) {
            if (add(el)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object el : c) {
            while (remove(el)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> node = head;
        while (node != null) {
            Node<E> next = node.nextInsertion;
            if (!c.contains(node.value)) {
                remove(node.value);
                changed = true;
            }
            node = next;
        }
        return changed;
    }

    @Override public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}
