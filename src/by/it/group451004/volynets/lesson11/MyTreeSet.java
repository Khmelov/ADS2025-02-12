package by.it.group451004.volynets.lesson11;

import java.util.Collection;
import java.util.Iterator;
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
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        elements = new Object[initialCapacity];
        size = 0;
    }

    // строковое представление в отсортированном порядке
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

    // добавление элемента с сохранением сортировки
    @Override
    @SuppressWarnings("unchecked")
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException();
        }

        // бинарный поиск позиции для вставки
        int index = binarySearch((Comparable<? super E>) e);
        if (index >= 0) {
            return false; // элемент уже существует
        }

        int insertIndex = -index - 1;
        ensureCapacity(size + 1);

        // сдвиг элементов для вставки
        System.arraycopy(elements, insertIndex, elements, insertIndex + 1, size - insertIndex);
        elements[insertIndex] = e;
        size++;
        return true;
    }

    // удаление элемента с сохранением сортировки
    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }

        int index = binarySearch((Comparable<? super E>) o);
        if (index < 0) {
            return false;
        }

        // сдвиг элементов для удаления
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        elements[--size] = null;
        return true;
    }

    // проверка наличия элемента (бинарный поиск)
    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        return binarySearch((Comparable<? super E>) o) >= 0;
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
        Object[] temp = new Object[size];
        int newSize = 0;

        // собираем элементы для сохранения
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                temp[newSize++] = elements[i];
            }
        }

        // перестраиваем множество если нужно
        if (newSize != size) {
            modified = true;
            clear();
            for (int i = 0; i < newSize; i++) {
                @SuppressWarnings("unchecked")
                E element = (E) temp[i];
                add(element);
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Object[] temp = new Object[size];
        int newSize = 0;

        // собираем элементы для сохранения
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                temp[newSize++] = elements[i];
            }
        }

        // перестраиваем множество если нужно
        if (newSize != size) {
            modified = true;
            clear();
            for (int i = 0; i < newSize; i++) {
                @SuppressWarnings("unchecked")
                E element = (E) temp[i];
                add(element);
            }
        }
        return modified;
    }

    // бинарный поиск в отсортированном массиве
    @SuppressWarnings("unchecked")
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
                return mid; // найден
            }
        }
        return -(low + 1); // позиция для вставки
    }

    // увеличение емкости массива
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length * 2;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            Object[] newElements = new Object[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

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