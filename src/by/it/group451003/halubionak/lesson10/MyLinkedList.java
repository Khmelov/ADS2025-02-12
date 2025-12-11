package by.it.group451003.halubionak.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> prev;

        Node(E v) {
            value = v;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size = 0;


    @Override
    public void addFirst(E e) {
        Node<E> n = new Node<>(e);
        if (head == null) {
            head = tail = n;
        } else {
            n.next = head;
            head.prev = n;
            head = n;
        }
        size++;
    }

    @Override
    public void addLast(E e) {
        Node<E> n = new Node<>(e);
        if (tail == null) {
            head = tail = n;
        } else {
            n.prev = tail;
            tail.next = n;
            tail = n;
        }
        size++;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    // remove(int index)
    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();

        Node<E> cur;
        if (index < size / 2) {
            cur = head;
            for (int i = 0; i < index; i++) cur = cur.next;
        } else {
            cur = tail;
            for (int i = size - 1; i > index; i--) cur = cur.prev;
        }

        E val = cur.value;
        unlink(cur);
        return val;
    }

    @Override
    public boolean remove(Object o) {
        Node<E> cur = head;
        while (cur != null) {
            if ((o == null && cur.value == null) ||
                    (o != null && o.equals(cur.value))) {
                unlink(cur);
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    private void unlink(Node<E> n) {
        Node<E> p = n.prev;
        Node<E> nx = n.next;

        if (p == null) head = nx;
        else p.next = nx;

        if (nx == null) tail = p;
        else nx.prev = p;

        size--;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return head.value;
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return tail.value;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E v = head.value;
        unlink(head);
        return v;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        E v = tail.value;
        unlink(tail);
        return v;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = head;
        boolean first = true;
        while (cur != null) {
            if (!first) sb.append(", ");
            first = false;
            sb.append(cur.value);
            cur = cur.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException();
    }

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
    public void push(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E pop() {
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
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
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

