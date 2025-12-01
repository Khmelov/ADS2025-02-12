package by.it.group451001.khokhlov.lesson10;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private E[] heap;
    private int size;
    private final Comparator<? super E> comparator;

    // Конструкторы (остаются без изменений)
    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
        comparator = null;
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
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
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
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
    @SuppressWarnings("unchecked")
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
            throw new NoSuchElementException("Priority queue is empty");
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
            throw new NullPointerException("Null elements are not allowed");
        }

        ensureCapacity();

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
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;

        if (size > 0) {
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
            throw new NoSuchElementException("Priority queue is empty");
        }
        return heap[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Исправленные методы работы с коллекциями

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
            throw new NullPointerException("Collection cannot be null");
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
            throw new NullPointerException("Collection cannot be null");
        }

        if (size == 0) {
            return false;
        }

        // Создаем временную кучу для элементов, которые нужно сохранить
        @SuppressWarnings("unchecked")
        E[] tempHeap = (E[]) new Object[size];
        int tempSize = 0;

        // Линейный проход по всем элементам кучи
        for (int i = 0; i < size; i++) {
            E element = heap[i];
            if (!c.contains(element)) {
                tempHeap[tempSize++] = element;
            }
        }

        if (tempSize == size) {
            return false; // Ничего не изменилось
        }

        // Восстанавливаем кучу из сохраненных элементов за O(n)
        rebuildHeap(tempHeap, tempSize);
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }

        if (size == 0) {
            return false;
        }

        // Создаем временную кучу для элементов, которые нужно сохранить
        @SuppressWarnings("unchecked")
        E[] tempHeap = (E[]) new Object[size];
        int tempSize = 0;

        // Линейный проход по всем элементам кучи
        for (int i = 0; i < size; i++) {
            E element = heap[i];
            if (c.contains(element)) {
                tempHeap[tempSize++] = element;
            }
        }

        if (tempSize == size) {
            return false; // Ничего не изменилось
        }

        // Восстанавливаем кучу из сохраненных элементов за O(n)
        rebuildHeap(tempHeap, tempSize);
        return true;
    }

    // Новый метод для восстановления кучи за O(n)
    private void rebuildHeap(E[] elements, int newSize) {
        // Копируем элементы в основную кучу
        if (heap.length < newSize) {
            @SuppressWarnings("unchecked")
            E[] newHeap = (E[]) new Object[newSize];
            heap = newHeap;
        }

        System.arraycopy(elements, 0, heap, 0, newSize);
        size = newSize;

        // Восстанавливаем свойства кучи за O(n) с помощью Floyd's algorithm
        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    // Вспомогательные методы для работы с кучей (без изменений)

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == heap.length) {
            int newCapacity = heap.length * 2;
            E[] newHeap = (E[]) new Object[newCapacity];
            System.arraycopy(heap, 0, newHeap, 0, size);
            heap = newHeap;
        }
    }

    private void siftUp(int index) {
        E element = heap[index];
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
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
        E element = heap[index];
        int half = size / 2;

        while (index < half) {
            int childIndex = (index * 2) + 1;
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
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        } else {
            Comparable<? super E> comparable = (Comparable<? super E>) a;
            return comparable.compareTo(b);
        }
    }

    // Остальные методы (без изменений)

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        System.arraycopy(heap, 0, array, 0, size);
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) toArray();
        }
        System.arraycopy(heap, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }
}