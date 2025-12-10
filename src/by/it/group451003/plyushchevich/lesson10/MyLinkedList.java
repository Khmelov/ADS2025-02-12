package by.it.group451003.plyushchevich.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        E value;
        Node<E> prev;
        Node<E> next;
        Node(E v, Node<E> p, Node<E> n) { value = v; prev = p; next = n; }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public MyLinkedList() {
        head = tail = null;
        size = 0;
    }


    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node<E> cur = head;
        while (cur != null) {
            sb.append(cur.value);
            cur = cur.next;
            if (cur != null) sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    // remove by index, возвращает удалённый элемент
    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("index: " + index);
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

    // remove by value, возвращает true если найдено и удалено
    @Override
    public boolean remove(Object element) {
        for (Node<E> cur = head; cur != null; cur = cur.next) {
            if (element == null ? cur.value == null : element.equals(cur.value)) {
                unlink(cur);
                return true;
            }
        }
        return false;
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E element) {
        if (element == null) throw new NullPointerException("null not supported");
        Node<E> node = new Node<>(element, null, head);
        if (head == null) {
            head = tail = node;
        } else {
            head.prev = node;
            head = node;
        }
        size++;
    }

    @Override
    public void addLast(E element) {
        if (element == null) throw new NullPointerException("null not supported");
        Node<E> node = new Node<>(element, tail, null);
        if (tail == null) {
            head = tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
    }

    @Override
    public E element() {
        E e = peekFirst();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override
    public E getFirst() {
        E e = peekFirst();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override
    public E getLast() {
        E e = peekLast();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (head == null) return null;
        E val = head.value;
        unlink(head);
        return val;
    }

    @Override
    public E pollLast() {
        if (tail == null) return null;
        E val = tail.value;
        unlink(tail);
        return val;
    }

    private void unlink(Node<E> node) {
        Node<E> p = node.prev;
        Node<E> n = node.next;

        if (p == null) {
            head = n;
        } else {
            p.next = n;
            node.prev = null;
        }

        if (n == null) {
            tail = p;
        } else {
            n.prev = p;
            node.next = null;
        }

        node.value = null; // help GC
        size--;
        if (size == 0) { head = tail = null; }
    }

    public E peekFirst() {
        return head == null ? null : head.value;
    }

    public E peekLast() {
        return tail == null ? null : tail.value;
    }














    //  Методы Deque/Collection, помеченные как unsupported
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e)  { addLast(e); return true; }
    @Override public boolean offer(E e)      { return add(e); }
    @Override public E peek()                { return peekFirst(); }
    @Override public E remove()              { E e = poll(); if (e==null) throw new NoSuchElementException(); return e; }
    @Override public E removeFirst()         { E e = pollFirst(); if (e==null) throw new NoSuchElementException(); return e; }
    @Override public E removeLast()          { E e = pollLast(); if (e==null) throw new NoSuchElementException(); return e; }

    // Unsupported
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public void push(E e) { throw new UnsupportedOperationException(); }
    @Override public E pop() { throw new UnsupportedOperationException(); }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }

    @Override public boolean isEmpty() { return size == 0; }
    @Override public void clear() {
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.next;
            cur.prev = cur.next = null;
            cur.value = null;
            cur = next;
        }
        head = tail = null;
        size = 0;
    }
    @Override public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> cur = head;
            @Override
            public boolean hasNext() { return cur != null; }
            @Override
            public E next() {
                if (cur == null) throw new NoSuchElementException();
                E v = cur.value;
                cur = cur.next;
                return v;
            }
        };
    }

    @Override public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private Node<E> cur = tail;
            @Override
            public boolean hasNext() { return cur != null; }
            @Override
            public E next() {
                if (cur == null) throw new NoSuchElementException();
                E v = cur.value;
                cur = cur.prev;
                return v;
            }
        };
    }

}

