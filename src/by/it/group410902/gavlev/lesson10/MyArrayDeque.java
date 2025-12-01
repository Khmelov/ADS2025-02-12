package by.it.group410902.gavlev.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Arrays;
import java.util.Collection;


public class MyArrayDeque<E> implements Deque<E> {

    private E[] data;

    private int size = 0;
    private int head = 0;
    private int tail = 0;

    public MyArrayDeque(int capacity) {
        data = (E[]) new Object[capacity];
    }

    public MyArrayDeque() {
        this(8);
    }

    private void updateCapacity() {
        E[] newData = (E[]) new Object[data.length * 3 / 2 + 1];
        if (head < tail) {
            System.arraycopy(data, head, newData, 0, size);
        }
        else {
            System.arraycopy(data, head, newData, 0, data.length - head);
            System.arraycopy(data, 0, newData, data.length - head, tail);
        }
        head = 0;
        tail = size;
        data = newData;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder answer = new StringBuilder(size * 3);
        answer.append("[");
        if (head < tail) {
            for (int i = head; i < tail - 1; i++) {
                answer.append(data[i].toString()).append(", ");
            }
        }
        else {
            for (int i = head; i < data.length; i++) {
                answer.append(data[i].toString()).append(", ");
            }
            for (int i = 0; i < tail - 1; i++) {
                answer.append(data[i].toString()).append(", ");
            }
        }
        answer.append(data[tail - 1].toString()).append("]");
        return answer.toString();
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
        if (size == data.length) updateCapacity();

        head = (head - 1 + data.length) % data.length;
        data[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        if (size == data.length) updateCapacity();

        data[tail] = element;
        tail = (tail + 1) % data.length;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException("Элемент отсутствует");
        return data[head];
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException("Элемент отсутствует");
        return data[(tail - 1 + data.length) % data.length];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E el = data[head];
        data[head] = null;
        head = (head + 1 + data.length) % data.length;
        size--;
        return el;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        int index = (tail - 1 + data.length) % data.length;
        E el = data[index];
        data[index] = null;
        tail = index;
        size--;
        return el;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int count = 0;
            private int index = head;
            @Override
            public boolean hasNext() {
                return count < size;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                E el = data[index];
                index = (index + 1) % data.length;
                count++;
                return el;
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private int count = 0;
            private int index = (tail - 1 + data.length) % data.length;
            @Override
            public boolean hasNext() {
                return count < size;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                E el = data[index];
                index = (index - 1 + data.length) % data.length;
                count++;
                return el;
            }
        };
    }


    @Override
    public boolean offer(E e) {
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
    public E remove() {
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
    public E peek() {
        return null;
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
    public void push(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E pop() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    // Методы из Collection
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return false;
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
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        Arrays.fill(data, null);
        head = tail = size = 0;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length > 0) a[0] = null;
        return a;
    }

}
