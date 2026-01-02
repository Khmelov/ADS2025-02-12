package by.it.group451001.drzhevetskiy.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private E[] data;
    private int head = 0;
    private int tail = 0;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        data = (E[]) new Object[16]; // начальный размер
    }

    private int capacity() {
        return data.length;
    }

    private void ensureCapacity() {
        if (size < capacity()) return;

        int newCap = capacity() * 2;

        @SuppressWarnings("unchecked")
        E[] newArr = (E[]) new Object[newCap];

        // копируем элементы в правильном порядке
        for (int i = 0; i < size; i++) {
            newArr[i] = data[(head + i) % capacity()];
        }

        data = newArr;
        head = 0;
        tail = size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[(head + i) % capacity()]);
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
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        ensureCapacity();
        head = (head - 1 + capacity()) % capacity();
        data[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        ensureCapacity();
        data[tail] = element;
        tail = (tail + 1) % capacity();
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return data[head];
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        int idx = (tail - 1 + capacity()) % capacity();
        return data[idx];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E elem = data[head];
        data[head] = null;
        head = (head + 1) % capacity();
        size--;
        return elem;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        tail = (tail - 1 + capacity()) % capacity();
        E elem = data[tail];
        data[tail] = null;
        size--;
        return elem;
    }


    @Override public boolean offer(E e) { return add(e); }
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E remove() { return removeFirst(); }
    @Override public E removeFirst() { E e = pollFirst(); if (e == null) throw new NoSuchElementException(); return e; }
    @Override public E removeLast() { E e = pollLast(); if (e == null) throw new NoSuchElementException(); return e; }
    @Override public E peek() { return peekFirst(); }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override public E peekFirst() { return size == 0 ? null : data[head]; }
    @Override public E peekLast() { return size == 0 ? null : data[(tail - 1 + capacity()) % capacity()]; }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return removeFirst(); }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    @Override public void clear() { while (pollFirst() != null); }
}
