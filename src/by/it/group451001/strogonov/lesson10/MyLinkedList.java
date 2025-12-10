package by.it.group451001.strogonov.lesson10;

import by.it.group451001.strogonov.lesson09.ListC;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyLinkedList<E> implements Deque<E> {

    private static class node<E>{
        node<E> next, prev;
        E data;
        public node(){
            next = null;
            prev = null;
        }
        public node(node<E> next, node<E> prev, E data){
            this.next = next;
            this.prev = prev;
            this.data = data;
        }
    }

    int len = 0;
    final node<E> head = new node<>();
    node<E> tail = head;

    @Override
    public String toString() {
        if (len == 0)
            return "[]";
        StringBuilder res = new StringBuilder("[");
        for (var tmp = head.next; tmp.next != null; tmp = tmp.next)
            res.append(tmp.data.toString()).append(", ");
        res.append(tail.data.toString());
        return res.toString() + ']';
    }

    @Override
    public void addFirst(E e) {
        len++;
        var tmp = new node<>(head.next, head, e);
        head.next = tmp;
        if(tmp.next != null)
            tmp.next.prev = tmp;
    }

    @Override
    public void addLast(E e) {
        add(e);
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
        return pollFirst();
    }

    @Override
    public E removeLast() {
        return null;
    }

    @Override
    public E pollFirst() {
        --len;
        final var tmp = head.next;
        head.next = tmp.next;
        if (tmp.next != null)
            tmp.next.prev = head;
        return tmp.data;
    }

    @Override
    public E pollLast() {
        len--;
        final var tmp = tail.data;
        tail = tail.prev;
        tail.next = null;
        return tmp;
    }

    @Override
    public E getFirst() {
        return head.next.data;
    }

    @Override
    public E getLast() {
        return tail.data;
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
        len++;
        tail.next = new node<>(null, tail, e);
        tail = tail.next;
        return true;
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E element() {
        return getFirst();
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
        for (var tmp = head.next; tmp != null; tmp = tmp.next)
            if (o.equals(tmp.data)){
                tmp.prev.next = tmp.next;
                if (tmp.next != null)
                    tmp.next.prev = tmp.prev;
                else
                    tail = tmp.prev;
                len--;
                return true;
            }
        return false;
    }


    public E remove(int index){
        if (index == len - 1){
            var tmp = tail.data;
            tail = tail.prev;
            tail.next = null;
            len--;
            return tmp;
        }
        else{
            len--;
            node<E> tmp = head.next;
            for (int i = 0; i < index; i++)
                tmp = tmp.next;
            tmp.prev.next = tmp.next;
            tmp.next.prev = tmp.prev;
            return tmp.data;
        }
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
        return len;
    }

    @Override
    public boolean isEmpty() {
        return len == 0;
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
