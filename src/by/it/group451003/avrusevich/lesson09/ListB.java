package by.it.group451003.avrusevich.lesson09;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E>
{

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    static int _initialSize = 8;
    E[] _list;
    int _currentItemIndex = 0;

    public ListB()
    {
        this(_initialSize);
    }
    public ListB(int size)
    {
        _list = (E[]) new Object[size];
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append('[');
        for (int i = 0; i < _currentItemIndex; i++)
        {
            sb.append(_list[i]);
            if (i < _currentItemIndex - 1)
            {
                sb.append(", ");
            }
        }
        sb.append("]");

        return sb.toString();
    }

    @Override
    public boolean add(E e)
    {
        if (_currentItemIndex == _list.length)
        {
            E[] listCopy = (E[]) new Object[_list.length * 2];
            for (int i = 0; i < _list.length; i++)
            {
                listCopy[i] = _list[i];
            }
            _list = listCopy;
        }

        _list[_currentItemIndex] = e;
        _currentItemIndex++;

        return true;
    }

    @Override
    public E remove(int index)
    {
        if (index < 0 || index >= _currentItemIndex)
        {
            return null;
        }

        E removedItem = _list[index];

        for (int i = index; i < _currentItemIndex - 1; i++)
        {
            _list[i] = _list[i + 1];
        }

        _currentItemIndex--;

        return removedItem;
    }

    @Override
    public int size() {
        return _currentItemIndex;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public void add(int index, E element)
    {
        if (index < 0 || index > _currentItemIndex)
        {
            return;
        }

        if (_currentItemIndex == _list.length)
        {
            E[] listCopy = (E[]) new Object[_list.length * 2];
            for (int i = 0; i < _list.length; i++)
            {
                listCopy[i] = _list[i];
            }
            _list = listCopy;
        }

        for (int i = _currentItemIndex; i > index; i--)
        {
            _list[i] = _list[i - 1];
        }

        _list[index] = element;
        _currentItemIndex++;
    }

    @Override
    public boolean remove(Object o)
    {
        for (int i = 0; i < _currentItemIndex; i++)
        {
            if (o.equals(_list[i]))
            {
                E removedItem = _list[i];

                for (int j = i; j < _currentItemIndex - 1; j++)
                {
                    _list[j] = _list[j + 1];
                }

                _currentItemIndex--;

                return true;
            }
        }

        return false;
    }

    @Override
    public E set(int index, E element)
    {
        if (index < 0 || index >= _currentItemIndex)
        {
            return null;
        }

        E setItem = _list[index];

        _list[index] = element;

        return setItem;
    }


    @Override
    public boolean isEmpty() {
        return _currentItemIndex == 0;
    }


    @Override
    public void clear()
    {
        _list = (E[]) new Object[_initialSize];

        _currentItemIndex = 0;
    }

    @Override
    public int indexOf(Object o)
    {
        for (int i = 0; i < _currentItemIndex; i++)
        {
            if (o.equals(_list[i]))
            {
                return i;
            }
        }

        return -1;
    }

    @Override
    public E get(int index)
    {
        if (index < 0 || index >= _currentItemIndex)
        {
            return null;
        }

        return _list[index];
    }

    @Override
    public boolean contains(Object o)
    {
        for (int i = 0; i < _currentItemIndex; i++)
        {
            if (o.equals(_list[i]))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public int lastIndexOf(Object o)
    {
        for (int i = _currentItemIndex - 1; i >= 0; i--)
        {
            if (o.equals(_list[i]))
            {
                return i;
            }
        }

        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        for (Object item : c)
        {
            if (!contains(item))
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c)
    {
        for (Object item : c)
        {
            add((E) item);
        }

        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c)
    {
        for (Object item : c)
        {
            add(index, (E) item);
            index++;
        }

        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        for (Object item : c)
        {
            if (!remove((E) item))
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        boolean retained = false;

        for (int i = 0; i < _currentItemIndex; i++)
        {
            if (!c.contains(_list[i]))
            {
                remove(i);
                i--;
                retained = true;
            }
        }

        return retained;
    }


    @Override
    public List<E> subList(int fromIndex, int toIndex)
    {
        ListA<E> subList = new ListA<E>(toIndex - fromIndex + 1);

        for (int i = 0; i < subList.size(); i++)
        {
            subList.add(_list[i + fromIndex]);
        }

        return subList;
    }

    @Override
    public ListIterator<E> listIterator(int index)
    {
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
    public Object[] toArray()
    {
        return _list;
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