package by.it.group410901.borisdubinin.lesson11;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private E[] elements;
    private int size;
    private final Comparator<? super E> comparator;

    public MyTreeSet() {
        this(DEFAULT_CAPACITY, null);
    }

    public MyTreeSet(Comparator<? super E> comparator) {
        this(DEFAULT_CAPACITY, comparator);
    }

    public MyTreeSet(int initialCapacity, Comparator<? super E> comparator) {
        this.elements = (E[]) new Object[initialCapacity];
        this.comparator = comparator;
        this.size = 0;
    }

    private int binarySearch(E key) {
        int begin = 0;
        int end = size - 1;

        while (begin <= end) {
            int mid = (begin + end) / 2;
            E midVal = elements[mid];
            int cmp = compare(midVal, key);

            if (cmp < 0) {
                begin = mid + 1;
            } else if (cmp > 0) {
                end = mid - 1;
            } else {
                return mid;
            }
        }
        return -(begin+1);
    }

    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        } else {
            return ((Comparable<? super E>) a).compareTo(b);
        }
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = elements.length * 2;
            E[] oldArray = elements;
            elements = (E[]) new Object[newCapacity];

            System.arraycopy(oldArray, 0, elements, 0, oldArray.length);
        }
    }

    /// ////////////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////

    @Override
    public String toString(){
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
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
        for(int i = 0; i < size; i++)
            elements[i] = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException("Null elements not allowed");
        }

        ensureCapacity();

        int index = binarySearch(e);
        if (index >= 0) {
            return false; // элемент уже существует
        }

        // Вставляем в нужную позицию
        int insertionPoint = -index - 1;
        System.arraycopy(elements, insertionPoint, elements, insertionPoint + 1, size - insertionPoint);
        elements[insertionPoint] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            throw new NullPointerException("Null elements not allowed");
        }

        int index = binarySearch((E)o);
        if (index < 0) {
            return false; // элемент не найден
        }

        // Удаляем элемент, сдвигая массив
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        elements[--size] = null; // очищаем последний элемент
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            throw new NullPointerException("Null elements not allowed");
        }
        if (size == 0) {
            return false;
        }

        int index = binarySearch((E)o);
        return index >= 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;

        // Проходим по массиву с конца, чтобы избежать проблем со сдвигом индексов
        for (int i = size - 1; i >= 0; i--) {
            E element = elements[i];
            if (!c.contains(element)) {
                remove(element);  // remove сдвигает массив, поэтому идем с конца
                modified = true;
            }
        }

        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object element : c) {
            if (remove(element)) {
                modified = true;
            }
        }
        return modified;
    }


    /// /////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }
}
