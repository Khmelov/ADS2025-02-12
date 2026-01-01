package by.it.group451002.karbanovich.lesson10;

import java.util.*;

public class MyLinkedList<E> implements Deque<E> {
    private Node<E> first = null;
    private Node<E> last = null;
    private int size = 0;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> f = first;
        while (f != null) {
            sb.append(f.item).append(", ");
            f = f.next;
        }
        if (sb.length() > 1) sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        return sb.toString();
    }

    public E remove(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException();

        Node<E> f = first;
        for (int i = 0; i < index; i++)
            f = f.next;

        removeNode(f);
        return f.item;
    }

    @Override
    public void addFirst(E e) {
        final Node<E> f = first;
        first = new Node<E>(e, null, first);
        if (f == null)
            last = first;
        else f.prev = first;
        size++;
    }

    @Override
    public void addLast(E e) {
        final Node<E> f = last;
        last = new Node<E>(e, f, null);
        if (f == null)
            first = last;
        else f.next = last;
        size++;
    }

    @Override
    public E pollFirst() {
        E f = first.item;
        removeNode(first);
        return f;
    }

    @Override
    public E pollLast() {
        E f = last.item;
        removeNode(last);
        return f;
    }

    @Override
    public E getFirst() { return first.item; }

    @Override
    public E getLast() { return last.item; }

    @Override
    public boolean remove(Object o) {
        Node<E> f = first;
        while (f != null) {
            if (Objects.equals(f.item, o))
            {
                removeNode(f);
                return true;
            }
            f = f.next;
        }
        return false;
    }

    public boolean removeNode(Node<E> f) {
        if (f == null) return false;

        if (f.prev != null)
            f.prev.next = f.next;
        else first = f.next;

        if (f.next != null)
            f.next.prev = f.prev;
        else last = f.prev;
        size--;
        return true;
    }

    @Override
    public E remove() {
        return null;
    }

    @Override
    public boolean offerFirst(E e) {
        return false;
    }

    @Override
    public boolean offerLast(E e) {
        return false;
    }

    @Override
    public E removeFirst() {
        return null;
    }

    @Override
    public E removeLast() {
        return null;
    }

    @Override
    public E peekFirst() {
        return null;
    }

    @Override
    public E peekLast() {
        return null;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E element() { return getFirst(); }

    @Override
    public E peek() {
        return null;
    }

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

    @Override
    public void push(E e) {

    }

    @Override
    public E pop() {
        return null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }

    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(E item, Node<E> prev, Node<E> next) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }
}