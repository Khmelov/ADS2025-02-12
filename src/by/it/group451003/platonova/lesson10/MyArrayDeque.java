package by.it.group451003.platonova.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private E[] data;
    private int head = 0;      // index first
    private int tail = 0;      // index after last
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 8;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        data = (E[]) new Object[DEFAULT_CAPACITY];
    }

    private void ensureCapacity() {
        if (size < data.length) return;

        int newCap = data.length * 2;
        @SuppressWarnings("unchecked")
        E[] newArr = (E[]) new Object[newCap];

        for (int i = 0; i < size; i++) {
            newArr[i] = data[(head + i) % data.length];
        }

        data = newArr;
        head = 0;
        tail = size;
    }

    @Override
    public void addFirst(E e) {
        ensureCapacity();
        head = (head - 1 + data.length) % data.length;
        data[head] = e;
        size++;
    }

    @Override
    public void addLast(E e) {
        ensureCapacity();
        data[tail] = e;
        tail = (tail + 1) % data.length;
        size++;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return data[head];
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return data[(tail - 1 + data.length) % data.length];
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E e = data[head];
        data[head] = null;
        head = (head + 1) % data.length;
        size--;
        return e;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        tail = (tail - 1 + data.length) % data.length;
        E e = data[tail];
        data[tail] = null;
        size--;
        return e;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[(head + i) % data.length]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // ======== Заглушки для интерфейса Deque =========

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }

    @Override public boolean offer(E e) { addLast(e); return true; }
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }

    @Override public E remove() { return pollFirst(); }
    @Override public E removeFirst() { E e = pollFirst(); if (e == null) throw new NoSuchElementException(); return e; }
    @Override public E removeLast() { E e = pollLast(); if (e == null) throw new NoSuchElementException(); return e; }

    @Override public E peek() { return isEmpty() ? null : getFirst(); }
    @Override public E peekFirst() { return isEmpty() ? null : getFirst(); }
    @Override public E peekLast() { return isEmpty() ? null : getLast(); }

    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public void clear() { while (pollFirst() != null); }

    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }

    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return removeFirst(); }

    @Override public boolean addAll(Collection<? extends E> c) { return false; }

    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return a; }
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
}

