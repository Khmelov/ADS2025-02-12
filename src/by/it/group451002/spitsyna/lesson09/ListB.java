package by.it.group451002.spitsyna.lesson09;

import java.util.*;

public class ListB<E> implements List<E> {
    private E[] list = (E[]) new Object[0];
    private int size = 0;

    private void checkCapacity(){
        if (size+1 > list.length){
            E[] newList = (E[]) new Object[(int)(list.length*1.5) + 1];

            for (int i = 0; i < size; i++){
                newList[i] = list[i];
            }
            list = newList;
        }
    }

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("[");
        Iterator<E> iter = iterator();

        while (iter.hasNext()){
            str.append(iter.next());
            if (iter.hasNext()) {
                str.append(", ");
            }
        }
        str.append("]");

        return str.toString();
    }

    @Override
    public boolean add(E e) {
        checkCapacity();
        list[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }

        E elemToDel = list[index];

        for (int i = index; i < size-1; i++){
            list[i] = list[i+1];
        }

        list[--size] = null;
        return elemToDel;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        checkCapacity();
        for (int i = size ; i > index; i--){
            list[i]  = list[i-1];
        }
        list[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        if (indexOf(o) != -1){
            remove(indexOf(o));
            return true;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }
        E oldElem = list[index];
        list[index] = element;

        return oldElem;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        for (int i = 0; i < size; i++){
            list[i] = null;
        }
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        int i = 0;

        while (i < size){
            if (Objects.equals(o,list[i])){
                return i;
            }
            i++;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return list[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size-1; i >= 0; i--)
            if (Objects.equals(o,list[i]))
                return i;

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
        return new Iterator<E>() {
            private int index = 0;
            @Override
            public boolean hasNext() {
                return (index < size);
            }

            @Override
            public E next() {
                return list[index++];
            }
        };
    }

}
