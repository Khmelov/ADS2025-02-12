package by.it.group451001.puzik.lesson10;

import java.util.*;

@SuppressWarnings("unchecked")
public class MyPriorityQueue<E> implements Queue<E> {

    private Object[] heap = new Object[8];
    private int size = 0;
    private Comparator<? super E> comparator;

    public MyPriorityQueue() { this(null); }
    public MyPriorityQueue(Comparator<? super E> comparator) { this.comparator = comparator; }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append('[');
        for (int i = 0; i < size; i++) {
            if (i > 0) out.append(", ");
            out.append(heap[i]);
        }
        out.append(']');
        return out.toString();
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() { Arrays.fill(heap, 0, size, null); size = 0; }

    @Override
    public boolean add(E e) { return offer(e); }

    @Override
    public boolean offer(E e) {
        Objects.requireNonNull(e);
        ensureCapacity(size + 1);
        heap[size] = e;
        siftUp(size++);
        return true;
    }

    @Override
    public E remove() {
        E v = poll();
        if (v == null) throw new NoSuchElementException();
        return v;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) if (Objects.equals(heap[i], o)) return true;
        return false;
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E result = (E) heap[0];
        E last = (E) heap[--size];
        heap[size] = null;
        if (size > 0) {
            heap[0] = last;
            siftDown(0);
        }
        return result;
    }

    @Override
    public E peek() { return size == 0 ? null : (E) heap[0]; }

    @Override
    public E element() { E v = peek(); if (v==null) throw new NoSuchElementException(); return v; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean containsAll(Collection<?> c) { for (Object o:c) if (!contains(o)) return false; return true; }

    @Override
    public boolean addAll(Collection<? extends E> c) { boolean ch=false; for(E e:c){ offer(e); ch=true;} return ch; }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        int w = 0;
        for (int r = 0; r < size; r++) {
            Object el = heap[r];
            if (!c.contains(el)) heap[w++] = el; else changed = true;
        }
        Arrays.fill(heap, w, size, null);
        size = w;
        heapify();
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        int w = 0;
        for (int r = 0; r < size; r++) {
            Object el = heap[r];
            if (c.contains(el)) heap[w++] = el; else changed = true;
        }
        Arrays.fill(heap, w, size, null);
        size = w;
        heapify();
        return changed;
    }

    // --------------- helpers ---------------
    private void ensureCapacity(int minCapacity) {
        if (heap.length >= minCapacity) return;
        int newCap = heap.length * 2;
        if (newCap < minCapacity) newCap = minCapacity;
        heap = Arrays.copyOf(heap, newCap);
    }

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) siftDown(i);
    }

    private void siftUp(int idx) {
        Object x = heap[idx];
        while (idx > 0) {
            int p = (idx - 1) >>> 1;
            Object parent = heap[p];
            if (compare((E) x, (E) parent) >= 0) break;
            heap[idx] = parent;
            idx = p;
        }
        heap[idx] = x;
    }

    private void siftDown(int idx) {
        Object x = heap[idx];
        int half = size >>> 1;
        while (idx < half) {
            int left = (idx << 1) + 1;
            int right = left + 1;
            int smallest = left;
            Object child = heap[left];
            if (right < size && compare((E) heap[right], (E) child) < 0) {
                smallest = right;
                child = heap[right];
            }
            if (compare((E) x, (E) child) <= 0) break;
            heap[idx] = child;
            idx = smallest;
        }
        heap[idx] = x;
    }

    private int compare(E a, E b) {
        if (comparator != null) return comparator.compare(a, b);
        return ((Comparable<? super E>) a).compareTo(b);
    }

    // ---- Boilerplate unsupported ----
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { return Arrays.copyOf(heap, size); }
    @Override public <T> T[] toArray(T[] a) { if (a.length<size) a=Arrays.copyOf(a,size); for(int i=0;i<size;i++){ a[i]=(T)heap[i]; } if(a.length>size) a[size]=null; return a; }
}


