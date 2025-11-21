package by.it.group410902.plekhova.lesson10;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class MyPriorityQueue<E extends Comparable<? super E>> implements java.util.Queue<E> {
    private E[] heap;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = (E[]) new Comparable[DEFAULT_CAPACITY];
        size = 0;
    }


    private void grow() {
        int newCap = heap.length * 2;
        @SuppressWarnings("unchecked")
        E[] newArr = (E[]) new Comparable[newCap];
        System.arraycopy(heap, 0, newArr, 0, size);
        heap = newArr;
    }

    private void ensureCapacityForAdd() {
        if (size == heap.length) grow();
    }


    private int siftUp(int idx) {
        E x = heap[idx];
        while (idx > 0) {
            int parent = (idx - 1) / 2;
            if (heap[parent].compareTo(x) <= 0) break;
            heap[idx] = heap[parent];
            idx = parent;
        }
        heap[idx] = x;
        return idx;
    }

    private int siftDown(int idx) {
        E x = heap[idx];
        int half = size / 2; // узлы с детьми
        while (idx < half) {
            int left = 2 * idx + 1;
            int right = left + 1;
            int smallest = left;
            if (right < size && heap[right].compareTo(heap[left]) < 0) smallest = right;
            if (heap[smallest].compareTo(x) >= 0) break;
            heap[idx] = heap[smallest];
            idx = smallest;
        }
        heap[idx] = x;
        return idx;
    }

    private int indexOf(Object o) {
        for (int i = 0; i < size; i++) if (Objects.equals(heap[i], o)) return i;
        return -1;
    }



    @Override
    public boolean offer(E e) {
        if (e == null) throw new NullPointerException();
        ensureCapacityForAdd();
        heap[size] = e;
        size++;
        siftUp(size - 1);
        return true;
    }

    @Override
    public boolean add(E e) {
        return offer(e);
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E result = heap[0];
        int last = size - 1;
        heap[0] = heap[last];
        heap[last] = null;
        size--;
        if (size > 0) siftDown(0);
        return result;
    }

    @Override
    public E remove() {
        E v = poll();
        if (v == null) throw new NoSuchElementException();
        return v;
    }

    @Override
    public E peek() {
        return size == 0 ? null : heap[0];
    }

    @Override
    public E element() {
        E v = peek();
        if (v == null) throw new NoSuchElementException();
        return v;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) heap[i] = null;
        size = 0;
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
    public boolean contains(Object o) {
        return indexOf(o) != -1;
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

    /*
      Удаляет первый найденный объект, равный o.
     */
    @Override
    public boolean remove(Object o) {
        int idx = indexOf(o);
        if (idx == -1) return false;
        int last = size - 1;
        heap[idx] = heap[last];
        heap[last] = null;
        size--;
        if (idx < size) {
            int newPos = siftDown(idx);
            if (newPos == idx) siftUp(idx);
        }
        return true;
    }

    /*
      removeAll: перестроим массив, оставив только элементы, НЕ входящие в c,
      затем выполним heapify (bottom-up).
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        @SuppressWarnings("unchecked")
        E[] newHeap = (E[]) new Comparable[heap.length];
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) newHeap[newSize++] = heap[i];
        }
        boolean changed = newSize != size;
        heap = newHeap;
        size = newSize;
        for (int i = (size / 2) - 1; i >= 0; i--) siftDown(i);
        return changed;
    }


    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        @SuppressWarnings("unchecked")
        E[] newHeap = (E[]) new Comparable[heap.length];
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) newHeap[newSize++] = heap[i];
        }
        boolean changed = newSize != size;
        heap = newHeap;
        size = newSize;
        for (int i = (size / 2) - 1; i >= 0; i--) siftDown(i);
        return changed;
    }


    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
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
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }


}
