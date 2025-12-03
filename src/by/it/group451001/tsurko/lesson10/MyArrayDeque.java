package by.it.group451001.tsurko.lesson10;

import java.util.*;

public class MyArrayDeque<E> implements Deque<E> {
    private Object[] elements;
    private int head;
    private int tail;
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
//    toString() --
//    size() --
//
//    add(E element) --
//    addFirst(E element) --
//    addLast(E element)  --
//
//    element() --
//    getFirst() --
//    getLast() --
//
//    poll() --
//    pollFirst() --
//    pollLast() --




    public MyArrayDeque() {
        elements = new Object[16]; // или любой дефолтный размер
        head = 0;
        tail = 0;
    }

    private void myGrow() {
        int oldCapacity = elements.length;
        int newCapacity = oldCapacity << 1;
        Object[] newElements = new Object[newCapacity];

        int r = oldCapacity - head;
        System.arraycopy(elements, head, newElements, 0, r);
        System.arraycopy(elements, 0, newElements, r, tail);

        head = 0;
        tail = oldCapacity;
        elements = newElements;
    }


    @Override
    public void addLast(E e) {
        if (e == null){
            throw new NullPointerException();
        }
        elements[tail] = e;
        tail = (tail + 1) % elements.length;
        if (tail == head) myGrow();
    }

    @Override
    public void addFirst(E e) {
        if (e == null){
            throw new NullPointerException();
        }
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = e;
        if (tail == head) myGrow();
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
        return null;
    }

    @Override
    public E removeLast() {
        return null;
    }

    @Override
    public E getFirst() {
        if (elements[head] == null) throw new NoSuchElementException();
        return (E) elements[head];
    }

    @Override
    public E getLast() {
        if (head == tail) throw new NoSuchElementException();
        int lastIndex = (tail - 1 + elements.length) % elements.length;
        return (E) elements[lastIndex];
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
    public E poll() {
        if (head == tail) return null;
        E elem = (E) elements[head];
        elements[head] = null;
        head = (head + 1 + elements.length) % elements.length;
        return elem;
    }

    @Override
    public E pollFirst() {
        return poll();
    }

    @Override
    public E pollLast() {
        if (head == tail) return null;
        int index = (tail - 1 + elements.length) % elements.length;
        E elem = (E) elements[index];
        tail = index;
        elements[tail] = null;
        return elem;
    }

    @Override
    public E element() {
        if (elements[head] == null) throw new NoSuchElementException();
        return (E) elements[head];
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
    public String toString() {
        StringBuilder output = new StringBuilder("");
        Iterator<E> it = iterator();
        output = output.append('[');
        while (it.hasNext()){
            E e = it.next();
            output.append(it == this ? "MyArrayDeque" : e);
            if (it.hasNext()) {
                output.append(", ");
            }
        }
        output.append(']');
        return output.toString();
    }

    @Override
    public int size() {
        return (tail - head + elements.length) % elements.length;
    }

    @Override
    public boolean isEmpty() {
        return false;
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
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private int index = head;
            private int remaining = (tail >= head)
                    ? tail - head
                    : tail + elements.length - head;

            @Override
            public boolean hasNext() {
                return remaining > 0;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                E e = (E) elements[index];
                index = (index + 1) % elements.length;
                remaining--;
                return e;
            }
        };
    }

}
