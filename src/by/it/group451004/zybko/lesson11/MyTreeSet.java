package by.it.group451004.zybko.lesson11;

import java.util.Arrays;
import java.util.Set;

public class MyTreeSet<E extends Comparable<E>> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private Object[] elements;
    private int size;

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
        Arrays.fill(elements, 0, size, null);
        size = 0;
    }

    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        int index = binarySearch(element);
        if (index >= 0) {
            return false;
        }

        if (size == elements.length) {
            elements = Arrays.copyOf(elements, elements.length * 2);
        }

        int insertIndex = -index - 1;
        System.arraycopy(elements, insertIndex, elements, insertIndex + 1, size - insertIndex);
        elements[insertIndex] = element;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object element) {
        if (element == null) {
            return false;
        }

        try {
            @SuppressWarnings("unchecked")
            E key = (E) element;
            int index = binarySearch(key);
            if (index < 0) {
                return false;
            }

            System.arraycopy(elements, index + 1, elements, index, size - index - 1);
            elements[--size] = null;
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public boolean contains(Object element) {
        if (element == null) {
            return false;
        }

        try {
            @SuppressWarnings("unchecked")
            E key = (E) element;
            return binarySearch(key) >= 0;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        boolean modified = false;
        for (Object element : c) {
            if (remove(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            @SuppressWarnings("unchecked")
            E element = (E) elements[i];
            if (!c.contains(element)) {
                remove(element);
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
                return mid;
            }
        }
        return -(low + 1);
    }

    @Override
    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}