package by.it.group451002.gorbach.lesson10;

import java.util.*;

public class MyPriorityQueue<E> implements Queue<E> {

    //Создайте class MyPriorityQueue<E>, который реализует интерфейс Queue<E>
    //и работает на основе кучи, построенной на приватном массиве типа E[]
    //БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private static final int INITIAL_CAPACITY = 10;
    private E[] heap;
    private int size;
    private final Comparator<? super E> comparator;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = (E[]) new Object[INITIAL_CAPACITY];
        size = 0;
        comparator = null;
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(Comparator<? super E> comparator) {
        heap = (E[]) new Object[INITIAL_CAPACITY];
        size = 0;
        this.comparator = comparator;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == heap.length) {
            E[] newHeap = (E[]) new Object[heap.length * 2];
            System.arraycopy(heap, 0, newHeap, 0, size);
            heap = newHeap;
        }
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        }
        return ((Comparable<? super E>) a).compareTo(b);
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (compare(heap[index], heap[parent]) >= 0) {
                break;
            }
            swap(index, parent);
            index = parent;
        }
    }

    private void siftDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
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

    private void swap(int i, int j) {
        E temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    private void heapify() {
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
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

    @Override
    public E poll() {
        if (isEmpty()) {
            return null;
        }
        E result = heap[0];
        size--;
        heap[0] = heap[size];
        heap[size] = null;
        if (size > 0) {
            siftDown(0);
        }
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
        int newSize = 0;
        @SuppressWarnings("unchecked")
        E[] newHeap = (E[]) new Object[heap.length];

        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                newHeap[newSize++] = heap[i];
            }
        }

        if (newSize != size) {
            modified = true;
            System.arraycopy(newHeap, 0, heap, 0, newSize);
            for (int i = newSize; i < size; i++) {
                heap[i] = null;
            }
            size = newSize;
            heapify(); // перестраиваем кучу
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        // Создаем новый массив только с сохраняемыми элементами
        int newSize = 0;
        @SuppressWarnings("unchecked")
        E[] newHeap = (E[]) new Object[heap.length];

        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                newHeap[newSize++] = heap[i];
            }
        }

        if (newSize != size) {
            modified = true;
            System.arraycopy(newHeap, 0, heap, 0, newSize);
            for (int i = newSize; i < size; i++) {
                heap[i] = null;
            }
            size = newSize;
            heapify(); // перестраиваем кучу
        }
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

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
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        // Находим индекс элемента
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, heap[i])) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return false;
        }

        // Заменяем удаляемый элемент последним и восстанавливаем кучу
        size--;
        heap[index] = heap[size];
        heap[size] = null;

        if (index < size) {
            // Восстанавливаем свойства кучи
            if (index > 0 && compare(heap[index], heap[(index - 1) / 2]) < 0) {
                siftUp(index);
            } else {
                siftDown(index);
            }
        }

        return true;
    }
}