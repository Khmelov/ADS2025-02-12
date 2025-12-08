package by.it.group451001.drzhevetskiy.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    private E[] heap;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = (E[]) new Comparable[16];
    }

    private void ensureCapacity() {
        if (size >= heap.length) {
            @SuppressWarnings("unchecked")
            E[] newArr = (E[]) new Comparable[heap.length * 2];
            System.arraycopy(heap, 0, newArr, 0, heap.length);
            heap = newArr;
        }
    }

    private void siftUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (heap[i].compareTo(heap[parent]) < 0) {
                E temp = heap[i];
                heap[i] = heap[parent];
                heap[parent] = temp;
                i = parent;
            } else break;
        }
    }

    private void siftDown(int i) {
        while (true) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = i;

            if (left < size && heap[left].compareTo(heap[smallest]) < 0)
                smallest = left;
            if (right < size && heap[right].compareTo(heap[smallest]) < 0)
                smallest = right;

            if (smallest == i) break;

            E tmp = heap[i];
            heap[i] = heap[smallest];
            heap[smallest] = tmp;

            i = smallest;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() { size = 0; }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public boolean offer(E element) {
        ensureCapacity();
        heap[size] = element;
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

        E result = heap[0];

        heap[0] = heap[size - 1];
        size--;

        siftDown(0);

        return result;
    }

    @Override
    public E element() {
        E res = peek();
        if (res == null) throw new NoSuchElementException();
        return res;
    }

    @Override
    public E peek() {
        return size == 0 ? null : heap[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null && heap[i] == null) return true;
            if (o != null && o.equals(heap[i])) return true;
        }
        return false;
    }


    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object x : c) {
            if (!contains(x)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E x : c) {
            offer(x);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;

        @SuppressWarnings("unchecked")
        E[] newHeap = (E[]) new Comparable[heap.length];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                newHeap[newSize++] = heap[i];
            } else {
                changed = true;
            }
        }

        heap = newHeap;
        size = newSize;

        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }

        return changed;
    }


    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;

        @SuppressWarnings("unchecked")
        E[] newHeap = (E[]) new Comparable[heap.length];

        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                newHeap[newSize++] = heap[i];
            } else {
                changed = true;
            }
        }

        heap = newHeap;
        size = newSize;

        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }

        return changed;
    }


    private void removeSingle(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && heap[i] == null) ||
                    (o != null && o.equals(heap[i]))) {

                heap[i] = heap[size - 1];
                size--;
                siftDown(i);
                siftUp(i);

                return;
            }
        }
    }

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }

}

