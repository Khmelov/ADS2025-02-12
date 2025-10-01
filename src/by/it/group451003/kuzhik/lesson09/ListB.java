package by.it.group451003.kuzhik.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {
    static final int DEFAULT_SIZE = 10;
    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    int size = 0;
    E[] aList = (E[]) new Object[DEFAULT_SIZE];
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0)
            return "[]";
        String res = "[" + aList[0];
        for (int i = 1; i < size; i++) {
            res += ", " + aList[i];
        }
        return res + "]";
    }

    @Override
    public boolean add(E e) {
        if (size == aList.length) {
            E[] newArr = (E[]) new Object[aList.length * 3 / 2 + 1];
            for (int i = 0; i < size; i++) {
                newArr[i] = aList[i];
            }
            aList = newArr;
        }
        aList[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        E removed = (E) aList[index];
        for (int i = index; i < size - 1; i++) {
            aList[i] = aList[i + 1];
        }
        aList[--size] = null;
        return removed;
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
        if (size == aList.length) {
            E[] newArr = (E[]) new Object[aList.length * 3 / 2 + 1];
            for (int i = 0; i < size; i++) {
                newArr[i] = aList[i];
            }
            aList = newArr;
        }

        for  (int i = size; i > index; i--) {
            aList[i] = aList[i - 1];
        }
        size++;
        aList[index] = element;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(get(i))) {
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
        E removed = aList[index];
        aList[index] = element;
        return removed;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        aList = (E[]) new Object[DEFAULT_SIZE];
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (aList[i] == null) {
                if (o == null)
                    return i;
            } else {
                if (aList[i].equals(o))
                    return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return aList[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (aList[i] == null) {
                if (o == null)
                    return i;
            } else {
                if (aList[i].equals(o))
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

}
