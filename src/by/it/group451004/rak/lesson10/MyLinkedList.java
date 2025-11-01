package by.it.group451004.rak.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    int size = 0;
    Node first = null;
    Node last = null;
    private class Node {
        public Node(E value, Node next, Node prev){
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
        public E value;
        public Node prev;
        public Node next;
    }

    public MyLinkedList(){}

    //ЗАДАНИЕ

    @Override
    public String toString() {
        if (first == null)
            return "[]";
        if (first == last)
            return "[" + first.value + "]";

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(first.value);

        Node current = first.next;
        while (current != null) {
            sb.append(", ");
            sb.append(current.value);
            current = current.next;
        }

        sb.append("]");
        return sb.toString();
    }

    @Override
    public void addFirst(E e) {
        if (first == null){
            first = new Node(e, null, null);
            last = first;
        } else if (first == last) {
            first = new Node(e, last, null);
            last.prev = first;
        } else {
            Node newFirst = new Node(e, first, null);
            first.prev = newFirst;
            first = newFirst;
        }
        size++;
    }

    @Override
    public void addLast(E e) {
        if (first == null){
            first = new Node(e, null, null);
            last = first;
        } else if (first == last) {
            last = new Node(e, null, first);
            first.next = last;
        } else {
            Node newLast = new Node(e, null, last);
            last.next = newLast;
            last = newLast;
        }
        size++;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean remove(Object o) {
        Node current = first;

        while (current != null){
            if (current.value == o){
                if (current.prev != null)
                    current.prev.next = current.next;
                else
                    first = current.next;

                if (current.next != null)
                    current.next.prev = current.prev;
                else
                    last = current.prev;

                size--;
                return true;
            }
            current = current.next;
        }

        return false;
    }

    public E remove(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);

        Node current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        if (current.prev != null)
            current.prev.next = current.next;
        else
            first = current.next;

        if (current.next != null)
            current.next.prev = current.prev;
        else
            last = current.prev;

        size--;
        return current.value;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getLast() {
        if (last == null)
            throw new NoSuchElementException();
        return last.value;
    }

    @Override
    public E getFirst() {
        if (first == null)
            throw new NoSuchElementException();
        return first.value;
    }

    @Override
    public E pollFirst() {
        if (first == null)
            return null;
        return remove(0);
    }

    @Override
    public E pollLast() {
        if (last == null)
            return null;
        return remove(size - 1);
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    //ЗАГЛУШКИ

    @Override
    public E remove() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
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
    public E peek() {
        return null;
    }

    @Override
    public E peekLast() {
        return null;
    }

    @Override
    public E peekFirst() {
        return null;
    }


    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public void push(E e) {

    }

    @Override
    public E pop() {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
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

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }
}
