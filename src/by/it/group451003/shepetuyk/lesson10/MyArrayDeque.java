package by.it.group451003.shepetuyk.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private E[] array;
    private int head = 0;
    private int tail = -1;
    private static int capacity = 16;

    public MyArrayDeque(Collection<? extends E> c) {
        array = (E[]) new Object[capacity += c.size()];
        for (E element : c)
            add(element);
    }

    public MyArrayDeque(int size) {
        array = (E[]) new Object[capacity += size];
    }

    public MyArrayDeque() {
        this(capacity);
    }

    ////////////////////////////
    // Обязательные к реализации
    ////////////////////////////

    @Override
    public String toString() {
        StringBuilder strbldr = new StringBuilder();
        strbldr.append("[");

        int i = head;
        while (i <= tail) {
            strbldr.append(array[i] + ", ");
            i++;
        }

        int sbLength = strbldr.length();
        if (sbLength > 1)
            strbldr.delete(sbLength - 2, sbLength);
        strbldr.append("]");

        return strbldr.toString();
    }

    @Override
    public int size() {
        return tail - head + 1;
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        if (element == null)
            throw new NullPointerException();

        if (head == 0) {
            tail++;

            if (tail == capacity) {
                E[] tempArr = (E[]) new Object[capacity += 16];
                for (int i = 0; i < tail; i++)
                    tempArr[i] = array[i];

                array = tempArr;
            }

            for (int i = tail; i > 0; i--)
                array[i] = array[i - 1];
        }
        else head--;
        array[head] = element;
    }

    @Override
    public void addLast(E element) {
        if (element == null)
            throw new NullPointerException();

        tail++;
        if (tail == capacity) {
            E[] tempArr = (E[]) new Object[capacity += 16];
            for (int i = 0; i < tail; i++)
                tempArr[i] = array[i];

            array = tempArr;
        }

        array[tail] = element;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        E element = array[head];
        if (element == null ||  size() == 0)
            throw new NoSuchElementException();
        return element;
    }

    @Override
    public E getLast() {
        E element = array[tail];
        if (element == null ||  size() == 0)
            throw new NoSuchElementException();
        return element;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size() == 0)
            return null;
        else {
            E element = array[head];
            head++;
            return element;
        }
    }

    @Override
    public E pollLast() {
        if (size() == 0)
            return null;
        else {
            E element = array[tail];
            tail--;
            return element;
        }
    }

    ///////////////////////
    //   Необязательные
    ///////////////////////

    @Override
    public boolean isEmpty() {
        return size() == 0;
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
}
