package by.it.group451004.maximenko.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> first;
    private Node<E> last;
    private int size = 0;

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Node<E> x = first;
        while (x != null) {
            sb.append(x.item);
            if (x.next != null) sb.append(", ");
            x = x.next;
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        Node<E> f = first;
        Node<E> newNode = new Node<>(null, element, f);
        first = newNode;
        if (f == null)
            last = newNode;
        else
            f.prev = newNode;
        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> l = last;
        Node<E> newNode = new Node<>(l, element, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        size++;
    }

    @Override
    public int size() {
        return size;
    }

    public E remove(int index) {
        checkIndex(index);
        Node<E> target = node(index);
        E oldVal = target.item;
        unlink(target);
        return oldVal;
    }

    @Override
    public boolean remove(Object o) {
        for (Node<E> x = first; x != null; x = x.next) {
            if (o == null ? x.item == null : o.equals(x.item)) {
                unlink(x);
                return true;
            }
        }
        return false;
    }

    @Override
    public E element() {
        if (first == null)
            throw new NoSuchElementException();
        return first.item;
    }

    @Override
    public E getFirst() {
        if (first == null)
            throw new NoSuchElementException();
        return first.item;
    }

    @Override
    public E getLast() {
        if (last == null)
            throw new NoSuchElementException();
        return last.item;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (first == null) return null;
        return unlinkFirst();
    }

    @Override
    public E pollLast() {
        if (last == null) return null;
        return unlinkLast();
    }

    /////////////////////////////////////////////////////////////////////////
    //////                Вспомогательные методы                        ///////
    /////////////////////////////////////////////////////////////////////////

    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    private Node<E> node(int index) {
        Node<E> x;
        if (index < (size >> 1)) {
            x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
        } else {
            x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
        }
        return x;
    }

    private E unlinkFirst() {
        Node<E> f = first;
        E element = f.item;
        Node<E> next = f.next;
        f.item = null;
        f.next = null;
        first = next;
        if (next == null)
            last = null;
        else
            next.prev = null;
        size--;
        return element;
    }

    private E unlinkLast() {
        Node<E> l = last;
        E element = l.item;
        Node<E> prev = l.prev;
        l.item = null;
        l.prev = null;
        last = prev;
        if (prev == null)
            first = null;
        else
            prev.next = null;
        size--;
        return element;
    }

    private void unlink(Node<E> x) {
        Node<E> prev = x.prev;
        Node<E> next = x.next;

        if (prev == null)
            first = next;
        else
            prev.next = next;

        if (next == null)
            last = prev;
        else
            next.prev = prev;

        size--;
    }

    /////////////////////////////////////////////////////////////////////////
    //////        Остальные методы (для совместимости)                 ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        Node<E> x = first;
        while (x != null) {
            Node<E> next = x.next;
            x.item = null;
            x.next = null;
            x.prev = null;
            x = next;
        }
        first = last = null;
        size = 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> current = first;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null)
                    throw new NoSuchElementException();
                E val = current.item;
                current = current.next;
                return val;
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            Node<E> current = last;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null)
                    throw new NoSuchElementException();
                E val = current.item;
                current = current.prev;
                return val;
            }
        };
    }

    /////////////////////////////////////////////////////////////////////////
    //////          Реализация offer*, peek*, push/pop                ///////
    /////////////////////////////////////////////////////////////////////////

    @Override public boolean offer(E e) { addLast(e); return true; }
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }

    @Override public E peek() { return peekFirst(); }
    @Override public E peekFirst() { return (first == null) ? null : first.item; }
    @Override public E peekLast() { return (last == null) ? null : last.item; }

    @Override public E remove() { E e = poll(); if (e == null) throw new NoSuchElementException(); return e; }
    @Override public E removeFirst() { E e = pollFirst(); if (e == null) throw new NoSuchElementException(); return e; }
    @Override public E removeLast() { E e = pollLast(); if (e == null) throw new NoSuchElementException(); return e; }

    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return removeFirst(); }

    /////////////////////////////////////////////////////////////////////////
    //////          Необязательные методы-заглушки                    ///////
    /////////////////////////////////////////////////////////////////////////

    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
}
