package by.it.group410901.zaverach.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E extends Comparable<E>> implements Set<E> {

    private Object[] elements;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public MyTreeSet() {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    private void ensureCapacity() {
        if (size >= elements.length) {
            Object[] newArr = new Object[elements.length * 2];
            System.arraycopy(elements, 0, newArr, 0, size);
            elements = newArr;
        }
    }

    private int findInsertIndex(E e) {
        int left = 0, right = size - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = e.compareTo((E) elements[mid]);
            if (cmp == 0) return mid;
            if (cmp < 0) right = mid - 1;
            else left = mid + 1;
        }
        return left;
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();
        int index = findInsertIndex(e);

        if (index < size && e.compareTo((E) elements[index]) == 0)
            return false;

        ensureCapacity();

        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = e;
        size++;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        E e = (E) o;

        int left = 0, right = size - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = e.compareTo((E) elements[mid]);
            if (cmp == 0) return true;
            if (cmp < 0) right = mid - 1;
            else left = mid + 1;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null || size == 0) return false;
        E e = (E) o;
        int left = 0, right = size - 1;

        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = e.compareTo((E) elements[mid]);
            if (cmp == 0) {
                System.arraycopy(elements, mid + 1, elements, mid, size - mid - 1);
                elements[--size] = null;
                return true;
            }
            if (cmp < 0) right = mid - 1;
            else left = mid + 1;
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) elements[i] = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (!contains(o))
                return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c)
            if (add(e))
                modified = true;
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c)
            if (remove(o))
                modified = true;
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        int newSize = 0;

        Object[] newArr = new Object[elements.length];
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                newArr[newSize++] = elements[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            elements = newArr;
            size = newSize;
        }

        return modified;
    }

    // Необязательные методы интерфейса
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean equals(Object o) { return super.equals(o); }
    @Override public int hashCode() { return super.hashCode(); }
}
