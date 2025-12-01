package by.it.group410901.gutseva.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {

    // внутреннее хранилище
    private Object[] elements = new Object[10];
    private int size = 0;

    // метод для расширения массива при необходимости
    private void ensureCapacity(int newSize) {
        if (newSize > elements.length) {
            Object[] newArr = new Object[elements.length * 2];
            System.arraycopy(elements, 0, newArr, 0, size);
            elements = newArr;
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i=0; i<size; i++) {
            sb.append(elements[i]);
            if (i < size-1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        ensureCapacity(size+1);
        elements[size++] = e;
        return true;
    }

    @Override
    public void add(int index, E element) {
        if (index<0 || index>size) throw new IndexOutOfBoundsException();
        ensureCapacity(size+1);
        System.arraycopy(elements, index, elements, index+1, size-index);
        elements[index] = element;
        size++;
    }

    @Override
    public E remove(int index) {
        if (index<0 || index>=size) throw new IndexOutOfBoundsException();
        @SuppressWarnings("unchecked")
        E old = (E) elements[index];
        System.arraycopy(elements, index+1, elements, index, size-index-1);
        elements[--size] = null;
        return old;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexOf(o);
        if (idx>=0) {
            remove(idx);
            return true;
        }
        return false;
    }

    // заменяет элемент в указанной позиции новым элементом
    @Override
    public E set(int index, E element) {
        if (index<0 || index>=size) throw new IndexOutOfBoundsException();
        @SuppressWarnings("unchecked")
        E old = (E) elements[index];
        elements[index] = element;
        return old;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E get(int index) {
        if (index<0 || index>=size) throw new IndexOutOfBoundsException();
        @SuppressWarnings("unchecked")
        E value = (E) elements[index];
        return value;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public void clear() {
        for (int i=0; i<size; i++) elements[i] = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i=0; i<size; i++) {
            if (o==null ? elements[i]==null : o.equals(elements[i]))
                return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i=size-1; i>=0; i--) {
            if (o==null ? elements[i]==null : o.equals(elements[i]))
                return i;
        }
        return -1;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }
}
