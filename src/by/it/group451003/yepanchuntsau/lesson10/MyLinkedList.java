package by.it.group451003.yepanchuntsau.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    private static final class Node<E> {
        E item;
        Node<E> prev, next;
        Node(E item, Node<E> prev, Node<E> next) {
            this.item = item; this.prev = prev; this.next = next;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    private void linkFirst(E e) {
        Node<E> h = head;
        Node<E> n = new Node<>(e, null, h);
        head = n;
        if (h == null) tail = n; else h.prev = n;
        size++;
    }

    private void linkLast(E e) {
        Node<E> t = tail;
        Node<E> n = new Node<>(e, t, null);
        tail = n;
        if (t == null) head = n; else t.next = n;
        size++;
    }

    private E unlinkFirst() {
        Node<E> h = head;
        if (h == null) return null;
        E val = h.item;
        Node<E> next = h.next;
        h.item = null; h.next = null;
        head = next;
        if (next == null) tail = null; else next.prev = null;
        size--;
        return val;
    }

    private E unlinkLast() {
        Node<E> t = tail;
        if (t == null) return null;
        E val = t.item;
        Node<E> prev = t.prev;
        t.item = null; t.prev = null;
        tail = prev;
        if (prev == null) head = null; else prev.next = null;
        size--;
        return val;
    }

    private Node<E> nodeAt(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
        if (index < (size >> 1)) {
            Node<E> x = head;
            for (int i = 0; i < index; i++) x = x.next;
            return x;
        } else {
            Node<E> x = tail;
            for (int i = size - 1; i > index; i--) x = x.prev;
            return x;
        }
    }

    private E unlink(Node<E> x) {
        E val = x.item;
        Node<E> p = x.prev, n = x.next;
        if (p == null) head = n; else { p.next = n; x.prev = null; }
        if (n == null) tail = p; else { n.prev = p; x.next = null; }
        x.item = null;
        size--;
        return val;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node<E> x = head;
        while (x != null) {
            sb.append(x.item);
            x = x.next;
            if (x != null) sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean add(E element) { addLast(element); return true; }

    public E remove(int index) { return unlink(nodeAt(index)); }

    @Override
    public boolean remove(Object o) {
        for (Node<E> x = head; x != null; x = x.next) {
            if (o == null ? x.item == null : o.equals(x.item)) {
                unlink(x);
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() { return size; }

    @Override
    public void addFirst(E element) { linkFirst(element); }

    @Override
    public void addLast(E element) { linkLast(element); }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return head.item;
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
    public E poll() { return pollFirst(); }

    @Override
    public E pollFirst() { return unlinkFirst(); }

    @Override
    public E pollLast() { return unlinkLast(); }


    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e)  { addLast(e);  return true; }
    @Override public boolean offer(E e)      { return add(e); }

    @Override public E removeFirst() { E x = pollFirst(); if (x==null) throw new NoSuchElementException(); return x; }
    @Override public E removeLast()  { E x = pollLast();  if (x==null) throw new NoSuchElementException(); return x; }
    @Override public E remove()      { E x = poll();      if (x==null) throw new NoSuchElementException(); return x; }

    @Override public E peekFirst() { return head == null ? null : head.item; }
    @Override public E peekLast()  { return tail == null ? null : tail.item; }
    @Override public E peek()      { return peekFirst(); }

    @Override public void push(E e) { addFirst(e); }
    @Override public E pop()        { return removeFirst(); }

    @Override
    public boolean removeFirstOccurrence(Object o) { return remove(o); }

    @Override
    public boolean removeLastOccurrence(Object o) {
        for (Node<E> x = tail; x != null; x = x.prev) {
            if (o == null ? x.item == null : o.equals(x.item)) {
                unlink(x);
                return true;
            }
        }
        return false;
    }

    @Override public boolean isEmpty() { return size == 0; }

    @Override
    public boolean contains(Object o) {
        for (Node<E> x = head; x != null; x = x.next)
            if (o == null ? x.item == null : o.equals(x.item)) return true;
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> cur = head, last;
            @Override public boolean hasNext() { return cur != null; }
            @Override public E next() { last = cur; cur = cur.next; return last.item; }
            @Override public void remove() { if (last==null) throw new IllegalStateException(); MyLinkedList.this.remove(last.item); last = null; }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            Node<E> cur = tail, last;
            @Override public boolean hasNext() { return cur != null; }
            @Override public E next() { last = cur; cur = cur.prev; return last.item; }
            @Override public void remove() { if (last==null) throw new IllegalStateException(); MyLinkedList.this.remove(last.item); last = null; }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] a = new Object[size];
        int i = 0;
        for (Node<E> x = head; x != null; x = x.next) a[i++] = x.item;
        return a;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (Node<E> x = head; x != null; x = x.next) a[i++] = (T) x.item;
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) { boolean ch=false; for (E e: c){ addLast(e); ch=true; } return ch; }

    @Override
    public boolean containsAll(Collection<?> c) { for (Object x: c) if (!contains(x)) return false; return true; }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean ch=false;
        for (Node<E> x=head; x!=null; ) {
            Node<E> next = x.next;
            if (c.contains(x.item)) { unlink(x); ch=true; }
            x = next;
        }
        return ch;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean ch=false;
        for (Node<E> x=head; x!=null; ) {
            Node<E> next = x.next;
            if (!c.contains(x.item)) { unlink(x); ch=true; }
            x = next;
        }
        return ch;
    }

    @Override
    public void clear() {
        for (Node<E> x=head; x!=null; ) {
            Node<E> next = x.next;
            x.item=null; x.prev=null; x.next=null;
            x = next;
        }
        head = tail = null;
        size = 0;
    }
}
