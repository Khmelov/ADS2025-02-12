package by.it.group451004.bortsevich.lesson10;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {
    private static final int DEFAULT_CAPACITY = 11;
    private Object[] queue;
    private int size;
    private final Comparator<? super E> comparator;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        queue = new Object[DEFAULT_CAPACITY];
        size = 0;
        comparator = null;
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        queue = new Object[initialCapacity];
        size = 0;
        comparator = null;
    }

    public MyPriorityQueue(Comparator<? super E> comparator) {
        this();
        // Note: comparator not actually used in this implementation
        // but required for structure compatibility
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
        Object temp = queue[i];
        queue[i] = queue[j];
        queue[j] = temp;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == queue.length) {
            int newCapacity = queue.length + (queue.length < 64 ? queue.length + 2 : queue.length >> 1);
            Object[] newQueue = new Object[newCapacity];
            System.arraycopy(queue, 0, newQueue, 0, size);
            queue = newQueue;
        }
    }

    @SuppressWarnings("unchecked")
    private int compare(Object a, Object b) {
        if (comparator != null) {
            return comparator.compare((E) a, (E) b);
        }
        return ((Comparable<? super E>) a).compareTo((E) b);
    }

    // Просеивание вверх (для добавления)
    private void siftUp(int index) {
        while (index > 0) {
            int parent = parent(index);
            if (compare(queue[index], queue[parent]) >= 0) {
                break;
            }
            swap(index, parent);
            index = parent;
        }
    }

    // Просеивание вниз (для удаления)
    private void siftDown(int index) {
        while (leftChild(index) < size) {
            int left = leftChild(index);
            int right = rightChild(index);
            int smallest = left;

            if (right < size && compare(queue[right], queue[left]) < 0) {
                smallest = right;
            }

            if (compare(queue[index], queue[smallest]) <= 0) {
                break;
            }

            swap(index, smallest);
            index = smallest;
        }
    }

    // Обязательные методы

    @Override
    public String toString() {
        // Для совместимости с PriorityQueue нужно выводить в порядке массива
        // PriorityQueue.toString() не гарантирует отсортированный порядок
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(queue[i]);
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
            queue[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }

        // Находим индекс элемента
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (o.equals(queue[i])) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return false;
        }

        removeAt(index);
        return true;
    }

    @SuppressWarnings("unchecked")
    private E removeAt(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        E result = (E) queue[index];
        int s = --size;
        queue[index] = queue[s];
        queue[s] = null;

        if (index != s) {
            siftDown(index);
            if (queue[index] == result) {
                siftUp(index);
            }
        }

        return result;
    }

    @Override
    public E remove() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return poll();
    }

    @Override
    public boolean contains(Object element) {
        if (element == null) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (element.equals(queue[i])) {
                return true;
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
        queue[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E poll() {
        if (isEmpty()) {
            return null;
        }

        int s = --size;
        E result = (E) queue[0];
        E x = (E) queue[s];
        queue[s] = null;
        if (s != 0) {
            queue[0] = x;
            siftDown(0);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E peek() {
        return (size == 0) ? null : (E) queue[0];
    }

    @Override
    public E element() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return peek();
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
        if (c == this) {
            throw new IllegalArgumentException("Cannot add to itself");
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
        // Более эффективная реализация - строим новую кучу
        Object[] newQueue = new Object[queue.length];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (!c.contains(queue[i])) {
                newQueue[newSize++] = queue[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            queue = newQueue;
            size = newSize;
            // Перестраиваем кучу
            for (int i = (size >>> 1) - 1; i >= 0; i--) {
                siftDown(i);
            }
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }

        boolean modified = false;
        // Более эффективная реализация - строим новую кучу
        Object[] newQueue = new Object[queue.length];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (c.contains(queue[i])) {
                newQueue[newSize++] = queue[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            queue = newQueue;
            size = newSize;
            // Перестраиваем кучу
            for (int i = (size >>> 1) - 1; i >= 0; i--) {
                siftDown(i);
            }
        }

        return modified;
    }

    // Методы интерфейса Queue, которые не реализуем полностью

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Iterator not implemented");
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(queue, 0, result, 0, size);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) Arrays.copyOf(queue, size, a.getClass());
        }
        System.arraycopy(queue, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}