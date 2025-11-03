package by.it.group451001.khomenkov.lesson11;

import java.util.Collection;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyTreeSet<E> implements Set<E> {

    private E[] elements;       // отсортированный массив
    private int size;           // количество элементов
    private final Comparator<E> comparator; // можно null — тогда используем Comparable

    private static final int DEFAULT_CAPACITY = 10;

    public interface Comparator<T> {
        int compare(T o1, T o2);
    }

    public MyTreeSet() {
        this(null);
    }

    public MyTreeSet(Comparator<E> comparator) {
        this.comparator = comparator;
        this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    // ================= Вспомогательные методы =================

    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        }
        return ((Comparable<E>) a).compareTo(b);
    }

    private int binarySearch(E key) {
        int low = 0;
        int high = size - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = compare(elements[mid], key);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid; // найден
            }
        }
        return -(low + 1); // не найден, возвращаем позицию вставки
    }

    private void ensureCapacity(int newSize) {
        if (newSize > elements.length) {
            int newCap = Math.max(newSize, elements.length * 2);
            E[] newArr = (E[]) new Object[newCap];
            for (int i = 0; i < size; i++) {
                newArr[i] = elements[i];
            }
            elements = newArr;
        }
    }

    // ================= Методы Set =================

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(Object o) {
        E e = (E) o;
        if (e == null) throw new NullPointerException("Null elements not supported");
        int idx = binarySearch(e);
        if (idx >= 0) return false; // уже есть
        int insertPos = -(idx + 1);
        ensureCapacity(size + 1);
        // сдвигаем элементы вправо
        for (int i = size; i > insertPos; i--) {
            elements[i] = elements[i - 1];
        }
        elements[insertPos] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        E e = (E) o;
        int idx = binarySearch(e);
        if (idx < 0) return false;
        // сдвигаем влево
        for (int i = idx; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[size - 1] = null;
        size--;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        E e = (E) o;
        return binarySearch(e) >= 0;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object x : c) {
            if (!contains(x)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E x : c) {
            if (add(x)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object x : c) {
            if (remove(x)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        int i = 0;
        while (i < size) {
            if (!c.contains(elements[i])) {
                remove(elements[i]);
                changed = true;
            } else {
                i++;
            }
        }
        return changed;
    }

    // Остальные методы Set (iterator, toArray) можно выбросить UnsupportedOperationException
    @Override
    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}
