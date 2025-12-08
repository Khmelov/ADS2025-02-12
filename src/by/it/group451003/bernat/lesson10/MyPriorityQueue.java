package by.it.group451003.bernat.lesson10;

import java.util.*;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {
    private E[] array;
    private int size;
    private static final int INITIAL_CAPACITY = 10;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        array = (E[]) new Comparable[INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(array[i]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        E[] newArray = (E[]) new Comparable[Math.max(array.length * 2, INITIAL_CAPACITY)];
        System.arraycopy(array, 0, newArray, 0, size);
        array = newArray;
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();
        if (size == array.length) resize();
        array[size] = e;
        siftUp(size);
        size++;
        return true;
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (array[index].compareTo(array[parent]) >= 0) break;
            swap(index, parent);
            index = parent;
        }
    }

    private void siftDown(int index) {
        while (index * 2 + 1 < size) {
            int left = index * 2 + 1;
            int right = index * 2 + 2;
            int smallest = left;
            if (right < size && array[right].compareTo(array[left]) < 0) {
                smallest = right;
            }
            if (array[index].compareTo(array[smallest]) <= 0) break;
            swap(index, smallest);
            index = smallest;
        }
    }

    private void swap(int i, int j) {
        E temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    @Override
    public E remove() {
        if (size == 0) throw new NoSuchElementException();
        E result = array[0];
        array[0] = array[size - 1];
        array[size - 1] = null;
        size--;
        if (size > 0) siftDown(0);
        return result;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, array[i])) return true;
        }
        return false;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E result = array[0];
        array[0] = array[size - 1];
        array[size - 1] = null;
        size--;
        if (size > 0) siftDown(0);
        return result;
    }

    @Override
    public E peek() {
        if (size == 0) return null;
        return array[0];
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return array[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
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
        @SuppressWarnings("unchecked")
        E[] newArray = (E[]) new Comparable[Math.max(size, INITIAL_CAPACITY)];
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(array[i])) {
                newArray[newSize++] = array[i];
            } else {
                modified = true;
            }
        }
        array = newArray;
        size = newSize;
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        @SuppressWarnings("unchecked")
        E[] newArray = (E[]) new Comparable[Math.max(size, INITIAL_CAPACITY)];
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(array[i])) {
                newArray[newSize++] = array[i];
            } else {
                modified = true;
            }
        }
        array = newArray;
        size = newSize;
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
        return modified;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, array[i])) {
                array[i] = array[size - 1];
                array[size - 1] = null;
                size--;
                if (size > 0) siftDown(i);
                return true;
            }
        }
        return false;
    }

    // Необязательные методы интерфейса Queue
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}