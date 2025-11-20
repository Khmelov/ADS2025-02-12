package by.it.group451004.zybko.lesson11;

import java.util.Arrays;
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
        Node<E> after;
        Node<E> before;

        Node(E data) {
            this.data = data;
        }
    }

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
        Arrays.fill(table, null);
        head = tail = null;
        size = 0;
    }

    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }

        int index = getIndex(element);
        Node<E> current = table[index];

        while (current != null) {
            if (current.data.equals(element)) {
                return false;
            }
            current = current.next;
        }

        Node<E> newNode = new Node<>(element);

        newNode.next = table[index];
        table[index] = newNode;

        linkNodeLast(newNode);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object element) {
        if (element == null) {
            return false;
        }

        int index = getIndex(element);
        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if (current.data.equals(element)) {

                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }

                unlinkNode(current);
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }

        return false;
    }

    @Override
    public boolean contains(Object element) {
        if (element == null) {
            return false;
        }

        int index = getIndex(element);
        Node<E> current = table[index];

        while (current != null) {
            if (current.data.equals(element)) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        boolean modified = false;
        for (Object element : c) {
            if (remove(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
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


    private int getIndex(Object element) {
        return Math.abs(element.hashCode()) % table.length;
    }

    private void resize() {
        Node<E>[] oldTable = table;
        table = new Node[oldTable.length * 2];

        Node<E> oldHead = head;
        Node<E> oldTail = tail;
        head = tail = null;
        size = 0;

        Node<E> current = oldHead;
        while (current != null) {
            add(current.data);
            current = current.after;
        }
    }

    private void linkNodeLast(Node<E> node) {
        if (tail == null) {
            head = tail = node;
        } else {
            tail.after = node;
            node.before = tail;
            tail = node;
        }
    }

    private void unlinkNode(Node<E> node) {
        Node<E> before = node.before;
        Node<E> after = node.after;

        if (before == null) {
            head = after;
        } else {
            before.after = after;
        }

        if (after == null) {
            tail = before;
        } else {
            after.before = before;
        }

        node.before = node.after = null;
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
