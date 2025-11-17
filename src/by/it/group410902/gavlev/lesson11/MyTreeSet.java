package by.it.group410902.gavlev.lesson11;

import java.util.*;

public class MyTreeSet<E> implements Set<E> {

    private E[] data;
    private int size = 0;
    private Comparator<? super E> comparator;

    @SuppressWarnings("unchecked")
    public MyTreeSet(int capacity, Comparator<? super E> comparator) {
        data = (E[]) new Object[capacity];
        this.comparator = comparator;
    }

    public MyTreeSet(int capacity) {
        this(capacity, null);
    }

    public MyTreeSet() {
        this(16, null);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        E[] newData = (E[]) new Object[data.length * 2];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        }
        else {
            return ((Comparable<? super E>) a).compareTo(b);
        }
    }

    private void swap(int i, int j) {
        E temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    // Возвращает либо позицию, где объект располагается либо должен располагаться
    private int binarySearch(Object o) {
        int left = 0;
        int right = size - 1;
        int mid;
        E obj = (E) o;
        while (left <= right) {
            mid = (left + right) / 2;
            int res = compare(obj, data[mid]);
            if (res > 0) {
                left = mid + 1;
            }
            else if (res < 0) {
                right = mid - 1;
            }
            else {
                return mid;
            }
        }

        // чтобы всегда были числа отрицательными если нужно вставить, потому что если без +1 то тогда 0 от -0 отличить нельзя было бы. Поэтому к возвр знач надо будет потом прибавить +1 чтобы компенсировать это.
        return -1 * (left + 1);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(Object o) {
        return binarySearch(o) >= 0;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (var el : this) {
            sb.append(el).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(Object o) {
        int index = binarySearch(o);
        if (index >= 0) {
            return false;
        }
        index = -(index + 1);
        if (index >= data.length) throw new IndexOutOfBoundsException();
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = (E) o;
        size++;
        if (size == data.length) resize();
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = binarySearch(o);
        return remove(index);
    }

    public boolean remove(int index) {
        if (index < 0) {
            return false;
        }
        System.arraycopy(data, index + 1, data, index, size - index - 1);
        data[size-1] = null;
        size--;
        return true;
    }

    @Override
    public Object[] toArray() {
        return data;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return (T[]) data;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (var el : collection) {
            if (!contains(el)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        boolean result = false;
        for (var el : collection) {
            if (!result) result = add(el);
            else add(el);
        }
        return result;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean result = false;
        for (var el : collection) {
            if (!result) result = remove(el);
            else remove(el);
        }
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean result = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E el = it.next();
            if (!collection.contains(el)) {
                it.remove();
                result = true;
            }
        }
        return result;
    }


    @Override
    public void clear() {
        Arrays.fill(data, null);
        size = 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int i = 0;
            @Override
            public boolean hasNext() {
                return i < size;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                return data[i++];
            }

            @Override
            public void remove() {
                MyTreeSet.this.remove(--i);
            }
        };
    }
}
