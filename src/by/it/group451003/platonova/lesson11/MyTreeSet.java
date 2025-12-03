package by.it.group451003.platonova.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyTreeSet<E extends Comparable<E>> implements Set<E> {

    private E[] data;
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 16;

    public MyTreeSet() {
        data = (E[]) new Comparable[DEFAULT_CAPACITY];
    }

    private void ensureCapacity() {
        if (size >= data.length) {
            E[] newData = (E[]) new Comparable[data.length * 2];
            System.arraycopy(data, 0, newData, 0, size);
            data = newData;
        }
    }

    // бинарный поиск: возвращает индекс элемента или -(вставка + 1)
    private int binarySearch(E e) {
        int left = 0, right = size - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = e.compareTo(data[mid]);
            if (cmp == 0) return mid;
            else if (cmp < 0) right = mid - 1;
            else left = mid + 1;
        }
        return - (left + 1);
    }

    @Override
    public boolean add(E e) {
        int idx = binarySearch(e);
        if (idx >= 0) return false; // уже есть
        idx = - (idx + 1); // место вставки
        ensureCapacity();
        // сдвигаем элементы вправо
        System.arraycopy(data, idx, data, idx + 1, size - idx);
        data[idx] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        try {
            E e = (E) o;
            int idx = binarySearch(e);
            if (idx < 0) return false; // нет элемента
            // сдвигаем элементы влево
            System.arraycopy(data, idx + 1, data, idx, size - idx - 1);
            data[size - 1] = null;
            size--;
            return true;
        } catch (ClassCastException ex) {
            return false;
        }
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
    public void clear() {
        for (int i = 0; i < size; i++) data[i] = null;
        size = 0;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(data[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    // ===== Методы Collection =====

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        for (E e : c) if (add(e)) modified = true;
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        for (Object o : c) if (remove(o)) modified = true;
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        int writeIndex = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(data[i])) {
                data[writeIndex++] = data[i];
            } else {
                modified = true;
            }
        }
        for (int i = writeIndex; i < size; i++) data[i] = null;
        size = writeIndex;
        return modified;
    }

    // ===== Заглушки для Set / Collection =====
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}
