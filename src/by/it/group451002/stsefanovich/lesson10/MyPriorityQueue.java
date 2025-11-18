package by.it.group451002.stsefanovich.lesson10;

import java.util.Queue;
import java.util.Collection;
import java.util.NoSuchElementException;

public class MyPriorityQueue<E extends Comparable<? super E>> implements Queue<E> {
    private E[] heap;
    private int size;
    private static final int DEFAULT_CAP = 16;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(int capacity) {
        if (capacity < 1) capacity = DEFAULT_CAP;
        heap = (E[]) new Comparable[capacity];//
        size = 0;
    }

    public MyPriorityQueue() {
        this(DEFAULT_CAP);
    }

    private int cap() { return heap.length; }

    @SuppressWarnings("unchecked")
    private void grow() {
        int n = cap() << 1;
        E[] nh = (E[]) new Comparable[n];
        for (int i = 0; i < size; i++) nh[i] = heap[i];
        heap = nh;
    }

    private void swap(int i, int j) {
        E t = heap[i]; heap[i] = heap[j]; heap[j] = t;
    }

    private void siftUp(int idx) {
        E val = heap[idx];
        while (idx > 0) {
            int p = (idx - 1) >>> 1;
            if (heap[p].compareTo(val) <= 0) break;
            heap[idx] = heap[p];
            idx = p;
        }
        heap[idx] = val;
    }

    private void siftDown(int idx) {
        E val = heap[idx];
        int half = size >>> 1;
        while (idx < half) {
            int l = (idx << 1) + 1;
            int r = l + 1;
            int smallest = l;
            if (r < size && heap[r].compareTo(heap[l]) < 0) smallest = r;
            if (heap[smallest].compareTo(val) >= 0) break;
            heap[idx] = heap[smallest];
            idx = smallest;
        }
        heap[idx] = val;
    }

    private E removeAt(int idx) {
        E removed = heap[idx];
        int last = --size;
        if (idx == last) {
            heap[idx] = null;
            return removed;
        }
        E moved = heap[last];
        heap[last] = null;
        heap[idx] = moved;
        int parent = (idx - 1) >>> 1;
        if (idx > 0 && heap[idx].compareTo(heap[parent]) < 0) {
            siftUp(idx);
        } else {
            siftDown(idx);
        }
        return removed;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            sb.append(String.valueOf(heap[i]));
            if (i != size - 1) sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) heap[i] = null;
        size = 0;
    }

    @Override
    public boolean add(E element) {
        if (element == null) throw new NullPointerException();
        if (size == cap()) grow();
        heap[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public boolean offer(E e) { return add(e); }

    @Override
    public E poll() {
        if (size == 0) return null;
        E res = heap[0];
        removeAt(0);
        return res;
    }

    @Override
    public E peek() { return size == 0 ? null : heap[0]; }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return heap[0];
    }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public E remove() {
        E r = poll();
        if (r == null) throw new NoSuchElementException();
        return r;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) if (o.equals(heap[i])) return true;
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object e : c) if (!contains(e)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        for (E e : c) { add(e); modified = true; }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        int j = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                heap[j++] = heap[i];
            }
        }
        if (j == size) return false;
        for (int k = j; k < size; k++) heap[k] = null;
        size = j;
        for (int k = (size >>> 1) - 1; k >= 0; k--) siftDown(k);
        return true;
    }



    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        int j = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                heap[j++] = heap[i];
            }
        }
        if (j == size) return false;
        for (int k = j; k < size; k++) heap[k] = null;
        size = j;
        for (int k = (size >>> 1) - 1; k >= 0; k--) siftDown(k);
        return true;
    }


    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean equals(Object o) { return super.equals(o); }
    @Override public int hashCode() { return super.hashCode(); }
}