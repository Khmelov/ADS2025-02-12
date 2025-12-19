package by.it.group451002.vishnevskiy.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    // Внутренний массив для хранения элементов
    private E[] elements = (E[]) new Object[10];
    // Количество элементов в списке
    private int size = 0;

    // Возвращает строковое представление списка [a, b, c]
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

    // Удаляет элемент по индексу и возвращает его
    @Override
    public E remove(int index) {
        checkIndex(index);
        E old = elements[index];
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
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
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: " + index);
        ensureCapacity(size + 1);
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
    }

    // Удаляет первый найденный элемент
    @Override
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i >= 0) {
            remove(i);
            return true;
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
        for (int i = 0; i < size; i++) elements[i] = null;
        size = 0;
    }

    // Возвращает индекс первого вхождения элемента
    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && elements[i] == null) || (o != null && o.equals(elements[i])))
                return i;
        }
        return -1;
    }

    // Возвращает индекс последнего вхождения элемента
    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if ((o == null && elements[i] == null) || (o != null && o.equals(elements[i])))
                return i;
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

    // Добавляет все элементы другой коллекции
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            add(e);
            modified = true;
        }
        return modified;
    }

    // Добавляет все элементы другой коллекции начиная с указанного индекса
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: " + index);
        int added = c.size();
        if (added == 0) return false;
        ensureCapacity(size + added);
        for (int i = size - 1; i >= index; i--) {
            elements[i + added] = elements[i];
        }
        int i = index;
        for (E e : c) {
            elements[i++] = e;
        }
        size += added;
        return true;
    }

    // Проверяет наличие всех элементов коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o))
                return false;
        }
        return true;
    }

    // Удаляет все элементы, содержащиеся в другой коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            while (remove(o)) modified = true;
        }
        return modified;
    }

    // Оставляет только элементы, содержащиеся в другой коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(i);
                i--;
                modified = true;
            }
        }
        return modified;
    }

    // Проверяет корректность индекса
    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    // Расширяет массив при необходимости
    private void ensureCapacity(int newCapacity) {
        if (newCapacity > elements.length) {
            int newLen = elements.length * 3 / 2 + 1;
            if (newLen < newCapacity) newLen = newCapacity;
            E[] newArr = (E[]) new Object[newLen];
            for (int i = 0; i < size; i++)
                newArr[i] = elements[i];
            elements = newArr;
        }
    }

    // Преобразует список в массив
    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) arr[i] = elements[i];
        return arr;
    }

    // Ниже методы-заглушки, не требующие реализации
    @Override public List<E> subList(int fromIndex, int toIndex) { return null; }
    @Override public ListIterator<E> listIterator(int index) { return null; }
    @Override public ListIterator<E> listIterator() { return null; }
    @Override public Iterator<E> iterator() { return null; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}
