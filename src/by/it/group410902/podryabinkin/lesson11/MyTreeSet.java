package by.it.group410902.podryabinkin.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E extends Comparable<E>> implements Set<E> {
    private E[] table;
    private int size = 0;


    public MyTreeSet(){
        table = (E[]) new Comparable[1];
    }

    private void resize() {
        if (size >= table.length) {
            E[] newElements = (E[])new Comparable[table.length * 2];
            for (int i = 0; i < table.length; i++)
                newElements[i] = table[i];
            table = (E[]) newElements;
        }
    }

    // Метод вставки с сортировкой
    private void insertSorted(E e) {
        int i = 0;

        while (i < size && ((E) table[i]).compareTo(e) < 0) {
            i++;
        }
        for (int j = size; j > i; j--) {
            table[j] = table[j - 1];
        }

        table[i] = e;
        size++;
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) return false;

        resize();
        insertSorted(e);
        return true;
    }

    @Override
    public boolean contains(Object o) {
        E e = (E) o;
        for (int i = 0; i < size; i++) {
            if (((E) table[i]).compareTo(e) == 0) return true;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        E e = (E) o;
        for (int i = 0; i < size; i++) {
            if (((E) table[i]).compareTo(e) == 0) {
                for (int j = i; j < size - 1; j++) {
                    table[j] = table[j + 1];
                }
                table[size - 1] = null;
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) table[i] = null;
        size = 0;
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
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(table[i]);
            if (i != size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }


    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            if (add(e)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            if (remove(o)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < size; ) {
            if (!c.contains(table[i])) {
                remove(table[i]);
                changed = true;
            } else {
                i++;
            }
        }
        return changed;
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
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
