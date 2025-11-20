package by.it.group451004.kozlov.lesson10;

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
    public MyPriorityQueue(Comparator<? super E> comparator) {
        heap = (E[]) new Object[DEFAULT_CAPACITY];
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
    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return poll();
    }

    @Override
    public E poll() {
        if (size == 0) {
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
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return heap[0];
    }

    @Override
    public E peek() {
        return (size == 0) ? null : heap[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
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
        // Точная копия поведения стандартной PriorityQueue
        // Создаем битовую маску для отслеживания удаляемых элементов
        boolean[] toRemove = new boolean[size];
        int removeCount = 0;

        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                toRemove[i] = true;
                removeCount++;
                modified = true;
            }
        }

        if (modified) {
            // Создаем новый массив с оставшимися элементами
            E[] newHeap = Arrays.copyOf(heap, heap.length);
            int newSize = 0;

            for (int i = 0; i < size; i++) {
                if (!toRemove[i]) {
                    newHeap[newSize++] = heap[i];
                }
            }

            // Заменяем старую кучу новой
            heap = newHeap;
            size = newSize;

            // Восстанавливаем свойства кучи
            for (int i = (size >>> 1) - 1; i >= 0; i--) {
                siftDown(i);
            }
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }

        boolean modified = false;
        // Точная копия поведения стандартной PriorityQueue
        // Создаем битовую маску для отслеживания сохраняемых элементов
        boolean[] toRetain = new boolean[size];
        int retainCount = 0;

        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                toRetain[i] = true;
                retainCount++;
            } else {
                modified = true;
            }
        }

        if (modified) {
            // Создаем новый массив с сохраняемыми элементами
            E[] newHeap = Arrays.copyOf(heap, heap.length);
            int newSize = 0;

            for (int i = 0; i < size; i++) {
                if (toRetain[i]) {
                    newHeap[newSize++] = heap[i];
                }
            }

            // Заменяем старую кучу новой
            heap = newHeap;
            size = newSize;

            // Восстанавливаем свойства кучи
            for (int i = (size >>> 1) - 1; i >= 0; i--) {
                siftDown(i);
            }
        }

        return modified;
    }

    // Вспомогательные методы для работы с кучей
    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
    private void siftDown(int index) {
        E element = heap[index];
        int half = size >>> 1;

        while (index < half) {
            int childIndex = (index << 1) + 1;
            E child = heap[childIndex];
            int rightIndex = childIndex + 1;

            if (rightIndex < size && compare(child, heap[rightIndex]) > 0) {
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
        }
        return ((Comparable<? super E>) a).compareTo(b);
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == heap.length) {
            int newCapacity = heap.length + (heap.length >> 1);
            E[] newHeap = (E[]) new Object[newCapacity];
            System.arraycopy(heap, 0, newHeap, 0, size);
            heap = newHeap;
        }
    }

    private int indexOf(Object o) {
        if (o != null) {
            for (int i = 0; i < size; i++) {
                if (o.equals(heap[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    /////////////////////////////////////////////////////////////////////////
    //////      Остальные методы интерфейса Queue (необязательные)    ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index == -1) {
            return false;
        }
        removeAt(index);
        return true;
    }

    private void removeAt(int index) {
        size--;
        if (size == index) {
            heap[index] = null;
        } else {
            E moved = heap[size];
            heap[size] = null;
            heap[index] = moved;
            siftDown(index);
            if (heap[index] == moved) {
                siftUp(index);
            }
        }
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
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