package by.it.group451004.akbulatov.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class MyArrayDeque<E> implements Deque<E>
{
    private static final int DEFAULT_CAPACITY = 16;

    private E[] elements;
    private int head;
    private int tail;
    private int size;

    public MyArrayDeque()
    {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    private void ensureCapacity()
    {
        if (size == elements.length)
        {
            int newCap = elements.length * 2;
            E[] newArr = (E[]) new Object[newCap];

            System.arraycopy(elements, 0, newArr, 0, size);

            elements = newArr;
            head = 0;
            tail = size;
        }
    }

    @Override
    public boolean isEmpty()
    {
        return size == 0;
    }

    @Override
    public String toString()
    {
        if (isEmpty()) return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < size; i++)
        {
            E e = elements[(head + i) % elements.length];
            sb.append(e == null ? "null" : e.toString());
            if (i < size - 1) sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public boolean add(E element)
    {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element)
    {
        ensureCapacity();

        head = (head - 1 + elements.length) % elements.length;
        size++;

        elements[head] = element;
    }

    @Override
    public void addLast(E element)
    {
        ensureCapacity();

        elements[tail] = element;

        tail = (tail + 1) % elements.length;
        size++;
    }

    @Override
    public E element()
    {
        return getFirst();
    }

    @Override
    public E getFirst()
    {
        if (isEmpty()) throw new NoSuchElementException();
        return elements[head];
    }

    @Override
    public E getLast()
    {
        if (isEmpty()) throw new NoSuchElementException();
        int idx = (tail - 1 + elements.length) % elements.length;
        return elements[idx];
    }

    @Override
    public E poll()
    {
        return pollFirst();
    }

    @Override
    public E pollFirst()
    {
        if (isEmpty()) return null;

        E res = elements[head];
        elements[head] = null;

        head = (head + 1) % elements.length;

        if (--size == 0)
            head = tail = 0;

        return res;
    }

    @Override
    public E pollLast()
    {
        if (isEmpty()) return null;

        int idx = (tail - 1 + elements.length) % elements.length;

        E res = elements[idx];
        elements[idx] = null;

        tail = idx;

        if (--size == 0)
            head = tail = 0;

        return res;
    }

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public boolean offerFirst(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean offerLast(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { throw new UnsupportedOperationException(); }
    @Override public E remove() { throw new UnsupportedOperationException(); }
    @Override public E removeFirst() { return remove(); }
    @Override public E removeLast() { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    @Override public E peek() { throw new UnsupportedOperationException(); }
    @Override public E peekFirst() { throw new UnsupportedOperationException(); }
    @Override public E peekLast() { throw new UnsupportedOperationException(); }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public void push(E e) { throw new UnsupportedOperationException(); }
    @Override public E pop() { throw new UnsupportedOperationException(); }
}
