package by.it.group410901.danilova.lesson10;


import java.util.*;

public class MyPriorityQueue<E> implements Queue<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] heap;
    private int size;
    private final Comparator<? super E> comparator;

    public MyPriorityQueue() {
        this(DEFAULT_CAPACITY, null);
    }

    public MyPriorityQueue(int initialCapacity) {
        this(initialCapacity, null);
    }

    public MyPriorityQueue(Comparator<? super E> comparator) {
        this(DEFAULT_CAPACITY, comparator);
    }

    public MyPriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
        if (initialCapacity < 1) throw new IllegalArgumentException();
        this.heap = new Object[initialCapacity];
        this.comparator = comparator;
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        }
        return ((Comparable<? super E>) a).compareTo(b);
    }

    private void grow(int minCapacity) {
        int oldCapacity = heap.length;
        int newCapacity = oldCapacity + (oldCapacity < 64 ? oldCapacity + 2 : oldCapacity >> 1);
        if (newCapacity < minCapacity) newCapacity = minCapacity;
        heap = Arrays.copyOf(heap, newCapacity);
    }

    private void siftUp(int k, E x) {
        if (comparator != null) siftUpUsingComparator(k, x);
        else siftUpComparable(k, x);
    }

    @SuppressWarnings("unchecked")
    private void siftUpComparable(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            Object e = heap[parent];
            if (key.compareTo((E) e) >= 0) break;
            heap[k] = e;
            k = parent;
        }
        heap[k] = key;
    }

    @SuppressWarnings("unchecked")
    private void siftUpUsingComparator(int k, E x) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            Object e = heap[parent];
            if (comparator.compare(x, (E) e) >= 0) break;
            heap[k] = e;
            k = parent;
        }
        heap[k] = x;
    }

    private void siftDown(int k, E x) {
        if (comparator != null) siftDownUsingComparator(k, x);
        else siftDownComparable(k, x);
    }

    @SuppressWarnings("unchecked")
    private void siftDownComparable(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            Object c = heap[child];
            int right = child + 1;
            if (right < size && ((Comparable<? super E>) c).compareTo((E) heap[right]) > 0) {
                child = right;
                c = heap[child];
            }
            if (key.compareTo((E) c) <= 0) break;
            heap[k] = c;
            k = child;
        }
        heap[k] = key;
    }

    @SuppressWarnings("unchecked")
    private void siftDownUsingComparator(int k, E x) {
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            Object c = heap[child];
            int right = child + 1;
            if (right < size && comparator.compare((E) c, (E) heap[right]) > 0) {
                child = right;
                c = heap[child];
            }
            if (comparator.compare(x, (E) c) <= 0) break;
            heap[k] = c;
            k = child;
        }
        heap[k] = x;
    }

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) siftDown(i, (E) heap[i]);
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

    private int indexOf(Object o) {
        if (o != null) {
            for (int i = 0; i < size; i++) {
                if (o.equals(heap[i])) return i;
            }
        }
        return -1;
    }

    @Override
    public boolean add(E e) {
        return offer(e);
    }

    @Override
    public boolean offer(E e) {
        if (e == null) throw new NullPointerException();
        int i = size;
        if (i >= heap.length) grow(i + 1);
        size = i + 1;
        if (i == 0) heap[0] = e;
        else siftUp(i, e);
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove() {
        E x = poll();
        if (x == null) throw new NoSuchElementException();
        return x;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E poll() {
        if (size == 0) return null;
        int s = --size;
        E result = (E) heap[0];
        E x = (E) heap[s];
        heap[s] = null;
        if (s != 0) siftDown(0, x);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E element() {
        E x = (E) heap[0];
        if (x == null) throw new NoSuchElementException();
        return x;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E peek() {
        return (size == 0) ? null : (E) heap[0];
    }

    @Override
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i == -1) return false;
        removeAt(i);
        return true;
    }

    @SuppressWarnings("unchecked")
    private E removeAt(int i) {
        int s = --size;
        if (s == i) heap[i] = null;
        else {
            E moved = (E) heap[s];
            heap[s] = null;
            siftDown(i, moved);
            if (heap[i] == moved) siftUp(i, moved);
        }
        return (E) heap[0];
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) if (!contains(e)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == this) throw new IllegalArgumentException();
        boolean modified = false;
        for (E e : c) if (add(e)) modified = true;
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;

        // Создаем новый массив только с элементами, которые не содержатся в c
        @SuppressWarnings("unchecked")
        E[] newHeap = (E[]) new Object[heap.length];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                newHeap[newSize++] = (E) heap[i];
            } else {
                modified = true;
            }
        }

        // Если есть изменения, обновляем кучу
        if (modified) {
            heap = newHeap;
            size = newSize;
            heapify();
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;

        // Создаем новый массив только с сохраняемыми элементами
        @SuppressWarnings("unchecked")
        E[] newHeap = (E[]) new Object[heap.length];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                newHeap[newSize++] = (E) heap[i];
            } else {
                modified = true;
            }
        }

        // Если есть изменения, обновляем кучу
        if (modified) {
            heap = newHeap;
            size = newSize;
            heapify();
        }

        return modified;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) heap[i] = null;
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(heap[i]);
        }
        return sb.append(']').toString();
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
}