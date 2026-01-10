package by.it.group451003.sirotkin.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private E[] elements;
    private int size;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        elements = (E[]) new Object[11];
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        return ((Comparable<? super E>) a).compareTo(b);
    }

    private void grow() {
        @SuppressWarnings("unchecked")
        E[] newElements = (E[]) new Object[elements.length * 2 + 1];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    private void siftUp(int index) {
        E key = elements[index];
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            E parent = elements[parentIndex];
            if (compare(key, parent) >= 0) {
                break;
            }
            elements[index] = parent;
            index = parentIndex;
        }
        elements[index] = key;
    }

    private void siftDown(int index) {
        E key = elements[index];
        int half = size / 2;
        while (index < half) {
            int childIndex = 2 * index + 1;
            E child = elements[childIndex];
            int rightIndex = childIndex + 1;

            if (rightIndex < size && compare(child, elements[rightIndex]) > 0) {
                childIndex = rightIndex;
                child = elements[childIndex];
            }

            if (compare(key, child) <= 0) {
                break;
            }
            elements[index] = child;
            index = childIndex;
        }
        elements[index] = key;
    }

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--)
            siftDown(i);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
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
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E e) {
        return offer(e);
    }

    @Override
    public E remove() {
        E x = poll();
        if (x != null)
            return x;
        else
            throw new NoSuchElementException();
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) return true;
        }
        return false;
    }

    @Override
    public boolean offer(E e) {
        if (e == null) throw new NullPointerException();
        if (size >= elements.length) {
            grow();
        }
        size++;
        int i = size - 1;
        elements[i] = e;
        siftUp(i);
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        int s = --size;
        E result = elements[0];
        E x = elements[s];
        elements[s] = null;
        if (s != 0) {
            elements[0] = x;
            siftDown(0);
        }
        return result;
    }

    @Override
    public E peek() {
        return (size == 0) ? null : elements[0];
    }

    @Override
    public E element() {
        E x = peek();
        if (x != null) return x;
        else throw new NoSuchElementException();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        if (c == this) throw new IllegalArgumentException();
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;

        int i = 0;
        int j = 0;

        for (; i < size; i++) {
            if (!c.contains(elements[i])) {
                elements[j++] = elements[i];
            } else {
                modified = true;
            }
        }

        for (int k = j; k < size; k++) {
            elements[k] = null;
        }
        size = j;

        if (modified) {
            heapify();
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;

        int i = 0;
        int j = 0;

        for (; i < size; i++) {
            if (c.contains(elements[i])) {
                elements[j++] = elements[i];
            } else {
                modified = true;
            }
        }

        for (int k = j; k < size; k++) {
            elements[k] = null;
        }
        size = j;

        if (modified) {
            heapify();
        }

        return modified;
    }

    @Override
    public boolean remove(Object o) {
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
}