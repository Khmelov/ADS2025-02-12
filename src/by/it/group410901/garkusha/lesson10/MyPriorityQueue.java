package by.it.group410901.garkusha.lesson10;

import java.util.*;

import java.util.*;

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

    // Вспомогательные методы для работы с кучей
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

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        } else {
            return ((Comparable<? super E>) a).compareTo(b);
        }
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parent = parent(index);
            if (compare(heap[index], heap[parent]) >= 0) {
                break;
            }
            swap(index, parent);
            index = parent;
        }
    }

    private void siftDown(int index) {
        int smallest = index;
        int left = leftChild(index);
        int right = rightChild(index);

        if (left < size && compare(heap[left], heap[smallest]) < 0) {
            smallest = left;
        }

        if (right < size && compare(heap[right], heap[smallest]) < 0) {
            smallest = right;
        }

        if (smallest != index) {
            swap(index, smallest);
            siftDown(smallest);
        }
    }

    // Построение кучи из массива
    private void heapify() {
        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i);
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
        if (isEmpty()) {
            return "[]";
        }

        String result = "[";
        for (int i = 0; i < size; i++) {
            result += heap[i];
            if (i < size - 1) {
                result += ", ";
            }
        }
        result += "]";
        return result;
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

        if (index < size) { // если удалили не последний элемент
            siftDown(index);
            if (heap[index] == removed) { // если элемент не сдвинулся при siftDow
                siftUp(index);
            }
        }

        return removed;
    }

    @Override
    public E remove() {
        if (isEmpty()) {
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
        ensureCapacity();
        heap[size] = element;
        siftUp(size);
        size++;
        return true;
    }


    // Удаление минимального элемента
    @Override
    public E poll() {
        if (isEmpty()) {
            return null;
        }
        E result = heap[0];
        removeAt(0);
        return result;
    }

    @Override
    public E peek() {
        return isEmpty() ? null : heap[0];
    }

    @Override
    public E element() {
        if (isEmpty()) {
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

        // Создаем новый массив без удаляемых элементов
        @SuppressWarnings("unchecked")
        E[] newHeap = (E[]) new Object[heap.length];
        int newIndex = 0;

        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                newHeap[newIndex++] = heap[i];
            } else {
                modified = true; // нашли элемент для удаления
            }
        }

        // Если были изменения, обновляем кучу
        if (modified) {
            heap = newHeap;
            size = newIndex;
            heapify();
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;

        // Создаем новый массив только с сохраняемыми элементами
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

        // Если есть изменения, обновляем кучу
        if (modified) {
            heap = newHeap;
            size = newSize;
            heapify();
        }

        return modified;
    }

    // Остальные методы интерфейса Queue
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return heap[currentIndex++];
            }
        };
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
            return (T[]) Arrays.copyOf(heap, size, a.getClass());
        }
        System.arraycopy(heap, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}