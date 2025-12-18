package by.it.group410902.linnik.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class MyHashSet<E> implements Set<E>{
    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<E>[] elements;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashSet(int capacity) {
        elements = new Node[capacity];
        size = 0;
    }

    public MyHashSet() {
        this(16); 
    }

    private int getElementIndex(Object key, int capacity) {
        if (key == null) {
            return 0;
        }
        int hashCode = key.hashCode();
        return (hashCode & 0x7FFFFFFF) % capacity; //хэшкод+убираем отрицательные числа+делим на массив
    }

    private int getElementIndex(Object key) {
        return getElementIndex(key, elements.length);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < elements.length; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        int index = getElementIndex(e);
        Node<E> current = elements[index];

        while (current != null) {
            if (Objects.equals(current.data, e)) {
                return false;
            }
            current = current.next;
        }

        Node<E> newNode = new Node<>(e);
        newNode.next = elements[index];
        elements[index] = newNode;
        size++;

        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = getElementIndex(o);
        Node<E> current = elements[index];
        Node<E> prev = null;

        while (current != null) {
            if (Objects.equals(current.data, o)) {
                if (prev == null) {
                    elements[index] = current.next;
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
    public boolean contains(Object o) {
        int index = getElementIndex(o);
        Node<E> current = elements[index];

        while (current != null) {
            if (Objects.equals(current.data, o)) {
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
        boolean first = true;

        for (Node<E> head : elements) {
            Node<E> current = head;
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(current.data);
                first = false;
                current = current.next;
            }
        }

        sb.append("]");
        return sb.toString();
    }

    @Override
    public Iterator<E> iterator() {
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
