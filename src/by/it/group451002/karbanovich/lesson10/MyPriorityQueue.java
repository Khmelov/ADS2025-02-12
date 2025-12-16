package by.it.group451002.karbanovich.lesson10;

import java.util.*;
import java.util.function.Predicate;

public class MyPriorityQueue<E> implements Queue<E> {
    private Object[] elements = new Object[8];
    private int size = 0;

    private void grow()
    {
        final int oldCapacity = elements.length;
        final int newCapacity = oldCapacity * 2;
        elements = Arrays.copyOf(elements, newCapacity);
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOfRange(elements, 0, size));
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++)
            if (Objects.equals(elements[i], o)) return true;
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

    int compare(int i, int j)
    {
        return ((Comparable<E>) elements[i]).compareTo((E) elements[j]);
    }

    void swap(int i, int j) {
        Object tmp = elements[i];
        elements[i] = elements[j];
        elements[j] = tmp;
    }

    void siftUp(int i) {
        while (compare(i, (i - 1) / 2) < 0){
            swap(i, (i - 1) / 2);
            i = (i - 1) / 2;
        }
    }

    void siftDown(int i) {
        while (2 * i + 1 < size){
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int j = left;
            if (right < size && compare(right, left) < 0)
                j = right;
            if (compare(i, j) <= 0)
                break;
            swap(i, j);
            i = j;
        }
    }

    @Override
    public boolean add(E e) {
        if (e == null)
            throw new NullPointerException();
        if (size == elements.length)
            grow();
        elements[size++] = e;
        siftUp(size - 1);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        int oldSize = size;
        for (E e : c)
            add(e);
        return oldSize != size;
    }

    public boolean fastRemove(Collection<?> c, Predicate<Object> filter)
    {
        final Object[] elems_2 = Arrays.copyOf(elements, elements.length);

        int ind = 0;
        for (int i = 0; i < size; i++)
        {
            if (filter.test(elements[i]))
                elements[ind++] = elems_2[i];
        }
        int oldSize = size;
        size = ind;
        for (int i = ind; i < elements.length; i++)
            elements[i] = null;

        for (int i = size / 2 - 1; i >= 0; i--)
            siftDown(i);

        return size != oldSize;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return fastRemove(c, e -> !c.contains(e));
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return fastRemove(c, c::contains);
    }

    @Override
    public void clear() {
        elements = new Object[8];
        size = 0;
    }

    @Override
    public boolean offer(E e) {
        add(e);
        return true;
    }

    @Override
    public E remove() {
        if (size == 0) return null;
        E result = (E) elements[0];
        swap(0, size - 1);
        elements[--size] = null;
        siftDown(0);
        return result;
    }

    @Override
    public E poll() {
        return remove();
    }

    @Override
    public E element() {
        if (size == 0) return null;
        return (E) elements[0];
    }

    @Override
    public E peek() {
        if (size == 0) return null;
        return (E) elements[0];
    }
}