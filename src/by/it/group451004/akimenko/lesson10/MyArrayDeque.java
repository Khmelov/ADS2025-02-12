package lesson10;

import lesson09.ListB;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private E[] elements = (E[]) (new Object[10]);
    private int size = 0;

    @Override
    public String toString()
    {
        StringBuilder res = new StringBuilder("[");
        var it = iterator();
        if (it.hasNext())
            res.append(it.next().toString());
        while (it.hasNext()) {
            res.append(", ");
            res.append(it.next().toString());
        }
        res.append("]");
        return res.toString();
    }
    @Override
    public int size()
    {
        return size;
    }
    private void grow()
    {
        E[] newElements = (E[]) (new Object[elements.length * 2]);
        System.arraycopy(elements, 0, newElements, 0, elements.length);
        elements = newElements;
    }
    @Override
    public void addFirst(E e) {
        if(size == elements.length)
            grow();
        System.arraycopy(elements, 0, elements, 1, size);
        elements[0] = e;
        size++;
    }

    @Override
    public void addLast(E e) {
        if(size == elements.length)
            grow();
        elements[size++] = e;
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
        if(size == 0)
            return null;
        E e = elements[0];
        System.arraycopy(elements, 1, elements, 0, size - 1);
        size--;
        return e;
    }

    @Override
    public E pollLast() {
        return elements[--size];
    }

    @Override
    public E getFirst() {
        if(size == 0)
            throw new NoSuchElementException();
        return elements[0];
    }

    @Override
    public E getLast() {
        if(size == 0)
            throw new NoSuchElementException();
        return elements[size - 1];
    }

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
        if(size == 0)
            return null;
        E e = elements[0];
        System.arraycopy(elements, 1, elements, 0, size - 1);
        size--;
        return e;
    }

    @Override
    public E element() {
        if(size == 0)
            throw new NoSuchElementException();
        return elements[0];
    }

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
    public boolean isEmpty() {
        return false;
    }
    private class DequeIterator implements Iterator<E> {

        int cursor = 0;
        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            return elements[cursor++];
        }
    }
    @Override
    public Iterator<E> iterator() {
        return new DequeIterator();
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

    @Override
    public Deque<E> reversed() {
        return Deque.super.reversed();
    }
}
