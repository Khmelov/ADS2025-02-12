package by.it.group451003.shepetuyk.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyLinkedList<E> implements Deque<E> {

    private static class Queue<E> {
        E item;
        Queue<E> next;
        Queue<E> prev;

        Queue(Queue<E> prev, E item, Queue<E> next) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }

    private int size = 0;
    private Queue<E> first;
    private Queue<E> last;


    public MyLinkedList(Collection<? extends E> c) {
        this();
        for (E element : c)
            add(element);
    }

    /////////////////////
    // обязательные
    /////////////////////

    public MyLinkedList() {
    }

    @Override
    public String toString() {
        StringBuilder strbldr = new StringBuilder();
        strbldr.append("[");
        Queue<E> tempQueue = first;

        for (int i = 0; i < size; i++) {
            strbldr.append(tempQueue.item + ", ");
            tempQueue = tempQueue.next;
        }

        if (strbldr.length() > 1)
            strbldr.delete(strbldr.length() - 2, strbldr.length());
        strbldr.append("]");
        return strbldr.toString();
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    public E remove(int index) {
        if (size != 0 && (index >= 0 || index < size)) {
            Queue<E> tempQueue = first;
            for (int i = 0; i < index; i++)
                tempQueue = tempQueue.next;

            tempQueue.prev.next = tempQueue.next;
            if (tempQueue.next != null)
                tempQueue.next.prev = tempQueue.prev;

            size--;
            return tempQueue.item;
        }
        return null;
    }

    @Override
    public boolean remove(Object o) {
        boolean changed = false;
        Queue<E> tempQueue = first;

        for (int i = 0; i < size; i++) {
            if (tempQueue.item == o) {
                tempQueue.prev.next = tempQueue.next;
                if (tempQueue.next != null)
                    tempQueue.next.prev = tempQueue.prev;

                changed = true;
                size--;
                break;
            }
            tempQueue = tempQueue.next;
        }

        return changed;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E element) {
        Queue<E> tempFirst = first;
        Queue<E> newElem = new Queue<>(null, element, first);
        first = newElem;
        if (tempFirst == null)
            last = first;
        else
            tempFirst.prev = first;

        size++;
    }

    @Override
    public void addLast(E element) {
        Queue<E> tempLast = last;
        Queue<E> newElem = new Queue<>(last, element, null);
        last = newElem;
        if (tempLast == null)
            first = last;
        else
            tempLast.next = last;

        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0)
            return null;
        return first.item;
    }

    @Override
    public E getLast() {
        if (size == 0)
            return null;
        return last.item;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size != 0) {
            Queue<E> tempQueue = first;
            first = first.next;
            if (first != null)
                first.prev = null;

            size--;
            return tempQueue.item;
        }
        return null;
    }

    @Override
    public E pollLast() {
        if (size != 0) {
            Queue<E> tempQueue = last;
            last = last.prev;
            if (last != null)
                last.next = null;

            size--;
            return tempQueue.item;
        }
        return null;
    }

    /////////////////
    // необязательные
    /////////////////

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
    public E remove() {
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
