package by.it.group451002.ivash.lesson06.ivash.lesson11;

import java.util.Collection;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyTreeSet<E extends Comparable<E>> implements Set<E> {

    private E[] data;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public MyTreeSet() {
        data = (E[]) new Comparable[DEFAULT_CAPACITY];
    }

    private void ensureCapacity() {
        if (size < data.length) return;

        E[] newArr = (E[]) new Comparable[data.length * 2];
        for (int i = 0; i < size; i++) newArr[i] = data[i];
        data = newArr;
    }

    private int binarySearch(Object o) {
        if (size == 0) return -1;
        E key = (E) o;

        int left = 0, right = size - 1;

        while (left <= right) {
            int mid = (left + right) >>> 1;
            int cmp = key.compareTo(data[mid]);

            if (cmp == 0) return mid;
            if (cmp < 0) right = mid - 1;
            else left = mid + 1;
        }
        return -(left + 1); // место вставки
    }

    @Override
    public boolean add(E e) {
        int pos = binarySearch(e);

        if (pos >= 0) return false; // уже есть

        int insertPos = -(pos + 1);
        ensureCapacity();

        for (int i = size; i > insertPos; i--)
            data[i] = data[i - 1];

        data[insertPos] = e;
        size++;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return binarySearch(o) >= 0;
    }

    @Override
    public boolean remove(Object o) {
        int pos = binarySearch(o);
        if (pos < 0) return false;

        for (int i = pos; i < size - 1; i++)
            data[i] = data[i + 1];

        size--;
        return true;
    }

    @Override
    public void clear() {
        size = 0;
    }

    @Override public int size() { return size; }
    @Override public boolean isEmpty() { return size == 0; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');

        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(data[i]);
        }

        sb.append(']');
        return sb.toString();
    }

    // -----------------------------
    // Методы уровня C
    // -----------------------------

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c)
            if (add(e)) changed = true;
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c)
            if (remove(o)) changed = true;
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
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

    // Iterator и toArray не требуются по заданию
    @Override public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}
