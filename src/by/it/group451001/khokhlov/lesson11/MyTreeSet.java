package by.it.group451001.khokhlov.lesson11;

import java.util.Collection;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {
    private Object[] a;
    private int size;

    public MyTreeSet() {
        a = new Object[10];
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private int compare(Object x, Object y) {
        return ((Comparable<Object>) x).compareTo(y);
    }

    private int findIndex(Object o) {
        int lo = 0, hi = size - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int cmp = compare(a[mid], o);
            if (cmp == 0) return mid;
            if (cmp < 0) lo = mid + 1; else hi = mid - 1;
        }
        return -(lo + 1);
    }

    private void ensureCapacity(int minCapacity) {
        if (a.length >= minCapacity) return;
        int newCap = a.length * 2;
        if (newCap < minCapacity) newCap = minCapacity;
        Object[] na = new Object[newCap];
        System.arraycopy(a, 0, na, 0, size);
        a = na;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(a[i]);
        }
        sb.append(']');
        return sb.toString();
    }

    public int size() {
        return size;
    }

    public void clear() {
        for (int i = 0; i < size; i++) a[i] = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();
        int idx = findIndex(e);
        if (idx >= 0) return false;
        int pos = -idx - 1;
        ensureCapacity(size + 1);
        if (pos < size) System.arraycopy(a, pos, a, pos + 1, size - pos);
        a[pos] = e;
        size++;
        return true;
    }

    public boolean remove(Object o) {
        if (o == null) return false;
        int idx;
        try {
            idx = findIndex(o);
        } catch (ClassCastException ex) {
            return false;
        }
        if (idx < 0) return false;
        int tail = size - idx - 1;
        if (tail > 0) System.arraycopy(a, idx + 1, a, idx, tail);
        a[--size] = null;
        return true;
    }

    public boolean contains(Object o) {
        if (o == null) return false;
        try {
            return findIndex(o) >= 0;
        } catch (ClassCastException ex) {
            return false;
        }
    }

    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            if (add(e)) changed = true;
        }
        return changed;
    }

    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object e : c) {
            if (remove(e)) changed = true;
        }
        return changed;
    }

    public boolean retainAll(Collection<?> c) {
        Object[] na = new Object[a.length];
        int ns = 0;
        for (int i = 0; i < size; i++) {
            Object v = a[i];
            if (c.contains(v)) {
                na[ns++] = v;
            }
        }
        if (ns == size) return false;
        a = na;
        size = ns;
        return true;
    }

    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    public <T> T[] toArray(T[] ts) {
        throw new UnsupportedOperationException();
    }

    public boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    public int hashCode() {
        throw new UnsupportedOperationException();
    }
}