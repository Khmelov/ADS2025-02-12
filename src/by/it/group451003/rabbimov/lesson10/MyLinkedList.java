package by.it.group451003.rabbimov.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyLinkedList<E> implements Deque<E> {
    class MyLinkedListNode<E> {
        public E Data;
        public MyLinkedListNode<E> Previous;
        public MyLinkedListNode<E> Next;

        public MyLinkedListNode(E data) {
            Data = data;
        }
    }

    MyLinkedListNode<E> _head;
    MyLinkedListNode<E> _tail;
    int _size;

    MyLinkedList() {
        _head = null;
        _tail = null;
        _size = 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        MyLinkedListNode<E> temp = _head;
        for (int i = 0; i < _size; i++) {
            sb.append(temp.Data);
            if (i < _size - 1) {
                sb.append(", ");
            }
            temp = temp.Next;
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    public E remove(int index) {
        if (index < 0 || index >= _size) {
            return null;
        }

        MyLinkedListNode<E> temp = _head;
        for (int i = 0; i < index; i++) {
            temp = temp.Next;
        }
        E e = temp.Data;

        if (temp.Previous != null) {
            temp.Previous.Next = temp.Next;
        } else {
            _head = temp.Next;
        }

        if (temp.Next != null) {
            temp.Next.Previous = temp.Previous;
        } else {
            _tail = temp.Previous;
        }

        _size--;

        return e;
    }

    @Override
    public boolean remove(Object o) {
        MyLinkedListNode<E> temp = _head;
        int index = 0;
        while (temp != null) {
            if (temp.Data.equals(o)) {
                remove(index);
                return true;
            }
            index++;
            temp = temp.Next;
        }
        return false;
    }

    @Override
    public int size() {
        return _size;
    }

    @Override
    public void addFirst(E e) {
        MyLinkedListNode<E> node = new MyLinkedListNode<>(e);
        if (_head != null) {
            node.Next = _head;
            _head.Previous = node;
        }
        _head = node;

        if (_tail == null) {
            _tail = node;
        }

        _size++;
    }

    @Override
    public void addLast(E e) {
        MyLinkedListNode<E> node = new MyLinkedListNode<>(e);
        if (_tail != null) {
            _tail.Next = node;
            node.Previous = _tail;
        }
        _tail = node;

        if (_head == null) {
            _head = node;
        }

        _size++;
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
        return _head.Data;
    }

    @Override
    public E getLast() {
        if (_size == 0) {
            return null;
        }
        return _tail.Data;
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
        E e = _head.Data;
        _head = _head.Next;

        if (_head != null) {
            _head.Previous = null;
        }
        else {
            _tail = null;
        }

        _size--;
        return e;
    }

    @Override
    public E pollLast() {
        if (_size == 0) {
            return null;
        }
        E e = _tail.Data;
        _tail = _tail.Previous;

        if (_tail != null) {
            _tail.Next = null;
        }
        else {
            _head = null;
        }

        _size--;
        return e;
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
