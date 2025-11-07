package by.it.group410901.zubchonak.lesson10;

import java.util.*;

@SuppressWarnings("unchecked")
public class MyPriorityQueue<E> implements Queue<E> {

    private Object[] queue;
    private int size;

    public MyPriorityQueue() {
        queue = new Object[11];
        size = 0;
    }

    // === Вспомогательные методы ===

    private int parent(int i) { return (i - 1) >>> 1; }
    private int left(int i) { return (i << 1) + 1; }
    private int right(int i) { return (i << 1) + 2; }

    private void siftUp(int k, E x) {
        while (k > 0) {
            int p = parent(k);
            Comparable<? super E> e = (Comparable<? super E>) queue[p];
            if (e.compareTo(x) <= 0) break;
            queue[k] = queue[p];
            k = p;
        }
        queue[k] = x;
    }

    private void siftDown(int k, E x) {
        int half = size >>> 1;
        while (k < half) {
            int l = left(k);
            int r = l + 1;
            Object child = queue[l];
            if (r < size && ((Comparable<? super E>) child).compareTo((E) queue[r]) > 0)
                child = queue[l = r];
            if (((Comparable<? super E>) x).compareTo((E) child) <= 0) break;
            queue[k] = child;
            k = l;
        }
        queue[k] = x;
    }

    // === Heapify за O(n) ===
    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i, (E) queue[i]);
        }
    }

    // === Обязательные методы ===

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(queue[i]);
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) queue[i] = null;
        size = 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();
        if (size >= queue.length) {
            int newCap = queue.length + (queue.length >> 1);
            Object[] nq = new Object[newCap];
            System.arraycopy(queue, 0, nq, 0, size);
            queue = nq;
        }
        siftUp(size++, e);
        return true;
    }

    @Override
    public E remove() {
        if (size == 0) throw new NoSuchElementException();
        return removeAt(0);
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? queue[i] == null : o.equals(queue[i]))
                return true;
        }
        return false;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        return removeAt(0);
    }

    @Override
    public E peek() {
        return size == 0 ? null : (E) queue[0];
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return (E) queue[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // === Collection методы ===

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        Object[] temp = new Object[size];
        int newSize = 0;
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            Object e = queue[i];
            if (!c.contains(e)) {
                temp[newSize++] = e;
            } else {
                modified = true;
            }
        }
        // Перезаписываем внутренний массив и heapify за O(n)
        queue = new Object[Math.max(11, newSize + (newSize >> 1))];
        System.arraycopy(temp, 0, queue, 0, newSize);
        size = newSize;
        heapify();
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        Object[] temp = new Object[size];
        int newSize = 0;
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            Object e = queue[i];
            if (c.contains(e)) {
                temp[newSize++] = e;
            } else {
                modified = true;
            }
        }
        queue = new Object[Math.max(11, newSize + (newSize >> 1))];
        System.arraycopy(temp, 0, queue, 0, newSize);
        size = newSize;
        heapify();
        return modified;
    }

    // === Обязательный метод из Collection ===
    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    // === Остальные методы Collection ===
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }

    // === Вспомогательный метод удаления по индексу ===
    private E removeAt(int i) {
        E result = (E) queue[i];
        E moved = (E) queue[--size];
        queue[size] = null;
        if (i != size) {
            siftDown(i, moved);
            if (queue[i] == moved) {
                siftUp(i, moved);
            }
        }
        return result;
    }
}