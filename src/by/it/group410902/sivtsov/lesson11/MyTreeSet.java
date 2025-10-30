package by.it.group410902.sivtsov.lesson11;

import java.util.Collection;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {
    private Object[] a;
    private int size;
    public MyTreeSet() {
        a = new Object[10];
        size = 0;
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(a[i]);
        }
        sb.append(']');
        return sb.toString();
    }
    @Override
    public int size(){return this.size;}
    @Override
    public void clear(){
        for (int i = 0; i < size; i++) a[i] = null;
        size = 0;
    }
    @Override
    public boolean isEmpty(){return this.size == 0;}
    @Override
    public boolean add(E e){
        if(e == null) throw new NullPointerException();
        int index = findIndex(e);
        if(index >= 0) return false;
        int insertion = -index - 1;
        ensureCapacity(size + 1);
        for(int i = size; i > insertion; i--) a[i] = a[i - 1];
        a[insertion] = e;
        size++;
        return true;
    }
    @Override
    public boolean remove(Object o){
        if(o == null) return false;
        int index = findIndex(o);
        if(index < 0) return false;
        for(int i = index; i < size - 1; i++) a[i] = a[i+1];
        a[size - 1] = null;
        size--;
        return true;
    }
    @Override
    public boolean contains(Object o){
        if(o == null) return false;
        return findIndex(o) >= 0;
    }

    @Override
    public boolean containsAll(Collection<?> c){
        for(Object element: c){
            if(!contains(element)) return false;
        }
        return true;
    }
    @Override
    public boolean addAll(Collection<? extends E> c){
        boolean modified = false;
        for(E element: c){
            if(add(element)) modified = true;
        }
        return modified;
    }
    @Override
    public boolean removeAll(Collection<?> c){
        boolean modified = false;
        for(Object element: c){
            if(remove(element)) modified = true;
        }
        return modified;
    }
    @Override
    public boolean retainAll(Collection<?> c){
        boolean modified = false;
        int i = 0;
        while(i < size){
            if(!c.contains(a[i])){
                remove(a[i]);
                modified = true;
            }
            else i++;
        }
        return modified;
    }



    private int findIndex(Object o) {
        int lo = 0, hi = size - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int cmp = compare(a[mid], o);
            if (cmp == 0) return mid;
            if (cmp < 0) lo = mid + 1; else hi = mid - 1;
        }
        return -(lo + 1);
    }
    private void ensureCapacity(int minCapacity) {
        if (a.length >= minCapacity) return;
        int newCap = a.length * 2;
        if (newCap < minCapacity) newCap = minCapacity;
        Object[] na = new Object[newCap];
        System.arraycopy(a, 0, na, 0, size);
        a = na;
    }
    @SuppressWarnings("unchecked")
    private int compare(Object x, Object y) {
        return ((Comparable<Object>) x).compareTo(y);
    }


    public java.util.Iterator<E> iterator() {throw new UnsupportedOperationException();}
    public Object[] toArray() {throw new UnsupportedOperationException();}
    public <T> T[] toArray(T[] ts) {throw new UnsupportedOperationException();}
    public boolean equals(Object o) {throw new UnsupportedOperationException();}
    public int hashCode() {throw new UnsupportedOperationException();}
}
