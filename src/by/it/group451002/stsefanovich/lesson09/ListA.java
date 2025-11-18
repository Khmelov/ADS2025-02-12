package by.it.group451002.stsefanovich.lesson09;

import java.util.*;

public class ListA<E> implements List<E> {
    private E[] elements;
    private int size;

    @SuppressWarnings("unchecked")
    public ListA() {
        elements = (E[]) new Object[10];
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }//

    @Override
    public boolean add(E e) {
        if (size == elements.length) {
            elements = Arrays.copyOf(elements, elements.length * 2);
        }
        elements[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        E removed = elements[index];
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[size - 1] = null;
        size--;
        return removed;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {}

    @Override
    public boolean remove(Object o) { return false; }

    @Override
    public E set(int index, E element) { return null; }

    @Override
    public boolean isEmpty() { return false; }

    @Override
    public void clear() {}

    @Override
    public int indexOf(Object o) { return 0; }

    @Override
    public E get(int index) { return null; }

    @Override
    public boolean contains(Object o) { return false; }

    @Override
    public int lastIndexOf(Object o) { return 0; }

    @Override
    public boolean containsAll(Collection<?> c) { return false; }

    @Override
    public boolean addAll(Collection<? extends E> c) { return false; }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) { return false; }

    @Override
    public boolean removeAll(Collection<?> c) { return false; }

    @Override
    public boolean retainAll(Collection<?> c) { return false; }

    @Override
    public List<E> subList(int fromIndex, int toIndex) { return null; }

    @Override
    public ListIterator<E> listIterator(int index) { return null; }

    @Override
    public ListIterator<E> listIterator() { return null; }

    @Override
    public <T> T[] toArray(T[] a) { return null; }

    @Override
    public Object[] toArray() { return new Object[0]; }

    @Override
    public Iterator<E> iterator() { return null; }
}