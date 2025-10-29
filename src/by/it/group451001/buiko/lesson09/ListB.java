package by.it.group451001.buiko.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private int size;
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elementData;

    public ListB(int capacity) {
        if(capacity > 0) {
            elementData = new Object[capacity];
        }
        else {
            elementData = new Object[0];
        }
    }
    public ListB() {
        this(0);
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    public String toString() {
        String sb = new String();
        if(size == 0) {return "[]";}
        sb += '[' + elementData[0].toString();
        for (int i = 1; i < size; i++) {
            sb += ", " + elementData[i].toString();
        }
        sb += "]";
        return sb;
    }
    @Override
    public boolean add(E e) {
        if(size == elementData.length) {
            Object[] newElementData = new Object[elementData.length + (elementData.length >> 1) + 1];
            System.arraycopy(elementData, 0, newElementData, 0, elementData.length);
            elementData = newElementData;

        }
        elementData[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        E Old = (E)elementData[index];
        System.arraycopy(elementData, index + 1, elementData, index, size - index - 1);
        elementData[--size] = null;
        return Old;
    }

    @Override
    public int size() {
        return size;
    }
    @Override
    public void add(int index, E element) {
        if(size == elementData.length) {
            Object[] newElementData = new Object[elementData.length + (elementData.length >> 1) + 1];
            System.arraycopy(elementData, 0, newElementData, 0, elementData.length);
            elementData = newElementData;
        }
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        elementData[index] = element;
        size++;
    }
    @Override
    public boolean remove(Object o) {
        for(int i = 0; i < size; i++) {
            if(elementData[i].equals(o)) {
                this.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        E Old = (E)elementData[index];
        elementData[index] = element;
        return Old;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        for(int i = 0; i < size; i++) {
            elementData[i] = null;
        }
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        for(int i = 0; i < size; i++) {
            if(elementData[i].equals(o)) {return i;}
        }
        return -1;
    }

    @Override
    public E get(int index) {
        return (E)elementData[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for(int i = size - 1; i >= 0; i--) {
            if(elementData[i].equals(o)) {return i;}
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
