package by.it.group451002.yeshmanski.lesson11;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

@SuppressWarnings("unchecked")
public class MyTreeSet<E extends Comparable<E>> implements Set<E> {
    private E[] elements;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public MyTreeSet() {
        elements = (E[]) new Comparable[DEFAULT_CAPACITY];
        size = 0;
    }

    private void ensureCapacity() {
        if (size >= elements.length) {
            E[] newArr = (E[]) new Comparable[elements.length * 2];
            for (int i = 0; i < size; i++) {
                newArr[i] = elements[i];
            }
            elements = newArr;
        }
    }

    //возвращает индекс элемента или -1, если не найден
    private int binarySearch(E value) {
        int left = 0, right = size - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = elements[mid].compareTo(value);
            if (cmp == 0) return mid;
            if (cmp < 0) left = mid + 1;
            else right = mid - 1;
        }
        return -1;
    }

    private int findInsertPos(E value) {
        int left = 0, right = size;
        while (left < right) {
            int mid = (left + right) / 2;
            if (elements[mid].compareTo(value) < 0) left = mid + 1;
            else right = mid;
        }
        return left;
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException("Null elements not supported");
        if (contains(e)) return false;

        ensureCapacity();
        int pos = findInsertPos(e);

        // сдвигаем элементы вправо
        for (int i = size; i > pos; i--) {
            elements[i] = elements[i - 1];
        }

        elements[pos] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        E e = (E) o;
        int index = binarySearch(e);
        if (index == -1) return false;

        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        size--;
        elements[size] = null;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        E e = (E) o;
        return binarySearch(e) != -1;
    }

    @Override
    public int size() {
        return size;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clear() {
        elements = (E[]) new Comparable[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

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
    public boolean containsAll(Collection<?> c) {
        for (Object el : c) {
            if (!contains(el)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E el : c) {
            if (add(el)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object el : c) {
            if (remove(el)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < size; ) {
            if (!c.contains(elements[i])) {
                remove(elements[i]);
                changed = true;
            } else {
                i++;
            }
        }
        return changed;
    }

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}