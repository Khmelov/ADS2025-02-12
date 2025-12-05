package by.it.group410902.linnik.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyTreeSet<E extends Comparable<E>> implements Set<E> {

    private E[] elements;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    public MyTreeSet() {
        elements = (E[]) new Comparable[DEFAULT_CAPACITY];
        size = 0;
    }

    private void ensureCapacity() {
        if (size >= elements.length) {
            E[] newElements = (E[]) new Comparable[elements.length * 2];
            for (int i = 0; i < size; i++) newElements[i] = elements[i];
            elements = newElements;
        }
    }

    private int binarySearch(E element) {
        int left = 0;
        int right = size - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = element.compareTo(elements[mid]);
            if (cmp == 0) return mid;
            else if (cmp < 0) right = mid - 1;
            else left = mid + 1;
        }
        return -left - 1;
    }

    @Override
    public boolean add(E e) {
        ensureCapacity();
        int index = binarySearch(e);
        if (index >= 0) return false;
        int insertPos = -index - 1;
        for (int i = size; i > insertPos; i--) elements[i] = elements[i - 1];
        elements[insertPos] = e;
        size++;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        try {
            E e = (E) o;
            return binarySearch(e) >= 0;
        } catch (ClassCastException ex) {
            return false;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        try {
            E e = (E) o;
            int index = binarySearch(e);
            if (index < 0) return false;
            for (int i = index; i < size - 1; i++) elements[i] = elements[i + 1];
            elements[size - 1] = null;
            size--;
            return true;
        } catch (ClassCastException ex) {
            return false;
        }
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) elements[i] = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size - 1; i++) sb.append(elements[i]).append(", ");
        sb.append(elements[size - 1]).append("]");
        return sb.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) changed |= add(e);
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) changed |= remove(o);
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[i])) {
                remove(elements[i]);
                changed = true;
            }
        }
        return changed;
    }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}
