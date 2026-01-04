package by.it.group451003.kishkov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private Object[] elements;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public MyTreeSet() {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    public MyTreeSet(int initialCapacity) {
        if (initialCapacity <= 0)
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        elements = new Object[initialCapacity];
        size = 0;
    }

    //////               Обязательные к реализации методы             //////

    @Override
    public String toString() {
        if (size == 0)
            return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1)
                sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++)
            elements[i] = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        if (element == null)
            throw new NullPointerException();
        if (contains(element))
            return false;
        ensureCapacity(size + 1);
        Comparable<? super E> comparable = (Comparable<? super E>) element;
        int insertIndex = 0;
        while (insertIndex < size) {
            E current = (E) elements[insertIndex];
            if (comparable.compareTo(current) < 0)
                break;
            insertIndex++;
        }
        for (int i = size; i > insertIndex; i--)
            elements[i] = elements[i - 1];
        elements[insertIndex] = element;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object element) {
        if (element == null)
            return false;
        Comparable<? super E> comparable = (Comparable<? super E>) element;
        int index = binarySearch(comparable);
        if (index < 0)
            return false;
        for (int i = index; i < size - 1; i++)
            elements[i] = elements[i + 1];
        elements[size - 1] = null;
        size--;
        return true;
    }

    @Override
    public boolean contains(Object element) {
        if (element == null)
            return false;
        Comparable<? super E> comparable = (Comparable<? super E>) element;
        int index = binarySearch(comparable);
        return index >= 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c) {
            if (add(element))
                modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        Object[] temp = new Object[size];
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            E element = (E) elements[i];
            if (!c.contains(element)) {
                temp[newSize++] = element;
            } else {
                modified = true;
            }
        }
        if (modified) {
            elements = temp;
            size = newSize;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Object[] temp = new Object[size];
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            E element = (E) elements[i];
            if (c.contains(element)) {
                temp[newSize++] = element;
            } else {
                modified = true;
            }
        }
        if (modified) {
            elements = temp;
            size = newSize;
        }
        return modified;
    }

    //////           Внутренние вспомогательные методы                ///////

    private int binarySearch(Comparable<? super E> key) {
        int low = 0;
        int high = size - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            E midVal = (E) elements[mid];
            int cmp = key.compareTo(midVal);
            if (cmp < 0) {
                high = mid - 1;
            } else if (cmp > 0) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length + (elements.length >> 1);
            if (newCapacity < minCapacity)
                newCapacity = minCapacity;
            Object[] newElements = new Object[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    //////              Методы-затычки (UnsupportedOperation)         ///////

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(elements, 0, result, 0, size);
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

}