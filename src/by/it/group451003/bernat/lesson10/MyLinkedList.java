package by.it.group451003.bernat.lesson10;

import java.util.*;

public class MyLinkedList<E> implements Deque<E> {
    private class Node {
        E data;
        Node prev;
        Node next;

        Node(E data) {
            this.data = data;
        }
    }

    private Node first;
    private Node last;
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
        Node current = first;
        for (int i = 0; i < size; i++) {
            sb.append(current.data);
            if (i < size - 1) sb.append(", ");
            current = current.next;
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void addFirst(E e) {
        Node newNode = new Node(e);
        if (size == 0) {
            first = last = newNode;
        } else {
            newNode.next = first;
            first.prev = newNode;
            first = newNode;
        }
        size++;
    }

    @Override
    public void addLast(E e) {
        Node newNode = new Node(e);
        if (size == 0) {
            first = last = newNode;
        } else {
            newNode.prev = last;
            last.next = newNode;
            last = newNode;
        }
        size++;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node node = getNode(index);
        E data = node.data;
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            first = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            last = node.prev;
        }
        size--;
        if (size == 0) {
            first = last = null;
        }
        return data;
    }

    @Override
    public boolean remove(Object o) {
        Node current = first;
        while (current != null) {
            if (Objects.equals(o, current.data)) {
                if (current.prev != null) {
                    current.prev.next = current.next;
                } else {
                    first = current.next;
                }
                if (current.next != null) {
                    current.next.prev = current.prev;
                } else {
                    last = current.prev;
                }
                size--;
                if (size == 0) {
                    first = last = null;
                }
                return true;
            }
            current = current.next;
        }
        return false;
    }

    private Node getNode(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node current;
        if (index < size / 2) {
            current = first;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = last;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return first.data;
    }

    @Override
    public E getFirst() {
        return element();
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return last.data;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E data = first.data;
        first = first.next;
        size--;
        if (size == 0) {
            last = null;
        } else {
            first.prev = null;
        }
        return data;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        E data = last.data;
        last = last.prev;
        size--;
        if (size == 0) {
            first = null;
        } else {
            last.next = null;
        }
        return data;
    }

    // Необязательные методы интерфейса Deque
    @Override
    public boolean offer(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E peek() {
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
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
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
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> descendingIterator() {
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