package by.it.group410901.bandarzheuskaya.lesson11;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;

    private Object[] elements;
    private int size;
    private final Comparator<? super E> comparator;

    // Конструкторы
    public MyTreeSet() {
        this(DEFAULT_CAPACITY, null);
    }

    public MyTreeSet(Comparator<? super E> comparator) {
        this(DEFAULT_CAPACITY, comparator);
    }

    @SuppressWarnings("unchecked")
    public MyTreeSet(int initialCapacity, Comparator<? super E> comparator) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        this.elements = new Object[initialCapacity];
        this.size = 0;
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
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object element) {
        return binarySearch((E) element) >= 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean add(E element) {
        // Проверяем, есть ли уже такой элемент
        int index = binarySearch(element);
        if (index >= 0) {
            return false; // Элемент уже существует
        }

        // Вычисляем позицию для вставки
        int insertionPoint = -index - 1;

        // Увеличиваем массив при необходимости
        if (size == elements.length) {
            resize();
        }

        // Сдвигаем элементы вправо для освобождения места
        System.arraycopy(elements, insertionPoint, elements, insertionPoint + 1, size - insertionPoint);

        // Вставляем новый элемент
        elements[insertionPoint] = element;
        size++;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object element) {
        int index = binarySearch((E) element);
        if (index < 0) {
            return false; // Элемент не найден
        }

        // Сдвигаем элементы влево для удаления
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        elements[--size] = null; // Очищаем последний элемент
        return true;
    }

    // Бинарный поиск с поддержкой компаратора
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
                return mid; // Элемент найден
            }
        }
        return -(low + 1); // Элемент не найден, возвращаем точку вставки
    }

    // Сравнение элементов с поддержкой компаратора и Comparable
    @SuppressWarnings("unchecked")
    private int compare(E e1, E e2) {
        if (comparator != null) {
            return comparator.compare(e1, e2);
        } else {
            return ((Comparable<? super E>) e1).compareTo(e2);
        }
    }

    // Увеличение размера массива
    private void resize() {
        int newCapacity = elements.length * 2;
        Object[] newElements = new Object[newCapacity];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
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
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    // toString() выводит элементы в порядке возрастания

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

    // Остальные методы интерфейса Set

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;
            private int lastReturned = -1;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (!hasNext()) return null;
                lastReturned = currentIndex;
                return (E) elements[currentIndex++];
            }

            @Override
            public void remove() {
                if (lastReturned == -1) {
                    throw new IllegalStateException();
                }
                MyTreeSet.this.remove(elements[lastReturned]);
                currentIndex--;
                lastReturned = -1;
            }
        };
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
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }

        System.arraycopy(elements, 0, a, 0, size);

        if (a.length > size) {
            a[size] = null;
        }

        return a;
    }
}
