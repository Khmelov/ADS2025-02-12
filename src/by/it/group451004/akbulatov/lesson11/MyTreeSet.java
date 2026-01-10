package by.it.group451004.akbulatov.lesson11;

import java.util.*;

public class MyTreeSet<E extends Comparable<? super E>> implements Set<E>
{
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size;

    public MyTreeSet()
    {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    @Override
    public String toString()
    {
        if (size == 0) return "[]";

        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < size; i++)
        {
            result.append(elements[i]);
            if (i < size - 1)
                result.append(", ");
        }
        result.append("]");

        return result.toString();
    }

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public void clear()
    {
        Arrays.fill(elements, null);
        size = 0;
    }

    @Override
    public boolean isEmpty()
    {
        return size == 0;
    }

    @Override
    public boolean add(E e)
    {
        if (e == null)
            throw new NullPointerException("MyTreeSet не допускает null элементы!");

        int index = findIndex(e);
        if (index >= 0) return false;

        int insertionPoint = -(index + 1);
        ensureCapacity();

        System.arraycopy(elements, insertionPoint, elements, insertionPoint + 1, size - insertionPoint);

        elements[insertionPoint] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o)
    {
        int index = findIndex(o);

        if (index < 0) return false;

        System.arraycopy(elements, index + 1, elements, index, size - index - 1);

        elements[--size] = null;

        return true;
    }

    @Override
    public boolean contains(Object o)
    {
        return findIndex(o) >= 0;
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        for (Object o : c)
            if (!contains(o)) return false;

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c)
    {
        Iterator<? extends E> it = c.iterator();
        boolean modified = false;

        while (it.hasNext())
            if (add(it.next()))
                modified = true;

        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        boolean modified = false;
        int writeIndex = 0;

        for (int readIndex = 0; readIndex < size; readIndex++)
        {
            Object current = elements[readIndex];

            if (c.contains(current))
                modified = true;
            else
            {
                if (writeIndex != readIndex)
                    elements[writeIndex] = elements[readIndex];

                writeIndex++;
            }
        }

        if (modified)
        {
            for (int i = writeIndex; i < size; i++)
                elements[i] = null;

            size = writeIndex;
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        boolean modified = false;
        int writeIndex = 0;

        for (int readIndex = 0; readIndex < size; readIndex++)
        {
            Object current = elements[readIndex];

            if (c.contains(current))
            {
                if (writeIndex != readIndex)
                    elements[writeIndex] = elements[readIndex];

                writeIndex++;
            }
            else
                modified = true;
        }

        if (modified)
        {
            for (int i = writeIndex; i < size; i++)
                elements[i] = null;

            size = writeIndex;
        }

        return modified;
    }

    private void ensureCapacity()
    {
        if (size == elements.length)
        {
            int newCapacity = elements.length * 2;
            Object[] newElements = new Object[newCapacity];

            System.arraycopy(elements, 0, newElements, 0, size);

            elements = newElements;
        }
    }

    @SuppressWarnings("unchecked")
    private int findIndex(Object o)
    {
        E key;

        try
        {
            key = (E) o;
            if (key == null) return -1;
        }
        catch (ClassCastException | NullPointerException e)
        {
            return -1;
        }

        int low = 0;
        int high = size - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;

            E midVal = (E) elements[mid];
            int cmp = key.compareTo(midVal);

            if (cmp < 0)
                high = mid - 1;
            else if (cmp > 0)
                low = mid + 1;
            else
                return mid;
        }
        return -(low + 1);
    }

    @Override
    public Iterator<E> iterator()
    {
        return new Iterator<E>()
        {
            private int currentIndex = 0;

            @Override
            public boolean hasNext()
            {
                return currentIndex < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next()
            {
                if (!hasNext()) throw new NoSuchElementException();

                return (E) elements[currentIndex++];
            }

            @Override
            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }
}