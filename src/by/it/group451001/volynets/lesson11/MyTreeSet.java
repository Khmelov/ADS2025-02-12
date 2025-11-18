package by.it.group451001.volynets.lesson11;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private E[] elements;
    private int size;
    private final Comparator<? super E> comparator;

    private static final int DEFAULT_CAPACITY = 10;

    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = null;
    }

    @SuppressWarnings("unchecked")
    public MyTreeSet(Comparator<? super E> comparator) {
        this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = comparator;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) elements[i] = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        }
        if (a == null || b == null) {
            throw new NullPointerException("Null elements are not supported without explicit comparator");
        }
        @SuppressWarnings("unchecked")
        Comparable<? super E> ca = (Comparable<? super E>) a;
        return ca.compareTo(b);
    }

    private int binarySearch(E key) {
        int lo = 0, hi = size - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int cmp = compare(elements[mid], key);
            if (cmp < 0) {
                lo = mid + 1;
            } else if (cmp > 0) {
                hi = mid - 1;
            } else {
                return mid;
            }
        }
        return -(lo + 1);
    }

    private void ensureCapacity(int min) {
        if (elements.length >= min) return;
        int newCap = Math.max(elements.length + (elements.length >> 1) + 1, min);
        @SuppressWarnings("unchecked")
        E[] newArr = (E[]) new Object[newCap];
        for (int i = 0; i < size; i++) newArr[i] = elements[i];
        elements = newArr;
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        E key = (E) o;
        if (size == 0) return false;
        int idx = binarySearch(key);
        return idx >= 0;
    }

    @Override
    public boolean add(E e) {
        int idx;
        if (size == 0) {
            ensureCapacity(1);
            elements[0] = e;
            size = 1;
            return true;
        } else {
            idx = binarySearch(e);
            if (idx >= 0) return false; // уже есть
            int ins = -idx - 1;
            ensureCapacity(size + 1);
            // сдвиг вправо
            for (int i = size; i > ins; i--) elements[i] = elements[i - 1];
            elements[ins] = e;
            size++;
            return true;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (size == 0) return false;
        @SuppressWarnings("unchecked")
        E key = (E) o;
        int idx = binarySearch(key);
        if (idx < 0) return false;
        // сдвиг влево
        int numMoved = size - idx - 1;
        if (numMoved > 0) {
            for (int i = 0; i < numMoved; i++) {
                elements[idx + i] = elements[idx + i + 1];
            }
        }
        elements[--size] = null;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object x : c) {
            if (!contains(x)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E x : c) {
            if (add(x)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        // Эффективно: пройти по входной коллекции и удалять по одному
        for (Object x : c) {
            if (remove(x)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        // Пройдём по массиву, удаляя те, которых нет в c
        int write = 0;
        for (int read = 0; read < size; read++) {
            E val = elements[read];
            if (collectionContains(c, val)) {
                elements[write++] = val;
            } else {
                changed = true;
            }
        }
        // Зачистка хвоста
        for (int i = write; i < size; i++) elements[i] = null;
        size = write;
        return changed;
    }

    private boolean collectionContains(Collection<?> c, Object key) {
        for (Object x : c) {
            if (x == key || (x != null && x.equals(key))) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(elements[i]);
        }
        sb.append(']');
        return sb.toString();
    }

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException();}
    @Override public boolean equals(Object o) { return super.equals(o);}
    @Override public int hashCode() { return super.hashCode();}
}