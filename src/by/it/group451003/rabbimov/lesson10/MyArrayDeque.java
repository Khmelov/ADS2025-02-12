package by.it.group451003.rabbimov.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyArrayDeque<E> implements Deque<E> {
    final static int InitialSize = 8;
    int _front;
    int _rear;
    int _size;
    E[] _elements;

    MyArrayDeque() {
        this(InitialSize);
    }

    MyArrayDeque(int size) {
        _front = size / 2;
        _rear = _front - 1;
        _size = 0;
        _elements = (E[]) new Object[size];
    }

    private void Resize(int size) {
        E[] temp = (E[]) new Object[size];
        int k = (size - _size) / 2;

        for (int i = 0; i < _size; i++) {
            temp[i + k] = _elements[_front + i];
        }

        _front = k;
        _rear = k + _size - 1;
        _elements = temp;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append('[');
        for (int i = 0; i < _size; i++) {
            sb.append(_elements[_front + i]);
            if (i < _size - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');

        return sb.toString();
    }

    @Override
    public int size() {
        return _size;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void addFirst(E e) {
        if (_front == 0) {
            Resize(_elements.length * 2);
        }

        _front--;
        _size++;
        _elements[_front] = e;
    }

    @Override
    public void addLast(E e) {
        if (_rear == _elements.length - 1) {
            Resize(_elements.length * 2);
        }

        _rear++;
        _size++;
        _elements[_rear] = e;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (_size == 0) {
            return null;
        }

        return _elements[_front];
    }

    @Override
    public E getLast() {
        if (_size == 0) {
            return null;
        }

        return _elements[_rear];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (_size == 0) {
            return null;
        }

        _front++;
        _size--;
        return _elements[_front - 1];
    }

    @Override
    public E pollLast() {
        if (_size == 0) {
            return null;
        }

        _rear--;
        _size--;
        return _elements[_rear + 1];
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
