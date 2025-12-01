package by.it.group451001.klevko.lesson11;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/*
*               toString()
                size()
                clear()
                isEmpty()
                add(Object)
                remove(Object)
                contains(Object)

                containsAll(Collection)
                addAll(Collection)
                removeAll(Collection)
                retainAll(Collection)
* */

public class MyTreeSet<E extends Comparable<E>> implements Set<E> {
    private E[] data = (E[]) new Comparable[16]; //от меньшего к большему
    private int size = 0;

    private void Grow(){
        E[] lastData = data;
        data = (E[]) new Comparable[data.length*2];
        System.arraycopy(lastData, 0, data, 0, lastData.length);
    }

    private int binFind(E e){
        int left = 0, right = size-1;
        while (left <= right) {
            int midle = (left + right) / 2;
            int cmp = data[midle].compareTo(e);
            if (cmp == 0) return midle;
            else if (cmp > 0) right = midle-1;
            else left = midle+1;
        }
        return -1;
    }

    //без проверок индекс действителен
    private void delByIndex(int pos) {
        for (int i = pos; i < size-1; i++) {
            data[i] = data[i+1];
        }
        size--;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder ans = new StringBuilder("[");
        for (int i = 0; i < size; i++){
            ans.append(data[i]).append(", ");
        }
        ans.setLength(ans.length() - 2);
        ans.append("]");
        return ans.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return binFind((E) o) != -1;
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
        if (contains(e)) return false;
        if (size+1 >= data.length) Grow();
        int left = 0, right = size-1;
        while (left <= right) {
            int midle = (left + right) / 2;
            if (data[midle].compareTo(e) > 0) right = midle-1;
            else left = midle+1;
        }
        int bestPos = left;
        for (int i = size; i > bestPos ; i--) {
            data[i] = data[i-1];
        }
        data[bestPos] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int pos = binFind((E) o);
        if (pos == -1) return false;
        delByIndex(pos);
        return true;
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
        boolean changed = false;
        for (E e : c) {
            changed |= add(e);
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < size; i++){
            if (!c.contains(data[i])) {
                delByIndex(i);
                i--;
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < size; i++){
            if (c.contains(data[i])) {
                delByIndex(i);
                i--;
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public void clear() {
        size = 0;
        Arrays.fill(data, null);
    }
}
