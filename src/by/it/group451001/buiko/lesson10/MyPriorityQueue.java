package by.it.group451001.buiko.lesson10;

import java.util.*;

public class MyPriorityQueue<E> implements Queue<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private E[] heap;
    private int size;
    private final Comparator<? super E> comparator;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
        comparator = null;
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(int initialCapacity) {
        if (initialCapacity < 1) throw new IllegalArgumentException();
        heap = (E[]) new Object[initialCapacity];
        size = 0;
        comparator = null;
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        }
        return ((Comparable<? super E>) a).compareTo(b);
    }

    @SuppressWarnings("unchecked")
    private void grow() {
        int newCapacity = heap.length + (heap.length < 64 ? heap.length + 2 : heap.length >> 1);
        E[] newHeap = (E[]) new Object[newCapacity];
        System.arraycopy(heap, 0, newHeap, 0, heap.length);
        heap = newHeap;
    }

    private void siftUp(int index) {
        E element = heap[index];
        while (index > 0) {
            int parent = (index - 1) >>> 1;
            E parentElement = heap[parent];
            if (compare(element, parentElement) >= 0) break;
            heap[index] = parentElement;
            index = parent;
        }
        heap[index] = element;
    }

    private void siftDown(int index) {
        E element = heap[index];
        int half = size >>> 1;
        while (index < half) {
            int child = (index << 1) + 1;
            E minChild = heap[child];
            int right = child + 1;

            if (right < size && compare(minChild, heap[right]) > 0) {
                child = right;
                minChild = heap[right];
            }

            if (compare(element, minChild) <= 0) break;

            heap[index] = minChild;
            index = child;
        }
        heap[index] = element;
    }

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";

        // Для совместимости с тестами создаем копию и выводим в порядке массива
        MyPriorityQueue<E> copy = new MyPriorityQueue<>();
        copy.heap = Arrays.copyOf(heap, heap.length);
        copy.size = size;

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(copy.heap[i]);
        }
        sb.append(']');
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
        E e = poll();
        if (e == null) throw new NoSuchElementException();
        return e;
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
        if (element == null) throw new NullPointerException();

        if (size >= heap.length) {
            grow();
        }

        heap[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) return null;

        E result = heap[0];
        removeAt(0);
        return result;
    }

    @Override
    public E peek() {
        return (size == 0) ? null : heap[0];
    }

    @Override
    public E element() {
        E e = peek();
        if (e == null) throw new NoSuchElementException();
        return e;
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
        if (c.isEmpty()) return false;

        boolean modified = false;
        // Создаем новый массив без элементов из коллекции c
        E[] newHeap = Arrays.copyOf(heap, heap.length);
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                newHeap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        if (!modified) return false;

        heap = newHeap;
        size = newSize;
        heapify();
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c.isEmpty()) {
            if (size > 0) {
                clear();
                return true;
            }
            return false;
        }

        boolean modified = false;
        // Создаем новый массив только с элементами из коллекции c
        E[] newHeap = Arrays.copyOf(heap, heap.length);
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                newHeap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        if (!modified) return false;

        heap = newHeap;
        size = newSize;
        heapify();
        return true;
    }

    private void removeAt(int index) {
        if (index >= size) return;

        int last = --size;
        if (last == index) {
            heap[index] = null;
        } else {
            E moved = heap[last];
            heap[last] = null;
            heap[index] = moved;
            siftDown(index);
            if (heap[index] == moved) {
                siftUp(index);
            }
        }
    }

    // Остальные методы интерфейса (не требуются для тестов)
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(heap, size);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
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
}