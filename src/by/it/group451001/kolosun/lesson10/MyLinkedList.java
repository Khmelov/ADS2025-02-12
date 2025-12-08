package by.it.group451001.kolosun.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;


public class MyLinkedList<E> implements Deque<E> {
    int size = 0;

    private static class Node<E> {
        E data;
        Node<E> previous;
        Node<E> next;

        private Node(E data) {
            this.data = data;
            this.next = null;
            this.previous = null;
        }
    }

    Node<E> first;
    Node<E> last;

    public String toString() {
        StringBuilder res = new StringBuilder("[");

        for (Node<E> tmpNode = first; tmpNode != null; tmpNode = tmpNode.next) {
            res.append(tmpNode.data.toString()).append(", ");
        }

        if (first != null) {
            res.delete(res.length() - 2, res.length());
        }

        return res.append(']').toString();
    }

    @Override
    public void addFirst(E e) {
        Node<E> tmpNode = new Node<E>(e);
        if (first == null) {
            first = tmpNode;
            last = tmpNode;
        } else {
            tmpNode.next = first;
            first.previous = tmpNode;
            first = tmpNode;
        }
        size++;
    }

    @Override
    public void addLast(E e) {
        Node<E> tmpNode = new Node<E>(e);
        if (first == null) {
            first = tmpNode;
            last = tmpNode;
        } else {
            tmpNode.previous = last;
            last.next = tmpNode;
            last = tmpNode;
        }
        size++;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }


    @Override
    public E removeFirst() {
        Node<E> tmpNode = first;
        first = first.next;
        first.previous = null;
        size--;
        return tmpNode.data;
    }

    @Override
    public E removeLast() {
        Node<E> tmpNode = last;
        last = last.previous;
        last.next = null;
        size--;
        return tmpNode.data;
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    public E remove(int index) {
        Node<E> tmpNode = first;
        for (int i = 0; i < index && tmpNode != null; i++)
            tmpNode = tmpNode.next;
        if (tmpNode != null) {
            Node<E> tmpP = tmpNode.previous;
            Node<E> tmpN = tmpNode.next;
            if (tmpP != null)
                tmpP.next = tmpN;
            if (tmpN != null)
                tmpN.previous = tmpP;
            size--;
            return tmpNode.data;
        }
        return null;
    }

    @Override
    public E element() {
        return getFirst();
    }


    @Override
    public E getFirst() {
        return first.data;
    }

    @Override
    public E getLast() {
        return last.data;
    }


    @Override
    public E pollFirst() {
        return removeFirst();
    }

    @Override
    public E pollLast() {
        return removeLast();
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean remove(Object o) {
        Node<E> tmpNode = first;
        while (tmpNode != null && !o.equals(tmpNode.data))
            tmpNode = tmpNode.next;
        if (tmpNode != null) {
            Node<E> tmpP = tmpNode.previous;
            Node<E> tmpN = tmpNode.next;
            if (tmpP != null)
                tmpP.next = tmpN;
            if (tmpN != null)
                tmpN.previous = tmpP;
            size--;
            return true;
        }
        return false;
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
    public boolean offer(E e) {
        return false;
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
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

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
}
