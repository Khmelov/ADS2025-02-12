package by.it.group410902.harkavy.lesson10;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    @SuppressWarnings("unchecked")
    private E[] a = (E[]) new Object[8]; // массив-куча (min-heap)
    private int size = 0;

    private int parent(int i) { return (i - 1) / 2; }
    private int left(int i)   { return 2 * i + 1; }
    private int right(int i)  { return 2 * i + 2; }

    @SuppressWarnings("unchecked")
    private int cmp(E x, E y) { return ((Comparable<? super E>) x).compareTo(y); }

    @SuppressWarnings("unchecked")
    private void growIfNeeded() {
        if (size < a.length) return;
        E[] b = (E[]) new Object[a.length << 1];
        for (int i = 0; i < size; i++) b[i] = a[i];
        a = b;
    }

    // поднимаем узел вверх
    private void siftUp(int i) {
        while (i > 0) {
            int p = parent(i);
            if (cmp(a[i], a[p]) >= 0) break;
            E t = a[i]; a[i] = a[p]; a[p] = t;
            i = p;
        }
    }

    // опускаем узел вниз
    private void siftDown(int i) {
        while (true) {
            int l = left(i), r = right(i), m = i;
            if (l < size && cmp(a[l], a[m]) < 0) m = l;
            if (r < size && cmp(a[r], a[m]) < 0) m = r;
            if (m == i) break;
            E t = a[i]; a[i] = a[m]; a[m] = t;
            i = m;
        }
    }

    // линейная фильтрация + один общий heapify
    private boolean rebuildKeepIf(boolean keepMatches, Collection<?> c) {
        int old = size, k = 0;
        for (int i = 0; i < size; i++) {
            E e = a[i];
            boolean match = c.contains(e);
            if ((keepMatches && match) || (!keepMatches && !match)) {
                a[k++] = e; // переносим нужные элементы в начало
            }
        }
        for (int i = k; i < old; i++) a[i] = null; // чистим хвост
        size = k;
        for (int i = (size >>> 1) - 1; i >= 0; i--) siftDown(i); // heapify за O(n)
        return k != old;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(a[i]);
            if (i + 1 < size) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    @Override public int size() { return size; }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) a[i] = null;
        size = 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();
        growIfNeeded();
        a[size] = e;     // вставляем в конец
        siftUp(size);    // восстанавливаем кучу
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
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? a[i] == null : o.equals(a[i])) return true;
        }
        return false;
    }

    @Override public boolean offer(E e) { return add(e); }

    @Override
    public E poll() {
        if (size == 0) return null;
        E res = a[0];            // минимальный элемент
        a[0] = a[size - 1];
        a[size - 1] = null;
        size--;
        if (size > 0) siftDown(0);
        return res;
    }

    @Override public E peek() { return size == 0 ? null : a[0]; }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return a[0];
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
        // удаляем все, что содержится в c: keepMatches=false
        return rebuildKeepIf(false, c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // оставляем только то, что содержится в c: keepMatches=true
        return rebuildKeepIf(true, c);
    }

    private void unsupported() { throw new UnsupportedOperationException("Not required by the assignment"); }
    @Override public java.util.Iterator<E> iterator() { unsupported(); return null; }
    @Override public Object[] toArray() { unsupported(); return null; }
    @Override public <T> T[] toArray(T[] arr) { unsupported(); return null; }
    @Override public boolean remove(Object o) { unsupported(); return false; }
}
