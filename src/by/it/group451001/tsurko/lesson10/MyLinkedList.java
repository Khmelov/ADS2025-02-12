package by.it.group451001.tsurko.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

//toString() --
//add(E element) --
//remove(int) --
//remove(E element) --
//size() --
//
//addFirst(E element) --
//addLast(E element) --
//
//element() --
//getFirst() --
//getLast() --
//
//poll() --
//pollFirst()
//pollLast()

public class MyLinkedList<E> implements Deque<E> {
    int size;
    Node<E> first;
    Node<E> last;

    public static class Node<E>{
        E item;
        Node<E> next;
        Node<E> prev;

        public Node(Node<E> prev, E item, Node<E> next) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node<E> x = first;
        while (x != null) {
            sb.append(x.item);
            x = x.next;
            if (x != null)
                sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }


    @Override
    public void addFirst(E e) {
        final Node<E> f = first;
        Node<E> newNode = new Node<>(null, e, f);
        first = newNode;
        if (f == null) last = newNode;
        else f.prev = newNode;
        size++;
        //modCount не добавлял из AbstractList
    }

    @Override
    public void addLast(E e) {
        final Node<E> l = last;
        Node<E> newNode = new Node<>(l, e, null);
        last = newNode;
        if (first == null) first = newNode;
        else l.next = newNode;
        size++;
    }

    @Override
    public boolean offerFirst(E e) {
        return false;
    }

    public E remove(int index) {
        if ((index < 0) || (index >= size)) throw new IndexOutOfBoundsException();
        Node<E> x;
        if (index < size >> 1){
            x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
        }
        else{
            x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
        }
//        final Node<E> next = x.next;
//        final Node<E> prev = x.prev;
//        final E elem = x.item;
//        if (x.prev == null) first = next;
//        else x.prev.next = next;
//        if (x.next == null) last = prev;
//        else x.next.prev = prev;
//        size--;
//        return elem;
        return unlink(x);
    }

    public E unlink(Node<E> node){
        final Node<E> next = node.next;
        final Node<E> prev = node.prev;
        final E elem = node.item;
        if (node.prev == null) first = next;
        else node.prev.next = next;
        if (node.next == null) last = prev;
        else node.next.prev = prev;

        node.next = null;
        node.prev = null;
        node.item = null;
        size--;
        return elem;
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
    public E pollFirst() {
        return (first == null) ? null : unlink(first);
    }

    @Override
    public E pollLast() {
        return (last == null) ? null : unlink(last);
    }

    @Override
    public E getFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        return first.item;
    }

    @Override
    public E getLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }
        return last.item;
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
    public E remove() {
        return null;
    }

    @Override
    public E poll() {
        if (first == null) return null;
        return unlink(first);
    }

    @Override
    public E element() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        return first.item;
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
    public boolean remove(Object o) {
        if (size == 0) return false;
        if (o == null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.item == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.item)) {
                    unlink(x);
                    return true;
                }
            }
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

    @Override
    public int size() {
        return size;
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
