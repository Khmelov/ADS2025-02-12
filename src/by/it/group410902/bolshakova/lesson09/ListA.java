package by.it.group410902.bolshakova.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {
    private E[] elements = (E[]) new Object[8]; // Основное хранилище (массив)
    private int size = 0;

    private void ensureCapacity() {
        if (size == elements.length) {
            E[] newElements = (E[]) new Object[elements.length * 3 / 2 + 1];

            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i]; // скопировать элементы в новый массив
            }
            elements = newElements;
        }
    }

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
    public boolean add(E e) {
        ensureCapacity();
        elements[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        E removedElement = elements[index]; // сохранить удаляемый элемент

        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1]; //сдвиг на одну позицию влево всех элементов при удалении
        }

        elements[--size] = null; // уменьшаем размер и очищаем последний элемент
        return removedElement; //возвращение удалённого элемента
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        ensureCapacity();

        for (int i = size; i > index; i--) { // сдвиг элементов вправо
            elements[i] = elements[i - 1];
        }

        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && elements[i] == null) ||
                    (o != null && o.equals(elements[i]))) {//поиск элемента в массиве
                remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        E oldElement = elements[index];
        elements[index] = element;
        return oldElement;
    }

    @Override
    public boolean isEmpty() {
        return size == 0; // пустой, если размер равен 0
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && elements[i] == null) ||
                    (o != null && o.equals(elements[i]))) {
                return i; // возвращение индекса при нахождении
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return elements[index]; // Возвращаем элемент по индексу
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if ((o == null && elements[i] == null) ||
                    (o != null && o.equals(elements[i]))) {
                return i;
            }
        }
        return -1;
    }

    // Остальные методы остаются нереализованными
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
    @Override
    public Iterator<E> iterator() { return null; }
}