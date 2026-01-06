package by.it.group451004.maximenko.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private E[] elements;
    private int head = 0;
    private int tail = 0;
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 8;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size < elements.length) return;
        int newCapacity = elements.length * 2;
        E[] newArr = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newArr[i] = elements[(head + i) % elements.length];
        }
        elements = newArr;
        head = 0;
        tail = size;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(elements[(head + i) % elements.length]);
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {   // <── добавлен нужный метод
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        ensureCapacity();
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        ensureCapacity();
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        size++;
    }

    @Override
    public E element() {
        if (size == 0)
            throw new NoSuchElementException();
        return elements[head];
    }

    @Override
    public E getFirst() {
        if (size == 0)
            throw new NoSuchElementException();
        return elements[head];
    }

    @Override
    public E getLast() {
        if (size == 0)
            throw new NoSuchElementException();
        return elements[(tail - 1 + elements.length) % elements.length];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E val = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return val;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        tail = (tail - 1 + elements.length) % elements.length;
        E val = elements[tail];
        elements[tail] = null;
        size--;
        return val;
    }

    /////////////////////////////////////////////////////////////////////////
    //////           Остальные методы (для совместимости)             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean offer(E e) {
        addLast(e);
        return true;
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
    public E peek() {
        return peekFirst();
    }

    @Override
    public E peekFirst() {
        return size == 0 ? null : elements[head];
    }

    @Override
    public E peekLast() {
        return size == 0 ? null : elements[(tail - 1 + elements.length) % elements.length];
    }

    @Override
    public E remove() {
        E val = poll();
        if (val == null) throw new NoSuchElementException();
        return val;
    }

    @Override
    public E removeFirst() {
        E val = pollFirst();
        if (val == null) throw new NoSuchElementException();
        return val;
    }

    @Override
    public E removeLast() {
        E val = pollLast();
        if (val == null) throw new NoSuchElementException();
        return val;
    }

    @Override
    public void clear() {
        for (int i = 0; i < elements.length; i++) elements[i] = null;
        head = 0;
        tail = 0;
        size = 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int idx = 0;
            @Override
            public boolean hasNext() {
                return idx < size;
            }
            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                return elements[(head + idx++) % elements.length];
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            int idx = size - 1;
            @Override
            public boolean hasNext() {
                return idx >= 0;
            }
            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                return elements[(head + idx--) % elements.length];
            }
        };
    }

    /////////////////////////////////////////////////////////////////////////
    //////          Необязательные методы (заглушки)                  ///////
    /////////////////////////////////////////////////////////////////////////

    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return removeFirst(); }
}
