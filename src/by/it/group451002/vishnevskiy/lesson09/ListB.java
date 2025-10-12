package by.it.group451002.vishnevskiy.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Arrays;
import java.util.Objects;


public class ListB<E> implements List<E> {

    // Внутренний массив, где хранятся элементы
    @SuppressWarnings("unchecked") // подавляем предупреждение о небезопасном приведении
    private E[] elements = (E[]) new Object[10]; // начальный размер — 10

    private int size = 0; // текущее количество элементов

    // Вывод списка в виде строки, например: [A, B, C]
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1)
                sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // Добавление элемента в конец списка
    @Override
    public boolean add(E e) {
        // если массив заполнен — увеличиваем в 2 раза
        if (size == elements.length)
            elements = Arrays.copyOf(elements, elements.length * 2);
        elements[size++] = e;
        return true;
    }

    // Добавление по индексу
    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException();
        if (size == elements.length)
            elements = Arrays.copyOf(elements, elements.length * 2);
        // сдвигаем элементы вправо
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
    }

    // Замена элемента по индексу
    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        E old = elements[index];
        elements[index] = element;
        return old;
    }

    // Удаление элемента по индексу
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        E removed = elements[index];
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;
        return removed;
    }

    // Удаление по значению
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, elements[i])) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    // Возвращает элемент по индексу
    @Override
    public E get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        return elements[index];
    }

    // Возвращает количество элементов
    @Override
    public int size() {
        return size;
    }

    // Проверка, пустой ли список
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Проверка, содержится ли элемент в списке
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    // Поиск первого вхождения элемента
    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++)
            if (Objects.equals(o, elements[i]))
                return i;
        return -1;
    }

    // Поиск последнего вхождения элемента
    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--)
            if (Objects.equals(o, elements[i]))
                return i;
        return -1;
    }

    // Очистка списка
    @Override
    public void clear() {
        for (int i = 0; i < size; i++)
            elements[i] = null;
        size = 0;
    }


    @Override public Iterator<E> iterator() { return null; }
    @Override public ListIterator<E> listIterator() { return null; }
    @Override public ListIterator<E> listIterator(int index) { return null; }
    @Override public List<E> subList(int fromIndex, int toIndex) { return null; }

    @Override public boolean containsAll(Collection<?> c) { return false; }
    @Override public boolean addAll(Collection<? extends E> c) { return false; }
    @Override public boolean addAll(int index, Collection<? extends E> c) { return false; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }

    @Override public Object[] toArray() { return Arrays.copyOf(elements, size); }
    @Override public <T> T[] toArray(T[] a) { return null; }
}
