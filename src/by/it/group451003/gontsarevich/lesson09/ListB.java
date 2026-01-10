package by.it.group451003.gontsarevich.lesson09;

import java.util.*;

public class ListB<E> implements List<E> {


    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    private static int capacity = 16;
    private int length = 0;
    E[] array;

    public ListB(int capacity) {
        array = (E[]) new Object[capacity];
        this.capacity = capacity;
    }

    public ListB() {
        this(capacity);
    }

    @Override
    public String toString() {
        StringBuilder strbldr = new StringBuilder();
        strbldr.append("[");

        int i = 0;
        while (i < length) {
            strbldr.append(array[i] + ", ");
            i++;
        }

        int sbLength = strbldr.length();
        if (sbLength > 1)
            strbldr.delete(sbLength - 2, sbLength);
        strbldr.append("]");

        return strbldr.toString();
    }

    @Override
    public boolean add(E e) {
        length++;

        if (length >= capacity) {
            E[] tempArr = (E[]) new Object[capacity += 16];

            for (int i = 0; i < length - 1; i++)
                tempArr[i] = array[i];

            array = tempArr;
        }
        array[length - 1] = e;

        return true;
    }

    @Override
    public E remove(int index) {
        if (index > length || index < 0)
            return null;

        else {
            E element = array[index];

            length--;

            for (int i = index; i < length; i++)
                array[i] = array[i + 1];

            return element;
        }
    }

    @Override
    public int size() {
        return length;
    }


    @Override
    public void add(int index, E element) {
        length++;

        if (length >= capacity) {
            E[] tempArr = (E[]) new Object[capacity += 16];

            for (int i = 0; i < length; i++)
                tempArr[i] = array[i];

            array = tempArr;
        }
        for (int i = length - 1; i > index; i--)
            array[i] = array[i - 1];

        array[index] = element;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < length; i++)
            if (array[i].equals(o)) {
                length--;

                for (int j = i; j < length; j++)
                    array[j] = array[j + 1];

                return true;
            }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if (index > length || index < 0)
            return null;

        else {
            E newElement = array[index];
            array[index] = element;

            return newElement;
        }
    }


    @Override
    public boolean isEmpty() {
        return length == 0;
    }

    @Override
    public void clear() {
        capacity =  16;
        array = (E[]) new Object[capacity];
        length = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < length; i++)
            if (array[i].equals(o))
                return i;

        return -1;
    }

    @Override
    public E get(int index) {
        if (index > length || index < 0)
            return null;
        else
            return array[index];
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < length; i++)
            if (array[i].equals(o))
                return true;
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = length - 1; i >= 0; i--)
            if (array[i].equals(o))
                return i;
        return -1;
    }


    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
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
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return null;
    }

}
