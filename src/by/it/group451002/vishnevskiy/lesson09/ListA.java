package by.it.group451002.vishnevskiy.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {

    // Внутренний массив для хранения элементов
    private E[] elements = (E[]) new Object[10];

    // Количество элементов в списке
    private int size = 0;

    // Возвращает строковое представление списка в формате [a, b, c]
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

    // Добавляет элемент в конец списка
    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1);
        elements[size++] = e;
        return true;
    }

    // Удаляет элемент по индексу, сдвигая остальные влево
    @Override
    public E remove(int index) {
        checkIndex(index);
        E old = elements[index];
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[size - 1] = null;
        size--;
        return old;
    }

    // Возвращает количество элементов
    @Override
    public int size() {
        return size;
    }

    // Вставляет элемент по указанному индексу
    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: " + index);
        ensureCapacity(size + 1);
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
    }

    // Удаляет первый найденный элемент, равный переданному объекту
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && elements[i] == null) || (o != null && o.equals(elements[i]))) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    // Заменяет элемент по индексу и возвращает старое значение
    @Override
    public E set(int index, E element) {
        checkIndex(index);
        E old = elements[index];
        elements[index] = element;
        return old;
    }

    // Возвращает элемент по индексу
    @Override
    public E get(int index) {
        checkIndex(index);
        return elements[index];
    }

    // Очищает список
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    // Проверяет, пуст ли список
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Возвращает индекс первого вхождения элемента
    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && elements[i] == null) || (o != null && o.equals(elements[i]))) {
                return i;
            }
        }
        return -1;
    }

    // Проверяет, содержится ли элемент в списке
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    // Преобразует список в массив
    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) {
            arr[i] = elements[i];
        }
        return arr;
    }

    // Проверяет корректность индекса
    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    // Расширяет массив при нехватке места
    private void ensureCapacity(int newCapacity) {
        if (newCapacity > elements.length) {
            int newLen = elements.length * 3 / 2 + 1;
            if (newLen < newCapacity)
                newLen = newCapacity;
            E[] newArr = (E[]) new Object[newLen];
            for (int i = 0; i < size; i++) {
                newArr[i] = elements[i];
            }
            elements = newArr;
        }
    }

    // Ниже — методы-заглушки (для совместимости с интерфейсом List)

    @Override public int lastIndexOf(Object o) { return -1; }
    @Override public boolean containsAll(Collection<?> c) { return false; }
    @Override public boolean addAll(Collection<? extends E> c) { return false; }
    @Override public boolean addAll(int index, Collection<? extends E> c) { return false; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }
    @Override public List<E> subList(int fromIndex, int toIndex) { return null; }
    @Override public ListIterator<E> listIterator(int index) { return null; }
    @Override public ListIterator<E> listIterator() { return null; }
    @Override public Iterator<E> iterator() { return null; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}
