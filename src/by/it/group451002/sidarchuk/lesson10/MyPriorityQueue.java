package by.it.group451002.sidarchuk.lesson10;

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
    public MyPriorityQueue(Comparator<? super E> comparator) {
        this.heap = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = comparator;
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(Collection<? extends E> c) {
        this.heap = (E[]) new Object[Math.max(DEFAULT_CAPACITY, c.size())];
        this.size = 0;
        this.comparator = null;
        addAll(c);
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
            throw new NoSuchElementException("Queue is empty");
        }
        return poll();
    }

    @Override
    public boolean contains(Object element) {
        for (int i = 0; i < size; i++) {
            if (element == null) {
                if (heap[i] == null) return true;
            } else {
                if (element.equals(heap[i])) return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        // Увеличиваем массив при необходимости
        if (size >= heap.length) {
            resizeHeap();
        }

        // Добавляем элемент в конец
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

        // Перемещаем последний элемент в корень
        heap[0] = heap[size];
        heap[size] = null; // Помогаем сборщику мусора

        // Просеиваем вниз
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
            throw new NoSuchElementException("Queue is empty");
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
        // Создаем временную копию для фильтрации
        @SuppressWarnings("unchecked")
        E[] temp = (E[]) new Object[size];
        int newSize = 0;

        // Копируем только те элементы, которые не входят в коллекцию c
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                temp[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        // Перестраиваем кучу
        if (modified) {
            this.heap = temp;
            this.size = newSize;
            buildHeap();
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        // Создаем временную копию для фильтрации
        @SuppressWarnings("unchecked")
        E[] temp = (E[]) new Object[size];
        int newSize = 0;

        // Копируем только те элементы, которые входят в коллекцию c
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                temp[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        // Перестраиваем кучу
        if (modified) {
            this.heap = temp;
            this.size = newSize;
            buildHeap();
        }

        return modified;
    }

    // Вспомогательные методы для работы с кучей
    @SuppressWarnings("unchecked")
    private void resizeHeap() {
        int newCapacity = heap.length * 2;
        E[] newHeap = (E[]) new Object[newCapacity];
        System.arraycopy(heap, 0, newHeap, 0, size);
        heap = newHeap;
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

    private void buildHeap() {
        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i);
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

    // Остальные методы интерфейса Queue (не реализованы)
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
        // Поиск элемента
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (o == null) {
                if (heap[i] == null) {
                    index = i;
                    break;
                }
            } else {
                if (o.equals(heap[i])) {
                    index = i;
                    break;
                }
            }
        }

        if (index == -1) {
            return false;
        }

        // Удаление элемента
        size--;
        heap[index] = heap[size];
        heap[size] = null;

        if (index < size) {
            siftDown(index);
            siftUp(index);
        }

        return true;
    }
}
