package by.it.group410902.sivtsov.lesson11;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private int len;
    private LinkedList<E>[] arr;
    final private int startLen = 32;
    private LinkedList<E>[] q;

    @SuppressWarnings("all")
    public MyLinkedHashSet(){
        len = 0;
        arr = new LinkedList[startLen];
        q = new LinkedList[1];
        q[0] = new LinkedList<E>();
    }

    @SuppressWarnings("all")
    public MyLinkedHashSet(int startLen){
        len = 0;
        arr = new LinkedList[startLen];
        q = new LinkedList[1];
        q[0] = new LinkedList<E>();
    }
    @Override
    public String toString(){
        if (len == 0)
            return "[]";
        StringBuilder s = new StringBuilder("[");
        for (var i : q[0]){
            s.append(i);
            s.append(", ");
        }
        s.deleteCharAt(s.length() - 1);
        s.deleteCharAt(s.length() - 1);
        return s.append("]").toString();
    }
    @Override
    public int size(){return this.len;}
    @Override
    public void clear(){
        for (int i = 0; i < arr.length; i++) {
            arr[i] = null;
        }
        q[0].clear();
        len = 0;
    }
    @Override
    public boolean isEmpty(){return this.len == 0;}
    @Override
    public boolean add(E o){
        if (contains(o))
            return false;
        q[0].add(o);
        len++;
        var hash = o.hashCode() % arr.length;
        if (arr[hash] == null)
            arr[hash] = new LinkedList<E>();
        return arr[hash].add(o);
    }
    @Override
    public boolean remove(Object o) {
        var hash = o.hashCode() % arr.length;
        if (arr[hash] == null)
            return false;
        if (arr[hash].remove(o)){
            len--;
            q[0].remove(o);
            return true;
        }
        return false;
    }
    @Override
    public boolean contains(Object o) {
        var hash = o.hashCode() % arr.length;
        return arr[hash] != null && arr[hash].contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c){
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }
    @Override
    public boolean addAll(Collection<? extends E> c){
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }
    @Override
    public boolean removeAll(Collection<?> c){
        boolean modified = false;
        for (Object element : c) {
            if (remove(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c){
        boolean modified = false;
        // Создаем копию для безопасной итерации
        LinkedList<E> copy = new LinkedList<>(q[0]);
        for (E element : copy) {
            if (!c.contains(element)) {
                remove(element);
                modified = true;
            }
        }
        return modified;
    }


    @Override public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean equals(Object o) { return super.equals(o); }
}

