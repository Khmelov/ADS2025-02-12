package by.it.group451003.khmilevskiy.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    static final int defaultSize = 8;
    int currentItem;
    E[] elem;

    public ListC()
    {
        this(defaultSize);
    }
    public ListC(int size)
    {
        elem = (E[]) new Object[size];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append('[');
        for (int i = 0; i < currentItem; i++)
        {
            sb.append(elem[i]);
            if (i < currentItem - 1)
            {
                sb.append(", ");
            }
        }
        sb.append("]");

        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        if (currentItem == elem.length)
        {
            E[] newElem = (E[]) new Object[elem.length * 2];
            for (int i = 0; i < elem.length; i++)
            {
                newElem[i] = elem[i];
            }
            elem = newElem;
        }

        elem[currentItem] = e;
        currentItem++;

        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= currentItem)
        {
            return null;
        }

        E removedItem = elem[index];

        for (int i = index; i < currentItem - 1; i++)
        {
            elem[i] = elem[i + 1];
        }

        currentItem--;

        return removedItem;
    }

    @Override
    public int size() {
        return currentItem;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > currentItem)
        {
            return;
        }

        if (currentItem == elem.length)
        {
            E[] listCopy = (E[]) new Object[elem.length * 2];
            for (int i = 0; i < elem.length; i++)
            {
                listCopy[i] = elem[i];
            }
            elem = listCopy;
        }

        for (int i = currentItem; i > index; i--)
        {
            elem[i] = elem[i - 1];
        }

        elem[index] = element;
        currentItem++;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < currentItem; i++)
        {
            if (o.equals(elem[i]))
            {
                E removedItem = elem[i];

                for (int j = i; j < currentItem - 1; j++)
                {
                    elem[j] = elem[j + 1];
                }

                currentItem--;

                return true;
            }
        }

        return false;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= currentItem)
        {
            return null;
        }

        E setElem = elem[index];

        elem[index] = element;

        return setElem;
    }


    @Override
    public boolean isEmpty() {
        return currentItem == 0;
    }


    @Override
    public void clear() {
        elem = (E[]) new Object[defaultSize];

        currentItem = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < currentItem; i++)
        {
            if (o.equals(elem[i]))
            {
                return i;
            }
        }

        return -1;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= currentItem)
        {
            return null;
        }

        return elem[index];
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < currentItem; i++)
        {
            if (o.equals(elem[i]))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = currentItem - 1; i >= 0; i--)
        {
            if (o.equals(elem[i]))
            {
                return i;
            }
        }

        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
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
    public boolean addAll(Collection<? extends E> c) {
        for (E item : c)
            add(item);
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean modified = false;
        for (E item : c) {
            if (index > -1 && index <= currentItem) {
                add(index, item);
                index++;
                modified = true;
            }
        }

        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean removed = false;

        for (int i = 0; i < currentItem; i++)
        {
            if (c.contains(elem[i]))
            {
                remove(i);
                i--;
                removed = true;
            }
        }

        return removed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean retained = false;

        for (int i = 0; i < currentItem; i++)
        {
            if (!c.contains(elem[i]))
            {
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
