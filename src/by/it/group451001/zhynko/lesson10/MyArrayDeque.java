package by.it.group451001.zhynko.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class MyArrayDeque<E> implements Deque<E> {
    private E[] elements;
    private int head;
    private int tail;
    private int size;
    private static final int DEFAULT_CAP = 16;

    @SuppressWarnings("unchecked")
    public MyArrayDeque(int capacity) {
        if (capacity < 1) capacity = DEFAULT_CAP;
        elements = (E[]) new Object[capacity];
        head = 0;
        tail = 0;
        size = 0;
    }

    public MyArrayDeque() {
        this(DEFAULT_CAP);
    }

    private int cap() { return elements.length; }

    private void ensureCapacity() {
        if (size == cap()) {
            grow();
        }
    }

    @SuppressWarnings("unchecked")
    private void grow() {
        int old = cap();
        int n = old << 1;
        E[] nArr = (E[]) new Object[n];
        for (int i = 0; i < size; i++) {
            nArr[i] = elements[(head + i) % old];
        }
        elements = nArr;
        head = 0;
        tail = size % n;
    }


    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            E e = elements[(head + i) % cap()];
            sb.append(String.valueOf(e));
            if (i != size - 1) sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        if (element == null) throw new NullPointerException();
        ensureCapacity();
        head = (head - 1 + cap()) % cap();
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        if (element == null) throw new NullPointerException();
        ensureCapacity();
        elements[tail] = element;
        tail = (tail + 1) % cap();
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return elements[head];
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        int idx = (tail - 1 + cap()) % cap();
        return elements[idx];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E r = elements[head];
        elements[head] = null;
        head = (head + 1) % cap();
        size--;
        if (size == 0) { head = tail = 0; }
        return r;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        tail = (tail - 1 + cap()) % cap();
        E r = elements[tail];
        elements[tail] = null;
        size--;
        if (size == 0) { head = tail = 0; }
        return r;
    }

    @Override public boolean offerFirst(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean offerLast(E e)  { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e)      { throw new UnsupportedOperationException(); }
    @Override public E peek()                { throw new UnsupportedOperationException(); }
    @Override public E peekFirst()           { throw new UnsupportedOperationException(); }
    @Override public E peekLast()            { throw new UnsupportedOperationException(); }
    @Override public E remove()              { throw new UnsupportedOperationException(); }
    @Override public E removeFirst()         { throw new UnsupportedOperationException(); }
    @Override public E removeLast()          { throw new UnsupportedOperationException(); }
    @Override public void push(E e)          { throw new UnsupportedOperationException(); }
    @Override public E pop()                 { throw new UnsupportedOperationException(); }

    @Override public boolean remove(Object o)                       { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o)                     { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator()                         { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator()               { throw new UnsupportedOperationException(); }
    @Override public boolean isEmpty()                              { throw new UnsupportedOperationException(); }
    @Override public void clear()                                   { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c)           { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c)      { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c)             { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c)             { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray()                             { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a)                         { throw new UnsupportedOperationException(); }
    @Override public boolean removeFirstOccurrence(Object o)        { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o)         { throw new UnsupportedOperationException(); }
}