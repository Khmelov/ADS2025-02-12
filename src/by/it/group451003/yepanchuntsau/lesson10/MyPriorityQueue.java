package by.it.group451003.yepanchuntsau.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E extends Comparable<? super E>> implements Queue<E> {

    private Object[] heap = new Object[8];
    private int size = 0;

    private void checkNotNull(E e) {
        if (e == null) throw new NullPointerException("null elements not allowed");
    }

    private void ensureCapacity(int need) {
        if (need <= heap.length) return;
        int newCap = heap.length + (heap.length >> 1) + 1; // ~1.5x + 1
        if (newCap < need) newCap = need;
        Object[] nh = new Object[newCap];
        System.arraycopy(heap, 0, nh, 0, size);
        heap = nh;
    }

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            sink(i);
        }
    }


    @SuppressWarnings("unchecked")
    private E at(int i) { return (E) heap[i]; }

    @SuppressWarnings("unchecked")
    private int cmp(int i, int j) { return ((Comparable<? super E>) heap[i]).compareTo((E) heap[j]); }

    private void swap(int i, int j) {
        Object t = heap[i]; heap[i] = heap[j]; heap[j] = t;
    }

    private void swim(int i) {
        while (i > 0) {
            int p = (i - 1) >>> 1;
            if (cmp(i, p) >= 0) break;
            swap(i, p);
            i = p;
        }
    }

    private void sink(int i) {
        int n = size;
        while (true) {
            int l = (i << 1) + 1;
            if (l >= n) break;
            int r = l + 1;
            int s = (r < n && cmp(r, l) < 0) ? r : l;
            if (cmp(i, s) <= 0) break;
            swap(i, s);
            i = s;
        }
    }

    private E removeAt(int idx) {
        if (idx < 0 || idx >= size) return null;
        E removed = at(idx);
        int last = --size;
        Object moved = heap[last];
        heap[last] = null;

        if (idx != last) {
            heap[idx] = moved;
            sink(idx);
            if (heap[idx] == moved) swim(idx);
        }
        return removed;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i + 1 < size) sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }

    @Override public int size() { return size; }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) heap[i] = null;
        size = 0;
    }

    @Override
    public boolean add(E e) {
        checkNotNull(e);
        ensureCapacity(size + 1);
        heap[size] = e;
        swim(size);
        size++;
        return true;
    }

    @Override
    public E remove() {
        E x = poll();
        if (x == null) throw new NoSuchElementException();
        return x;
    }

    public boolean contains(E element) { return contains((Object) element); }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? heap[i] == null : o.equals(heap[i])) return true;
        }
        return false;
    }

    @Override
    public boolean offer(E e) { return add(e); }

    @Override
    public E poll() {
        if (size == 0) return null;
        return removeAt(0);
    }

    @Override
    public E peek() { return size == 0 ? null : at(0); }

    @Override
    public E element() {
        E x = peek();
        if (x == null) throw new NoSuchElementException();
        return x;
    }

    @Override public boolean isEmpty() { return size == 0; }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object x : c) if (!contains(x)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E x : c) { add(x); changed = true; }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            Object e = heap[i];
            if (!c.contains(e)) {
                heap[newSize++] = e;
            } else {
                changed = true;
            }
        }
        for (int i = newSize; i < size; i++) heap[i] = null;
        size = newSize;
        heapify();
        return changed;
    }


    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            Object e = heap[i];
            if (c.contains(e)) {
                heap[newSize++] = e;
            } else {
                changed = true;
            }
        }
        for (int i = newSize; i < size; i++) heap[i] = null;
        size = newSize;
        heapify();
        return changed;
    }


    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int idx = 0;
            int last = -1;
            @Override public boolean hasNext() { return idx < size; }
            @Override public E next() { last = idx; return at(idx++); }
            @Override public void remove() {
                if (last < 0) throw new IllegalStateException();
                removeAt(last);
                idx = last;
                last = -1;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] a = new Object[size];
        System.arraycopy(heap, 0, a, 0, size);
        return a;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array
                    .newInstance(a.getClass().getComponentType(), size);
        }
        for (int i = 0; i < size; i++) a[i] = (T) heap[i];
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? heap[i] == null : o.equals(heap[i])) { removeAt(i); return true; }
        }
        return false;
    }
}
