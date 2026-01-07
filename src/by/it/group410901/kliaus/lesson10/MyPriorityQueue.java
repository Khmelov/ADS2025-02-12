package by.it.group410901.kliaus.lesson10;

import java.util.*;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {
    private static final int DEFAULT_CAPACITY = 11;
    private Object[] heap;
    private int size;

    public MyPriorityQueue() {
        heap = new Object[DEFAULT_CAPACITY];
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
    public void clear() {
        Arrays.fill(heap, 0, size, null);
        size = 0;
    }

    private void ensureCapacity() {
        if (size == heap.length) {
            int newCapacity = heap.length * 2;
            heap = Arrays.copyOf(heap, newCapacity);
        }
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        ensureCapacity();
        heap[size] = e;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    private void siftUp(int index) {
        E element = (E) heap[index];
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            E parent = (E) heap[parentIndex];
            if (element.compareTo(parent) >= 0) {
                break;
            }
            heap[index] = parent;
            index = parentIndex;
        }
        heap[index] = element;
    }

    @Override
    public E poll() {
        if (isEmpty()) {
            return null;
        }
        E result = (E) heap[0];
        size--;
        if (size > 0) {
            heap[0] = heap[size];
            heap[size] = null;
            siftDown(0);
        } else {
            heap[0] = null;
        }
        return result;
    }

    @Override
    public E remove() {
        E result = poll();
        if (result == null) {
            throw new NoSuchElementException();
        }
        return result;
    }

    private void siftDown(int index) {
        E element = (E) heap[index];
        int half = size / 2;
        while (index < half) {
            int leftChild = 2 * index + 1;
            int rightChild = leftChild + 1;
            int smallestChild = leftChild;

            if (rightChild < size && ((E) heap[rightChild]).compareTo((E) heap[leftChild]) < 0) {
                smallestChild = rightChild;
            }

            if (element.compareTo((E) heap[smallestChild]) <= 0) {
                break;
            }

            heap[index] = heap[smallestChild];
            index = smallestChild;
        }
        heap[index] = element;
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            return null;
        }
        return (E) heap[0];
    }

    @Override
    public E element() {
        E result = peek();
        if (result == null) {
            throw new NoSuchElementException();
        }
        return result;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(heap[i], o)) {
                return true;
            }
        }
        return false;
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
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null || c.isEmpty()) {
            return false;
        }

        // Создаем HashSet для O(1) проверки содержания
        Set<?> set = (c instanceof Set) ? (Set<?>) c : new HashSet<>(c);

        boolean modified = false;
        int writeIndex = 0;

        // Проходим по куче и оставляем только элементы, которых нет в c
        for (int readIndex = 0; readIndex < size; readIndex++) {
            if (!set.contains(heap[readIndex])) {
                heap[writeIndex++] = heap[readIndex];
            } else {
                modified = true;
            }
        }

        // Очищаем оставшиеся элементы
        for (int i = writeIndex; i < size; i++) {
            heap[i] = null;
        }

        size = writeIndex;

        // Восстанавливаем свойства кучи (heapify)
        if (modified && size > 1) {
            heapify();
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }

        // Создаем HashSet для O(1) проверки содержания
        Set<?> set = (c instanceof Set) ? (Set<?>) c : new HashSet<>(c);

        boolean modified = false;
        int writeIndex = 0;

        // Проходим по куче и оставляем только элементы, которые есть в c
        for (int readIndex = 0; readIndex < size; readIndex++) {
            if (set.contains(heap[readIndex])) {
                heap[writeIndex++] = heap[readIndex];
            } else {
                modified = true;
            }
        }

        // Очищаем оставшиеся элементы
        for (int i = writeIndex; i < size; i++) {
            heap[i] = null;
        }

        size = writeIndex;

        // Восстанавливаем свойства кучи (heapify)
        if (modified && size > 1) {
            heapify();
        }

        return modified;
    }

    private void heapify() {
        // Строим кучу снизу вверх (алгоритм Floyd)
        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) {
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

    // Нереализованные методы
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
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }
}