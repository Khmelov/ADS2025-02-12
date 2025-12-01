package by.it.group451004.kozlov.lesson11;

import java.util.Collection;
import java.util.Set;

public class MyTreeSet<E extends Comparable<E>> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private Object[] elements;
    private int size;

    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        elements = new Object[DEFAULT_CAPACITY];
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
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        return binarySearch((E) o) >= 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        int index = binarySearch(e);
        if (index >= 0) {
            return false; // Элемент уже существует
        }

        if (size == elements.length) {
            resize();
        }

        int insertionPoint = -index - 1;
        System.arraycopy(elements, insertionPoint, elements, insertionPoint + 1, size - insertionPoint);
        elements[insertionPoint] = e;
        size++;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        int index = binarySearch((E) o);
        if (index < 0) {
            return false;
        }

        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        elements[--size] = null;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object element : c) {
            if (remove(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[i])) {
                remove(elements[i]);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private int binarySearch(E key) {
        int low = 0;
        int high = size - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            E midVal = (E) elements[mid];
            int cmp = midVal.compareTo(key);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid; // ключ найден
            }
        }
        return -(low + 1); // ключ не найден
    }

    private void resize() {
        Object[] newElements = new Object[elements.length * 2];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    @Override
    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}