package by.it.group410901.meshcheryakovegor.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

@SuppressWarnings("unchecked")
public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {
    private E[] heap;
    private int size;

    public MyPriorityQueue() {
        heap = (E[]) new Comparable[16];
        size = 0;
    }

    private void ensureCapacity() {
        if (size == heap.length) {
            E[] newHeap = (E[]) new Comparable[heap.length * 2];
            System.arraycopy(heap, 0, newHeap, 0, size);
            heap = newHeap;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) heap[i] = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // ===== ДОБАВЛЕНИЕ =====
    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public boolean offer(E element) {
        ensureCapacity();
        heap[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    private void siftUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (heap[i].compareTo(heap[parent]) >= 0) break;
            swap(i, parent);
            i = parent;
        }
    }

    // ===== УДАЛЕНИЕ =====
    @Override
    public E remove() {
        E result = poll();
        if (result == null) throw new NoSuchElementException();
        return result;
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E result = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        siftDown(0);
        return result;
    }

    private void siftDown(int i) {
        while (true) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = i;

            if (left < size && heap[left].compareTo(heap[smallest]) < 0) {
                smallest = left;
            }
            if (right < size && heap[right].compareTo(heap[smallest]) < 0) {
                smallest = right;
            }
            if (smallest == i) break;
            swap(i, smallest);
            i = smallest;
        }
    }

    private void swap(int i, int j) {
        E tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    // ===== ДОСТУП =====
    @Override
    public E peek() {
        return (size == 0) ? null : heap[0];
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return heap[0];
    }

    // ===== ПРОВЕРКА =====
    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && heap[i] == null) || (o != null && o.equals(heap[i]))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false;
        }
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

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                heap[newSize++] = heap[i]; // сохраняем элемент
            } else {
                modified = true;
            }
        }
        // очистить хвост
        for (int i = newSize; i < size; i++) {
            heap[i] = null;
        }
        size = newSize;
        heapify(); // перестроить кучу
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                heap[newSize++] = heap[i]; // оставляем только те, что есть в c
            } else {
                modified = true;
            }
        }
        // очистить хвост
        for (int i = newSize; i < size; i++) {
            heap[i] = null;
        }
        size = newSize;
        heapify(); // перестроить кучу
        return modified;
    }


    private boolean removeElement(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && heap[i] == null) || (o != null && o.equals(heap[i]))) {
                heap[i] = heap[size - 1];
                heap[size - 1] = null;
                size--;
                siftDown(i);
                siftUp(i);
                return true;
            }
        }
        return false;
    }
    @SuppressWarnings("unchecked")
    private void heapify() {
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    // ===== ВСПОМОГАТЕЛЬНОЕ =====
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

    // ===== Заглушки для лишних методов Queue/Collection =====
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean remove(Object o) { return removeElement(o); }
}

