package by.it.group451001.strogonov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E extends Comparable<E>> implements Set<E> {
    private E[] v;
    int len;
    final int startLen = 8;

    @SuppressWarnings("unchecked")
    private void extend_len(){
        var new_v = (E[]) new Comparable[v.length << 1];
        System.arraycopy(v, 0, new_v,0, v.length);
        v = new_v;
    }

    private int binpos(E target, int l, int r){
        if (r - l <= 1)
            return v[l].compareTo(target) >= 0 ? l : r;
        var m = (r + l) >> 1;
        return v[m].compareTo(target) < 0 ? binpos(target, m + 1, r) : binpos(target, l, m);
    }

    private boolean insert(E target, int index){
        if (len + 1 == v.length)
            extend_len();
        System.arraycopy(v, index, v, index + 1, len++ - index);
        v[index] = target;
        return true;
    }

    private boolean delete(int index){
        System.arraycopy(v,index + 1, v, index, len-- - index - 1);
        return true;
    }

    @Override
    public String toString(){
        if (len == 0)
            return "[]";
        StringBuilder s = new StringBuilder("[");
        for (int i = 0; i < len; i++)
            s.append(v[i]).append(", ");
        return s.deleteCharAt(s.length() - 1).deleteCharAt(s.length() - 1).append("]").toString();
    }

    private int lower_bound(E target){
        if (len == 0)
            return 0;
        if (v[len - 1].compareTo(target) < 0)
            return len;
        return binpos(target, 0, len - 1);
    }

    @SuppressWarnings("all")
    public MyTreeSet(){
        v = (E[]) new Comparable[startLen];
        len = 0;
    }

    @Override
    public int size() {
        return len;
    }

    @Override
    public boolean isEmpty() {
        return len == 0;
    }

    @SuppressWarnings("all")
    @Override
    public boolean contains(Object o) {
        var tmp = lower_bound((E) o);
        return tmp != len && v[tmp].equals(o);
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
    public boolean add(E e) {
        var tmp = lower_bound(e);
        if (len == 0 || tmp == len)
            return insert(e, tmp);
        return !v[tmp].equals(e) && insert(e, tmp);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object o) {
        var tmp = lower_bound((E) o);
        return tmp != len && v[tmp].equals(o) && delete(tmp);
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
        var tmp = len;
        for (int i = 0; i < len;)
            if (!c.contains(v[i]))
                delete(i);
            else
                i++;
        return tmp != len;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        var tmp = len;
        for (var i : c)
            remove(i);
        return tmp != len;
    }

    @SuppressWarnings("all")
    @Override
    public void clear() {
        v = (E[]) new Comparable[startLen];
        len = 0;
    }
}
