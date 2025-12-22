package by.it.group451001.volynets.lesson10;

import java.util.Deque;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {
    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> prev;
        Node(E value) { this.value = value; }
        Node(E value, Node<E> prev, Node<E> next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> first;
    private Node<E> last;
    private int size;

    public MyLinkedList() {
        first = null;
        last = null;
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = first;
        while (cur != null) {
            sb.append(cur.value);
            if (cur.next != null) sb.append(", ");
            cur = cur.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    public E remove(int index) {
        checkIndex(index);
        return unlink(node(index));
    }

    @Override
    public boolean remove(Object o) {
        for (Node<E> cur = first; cur != null; cur = cur.next) {
            if (eq(o, cur.value)) {
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
    public void addFirst(E e) {
        Node<E> n = new Node<>(e);
        Node<E> f = first;
        n.next = f;
        first = n;
        if (f == null) {
            last = n;
        } else {
            f.prev = n;
        }
        size++;
    }

    @Override
    public void addLast(E e) {
        Node<E> l = last;
        Node<E> n = new Node<>(e, l, null);
        last = n;
        if (l == null) {
            first = n;
        } else {
            l.next = n;
        }
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (first == null) throw new NoSuchElementException();
        return first.value;
    }

    @Override
    public E getLast() {
        if (last == null) throw new NoSuchElementException();
        return last.value;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        return (first == null) ? null : unlink(first);
    }

    @Override
    public E pollLast() {
        return (last == null) ? null : unlink(last);
    }

    private Node<E> node(int index) {
        if (index < (size >> 1)) {
            Node<E> x = first;
            for (int i = 0; i < index; i++) x = x.next;
            return x;
        } else {
            Node<E> x = last;
            for (int i = size - 1; i > index; i--) x = x.prev;
            return x;
        }
    }

    private E unlink(Node<E> n) {
        E val = n.value;
        Node<E> p = n.prev;
        Node<E> q = n.next;

        if (p == null) {
            first = q;
        } else {
            p.next = q;
            n.prev = null;
        }

        if (q == null) {
            last = p;
        } else {
            q.prev = p;
            n.next = null;
        }

        n.value = null;
        size--;
        return val;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
    }

    private boolean eq(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E remove() {
        if (first == null) throw new NoSuchElementException();
        return pollFirst();
    }

    @Override
    public E removeFirst() {
        return remove();
    }

    @Override
    public E removeLast() {
        if (last == null) throw new NoSuchElementException();
        return pollLast();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public E peekFirst() {
        return (first == null) ? null : first.value;
    }

    @Override
    public E peekLast() {
        return (last == null) ? null : last.value;
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean contains(Object o) {
        for (Node<E> cur = first; cur != null; cur = cur.next) {
            if (eq(o, cur.value)) return true;
        }
        return false;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        for (Node<E> cur = last; cur != null; cur = cur.prev) {
            if (eq(o, cur.value)) {
                unlink(cur);
                return true;
            }
        }
        return false;
    }

    @Override public void clear() { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
}
