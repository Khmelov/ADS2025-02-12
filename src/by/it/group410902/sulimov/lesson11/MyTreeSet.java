package by.it.group410902.sulimov.lesson11;
import java.util.*;

public class MyTreeSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size;

    public MyTreeSet() {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    // Вспомогательные методы
    private int compare(E a, E b) {
        return ((Comparable<? super E>) a).compareTo(b);
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            elements = Arrays.copyOf(elements, elements.length * 2);
        }
    }

    // Бинарный поиск - возвращает индекс элемента или позицию для вставки
    private int binarySearch(Object element) {
        int low = 0;
        int high = size - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            E midVal = (E) elements[mid];
            int cmp = compare(midVal, (E) element);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid; // элемент найден
            }
        }
        return -(low + 1); // элемент не найден, возвращаем позицию для вставки
    }

    // Обязательные методы
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
            throw new NullPointerException();
        }

        int index = binarySearch(element);
        if (index >= 0) {
            return false; // элемент уже существует
        }

        int insertionPoint = -index - 1;
        ensureCapacity();

        // Сдвигаем элементы для освобождения места
        if (insertionPoint < size) {
            System.arraycopy(elements, insertionPoint, elements, insertionPoint + 1, size - insertionPoint);
        }

        elements[insertionPoint] = element;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object element) {
        if (element == null) {
            throw new NullPointerException();
        }

        int index = binarySearch(element);
        if (index < 0) {
            return false; // элемент не найден
        }

        // Сдвигаем элементы для заполнения пустоты
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }

        elements[--size] = null;
        return true;
    }

    @Override
    public boolean contains(Object element) {
        if (element == null) {
            throw new NullPointerException();
        }
        return binarySearch(element) >= 0;
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
    public boolean removeAll(Collection<?> c) {
        // Оптимизированная версия O(n)
        if (c.isEmpty() || size == 0) {
            return false;
        }

        // Создаем временный массив для элементов, которые нужно сохранить
        Object[] newElements = new Object[elements.length];
        int newSize = 0;

        // Проходим по всем элементам в отсортированном порядке
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                newElements[newSize++] = elements[i];
            }
        }

        if (newSize == size) {
            return false; // Ничего не изменилось
        }

        // Заменяем старый массив новым
        this.elements = newElements;
        this.size = newSize;
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // Оптимизированная версия O(n)
        if (size == 0) {
            return false;
        }

        // Создаем временный массив для элементов, которые нужно сохранить
        Object[] newElements = new Object[elements.length];
        int newSize = 0;

        // Проходим по всем элементам в отсортированном порядке
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                newElements[newSize++] = elements[i];
            }
        }

        if (newSize == size) {
            return false; // Ничего не изменилось
        }

        // Заменяем старый массив новым
        this.elements = newElements;
        this.size = newSize;
        return true;
    }

    // Остальные методы интерфейса Set - не реализованы
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