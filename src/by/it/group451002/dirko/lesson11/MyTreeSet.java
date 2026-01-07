package by.it.group451002.dirko.lesson11;

import java.util.*;

public class MyTreeSet<E> implements Set<E> {
    private Object[] elements = new Object[10000];
    private int size;

    public String toString() {
        Arrays.sort(elements, 0, size);

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++)
            sb.append(elements[i]).append(", ");
        if (sb.length() > 1) sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++)
            if (Objects.equals(elements[i], o)) return true;
        return false;
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) return false;
        elements[size++] = e;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (!contains(o)) return false;
        int ind = -1;
        for (int i = 0; i < size; i++)
            if (Objects.equals(elements[i], o)) { ind = i; break; }
        System.arraycopy(elements, ind + 1, elements, ind, size - ind - 1);
        elements[--size] = null;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o: c)
            if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        int oldSize = size;
        for (E o: c) add(o);
        return oldSize != size;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int oldSize = size;
        Object[] newElems = new Object[10000];
        int count = 0;
        for (int i = 0; i < size; i++)
            if (c.contains(elements[i]))
                newElems[count++] = elements[i];
        elements = newElems;
        size = count;
        return oldSize != size;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int oldSize = size;
        for (Object o: c) remove(o);
        return oldSize != size;
    }

    @Override
    public void clear() {
        elements = new Object[10000];
        size = 0;
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
