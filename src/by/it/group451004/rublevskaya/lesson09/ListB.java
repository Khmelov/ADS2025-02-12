package by.it.group451004.rublevskaya.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {
    private static final int CAPACITY = 10;
    private Object[] data;
    private int currentSize;

    public ListB() {
        data = new Object[CAPACITY];
        currentSize = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder resultStr = new StringBuilder();
        resultStr.append("[");
        for (int i = 0; i < currentSize; i++) {
            resultStr.append(data[i]);
            if (i < currentSize - 1) {
                resultStr.append(", ");
            }
        }
        resultStr.append("]");
        return resultStr.toString();
    }

    @Override
    public boolean add(E e) {
        if (currentSize == data.length) {
            resize();
        }
        data[currentSize] = e;
        currentSize++;
        return true;
    }

    @Override
    public E remove(int index) {
        isCorrectIndex(index);
        E delElm = (E) data[index];
        int countMovedElms = currentSize - index - 1;
        if (countMovedElms > 0) {
            System.arraycopy(data, index + 1, data, index, countMovedElms);
        }
        currentSize--;
        data[currentSize] = null;
        return delElm;
    }

    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > currentSize) {
            throw new IndexOutOfBoundsException();
        }
        if (currentSize == data.length) {
            resize();
        }
        for (int i = currentSize; i > index; i--) {
            data[i] = data[i - 1];
        }
        data[index] = element;
        currentSize++;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index >= 0) {
            remove(index);
            return true;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= currentSize) {
            throw new IndexOutOfBoundsException();
        }
        E oldElement = (E) data[index];
        data[index] = element;
        return oldElement;
    }


    @Override
    public boolean isEmpty() {
        return currentSize == 0;
    }


    @Override
    public void clear() {
        for (int i = 0; i < currentSize; i++) {
            data[i] = null;
        }
        currentSize = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < currentSize; i++) {
            if (data[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= currentSize) {
            throw new IndexOutOfBoundsException();
        }
        return (E) data[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = currentSize - 1; i >= 0; i--) {
            if (data[i].equals(o)) {
                return i;
            }
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

    private void resize() {
        int newCapacity = data.length * 2;
        data = java.util.Arrays.copyOf(data, newCapacity);
    }

    private void isCorrectIndex(int currentIndex) {
        if (currentIndex < 0 || currentIndex >= currentSize) {
            throw new IndexOutOfBoundsException("Index: " + currentIndex + ", Size: " + currentSize);
        }
    }
}
