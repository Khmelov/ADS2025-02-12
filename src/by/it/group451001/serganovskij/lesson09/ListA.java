package by.it.group451001.serganovskij.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {

    // Внутренний массив для хранения элементов и переменная для отслеживания размера
    private static final int INITIAL_CAPACITY = 10;
    private Object[] elements;
    private int size;

    // Конструктор инициализирует пустой массив с начальной емкостью
    public ListA() {
        elements = new Object[INITIAL_CAPACITY];
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    // ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ ИНТЕРФЕЙСА LIST
    /////////////////////////////////////////////////////////////////////////

    // Преобразование списка в строку для вывода
    @Override
    public String toString() {
        if (size == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    // Добавление элемента в конец списка
    @Override
    public boolean add(E e) {
        ensureCapacity(); // Проверка необходимости расширения массива
        elements[size++] = e;
        return true;
    }

    // Удаление элемента по индексу со сдвигом оставшихся элементов
    @Override
    public E remove(int index) {
        checkIndex(index);
        E removedElement = (E) elements[index];

        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;
        return removedElement;
    }

    // Возврат текущего количества элементов
    @Override
    public int size() {
        return size;
    }

    /////////////////////////////////////////////////////////////////////////
    // ОПЦИОНАЛЬНЫЕ МЕТОДЫ РЕАЛИЗАЦИИ
    /////////////////////////////////////////////////////////////////////////

    // Вставка элемента по указанному индексу
    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index);
        ensureCapacity();

        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
    }

    // Удаление первого вхождения объекта
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    // Замена элемента по индексу
    @Override
    public E set(int index, E element) {
        checkIndex(index);
        E oldElement = (E) elements[index];
        elements[index] = element;
        return oldElement;
    }

    // Проверка на пустоту списка
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Очистка списка - обнуление всех ссылок
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    // Поиск индекса первого вхождения элемента
    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) return i;
        }
        return -1;
    }

    // Получение элемента по индексу
    @Override
    public E get(int index) {
        checkIndex(index);
        return (E) elements[index];
    }

    // Проверка наличия элемента в списке
    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    // Поиск индекса последнего вхождения элемента
    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (o.equals(elements[i])) return i;
        }
        return -1;
    }

    /////////////////////////////////////////////////////////////////////////
    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ДЛЯ РАБОТЫ С ВНУТРЕННИМ МАССИВОМ
    /////////////////////////////////////////////////////////////////////////

    // Увеличение емкости массива при необходимости
    private void ensureCapacity() {
        if (size == elements.length) {
            Object[] newElements = new Object[elements.length * 2];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    // Проверка корректности индекса для операций доступа
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    // Проверка корректности индекса для операций вставки
    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    // МЕТОДЫ, КОТОРЫЕ МОЖНО НЕ РЕАЛИЗОВЫВАТЬ (ЗАГЛУШКИ)
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean containsAll(Collection<?> c) { return false; }

    @Override
    public boolean addAll(Collection<? extends E> c) { return false; }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) { return false; }

    @Override
    public boolean removeAll(Collection<?> c) { return false; }

    @Override
    public boolean retainAll(Collection<?> c) { return false; }

    @Override
    public List<E> subList(int fromIndex, int toIndex) { return null; }

    @Override
    public ListIterator<E> listIterator(int index) { return null; }

    @Override
    public ListIterator<E> listIterator() { return null; }

    @Override
    public <T> T[] toArray(T[] a) { return null; }

    @Override
    public Object[] toArray() { return new Object[0]; }

    // Простая реализация итератора для перебора элементов
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public E next() {
                return (E) elements[currentIndex++];
            }
        };
    }
}