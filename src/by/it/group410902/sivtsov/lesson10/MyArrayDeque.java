package by.it.group410902.sivtsov.lesson10;

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

    private void ensureCapacity() {
        if (size == elements.length) {
            @SuppressWarnings("unchecked")
            E[] newElements = (E[]) new Object[elements.length * 2];
            int firstPart = elements.length - head;
            System.arraycopy(elements, head, newElements, 0, firstPart);
            if (tail < head) {
                System.arraycopy(elements, 0, newElements, firstPart, tail);
            }

            elements = newElements;
            head = 0;
            tail = size;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            int index = (head + i) % elements.length;
            sb.append(elements[index]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
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
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }
        ensureCapacity();

        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;

        if (size == 1) {
            tail = (head + 1) % elements.length;
        }
    }

    @Override
    public void addLast(E element) {
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }
        ensureCapacity();

        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        return elements[head];
    }

    @Override
    public E getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        int lastIndex = (tail - 1 + elements.length) % elements.length;
        return elements[lastIndex];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (isEmpty()) {
            return null;
        }
        E element = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return element;
    }

    @Override
    public E pollLast() {
        if (isEmpty()) {
            return null;
        }
        tail = (tail - 1 + elements.length) % elements.length;
        E element = elements[tail];
        elements[tail] = null;
        size--;
        return element;
    }
    @Override public boolean isEmpty(){
        return size == 0;
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