package by.it.group451004.rak.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

/*
    Задание на уровень А
    Создайте class MyArrayDeque<E>, который реализует интерфейс Deque<E>
    и работает на основе приватного массива типа E[]

                toString()
                size()

                add(E element)
                addFirst(E element)
                addLast(E element)

                element()
                getFirst()
                getLast()

                poll()
                pollFirst()
                pollLast()
*/

public class MyArrayDeque<E> implements Deque<E> {

    E[] elements;
    int head;
    int tail;

    public MyArrayDeque(){
        this(16);
    }

    public MyArrayDeque(int initialCapacity){
        elements = (E[]) new Object[initialCapacity];
        head = 0;
        tail = 0;
    }

    //ЗАДАНИЕ

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("[");
        boolean isFirstElement = true;
        for (int i = head; i != tail; i = (i + 1) % elements.length) {
            if (isFirstElement) {
                s.append(elements[i]);
                isFirstElement = false;
            } else {
                s.append(", ");
                s.append(elements[i]);
            }
        }
        s.append("]");
        return s.toString();
    }

    @Override
    public int size() {
        return (tail - head + elements.length) % elements.length;
    }

    private void expandArray(){
        E[] temp = (E[]) new Object[elements.length * 2];
        int j = 0;
        for (int i = head; i != tail; i = (i + 1) % elements.length)
            temp[j++] = elements[i];
        tail = j;
        head = 0;
        elements = temp;
    }

    @Override
    public void addFirst(E e) {
        if ((tail + 1) % elements.length == head)
            expandArray();

        head = (head - 1 + elements.length) % elements.length;
        elements[head] = e;
    }

    @Override
    public void addLast(E e) {
        if ((tail + 1) % elements.length == head)
            expandArray();

        elements[tail] = e;
        tail = (tail + 1) % elements.length;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (head == tail)
            throw new NoSuchElementException();
        return elements[head];
    }

    @Override
    public E getLast() {
        if (head == tail)
            throw new NoSuchElementException();
        return elements[(tail - 1 + elements.length) % elements.length];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (head == tail)
            return null;
        E result = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        return result;
    }

    @Override
    public E pollLast() {
        if (head == tail)
            return null;
        E result = elements[(tail - 1 + elements.length) % elements.length];
        elements[(tail - 1 + elements.length) % elements.length] = null;
        tail = (tail - 1 + elements.length) % elements.length;
        return result;
    }

    //ДОПОЛНИТЕЛЬНО РЕАЛИЗОВАНО

    @Override
    public E peekFirst() {
        if (head == tail)
            return null;

        return elements[head];
    }

    @Override
    public E peekLast() {
        if (head == tail)
            return null;

        return elements[(tail - 1 + elements.length) % elements.length];
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public boolean isEmpty() {
        return head == tail;
    }

    @Override
    public void clear() {
        for (int i = head; i != tail; i = (i + 1) % elements.length)
            elements[i] = null;
        tail = 0;
        head = 0;
    }

    //ЗАГЛУШКИ

    @Override
    public E removeLast() {
        return null;
    }

    @Override
    public E removeFirst() {
        return null;
    }

    @Override
    public boolean offer(E e) {
        return false;
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
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override
    public E remove() {
        return null;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
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
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Iterator<E> descendingIterator() {
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
    public boolean containsAll(Collection<?> c) {
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
}
