package by.it.group451003.plyushchevich.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {
    private E[] elements;
    private int head; // индекс первого элемента
    private int tail; // индекс позиции после последнего элемента
    private int size;

    @SuppressWarnings("unchecked")
    public MyArrayDeque(int initialCapacity) {
        if (initialCapacity < 1) initialCapacity = 16;
        elements = (E[]) new Object[initialCapacity];
        head = tail = size = 0;
    }


    public MyArrayDeque() {
        this(16);
    }

    private int capacity() {
        return elements.length;
    }



    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(elements[(head + i) % capacity()]);
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
        if (element == null) throw new NullPointerException("null elements not supported");
        ensureCapacity();
        head = (head - 1 + capacity()) % capacity();
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        if (element == null) throw new NullPointerException("null elements not supported");
        ensureCapacity();
        elements[tail] = element;
        tail = (tail + 1) % capacity();
        size++;
    }

    @Override
    public E element() {
        E e = peekFirst();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override
    public E getFirst() {
        E e = peekFirst();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override
    public E getLast() {
        E e = peekLast();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E e = elements[head];
        elements[head] = null;
        head = (head + 1) % capacity();
        size--;
        if (size == 0) { head = tail = 0; }
        return e;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        tail = (tail - 1 + capacity()) % capacity();
        E e = elements[tail];
        elements[tail] = null;
        size--;
        if (size == 0) { head = tail = 0; }
        return e;
    }




    //  Методы из Collection/Deque, не реализованные (требуются, но выбраны как unsupported)

    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o)  { throw new UnsupportedOperationException(); }
    @Override public void push(E e)                           { throw new UnsupportedOperationException(); }
    @Override public E pop()                                 { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray()                      { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a)                  { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    private void ensureCapacity() {
        if (size < capacity()) return;
        grow();
    }

    @SuppressWarnings("unchecked")
    private void grow() {
        int oldCap = capacity();
        int newCap = oldCap * 2 + 1;
        E[] newArr = (E[]) new Object[newCap];
        for (int i = 0; i < size; i++) {
            newArr[i] = elements[(head + i) % oldCap];
        }
        elements = newArr;
        head = 0;
        tail = size;
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
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public E peekFirst() {
        if (size == 0) return null;
        return elements[head];
    }

    @Override
    public E peekLast() {
        if (size == 0) return null;
        int idx = (tail - 1 + capacity()) % capacity();
        return elements[idx];
    }

    @Override
    public E remove() {
        E e = poll();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override
    public E removeFirst() {
        E e = pollFirst();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override
    public E removeLast() {
        E e = pollLast();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[(head + i) % capacity()] = null;
        }
        head = tail = size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            E e = elements[(head + i) % capacity()];
            if (o.equals(e)) return true;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        int cap = capacity();
        for (int i = 0; i < size; i++) {
            int idx = (head + i) % cap;
            if (o.equals(elements[idx])) {
                // сдвигаем все элементы после idx на одну позицию влево
                for (int j = i; j < size - 1; j++) {
                    elements[(head + j) % cap] = elements[(head + j + 1) % cap];
                }
                int lastPos = (head + size - 1) % cap;
                elements[lastPos] = null;
                tail = lastPos;
                size--;
                if (size == 0) head = tail = 0;
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int idx = 0;
            @Override
            public boolean hasNext() { return idx < size; }
            @Override
            public E next() {
                E e = elements[(head + idx) % capacity()];
                idx++;
                return e;
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private int idx = 0;
            @Override
            public boolean hasNext() { return idx < size; }
            @Override
            public E next() {
                int pos = (tail - 1 - idx + capacity()) % capacity();
                E e = elements[pos];
                idx++;
                return e;
            }
        };
    }
}

