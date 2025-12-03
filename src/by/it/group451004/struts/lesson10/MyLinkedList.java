package by.it.group451004.struts.lesson10;

import java.util.*;
import java.util.function.Consumer;

public class MyLinkedList<E> implements Deque<E> {
    public static class ListItem<E> {
        public E item;
        public ListItem<E> next;
        public ListItem<E> previous;

        public ListItem(E item) {
            this.item = item;
        }
    }

    private ListItem<E> head;
    private ListItem<E> tail;
    private int size;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        iterator().forEachRemaining(e -> {
            sb.append(e).append(", ");
        });
        sb.replace(sb.length() - 2, sb.length(), "]");

        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        ListItem<E> item = new ListItem<>(e);
        if (tail != null) {
            tail.next = item;
            item.previous = tail;
        }
        if (head == null)
            head = item;
        tail = item;

        size++;

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

    public E remove(int index) {
        Iterator<E> iterator = iterator();
        E item = head.item;
        for (int i = 0; i < index; i++) {
            item = iterator.next();
        }

        iterator.remove();
        return item;
    }

    @Override
    public boolean remove(Object o) {
        Iterator<E> iterator = iterator();
        E item = head.item;
        while (!o.equals(item) && iterator.hasNext()) {
            item = iterator.next();
        }

        if (o.equals(item))
            iterator.remove();

        return o.equals(item);
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
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void addFirst(E e) {
        ListItem<E> item = new ListItem<>(e);
        if (head != null) {
            head.previous = item;
            item.next = head;
        }
        if (tail == null)
            tail = item;
        head = item;

        size++;
    }

    @Override
    public void addLast(E e) {
        this.add(e);
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
        return head.item;
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
        return head.item;
    }

    @Override
    public E getLast() {
        return tail.item;
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
        if (head == null)
            return null;

        E item = head.item;
        head = head.next;
        if (head != null)
            head.previous = null;
        size--;

        return item;
    }

    @Override
    public E pollFirst() {
        return this.poll();
    }

    @Override
    public E pollLast() {
        if (tail == null)
            return null;

        E item = tail.item;
        tail = tail.previous;
        if (tail != null)
            tail.next = null;
        size--;

        return item;
    }

    @Override
    public Iterator<E> iterator() {
        return this.createIterator();
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

    private Iterator<E> createIterator() {
        return new Iterator<E>() {
            private ListItem<E> current = head;

            @Override
            public boolean hasNext() {
                return current.next != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    current = null;
                    return null;
                }
                current = current.next;
                return current.item;
            }

            @Override
            public void remove() {
                current.item = null;
                if (current.previous != null)
                    current.previous.next = current.next;
                else
                    head = current.next;

                if (current.next != null)
                    current.next.previous = current.previous;
                else
                    tail = current.previous;

                size--;
            }

            @Override
            public void forEachRemaining(Consumer<? super E> action) {
                while (current != null) {
                    action.accept(current.item);
                    next();
                }
            }
        };
    }
}
