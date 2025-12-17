package by.it.group451004.momotyuk.lesson11;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;

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
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        return binarySearch((E) o) >= 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        int index = binarySearch(e);
        if (index >= 0) {
            return false; // элемент уже существует
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
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        int index = binarySearch((E) o);
        if (index < 0) {
            return false; // элемент не найден
        }

        // Сдвигаем элементы влево
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        elements[--size] = null;

        return true;
    }

    @Override
    public String toString() {
        if (size == 0) {
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

    // Методы работы с коллекциями

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
    @SuppressWarnings("unchecked")
    public boolean retainAll(Collection<?> c) {

        boolean modified = false;

        Object[] retained = new Object[size];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            // Проверяем, содержится ли текущий элемент в переданной коллекции c
            if (c.contains(elements[i])) {
                // Если элемент содержится в коллекции c, сохраняем его во временный массив
                retained[newSize++] = elements[i];
            }
            // Если элемент не содержится в c - он не добавляется в retained (будет удален)
        }

        if (newSize != size) {
            modified = true;
            elements = retained;
            size = newSize;
        }

        return modified;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;

        // Создаем временный массив для элементов, которые не нужно удалять
        Object[] remaining = new Object[size];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remaining[newSize++] = elements[i];
            }
        }

        if (newSize != size) {
            modified = true;
            elements = remaining;
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
            int cmp = compare(midVal, key);

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

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        } else {
            return ((Comparable<? super E>) a).compareTo(b);
        }
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = elements.length * 2;
            Object[] newElements = new Object[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    // Итератор для обхода в порядке возрастания

    private class TreeSetIterator implements Iterator<E> {
        private int currentIndex = 0;
        private int lastReturned = -1;

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            lastReturned = currentIndex;
            return (E) elements[currentIndex++];
        }

        @Override
        public void remove() {
            if (lastReturned == -1) {
                throw new IllegalStateException();
            }
            MyTreeSet.this.remove(elements[lastReturned]);
            currentIndex = lastReturned;
            lastReturned = -1;
        }
    }

    // Методы интерфейса Set, которые не требуются по заданию

    @Override
    public Iterator<E> iterator() {
        return new TreeSetIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        System.arraycopy(elements, 0, array, 0, size);
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) toArray();
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}