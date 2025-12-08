package by.it.group410901.kovalevich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyTreeSet<E extends Comparable<E>> implements Set<E> {

    private E[] data;
    private int size;

    public MyTreeSet() {
        data = (E[]) new Comparable[8];
        size = 0;
    }


    private void ensureCapacity(int need) {
        if (need <= data.length) return;
        int cap = data.length;
        while (cap < need) cap <<= 1;
        E[] nd = (E[]) new Comparable[cap];
        System.arraycopy(data, 0, nd, 0, size);
        data = nd;
    }

    // бинарный поиск
    private int binSearch(Object o) {
        if (o == null) throw new NullPointerException("MyTreeSet does not allow null");
        E key = (E) o;
        int l = 0, r = size - 1;
        while (l <= r) {
            int m = (l + r) >>> 1;
            int cmp = data[m].compareTo(key);
            if (cmp < 0) l = m + 1;
            else if (cmp > 0) r = m - 1;
            else return m;
        }
        return -(l + 1);
    }

    @Override public int size() { return size; }
    @Override public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) data[i] = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false; // как у TreeSet с natural ordering
        return binSearch(o) >= 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException("MyTreeSet does not allow null");
        int p = binSearch(e);
        if (p >= 0) return false;
        int ins = -(p + 1);                 // вставка
        ensureCapacity(size + 1);
        // сдвиг вправо [ins..size-1]
        if (ins < size) System.arraycopy(data, ins, data, ins + 1, size - ins);
        data[ins] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        int p = binSearch(o);
        if (p < 0) return false;
        int numMoved = size - p - 1;
        if (numMoved > 0) System.arraycopy(data, p + 1, data, p, numMoved);
        data[--size] = null;
        return true;
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
        int i = 0;
        while (i < size) {
            if (c.contains(data[i])) {
                remove(data[i]);
                changed = true;
            } else {
                i++;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        int i = 0;
        while (i < size) {
            if (!c.contains(data[i])) {
                remove(data[i]);
                changed = true;
            } else {
                i++;
            }
        }
        return changed;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(data[i]);
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int idx = 0;
            @Override public boolean hasNext() { return idx < size; }
            @Override public E next() {
                if (idx >= size) throw new NoSuchElementException();
                return data[idx++];
            }
            @Override public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] a = new Object[size];
        System.arraycopy(data, 0, a, 0, size);
        return a;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        int n = size;
        T[] out = a.length >= n ? a
                : (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), n);
        for (int i = 0; i < n; i++) out[i] = (T) data[i];
        if (out.length > n) out[n] = null;
        return out;
    }

}
