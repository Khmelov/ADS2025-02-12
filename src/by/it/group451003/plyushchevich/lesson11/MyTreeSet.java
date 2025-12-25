package by.it.group451003.plyushchevich.lesson11;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(elements[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        int idx = binarySearch((E) o);
        return idx >= 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null) return false;
        int idx = binarySearch(e);
        if (idx >= 0) return false; // уже есть

        ensureCapacity();

        // восстановление позиции вставки из отрицательного кода
        int insertPos = -(idx + 1);

        for (int i = size; i > insertPos; i--) {
            // Сдвиг элементов вправо от insertPos до size-1
            elements[i] = elements[i - 1];
        }
        elements[insertPos] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        int idx = binarySearch((E) o);
        if (idx < 0) return false;

        // сдвигаем элементы влево, перезаписывая позицию idx
        for (int i = idx; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        size--;
        return true;
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

    // ----------- вспомогательные -----------

    private void ensureCapacity() {
        if (size >= elements.length) {
            E[] newArr = (E[]) new Comparable[elements.length * 2];
            for (int i = 0; i < size; i++) {
                newArr[i] = elements[i];
            }
            elements = newArr;
        }
    }

    private int binarySearch(E key) {
        int low = 0, high = size - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = key.compareTo(elements[mid]);
            if (cmp > 0) low = mid + 1; // ключ больше — ищем в правой половине
            else if (cmp < 0) high = mid - 1; // ключ меньше — ищем в левой половине
            else return mid; // найден
        }
        return -(low + 1); // не найден — возвращаем код для вычисления позиции вставки
    }

    // ----------- заглушки -----------

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("iterator() не реализован");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("toArray() не реализован");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("toArray(T[]) не реализован");
    }

    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException("equals() не реализован");
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("hashCode() не реализован");
    }
}

