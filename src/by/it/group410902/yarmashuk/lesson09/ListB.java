package by.it.group410902.yarmashuk.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {
    private Object[] data; // Используем Object[], т.к. E - generic
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public ListB() {
        this(DEFAULT_CAPACITY);
    }

    public ListB(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Размер должен быть не отрицательным");
        }
        this.data = new Object[initialCapacity];
        this.size = 0;
    }


    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1);
        data[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        E removedElement = (E) data[index];
        System.arraycopy(data, index + 1, data, index, size - index - 1); // Сдвиг элементов
        data[--size] = null; // Освобождаем место для GC
        return removedElement;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index);
        ensureCapacity(size + 1);
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? data[i] == null : o.equals(data[i])) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        E oldElement = (E) data[index];
        data[index] = element;
        return oldElement;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;  // Освобождаем ссылки для GC
        }
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? data[i] == null : o.equals(data[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        return (E) data[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (o == null ? data[i] == null : o.equals(data[i])) {
                return i;
            }
        }
        return -1;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > data.length) {
            grow(minCapacity);
        }
    }
    private void grow(int minCapacity) {
        int oldCapacity = data.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1); // oldCapacity * 1.5
        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }
        if (newCapacity > Integer.MAX_VALUE - 8) { // Avoid potential overflow
            newCapacity = (minCapacity > Integer.MAX_VALUE - 8) ? Integer.MAX_VALUE : Integer.MAX_VALUE - 8;
        }
        data = java.util.Arrays.copyOf(data, newCapacity); // Используем Arrays.copyOf для создания нового массива
    }
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
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
        throw new UnsupportedOperationException("");
    }

}
