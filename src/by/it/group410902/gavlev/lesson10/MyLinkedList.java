package by.it.group410902.gavlev.lesson10;

import java.util.*;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        public E item;
        public Node<E> prev;
        public Node<E> next;

        public Node(E item) {
            this.item = item;
            this.prev = null;
            this.next = null;
        }

        public Node(E item, Node<E> prev, Node<E> next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> sentinel; //next - начало, prev - конец
    private int size;

    public MyLinkedList() {
        sentinel = new Node<E>(null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder answer = new StringBuilder();
        Iterator<E> iterator = iterator();
        answer.append("[");
        while (iterator.hasNext()) {
            answer.append(iterator.next()).append(", ");
        }
        if (size != 0) answer.delete(answer.length() - 2, answer.length());
        answer.append("]");
        return answer.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E el) {
        addFirst(el);
        return true;
    }

    @Override
    public void addFirst(E el) {
        Node<E> newStart = new Node<E>(el, sentinel, sentinel.next);
        sentinel.next.prev = newStart;
        sentinel.next = newStart;
        size++;
    }

    @Override
    public void addLast(E el) {
        Node<E> newEnd = new Node<E>(el, sentinel.prev, sentinel);
        sentinel.prev.next = newEnd;
        sentinel.prev = newEnd;
        size++;
    }

    @Override
    public boolean offer(E el) {
        return offerLast(el);
    }

    @Override
    public boolean offerFirst(E el) {
        addFirst(el);
        return true;
    }

    @Override
    public boolean offerLast(E el) {
        addLast(el);
        return true;
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E removeFirst() {
        if (size == 0) throw new NoSuchElementException();
        E head = sentinel.next.item;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        size--;
        return head;
    }

    @Override
    public E removeLast() {
        if (size == 0) throw new NoSuchElementException();
        E tail = sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        size--;
        return tail;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        try {
            return removeFirst();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public E pollLast() {
        try {
            return removeLast();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return sentinel.next.item;
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return sentinel.prev.item;
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public E peekFirst() {
        try {
            return getFirst();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public E peekLast() {
        try {
            return getLast();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> temp = sentinel.next;
            Node<E> lastReturned = null;
            @Override
            public boolean hasNext() {
                return temp != sentinel;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                lastReturned = temp;
                temp = temp.next;
                return lastReturned.item;
            }

            @Override
            public void remove() {
                if (lastReturned == null) throw new IllegalStateException();
                lastReturned.prev.next = lastReturned.next;
                lastReturned.next.prev = lastReturned.prev;
                size--;
                lastReturned = null;
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            Node<E> temp = sentinel.prev;
            @Override
            public boolean hasNext() {
                return temp != sentinel;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                E item = temp.item;
                temp = temp.prev;
                return item;
            }
        };
    }

    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Iterator<E> iterator = iterator();
        E removed = null;
        for (int i = 0; i <= index; i++) {
            removed = iterator.next();
        }
        iterator.remove();
        return removed;
    }

    @Override
    public boolean remove(Object o) {
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (Objects.equals(it.next(), o)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
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
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
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
