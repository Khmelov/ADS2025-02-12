package by.it.group451004.redko.lesson10;

import java.util.*;

class MyArrayDeque<E> implements Deque<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private E[] elements;
    private int head;
    private int tail;
    private int size;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = elements.length * 2;
            E[] newElements = (E[]) new Object[newCapacity];

            for (int i = 0; i < size; i++) {
                newElements[i] = elements[(head + i) % elements.length];
            }

            elements = newElements;
            head = 0;
            tail = size - 1;
        }
    }

    private int wrapIndex(int index) {
        return (index % elements.length + elements.length) % elements.length;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[(head + i) % elements.length]);
            if (i < size - 1) {
                sb.append(", ");
            }
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
        return size == 0;
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

        if (size == 0) {
            // Первый элемент
            elements[head] = element;
            tail = head;
        } else {
            head = wrapIndex(head - 1);
            elements[head] = element;
        }
        size++;
    }

    @Override
    public void addLast(E element) {
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }

        ensureCapacity();

        if (size == 0) {
            // Первый элемент
            elements[tail] = element;
            head = tail;
        } else {
            tail = wrapIndex(tail + 1);
            elements[tail] = element;
        }
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        return elements[head];
    }

    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        return elements[tail];
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

        if (size == 1) {
            head = 0;
            tail = 0;
        } else {
            head = wrapIndex(head + 1);
        }
        size--;

        return element;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }

        E element = elements[tail];
        elements[tail] = null; // Помогаем сборщику мусора

        if (size == 1) {
            // Если был только один элемент
            head = 0;
            tail = 0;
        } else {
            tail = wrapIndex(tail - 1);
        }
        size--;

        return element;
    }

    // Остальные методы - не реализованы (оставляем как есть)
    @Override
    public boolean offer(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E peek() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean offerFirst(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean offerLast(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E removeFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E removeLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E peekFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E peekLast() {
        throw new UnsupportedOperationException();
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
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void push(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E pop() {
        throw new UnsupportedOperationException();
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