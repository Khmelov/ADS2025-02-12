package by.it.group410902.siomchena.lesson10;

import java.util.*;

public class MyArrayDeque<E> implements Deque<E> {
    private E[] elements;
    private int size;
    private int head;
    private int tail;

    public MyArrayDeque() {
        elements = (E[]) new Object[10];
        size = 0;
        head = 0;
        tail = 0;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            int index = (head + i) % elements.length;
            sb.append(elements[index]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = elements.length * 2;
            E[] newElements = (E[]) new Object[newCapacity];

            for (int i = 0; i < size; i++) {
                int index = (head + i) % elements.length;
                newElements[i] = elements[index];
            }

            elements = newElements;
            head = 0;
            tail = size;
        }
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void addFirst(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        ensureCapacity();

        head = (head - 1 + elements.length) % elements.length;
        elements[head] = e;
        size++;
    }

    @Override
    public void addLast(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        ensureCapacity();

        elements[tail] = e;
        tail = (tail + 1) % elements.length;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return elements[head];
    }

    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return elements[(tail - 1 + elements.length) % elements.length];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) {
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
        if (size == 0) {
            return null;
        }
        tail = (tail - 1 + elements.length) % elements.length;
        E element = elements[tail];
        elements[tail] = null;
        size--;
        return element;
    }

    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return pollFirst();
    }

    @Override
    public E removeLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return pollLast();
    }

    @Override
    public E peekFirst() {
        if (size == 0) {
            return null;
        }
        return elements[head];
    }

    @Override
    public E peekLast() {
        if (size == 0) {
            return null;
        }
        return elements[(tail - 1 + elements.length) % elements.length];
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            int index = (head + i) % elements.length;
            if (Objects.equals(o, elements[index])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            add(e);
        }
        return !c.isEmpty();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            while (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            int index = (head + i) % elements.length;
            elements[index] = null;
        }
        size = 0;
        head = 0;
        tail = 0;
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