package by.it.group410902.siomchena.lesson10;

import java.util.*;

public class MyPriorityQueue<E> implements Queue<E> {
    private static final int capacity = 10;
    private E[] heap;
    private int size;
    private Comparator<? super E> comparator;

    public MyPriorityQueue() {
        this(capacity, null);
    }

    public MyPriorityQueue(int initialCapacity) {
        this(initialCapacity, null);
    }

    public MyPriorityQueue(Comparator<? super E> comparator) {
        this(capacity, comparator);
    }

    public MyPriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
        if (initialCapacity < 1)
            throw new IllegalArgumentException();
        this.heap = (E[]) new Object[initialCapacity];
        this.comparator = comparator;
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(Collection<? extends E> c) {
        this.heap = (E[]) new Object[Math.max(c.size(), capacity)];
        this.size = 0;
        this.comparator = null;

        for (E element : c) {
            if (element != null) {
                if (size == heap.length) {
                    resize();
                }
                heap[size++] = element;
            }
        }

        for (int i = size / 2 - 1; i >= 0; i--) {
            heapifyDown(i);
        }
    }

    private void resize() {
        int newCapacity = heap.length * 2;
        E[] newHeap = (E[]) new Object[newCapacity];
        System.arraycopy(heap, 0, newHeap, 0, size);
        heap = newHeap;
    }

    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        } else {
            @SuppressWarnings("unchecked")
            Comparable<? super E> comparable = (Comparable<? super E>) a;
            return comparable.compareTo(b);
        }
    }

    private int parent(int index) {
        return (index - 1) / 2;
    }

    private int leftChild(int index) {
        return 2 * index + 1;
    }

    private int rightChild(int index) {
        return 2 * index + 2;
    }

    private void swap(int i, int j) {
        E temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    private void heapifyUp(int index) {
        while (index > 0) {
            int parent = parent(index);
            if (compare(heap[index], heap[parent]) >= 0) {
                break;
            }
            swap(index, parent);
            index = parent;
        }
    }

    private void heapifyDown(int index) {
        while (true) {
            int left = leftChild(index);
            int right = rightChild(index);
            int smallest = index;

            if (left < size && compare(heap[left], heap[smallest]) < 0) {
                smallest = left;
            }

            if (right < size && compare(heap[right], heap[smallest]) < 0) {
                smallest = right;
            }

            if (smallest == index) {
                break;
            }

            swap(index, smallest);
            index = smallest;
        }
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
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
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, heap[i])) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    private E removeAt(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        E removed = heap[index];

        heap[index] = heap[size - 1];
        heap[size - 1] = null;
        size--;

        if (index < size) {
            heapifyDown(index);
            if (index > 0 && compare(heap[index], heap[parent(index)]) < 0) {
                heapifyUp(index);
            }
        }

        return removed;
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
            if (Objects.equals(element, heap[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException();
        }

        if (size == heap.length) {
            resize();
        }

        heap[size] = element;
        heapifyUp(size);
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }

        E result = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;

        if (size > 0) {
            heapifyDown(0);
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
        boolean modified = false;
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                heap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        for (int i = newSize; i < size; i++) {
            heap[i] = null;
        }

        size = newSize;

        if (modified) {
            for (int i = size / 2 - 1; i >= 0; i--) {
                heapifyDown(i);
            }
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                heap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        for (int i = newSize; i < size; i++) {
            heap[i] = null;
        }

        size = newSize;

        if (modified) {
            for (int i = size / 2 - 1; i >= 0; i--) {
                heapifyDown(i);
            }
        }

        return modified;
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