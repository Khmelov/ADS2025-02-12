package by.it.group451003.burshtyn.lesson11;


import java.util.Set;

public class MyHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<E>[] table;
    private int size;

    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
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
        size = 0;
    }

    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not supported");
        }

        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }

        int index = getIndex(element);
        Node<E> current = table[index];

        while (current != null) {
            if (element.equals(current.data)) {
                return false;
            }
            current = current.next;
        }

        table[index] = new Node<>(element, table[index]);
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
            if (element.equals(current.data)) {

                if (prev == null) {
                    table[index] = current.next;
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
    public boolean contains(Object element) {
        if (element == null) {
            return false;
        }

        int index = getIndex(element);
        Node<E> current = table[index];

        while (current != null) {
            if (element.equals(current.data)) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        int count = 0;

        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            while (current != null) {
                sb.append(current.data);
                if (++count < size) {
                    sb.append(", ");
                }
                current = current.next;
            }
        }

        sb.append("]");
        return sb.toString();
    }


    private int getIndex(Object element) {
        return Math.abs(element.hashCode()) % table.length;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        table = (Node<E>[]) new Node[oldTable.length * 2];
        size = 0;

        for (int i = 0; i < oldTable.length; i++) {
            Node<E> current = oldTable[i];
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

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }
}
