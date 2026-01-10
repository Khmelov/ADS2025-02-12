package by.it.group451002.ivash.lesson06.ivash.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@SuppressWarnings("unchecked")
public class ListC<E> implements List<E> {

    private E[] data = (E[]) new Object[]{};
    private int size = 0;

    private void ensureCapacity(int capacity) {
        if (capacity <= data.length) return;
        int newCapacity = Math.max(capacity, data.length * 2 + 1);
        E[] newArr = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newArr[i] = data[i];
        }
        data = newArr;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i + 1 < size) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1);
        data[size++] = e;
        return true;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException();
        ensureCapacity(size + 1);
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
        }
        data[index] = element;
        size++;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        E old = data[index];
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        size--;
        return old;
    }

    @Override
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i >= 0) {
            remove(i);
            return true;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        E old = data[index];
        data[index] = element;
        return old;
    }

    @Override
    public void clear() {
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
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && data[i] == null) || (o != null && o.equals(data[i])))
                return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if ((o == null && data[i] == null) || (o != null && o.equals(data[i])))
                return i;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        return data[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    ////////////////////////////////////////////////
    //  addAll, removeAll, retainAll — O(n)
    ////////////////////////////////////////////////

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            add(e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException();
        boolean changed = false;
        for (E e : c) {
            add(index++, e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        int i = 0;
        while (i < size) {
            if (c.contains(data[i])) {
                remove(i);
                changed = true;
            } else i++;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        int i = 0;
        while (i < size) {
            if (!c.contains(data[i])) {
                remove(i);
                changed = true;
            } else i++;
        }
        return changed;
    }

    ////////////////////////////////////////////////
    //  Optional methods
    ////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int pos = 0;

            @Override
            public boolean hasNext() {
                return pos < size;
            }

            @Override
            public E next() {
                return data[pos++];
            }
        };
    }

    @Override public Object[] toArray() { return data; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public List<E> subList(int fromIndex, int toIndex) { return null; }
    @Override public ListIterator<E> listIterator(int index) { return null; }
    @Override public ListIterator<E> listIterator() { return null; }

}
