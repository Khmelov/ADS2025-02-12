package by.it.group451002.spizharnaya.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {


    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    private static final int defaultSize = 10;
    private Object[] list;
    private int size;

    public ListB(int capacity){
        list = new Object[capacity];
    }

    public ListB(){
        list = new Object[defaultSize];
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("[");
        if (size>0)
            res.append(list[0]);
        for (int i=1; i<size; i++){
            res.append(", ");
            res.append(list[i]);
        }
        res.append("]");
        return res.toString();
    }

    @Override
    public boolean add(E e) {
        if (size == list.length){
            Object[] newList = new Object[size*2];
            for (int i = 0; i<size; i++){
                newList[i] = list[i];
            }
            list = newList;
        }
        list[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index>=0 && index<size){
            Object delObj = list[index];
            for (int i=index; i<size-1; i++)
                list[i] = list[i+1];
            size--;
            return (E)delObj;
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (size == list.length){
            Object[] newList = new Object[size*2];
            for (int i = 0; i<index; i++){
                newList[i] = list[i];
            }
            newList[index] = element;
            for (int i = index; i<size; i++){
                newList[i+1] = list[i];
            }
            list = newList;
        }
        else{
            for (int i = size; i>index; i--){
                list[i] = list[i-1];
            }
            list[index] = element;
        }
        size++;
    }

    @Override
    public boolean remove(Object o) {
        boolean found = false;
        int i=0;
        while (!found && i<size) {
            if (list[i].equals(o))
                found = true;
            i++;
        }
        if (found)
            remove(i-1);
        return found;
    }

    @Override
    public E set(int index, E element) {
        if (index < size) {
            Object prev = list[index];
            list[index] = element;
            return (E)prev;
        }
        return null;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i=0; i<size; i++){
            if (list[i].equals(o))
                return i;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        return (E)list[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i=size-1; i>=0; i--){
            if (list[i].equals(o))
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

