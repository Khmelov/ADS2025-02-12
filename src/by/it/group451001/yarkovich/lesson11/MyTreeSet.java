package by.it.group451001.yarkovich.lesson11;

import java.util.Collection;
import java.util.Iterator;

/**
 * Реализация множества на основе отсортированного массива.
 * Элементы хранятся в естественном порядке (сортировка по возрастанию).
 */
public class MyTreeSet<E extends Comparable<E>> implements Collection<E> {
    // Начальные константы
    private static final int DEFAULT_CAPACITY = 16;

    // Основные поля
    private Object[] elements;   // Массив для хранения элементов в отсортированном порядке
    private int size;           // Количество элементов в множестве
    private int capacity;       // Текущая емкость массива

    // Конструкторы
    public MyTreeSet() {
        this(DEFAULT_CAPACITY);
    }

    public MyTreeSet(int initialCapacity) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);

        this.capacity = initialCapacity > 0 ? initialCapacity : DEFAULT_CAPACITY;
        this.elements = new Object[this.capacity];
        this.size = 0;
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
        // Обнуляем массив элементов
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    /**
     * Добавляет элемент в множество, поддерживая отсортированный порядок
     * Использует бинарный поиск для нахождения позиции вставки
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean add(E element) {
        // Проверяем, нет ли уже такого элемента
        if (contains(element)) {
            return false;
        }

        // Увеличиваем емкость массива при необходимости
        if (size == capacity) {
            resize();
        }

        // Находим позицию для вставки (бинарный поиск)
        int index = findInsertionIndex(element);

        // Сдвигаем элементы вправо для освобождения места
        if (index < size) {
            System.arraycopy(elements, index, elements, index + 1, size - index);
        }

        // Вставляем элемент на найденную позицию
        elements[index] = element;
        size++;

        return true;
    }

    /**
     * Удаляет элемент из множества с сохранением отсортированного порядка
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object object) {
        // Находим индекс элемента бинарным поиском
        int index = binarySearch((E) object);

        if (index >= 0) {
            // Элемент найден - удаляем его
            removeElementAtIndex(index);
            return true;
        }

        return false; // Элемент не найден
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object object) {
        return binarySearch((E) object) >= 0;
    }

    /**
     * Возвращает строковое представление в отсортированном порядке
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");

        // Выводим элементы в порядке их хранения (отсортированном)
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(elements[i]);
        }

        sb.append("]");
        return sb.toString();
    }

    // Вспомогательные методы для работы с отсортированным массивом

    /**
     * Бинарный поиск элемента в отсортированном массиве
     * @return индекс элемента или -1 если не найден
     */
    @SuppressWarnings("unchecked")
    private int binarySearch(E element) {
        int left = 0;
        int right = size - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            E midElement = (E) elements[mid];
            int comparison = element.compareTo(midElement);

            if (comparison == 0) {
                return mid; // Элемент найден
            } else if (comparison < 0) {
                right = mid - 1; // Ищем в левой половине
            } else {
                left = mid + 1; // Ищем в правой половине
            }
        }

        return -1; // Элемент не найден
    }

    /**
     * Находит позицию для вставки нового элемента (бинарный поиск позиции)
     * @return индекс, куда нужно вставить элемент
     */
    @SuppressWarnings("unchecked")
    private int findInsertionIndex(E element) {
        int left = 0;
        int right = size - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            E midElement = (E) elements[mid];
            int comparison = element.compareTo(midElement);

            if (comparison == 0) {
                return mid; // Элемент уже существует (не должно происходить)
            } else if (comparison < 0) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return left; // Позиция для вставки
    }

    /**
     * Удаляет элемент по указанному индексу со сдвигом элементов
     */
    private void removeElementAtIndex(int index) {
        // Сдвигаем элементы влево, начиная с позиции после удаляемого элемента
        if (index < size - 1) {
            System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        }

        // Обнуляем последний элемент и уменьшаем размер
        elements[size - 1] = null;
        size--;
    }

    /**
     * Увеличивает емкость массива при заполнении
     */
    private void resize() {
        capacity *= 2;
        Object[] newElements = new Object[capacity];

        // Копируем элементы в новый массив
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    // Методы интерфейса Collection

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Iterator not implemented");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("toArray not implemented");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("toArray not implemented");
    }

    @Override
    @SuppressWarnings("unchecked")
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
    @SuppressWarnings("unchecked")
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;

        // Создаем временный массив для элементов, которые нужно сохранить
        Object[] temp = new Object[capacity];
        int newSize = 0;

        // Проходим по текущим элементам и сохраняем только те, что есть в коллекции
        for (int i = 0; i < size; i++) {
            E current = (E) elements[i];
            if (c.contains(current)) {
                temp[newSize++] = current;
            } else {
                modified = true; // Нашли элемент для удаления
            }
        }

        // Заменяем старый массив новым
        if (modified) {
            elements = temp;
            size = newSize;
        }

        return modified;
    }
}