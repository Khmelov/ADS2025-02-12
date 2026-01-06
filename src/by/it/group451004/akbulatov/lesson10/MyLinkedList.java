package by.it.group451004.akbulatov.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class MyLinkedList<E> implements Deque<E>
{
    private static class Node<E>
    {
        E item;
        Node<E> prev;
        Node<E> next;

        Node(E item, Node<E> prev, Node<E> next)
        {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public MyLinkedList()
    {
        head = tail = null;
        size = 0;
    }

    @Override
    public String toString()
    {
        if (isEmpty()) return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        Node<E> cur = head;
        while (cur != null)
        {
            sb.append(cur.item == null ? "null" : cur.item.toString());
            cur = cur.next;

            if (cur != null) sb.append(", ");
        }

        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E element)
    {
        addLast(element);
        return true;
    }

    public E remove(int index)
    {
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
        unlink(cur);
        return item;
    }

    @Override
    public boolean remove(Object o)
    {
        Node<E> cur = head;
        while (cur != null)
        {
            if (Objects.equals(o, cur.item))
            {
                unlink(cur);
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    private void unlink(Node<E> node) {
        Node<E> prev = node.prev;
        Node<E> next = node.next;

        if (prev == null) head = next;
        else prev.next = next;

        if (next == null) tail = prev;
        else next.prev = prev;

        node.prev = node.next = null;
        node.item = null;

        size--;
    }

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public void addFirst(E element)
    {
        Node<E> newNode = new Node<>(element, null, head);
        if (head == null)
            head = tail = newNode;
        else
        {
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    @Override
    public void addLast(E element)
    {
        Node<E> newNode = new Node<>(element, tail, null);
        if (tail == null)
            head = tail = newNode;
        else
        {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    @Override
    public E element()
    {
        return getFirst();
    }

    @Override
    public E getFirst()
    {
        if (isEmpty()) throw new NoSuchElementException();
        return head.item;
    }

    @Override
    public E getLast()
    {
        if (isEmpty()) throw new NoSuchElementException();
        return tail.item;
    }

    @Override
    public E poll()
    {
        return pollFirst();
    }

    @Override
    public E pollFirst()
    {
        if (isEmpty()) return null;
        E res = head.item;
        unlink(head);
        return res;
    }

    @Override
    public E pollLast()
    {
        if (isEmpty()) return null;
        E res = tail.item;
        unlink(tail);
        return res;
    }

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public boolean offer(E e) { return offerLast(e); }
    @Override public E remove() {
        E r = pollFirst();
        if (r == null) throw new NoSuchElementException();
        return r;
    }
    @Override public E removeFirst() { return remove(); }
    @Override public E removeLast() {
        E r = pollLast();
        if (r == null) throw new NoSuchElementException();
        return r;
    }

    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() {
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.next;
            cur.item = null;
            cur.prev = cur.next = null;
            cur = next;
        }
        head = tail = null;
        size = 0;
    }
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    @Override public E peek() { return isEmpty() ? null : head.item; }
    @Override public E peekFirst() { return peek(); }
    @Override public E peekLast() { return isEmpty() ? null : tail.item; }
    @Override public boolean removeFirstOccurrence(Object o) { return remove(o); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return remove(); }
    @Override public boolean isEmpty() { return size == 0; }
}

