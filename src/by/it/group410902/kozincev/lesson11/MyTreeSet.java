package by.it.group410902.kozincev.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyTreeSet<E extends Comparable<E>> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private E[] elements;
    private int size;


    public MyTreeSet() {
        this.elements = (E[]) new Comparable[DEFAULT_CAPACITY];
        this.size = 0;
    }

    // Вспомогательный метод для расширения массива
    private void grow() {
        int newCapacity = elements.length * 2;
        E[] newElements = (E[]) new Comparable[newCapacity];
        // Копируем все элементы
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;
    }

    private int binarySearch(Object o) {
        if (o == null) return -1; // null не поддерживается для Comparable

        int low = 0;
        int high = size - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1; // Безопасное вычисление середины

            E midVal = elements[mid];

            // Сравнение. Поскольку o - это Object, используем compareTo
            // E extends Comparable<E>, поэтому midVal.compareTo(o) безопасно.
            int cmp = midVal.compareTo((E) o);

            if (cmp < 0) { // midVal < o
                low = mid + 1;
            } else if (cmp > 0) { // midVal > o
                high = mid - 1;
            } else {
                return mid; // Найдено
            }
        }
        return -(low + 1); // Не найдено, low - это точка вставки
    }

    // ================== Обязательные методы ==================

    @Override
    public boolean add(E e) {
        if (e == null) return false;

        int index = binarySearch(e);

        if (index >= 0) {
            return false; // Элемент уже существует
        }

        int insertionPoint = -(index + 1); // Определяем точку вставки

        if (size == elements.length) {
            grow(); // Расширяем массив, если нет места
        }

        // Сдвигаем все элементы, начиная с точки вставки, на одну позицию вправо
        for (int i = size; i > insertionPoint; i--) {
            elements[i] = elements[i - 1];
        }

        elements[insertionPoint] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = binarySearch(o);

        if (index < 0) {
            return false; // Элемент не найден
        }

        // Сдвигаем все элементы, начиная с индекса + 1, на одну позицию влево
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        // Удаляем ссылку на последний элемент, чтобы помочь GC
        elements[size - 1] = null;
        size--;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return binarySearch(o) >= 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        // Очищаем ссылки для GC
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
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('[');

        // Итерация по элементам массива. Поскольку массив всегда отсортирован,
        // элементы выводятся в порядке возрастания.
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", "); // Разделитель: запятая с пробелом
            }
        }
        sb.append(']');
        return sb.toString();
    }

    // ================== Методы Collection (обязательные) ==================

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            if (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;

        // Создаем новый временный массив для элементов, которые нужно оставить
        // Оптимизировать без использования List или других коллекций
        @SuppressWarnings("unchecked")
        E[] tempArray = (E[]) new Comparable[size];
        int tempSize = 0;

        // Проходим по текущему массиву
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                tempArray[tempSize++] = elements[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            // Копируем оставшиеся элементы обратно
            elements = tempArray;
            size = tempSize;

            // Если размер уменьшился, очищаем "хвост" старого массива
            for (int i = size; i < elements.length; i++) {
                elements[i] = null;
            }
        }

        return modified;
    }

    // ================== Необязательные методы ==================
    // Оставляем заглушки для остальных методов Set/Collection.

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException("Iterator not implemented"); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException("toArray not implemented"); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException("toArray not implemented"); }
}