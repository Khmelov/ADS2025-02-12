package by.it.group451002.koltsov.lesson10;

import java.util.*;

public class MyLinkedList<E> implements Deque<E> {
    DequeNode<E> head = null;
    DequeNode<E> tail = null;
    int size = 0;

    // Методы, которые необходимо реализовать

    @Override
    public String toString() {
        StringBuilder resStr = new StringBuilder("[");
        DequeNode<E> currNode = head;

        for (int i = 0; i < size(); i++)
        {
            resStr.append(currNode.value);
            if (i + 1 < size()) {
                resStr.append(", ");
                currNode = currNode.nextNode;
            }
        }
        return resStr + "]";
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
        if (head == null)
        {
            head = new DequeNode<>(e);
            head.nextNode = head;
            head.prevNode = head;
            tail = head;
        }
        else {
            DequeNode<E> newNode = new DequeNode<>(e);
            newNode.nextNode = head;
            head.prevNode = newNode;
            head = newNode;
        }
        size++;
    }

    @Override
    public void addLast(E e) {
        if (head == null)
        {
            head = new DequeNode<>(e);
            head.nextNode = head;
            head.prevNode = head;
            tail = head;
        }
        else {
            DequeNode<E> newNode = new DequeNode<>(e);
            newNode.prevNode = tail;
            tail.nextNode = newNode;
            tail = newNode;
        }
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (head == null)
            throw new NoSuchElementException();
        return head.value;
    }

    @Override
    public E getLast() {
        if (tail == null)
            throw new NoSuchElementException();
        return tail.value;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (head == null)
            return null;
        E resValue = head.value;
        head = head.nextNode;
        head.prevNode = null;
        size--;
        return resValue;
    }

    @Override
    public E pollLast() {
        if (tail == null)
            return null;
        E resValue = tail.value;
        tail = tail.prevNode;
        tail.nextNode = null;
        size--;
        return resValue;
    }

    public E remove(int index) {
        DequeNode<E> tempNode = getPointerOnElem(index);
        if (tempNode.prevNode != null)
            tempNode.prevNode.nextNode = tempNode.nextNode;
        if (tempNode.nextNode != null)
            tempNode.nextNode.prevNode = tempNode.prevNode;
        size--;
        return tempNode.value;
    }

    @Override
    public boolean remove(Object o) {
        DequeNode<E> tempNode = head;
        for (int i = 0; i < size; i++)
            if (tempNode.value.equals(o))
            {
                if (tempNode.prevNode != null)
                    tempNode.prevNode.nextNode = tempNode.nextNode;
                if (tempNode.nextNode != null)
                    tempNode.nextNode.prevNode = tempNode.prevNode;
                size--;
                return true;
            }
            else
                tempNode = tempNode.nextNode;
        return false;
    }

    public DequeNode<E> getPointerOnElem(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException();

        if (index <= size / 2) {
            // Идём к элементу с начала списка
            DequeNode<E> tempElem = head;
            for (int i = 0; i < index; i++)
                tempElem = tempElem.nextNode;
            return tempElem;
        }
        else {
            // Идём к элементу с конца списка
            DequeNode<E> tempElem = tail;
            for (int i = size - 1; i > index; i--)
                tempElem = tempElem.prevNode;
            return tempElem;
        }
    }

    // Методы, которые можно не реализовывать

    @Override
    public E remove() {
        return null;
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