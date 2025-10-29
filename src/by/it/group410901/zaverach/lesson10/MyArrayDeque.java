package by.it.group410901.zaverach.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
public class MyArrayDeque<E> implements Deque<E> {
    private static final int DEF_CAPACITY = 10;
    private int head;
    private int tail;
    private int size;
    private Object[] elements;

    MyArrayDeque(){
        this.elements = new Object[DEF_CAPACITY];
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }

    MyArrayDeque(int initialCapacity){
        if(initialCapacity<0) throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);

        this.elements = new Object[initialCapacity];
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = elements.length * 2;
            Object[] newArr = new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newArr[i] = elements[(head + i) % elements.length];
            }
            elements = newArr;
            head = 0;
            tail = size;
        }
    }


    @Override
    public String toString(){
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[(head + i) % elements.length]);
            if (i != size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void addFirst(E e) {
        ensureCapacity();
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = e;
        size++;
    }

    @Override
    public void addLast(E e) {
        ensureCapacity();
        elements[tail] = e;
        tail = (tail + 1) % elements.length;
        size++;
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return (E) elements[head];
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return (E) elements[head];
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return (E) elements[(tail - 1 + elements.length) % elements.length];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E val = (E) elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return val;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        tail = (tail - 1 + elements.length) % elements.length;
        E val = (E) elements[tail];
        elements[tail] = null;
        size--;
        return val;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public E peekFirst() {
        return null;
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
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
        return null;
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

    @Override
    public Iterator<E> iterator() {
        return null;
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
}