package by.it.group451004.volynets.lesson10;

import java.util.Deque;
import java.util.Iterator;

public class MyArrayDeque<E> implements Deque<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size;
    private int head; // индекс первого элемента
    private int tail; // индекс следующего за последним элемента
    // циклический буфер

    // двухсторонняя очередь
    public MyArrayDeque() {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
        head = 0;
        tail = 0;
    }

    public MyArrayDeque(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
        elements = new Object[initialCapacity];
        size = 0;
        head = 0;
        tail = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    // строковое представление дека
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[(head + i) % elements.length]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // количество элементов в деке
    @Override
    public int size() {
        return size;
    }

    // добавление элемента в конец (addLast)
    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    // добавление элемента в начало
    @Override
    public void addFirst(E e) {
        ensureCapacity(size + 1);
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = e;
        size++;
    }

    // добавление элемента в конец
    @Override
    public void addLast(E e) {
        ensureCapacity(size + 1);
        elements[tail] = e;
        tail = (tail + 1) % elements.length;
        size++;
    }

    // получение первого элемента без удаления (бросает исключение если пусто)
    @Override
    public E element() {
        return getFirst();
    }

    // получение первого элемента без удаления
    @Override
    @SuppressWarnings("unchecked")
    public E getFirst() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }
        return (E) elements[head];
    }

    // получение последнего элемента без удаления
    @Override
    @SuppressWarnings("unchecked")
    public E getLast() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }
        return (E) elements[(tail - 1 + elements.length) % elements.length];
    }

    // удаление и возврат первого элемента (возвращает null если пусто)
    @Override
    public E poll() {
        return pollFirst();
    }

    // удаление и возврат первого элемента
    @Override
    @SuppressWarnings("unchecked")
    public E pollFirst() {
        if (size == 0) {
            return null;
        }
        E element = (E) elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return element;
    }

    // удаление и возврат последнего элемента
    @Override
    @SuppressWarnings("unchecked")
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        tail = (tail - 1 + elements.length) % elements.length;
        E element = (E) elements[tail];
        elements[tail] = null;
        size--;
        return element;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы                     ///////
    /////////////////////////////////////////////////////////////////////////

    // увеличение емкости массива при необходимости
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length * 2;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            Object[] newElements = new Object[newCapacity];

            // Копируем элементы с учетом циклического буфера
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[(head + i) % elements.length];
            }

            elements = newElements;
            head = 0;
            tail = size;
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////       Остальные методы Deque - заглушки                ///////
    /////////////////////////////////////////////////////////////////////////

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
    public void clear() {
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
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Iterator<E> descendingIterator() {
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