package by.it.group410901.zubchonak.lesson10;

import java.util.*;

public class MyArrayDeque<E> implements Deque<E> {

    private static final int INITIAL_CAPACITY = 16;
    private Object[] elements;
    private int head;
    private int tail;
    private int size;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = new Object[INITIAL_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    private int mod(int index, int capacity) {
        return (index % capacity + capacity) % capacity;
    }
//Увеличение ёмкости при необходимости
    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == elements.length) {
            Object[] newElements = new Object[elements.length * 2];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[mod(head + i, elements.length)];
            }
            elements = newElements;
            head = 0;
            tail = size;
        }
    }
//Метод toString — возвращает строку вида [элем1, элем2, ...].
    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(elements[mod(head + i, elements.length)]);
        }
        return sb.append("]").toString();
    }
//Метод size — возвращает текущее количество элементов.
    @Override
    public int size() {
        return size;
    }
//Метод add — добавляет элемент в конец
    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }
//Метод addFirst — добавляет элемент в начало
    @Override
    public void addFirst(E e) {
        if (e == null) throw new NullPointerException();
        ensureCapacity();
        head = mod(head - 1, elements.length);
        elements[head] = e;
        size++;
    }
//Добавление в конец
    @Override
    public void addLast(E e) {
        if (e == null) throw new NullPointerException();
        ensureCapacity();
        elements[tail] = e;
        tail = mod(tail + 1, elements.length);
        size++;
    }
//Метод `element` — возвращает первый элемент
    @Override
    public E element() {
        return getFirst();
    }
//Метод `getFirst` — возвращает первый элемент
    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        @SuppressWarnings("unchecked") E first = (E) elements[head];
        return first;
    }
//Метод `getLast` — возвращает последний элемент
    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        int idx = mod(tail - 1, elements.length);
        @SuppressWarnings("unchecked") E last = (E) elements[idx];
        return last;
    }
//Метод `poll` — удаляет и возвращает первый элемент
    @Override
    public E poll() {
        return pollFirst();
    }
//Метод `pollFirst` — удаляет и возвращает первый элемент.
    @Override
    public E pollFirst() {
        if (size == 0) return null;
        @SuppressWarnings("unchecked") E first = (E) elements[head];
        elements[head] = null;
        head = mod(head + 1, elements.length);
        size--;
        return first;
    }
//Метод `pollLast` — удаляет и возвращает последний элемент
    @Override
    public E pollLast() {
        if (size == 0) return null;
        int idx = mod(tail - 1, elements.length);
        @SuppressWarnings("unchecked") E last = (E) elements[idx];
        elements[idx] = null;
        tail = idx;
        size--;
        return last;
    }

    // === Обязательная реализация остальных методов интерфейса Deque ===
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean offerFirst(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean offerLast(E e) { throw new UnsupportedOperationException(); }
    @Override public E peek() { throw new UnsupportedOperationException(); }
    @Override public E peekFirst() { throw new UnsupportedOperationException(); }
    @Override public E peekLast() { throw new UnsupportedOperationException(); }
    @Override public E remove() { throw new UnsupportedOperationException(); }
    @Override public E removeFirst() { throw new UnsupportedOperationException(); }
    @Override public E removeLast() { throw new UnsupportedOperationException(); }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean isEmpty() { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public E pop() { throw new UnsupportedOperationException(); }
    @Override public void push(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
}