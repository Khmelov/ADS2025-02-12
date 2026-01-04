package by.it.group451003.kishkov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
        }
    }

    private Node<E>[] table;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    public MyHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    public MyHashSet(int initialCapacity) {
        if (initialCapacity <= 0)
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        table = (Node<E>[]) new Node[initialCapacity];
        size = 0;
    }

    //////               Обязательные к реализации методы             ///////

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++)
            table[i] = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        if (element == null)
            throw new NullPointerException();
        if (contains(element))
            return false;
        if (size + 1 > table.length * LOAD_FACTOR)
            resize();
        int index = getIndex(element);
        Node<E> newNode = new Node<>(element);
        newNode.next = table[index];
        table[index] = newNode;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object element) {
        if (element == null)
            return false;
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
        if (element == null)
            return false;
        int index = getIndex(element);
        Node<E> current = table[index];
        while (current != null) {
            if (element.equals(current.data))
                return true;
            current = current.next;
        }
        return false;
    }

    @Override
    public String toString() {
        if (size == 0)
            return "[]";
        StringBuilder sb = new StringBuilder("[");
        boolean firstElement = true;
        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            while (current != null) {
                if (!firstElement)
                    sb.append(", ");
                sb.append(current.data);
                firstElement = false;
                current = current.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    //////           Внутренние вспомогательные методы                ///////

    private int getIndex(Object element) {
        int hashCode = element.hashCode();
        return (hashCode & 0x7FFFFFFF) % table.length;
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

    //////              Методы-затычки (UnsupportedOperation)         ///////

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