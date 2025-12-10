package by.it.group451003.sorokin.lesson10;

import java.util.Deque;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {
    private E[] elements;
    private int size;
    private int head;
    private int tail;
    private static final int INITIAL_CAPACITY = 10;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[INITIAL_CAPACITY];
        size = 0;
        head = 0;
        tail = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

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

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E element) {
        // addLast(element)
        return offerLast(element);
    }

    @Override
    public void addFirst(E element) {
        // Проверяем и увеличиваем массив если нужно
        if (size == elements.length) {
            resize();
        }

        // Сдвигаем head назад по кругу
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        if (size == elements.length) {
            resize();
        }

        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        size++;
    }

    @Override
    public E element() {
        // getFirst()
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        return elements[head];
    }

    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty");
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
        return element;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы                      ///////
    /////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = elements.length * 2;
        E[] newElements = (E[]) new Object[newCapacity];

        // Копируем элементы в новый массив в правильном порядке
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[(head + i) % elements.length];
        }

        elements = newElements;
        head = 0;
        tail = size;
    }

    /////////////////////////////////////////////////////////////////////////
    //////       Остальные методы Deque - можно оставить пустыми     ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean offerFirst(E e) { return false; }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
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
    public E peekFirst() {
        return (size == 0) ? null : getFirst();
    }

    @Override
    public E peekLast() {
        return (size == 0) ? null : getLast();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) { return false; }

    @Override
    public boolean removeLastOccurrence(Object o) { return false; }

    @Override
    public boolean offer(E e) { return offerLast(e); }

    @Override
    public E remove() { return removeFirst(); }

    @Override
    public E peek() { return peekFirst(); }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) { return false; }

    @Override
    public boolean removeAll(java.util.Collection<?> c) { return false; }

    @Override
    public boolean retainAll(java.util.Collection<?> c) { return false; }

    @Override
    public void clear() {
        while (size > 0) {
            pollFirst();
        }
    }

    @Override
    public void push(E e) { addFirst(e); }

    @Override
    public E pop() { return removeFirst(); }

    @Override
    public boolean remove(Object o) { return false; }

    @Override
    public boolean containsAll(java.util.Collection<?> c) { return false; }

    @Override
    public boolean contains(Object o) { return false; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public java.util.Iterator<E> iterator() { return null; }

    @Override
    public java.util.Iterator<E> descendingIterator() { return null; }

    @Override
    public Object[] toArray() { return new Object[0]; }

    @Override
    public <T> T[] toArray(T[] a) { return null; }
}