package by.it.group410901.tomashevich.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/*
    Задание:
    Создайте аналог списка (динамического массива), без использования стандартных коллекций.
    Этот список должен поддерживать базовые операции: добавление, удаление, доступ по индексу,
    замена элемента и т.п.
*/
public class ListB<E> implements List<E> {

    // Массив для хранения элементов
    private E[] elements = (E[]) new Object[10];
    // Количество элементов в списке
    private int size = 0;

    // Увеличение массива при нехватке места
    private void grow() {
        E[] newArray = (E[]) new Object[elements.length * 2];
        System.arraycopy(elements, 0, newArray, 0, size);
        elements = newArray;
    }

    // Проверка индекса на выход за границы
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    // Возвращает строковое представление списка (для отладки и вывода)
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

    // Добавляет элемент в конец списка
    @Override
    public boolean add(E e) {
        if (size == elements.length) {
            grow();
        }
        elements[size++] = e;
        return true;
    }

    // Удаляет элемент по индексу и возвращает его
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

    // Возвращает количество элементов
    @Override
    public int size() {
        return size;
    }

    // Вставляет элемент по индексу
    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (size == elements.length) {
            grow();
        }
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }

    // Удаляет первый найденный элемент по значению
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (elements[i] == null ? o == null : elements[i].equals(o)) {
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

    // Проверяет, пуст ли список
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Очищает список
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    // Возвращает индекс первого вхождения элемента или -1
    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (elements[i] == null ? o == null : elements[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    // Возвращает элемент по индексу
    @Override
    public E get(int index) {
        checkIndex(index);
        return elements[index];
    }

    // Проверяет, содержится ли элемент в списке
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    // Возвращает индекс последнего вхождения элемента или -1
    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (elements[i] == null ? o == null : elements[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    // Остальные методы оставлены пустыми (необязательная реализация)
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
