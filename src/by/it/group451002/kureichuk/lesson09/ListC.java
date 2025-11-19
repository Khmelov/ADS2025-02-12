package by.it.group451002.kureichuk.lesson09;

import java.util.*;

public class ListC<E> implements List<E> {
    private E[] elem;
    private int size;
    public ListC() {
        elem = (E[])new Object[1];
        size = 0;
    }
    public ListC(int capacity) {
        elem = (E[])new Object[capacity];
        size = 0;
    }
    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            res.append(elem[i]);
            if (i < size - 1) res.append(", ");
        }
        res.append("]");
        return res.toString();
    }

    @Override
    public boolean add(E e) {
        if (elem.length == size) {
            int newSize = elem.length * 2;
            E[] newElem = (E[])new Object[newSize];
            System.arraycopy(elem, 0, newElem, 0, size);
            elem = newElem;
        }

        elem[size] = e;
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        E removed = elem[index];
        for (int i = index; i < size - 1; i++) {
            elem[i] = elem[i+1];
        }

        elem[size] = null;
        --size;
        return removed;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (elem.length == size) {
            int newSize = elem.length * 2;
            E[] newElem = (E[])new Object[newSize];
            System.arraycopy(elem, 0, newElem, 0, size);
            elem = newElem;
        }

        E next = element;
        for (int i = index; i <= size; i++) {
            E tmp = elem[i];
            elem[i] = next;
            next = tmp;
        }
        size++;
    }

    @Override
    public boolean remove(Object o) {
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (o.equals(elem[i])) {
                index = i;
                break;
            }
        }
        if (index == -1) return false;

        for (int i = index; i < size - 1; i++) {
            elem[i] = elem[i+1];
        }
        elem[size] = null;
        --size;
        return true;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        E prev = elem[index];
        elem[index] = element;
        return prev;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        elem = (E[]) new Object[1];
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(elem[i])) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return elem[index];
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(elem[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (o.equals(elem[i])) {
                index = i;
            }
        }
        return index;
    }


    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("Collection to add cannot be null");
        }
        for (Object o : c) {
            if ( !this.contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException("Collection to add cannot be null");
        }
        for (E o : c) {
            this.add(o);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException("Collection to add cannot be null");
        }
        int i = index;
        for (E o : c) {
            this.add(i, o);
            i++;
        }

        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean res = false;
        for (int i = 0; i < size; ) {
            if (c.contains(elem[i])) {
                remove(i);
                res = true;
            } else {
                i++;
            }
        }
        return res;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("Collection to add cannot be null");
        }
        boolean res = false;
        for (int i = 0; i < size;) {
            if (!c.contains(elem[i])) {
                remove(elem[i]);
                res = true;
            } else {
                i++;
            }
        }

        return res;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

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
        return new Iterator<E>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                return elem[index++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove not supported");
            }
        };
    }


}
