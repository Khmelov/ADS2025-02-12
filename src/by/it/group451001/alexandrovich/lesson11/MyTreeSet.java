package by.it.group451001.alexandrovich.lesson11;

import java.io.ObjectStreamException;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

public class MyTreeSet<E> implements Set<E> {

    Comparable arr[] = new Comparable[10];
    int capacity = 10;
    int size = 0;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size-1; i++){
            sb.append(arr[i].toString() + ", ");
        }
        if (size > 1) sb.append(arr[size-1].toString());
        sb.append("]");
        return sb.toString();
    }

    int binarySearch(Comparable el)
    {
        int l = 0, r = size-1, m = (l + r)/2;
        while (l <= r)
        {
            m = (l + r)/2;
            if (el.compareTo(arr[m]) == 0) return m;
            else if (el.compareTo(arr[m]) > 0) l = m + 1;
            else r = m - 1;
        }
        return m;
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return arr[binarySearch((Comparable)o)].equals(o);
    }

    @Override
    public boolean add(E e) {
        if (size == 0) arr[0] = (Comparable)e;
        else {
            int i = binarySearch((Comparable) e);
            if (arr[i].compareTo((Comparable)e) < 0) i++;
            if (i == size) arr[i] = (Comparable)e;
            else {
                if (arr[i].equals(e)) return false;
                if (size == capacity) {
                    capacity *= 2;
                    Comparable[] temp = new Comparable[capacity];
                    System.arraycopy(arr, 0, temp, 0, size);
                    arr = temp;
                }
                System.arraycopy(arr, i, arr, i + 1, size - i);
                arr[i] = (Comparable) e;
            }
        }
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (size == 0) return false;
        int i = binarySearch((Comparable)o);
        if (!arr[i].equals(o)) return false;
        System.arraycopy(arr, i+1, arr, i, size - i-1);
        size--;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c){
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean ch = false;
        for (E e : c){
            if (add(e)) ch = true;
        }
        return ch;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean ch = false;
        for (int i = 0; i < size;){
            if (!c.contains(arr[i]))
            {
                remove(arr[i]);
                ch = true;
            }
            else i++;
        }
        return ch;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean ch = false;
        for (Object o : c){
            if (remove(o)) ch = true;
        }
        return ch;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }
}
