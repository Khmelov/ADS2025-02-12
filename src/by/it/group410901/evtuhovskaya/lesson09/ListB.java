package by.it.group410901.evtuhovskaya.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size;

    public ListB() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    //юольше функционала

    @Override
    public String toString() { //строим список
        if (size == 0) {
            return "[]";
        }

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
    public boolean add(E e) { //добавить в конец
        ensureCapacity();
        elements[size] = e;
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);

        @SuppressWarnings("unchecked")
        E removedElement = (E) elements[index];

        //сдвиг влево
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        elements[size - 1] = null;
        size--;

        return removedElement;
    }

    @Override
    public int size() {
        return size;
    }

    /// НОВОЕ ///

    @Override
    public void add(int index, E element) { //добавление по индексу
        checkIndexForAdd(index); //ПРОВЕРКА ИНДЕКСА
        ensureCapacity(); //емкости

        //сдвиг вправо
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }

        elements[index] = element; //добавляем элемент
        size++;
    }

    @Override
    public boolean remove(Object o) { //удаление по индексу
        for (int i = 0; i < size; i++) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) { //замена элемента по индексу
        checkIndex(index);

        @SuppressWarnings("unchecked")
        E oldElement = (E) elements[index]; //сохраняем старый
        elements[index] = element; //меняем на новый

        return oldElement;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() { //очистка
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public int indexOf(Object o) { //поиск индекса элемента
        for (int i = 0; i < size; i++) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {   //обработка нулл и поиск по всем элементам
                                                                            // первое появление искомого
                return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) { //элемент по индексу
        checkIndex(index); //проверка корректности

        @SuppressWarnings("unchecked")
        E element = (E) elements[index]; //приводим к дженерику
        return element;
    }

    @Override
    public boolean contains(Object o) { //проверка наличия
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) { //поиск с конца

        //перебор с конца до последнего появления элемента
        for (int i = size - 1; i >= 0; i--) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    private void ensureCapacity() { //увеличение массива
        if (size == elements.length) {
            Object[] newElements = new Object[elements.length * 2];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    private void checkIndex(int index) { //проверка, чтобы индексы были на м
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private void checkIndexForAdd(int index) {   //проверка индексов с возможностью добавить в конец,
                                                // тк индекс мб = размеру
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
}