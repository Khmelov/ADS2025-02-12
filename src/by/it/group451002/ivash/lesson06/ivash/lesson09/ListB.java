package by.it.group451002.ivash.lesson06.ivash.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@SuppressWarnings("unchecked")
public class ListB<E> implements List<E> {

    private E[] elements = (E[]) new Object[10];
    private int size = 0;

    private void ensureCapacity() {
        if (size >= elements.length) {
            E[] newArr = (E[]) new Object[elements.length * 2];
            for (int i = 0; i < size; i++)
                newArr[i] = elements[i];
            elements = newArr;
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ
    //////////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        ensureCapacity();
        elements[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        E removed = elements[index];
        for (int i = index + 1; i < size; i++)
            elements[i - 1] = elements[i];
        elements[--size] = null;
        return removed;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        ensureCapacity();
        for (int i = size; i > index; i--)
            elements[i] = elements[i - 1];
        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexOf(o);
        if (idx == -1) return false;
        remove(idx);
        return true;
    }

    @Override
    public E set(int index, E element) {
        E old = elements[index];
        elements[index] = element;
        return old;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) elements[i] = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++)
                if (elements[i] == null) return i;
        } else {
            for (int i = 0; i < size; i++)
                if (o.equals(elements[i])) return i;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        return elements[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--)
                if (elements[i] == null) return i;
        } else {
            for (int i = size - 1; i >= 0; i--)
                if (o.equals(elements[i])) return i;
        }
        return -1;
    }

    //////////////////////////////////////////////////////////////////////////////
    // ОПЦИОНАЛЬНЫЕ МЕТОДЫ (минимальные рабочие версии)
    //////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object x : c)
            if (!contains(x)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E x : c) add(x);
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        for (E x : c) {
            add(index++, x);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object x : c)
            while (remove(x)) changed = true;
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(i);
                i--;
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        ListB<E> sub = new ListB<>();
        for (int i = fromIndex; i < toIndex; i++)
            sub.add(elements[i]);
        return sub;
    }

    @Override
    public ListIterator<E> listIterator(int index) { return null; }

    @Override
    public ListIterator<E> listIterator() { return null; }

    @Override
    public <T> T[] toArray(T[] a) { return null; }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) arr[i] = elements[i];
        return arr;
    }

    @Override
    public Iterator<E> iterator() { return null; }
}
