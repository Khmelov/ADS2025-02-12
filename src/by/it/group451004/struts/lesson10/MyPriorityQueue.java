package by.it.group451004.struts.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {
    E[] storage;
    int size;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        storage = (E[]) new Comparable[0];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("[");
        for (int i = 0; i < size; i++) {
            if (storage[i] != null)
                sb.append(storage[i]).append(", ");
        }
        if (sb.length() > 2)
            sb.replace(sb.length() - 2, sb.length(), "]");
        else
            sb.append("]");

        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @SuppressWarnings("unchecked")
    public void clear() {
        storage = (E[]) new Comparable[0];
        size = 0;
    }

    @Override
    public boolean add(E e) {
        return this.offer(e);
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean offer(E e) {
        if (storage.length <= size + 1)
            resize(size + 1);

        storage[size++] = e;
        siftUp(size - 1);

        return true;
    }

    @Override
    public E remove() {
        return this.poll();
    }

    @Override
    public E poll() {
        E elem = storage[0];
        storage[0] = storage[--size];
        storage[size] = null;

        siftDown(0);

        return elem;
    }

    @Override
    public E element() {
        return storage[0];
    }

    @Override
    public E peek() {
        return storage[0];
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++)
            if (o.equals(storage[i]))
                return true;

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

    @SuppressWarnings("unchecked")
    private void resize(int length) {
        E[] newArr = (E[]) new Comparable[length];
        System.arraycopy(storage, 0, newArr, 0, size);
        storage = newArr;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (!this.contains(o))
                return false;

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (size + c.size() > storage.length)
            resize(size + c.size());

        for (E e : c) {
            storage[size++] = e;
            siftUp(size - 1);
        }

        heapify();
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        final int saved = size;
        for (int i = 0, j = 0; j < size; i++) {
            if (c.contains(storage[i])) {
                size--;
                storage[i] = null;
            }
            else {
                storage[j] = storage[i];
                j++;
            }
        }

        heapify();
        return saved != size;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        final int prevSize = size;
        for (int i = 0, j = 0; j < size; i++) {
            if (!c.contains(storage[i])) {
                size--;
                storage[i] = null;
            }
            else {
                storage[j] = storage[i];
                j++;
            }
        }

        heapify();
        return prevSize != size;
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;

            if (storage[parentIndex].compareTo(storage[index]) < 0)
                break;

            E temp = storage[index];
            storage[index] = storage[parentIndex];
            storage[parentIndex] = temp;
            index = parentIndex;
        }
    }

    private void siftDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int min = index;

            if (left < size && storage[left].compareTo(storage[min]) < 0)
                min = left;

            if (right < size && storage[right].compareTo(storage[min]) < 0)
                min = right;

            if (min == index)
                break;

            E temp = storage[index];
            storage[index] = storage[min];
            storage[min] = temp;
            index = min;
        }
    }

    private void heapify() {
        for (int i = (size / 2) - 1; i >= 0; i--)
            siftDown(i);
    }
}
