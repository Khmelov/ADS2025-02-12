package by.it.group410901.garkusha.lesson11;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size;
    private final Comparator<? super E> comparator; // Объявление поля компаратора

    // Конструкторы
    public MyTreeSet() {
        this(DEFAULT_CAPACITY, null);
    }

    public MyTreeSet(Comparator<? super E> comparator) {
        this(DEFAULT_CAPACITY, comparator);
    }

    public MyTreeSet(int initialCapacity) {
        this(initialCapacity, null);
    }

    @SuppressWarnings("unchecked")
    public MyTreeSet(int initialCapacity, Comparator<? super E> comparator) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        this.elements = new Object[initialCapacity];
        this.size = 0;
        this.comparator = comparator;
    }

    // Вспомогательные методы

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = Math.max(elements.length * 2, minCapacity);
            Object[] newElements = new Object[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    @SuppressWarnings("unchecked")
    private int compare(E e1, E e2) {
        if (comparator != null) {
            return comparator.compare(e1, e2);
        } else {
            // Приведение типа
            Comparable<? super E> comparable = (Comparable<? super E>) e1;
            return comparable.compareTo(e2);
        }
    }

    // Бинарный поиск для нахождения индекса элемента
    private int binarySearch(Object o) {
        @SuppressWarnings("unchecked")
        E key = (E) o;

        int low = 0;
        int high = size - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            @SuppressWarnings("unchecked")
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

    // Вставка элемента в отсортированную позицию
    private void insertAt(int index, E element) {
        ensureCapacity(size + 1);
        // Сдвиг элементов вправо
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }

    // Удаление элемента по индексу
    private void removeAt(int index) {
        // Количество элементов для сдвига
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null; // Помощь GC
    }

    // Реализация методов интерфейса Set

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) {
                    return true;
                }
            }
            return false;
        }

        try {
            return binarySearch(o) >= 0;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            // Обработка null элементов
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) {
                    return false; // null уже существует
                }
            }
            // Добавление null в конец (null считается наибольшим)
            ensureCapacity(size + 1);
            elements[size++] = null;
            return true;
        }

        int index = binarySearch(e);
        if (index >= 0) {
            return false; // Элемент уже существует
        }

        // Преобразование отрицательного индекса в позицию для вставки
        int insertionPoint = -index - 1;
        insertAt(insertionPoint, e);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) {
                    removeAt(i);
                    return true;
                }
            }
            return false;
        }

        try {
            int index = binarySearch(o);
            if (index >= 0) {
                removeAt(index);
                return true;
            }
        } catch (ClassCastException e) {
            // Игнорируем - элемент не может быть в множестве
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        String result = "[";
        for (int i = 0; i < size; i++) {
            result += elements[i];
            if (i < size - 1) {
                result += ", ";
            }
        }
        result += "]";
        return result;
    }

    // Методы для работы с коллекциями

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

        // Создаем временный массив для элементов, которые нужно сохранить
        Object[] retained = new Object[size];
        int retainedSize = 0;

        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                retained[retainedSize++] = elements[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            // Заменяем исходный массив
            elements = retained;
            size = retainedSize;
        }

        return modified;
    }

    // Дополнительные методы для работы с отсортированным множеством

    @SuppressWarnings("unchecked")
    public E first() {
        if (isEmpty()) {
            throw new IllegalStateException("Set is empty");
        }
        return (E) elements[0];
    }

    @SuppressWarnings("unchecked")
    public E last() {
        if (isEmpty()) {
            throw new IllegalStateException("Set is empty");
        }
        return (E) elements[size - 1];
    }

    // Методы, которые не требуются

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Iterator not implemented");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("toArray() not implemented");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("toArray(T[] a) not implemented");
    }

    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException("equals() not implemented");
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("hashCode() not implemented");
    }
}