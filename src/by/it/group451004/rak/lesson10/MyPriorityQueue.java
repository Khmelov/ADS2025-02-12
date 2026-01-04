package by.it.group451004.rak.lesson10;

import java.util.*;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {
    private E[] elements;
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 16;

    private void expandArray() {
        E[] temp = (E[]) new Comparable[elements.length * 2];
        for (int i = 0; i < size; i++) {
            temp[i] = elements[i];
        }
        elements = temp;
    }

    public MyPriorityQueue(int initialCapacity){
        elements = (E[]) new Comparable[initialCapacity];
    }

    public MyPriorityQueue() {
        this(DEFAULT_CAPACITY);
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(elements[0]);
        for (int i = 1; i < size; i++){
            sb.append(", ");
            sb.append(elements[i]);
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
        for (int i = 0; i < size; i++)
            elements[i] = null;
        size = 0;
    }

    @Override
    public boolean add(E e) {
        if (size + 1 == elements.length)
            expandArray();

        elements[size] = e;
        int i = size;
        size++;

        while (i > 0) {
            int parent = (i - 1) / 2;
            if (elements[i].compareTo(elements[parent]) >= 0) break;

            E temp = elements[i];
            elements[i] = elements[parent];
            elements[parent] = temp;

            i = parent;
        }

        return true;
    }

    private E removeWithoutCheck(){
        E result = elements[0];
        elements[0] = elements[size - 1];
        elements[size - 1] = null;
        size--;

        siftDown(0);
        return result;
    }

    @Override
    public E remove() {
        if (size == 0)
            throw new NoSuchElementException();
        return removeWithoutCheck();
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++)
            if (elements[i] == o)
                return true;
        return false;
    }

    @Override
    public boolean offer(E e) {
        add(e);
        return true;
    }

    @Override
    public E poll() {
        return size == 0 ? null : removeWithoutCheck();
    }

    @Override
    public E peek() {
        return size == 0 ? null : elements[0];
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) return false;
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean isModified = false;
        for (E element : c) {
            add(element);
            isModified = true;
        }
        return isModified;
    }

    @Override
    public E element() {
        if (size == 0)
            throw new NoSuchElementException();
        return elements[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null || c.isEmpty()) {
            boolean changed = size > 0;
            clear();
            return changed;
        }

        int oldSize = size;
        int j = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                elements[j++] = elements[i];
            }
        }
        for (int k = j; k < size; k++) {
            elements[k] = null;
        }
        size = j;

        heapify();

        return size != oldSize;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null || c.isEmpty()) return false;

        int oldSize = size;
        int j = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                elements[j++] = elements[i];
            }
        }
        for (int k = j; k < size; k++) {
            elements[k] = null;
        }
        size = j;

        heapify();

        return size != oldSize;
    }

    private void heapify() {
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    private void siftDown(int i) {
        while (true) {
            int leftChild = 2 * i + 1;
            int rightChild = 2 * i + 2;
            int smallest = i;

            if (leftChild < size && elements[leftChild].compareTo(elements[smallest]) < 0)
                smallest = leftChild;
            if (rightChild < size && elements[rightChild].compareTo(elements[smallest]) < 0)
                smallest = rightChild;

            if (smallest == i)
                break;

            E temp = elements[i];
            elements[i] = elements[smallest];
            elements[smallest] = temp;

            i = smallest;
        }
    }

    //ЗАГЛУШКИ

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

    @Override
    public boolean remove(Object o) {
        return false;
    }
}
