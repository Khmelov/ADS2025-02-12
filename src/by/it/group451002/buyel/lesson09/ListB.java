package by.it.group451002.buyel.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {


    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    private Object[] elements;
    private int size;

    public ListB() {
        this.elements = new Object[0];
        this.size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        String result = "[";
        for (int i = 0; i < this.size; i++) {
            result = result + elements[i];
            if (i < this.size - 1) {
                result = result + ", ";
            }
        }
        result = result + "]";
        return result;
    }

    @Override
    public boolean add(E e) {
        size += 1;
        Object[] newArr = new Object[size];

        for (int i = 0; i < size - 1; i++) {
            newArr[i] = elements[i];
        }
        newArr[size-1] = e;
        this.elements = newArr;

        return true;
    }

    @Override
    public E remove(int index) {
        if (size == 0) {
            return null;
        }

        size -= 1;
        Object[] newArr = new Object[size];
        int passedInd = 0;
        E removedElem = (E) elements[index];

        for (int i = 0; i <= size; i++) {
            if (i != index) {
                newArr[i - passedInd] = elements[i];
            } else {
                passedInd = 1;
            }
        }
        this.elements = newArr;
        return removedElem;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void add(int index, E element) {
        int i = 0;
        this.size += 1;
        Object[] result = new Object[this.size];

        while (i < index) {
            result[i] = this.elements[i];
            i++;
        }
        result[i] = element;
        for (int j = i+1; j < this.size; j++) {
            result[j] = elements[j-1];
        }

        this.elements = result;
    }

    @Override
    public boolean remove(Object o) {
        boolean isIn = false;
        for (int i = 0; i < this.size; i++) {
            if (o.equals(elements[i])) {
                isIn = true;
            }
        }

        if (isIn) {
            int i = 0;
            this.size -= 1;
            Object[] result = new Object[this.size];

            while (!o.equals(elements[i]) && i < this.size) {
                result[i] = elements[i];
                i++;
            }
            for (int j = i+1; j < this.size+1; j++) {
                result[j-1] = elements[j];
            }

            this.elements = result;
            return true;
        }

        return false;
    }

    @Override
    public E set(int index, E element) {
        Object retValue = elements[index];
        elements[index] = element;
        return (E) retValue;
    }


    @Override
    public boolean isEmpty() {
        return (this.size == 0);
    }


    @Override
    public void clear() {
        this.size = 0;
        this.elements = new Object[0];
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < this.size; i++) {
                if (elements[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < this.size; i++) {
                if (o.equals(elements[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        return (E) elements[index];
    }

    @Override
    public boolean contains(Object o) {
        int isIn = 0, temp;
        for (int i = 0; i < this.size; i++) {
            temp = (o.equals(elements[i])) ? 1 : 0;
            isIn += temp;
        }
        return (isIn > 0);
    }

    @Override
    public int lastIndexOf(Object o) {
        int index = -1;
        for (int i = 0; i < this.size; i++) {
            if (o.equals(elements[i])) {
                index = i;
            }
        }
        return index;
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
