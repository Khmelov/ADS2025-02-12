package by.it.group451002.mishchenko.lesson10;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Queue;

@SuppressWarnings("unchecked")
public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {
    private E[] data;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    public MyPriorityQueue() {
        data = (E[]) new Comparable[DEFAULT_CAPACITY];
        size = 0;
    }

    private void grow() {
        E[] newData = (E[]) new Comparable[data.length * 2];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }

    private void swap(int i, int j) {
        E tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    private void siftUp(int idx) {
        while (idx > 0) {
            int parent = (idx - 1) / 2;
            if (data[idx].compareTo(data[parent]) >= 0) break;
            swap(idx, parent);
            idx = parent;
        }
    }

    private void siftDown(int idx) {
        while (true) {
            int left = 2 * idx + 1;
            int right = 2 * idx + 2;
            int smallest = idx;
            if (left < size && data[left].compareTo(data[smallest]) < 0) smallest = left;
            if (right < size && data[right].compareTo(data[smallest]) < 0) smallest = right;
            if (smallest == idx) break;
            swap(idx, smallest);
            idx = smallest;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) sb.append(", ");
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
        for (int i = 0; i < size; i++) data[i] = null;
        size = 0;
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public boolean offer(E element) {
        if (size == data.length) grow();
        data[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E remove() {
        E res = poll();
        if (res == null) throw new NoSuchElementException();
        return res;
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E res = data[0];
        data[0] = data[size - 1];
        data[size - 1] = null;
        size--;
        if (size > 0) siftDown(0);
        return res;
    }

    @Override
    public E peek() {
        return size == 0 ? null : data[0];
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return data[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && data[i] == null) || (o != null && o.equals(data[i]))) return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) if (!contains(e)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            offer(e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int oldSize = size;
        // создаём временный массив для новых элементов
        E[] newData = (E[]) new Comparable[data.length];
        int newSize = 0;
        for (int i = 0; i < oldSize; i++) {
            if (c.contains(data[i])) {
                newData[newSize++] = data[i];
            }
        }
        size = newSize;
        data = newData;
        // перестроить кучу
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
        return size != oldSize;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int oldSize = size;
        E[] newData = (E[]) new Comparable[data.length];
        int newSize = 0;
        for (int i = 0; i < oldSize; i++) {
            if (!c.contains(data[i])) {
                newData[newSize++] = data[i];
            }
        }
        size = newSize;
        data = newData;
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
        return size != oldSize;
    }


    private void removeElement(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && data[i] == null) || (o != null && o.equals(data[i]))) {
                data[i] = data[size - 1];
                data[size - 1] = null;
                size--;
                if (i < size) {
                    siftDown(i);
                    siftUp(i);
                }
                return;
            }
        }
    }

    // ===== Заглушки для остальных методов Collection =====
    @Override public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
}
