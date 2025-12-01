package by.it.group451004.zarivniak.lesson10;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    private E[] heap;
    private int size;
    private final Comparator<? super E> comparator;

    // Конструкторы
    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
        comparator = null;
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Initial capacity must be at least 1");
        }
        heap = (E[]) new Object[initialCapacity];
        size = 0;
        comparator = null;
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(Comparator<? super E> comparator) {
        heap = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
        this.comparator = comparator;
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Initial capacity must be at least 1");
        }
        heap = (E[]) new Object[initialCapacity];
        size = 0;
        this.comparator = comparator;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
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
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return poll();
    }

    @Override
    public boolean contains(Object element) {
        for (int i = 0; i < size; i++) {
            if (element == null) {
                if (heap[i] == null) {
                    return true;
                }
            } else {
                if (element.equals(heap[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException();
        }

        ensureCapacity(size + 1);

        heap[size] = element;
        siftUp(size);
        size++;

        return true;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }

        E result = heap[0];
        size--;

        E last = heap[size];
        heap[size] = null;

        if (size > 0) {
            heap[0] = last;
            siftDown(0);
        }

        return result;
    }

    @Override
    public E peek() {
        return (size == 0) ? null : heap[0];
    }

    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return heap[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        if (c == this) {
            throw new IllegalArgumentException();
        }

        boolean modified = false;
        for (E element : c) {
            if (offer(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }

        boolean modified = false;
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            E element = heap[i];
            if (!c.contains(element)) {
                heap[newSize++] = element;
            } else {
                modified = true;
            }
        }

        if (modified) {
            for (int i = newSize / 2; i >= 0; i--) {
                siftDown(i, newSize);
            }
            for (int i = newSize; i < size; i++) {
                heap[i] = null;
            }
            size = newSize;
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }

        boolean modified = false;
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            E element = heap[i];
            if (c.contains(element)) {
                heap[newSize++] = element;
            } else {
                modified = true;
            }
        }

        if (modified) {
            for (int i = newSize / 2; i >= 0; i--) {
                siftDown(i, newSize);
            }
            for (int i = newSize; i < size; i++) {
                heap[i] = null;
            }
            size = newSize;
        }

        return modified;
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        } else {
            return ((Comparable<? super E>) a).compareTo(b);
        }
    }

    private void siftUp(int index) {
        E element = heap[index];

        while (index > 0) {
            int parentIndex = (index - 1) >>> 1;
            E parent = heap[parentIndex];

            if (compare(element, parent) >= 0) {
                break;
            }

            heap[index] = parent;
            index = parentIndex;
        }

        heap[index] = element;
    }

    private void siftDown(int index) {
        siftDown(index, size);
    }

    private void siftDown(int index, int size) {
        E element = heap[index];
        int half = size >>> 1;

        while (index < half) {
            int childIndex = (index << 1) + 1;
            E child = heap[childIndex];
            int rightIndex = childIndex + 1;

            if (rightIndex < size && compare(heap[rightIndex], child) < 0) {
                childIndex = rightIndex;
                child = heap[rightIndex];
            }

            if (compare(element, child) <= 0) {
                break;
            }

            heap[index] = child;
            index = childIndex;
        }

        heap[index] = element;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > heap.length) {
            int newCapacity = calculateNewCapacity(heap.length);
            grow(newCapacity);
        }
    }

    private int calculateNewCapacity(int oldCapacity) {
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - MAX_ARRAY_SIZE > 0) {
            if (oldCapacity == MAX_ARRAY_SIZE) {
                throw new OutOfMemoryError();
            }
            return MAX_ARRAY_SIZE;
        }
        return Math.max(newCapacity, DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    private void grow(int newCapacity) {
        E[] newHeap = (E[]) new Object[newCapacity];
        System.arraycopy(heap, 0, newHeap, 0, size);
        heap = newHeap;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(heap, 0, result, 0, size);
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (o == null ? heap[i] == null : o.equals(heap[i])) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return false;
        }

        size--;
        E last = heap[size];
        heap[size] = null;

        if (index != size) {
            heap[index] = last;
            siftDown(index);
            if (heap[index] == last) {
                siftUp(index);
            }
        }

        return true;
    }
}
