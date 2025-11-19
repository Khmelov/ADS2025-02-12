package by.it.group410901.tomashevich.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
//Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

public class ListA<E> implements List<E> {

    // Внутренний массив для хранения элементов
    private E[] elements = (E[]) new Object[10];
    private int size = 0; // текущее количество элементов

    // Увеличение массива при переполнении
    private void grow() {
        E[] newArray = (E[]) new Object[elements.length * 2];
        System.arraycopy(elements, 0, newArray, 0, size);
        elements = newArray;
    }

    // Проверка корректности индекса
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // Добавление элемента в конец
    @Override
    public boolean add(E e) {
        if (size == elements.length) {
            grow();
        }
        elements[size++] = e;
        return true;
    }

    // Удаление по индексу
    @Override
    public E remove(int index) {
        checkIndex(index);
        E old = elements[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null;
        return old;
    }

    @Override
    public int size() {
        return size;
    }

    // Остальные методы пока не реализованы (заглушки)
    @Override public void add(int index, E element) {}
    @Override public boolean remove(Object o) { return false; }
    @Override public E set(int index, E element) { return null; }
    @Override public boolean isEmpty() { return false; }
    @Override public void clear() {}
    @Override public int indexOf(Object o) { return 0; }
    @Override public E get(int index) { return null; }
    @Override public boolean contains(Object o) { return false; }
    @Override public int lastIndexOf(Object o) { return 0; }
    @Override public boolean containsAll(Collection<?> c) { return false; }
    @Override public boolean addAll(Collection<? extends E> c) { return false; }
    @Override public boolean addAll(int index, Collection<? extends E> c) { return false; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }
    @Override public List<E> subList(int fromIndex, int toIndex) { return null; }
    @Override public ListIterator<E> listIterator(int index) { return null; }
    @Override public ListIterator<E> listIterator() { return null; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public Iterator<E> iterator() { return null; }
}
