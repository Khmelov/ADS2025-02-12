package by.it.group451004.ivanov.lesson09;

import java.util.*;

//ARRAYLIST
public class    ListA<E> implements List<E> {

    private Object[] arr;
    private int usedLength;
    final int startLength = 128;

    public ListA(E... elements)
    {
        int length = startLength;
        while (length < elements.length) {
            length = Integer.MAX_VALUE / 2 <= length ? Integer.MAX_VALUE: length * 2;
        }
        arr = Arrays.copyOf(elements, length);
        usedLength = elements.length;
    }
    public ListA()
    {
        arr = new Object[startLength];
        usedLength = 0;
    }

    @Override
    public String toString() {
        if (usedLength == 0) {
            return "[]";
        }

        StringBuilder result = new StringBuilder();
        result.append("[");
        for (int i = 0; i < usedLength; i++) {
            if (i > 0) result.append(", ");
            if (arr[i] != null) {
                result.append(arr[i]);
            } else {
                result.append("null");
            }
        }
        result.append("]");

        return result.toString();
    }

    @Override
    public boolean add(E e) {
        if (usedLength == Integer.MAX_VALUE)
            return false;

        while (usedLength >= arr.length) //расширяем - всегда удачно
        {
            int length = Integer.MAX_VALUE / 2 <= arr.length ? Integer.MAX_VALUE: arr.length * 2;
            arr = Arrays.copyOf(arr, length);
        }
        //на выходе usedLength < arr.Length, поэтому ошибки ниже не будет
        arr[usedLength++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= usedLength)
            throw new IndexOutOfBoundsException();

        E result = ((E) arr[index]);

        usedLength--;
        for (int i = index; i < usedLength; i++)
        {
            arr[i] = arr[i+1];
        }
        arr[usedLength] = null;
        return result;
    }

    @Override
    public int size() {
        return usedLength;
    }

    @Override
    public void add(int index, E element) {
        if (usedLength == Integer.MAX_VALUE)
            throw new IllegalStateException("List has reached maximum capacity");

        if (index < 0 || index >= usedLength)
            throw new IndexOutOfBoundsException();

        while (usedLength >= arr.length) //расширяем - всегда удачно
        {
            int length = Integer.MAX_VALUE / 2 <= arr.length ? Integer.MAX_VALUE: arr.length * 2;
            arr = Arrays.copyOf(arr, length);
        }
        //на выходе usedLength < arr.Length, поэтому ошибки ниже не будет

        for (int i = usedLength; i > index ; i--) {
            arr[i] = arr[i-1];
        }
        arr[index] = element;
        usedLength++;
    }

    @Override
    public boolean remove(Object o) {
        for(int i = 0; i < usedLength; i++) {
            if (arr[i].equals(o)){
                remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= usedLength)
            throw new IndexOutOfBoundsException();
        E result = (E)arr[index];
        arr[index] = element;
        return result;
    }


    @Override
    public boolean isEmpty() {
        return usedLength == 0;
    }


    @Override
    public void clear() {
        for(int i = 0; i < usedLength; i++) {
            arr[i] = null;
        }
        usedLength = 0;
    }

    @Override
    public int indexOf(Object o) {
        for(int i = 0; i < usedLength; i++) {
            if (arr[i].equals(o)){
                return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= usedLength)
            throw new IndexOutOfBoundsException();
        return (E)arr[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for(int i = usedLength - 1; i >= 0; i--) {
            if (arr[i].equals(o)){
                return i;
            }
        }
        return -1;
    }

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
        if (fromIndex < 0 || fromIndex >= usedLength)
            throw new IndexOutOfBoundsException();
        if (toIndex < 0 || toIndex >= usedLength)
            throw new IndexOutOfBoundsException();
        return new ListA<E>((E)Arrays.copyOfRange(arr, fromIndex, toIndex));
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
        return arr;
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