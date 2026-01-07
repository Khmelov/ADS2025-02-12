package by.it.group451004.bortsevich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E extends Comparable<E>> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size;

    public MyTreeSet() {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
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

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        elements = new Object[DEFAULT_CAPACITY];
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

        // Бинарный поиск для определения позиции
        int index = binarySearch((E)element);

        // Если элемент уже существует
        if (index >= 0) {
            return false;
        }

        // Вычисляем позицию для вставки
        int insertIndex = -index - 1;

        // Увеличиваем массив если нужно
        ensureCapacity(size + 1);

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

        try {
            @SuppressWarnings("unchecked")
            E e = (E) element;
            int index = binarySearch(e);

            if (index < 0) {
                return false; // Элемент не найден
            }

            // Сдвигаем элементы влево
            for (int i = index; i < size - 1; i++) {
                elements[i] = elements[i + 1];
            }

            elements[size - 1] = null;
            size--;
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
            E e = (E) element;
            return binarySearch(e) >= 0;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object element : collection) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        boolean modified = false;
        for (E element : collection) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean modified = false;
        for (Object element : collection) {
            if (remove(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean modified = false;

        // Создаем временный массив для хранения элементов, которые нужно сохранить
        Object[] temp = new Object[size];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            @SuppressWarnings("unchecked")
            E element = (E) elements[i];
            if (collection.contains(element)) {
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
            Object[] newElements = new Object[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    // Методы интерфейса Set, которые не являются обязательными по заданию,
    // но должны быть реализованы

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Iterator not implemented");
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        System.arraycopy(elements, 0, array, 0, size);
        return array;
    }

    @Override
    public <T> T[] toArray(T[] array) {
        throw new UnsupportedOperationException("toArray(T[]) not implemented");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Set)) return false;

        Set<?> other = (Set<?>) obj;
        return size == other.size() && containsAll(other);
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