package by.it.group451001.romeyko.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    private E[] heap;
    private int size;
    private static final int DEFAULT_CAPACITY = 8;

    public MyPriorityQueue() {
        heap = (E[]) new Comparable[DEFAULT_CAPACITY];
        size = 0;
    }

    // -----------------------------
    // Вспомогательные методы
    // -----------------------------
    private void ensureCapacity() {
        if (size == heap.length) {
            E[] newArr = (E[]) new Comparable[heap.length * 2];
            for (int i = 0; i < heap.length; i++) newArr[i] = heap[i];
            heap = newArr;
        }
    }

    private void swap(int i, int j) {
        E tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    private void heapifyUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap[index].compareTo(heap[parent]) >= 0) break;
            swap(index, parent);
            index = parent;
        }
    }

    private void heapifyDown(int index) {
        for (;;) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;
            if (left < size && heap[left].compareTo(heap[smallest]) < 0) smallest = left;
            if (right < size && heap[right].compareTo(heap[smallest]) < 0) smallest = right;
            if (smallest == index) break;
            swap(index, smallest);
            index = smallest;
        }
    }

    // Построить кучу "с нуля" из текущего массива (элементами 0..size-1)
    private void buildHeap() {
        for (int i = (size / 2) - 1; i >= 0; i--) heapifyDown(i);
    }

    // -----------------------------
    // Обязательные методы
    // -----------------------------
    @Override
    public String toString() {
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
        for (int i = 0; i < size; i++) heap[i] = null;
        size = 0;
    }

    @Override
    public boolean add(E element) { return offer(element); }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean offer(E element) {
        if (element == null) throw new NullPointerException("Null elements not allowed");
        ensureCapacity();
        heap[size] = element;
        heapifyUp(size);
        size++;
        return true;
    }

    @Override
    public E remove() {
        E e = poll();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E root = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        if (size > 0) heapifyDown(0);
        return root;
    }

    @Override
    public E peek() {
        return size == 0 ? null : heap[0];
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return heap[0];
    }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && heap[i] == null) || (o != null && o.equals(heap[i]))) return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) if (!contains(e)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            offer(e);
            modified = true;
        }
        return modified;
    }

    // Удаляем все элементы, которые присутствуют в c.
    // Надёжный способ — собрать оставшиеся элементы и перестроить кучу.
    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null || size == 0) return false;
        int write = 0;
        boolean modified = false;
        for (int read = 0; read < size; read++) {
            Object val = heap[read];
            if (c.contains(val)) {
                modified = true; // пропускаем (удаляем)
            } else {
                heap[write++] = heap[read];
            }
        }
        // очистить хвост
        for (int i = write; i < size; i++) heap[i] = null;
        if (modified) {
            size = write;
            buildHeap();
        }
        return modified;
    }

    // Оставляем только элементы, которые присутствуют в c.
    // Перестроим кучу из оставшихся элементов.
    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null || size == 0) return false;
        int write = 0;
        boolean modified = false;
        for (int read = 0; read < size; read++) {
            Object val = heap[read];
            if (c.contains(val)) {
                heap[write++] = heap[read];
            } else {
                modified = true; // удаляем
            }
        }
        for (int i = write; i < size; i++) heap[i] = null;
        if (modified) {
            size = write;
            buildHeap();
        }
        return modified;
    }

    // -----------------------------
    // Остальные методы интерфейса (необязательные)
    // -----------------------------
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }
}
