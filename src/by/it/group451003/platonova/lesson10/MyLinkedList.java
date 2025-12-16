package by.it.group451003.platonova.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Collection;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        E item;
        Node<E> prev;
        Node<E> next;

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
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E e) {
        Node<E> newNode = new Node<>(e, null, head);
        if (head != null) head.prev = newNode;
        head = newNode;
        if (tail == null) tail = newNode;
        size++;
    }

    @Override
    public void addLast(E e) {
        Node<E> newNode = new Node<>(e, tail, null);
        if (tail != null) tail.next = newNode;
        tail = newNode;
        if (head == null) head = newNode;
        size++;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public boolean remove(Object obj) {
        Node<E> current = head;
        while (current != null) {
            if ((obj == null && current.item == null) ||
                    (obj != null && obj.equals(current.item))) {

                // remove node
                if (current.prev != null) current.prev.next = current.next;
                else head = current.next;

                if (current.next != null) current.next.prev = current.prev;
                else tail = current.prev;

                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }


    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return head.item;
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return tail.item;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E item = head.item;
        head = head.next;
        if (head != null) head.prev = null;
        else tail = null;
        size--;
        return item;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        E item = tail.item;
        tail = tail.prev;
        if (tail != null) tail.next = null;
        else head = null;
        size--;
        return item;
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

        E item = cur.item;

        if (cur.prev != null) cur.prev.next = cur.next;
        else head = cur.next;

        if (cur.next != null) cur.next.prev = cur.prev;
        else tail = cur.prev;

        size--;
        return item;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = head;
        while (cur != null) {
            sb.append(cur.item);
            cur = cur.next;
            if (cur != null) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // ============ Заглушки для Deque/Collection ============

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { addLast(e); return true; }
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E remove() { return pollFirst(); }
    @Override public E removeFirst() { E e = pollFirst(); if (e == null) throw new NoSuchElementException(); return e; }
    @Override public E removeLast() { E e = pollLast(); if (e == null) throw new NoSuchElementException(); return e; }
    @Override public E peek() { return size == 0 ? null : getFirst(); }
    @Override public E peekFirst() { return size == 0 ? null : getFirst(); }
    @Override public E peekLast() { return size == 0 ? null : getLast(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public void clear() { while (pollFirst() != null); }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return removeFirst(); }
    @Override public boolean isEmpty() { return size == 0; }

    @Override public boolean addAll(Collection<? extends E> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
    @Override public boolean containsAll(Collection<?> c) { return false; }

    @Override public Object[] toArray() { return new Object[0]; }

    @Override public <T> T[] toArray(T[] a) { return null; }

}

