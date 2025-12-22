package by.it.group451001.volynets.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private E[] elements;
    private int head; // индекс первого элемента
    private int tail; // индекс позиции после последнего элемента
    private int size;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[8];
        head = 0;
        tail = 0;
        size = 0;
    }

    private int inc(int i, int mod) {
        i++;
        if (i >= mod) i = 0;
        return i;
    }

    private int dec(int i, int mod) {
        i--;
        if (i < 0) i = mod - 1;
        return i;
    }

    private void ensureCapacityForOneMore() {
        if (size < elements.length) return;
        grow();
    }

    @SuppressWarnings("unchecked")
    private void grow() {
        int oldCap = elements.length;
        int newCap = oldCap << 1;
        if (newCap <= 0) throw new OutOfMemoryError("Capacity overflow");
        E[] newArr = (E[]) new Object[newCap];
        int idx = head;
        for (int i = 0; i < size; i++) {
            newArr[i] = elements[idx];
            idx = inc(idx, oldCap);
        }
        elements = newArr;
        head = 0;
        tail = size;
    }

    @Override
    public String toString() {
        String[] parts = new String[size];
        int totalChars = 2; // '[' и ']'
        int idx = head;
        for (int i = 0; i < size; i++) {
            E e = elements[idx];
            String s = (e == null) ? "null" : String.valueOf(e);
            parts[i] = s;
            totalChars += s.length();
            idx = inc(idx, elements.length);
        }
        if (size > 1) totalChars += 2 * (size - 1); // ", "
        char[] out = new char[totalChars];
        int p = 0;
        out[p++] = '[';
        for (int i = 0; i < size; i++) {
            String s = parts[i];
            int len = s.length();
            for (int k = 0; k < len; k++) out[p++] = s.charAt(k);
            if (i + 1 < size) {
                out[p++] = ',';
                out[p++] = ' ';
            }
        }
        out[p++] = ']';
        return new String(out);
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
        ensureCapacityForOneMore();
        head = dec(head, elements.length);
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        ensureCapacityForOneMore();
        elements[tail] = element;
        tail = inc(tail, elements.length);
        size++;
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException("Deque is empty");
        return elements[head];
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException("Deque is empty");
        return elements[head];
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException("Deque is empty");
        int lastIndex = dec(tail, elements.length);
        return elements[lastIndex];
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
        head = inc(head, elements.length);
        size--;
        return val;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        tail = dec(tail, elements.length);
        E val = elements[tail];
        elements[tail] = null;
        size--;
        return val;
    }


    @Override public void push(E e) { throw new UnsupportedOperationException(); }
    @Override public E pop() { throw new UnsupportedOperationException(); }
    @Override public boolean offerFirst(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean offerLast(E e) { throw new UnsupportedOperationException(); }
    @Override public E removeFirst() { throw new UnsupportedOperationException(); }
    @Override public E removeLast() { throw new UnsupportedOperationException(); }
    @Override public E peekFirst() { throw new UnsupportedOperationException(); }
    @Override public E peekLast() { throw new UnsupportedOperationException(); }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }

    // Queue
    @Override public boolean offer(E e) { throw new UnsupportedOperationException(); }
    @Override public E remove() { throw new UnsupportedOperationException(); }
    @Override public E peek() { throw new UnsupportedOperationException(); }

    // Collection
    @Override public void clear() { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public boolean isEmpty() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }

}
