package by.it.group451001.strogonov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class MyLinkedHashSet <E> implements Set<E>{
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

    @SuppressWarnings("all")
    private void extend_vir_len(){
        var tmp = new MyLinkedHashSet<E>(arr.length << 1);
        for (var i = 0; i < arr.length; i++){
            if (arr[i] != null)
                for (var j : arr[i])
                    tmp.add(j);
        }
        arr = tmp.arr;
    }

    @Override
    public boolean add(E e) {
        if (contains(e))
            return false;
        if (len > arr.length)
            extend_vir_len();
        q[0].add(e);
        len++;
        var hash = e.hashCode() % arr.length;
        if (arr[hash] == null)
            arr[hash] = new LinkedList<E>();
        return arr[hash].add(e);
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
    public int size() {
        return len;
    }

    @Override
    public boolean isEmpty() {
        return len == 0;
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


    @Override
    public boolean containsAll(Collection<?> c) {
        for (var i : c)
            if (!contains(i))
                return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        var tmp = len;
        for (var i : c)
            add(i);
        return tmp != len;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        var tmp = new MyLinkedHashSet<E>(arr.length);
        for (var i : arr)
            if (i != null)
                for (var j : i){
                    if (c.contains(j))
                        tmp.add(j);
                    else
                        q[0].remove(j);
                }
        if (tmp.size() == len)
            return false;
        arr = tmp.arr;
        len = tmp.len;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        var tmp = len;
        for (var i : c)
            remove(i);
        return tmp != len;
    }

    @Override
    public void clear() {
        len = 0;
        arr = new LinkedList[startLen];
        q = new LinkedList[1];
        q[0] = new LinkedList<E>();
    }
}
