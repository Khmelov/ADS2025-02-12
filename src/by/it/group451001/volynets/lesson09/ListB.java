package by.it.group451001.volynets.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {

    // внутреннее хранилище
    private E[] elements = (E[]) new Object[10];
    private int size = 0;

    private void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            int newCap = elements.length * 3 / 2 + 1;
            if (newCap < capacity) newCap = capacity;
            E[] newArr = (E[]) new Object[newCap];
            System.arraycopy(elements, 0, newArr, 0, size);
            elements = newArr;
        }
    }

//Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы              ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append('[');
        for (int i=0;i<size;i++){
            sb.append(elements[i]);
            if (i<size-1) sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        ensureCapacity(size+1);
        elements[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index<0 || index>=size) throw new IndexOutOfBoundsException();
        E old = elements[index];
        int numMoved = size-index-1;
        if (numMoved>0){
            System.arraycopy(elements, index+1, elements, index, numMoved);
        }
        elements[--size]=null;
        return old;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (index<0 || index>size) throw new IndexOutOfBoundsException();
        ensureCapacity(size+1);
        int numMoved = size-index;
        if (numMoved>0){
            System.arraycopy(elements, index, elements, index+1, numMoved);
        }
        elements[index]=element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexOf(o);
        if (idx==-1) return false;
        remove(idx);
        return true;
    }

    @Override
    public E set(int index, E element) {
        if (index<0 || index>=size) throw new IndexOutOfBoundsException();
        E old = elements[index];
        elements[index]=element;
        return old;
    }


    @Override
    public boolean isEmpty() {
        return size==0;
    }


    @Override
    public void clear() {
        for (int i=0;i<size;i++) elements[i]=null;
        size=0;
    }

    @Override
    public int indexOf(Object o) {
        if (o==null){
            for (int i=0;i<size;i++){
                if (elements[i]==null) return i;
            }
        } else {
            for (int i=0;i<size;i++){
                if (o.equals(elements[i])) return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if (index<0 || index>=size) throw new IndexOutOfBoundsException();
        return elements[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o)>=0;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o==null){
            for (int i=size-1;i>=0;i--){
                if (elements[i]==null) return i;
            }
        } else {
            for (int i=size-1;i>=0;i--){
                if (o.equals(elements[i])) return i;
            }
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