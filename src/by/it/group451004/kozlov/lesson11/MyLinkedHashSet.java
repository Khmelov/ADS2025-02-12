package by.it.group451004.kozlov.lesson11;

import java.util.Collection;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<E>[] table;
    private Node<E> head;
    private Node<E> tail;
    private int size;

    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        Node(E data) {
            this.data = data;
        }
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
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
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;

        int index = getIndex(o);
        Node<E> current = table[index];

        while (current != null) {
            if (current.data.equals(o)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        if (contains(e)) {
            return false;
        }

        if ((double) size / table.length >= LOAD_FACTOR) {
            resize();
        }

        int index = getIndex(e);
        Node<E> newNode = new Node<>(e);

        newNode.next = table[index];
        table[index] = newNode;

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode;
        }

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;

        int index = getIndex(o);
        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if (current.data.equals(o)) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }

                if (current.before != null) {
                    current.before.after = current.after;
                } else {
                    head = current.after;
                }

                if (current.after != null) {
                    current.after.before = current.before;
                } else {
                    tail = current.before;
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
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

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

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object element : c) {
            if (remove(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> current = head;
        while (current != null) {
            if (!c.contains(current.data)) {
                Node<E> next = current.after;
                remove(current.data);
                current = next;
                modified = true;
            } else {
                current = current.after;
            }
        }
        return modified;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.after != null) {
                sb.append(", ");
            }
            current = current.after;
        }
        sb.append("]");
        return sb.toString();
    }

    private int getIndex(Object o) {
        return Math.abs(o.hashCode()) % table.length;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        size = 0;
        head = null;
        tail = null;

        for (Node<E> head : oldTable) {
            Node<E> current = head;
            while (current != null) {
                add(current.data);
                current = current.next;
            }
        }
    }

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