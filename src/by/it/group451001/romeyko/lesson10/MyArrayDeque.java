package by.it.group451001.romeyko.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private E[] elements;
    private int head = 0; // индекс первого элемента
    private int tail = 0; // индекс после последнего элемента
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 8;

    public MyArrayDeque() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
    }

    // -----------------------------
    // Вспомогательные методы
    // -----------------------------
    private void ensureCapacity() {
        if (size == elements.length) {
            int newCap = elements.length * 2;
            E[] newArr = (E[]) new Object[newCap];
            for (int i = 0; i < size; i++) {
                newArr[i] = elements[(head + i) % elements.length];
            }
            elements = newArr;
            head = 0;
            tail = size;
        }
    }

    private int dec(int i) {
        return (i - 1 + elements.length) % elements.length;
    }

    private int inc(int i) {
        return (i + 1) % elements.length;
    }

    // -----------------------------
    // Обязательные методы
    // -----------------------------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[(head + i) % elements.length]);
            if (i < size - 1) sb.append(", ");
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
        return false;
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        ensureCapacity();
        head = dec(head);
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        ensureCapacity();
        elements[tail] = element;
        tail = inc(tail);
        size++;
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return elements[head];
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return elements[head];
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return elements[(tail - 1 + elements.length) % elements.length];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E elem = elements[head];
        elements[head] = null;
        head = inc(head);
        size--;
        return elem;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        tail = dec(tail);
        E elem = elements[tail];
        elements[tail] = null;
        size--;
        return elem;
    }

    // -----------------------------
    // Методы интерфейса Deque, которые мы не реализуем (заглушки)
    // -----------------------------
    @Override public boolean offer(E e) { return add(e); }
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E remove() { E e = poll(); if (e == null) throw new NoSuchElementException(); return e; }
    @Override public E removeFirst() { E e = pollFirst(); if (e == null) throw new NoSuchElementException(); return e; }
    @Override public E removeLast() { E e = pollLast(); if (e == null) throw new NoSuchElementException(); return e; }
    @Override public E peek() { return size == 0 ? null : elements[head]; }

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
    public void push(E e) {

    }

    @Override
    public E pop() {
        return null;
    }

    @Override public E peekFirst() { return peek(); }
    @Override public E peekLast() { return size == 0 ? null : elements[(tail - 1 + elements.length) % elements.length]; }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    // Остальные методы Deque можно оставить пустыми
    @Override public void clear() { while (poll() != null); }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override public java.util.Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
}
