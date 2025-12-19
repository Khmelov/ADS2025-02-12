package by.it.group451002.dirko.lesson09;

import java.util.*;

public class ListB<E> implements List<E> {
    private E[] elementData = (E[]) new Object[0];
    private int size = 0;

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() { return Arrays.toString(Arrays.copyOf(elementData, size)); }

    @Override
    public boolean add(E e) {
        if (size == elementData.length) {
            elementData = Arrays.copyOf(elementData, elementData.length * 2 + 1);
        }
        elementData[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) { throw new IndexOutOfBoundsException(); }
        E res = elementData[index];
        System.arraycopy(elementData, index + 1, elementData, index, elementData.length - index - 1);
        elementData[--size] = null;
        return res;
    }

    @Override
    public int size() { return size; }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) { throw new IndexOutOfBoundsException(); }
        if (size == elementData.length) {
            elementData = Arrays.copyOf(elementData, elementData.length * 2 + 1);
        }
        System.arraycopy(elementData, index , elementData, index + 1, elementData.length - index - 1);
        elementData[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (elementData[i].equals(o)) {
                System.arraycopy(elementData, i + 1, elementData, i, elementData.length - i - 1);
                elementData[--size] = null;
                return true;
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) { throw new IndexOutOfBoundsException(); }
        E res = elementData[index];
        elementData[index] = element;
        return res;
    }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        elementData = (E[]) new Object[0];
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(elementData[i])) { return i; }
        }
        return -1;
    }

    @Override
    public E get(int index) { return elementData[index]; }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(elementData[i])) { return true; }
        }
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size; i >= 0; i--) {
            if (o.equals(elementData[i])) { return i; }
        }
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
