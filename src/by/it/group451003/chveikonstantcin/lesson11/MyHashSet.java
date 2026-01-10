package by.it.group451003.chveikonstantcin.lesson11;

import java.util.*;

public class MyHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
        }
    }

    private Node<E>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int count = 0;
        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            while (current != null) {
                sb.append(current.data);
                if (count < size - 1) {
                    sb.append(", ");
                }
                count++;
                current = current.next;
            }
        }
        sb.append("]");
        return sb.toString();
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
            throw new NullPointerException();
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
        size++;

        if (size > table.length * LOAD_FACTOR) {
            resize();
        }

        return true;
    }

    @Override
    public boolean remove(Object element) {
        if (element == null) {
            throw new NullPointerException();
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
            throw new NullPointerException();
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

    private int getIndex(Object element) {
        return Math.abs(element.hashCode()) % table.length;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        size = 0;

        for (Node<E> node : oldTable) {
            Node<E> current = node;
            while (current != null) {
                add(current.data);
                current = current.next;
            }
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int tableIndex = 0;
            private Node<E> current = null;
            private int count = 0;

            @Override
            public boolean hasNext() {
                return count < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                while (current == null) {
                    current = table[tableIndex++];
                }

                E data = current.data;
                current = current.next;
                count++;
                return data;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int index = 0;
        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            while (current != null) {
                array[index++] = current.data;
                current = current.next;
            }
        }
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int index = 0;
        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            while (current != null) {
                a[index++] = (T) current.data;
                current = current.next;
            }
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
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
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
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
}