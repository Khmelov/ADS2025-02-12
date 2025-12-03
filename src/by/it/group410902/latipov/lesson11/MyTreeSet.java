package by.it.group410902.latipov.lesson11;

import java.util.*;

public class MyTreeSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size;
    private final Comparator<? super E> comparator;

    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = null;
    }

    @SuppressWarnings("unchecked")
    public MyTreeSet(Comparator<? super E> comparator) {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = comparator;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

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
            throw new NullPointerException("Element cannot be null");
        }

        // Бинарный поиск для определения позиции
        int index = binarySearch(element);

        // Если элемент уже существует
        if (index >= 0) {
            return false;
        }

        // Вычисляем позицию для вставки
        int insertIndex = -index - 1;

        // Обеспечиваем достаточную емкость
        ensureCapacity(size + 1);

        // Сдвигаем элементы для освобождения места
        if (insertIndex < size) {
            System.arraycopy(elements, insertIndex, elements, insertIndex + 1, size - insertIndex);
        }

        // Вставляем элемент
        elements[insertIndex] = element;
        size++;

        return true;
    }

    @Override
    public boolean remove(Object element) {
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }

        // Бинарный поиск элемента
        int index = binarySearch(element);

        // Если элемент не найден
        if (index < 0) {
            return false;
        }

        // Удаляем элемент
        removeAt(index);
        return true;
    }

    @Override
    public boolean contains(Object element) {
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }
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
        if (c.isEmpty()) {
            return false;
        }

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
        if (c.isEmpty() || size == 0) {
            return false;
        }

        boolean modified = false;
        // Создаем временный массив для элементов, которые нужно сохранить
        Object[] temp = new Object[size];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            @SuppressWarnings("unchecked")
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
        if (size == 0) {
            return false;
        }
        if (c.isEmpty()) {
            clear();
            return true;
        }

        boolean modified = false;
        // Создаем временный массив для элементов, которые нужно сохранить
        Object[] temp = new Object[size];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            @SuppressWarnings("unchecked")
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

    // Вспомогательные методы

    @SuppressWarnings("unchecked")
    private int binarySearch(Object key) {
        int low = 0;
        int high = size - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            E midVal = (E) elements[mid];
            int cmp = compare(midVal, (E) key);

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
        }
        return ((Comparable<? super E>) a).compareTo(b);
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = Math.max(elements.length * 2, minCapacity);
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }

    private void removeAt(int index) {
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null; // help GC
    }

    /////////////////////////////////////////////////////////////////////////
    //////        Остальные методы - необязательные к реализации     ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return (E) elements[currentIndex++];
            }
        };
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) Arrays.copyOf(elements, size, a.getClass());
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}