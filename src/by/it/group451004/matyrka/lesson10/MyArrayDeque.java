package by.it.group451004.matyrka.lesson10;

import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.Collection;

public class MyArrayDeque<E> implements Deque<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    private E[] elements;
    private int head;
    private int tail;
    private int size;

    // Конструкторы
    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    @SuppressWarnings("unchecked")
    public MyArrayDeque(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
        elements = (E[]) new Object[initialCapacity];
        head = 0;
        tail = 0;
        size = 0;
    }

    @SuppressWarnings("unchecked")
    public MyArrayDeque(Collection<? extends E> c) {
        this(c.size() + DEFAULT_CAPACITY);
        addAll(c);
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            int index = (head + i) % elements.length;
            sb.append(elements[index]);
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
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        if (element == null) {
            throw new NullPointerException();
        }

        ensureCapacity(size + 1);

        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;

        if (size == 1) {
            tail = (head + 1) % elements.length;
        } else {
            tail = (head + size) % elements.length;
        }
    }

    @Override
    public void addLast(E element) {
        if (element == null) {
            throw new NullPointerException();
        }

        ensureCapacity(size + 1);

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

        if (size == 0) {
            head = tail = 0;
        }

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

        if (size == 0) {
            head = tail = 0;
        }

        return element;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length + (elements.length >> 1);
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            if (newCapacity > MAX_ARRAY_SIZE) {
                if (minCapacity > MAX_ARRAY_SIZE) {
                    throw new OutOfMemoryError("Required array size too large");
                }
                newCapacity = MAX_ARRAY_SIZE;
            }
            grow(newCapacity);
        }
    }

    @SuppressWarnings("unchecked")
    private void grow(int newCapacity) {
        E[] newElements = (E[]) new Object[newCapacity];

        if (head < tail) {
            System.arraycopy(elements, head, newElements, 0, size);
        } else if (size > 0) {
            int firstPart = elements.length - head;
            System.arraycopy(elements, head, newElements, 0, firstPart);
            System.arraycopy(elements, 0, newElements, firstPart, tail);
        }

        elements = newElements;
        head = 0;
        tail = (size == newCapacity) ? 0 : size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
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
    public E remove() {
        if (size == 0) throw new NoSuchElementException();
        return pollFirst();
    }

    @Override
    public E removeFirst() {
        if (size == 0) throw new NoSuchElementException();
        return pollFirst();
    }

    @Override
    public E removeLast() {
        if (size == 0) throw new NoSuchElementException();
        return pollLast();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public E peekFirst() {
        return (size == 0) ? null : elements[head];
    }

    @Override
    public E peekLast() {
        return (size == 0) ? null : elements[(tail - 1 + elements.length) % elements.length];
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
        throw new UnsupportedOperationException("remove(Object o) not supported");
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException("removeFirstOccurrence not supported");
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException("removeLastOccurrence not supported");
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException("contains not supported");
    }

    @Override
    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException("iterator not supported");
    }

    @Override
    public java.util.Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException("descendingIterator not supported");
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        for (int i = 0; i < elements.length; i++) {
            elements[i] = null;
        }
        head = tail = size = 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("containsAll not supported");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("removeAll not supported");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("retainAll not supported");
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        if (head < tail) {
            System.arraycopy(elements, head, result, 0, size);
        } else if (size > 0) {
            int firstPart = elements.length - head;
            System.arraycopy(elements, head, result, 0, firstPart);
            System.arraycopy(elements, 0, result, firstPart, tail);
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }

        if (head < tail) {
            System.arraycopy(elements, head, a, 0, size);
        } else if (size > 0) {
            int firstPart = elements.length - head;
            System.arraycopy(elements, head, a, 0, firstPart);
            System.arraycopy(elements, 0, a, firstPart, tail);
        }

        if (a.length > size) {
            a[size] = null;
        }

        return a;
    }
}