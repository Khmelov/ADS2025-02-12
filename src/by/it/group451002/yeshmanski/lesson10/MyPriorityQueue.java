package by.it.group451002.yeshmanski.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    private static final int DEF_CAPACITY = 10;
    private Object[] heap;
    private int size;

    public MyPriorityQueue() {
        heap = new Object[DEF_CAPACITY];
        size = 0;
    }

    public MyPriorityQueue(int capacity) {
        heap = new Object[Math.max(capacity, DEF_CAPACITY)];
        size = 0;
    }

    private void ensureCapacity() {
        if (size == heap.length) {
            Object[] newHeap = new Object[heap.length * 2];
            System.arraycopy(heap, 0, newHeap, 0, size);
            heap = newHeap;
        }
    }

    private void swap(int i, int j) {
        Object tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    @SuppressWarnings("unchecked")
    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (((E) heap[index]).compareTo((E) heap[parent]) >= 0) break;
            swap(index, parent);
            index = parent;
        }
    }

    @SuppressWarnings("unchecked")
    private void siftDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size && ((E) heap[left]).compareTo((E) heap[smallest]) < 0)
                smallest = left;
            if (right < size && ((E) heap[right]).compareTo((E) heap[smallest]) < 0)
                smallest = right;

            if (smallest == index) break;
            swap(index, smallest);
            index = smallest;
        }
    }

    private void heapify() {
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    private void rebuildHeap() {
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i != size - 1) sb.append(", ");
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
        for (int i = 0; i < size; i++) heap[i] = null;
        size = 0;
    }

    @Override
    public boolean add(E e) {
        return offer(e);
    }

    @Override
    public boolean offer(E e) {
        if (e == null) throw new NullPointerException();
        ensureCapacity();
        heap[size] = e;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E peek() {
        if (size == 0) return null;
        return (E) heap[0];
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return (E) heap[0];
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E result = (E) heap[0];
        heap[0] = heap[--size];
        heap[size] = null;
        siftDown(0);
        return result;
    }

    @Override
    public E remove() {
        E e = poll();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && heap[i] == null) || (o != null && o.equals(heap[i]))) return true;
        }
        return false;
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
        boolean modified = false;
        for (E e : c) {
            if (offer(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        Object[] newHeap = new Object[heap.length];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                newHeap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            heap = newHeap;
            size = newSize;
            rebuildHeap();
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Object[] newHeap = new Object[heap.length];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                newHeap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            Object[] tmp = new Object[heap.length];
            int tmpSize = 0;
            for (int i = 0; i < newSize; i++) {
                tmp[tmpSize++] = newHeap[i];
            }
            heap = tmp;
            size = newSize;
            rebuildHeap();
        }

        return modified;
    }

    private boolean removeElement(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && heap[i] == null) || (o != null && o.equals(heap[i]))) {
                heap[i] = heap[--size];
                heap[size] = null;
                siftDown(i);
                return true;
            }
        }
        return false;
    }

    /////////////////////////////////////////////////////////////////////////
    //////                    Остальные методы                        ///////
    /////////////////////////////////////////////////////////////////////////

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean remove(Object o) { return removeElement(o); }
}