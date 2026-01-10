package by.it.group451004.maximenko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyTreeSet<E extends Comparable<E>> implements Set<E> {

    private E[] data;
    private int size;

    private static final int DEFAULT_CAPACITY = 10;

    public MyTreeSet() {
        data = (E[]) new Comparable[DEFAULT_CAPACITY];
        size = 0;
    }

    public MyTreeSet(int capacity) {
        if (capacity <= 0) capacity = DEFAULT_CAPACITY;
        data = (E[]) new Comparable[capacity];
        size = 0;
    }

    private void ensureCapacity() {
        if (size < data.length) return;

        E[] newArr = (E[]) new Comparable[data.length * 2];
        for (int i = 0; i < size; i++)
            newArr[i] = data[i];
        data = newArr;
    }

    private int binarySearch(Object o) {
        E key = (E) o;
        int left = 0, right = size - 1;

        while (left <= right) {
            int mid = (left + right) >>> 1;
            int cmp = key.compareTo(data[mid]);

            if (cmp == 0) return mid;
            if (cmp < 0) right = mid - 1;
            else left = mid + 1;
        }

        return -(left + 1);
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
        if (o == null) return false;
        return binarySearch(o) >= 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException("MyTreeSet does not allow null");

        int pos = binarySearch(e);

        if (pos >= 0) return false;

        int insertIndex = -(pos + 1);

        ensureCapacity();

        for (int i = size; i > insertIndex; i--)
            data[i] = data[i - 1];

        data[insertIndex] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;

        int pos = binarySearch(o);
        if (pos < 0) return false;

        for (int i = pos; i < size - 1; i++)
            data[i] = data[i + 1];

        data[size - 1] = null;
        size--;
        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++)
            data[i] = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');

        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(String.valueOf(data[i]));
        }

        sb.append(']');
        return sb.toString();
    }


    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object o : c)
            if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;
        for (E e : c)
            if (add(e)) changed = true;
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;
        for (Object o : c)
            if (remove(o)) changed = true;
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();

        boolean changed = false;

        int i = 0;
        while (i < size) {
            if (!c.contains(data[i])) {
                remove(data[i]);
                changed = true;
            } else {
                i++;
            }
        }
        return changed;
    }


    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int cursor = 0;
            boolean canRemove = false;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public E next() {
                if (cursor >= size) throw new NoSuchElementException();
                canRemove = true;
                return data[cursor++];
            }

            @Override
            public void remove() {
                if (!canRemove) throw new IllegalStateException();
                MyTreeSet.this.remove(data[cursor - 1]);
                cursor--;
                canRemove = false;
            }
        };
    }


    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++)
            arr[i] = data[i];
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            a = (T[]) java.lang.reflect.Array
                    .newInstance(a.getClass().getComponentType(), size);

        for (int i = 0; i < size; i++)
            a[i] = (T) data[i];

        if (a.length > size)
            a[size] = null;

        return a;
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (int i = 0; i < size; i++)
            h += data[i].hashCode();
        return h;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Set)) return false;

        Set<?> other = (Set<?>) o;
        if (other.size() != this.size) return false;

        return containsAll(other);
    }
}

