package by.it.group451001.russu.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {


    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    E[] elems = (E[]) new Object[0];
    int count;
    @Override
    public String toString()
    {

        if (count<=0) return "[]";
        StringBuilder sb = new StringBuilder();
        if (count>0){
            sb.append("[");


        for (int i = 0; i< count;i++)
        {
            sb.append(elems[i] + ", ");
        }
            sb.delete(sb.length()-2,sb.length());
            sb.append("]");}
        return sb.toString();
    }

    @Override
    public boolean add(E e)
    {
        count++;
        E[] array = (E[]) new Object[count];
        for (int i = 0; i< count-1;i++)
        {
            array[i] = elems[i];
        }
        array[count-1] = e;

        elems = array;

        return true;
    }

    @Override
    public E remove(int index)
    {
        E ret = null;
        count--;
        int d = 0;
        E[] array = (E[]) new Object[count];
        for (int i = 0; i< count+1;i++)
        {
            if (index==i)
            {
                ret = elems[i];d++;
            }
            else{
                array[i-d] = elems[i];}
        }
        elems = array;
        return ret;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public void add(int index, E element) {
        count++;
        E[] array = (E[]) new Object[count];

        for (int i = 0; i < index; i++) {
            array[i] = elems[i];
        }

        array[index] = element;

        for (int i = index; i < count - 1; i++) {
            array[i + 1] = elems[i];
        }
        elems = array;
    }

    @Override
    public boolean remove(Object o) {
        if (contains(o)){
        count--;
        int d = 0;
        E[] array = (E[]) new Object[count];
        for (int i = 0; i< count+1;i++)
        {
            if (elems[i].equals(o))
            {
                d++;
            }
            else{
                array[i-d] = elems[i];}
        }
        elems = array;
        return true;}
        return false;
    }

    @Override
    public E set(int index, E element)
    {
        E ret = null;
        for (int i = 0; i< count;i++)
        {
            if (index==i)
            {
                ret = elems[i];
                elems[i] = element;
                break;
            }
        }
        return ret;
    }


    @Override
    public boolean isEmpty() {
        return count<=0;
    }


    @Override
    public void clear() {
        for (int i = 0; i < count;i++)
            elems[i] = null;
        count = 0;
    }

    @Override
    public int indexOf(Object o)
    {
        for (int i = 0; i< count;i++)
        {
            if (elems[i].equals(o))
            {
                return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        return elems[index];
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i< count;i++)
        {
            if ( elems[i].equals(o))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        int ret = -1;
        for (int i = 0; i< count;i++)
        {
            if ( elems[i].equals(o))
            {
                ret = i;
            }
        }
        return ret;
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
