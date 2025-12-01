package by.it.group451001.ivanov.lesson09;

import java.util.*;

public class ListB<E> implements List<E> {


    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    static final int defaultSize = 8;
    E[] _list;
    int _current;

    public ListB() {
        this(defaultSize);
    }


    public ListB(int size) {
        _list = (E[]) new Object[size];
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < _current; i++) {
            sb.append(_list[i]);
            if (i < _current - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        ensureCapacity(_current + 1);
        _list[_current++] = e;
        return true;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > _list.length) {
            int newCapacity = Math.max(_list.length * 2, minCapacity);
            E[] newArray = (E[]) new Object[newCapacity];
            System.arraycopy(_list, 0, newArray, 0, _current);
            _list = newArray;
        }
    }

    @Override
    public E remove(int index) {
        if (index > -1 && index < _current) {
            E elem = _list[index];
            for (int i = index; i < _current - 1; i++)
                _list[i] = _list[i + 1];
            _current--;
            return elem;
        }
        return null;
    }

    @Override
    public int size() {
        return _current;
    }

    @Override
    public void add(int index, E element) {
        if (_current == _list.length) {
            E[] newList = (E[]) new Object[_list.length * 2];
            for (int i = 0; i < _list.length; i++) {
                newList[i] = _list[i];
            }
            _list = newList;
        }
        if (index > -1 && index <= _current) {
            for (int i = _current; i > index; i--) {
                _list[i] = _list[i - 1];
            }
            _list[index] = element;
            _current++;
        }
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index != -1) {
            remove(index);
            return true;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        E item = null;
        if (index > -1 && index < _current) {
            item = _list[index];
            _list[index] = element;
        }
        return item;
    }


    @Override
    public boolean isEmpty() {
        return _current == 0;
    }


    @Override
    public void clear() {
        for (int i = 0; i < _current; i++) {
            _list[i] = null;
        }
        _current = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < _current; i++) {
            if (Objects.equals(_list[i], o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if (index > -1 && index < _current)
            return _list[index];
        return null;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = _current - 1; i > -1; i--) {
            if (Objects.equals(o, _list[i]))
                return i;
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
