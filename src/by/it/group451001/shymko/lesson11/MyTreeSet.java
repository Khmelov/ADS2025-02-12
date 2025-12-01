package by.it.group451001.shymko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    Object[] elements;
    final int initialCapacity = 10;
    int size;
    int capacity;

    public MyTreeSet() {
        elements = new Object[initialCapacity];
        capacity = initialCapacity;
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public String toString(){
        String s = "[";
        if(isEmpty()){
            return "[]";
        }
        s += elements[0].toString();
        for (int i = 1; i < size; i++) {
            s += ", " + elements[i].toString();
        }
        return s + "]";
    }

    public boolean contains(Object o) {
        int l = 0, r = size;
        E k = (E) o;
        Comparable<? super E> key = (Comparable<? super E>) k;
        while (r - l > 1) {
            int m = (l + r) / 2;
            if (elements[m].equals(o)) {
                return true;
            }
            if(key.compareTo((E)elements[m]) > 0){
                l = m;
            }
            else{
                r = m;
            }
        }
        return elements[l].equals(o);
    }

    public Iterator<E> iterator() {
        return null;
    }

    public Object[] toArray() {
        return new Object[0];
    }

    public <T> T[] toArray(T[] a) {
        return null;
    }

    public boolean add(E e) {
        int l = 0, r = size;
        E k = (E) e;
        Comparable<? super E> key = (Comparable<? super E>) k;
        while (r - l > 1) {
            int m = (l + r) / 2;
            if (elements[m].equals(e)) {
                return false;
            }
            if(key.compareTo((E)elements[m]) > 0){
                l = m;
            }
            else{
                r = m;
            }
        }
        if(size == capacity){
            int newCapacity = capacity * 2;
            Object[] newElements = new Object[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
            capacity = newCapacity;
        }
        if(l == size){
            elements[size] = e;
            size++;
            return true;
        }
        if(key.compareTo((E)elements[l]) > 0){
            l++;
        }
        if(l == size){
            elements[size] = e;
            size++;
            return true;
        }
        if(elements[l].equals(e)){
            return false;
        }
        System.arraycopy(elements, l, elements, l + 1, size - l);
        elements[l] = e;
        size++;
        return true;
    }

    public boolean remove(Object o) {
        int l = 0, r = size;
        E k = (E) o;
        Comparable<? super E> key = (Comparable<? super E>) k;
        while (r - l > 1) {
            int m = (l + r) / 2;
            if (elements[m].equals(o)) {
                elements[m] = null;
                size--;
                System.arraycopy(elements, m + 1, elements, m, size - m);
                return true;
            }
            if(key.compareTo((E)elements[m]) > 0){
                l = m;
            }
            else{
                r = m;
            }
        }
        if(l == size){
            return false;
        }
        if(key.compareTo((E)elements[l]) > 0){
            l++;
        }
        if(l == size){
            return false;
        }
        if(elements[l].equals(o)){
            elements[l] = null;
            size--;
            System.arraycopy(elements, l + 1, elements, l, size - l);
            return true;
        }
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            changed |= add(e);
        }
        return changed;
    }

    public boolean retainAll(Collection<?> c) {
        int w = 0;
        boolean changed = false;
        for(int i = 0; i < size; i++){
            if(c.contains(elements[i])){
                elements[w++] = elements[i];
            }
        }
        changed = changed || w < size;
        size = w;
        return changed;
    }

    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            changed |= remove(o);
        }
        return changed;
    }

    public void clear() {
        elements = new Object[initialCapacity];
        size = 0;
        capacity = initialCapacity;
    }
}
