package by.it.group451001.puzik.lesson10;

import java.util.*;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        E item;
        Node<E> prev;
        Node<E> next;
        Node(E item) { this.item = item; }
    }

    private Node<E> first;
    private Node<E> last;
    private int size;

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append('[');
        Node<E> x = first;
        int i = 0;
        while (x != null) {
            if (i++ > 0) out.append(", ");
            out.append(x.item);
            x = x.next;
        }
        out.append(']');
        return out.toString();
    }

    @Override
    public boolean add(E e) { addLast(e); return true; }

    @Override
    public void addFirst(E e) {
        Node<E> f = first;
        Node<E> newNode = new Node<>(e);
        newNode.next = f;
        first = newNode;
        if (f == null) last = newNode; else f.prev = newNode;
        size++;
    }

    @Override
    public void addLast(E e) {
        Node<E> l = last;
        Node<E> newNode = new Node<>(e);
        newNode.prev = l;
        last = newNode;
        if (l == null) first = newNode; else l.next = newNode;
        size++;
    }

    @Override
    public E element() { return getFirst(); }

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
    public E poll() { return pollFirst(); }

    @Override
    public E pollFirst() {
        if (first == null) return null;
        E item = first.item;
        Node<E> n = first.next;
        first.item = null;
        first.next = null;
        first = n;
        if (n == null) last = null; else n.prev = null;
        size--;
        return item;
    }

    @Override
    public E pollLast() {
        if (last == null) return null;
        E item = last.item;
        Node<E> p = last.prev;
        last.item = null;
        last.prev = null;
        last = p;
        if (p == null) first = null; else p.next = null;
        size--;
        return item;
    }

    // Additional for Level B in test file
    public E remove(int index) {
        checkIndex(index);
        return unlink(node(index));
    }

    public boolean remove(Object o) {
        for (Node<E> x = first; x != null; x = x.next) {
            if (Objects.equals(x.item, o)) { unlink(x); return true; }
        }
        return false;
    }

    public int size() { return size; }

    // helpers
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

    private E unlink(Node<E> x) {
        E item = x.item;
        Node<E> p = x.prev;
        Node<E> n = x.next;
        if (p == null) first = n; else { p.next = n; x.prev = null; }
        if (n == null) last = p; else { n.prev = p; x.next = null; }
        x.item = null;
        size--;
        return item;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
    }

    // ---- Boilerplate unsupported/unused ----
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E removeFirst() { E v = pollFirst(); if (v==null) throw new NoSuchElementException(); return v; }
    @Override public E removeLast() { E v = pollLast(); if (v==null) throw new NoSuchElementException(); return v; }
    @Override public E peekFirst() { return first==null? null : first.item; }
    @Override public E peekLast() { return last==null? null : last.item; }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { return add(e); }
    @Override public E remove() { E v = poll(); if (v==null) throw new NoSuchElementException(); return v; }
    @Override public E peek() { return first==null? null : first.item; }
    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return removeFirst(); }
    @Override public boolean contains(Object o) { for (Node<E> x=first; x!=null; x=x.next) if (Objects.equals(x.item,o)) return true; return false; }
    @Override public boolean containsAll(Collection<?> c) { for (Object o:c) if (!contains(o)) return false; return true; }
    @Override public boolean addAll(Collection<? extends E> c) { boolean ch=false; for(E e:c){ addLast(e); ch=true;} return ch; }
    @Override public void clear() { while (pollFirst()!=null){} }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { Object[] a=new Object[size]; int i=0; for(Node<E>x=first;x!=null;x=x.next) a[i++]=x.item; return a; }
    @Override public <T> T[] toArray(T[] a) { if(a.length<size) a=Arrays.copyOf(a,size); int i=0; for(Node<E>x=first;x!=null;x=x.next){ @SuppressWarnings("unchecked") T v=(T)x.item; a[i++]=v;} if(a.length>size) a[size]=null; return a; }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean isEmpty() { return size==0; }
}


