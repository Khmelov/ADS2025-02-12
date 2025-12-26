package by.it.group410901.korneew.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    private Object[] elementData;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public ListC() {
        elementData = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity <= elementData.length) return;
        int newCapacity = elementData.length + (elementData.length >> 1);
        if (newCapacity < minCapacity) newCapacity = minCapacity;
        Object[] newArr = new Object[newCapacity];
        System.arraycopy(elementData, 0, newArr, 0, size);
        elementData = newArr;
    }

    private void rangeCheck(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            sb.append(elementData[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1);
        elementData[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        rangeCheck(index);
        @SuppressWarnings("unchecked")
        E old = (E) elementData[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elementData, index + 1, elementData, index, numMoved);
        }
        elementData[--size] = null;
        return old;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        ensureCapacity(size + 1);
        int numMoved = size - index;
        if (numMoved > 0) {
            System.arraycopy(elementData, index, elementData, index + 1, numMoved);
        }
        elementData[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexOf(o);
        if (idx == -1) return false;
        remove(idx);
        return true;
    }

    @Override
    public E set(int index, E element) {
        rangeCheck(index);
        @SuppressWarnings("unchecked")
        E old = (E) elementData[index];
        elementData[index] = element;
        return old;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) elementData[i] = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++)
                if (elementData[i] == null) return i;
        } else {
            for (int i = 0; i < size; i++)
                if (o.equals(elementData[i])) return i;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        rangeCheck(index);
        @SuppressWarnings("unchecked")
        E e = (E) elementData[index];
        return e;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--)
                if (elementData[i] == null) return i;
        } else {
            for (int i = size - 1; i >= 0; i--)
                if (o.equals(elementData[i])) return i;
        }
        return -1;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Дополнительно реализованные методы            //////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) if (!contains(item)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Object[] a = c.toArray();
        int numNew = a.length;
        if (numNew == 0) return false;
        ensureCapacity(size + numNew);
        System.arraycopy(a, 0, elementData, size, numNew);
        size += numNew;
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        Object[] a = c.toArray();
        int numNew = a.length;
        if (numNew == 0) return false;
        ensureCapacity(size + numNew);
        int numMoved = size - index;
        if (numMoved > 0) {
            System.arraycopy(elementData, index, elementData, index + numNew, numMoved);
        }
        System.arraycopy(a, 0, elementData, index, numNew);
        size += numNew;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object item : c) {
            int idx;
            while ((idx = indexOf(item)) != -1) {
                remove(idx);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        int write = 0;
        for (int read = 0; read < size; read++) {
            Object elem = elementData[read];
            if (c.contains(elem)) {
                elementData[write++] = elem;
            } else {
                modified = true;
            }
        }
        for (int i = write; i < size; i++) elementData[i] = null;
        if (modified) size = write;
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные / заглушки методов                //////
    /////////////////////////////////////////////////////////////////////////

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
        if (a.length < size) {
            @SuppressWarnings("unchecked")
            T[] newArr = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
            a = newArr;
        }
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        System.arraycopy(elementData, 0, arr, 0, size);
        return arr;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {
        private int cursor = 0;
        private int lastRet = -1;

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            if (cursor >= size) throw new java.util.NoSuchElementException();
            @SuppressWarnings("unchecked")
            E e = (E) elementData[cursor];
            lastRet = cursor++;
            return e;
        }

        @Override
        public void remove() {
            if (lastRet < 0) throw new IllegalStateException();
            ListC.this.remove(lastRet);
            cursor = lastRet;
            lastRet = -1;
        }
    }
}