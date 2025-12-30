package by.it.group451004.matyrka.lesson10;

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

    private Node<E> first;
    private Node<E> last;
    private int size = 0;

    // ---------------- BASIC HELPERS ----------------

    private Node<E> node(int index) {
        if (index < (size >> 1)) {
            Node<E> x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            Node<E> x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }

    private void linkFirst(E e) {
        Node<E> f = first;
        Node<E> newNode = new Node<>(e, null, f);
        first = newNode;
        if (f == null)
            last = newNode;
        else
            f.prev = newNode;
        size++;
    }

    private void linkLast(E e) {
        Node<E> l = last;
        Node<E> newNode = new Node<>(e, l, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        size++;
    }

    private E unlink(Node<E> x) {
        E element = x.item;
        Node<E> p = x.prev;
        Node<E> n = x.next;

        if (p == null)
            first = n;
        else
            p.next = n;

        if (n == null)
            last = p;
        else
            n.prev = p;

        size--;
        return element;
    }

    // =============== REQUIRED METHODS ===============

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void addFirst(E e) {
        linkFirst(e);
    }

    @Override
    public void addLast(E e) {
        linkLast(e);
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (first == null) throw new NoSuchElementException();
        return first.item;
    }

    @Override
    public E getLast() {
        if (last == null) throw new NoSuchElementException();
        return last.item;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (first == null) return null;
        return unlink(first);
    }

    @Override
    public E pollLast() {
        if (last == null) return null;
        return unlink(last);
    }

    @Override
    public boolean remove(Object o) {
        Node<E> x = first;
        while (x != null) {
            if ((o == null && x.item == null) || (o != null && o.equals(x.item))) {
                unlink(x);
                return true;
            }
            x = x.next;
        }
        return false;
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E removeFirst() {
        if (first == null) throw new NoSuchElementException();
        return unlink(first);
    }

    @Override
    public E removeLast() {
        if (last == null) throw new NoSuchElementException();
        return unlink(last);
    }

    @Override
    public E remove(int index) {
        Node<E> x = node(index);
        return unlink(x);
    }

    @Override
    public int size() {
        return size;
    }

    // ---------------- TO STRING ----------------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');

        Node<E> x = first;
        boolean firstPrint = true;

        while (x != null) {
            if (!firstPrint) sb.append(", ");
            sb.append(x.item);
            firstPrint = false;
            x = x.next;
        }
        sb.append(']');
        return sb.toString();
    }

    // ========== ITERATOR (MINIMAL, WORKING) ==========

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> cur = first;

            @Override
            public boolean hasNext() {
                return cur != null;
            }

            @Override
            public E next() {
                if (cur == null) throw new NoSuchElementException();
                E val = cur.item;
                cur = cur.next;
                return val;
            }
        };
    }

    // ======== UNUSED METHODS (THROW EXCEPTIONS) ========

    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return removeFirst(); }

    @Override public boolean offer(E e) { return add(e); }
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }

    @Override public E peek() { return (first == null ? null : first.item); }
    @Override public E peekFirst() { return peek(); }
    @Override public E peekLast() { return (last == null ? null : last.item); }

    @Override public boolean removeFirstOccurrence(Object o) { return remove(o); }
    @Override public boolean removeLastOccurrence(Object o) { return remove(o); }

    @Override public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException();
    }

    @Override public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;
        for (E e : this) arr[i++] = e;
        return arr;
    }

    @Override public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override public boolean contains(Object o) {
        Node<E> x = first;
        while (x != null) {
            if ((o == null && x.item == null) || (o != null && o.equals(x.item)))
                return true;
            x = x.next;
        }
        return false;
    }

    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() {
        first = last = null;
        size = 0;
    }
}
