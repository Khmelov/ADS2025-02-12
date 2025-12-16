package by.it.group410901.volkov.lesson10;

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

    // Конструкторы
    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        this.heap = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = null;
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        this.heap = (E[]) new Object[initialCapacity];
        this.size = 0;
        this.comparator = null;
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(Comparator<? super E> comparator) {
        this.heap = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = comparator;
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        this.heap = (E[]) new Object[initialCapacity];
        this.size = 0;
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
            throw new NullPointerException("Element cannot be null");
        }

        ensureCapacity();

        // Добавляем элемент в конец кучи
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

        // Перемещаем последний элемент в корень и восстанавливаем кучу
        size--;
        heap[0] = heap[size];
        heap[size] = null; // Помогаем сборщику мусора
        siftDown(0);

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
        if (c.isEmpty()) {
            return false;
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
        if (c.isEmpty()) {
            return false;
        }

        boolean modified = false;
        // Создаем временную кучу для элементов, которые нужно сохранить
        @SuppressWarnings("unchecked")
        E[] newHeap = (E[]) new Object[heap.length];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                newHeap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        // Заменяем старую кучу новой и восстанавливаем свойства кучи
        this.heap = newHeap;
        this.size = newSize;
        heapify();

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }

        boolean modified = false;
        // Создаем временную кучу для элементов, которые нужно сохранить
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

        // Заменяем старую кучу новой и восстанавливаем свойства кучи
        this.heap = newHeap;
        this.size = newSize;
        heapify();

        return modified;
    }

    @Override
    public boolean remove(Object o) {
        // Линейный поиск элемента
        for (int i = 0; i < size; i++) {
            if (o == null ? heap[i] == null : o.equals(heap[i])) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    // Вспомогательные методы для работы с кучей

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

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    // Вспомогательный метод для удаления элемента по индексу
    private void removeAt(int index) {
        size--;
        if (size == index) {
            // Удаляем последний элемент
            heap[index] = null;
        } else {
            // Перемещаем последний элемент на место удаляемого
            E moved = heap[size];
            heap[size] = null;
            heap[index] = moved;
            siftDown(index);
            // Если элемент не опустился вниз, пробуем поднять его вверх
            if (heap[index] == moved) {
                siftUp(index);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        } else {
            return ((Comparable<? super E>) a).compareTo(b);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////     Остальные методы интерфейса Queue (не реализованы)    ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not implemented");
    }
}