package lesson10;

import org.w3c.dom.Node;

import java.util.*;

public class MyLinkedList<E> implements Deque<E> {

    private class MyNode<E>{
        MyNode<E> next;
        MyNode<E> prev;
        E element;
        MyNode(MyNode<E> prev,MyNode<E> next, E element) {
            this.prev = prev;
            this.next = next;
            this.element = element;
        }
    }
    private MyNode<E> head = null;
    private MyNode<E> tail = null;
    int size = 0;

    @Override
    public String toString()
    {
        StringBuilder res = new StringBuilder("[");
        var it = iterator();
        if (it.hasNext())
            res.append(it.next().toString());
        while (it.hasNext()) {
            res.append(", ");
            res.append(it.next().toString());
        }
        res.append("]");
        return res.toString();
    }
    @Override
    public int size()
    {
        return size;
    }
    @Override
    public void addFirst(E e) {
        MyNode<E> newNode;
        if (head == null) {
            newNode = new MyNode<>(null, null, e);
            head = newNode;
            tail = newNode;
        }
        else {
            newNode = new MyNode<>(null, head, e);
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    @Override
    public void addLast(E e) {
        MyNode<E> newNode;
        if (tail == null) {
            newNode = new MyNode<>(null, null, e);
            tail = newNode;
            head = newNode;
        }
        else {
            newNode = new MyNode<>(tail, null, e);
            tail.next = newNode;
            tail = newNode;
        }
        size++;
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
    public E pollFirst() {
        if (head == null)
            return null;
        E ret = head.element;
        head = head.next;
        head.prev = null;
        size--;
        return ret;
    }

    @Override
    public E pollLast() {
        if (tail == null)
            return null;
        E ret = tail.element;
        tail = tail.prev;
        tail.next = null;
        size--;
        return ret;
    }

    @Override
    public E getFirst() {
        if (head == null)
            throw new NoSuchElementException();
        return head.element;
    }

    @Override
    public E getLast() {
        if (tail == null)
            throw new NoSuchElementException();
        return tail.element;
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
        if (head == null)
            return null;
        E ret = head.element;
        head = head.next;
        size--;
        return ret;
    }

    @Override
    public E element() {
        if (head == null)
            throw new NoSuchElementException();
        return head.element;
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
    }//   @Override
    public E remove(int index)
    {
        E old = null;
        if(index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        var it = new MyIterator();
        for(int i = 0; i <= index; i++) {
            old = it.next();
        }
        it.remove();
        size--;
        return old;
    }
    @Override
    public boolean remove(Object o) {
        var it = iterator();
        while (it.hasNext()) {
            if (Objects.equals(it.next(), o)) {
                it.remove();
                size--;
                return true;
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
    public boolean isEmpty() {
        return false;
    }
    private class MyIterator implements Iterator<E> {
        MyNode<E> cursor = head;
        MyNode<E> lastRet = null;
        @Override
        public boolean hasNext() {
            return cursor != null;
        }

        @Override
        public E next() {
            lastRet = cursor;
            E ret = cursor.element;
            cursor = cursor.next;
            return ret;
        }

        @Override
        public void remove() {
            if (lastRet != null) {
                if(Objects.equals(lastRet, head))
                    head = lastRet.next;
                if(Objects.equals(lastRet, tail))
                    tail = lastRet.prev;
                lastRet.prev.next = lastRet.next;
                lastRet.next.prev = lastRet.prev;
            }
        }
    }
    @Override
    public Iterator<E> iterator() {
        return new MyIterator();
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
