package by.it.group410902.bobrovskaya.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    private E[] elements;
    private int size;
    public ListC(){
        elements =(E[]) new Object[1];
        size=0;
    }
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder("[");
        for (int i=0; i<size; i++) {
            sb.append(elements[i]);
            if (i<size-1) sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        if (size== elements.length) {
            int newsize= elements.length+1;
            E[] newel=(E[]) new Object[newsize];
            System.arraycopy(elements, 0, newel, 0, size);
            elements =newel;
        }
        elements[size]=e;
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        E removed= elements[index];
        for (int i=index; i<size-1; i++){
            elements[i]= elements[i+1];
        }
        elements[size - 1]=null;
        size--;
        return removed;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (size == elements.length) {
            int newsize= elements.length*2;
            E[] newel = (E[]) new Object[newsize];
            System.arraycopy(elements, 0, newel, 0, size);
            elements = newel;
        }
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index]=element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        int index=indexOf(o);
        if (index>=0) {
            remove(index);
            return true;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        E ele = elements[index];
        elements[index] = element;
        return ele;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        size=0;
        E[] newel = (E[]) new Object[1];
        elements = newel;
    }

    @Override
    public int indexOf(Object o) {
        for (int i=0; i<size; i++){
            if (o.equals(elements[i])) return i;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return elements[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o)>=0;
    } // если не найдет, то вернет -1

    @Override
    public int lastIndexOf(Object o) {
        for (int i=size-1; i>=0; i--){
            if (o.equals(elements[i])) return i;
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!(contains(element))) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean f = false;
        for (E e: c){
            add(e);
            f = true;
        }
        return f;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        boolean f = false;
        for (E e : c) {
            add(index++, e);
            f = true;
        }
        return f;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean f = false;
        for (int i = 0; i < size; ) {
            if (c.contains(elements[i])) {
                remove(i);
                f = true;
            } else {
                i++;
            }
        }
        return f;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean f = false;
        for (int i = 0; i < size; ) {
            if (!c.contains(elements[i])) {
                remove(i);
                f = true;
            } else {
                i++;
            }
        }
        return f;
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
