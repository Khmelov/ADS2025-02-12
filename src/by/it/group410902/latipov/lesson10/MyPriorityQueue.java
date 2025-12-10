package by.it.group410902.latipov.lesson10;

import java.util.*;

public class MyPriorityQueue<E> implements Queue<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private Object[] heap;
    private int size;
    private final Comparator<? super E> comparator;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        this.heap = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = null;
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(Comparator<? super E> comparator) {
        this.heap = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = comparator;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        // ВАЖНО: НЕ СОРТИРОВАТЬ! Должен возвращать элементы в порядке кучи
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
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, heap[i])) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, heap[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }

        ensureCapacity(size + 1);
        heap[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E poll() {
        if (size == 0) {
            return null;
        }

        E result = (E) heap[0];
        removeAt(0);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E peek() {
        return (size == 0) ? null : (E) heap[0];
    }

    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException("Queue is empty");
        }
        return peek();
    }

    @Override
    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException("Queue is empty");
        }
        return poll();
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
        if (c.isEmpty()) {
            return false;
        }

        ensureCapacity(size + c.size());
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
        if (c.isEmpty() || size == 0) {
            return false;
        }

        // Эффективная реализация за O(n)
        boolean modified = false;
        int newSize = 0;

        // Проходим по всем элементам и сохраняем только те, которых нет в коллекции c
        for (int i = 0; i < size; i++) {
            @SuppressWarnings("unchecked")
            E element = (E) heap[i];
            if (!c.contains(element)) {
                heap[newSize++] = element;
            } else {
                modified = true;
            }
        }

        // Очищаем оставшиеся элементы
        for (int i = newSize; i < size; i++) {
            heap[i] = null;
        }

        size = newSize;

        // Перестраиваем кучу за O(n)
        if (modified && size > 0) {
            for (int i = (size >>> 1) - 1; i >= 0; i--) {
                siftDown(i);
            }
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (size == 0) {
            return false;
        }
        if (c.isEmpty()) {
            clear();
            return true;
        }

        // Эффективная реализация за O(n)
        boolean modified = false;
        int newSize = 0;

        // Проходим по всем элементам и сохраняем только те, которые есть в коллекции c
        for (int i = 0; i < size; i++) {
            @SuppressWarnings("unchecked")
            E element = (E) heap[i];
            if (c.contains(element)) {
                heap[newSize++] = element;
            } else {
                modified = true;
            }
        }

        // Очищаем оставшиеся элементы
        for (int i = newSize; i < size; i++) {
            heap[i] = null;
        }

        size = newSize;

        // Перестраиваем кучу за O(n)
        if (modified && size > 0) {
            for (int i = (size >>> 1) - 1; i >= 0; i--) {
                siftDown(i);
            }
        }

        return modified;
    }

    // Вспомогательные методы для работы с кучей

    @SuppressWarnings("unchecked")
    private void siftUp(int index) {
        E element = (E) heap[index];
        while (index > 0) {
            int parentIndex = (index - 1) >>> 1;
            E parent = (E) heap[parentIndex];
            if (compare(element, parent) >= 0) {
                break;
            }
            heap[index] = parent;
            index = parentIndex;
        }
        heap[index] = element;
    }

    @SuppressWarnings("unchecked")
    private void siftDown(int index) {
        E element = (E) heap[index];
        int half = size >>> 1;
        while (index < half) {
            int childIndex = (index << 1) + 1;
            E child = (E) heap[childIndex];
            int rightIndex = childIndex + 1;

            if (rightIndex < size && compare(child, (E) heap[rightIndex]) > 0) {
                childIndex = rightIndex;
                child = (E) heap[childIndex];
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
        }
        return ((Comparable<? super E>) a).compareTo(b);
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > heap.length) {
            int newCapacity = Math.max(heap.length * 2, minCapacity);
            heap = Arrays.copyOf(heap, newCapacity);
        }
    }

    @SuppressWarnings("unchecked")
    private E removeAt(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        E removed = (E) heap[index];

        // Перемещаем последний элемент на место удаляемого
        int lastIndex = --size;
        heap[index] = heap[lastIndex];
        heap[lastIndex] = null;

        // Восстанавливаем свойства кучи
        if (index < lastIndex) {
            siftDown(index);
        }

        return removed;
    }

    /////////////////////////////////////////////////////////////////////////
    //////        Остальные методы - необязательные к реализации     ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return Arrays.asList((E[]) toArray()).iterator();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(heap, size);
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