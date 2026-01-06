package by.it.group451004.akbulatov.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E>
{
    private static final int DEFAULT_CAPACITY = 10;
    private E[] heap;
    private int size;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue()
    {
        this.heap = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty()
    {
        return size == 0;
    }

    @Override
    public void clear()
    {
        for (int i = 0; i < size; i++)
            heap[i] = null;

        size = 0;
    }

    @Override
    public boolean contains(Object o)
    {
        if (o == null) return false;
        // Простой линейный поиск, так как в куче элементы не отсортированы полностью
        for (int i = 0; i < size; i++) {
            if (o.equals(heap[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean add(E element)
    {
        return offer(element);
    }

    @Override
    public boolean offer(E element)
    {
        if (element == null)
            throw new NullPointerException();

        if (size >= heap.length)
            grow();

        int i = size++;

        if (i == 0)
            heap[0] = element;
        else
            siftUp(i, element);

        return true;
    }

    @Override
    public E remove()
    {
        E x = poll();

        if (x == null)
            throw new NoSuchElementException();

        return x;
    }

    @Override
    public E poll()
    {
        if (size == 0)
            return null;

        E result = heap[0];

        int s = --size;
        E last = heap[s];
        heap[s] = null;
        if (s != 0)
            siftDown(0, last);

        return result;
    }

    @Override
    public E element()
    {
        E x = peek();

        if (x == null)
            throw new NoSuchElementException();

        return x;
    }

    @Override
    public E peek()
    {
        return (size == 0) ? null : heap[0];
    }

    @Override
    public String toString()
    {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++)
        {
            sb.append(heap[i]);

            if (i < size - 1)
                sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean addAll(Collection<? extends E> c)
    {
        if (c == null)
            throw new NullPointerException();

        if (c == this)
            throw new IllegalArgumentException();

        boolean modified = false;

        for (E e : c)
            if (add(e))
                modified = true;

        return modified;
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        for (Object e : c)
            if (!contains(e))
                return false;

        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        boolean modified = false;

        int j = 0; // индекс записи (новый массив)

        for (int i = 0; i < size; i++)
        {
            if (!c.contains(heap[i]))
                heap[j++] = heap[i];
            else
                modified = true;
        }

        if (modified)
        {
            for (int k = j; k < size; k++)
                heap[k] = null;
            size = j;

            heapify();
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        boolean modified = false;
        int j = 0;

        for (int i = 0; i < size; i++)
        {
            if (c.contains(heap[i]))
                heap[j++] = heap[i];
            else
                modified = true;
        }

        if (modified)
        {
            for (int k = j; k < size; k++)
                heap[k] = null;
            size = j;

            heapify();
        }
        return modified;
    }

    @SuppressWarnings("unchecked")
    private void grow()
    {
        int newCapacity = heap.length << 1;

        E[] newHeap = (E[]) new Object[newCapacity];

        if (size >= 0) System.arraycopy(heap, 0, newHeap, 0, size);

        heap = newHeap;
    }

    @SuppressWarnings("unchecked")
    private void siftUp(int k, E x)
    {
        Comparable<? super E> key = (Comparable<? super E>) x;
        while (k > 0)
        {
            int parent = (k - 1) >>> 1;
            E e = heap[parent];

            if (key.compareTo(e) >= 0)
                break;

            heap[k] = e;
            k = parent;
        }
        heap[k] = x;
    }

    @SuppressWarnings("unchecked")
    private void siftDown(int k, E x)
    {
        Comparable<? super E> key = (Comparable<? super E>) x;
        int half = size >>> 1;
        while (k < half)
        {
            int child = (k << 1) + 1;
            E c = heap[child];
            int right = child + 1;

            // Если есть правый ребенок и он меньше левого
            if (right < size && ((Comparable<? super E>) c).compareTo(heap[right]) > 0)
                c = heap[child = right];

            if (key.compareTo(c) <= 0)
                break;

            heap[k] = c;
            k = child;
        }
        heap[k] = x;
    }

    private void heapify()
    {
        for (int i = (size >>> 1) - 1; i >= 0; i--)
            siftDown(i, heap[i]);
    }

    private boolean removeObject(Object o)
    {
        int i = indexOf(o);
        if (i == -1)
            return false;

        removeAt(i);

        return true;
    }

    private int indexOf(Object o)
    {
        if (o == null)
            return -1;

        for (int i = 0; i < size; i++)
            if (o.equals(heap[i]))
                return i;

        return -1;
    }

    private void removeAt(int i)
    {
        if (i == size - 1)
            heap[--size] = null;
        else
        {
            E moved = heap[--size];
            heap[size] = null;

            siftDown(i, moved);

            if (heap[i] == moved)
                siftUp(i, moved);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0;
            @Override
            public boolean hasNext() { return cursor < size; }
            @Override
            public E next() {
                if (cursor >= size) throw new NoSuchElementException();
                return heap[cursor++];
            }
        };
    }

    @Override public Object[] toArray() { return null; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public boolean remove(Object o) { return removeObject(o); }
}