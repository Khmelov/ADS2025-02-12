package by.it.group451004.zarivniak.lesson11;

import java.util.Collection;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 10;

    private Object[] elements;
    private int size;

    public MyTreeSet() {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    public MyTreeSet(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        elements = new Object[initialCapacity];
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
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
    public boolean contains(Object element) {
        if (element == null) {
            return false;
        }

        @SuppressWarnings("unchecked")
        Comparable<? super E> comparableElement = (Comparable<? super E>) element;
        int index = binarySearch(comparableElement);
        return index >= 0;
    }

    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        if (contains(element)) {
            return false;
        }

        ensureCapacity(size + 1);

        @SuppressWarnings("unchecked")
        Comparable<? super E> comparableElement = (Comparable<? super E>) element;

        // Находим позицию для вставки
        int insertIndex = 0;
        while (insertIndex < size) {
            @SuppressWarnings("unchecked")
            E current = (E) elements[insertIndex];
            if (comparableElement.compareTo(current) < 0) {
                break;
            }
            insertIndex++;
        }

        // Сдвигаем элементы вправо
        for (int i = size; i > insertIndex; i--) {
            elements[i] = elements[i - 1];
        }

        // Вставляем новый элемент
        elements[insertIndex] = element;
        size++;

        return true;
    }

    @Override
    public boolean remove(Object element) {
        if (element == null) {
            return false;
        }

        @SuppressWarnings("unchecked")
        Comparable<? super E> comparableElement = (Comparable<? super E>) element;
        int index = binarySearch(comparableElement);

        if (index < 0) {
            return false;
        }

        // Сдвигаем элементы влево
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        elements[size - 1] = null;
        size--;

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

        // Создаем временный массив для элементов, которые нужно оставить
        Object[] temp = new Object[size];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            E element = getElement(i);
            if (!c.contains(element)) {
                temp[newSize++] = element;
            } else {
                modified = true;
            }
        }

        if (modified) {
            // Заменяем исходный массив
            elements = temp;
            size = newSize;
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;

        // Создаем временный массив для элементов, которые нужно оставить
        Object[] temp = new Object[size];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            E element = getElement(i);
            if (c.contains(element)) {
                temp[newSize++] = element;
            } else {
                modified = true;
            }
        }

        if (modified) {
            // Заменяем исходный массив
            elements = temp;
            size = newSize;
        }

        return modified;
    }

    @SuppressWarnings("unchecked")
    private E getElement(int index) {
        return (E) elements[index];
    }

    private int binarySearch(Comparable<? super E> key) {
        int low = 0;
        int high = size - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            E midVal = getElement(mid);
            int cmp = key.compareTo(midVal);

            if (cmp < 0) {
                high = mid - 1;
            } else if (cmp > 0) {
                low = mid + 1;
            } else {
                return mid; // ключ найден
            }
        }
        return -(low + 1); // ключ не найден
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length + (elements.length >> 1); // +50%
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            Object[] newElements = new Object[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    @Override
    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException("iterator");
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(elements, 0, result, 0, size);
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("toArray");
    }
}