package by.it.group451001.ivanov.lesson10;

import java.util.*;

public class MyPriorityQueue<T> implements Queue<T> {

    private static final int DEFAULT_CAPACITY = 10;

    private Object[] queue;
    private int size;

    public MyPriorityQueue() {
        queue = new Object[DEFAULT_CAPACITY];
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder().append("[");
        for (int i = 0; i < size - 1; i++) {
            sb.append(queue[i]).append(", ");
        }

        return sb.append(queue[size - 1]).append("]").toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            queue[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(T t) {
        return offer(t);
    }

    @Override
    public T remove() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return poll();
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public boolean offer(T t) {

        if (t == null) {
            throw new NullPointerException();
        }

        if (size == queue.length) {
            grow();
        }

        this.siftUp(size, t);
        size++;

        return true;
    }

    @Override
    public T poll() {
        T element = (T) queue[0];
        if (element != null) {
            size--;
            T last = (T) queue[size];
            queue[size] = null;
            queue[0] = last;
            if (size > 0) {
                siftDown(0);
            }
        }
        return element;
    }

    @Override
    public T peek() {
        return (T) queue[0];
    }

    @Override
    public T element() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return (T) queue[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean modified = false;
        for (T e : c)
            if (add(e))
                modified = true;
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Object[] newData = new Object[size];
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(queue[i])) {
                newData[newSize] = queue[i];
                newSize++;
            }
        }
        boolean modified = newSize != size;
        queue = newData;
        size = newSize;
        for (int i = size / 2; i >= 0; i--)
            siftDown(i);
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Object[] newData = new Object[size];
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(queue[i])) {
                newData[newSize] = queue[i];
                newSize++;
            }
        }

        boolean modified = newSize != size;
        queue = newData;
        size = newSize;
        for (int i = size / 2; i >= 0; i--)
            siftDown(i);
        return modified;
    }

    private void grow() {
        queue = Arrays.copyOf(queue, queue.length * 3 / 2);
    }

    private int indexOf(Object o) {
        if (o != null) {
            for (int i = 0; i < size; i++) {
                if (o.equals(queue[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void siftUp(int index, T item) {

        Comparable<? super T> comparable = (Comparable<? super T>) item;
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            Object parent = queue[parentIndex];
            if (comparable.compareTo((T) parent) >= 0) {
                break;
            }

            queue[index] = parent;
            index = parentIndex;
        }

        queue[index] = item;
    }

    private void siftDown(int index) {

        int left = 2 * index + 1;
        int right = left + 1;

        int largest = index;

        if (left < size && ((Comparable<? super T>) queue[left]).compareTo((T) queue[index]) < 0) {
            largest = left;
        }

        if (right < size && ((Comparable<? super T>) queue[right]).compareTo((T) queue[largest]) < 0) {
            largest = right;
        }

        if (largest != index) {
            swap(index, largest);
            siftDown(largest);
        }


    }

    private void swap(int index1, int index2) {
        Object temp = queue[index1];
        queue[index1] = queue[index2];
        queue[index2] = temp;
    }

    //————————————————————

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index == -1) {
            return false;
        }
        size--;
        T last = (T) queue[size];
        queue[size] = null;
        queue[index] = last;
        if (size > 0) {
            siftDown(index);
        }

        return true;
    }
}
