package by.it.group451002.morozov.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {
    E[] elements = (E[]) new Object[0];
    int headInd = -1;
    int tailInd = -1;

    private void grow(int elemsAmount) {
        if (size() + elemsAmount > elements.length) {
            E[] newelements = (E[]) new Object[(size() + elemsAmount) * 2];

            if (tailInd < headInd) {
                System.arraycopy(elements, headInd, newelements, newelements.length - headInd, elements.length - headInd);
                System.arraycopy(elements, 0, newelements, 0, tailInd + 1);
            } else {
                if (headInd != -1) {
                    System.arraycopy(elements, headInd, newelements, headInd, size());
                    headInd = 0;
                    tailInd = headInd + size() - 1;
                }
            }
            elements = newelements;
        }
    }
    
    private void removeElem(int index) {
        if (index == tailInd) {
            if (headInd == tailInd) {
                headInd = tailInd = -1;
        	} else {
                if (tailInd == 0) tailInd = elements.length - 1;
                else tailInd--;
            }
        } else if (index == headInd) {
            if (headInd == elements.length - 1) headInd = 0;
            else headInd++;
        }
    }

    @Override
    public String toString() {
        StringBuilder resStr = new StringBuilder("[");
        for (int i = 0; i < size(); i++) {
            resStr.append(elements[(headInd + i) % elements.length]);
            if (i + 1 < size())
                resStr.append(", ");
        }
        resStr.append("]");
        return resStr.toString();
    }

    @Override
    public int size() {
        if (headInd == -1)
            return 0;
        else
            return tailInd >= headInd ? tailInd - headInd + 1 : elements.length - (headInd - tailInd - 1);
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void addFirst(E e) {
        if (e == null) throw new NullPointerException();
        grow(1);
        if (headInd == -1)
            headInd = tailInd = 0;
        else
            headInd = headInd == 0 ? elements.length - 1 : headInd - 1;
        elements[headInd] = e;
    }

    @Override
    public void addLast(E e) {
        if (e == null) throw new NullPointerException();
        grow(1);
        if (headInd == -1)
            headInd = tailInd = 0;
        else
            tailInd = (tailInd + 1) % elements.length;
        elements[tailInd] = e;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (headInd == -1) throw new NoSuchElementException();
        return elements[headInd];
    }

    @Override
    public E getLast() {
        if (headInd == -1) throw new NoSuchElementException();
        return elements[tailInd];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (headInd == -1)
            return null;
        
        E tempVal = getFirst();
        removeElem(headInd);
        return tempVal;
    }

    @Override
    public E pollLast() {
        if (tailInd == -1)
            return null;
        
        E tempVal = getLast();
        removeElem(tailInd);
        return tempVal;
    }

    // Необязательные к реализации методы

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
    public E remove() {
        return null;
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