package by.it.group451001.alexandrovich.lesson09;

import java.util.*;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    int size = 0;
    Object[] Arr;
    public ListC(){
        Arr = new Object[10];
    }

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            sb.append(Arr[i].toString());
            if (i != size-1) sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        if (size == Arr.length){
            Arr = Arrays.copyOf(Arr,Arr.length*2);
        }
        Arr[size] = e;
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        Object el = Arr[index];
        size--;
        for (int i = index; i < size; i++) {
            Arr[i] = Arr[i + 1];
        }
        return (E) el;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (size == Arr.length) {
            Arr = Arrays.copyOf(Arr, Arr.length * 2);
        }
        for (int i = size; i > index; i--) {
            Arr[i]=Arr[i-1];
        }
        size++;
        Arr[index] = element;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(Arr[i],o)) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        Object prev = Arr[index];
        Arr[index] = element;
        return (E)prev;
    }


    @Override
    public boolean isEmpty() {
        return size==0;
    }


    @Override
    public void clear() {
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(Arr[i],o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        return (E) Arr[index];
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(Arr[i],o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size-1; i >= 0; i--) {
            if (Objects.equals(Arr[i],o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            add(e);
        }
        return !c.isEmpty();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        for (E e : c) {
            add(index,e);
            index++;
        }
        return !c.isEmpty();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int i = 0;
        int j = 0;
        boolean res = false;
        while (i < size) {
            if (!c.contains(Arr[i])) {
                Arr[j] = Arr[i];
                j++;
            }
            else res = true;
            i++;
        }
        size-=i-j;
        return res;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int i = 0;
        int j = 0;
        while (i < size) {
            if (c.contains(Arr[i])) {
                Arr[j] = Arr[i];
                j++;
            }
            i++;
        }
        size-=i-j;
        return true;
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
