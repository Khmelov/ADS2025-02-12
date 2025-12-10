package by.it.group410902.menshikov.lesson10;

import java.util.Collection;

public class MyPriorityQueue<E> implements java.util.Queue<E> {

    private E[] heap;
    private int size;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = (E[]) new Object[16];
        size = 0;
    }

    private int left(int i)   { return 2*i + 1; }
    private int right(int i)  { return 2*i + 2; }
    private int parent(int i) { return (i - 1)/2; }

    private void swap(int i, int j) {
        E tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        return ((Comparable<? super E>)a).compareTo(b);
    }

    private void siftUp(int i) {
        while (i > 0 && compare(heap[parent(i)], heap[i]) > 0) {
            swap(parent(i), i);
            i = parent(i);
        }
    }

    private void siftDown(int i) {
        int smallest = i;
        int l = left(i), r = right(i);
        if (l < size && compare(heap[l], heap[smallest]) < 0) smallest = l;
        if (r < size && compare(heap[r], heap[smallest]) < 0) smallest = r;
        if (smallest != i) {
            swap(i, smallest);
            siftDown(smallest);
        }
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == heap.length) {
            E[] newHeap = (E[]) new Object[heap.length * 2];
            System.arraycopy(heap, 0, newHeap, 0, size);
            heap = newHeap;
        }
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
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
        for (int i = 0; i < size; i++) heap[i] = null;
        size = 0;
    }

    @Override
    public boolean add(E element) {
        ensureCapacity();
        heap[size] = element;
        siftUp(size++);
        return true;
    }

    @Override
    public E remove() {
        if (size == 0) throw new java.util.NoSuchElementException();
        E root = heap[0];
        heap[0] = heap[--size];
        heap[size] = null;
        if (size > 0) siftDown(0);
        return root;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null ? heap[i] == null : o.equals(heap[i]))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E element) {
        return add(element);
    }

    @Override
    public E poll() {
        return size == 0 ? null : remove();
    }

    @Override
    public E peek() {
        return size == 0 ? null : heap[0];
    }

    @Override
    public E element() {
        if (size == 0) throw new java.util.NoSuchElementException();
        return heap[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? heap[i] == null : o.equals(heap[i])) {
                heap[i] = heap[--size];
                heap[size] = null;
                siftDown(i);
                siftUp(i);
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        E[] newHeap = (E[]) new Object[heap.length];
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                newHeap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }
        if (!modified) {
            return false;
        }
        heap = newHeap;
        size = newSize;
        for (int i = parent(size - 1); i >= 0; i--) {
            siftDown(i);
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        @SuppressWarnings("unchecked")
        E[] newHeap = (E[]) new Object[heap.length];
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                newHeap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }
        if (!modified) {
            return false;
        }
        heap = newHeap;
        size = newSize;
        for (int i = parent(size - 1); i >= 0; i--) {
            siftDown(i);
        }
        return true;
    }



    // Остальные методы Queue/Collection
    @Override public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}
