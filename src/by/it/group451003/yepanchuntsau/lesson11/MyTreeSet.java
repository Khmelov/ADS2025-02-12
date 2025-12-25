package by.it.group451003.yepanchuntsau.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E extends Comparable<? super E>> implements Set<E> {

    private Object[] a = new Object[16];
    private int size = 0;

    private void ensureCapacity(int need) {
        if (need <= a.length) return;
        int newCap = a.length + (a.length >> 1) + 1; // ~1.5x + 1
        if (newCap < need) newCap = need;
        Object[] b = new Object[newCap];
        System.arraycopy(a, 0, b, 0, size);
        a = b;
    }

    @SuppressWarnings("unchecked")
    private E elem(int i) { return (E) a[i]; }

    private int findIndexE(E key) {
        int lo = 0, hi = size - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int cmp = elem(mid).compareTo(key);
            if (cmp < 0) lo = mid + 1;
            else if (cmp > 0) hi = mid - 1;
            else return mid;
        }
        return -(lo + 1);
    }

    @SuppressWarnings("unchecked")
    private int findIndexObj(Object key) {
        if (key == null) return -1;
        int lo = 0, hi = size - 1;
        try {
            E k = (E) key;
            while (lo <= hi) {
                int mid = (lo + hi) >>> 1;
                int cmp = elem(mid).compareTo(k);
                if (cmp < 0) lo = mid + 1;
                else if (cmp > 0) hi = mid - 1;
                else return mid;
            }
            return -1;
        } catch (ClassCastException ex) {
            return -1;
        }
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            sb.append(a[i]);
            if (i + 1 < size) sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) a[i] = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException("null not allowed");
        int idx = findIndexE(e);
        if (idx >= 0) return false;
        int ins = -idx - 1;  
        ensureCapacity(size + 1);
        int moved = size - ins;
        if (moved > 0) System.arraycopy(a, ins, a, ins + 1, moved);
        a[ins] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = findIndexObj(o);
        if (idx < 0) return false;
        int moved = size - idx - 1;
        if (moved > 0) System.arraycopy(a, idx + 1, a, idx, moved);
        a[--size] = null;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return findIndexObj(o) >= 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object x : c) if (!contains(x)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E x : c) if (add(x)) changed = true;
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object x : c) if (remove(x)) changed = true;
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            Object e = a[i];
            if (c.contains(e)) {
                a[newSize++] = e;
            } else {
                changed = true;
            }
        }
        for (int i = newSize; i < size; i++) a[i] = null;
        size = newSize;
        return changed;
    }


    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean equals(Object o) { return super.equals(o); }
    @Override public int hashCode() { return super.hashCode(); }
}
