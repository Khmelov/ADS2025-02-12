package by.it.group451003.shepetuyk.lesson09;

import java.util.*;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    static final int defaultSize = 8;
    E[] _list;
    int _current;

    public ListC() {
        this(defaultSize);
    }


    public ListC(int size) {
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
        if (_current == _list.length) {
            E[] newList = (E[]) new Object[_list.length * 2];
            for (int i = 0; i < _list.length; i++) {
                newList[i] = _list[i];
            }
            _list = newList;
        }
        _list[_current++] = e;
        return true;
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

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c)
            if (!contains(item))
                return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E item : c)
            add(item);
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean modified = false;
        for (E item : c) {
            if (index > -1 && index <= _current) {
                add(index, item);
                index++;
                modified = true;
            }
        }

        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean deleted = false;

        for (int i = 0; i < _current; i++) {
            if (c.contains(_list[i])) {
                remove(i);
                i--; // Уменьшаем счетчик, так как размер списка уменьшился
                deleted = true;
            }
        }
        return deleted;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean retained = false;

        for (int i = 0; i < _current; i++) {
            if (!c.contains(_list[i])) {
                remove(i);
                i--;
                retained = true;
            }
        }
        return retained;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > _current || fromIndex > toIndex) {
            ListC<E> subList = new ListC<>(toIndex - fromIndex);
            for (int i = fromIndex; i < toIndex; i++)
                subList.add(_list[i]);
            return subList;
        }
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
        Object[] result = new Object[_current];
        for (int i = 0; i < _current; i++) {
            result[i] = _list[i];
        }
        return result;
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
