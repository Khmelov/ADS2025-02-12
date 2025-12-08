package by.it.group451002.popeko.lesson11;

import java.util.*;

public class MyTreeSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size;
    private final Comparator<? super E> comparator;

    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
        comparator = null;
    }

    @SuppressWarnings("unchecked")
    public MyTreeSet(Comparator<? super E> comparator) {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
        this.comparator = comparator;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";

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
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        E element = (E) o;
        return binarySearch(element) >= 0;
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

        // Вычисляем позицию для вставки
        int insertIndex = -index - 1;

        ensureCapacity();

        // Сдвигаем элементы вправо
        System.arraycopy(elements, insertIndex, elements, insertIndex + 1, size - insertIndex);
        elements[insertIndex] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        @SuppressWarnings("unchecked")
        E element = (E) o;
        int index = binarySearch(element);
        if (index < 0) {
            return false; // Элемент не найден
        }

        // Сдвигаем элементы влево
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
        // Создаем новый массив только с элементами, которых нет в коллекции c
        Object[] newElements = new Object[elements.length];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            @SuppressWarnings("unchecked")
            E element = (E) elements[i];
            if (!c.contains(element)) {
                newElements[newSize++] = element;
            } else {
                modified = true;
            }
        }

        if (modified) {
            elements = newElements;
            size = newSize;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        // Создаем новый массив только с элементами, которые есть в коллекции c
        Object[] newElements = new Object[elements.length];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            @SuppressWarnings("unchecked")
            E element = (E) elements[i];
            if (c.contains(element)) {
                newElements[newSize++] = element;
            } else {
                modified = true;
            }
        }

        if (modified) {
            elements = newElements;
            size = newSize;
        }
        return modified;
    }

    // Вспомогательные методы
    @SuppressWarnings("unchecked")
    private int binarySearch(E key) {
        int low = 0;
        int high = size - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            E midVal = (E) elements[mid];
            int cmp = compare(key, midVal);

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

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        return comparator == null
                ? ((Comparable<? super E>) a).compareTo(b)
                : comparator.compare(a, b);
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = elements.length * 2;
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }

    // Остальные методы интерфейса Set (не реализованы)
    @Override
    public Iterator<E> iterator() {
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