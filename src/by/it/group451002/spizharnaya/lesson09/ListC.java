package by.it.group451002.spizharnaya.lesson09;

import java.util.*;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    private static final int defaultSize = 10;
    private Object[] list;
    private int size;

    public ListC(int capacity){
        list = new Object[capacity];
    }

    public ListC(){
        list = new Object[defaultSize];
    }

    private Object[] Copy(Object[] arr, int  startInd, int endInd){
        if (startInd<0 || endInd>=arr.length)
            return null;
        Object[] res = new Object[endInd-startInd+1];
        for (int i=startInd; i<=endInd; i++){
            res[i-startInd] = arr[i];
        }
        return res;
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

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c){
            if (!contains(o))
                return false;
        }
        return true;
    }



    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) { return false; }
        for (E o : c){
            add(o);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (c.isEmpty()) { return false; }
        Object[] newList = Copy(list,index,size-1);
        size -= newList.length;
        for (E o : c){
            add(o);
        }
        for (Object o : newList){
            add((E)o);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        int i=0;
        while (i<size){
            if (c.contains(list[i])) {
                remove(i);
                i--;
                modified = true;
            }
            i++;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        int i=0;
        while (i<size){
            if (!c.contains(list[i])) {
                remove(i);
                i--;
                modified = true;
            }
            i++;
        }
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex<0 || toIndex>=size)
            return null;
        List<E> res = new ListC(toIndex-fromIndex);
        for (int i=fromIndex; i<toIndex; i++){
            res.add((E)list[i]);
        }
        return res;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListIterator<E>(){
            private int currInd = index;
            private int lastRet = -1;

            @Override
            public boolean hasNext(){
                return currInd<size;
            }

            @Override
            public E next(){
                if (hasNext()) {
                    lastRet = currInd;
                    return (E) list[currInd++];
                }else
                    throw new NoSuchElementException();
            }

            @Override
            public boolean hasPrevious(){
                return currInd != 0;
            }

            @Override
            public E previous(){
                if (hasPrevious()) {
                    lastRet = --currInd;
                    return (E) list[currInd];
                }else
                    throw new NoSuchElementException();
            }

            @Override
            public void remove(){
                if (lastRet == -1) {
                    throw new IllegalStateException();
                }else{
                    ListC.this.remove(lastRet);
                    currInd = lastRet;
                    lastRet = -1;
                }
            }

            @Override
            public int nextIndex(){
                if (hasNext())
                    return currInd;
                else
                    return size;
            }

            @Override
            public int previousIndex(){
                if (hasPrevious())
                    return currInd-1;
                else
                    return -1;
            }

            @Override
            public void set (E obj){
                if (lastRet == -1) {
                    throw new IllegalStateException();
                }
                ListC.this.set(lastRet, obj);
            }

            @Override
            public void add (E obj){
                ListC.this.add(currInd++, obj);
            }
        };
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }



    @Override
    public <T> T[] toArray(T[] a) {
        return (T[])Copy(list,0, size-1);
    }

    @Override
    public Object[] toArray() {
        return Copy(list,0, size-1);
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        // Only next(), hasNext(), remove() from ListIterator
        return null;
    }

}
