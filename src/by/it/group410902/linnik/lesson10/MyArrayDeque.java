package by.it.group410902.linnik.lesson10;
import java.util.NoSuchElementException;
import java.util.*;

public class MyArrayDeque<E> implements Deque<E>{
    private E[] el;
    private int size;
    private  int head;
    private int tail;

    public MyArrayDeque() {
        el = (E[]) new Object[8]; // начальная ёмкость
        head = 0;
        tail = 0;
        size = 0;
    }

    private void ensureCapacity() {
        if (size == el.length) {
            E[] newArr = (E[]) new Object[el.length * 2];
            for (int i = 0; i < size; i++) {
                newArr[i] = el[(head + i) % el.length];
            }
            el = newArr;
            head = 0;
            tail = size;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(el[(head + i) % el.length]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    public int size() {
        return size;
    }

    public boolean add(E element) {
        addLast(element);
        return true;
    }

    public void addFirst(E element) {
        ensureCapacity();
        head = (head - 1 + el.length) % el.length;
        el[head] = element;
        size++;
    }

    public void addLast(E element) {
        ensureCapacity();
        el[tail] = element;
        tail = (tail + 1) % el.length;
        size++;
    }

    public E element() {
        return getFirst();
    }

    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return el[head];
    }

    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return el[(tail - 1 + el.length) % el.length];
    }

    public E poll() {
        return pollFirst();
    }

    public E pollFirst() {
        if (size == 0) return null;
        E value = el[head];
        el[head] = null;
        head = (head + 1) % el.length;
        size--;
        return value;
    }

    public E pollLast() {
        if (size == 0) return null;
        tail = (tail - 1 + el.length) % el.length;
        E value = el[tail];
        el[tail] = null;
        size--;
        return value;
    }

    @Override public void push(E e) { throw new UnsupportedOperationException(); }
    @Override public E pop() { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean offerFirst(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean offerLast(E e) { throw new UnsupportedOperationException(); }
    @Override public E peek() { throw new UnsupportedOperationException(); }
    @Override public E peekFirst() { throw new UnsupportedOperationException(); }
    @Override public E peekLast() { throw new UnsupportedOperationException(); }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean isEmpty() { return size == 0; }
    @Override public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public E remove() { throw new UnsupportedOperationException(); }
    @Override public E removeFirst() { throw new UnsupportedOperationException(); }
    @Override public E removeLast() { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException();    }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException();}
    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }
}
