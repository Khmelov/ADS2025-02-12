package by.it.group451002.dirko.lesson10;

import java.util.*;

public class MyArrayDeque<E> implements Deque<E> {
    private Object[] elements = new Object[16];
    private int head = 8;
    private int tail = 8;

    private void grow()
    {
        final int oldCapacity = elements.length;
        final int newCapacity = oldCapacity * 2;
        final Object[] oldElements = elements;
        elements = new Object[newCapacity];
        final int halfDelta = (newCapacity - oldCapacity) / 2;
        System.arraycopy(oldElements, 0, elements, halfDelta, oldCapacity); //??
        head += halfDelta; tail += halfDelta;
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOfRange(elements, head, tail));
    }

    @Override
    public void addFirst(E e) {
        if (e == null)
            throw new NullPointerException();
        elements[--head] = e;
        if (head == 0)
            grow();
    }

    @Override
    public void addLast(E e) {
        if (e == null)
            throw new NullPointerException();
        elements[tail++] = e;
        if (tail == elements.length)
            grow();
    }

    @Override
    public boolean offerFirst(E e) {
        return false;
    }

    @Override
    public boolean offerLast(E e) {
        return false;
    }

    @Override
    public E removeFirst() {
        return null;
    }

    @Override
    public E removeLast() {
        return null;
    }

    @Override
    public E pollFirst() {
        E e = (E) elements[head];
        if (e != null)
            elements[head++] = null;
        return e;
    }

    @Override
    public E pollLast() {
        E e = (E) elements[--tail];
        if (e != null)
            elements[tail] = null;
        return e;
    }

    @Override
    public E getFirst() { return (E) elements[head]; }

    @Override
    public E getLast() { return (E) elements[tail - 1]; }

    @Override
    public E peekFirst() {
        return null;
    }

    @Override
    public E peekLast() {
        return null;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
        return null;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E element() { return getFirst(); }

    @Override
    public E peek() {
        return null;
    }

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

    @Override
    public void clear() {

    }

    @Override
    public void push(E e) {

    }

    @Override
    public E pop() {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public int size() { return tail - head; }

    @Override
    public boolean isEmpty() {
        return false;
    }

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

    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }


}
