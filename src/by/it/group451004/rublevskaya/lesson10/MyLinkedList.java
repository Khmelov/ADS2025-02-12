package by.it.group451004.rublevskaya.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {
    private Node<E> head;
    private Node<E> tail;
    private int currentSize;

    private static class Node<E> {
        E info;
        Node<E> prev;
        Node<E> next;

        Node(E info, Node<E> prev, Node<E> next) {
            this.info = info;
            this.prev = prev;
            this.next = next;
        }
    }

    @Override
    public String toString() {
        StringBuilder resultStr = new StringBuilder();
        resultStr.append("[");
        Node<E> node = head;
        while (node != null) {
            resultStr.append(node.info);
            if (node.next != null) {
                resultStr.append(", ");
            }
            node = node.next;
        }
        resultStr.append("]");
        return resultStr.toString();
    }

    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public boolean isEmpty() {
        return currentSize == 0;
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
    public E remove() {
        return null;
    }

    @Override
    public void addFirst(E e) {
        Node<E> node = new Node<>(e, null, head);
        if (head != null) {
            head.prev = node;
        }
        head = node;
        if (tail == null) {
            tail = node;
        }
        currentSize++;
    }

    @Override
    public void addLast(E e) {
        Node<E> node = new Node<>(e, tail, null);
        if (tail != null) {
            tail.next = node;
        }
        tail = node;
        if (head == null) {
            head = node;
        }
        currentSize++;
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
    public E element() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        return head.info;
    }

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
    public E getFirst() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        return head.info;
    }

    @Override
    public E getLast() {
        if (tail == null) {
            throw new NoSuchElementException();
        }
        return tail.info;
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
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (head == null) {
            return null;
        }
        E info = head.info;
        head = head.next;
        if (head != null) {
            head.prev = null;
        } else {
            tail = null;
        }
        currentSize--;
        return info;
    }

    @Override
    public E pollLast() {
        if (tail == null) {
            return null;
        }
        E info = tail.info;
        tail = tail.prev;
        if (tail != null) {
            tail.next = null;
        } else {
            head = null;
        }
        currentSize--;
        return info;
    }

    @Override
    public boolean remove(Object o) {
        Node<E> node = head;
        while (node != null) {
            if (node.info.equals(o)) {
                unlink(node);
                return true;
            }
            node = node.next;
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

    public E remove(int index) {
        if (index < 0 || index >= currentSize) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> node = head;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        E value = node.info;
        unlink(node);
        return value;
    }

    private void unlink(Node<E> node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
        currentSize--;
    }
}