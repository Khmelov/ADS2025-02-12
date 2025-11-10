package by.it.group451001.klevko.lesson10;

import java.util.*;
/*

                toString()
                size()

                add(E element)
                addFirst(E element)
                addLast(E element)

                element()
                getFirst()
                getLast()

                poll()
                pollFirst()
                pollLast()*/

public class MyArrayDeque<E> implements Deque<E> {
    private final int startSize = 10;
    private Object[] data = new Object[startSize];
    private int head = 5; //first element
    private int tail = 5; //last free space

    private void Grow(){
        int size = data.length;
        Object[] newData = new Object[size*2];
        System.arraycopy(data, 0, newData, 0, head);
        System.arraycopy(data, head, newData, newData.length-(size-head), size-head);
        data = newData;
        head = newData.length-(size-head);
    }

    @Override
    public String toString() {
        if (this.isEmpty()) return "[]";
        StringBuilder ans = new StringBuilder("[");
        int pos = head;
        for (int i = 0; i < size()-1; i++){
            ans.append(data[pos++]).append(", ");
            pos %= data.length;
        }
        ans.append(data[pos]).append("]");
        return ans.toString();
    }

    @Override
    public int size() {
        return (tail - head + data.length) % data.length;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void addFirst(E e) {
        head = (--head + data.length) % data.length;
        data[head] = e;
        if (head == tail) Grow();
    }

    @Override
    public void addLast(E e) {
        data[tail] = e;
        tail = (++tail) % data.length;
        if (head == tail) Grow();
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (head != tail) return (E)data[head];
        return null;
    }

    @Override
    public E getLast() {
        if (tail != head) return (E)data[(tail-1 + data.length) % data.length];
        return null;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        E temp = getFirst();
        if (temp != null){
            data[head++] = null;
            head %= data.length;
            //head = (--head + data.length) % data.length;
        }
        return temp;
    }

    @Override
    public E pollLast() {
        E temp = getLast();
        if (temp != null){
            tail = (tail - 1 + data.length) % data.length;
            data[tail] = null;
        }
        return temp;
    }



    @Override
    public boolean isEmpty() { return head == tail; }





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
