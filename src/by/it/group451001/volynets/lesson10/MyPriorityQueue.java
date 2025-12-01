package by.it.group451001.volynets.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E extends Comparable<? super E>> implements Queue<E> {

    private E[] heap;
    private int size;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        this.heap = (E[]) new Comparable[16]; // начальная ёмкость
        this.size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i + 1 < size) sb.append(", ");
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
        for (int i = 0; i < size; i++) heap[i] = null;
        size = 0;
    }

    // Queue core

    @Override
    public boolean add(E e) {
        offer(e);
        return true;
    }

    @Override
    public boolean offer(E e) {
        if (e == null) throw new NullPointerException();
        ensureCapacity(size + 1);
        heap[size] = e;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E remove() {
        E x = poll();
        if (x == null) throw new NoSuchElementException();
        return x;
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E res = heap[0];
        E last = heap[size - 1];
        heap[size - 1] = null;
        size--;
        if (size > 0) {
            heap[0] = last;
            siftDown(0);
        }
        return res;
    }

    @Override
    public E element() {
        E x = peek();
        if (x == null) throw new NoSuchElementException();
        return x;
    }

    @Override
    public E peek() {
        return size == 0 ? null : heap[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            offer(e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        if (size == 0) return false;
        int write = 0;
        for (int read = 0; read < size; read++) {
            if (!containsInCollection(c, heap[read])) {
                heap[write++] = heap[read];
            } else {
                changed = true;
            }
        }
        for (int i = write; i < size; i++) heap[i] = null;
        size = write;
        heapify();
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        if (size == 0) return false;
        int write = 0;
        for (int read = 0; read < size; read++) {
            if (containsInCollection(c, heap[read])) {
                heap[write++] = heap[read];
            } else {
                changed = true;
            }
        }
        for (int i = write; i < size; i++) heap[i] = null;
        size = write;
        heapify();
        return changed;
    }

    private void siftUp(int i) {
        int idx = i;
        E x = heap[idx];
        while (idx > 0) {
            int p = (idx - 1) >>> 1;
            E parent = heap[p];
            if (x.compareTo(parent) >= 0) break;
            heap[idx] = parent;
            idx = p;
        }
        heap[idx] = x;
    }

    private void siftDown(int i) {
        int idx = i;
        int half = size >>> 1; // узлы с индексом >= half — листья
        E x = heap[idx];
        while (idx < half) {
            int left = (idx << 1) + 1;
            int right = left + 1;
            int smallest = left;
            if (right < size && heap[right].compareTo(heap[left]) < 0) {
                smallest = right;
            }
            if (heap[smallest].compareTo(x) >= 0) break;
            heap[idx] = heap[smallest];
            idx = smallest;
        }
        heap[idx] = x;
    }

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity(int minCapacity) {
        int oldCap = heap.length;
        if (minCapacity <= oldCap) return;
        int newCap = oldCap == 0 ? 16 : oldCap << 1;
        if (newCap < minCapacity) newCap = minCapacity;
        E[] newArr = (E[]) new Comparable[newCap];
        for (int i = 0; i < size; i++) newArr[i] = heap[i];
        heap = newArr;
    }

    private boolean eq(E a, E b) {
        return a == b || (a != null && a.equals(b));
    }

    private boolean containsInCollection(Collection<?> c, Object x) {
        for (Object o : c) {
            if (o == x || (o != null && o.equals(x))) return true;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        if (size == 0) return false;
        for (int i = 0; i < size; i++) {
            if (o == heap[i] || (o != null && o.equals(heap[i]))) return true;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        if (size == 0) return false;
        int idx = -1;
        for (int i = 0; i < size; i++) {
            if (o == heap[i] || (o != null && o.equals(heap[i]))) {
                idx = i;
                break;
            }
        }
        if (idx == -1) return false;

        E last = heap[size - 1];
        heap[size - 1] = null;
        size--;
        if (idx < size) {
            heap[idx] = last;
            // Восстанавливаем кучу: сначала вниз, затем вверх
            siftDown(idx);
            siftUp(idx);
        }
        return true;
    }

    // Остальные методы можно оставить заглушками, если тесты их не используют

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean removeIf(java.util.function.Predicate<? super E> filter) { throw new UnsupportedOperationException(); }
    @Override public boolean equals(Object o) { return super.equals(o); }
    @Override public int hashCode() { return super.hashCode(); }
}