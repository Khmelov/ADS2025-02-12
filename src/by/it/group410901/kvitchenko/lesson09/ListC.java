package by.it.group410901.kvitchenko.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private static int DEF_CAPACITY = 10;
    private Object[] elems;
    private int size;

    ListC(){
        this.elems = new Object[DEF_CAPACITY];
        this.size = 0;
    }

    ListC(int currentSize){
        if(currentSize<0) throw new IllegalArgumentException("Illegal Capacity: " + currentSize);

        this.elems = new Object[currentSize];
        this.size = currentSize;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        if(size==0) return "[]";

        String result = "[";
        for (int i = 0; i < size; i++) {
            if (i < size - 1) {
                result += elems[i] + ", ";
            } else {
                result += elems[i] + "]";
            }
        }
        return result;
    }

    @Override
    public boolean add(E e) {
        if(size==elems.length) {
            Object[] newList = new Object[elems.length*2];
            System.arraycopy(elems, 0, newList, 0,size);
            elems = newList;
        }
        elems[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {

        E element = (E) elems[index];

        for (int i = index; i < size - 1; i++) {
            elems[i] = elems[i + 1];
        }
        elems[--size] = null;
        return element;

    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if(elems.length==size){
            Object[] newList = new Object[elems.length*2];
            System.arraycopy(elems, 0, newList, 0, size);
            elems = newList;
        }

        size++;

        for(int i = size-1; i>index;i--){
            elems[i]=elems[i-1];
        }
        elems[index] = element;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elems[i] == null) {
                    remove(i);
                    return true;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elems[i])) {
                    remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if(index>=size || index<0) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);

        E last_element = (E) elems[index];

        elems[index] = element;
        return last_element;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        for(int i=0; i<size; i++){
            elems[i] = null;
        }
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        for(int i=0;i<size;i++){
            if (o == null) {
                if (elems[i] == null) return i;
            } else {
                if (o.equals(elems[i])) return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {

        if(index<0||index>=size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        return (E) elems[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        if(isEmpty()) return -1;

        if(o==null){
            for(int i = size-1; i>=0;i--){
                if(elems[i]==null){
                    return i;
                }
            }
        }
        else{
            for (int i = size-1; i>=0; i--){
                if(elems[i].equals(o)) return i;
            }
        }

        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for(Object item : c){
            if(!contains(item)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        }

        for(E item : c){
            add(item);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if(c.isEmpty()) return false;
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);

        for(E item : c){
            add(index++, item);
        }

        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c.isEmpty()) {
            return false;
        }

        boolean flag = false;
        int i = 0;
        while (i < size) {
            if (c.contains(elems[i])) {
                remove(i);
                flag = true;
            } else {
                i++;
            }
        }
        return flag;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c.isEmpty()) {
            if (size > 0) {
                clear();
                return true;
            }
            return false;
        }

        boolean flag = false;
        int i = 0;
        while (i < size) {
            if (!c.contains(elems[i])) {
                remove(i);
                flag = true;
            } else {
                i++;
            }
        }
        return flag;
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
