package by.it.group451002.koltsov.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {
    // Собственно массия для хранения элементов
    E[] elems = (E[]) new Object[0];
    // Индексы первого и последнего элементов Deque
    int headInd = -1;
    int tailInd = -1;

    // Данный метод расширяет массив для хранения элементов, если необходимо
    private void grow(int elemsAmount)
    {
        // Если помещаемые элементы не помещаются в массив текущей длины
        if (size() + elemsAmount > elems.length)
        {
            E[] newElems = (E[]) new Object[(size() + elemsAmount) * 2];

            // Если элементы в массиве лежат в неестественном порядке
            if (tailInd < headInd) {
                // Переносим элементы с головы
                System.arraycopy(elems, headInd, newElems, newElems.length - headInd, elems.length - headInd);
                // Переносим хвостовые элементы
                System.arraycopy(elems, 0, newElems, 0, tailInd + 1);
            }
            else {
                // Если массив непустой, нужно перенести элементы и переопределить индексы
                if (headInd != -1) {
                    System.arraycopy(elems, headInd, newElems, headInd, size());
                    headInd = 0;
                    tailInd = headInd + size() - 1;
                }
            }
            elems = newElems;
        }
    }

    // Методы, которые необходимо реализовать

    @Override
    public String toString() {
        StringBuilder resStr = new StringBuilder("[");
        for (int i = 0; i < size(); i++)
        {
            resStr.append(elems[(headInd + i) % elems.length]);
            if (i + 1 < size())
                resStr.append(", ");
        }
        return resStr + "]";
    }

    @Override
    public int size() {
        if (headInd == -1)
            return 0;
        else
            return tailInd >= headInd ?  tailInd - headInd + 1 : elems.length - (headInd - tailInd - 1);
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void addFirst(E e) {
        if (e == null)
            throw new NullPointerException();
        grow(1);
        if (headInd == -1)
            headInd = tailInd = 0;
        else
            headInd = headInd == 0 ? elems.length - 1 : headInd - 1;
        elems[headInd] = e;
    }

    @Override
    public void addLast(E e) {
        if (e == null)
            throw new NullPointerException();
        grow(1);
        if (headInd == -1)
            headInd = tailInd = 0;
        else
            tailInd = (tailInd + 1) % elems.length;
        elems[tailInd] = e;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (headInd == -1)
            throw new NoSuchElementException();
        return elems[headInd];
    }

    @Override
    public E getLast() {
        if (headInd == -1)
            throw new NoSuchElementException();
        return elems[tailInd];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (headInd == -1)
            return null;
        else {
            E tempVal = getFirst();
            removeElem(headInd);
            return tempVal;
        }
    }

    @Override
    public E pollLast() {
        if (tailInd == -1)
            return null;
        else {
            E tempVal = getLast();
            removeElem(tailInd);
            return tempVal;
        }
    }

    private void removeElem(int index) {
        if (index == tailInd)
        {
            if (headInd == tailInd)
                headInd = tailInd = -1;
            else
            {
                if (tailInd == 0)
                    tailInd = elems.length - 1;
                else
                    tailInd--;
            }
        }
        else if (index == headInd)
        {
            if (headInd == elems.length - 1)
                headInd = 0;
            else
                headInd++;
        }
    }

    // Методы, которые можно не реализовывать

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