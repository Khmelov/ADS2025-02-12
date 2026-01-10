package by.it.group451003.kuzhik.lesson10;

import java.util.Deque;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {
    private static class Node<E> {
        E data;
        Node<E> prev;
        Node<E> next;

        Node(E data) {
            this.data = data;
            this.prev = null;
            this.next = null;
        }

        Node(E data, Node<E> prev, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }
        Node<E> newNode = new Node<>(element);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    @Override
    public void addLast(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }
        Node<E> newNode = new Node<>(element);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<E> current = getNode(index);
        return unlink(current);
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            throw new NullPointerException("Null elements are not allowed");
        }
        Node<E> current = head;
        while (current != null) {
            if (o.equals(current.data)) {
                unlink(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    private E unlink(Node<E> node) {
        E data = node.data;
        if (node.prev == null) {
            // Удаляем голову
            head = node.next;
        } else {
            node.prev.next = node.next;
        }
        if (node.next == null) {
            // Удаляем хвост
            tail = node.prev;
        } else {
            node.next.prev = node.prev;
        }
        node.data = null;
        node.prev = null;
        node.next = null;
        size--;
        return data;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        return head.data;
    }

    @Override
    public E getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        return tail.data;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (isEmpty()) {
            return null;
        }
        return unlink(head);
    }

    @Override
    public E pollLast() {
        if (isEmpty()) {
            return null;
        }
        return unlink(tail);
    }

    @Override
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private Node<E> getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<E> current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean offer(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean offerFirst(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean offerLast(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E removeFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E removeLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E peek() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E peekFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E peekLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void push(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E pop() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}