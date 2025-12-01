package by.it.group451001.drzhevetskiy.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(E item, Node<E> prev, Node<E> next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> head = null;
    private Node<E> tail = null;
    private int size = 0;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = head;
        while (cur != null) {
            sb.append(cur.item);
            if (cur.next != null) sb.append(", ");
            cur = cur.next;
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
        Node<E> newNode = new Node<>(element, null, head);

        if (head == null) {
            // список пуст
            head = tail = newNode;
        } else {
            head.prev = newNode;
            head = newNode;
        }

        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> newNode = new Node<>(element, tail, null);

        if (tail == null) {
            // список пуст
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }

        size++;
    }

    @Override
    public E getFirst() {
        if (head == null) throw new NoSuchElementException();
        return head.item;
    }

    @Override
    public E getLast() {
        if (tail == null) throw new NoSuchElementException();
        return tail.item;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E pollFirst() {
        if (head == null) return null;

        E value = head.item;

        head = head.next;

        if (head == null) {
            tail = null; // список стал пуст
        } else {
            head.prev = null;
        }

        size--;
        return value;
    }

    @Override
    public E pollLast() {
        if (tail == null) return null;

        E value = tail.item;

        tail = tail.prev;

        if (tail == null) {
            head = null; // список стал пуст
        } else {
            tail.next = null;
        }

        size--;
        return value;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

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

        E value = cur.item;

        Node<E> p = cur.prev;
        Node<E> n = cur.next;

        if (p == null) {
            head = n;
        } else {
            p.next = n;
        }

        if (n == null) {
            tail = p;
        } else {
            n.prev = p;
        }

        size--;
        return value;
    }

    @Override
    public boolean remove(Object o) {
        Node<E> cur = head;

        while (cur != null) {
            if ((o == null && cur.item == null) ||
                    (o != null && o.equals(cur.item))) {

                Node<E> p = cur.prev;
                Node<E> n = cur.next;

                if (p == null) head = n;
                else p.next = n;

                if (n == null) tail = p;
                else n.prev = p;

                size--;
                return true;
            }
            cur = cur.next;
        }
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

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { return add(e); }
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E remove() { return removeFirst(); }
    @Override public E removeFirst() { E r = pollFirst(); if (r == null) throw new NoSuchElementException(); return r; }
    @Override public E removeLast() { E r = pollLast(); if (r == null) throw new NoSuchElementException(); return r; }
    @Override public E peek() { return peekFirst(); }

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

    @Override public E peekFirst() { return head == null ? null : head.item; }
    @Override public E peekLast() { return tail == null ? null : tail.item; }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return removeFirst(); }

}

