package by.it.group451001.drzhevetskiy.lesson11;

import java.util.Collection;
import java.util.Set;

public class MyTreeSet<E extends Comparable<E>> implements Set<E> {

    private E[] data;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        data = (E[]) new Comparable[DEFAULT_CAPACITY];
        size = 0;
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) data[i] = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size < data.length) return;
        E[] newArr = (E[]) new Comparable[data.length * 2];
        System.arraycopy(data, 0, newArr, 0, size);
        data = newArr;
    }

    private int binarySearch(E element) {
        int l = 0, r = size - 1;

        while (l <= r) {
            int mid = (l + r) >>> 1;
            int cmp = element.compareTo(data[mid]);

            if (cmp == 0) return mid;
            if (cmp < 0) r = mid - 1;
            else l = mid + 1;
        }

        return -(l + 1);
    }

    @Override
    public boolean contains(Object o) {
        try {
            @SuppressWarnings("unchecked")
            E elem = (E) o;
            return binarySearch(elem) >= 0;
        } catch (ClassCastException ex) {
            return false;
        }
    }

    @Override
    public boolean add(E element) {
        int pos = binarySearch(element);

        if (pos >= 0) return false;

        pos = -(pos + 1);

        ensureCapacity();

        System.arraycopy(data, pos, data, pos + 1, size - pos);
        data[pos] = element;

        size++;
        return true;
    }


    @Override
    public boolean remove(Object o) {
        try {
            @SuppressWarnings("unchecked")
            E elem = (E) o;

            int pos = binarySearch(elem);
            if (pos < 0) return false;

            System.arraycopy(data, pos + 1, data, pos, size - pos - 1);

            data[size - 1] = null;
            size--;
            return true;

        } catch (ClassCastException ex) {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(data[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object el : c)
            if (!contains(el)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E el : c)
            if (add(el)) changed = true;
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object el : c)
            while (remove(el)) changed = true;
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;

        for (int i = 0; i < size; ) {
            if (!c.contains(data[i])) {
                remove(data[i]);
                changed = true;
            } else {
                i++;
            }
        }

        return changed;
    }

    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }
}

