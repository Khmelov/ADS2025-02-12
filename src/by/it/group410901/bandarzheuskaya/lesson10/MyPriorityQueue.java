package by.it.group410901.bandarzheuskaya.lesson10;

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
        this.comparator = null;
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(Comparator<? super E> comparator) {
        this.heap = (E[]) new Object[DEFAULT_CAPACITY];
        this.comparator = comparator;
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(Collection<? extends E> c) {
        this.comparator = null;
        this.heap = (E[]) new Object[Math.max(c.size(), DEFAULT_CAPACITY)];
        addAll(c);
    }

    // Вспомогательные методы для работы с кучей
    private void ensureCapacity() {
        if (size == heap.length) {
            @SuppressWarnings("unchecked")
            E[] newHeap = (E[]) new Object[heap.length * 2];
            System.arraycopy(heap, 0, newHeap, 0, size);
            heap = newHeap;
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

    private void heapifyUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (compare(heap[index], heap[parent]) >= 0) {
                break;
            }
            swap(index, parent);
            index = parent;
        }
    }

    private void heapifyDown(int index) {
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

    // Основные методы Queue
    @Override
    public String toString() {
        if (size == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E element) { return offer(element); }

    @Override
    public E remove() {
        if (size == 0) throw new NoSuchElementException();
        return poll();
    }

    @Override
    public boolean contains(Object element) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(element, heap[i])) return true;
        }
        return false;
    }

    @Override
    public boolean offer(E element) {
        if (element == null) throw new NullPointerException();
        ensureCapacity();
        heap[size] = element;
        heapifyUp(size);
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        return removeAt(0);
    }

    @Override
    public E peek() { return (size == 0) ? null : heap[0]; }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return heap[0];
    }

    @Override
    public boolean isEmpty() { return size == 0; }

    private E removeAt(int index) {
        E removed = heap[index];
        heap[index] = heap[size - 1];
        heap[size - 1] = null;
        size--;

        if (index < size) {
            heapifyDown(index);
            if (index > 0 && compare(heap[index], heap[(index - 1) / 2]) < 0) {
                heapifyUp(index);
            }
        }
        return removed;
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

    // Вспомогательный метод для проверки наличия элемента в коллекции
    private boolean containsInCollection(Collection<?> c, Object element) {
        for (Object e : c) {
            if (Objects.equals(e, element)) {
                return true;
            }
        }
        return false;
    }

    // Оптимизированные методы bulk operations за O(n)
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c) {
            if (offer(element)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // Оптимизированная версия за O(n*m), но без использования HashSet
        if (c.isEmpty() || size == 0) return false;

        // Строим новую кучу только из элементов, которые нужно оставить
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (!containsInCollection(c, heap[i])) {
                heap[newSize++] = heap[i];
            }
        }

        boolean modified = newSize != size;
        size = newSize;

        // Перестраиваем кучу за O(n)
        if (size > 0) {
            for (int i = (size - 1) / 2; i >= 0; i--) {
                heapifyDown(i);
            }
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // Оптимизированная версия за O(n*m), но без использования HashSet
        if (size == 0) return false;
        if (c.isEmpty()) {
            clear();
            return true;
        }

        // Строим новую кучу только из элементов, которые нужно оставить
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (containsInCollection(c, heap[i])) {
                heap[newSize++] = heap[i];
            }
        }

        boolean modified = newSize != size;
        size = newSize;

        // Перестраиваем кучу за O(n)
        if (size > 0) {
            for (int i = (size - 1) / 2; i >= 0; i--) {
                heapifyDown(i);
            }
        }

        return modified;
    }

    // Итератор и остальные методы
    @Override
    public Iterator<E> iterator() {
        return new PriorityQueueIterator();
    }

    private class PriorityQueueIterator implements Iterator<E> {
        private int currentIndex = 0;
        private int lastReturned = -1;

        @Override
        public boolean hasNext() { return currentIndex < size; }

        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            lastReturned = currentIndex;
            return heap[currentIndex++];
        }

        @Override
        public void remove() {
            if (lastReturned == -1) throw new IllegalStateException();
            removeAt(lastReturned);
            currentIndex--;
            lastReturned = -1;
        }
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(heap, 0, result, 0, size);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        System.arraycopy(heap, 0, a, 0, size);
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Queue)) return false;
        Queue<?> other = (Queue<?>) o;
        if (size != other.size()) return false;

        // Создаем копии для сравнения
        MyPriorityQueue<E> copy1 = new MyPriorityQueue<>(this);
        Queue<?> copy2 = createCopy(other);

        while (!copy1.isEmpty() && !copy2.isEmpty()) {
            if (!Objects.equals(copy1.poll(), copy2.poll())) {
                return false;
            }
        }
        return copy1.isEmpty() && copy2.isEmpty();
    }

    private Queue<?> createCopy(Queue<?> queue) {
        // Простая реализация создания копии через массив
        Object[] elements = queue.toArray();
        MyPriorityQueue<Object> copy = new MyPriorityQueue<>();
        for (Object element : elements) {
            copy.offer(element);
        }
        return copy;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (E element : this) {
            result = 31 * result + (element == null ? 0 : element.hashCode());
        }
        return result;
    }
}