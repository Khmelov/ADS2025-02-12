package by.it.group451002.spitsyna.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {
    private E[] arrayDeque = (E[]) new Object[16];
    private int head = 0;
    private int tail = 0;
    private int size = 0;

    private void doubleCapacity() {
        int newCapacity = 0;
        if (arrayDeque.length < 64) {
            newCapacity = arrayDeque.length * 2;
        }
        else {
            newCapacity = (int)(arrayDeque.length * 1.5);
        }
        E[] newArrayDeque = (E[]) new Object[newCapacity];

        int index = head;
        for (int i = 0; i < size; i++){
            newArrayDeque[i] = arrayDeque[index];
            index = (index+1) % arrayDeque.length;
        }

        arrayDeque = newArrayDeque;
        head = 0;
        tail = size;
    }

    //////ОБЯЗАТЕЛЬНЫЕ ДЛЯ РЕАЛИЗАЦИИ МЕТОДЫ/////////////////
    @Override
    public String toString() {
        StringBuilder arrToStr = new StringBuilder("[");

        int index = head;
        for (int i = 0; i < size; i++){
            arrToStr.append(arrayDeque[index]);
            if (i < size-1)
                arrToStr.append(", ");

            index = (index+1) % arrayDeque.length;
        }

        arrToStr.append("]");
        return arrToStr.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E e) {
        if (e == null)
            throw new NullPointerException();

        if (size == arrayDeque.length)
            doubleCapacity();

        arrayDeque[tail] = e;
        tail = (tail+1) % arrayDeque.length;
        size++;
        return true;
    }

    @Override
    public void addFirst(E e) {
        if (e == null)
            throw new NullPointerException();

        if (size == arrayDeque.length){
            doubleCapacity();
        }

        head = (head - 1 + arrayDeque.length) % arrayDeque.length;
        arrayDeque[head] = e;
        size++;
    }

    @Override
    public void addLast(E e) {
        if (e == null){
            throw new NullPointerException();
        }

        if (size == arrayDeque.length){
            doubleCapacity();
        }

        arrayDeque[tail] = e;
        tail = (tail+1) % arrayDeque.length;
        size++;
    }

    @Override
    public E element() {
        if (size == 0)
            throw new NoSuchElementException();
        return arrayDeque[head];
    }

    @Override
    public E getFirst() {
        if (size == 0)
            throw new NoSuchElementException();
        return arrayDeque[head];
    }

    @Override
    public E getLast() {
        if (size == 0)
            throw new NoSuchElementException();
        return arrayDeque[(tail-1+arrayDeque.length)% arrayDeque.length];
    }

    //возвращает первый элемент и удаляет его из списка
    @Override
    public E poll() {
        if (size == 0)
            return null;

        E headElem = arrayDeque[head];
        arrayDeque[head] = null;
        head = (head+1) % arrayDeque.length;
        size--;
        return headElem;
    }

    @Override
    public E pollFirst() {
        if (size == 0)
            return null;

        E headElem = arrayDeque[head];
        arrayDeque[head] = null;
        head = (head+1) % arrayDeque.length;
        size--;
        return headElem;
    }

    @Override
    public E pollLast() {
        if (size == 0)
            return null;

        E tailElem = arrayDeque[(tail-1+arrayDeque.length) % arrayDeque.length];
        arrayDeque[(tail-1+arrayDeque.length) % arrayDeque.length] = null;
        tail = (tail-1+arrayDeque.length) % arrayDeque.length;
        size--;
        return tailElem;
    }

    /////////////НЕОБЯЗАТЕЛЬНЫЕ ДЛЯ РЕАЛИЗАЦИИ МЕТОДЫ//////////////////////

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
