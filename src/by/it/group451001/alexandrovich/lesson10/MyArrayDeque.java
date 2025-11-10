package by.it.group451001.alexandrovich.lesson10;

import java.util.*;

/* Создайте class MyArrayDeque<E>, который реализует интерфейс Deque<E>
и работает на основе приватного массива типа E[]
БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ */

public class MyArrayDeque<E> implements Deque<E> {

    private Object[] Deck;
    int left;
    int right;
    int size;

    public MyArrayDeque(){
        Deck = new Object[10];
        size = 10;
        left = 4;
        right = 5;
    }

    //////               Обязательные к реализации методы             ///////

    private void check(){
        if (right >= size) right = 0;
        if (right < 0) right = size-1;
        if (left < 0) left = size-1;
        if (left >= size) left = 0;
        if (right == left) {
            size *= 2;
            Object[] temp = Deck;
            Deck = new Object[size];
            System.arraycopy(temp, left+1, Deck, 0, size/2-left-1);
            System.arraycopy(temp, 0, Deck, size/2-left-1, right);
            left = size-1;
            right = size/2-1;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = left+1; i != right; i++) {
            if (i == size) i = 0;
            sb.append(Deck[i].toString());
            if (i != right-1) sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public int size() {
        return right>left?right-left-1:size+right-left-1;
    }

    @Override
    public boolean add(E e) {
        Deck[right++] = e;
        check();
        return true;
    }

    @Override
    public void addFirst(E e) {
        Deck[left--] = e;
        check();
    }

    @Override
    public void addLast(E e) {
        Deck[right++] = e;
        check();
    }

    @Override
    public E element() {
        if (left == size-1) return (E) Deck[0];
        return (E) Deck[left+1];
    }

    @Override
    public E getFirst() {
        if (left == size-1) return (E) Deck[0];
        return (E) Deck[left+1];
    }

    @Override
    public E getLast() {
        if (right == 0) return (E) Deck[size-1];
        return (E) Deck[right-1];
    }

    @Override
    public E poll() {
        if (size() == 0) return null;
        left++;
        check();
        return (E) Deck[left];
    }

    @Override
    public E pollFirst() {
        return poll();
    }

    @Override
    public E pollLast() {
        if (size() == 0) return null;
        right--;
        check();
        return (E) Deck[right];
    }

    //////               Необязательные к реализации методы             ///////

    @Override
    public boolean offerFirst(E e) {return false;}

    @Override
    public boolean offerLast(E e) {return false;}

    @Override
    public E removeFirst() {return null;}

    @Override
    public boolean removeFirstOccurrence(Object o) {return false;}

    @Override
    public E removeLast() {return null;}

    @Override
    public E peekFirst() {return null;}

    @Override
    public E peekLast() { return null;}

    @Override
    public boolean removeLastOccurrence(Object o) { return false; }

    @Override
    public boolean offer(E e) { return false; }

    @Override
    public E remove() { return null; }

    @Override
    public E peek() { return null; }

    @Override
    public boolean addAll(Collection<? extends E> c) { return false; }

    @Override
    public boolean removeAll(Collection<?> c) { return false; }

    @Override
    public boolean retainAll(Collection<?> c) { return false; }

    @Override
    public void clear() { }

    @Override
    public void push(E e) { }

    @Override
    public E pop() { return null; }

    @Override
    public boolean remove(Object o) { return false; }

    @Override
    public boolean containsAll(Collection<?> c) { return false; }

    @Override
    public boolean contains(Object o) { return false; }

    @Override
    public boolean isEmpty() { return false; }

    @Override
    public Iterator<E> iterator() { return null; }

    @Override
    public Object[] toArray() { return new Object[0]; }

    @Override
    public <T> T[] toArray(T[] a) { return null; }

    @Override
    public Iterator<E> descendingIterator() { return null; }

}
