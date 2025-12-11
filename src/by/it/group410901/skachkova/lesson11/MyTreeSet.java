package by.it.group410901.skachkova.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size;

    @SuppressWarnings("unchecked")
    public MyTreeSet()
    {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public boolean isEmpty()
    {
        return size == 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear()
    {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) //есть ли элемент
    {
        return binarySearch((E) o) >= 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean add(E e)
    {
        int index = binarySearch(e);
        if (index >= 0)
        {
            return false; //без повторений
        }

        if (size == elements.length)
        {
            resize();
        }

        int insertionPoint = -index - 1; //вычисляет позицию для вставки

        // сдвигаем элементы вправо
        for (int i = size; i > insertionPoint; i--)
        {
            elements[i] = elements[i - 1];
        }

        elements[insertionPoint] = e; //вставляем элемент
        size++;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o)
    {
        int index = binarySearch((E) o);
        if (index < 0)
        {
            return false;
        }

        //сдвигаем элементы влево
        for (int i = index; i < size - 1; i++)
        {
            elements[i] = elements[i + 1];
        }

        elements[size - 1] = null;
        size--;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        for (Object element : c)
        {
            if (!contains(element))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c)
    {
        boolean modified = false;
        for (E element : c)
        {
            if (add(element))
            {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        boolean modified = false;
        for (Object element : c)
        {
            if (remove(element))
            {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[i])) {
                remove(elements[i]);
                modified = true;
            }
        }
        return modified;
    }

    @SuppressWarnings("unchecked")
    private int binarySearch(E key)
    {
        int low = 0;
        int high = size - 1;

        while (low <= high)
        {
            int mid = (low + high) >>> 1; //безопасное вычисление середины
            E midVal = (E) elements[mid];
            int cmp = compare(midVal, key);

            if (cmp < 0) //больше среднего
            {
                low = mid + 1;
            }
            else if (cmp > 0) //меньше среднего
            {
                high = mid - 1;
            } else
            {
                return mid; // найден
            }
        }
        return -(low + 1); // не найден
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;

        // For Comparable objects
        if (a instanceof Comparable && b instanceof Comparable) {
            return ((Comparable<E>) a).compareTo(b);
        }

        // Fallback to hashCode comparison for non-Comparable objects
        return Integer.compare(a.hashCode(), b.hashCode());
    }

    private void resize() {
        Object[] newElements = new Object[elements.length * 2];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    @Override
    public String toString()
    {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(elements[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    // Required by interface but not implemented for this test
    @Override
    public Iterator<E> iterator() {
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