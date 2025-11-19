package by.it.group410901.zubchonak.lesson11;

import java.util.*;

@SuppressWarnings("unchecked")
public class MyTreeSet<E> implements Set<E> {

    private static final int INITIAL_CAPACITY = 10;
    private Object[] elements;
    private int size = 0;

    public MyTreeSet() {
        elements = new Object[INITIAL_CAPACITY];
    }

    private int binarySearch(Object key) {
        int low = 0, high = size - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            Comparable<Object> midVal = (Comparable<Object>) elements[mid];
            int cmp = midVal.compareTo(key);
            if (cmp < 0) low = mid + 1;
            else if (cmp > 0) high = mid - 1;
            else return mid;
        }
        return -(low + 1);
    }

    private void ensureCapacity() {
        if (size >= elements.length) {
            Object[] newElements = new Object[elements.length * 2];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
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
    public boolean contains(Object o) {
        if (o == null) return false;
        return binarySearch(o) >= 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null) return false;
        int pos = binarySearch(e);
        if (pos >= 0) return false; // уже есть

        int insertIndex = -(pos + 1);
        ensureCapacity();
        System.arraycopy(elements, insertIndex, elements, insertIndex + 1, size - insertIndex);
        elements[insertIndex] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        int pos = binarySearch(o);
        if (pos < 0) return false;

        System.arraycopy(elements, pos + 1, elements, pos, size - pos - 1);
        elements[--size] = null;
        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(elements[i]);
        }
        return sb.append("]").toString();
    }

    // === Collection methods ===
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        Object[] toRemove = new Object[size];
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                toRemove[count++] = elements[i];
            }
        }
        for (int i = 0; i < count; i++) {
            if (remove(toRemove[i])) modified = true;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Object[] toRemove = new Object[size];
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                toRemove[count++] = elements[i];
            }
        }
        for (int i = 0; i < count; i++) {
            if (remove(toRemove[i])) modified = true;
        }
        return modified;
    }

    // === Остальные методы ===
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
}