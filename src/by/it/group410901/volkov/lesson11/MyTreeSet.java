package by.it.group410901.volkov.lesson11;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

/**
 * Реализация TreeSet на основе отсортированного массива
 * Элементы хранятся в отсортированном порядке согласно компаратору
 * Использует бинарный поиск для операций поиска O(log n)
 */
public class MyTreeSet<E> implements Set<E> {
    // Начальная емкость массива для хранения элементов
    private static final int DEFAULT_CAPACITY = 10;
    // Массив для хранения элементов в отсортированном порядке
    // Элементы всегда поддерживаются в отсортированном состоянии
    private Object[] elements;
    // Количество элементов в множестве
    private int size;
    // Компаратор для сравнения элементов (null если используется естественный порядок)
    private final Comparator<? super E> comparator;

    // Конструкторы
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

    @SuppressWarnings("unchecked")
    public MyTreeSet(Collection<? extends E> c) {
        this();
        addAll(c);
    }

    // Основные методы
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
        return binarySearch(o) >= 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException("Null elements are not supported");
        }

        int index = binarySearch(e);
        if (index >= 0) {
            return false; // элемент уже существует
        }

        // Вставляем элемент в правильную позицию
        int insertionPoint = -index - 1;
        ensureCapacity(size + 1);

        // Сдвигаем элементы вправо
        System.arraycopy(elements, insertionPoint, elements, insertionPoint + 1, size - insertionPoint);
        elements[insertionPoint] = e;
        size++;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        int index = binarySearch(o);
        if (index < 0) {
            return false; // элемент не найден
        }

        // Удаляем элемент, сдвигая остальные влево
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null; // очищаем последний элемент
        return true;
    }

    /**
     * Бинарный поиск элемента в отсортированном массиве
     * Время выполнения: O(log n)
     * @param key искомый элемент
     * @return индекс элемента если найден (>= 0),
     *         или -(insertionPoint + 1) если не найден (insertionPoint - позиция для вставки)
     */
    @SuppressWarnings("unchecked")
    private int binarySearch(Object key) {
        int low = 0;
        int high = size - 1;

        while (low <= high) {
            // Используем беззнаковый сдвиг для избежания переполнения
            int mid = (low + high) >>> 1;
            E midVal = (E) elements[mid];
            int cmp = compare((E) key, midVal);

            if (cmp < 0) {
                // Искомый элемент меньше среднего, ищем в левой половине
                high = mid - 1;
            } else if (cmp > 0) {
                // Искомый элемент больше среднего, ищем в правой половине
                low = mid + 1;
            } else {
                return mid; // ключ найден
            }
        }
        // Ключ не найден, возвращаем отрицательное значение
        // -(low + 1) позволяет восстановить позицию вставки как -returnValue - 1
        return -(low + 1);
    }

    /**
     * Сравнивает два элемента с использованием компаратора или естественного порядка
     * @param e1 первый элемент
     * @param e2 второй элемент
     * @return отрицательное число если e1 < e2, 0 если равны, положительное если e1 > e2
     */
    @SuppressWarnings("unchecked")
    private int compare(E e1, E e2) {
        if (comparator != null) {
            // Используем заданный компаратор
            return comparator.compare(e1, e2);
        } else {
            // Используем естественный порядок (элементы должны быть Comparable)
            Comparable<? super E> comparable = (Comparable<? super E>) e1;
            return comparable.compareTo(e2);
        }
    }

    // Обеспечение достаточной емкости массива
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

    // Метод toString() для вывода в порядке возрастания
    @Override
    public String toString() {
        if (isEmpty()) {
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

    // Итератор для обхода в порядке возрастания
    @Override
    public Iterator<E> iterator() {
        return new TreeSetIterator();
    }

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

    // Остальные методы интерфейса Set
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
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        }

        System.arraycopy(elements, 0, a, 0, size);

        if (a.length > size) {
            a[size] = null;
        }

        return a;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Set)) return false;

        Set<?> other = (Set<?>) o;
        if (size != other.size()) return false;

        return containsAll(other);
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

    // Дополнительные методы для TreeSet (не обязательные, но полезные)
    @SuppressWarnings("unchecked")
    public E first() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        return (E) elements[0];
    }

    @SuppressWarnings("unchecked")
    public E last() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        return (E) elements[size - 1];
    }
}
