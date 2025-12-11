package by.it.group451003.halubionak.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

@SuppressWarnings("unchecked")
public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    private E[] data;
    private int size = 0;

    public MyPriorityQueue() {
        data = (E[]) new Comparable[10];
    }

    private void grow() {
        E[] newData = (E[]) new Comparable[data.length * 2];
        for (int i = 0; i < size; i++) newData[i] = data[i];
        data = newData;
    }

    private void swap(int i, int j) {
        E tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    private void heapifyUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (data[index].compareTo(data[parent]) >= 0) break;
            swap(index, parent);
            index = parent;
        }
    }

    private void heapifyDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size && data[left].compareTo(data[smallest]) < 0) smallest = left;
            if (right < size && data[right].compareTo(data[smallest]) < 0) smallest = right;

            if (smallest == index) break;
            swap(index, smallest);
            index = smallest;
        }
    }

    // ------------------- LEVEL C REQUIRED -------------------

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();
        if (size == data.length) grow();
        data[size] = e;
        heapifyUp(size);
        size++;
        return true;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E remove() {
        E v = poll();
        if (v == null) throw new NoSuchElementException();
        return v;
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E root = data[0];
        data[0] = data[size - 1];
        data[size - 1] = null;
        size--;
        if (size > 0) heapifyDown(0);
        return root;
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return data[0];
    }

    @Override
    public E peek() {
        if (size == 0) return null;
        return data[0];
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
        for (int i = 0; i < size; i++) data[i] = null;
        size = 0;
    }

    public boolean contains(E e) {
        for (int i = 0; i < size; i++) if (data[i].equals(e)) return true;
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return contains((E) o);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(data[i]);
        }
        sb.append("]");
        return sb.toString();
    }


    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            if (add(e)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (data[i].equals(o)) {
                data[i] = data[size - 1];
                data[size - 1] = null;
                size--;
                if (i < size) {
                    heapifyDown(i);
                    heapifyUp(i);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        E[] newData = (E[]) new Comparable[data.length];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (!c.contains(data[i])) {
                newData[newSize++] = data[i];
            } else {
                changed = true;
            }
        }

        data = newData;
        size = newSize;

        for (int i = (size - 2) / 2; i >= 0; i--) {
            heapifyDown(i);
        }

        return changed;
    }


    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        E[] newData = (E[]) new Comparable[data.length];
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(data[i])) {
                newData[newSize++] = data[i];
            } else {
                changed = true;
            }
        }
        data = newData;
        size = newSize;
        for (int i = (size - 2) / 2; i >= 0; i--) {
            heapifyDown(i);
        }
        return changed;
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
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }
}

