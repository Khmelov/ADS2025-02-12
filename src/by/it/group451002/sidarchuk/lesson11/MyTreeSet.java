package by.it.group451002.sidarchuk.lesson11;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public class MyTreeSet<E extends Comparable<E>> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size;

    public MyTreeSet() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public MyTreeSet(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
        this.elements = new Object[initialCapacity];
        this.size = 0;
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
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        // Проверяем, существует ли элемент
        int index = binarySearch(element);
        if (index >= 0) {
            return false; // Элемент уже существует
        }

        // Вычисляем позицию для вставки
        int insertionPoint = -(index + 1);

        // Увеличиваем массив при необходимости
        ensureCapacity(size + 1);

        // Сдвигаем элементы для освобождения места
        System.arraycopy(elements, insertionPoint, elements, insertionPoint + 1, size - insertionPoint);

        // Вставляем новый элемент
        elements[insertionPoint] = element;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object obj) {
        @SuppressWarnings("unchecked")
        E element = (E) obj;
        int index = binarySearch(element);
        if (index < 0) {
            return false; // Элемент не найден
        }

        // Сдвигаем элементы для заполнения пустого места
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }

        elements[--size] = null; // Очищаем последний элемент
        return true;
    }

    @Override
    public boolean contains(Object obj) {
        @SuppressWarnings("unchecked")
        E element = (E) obj;
        return binarySearch(element) >= 0;
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
    public boolean retainAll(Collection<?> c) {
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

    // Вспомогательные методы

    private int binarySearch(E key) {
        int low = 0;
        int high = size - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            @SuppressWarnings("unchecked")
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

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = Math.max(elements.length * 2, minCapacity);
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }

    // Методы интерфейса Set, которые не реализованы (опционально можно добавить)

    @Override
    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException("Iterator not implemented");
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("toArray(T[] a) not implemented");
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Set)) return false;

        Set<?> other = (Set<?>) o;
        if (size != other.size()) return false;

        try {
            return containsAll(other);
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (int i = 0; i < size; i++) {
            if (elements[i] != null) {
                hashCode += elements[i].hashCode();
            }
        }
        return hashCode;
    }
}
