package by.it.group451001.romeyko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

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
            for (int i = 0; i < size; i++) newArr[i] = elements[i];
            elements = newArr;
        }
    }

    // бинарный поиск (возвращает индекс элемента или -1, если не найден)
    private int binarySearch(E e) {
        int left = 0, right = size - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = e.compareTo(elements[mid]);
            if (cmp == 0) return mid;
            if (cmp < 0) right = mid - 1;
            else left = mid + 1;
        }
        return -1;
    }

    // позиция для вставки (поиск через бинарный поиск)
    private int findInsertPosition(E e) {
        int left = 0, right = size;
        while (left < right) {
            int mid = (left + right) / 2;
            if (e.compareTo(elements[mid]) > 0)
                left = mid + 1;
            else
                right = mid;
        }
        return left;
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException("null elements not supported");
        if (contains(e)) return false;
        ensureCapacity();
        int pos = findInsertPosition(e);
        // сдвигаем вправо
        for (int i = size; i > pos; i--) {
            elements[i] = elements[i - 1];
        }
        elements[pos] = e;
        size++;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        try {
            E e = (E) o;
            return binarySearch(e) >= 0;
        } catch (ClassCastException ex) {
            return false;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        try {
            E e = (E) o;
            int idx = binarySearch(e);
            if (idx < 0) return false;
            for (int i = idx; i < size - 1; i++) {
                elements[i] = elements[i + 1];
            }
            elements[--size] = null;
            return true;
        } catch (ClassCastException ex) {
            return false;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) elements[i] = null;
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
        sb.append(']');
        return sb.toString();
    }

    // ---------------- Методы с коллекциями ---------------- //

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c)
            if (add(e)) modified = true;
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c)
            if (remove(o)) modified = true;
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        int i = 0;
        while (i < size) {
            if (!c.contains(elements[i])) {
                remove(elements[i]);
                modified = true;
            } else {
                i++;
            }
        }
        return modified;
    }

    // --- Необязательные для задания методы ---
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean equals(Object o) { throw new UnsupportedOperationException(); }
    @Override public int hashCode() { throw new UnsupportedOperationException(); }
}
