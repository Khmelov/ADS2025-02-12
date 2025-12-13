package by.it.group451001.russu.lesson11;

import java.util.Collection;
import java.util.Set;
import java.util.Iterator;

public class MyHashSet<E> implements Set<E>{
    private Object[] elements;
    private int size;

    public MyHashSet() {
        elements = new Object[10];
        size = 0;
    }

    public int size() {
        return size;
    }

    public void clear() {
        elements = new Object[10];
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean add(E element) {
        if (contains(element)) {
            return false;
        }

        if (size >= elements.length) {
            Object[] newElements = new Object[elements.length * 2];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }

        elements[size] = element;
        size++;
        return true;
    }

    public boolean remove(Object element) {
        for (int i = 0; i < size; i++) {
            if (elements[i] != null && elements[i].equals(element)) {
                for (int j = i; j < size - 1; j++) {
                    elements[j] = elements[j + 1];
                }
                elements[size - 1] = null;
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    public boolean contains(Object element) {
        for (int i = 0; i < size; i++) {
            if (elements[i] != null && elements[i].equals(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    public String toString() {
        if (size == 0) {
            return "[]";
        }

        String result = "[";
        for (int i = 0; i < size; i++) {
            result += elements[i];
            if (i < size - 1) {
                result += ", ";
            }
        }
        result += "]";
        return result;
    }
}