package by.it.group410902.sivtsov.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        E value;
        Node<E> prev, next;
        Node(E e){ this.value=e; }
    }

    private Node<E> head, tail;
    private int size;

    public MyLinkedList(){}

    @Override
    public String toString(){
        if(size==0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node<E> e = head;
        while(e!=null){
            sb.append(String.valueOf(e.value));
            e = e.next;
            if(e!=null) sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }

    public boolean add(E element){
        addLast(element);
        return true;
    }
    public E remove(int index){
        if(index<0 || index>=size) throw new IndexOutOfBoundsException();
        Node<E> cur;
        if(index < (size>>1)){
            cur = head;
            for(int i=0;i<index;i++) cur = cur.next;
        } else {
            cur = tail;
            for(int i=size-1;i>index;i--) cur = cur.prev;
        }
        E val = cur.value;
        unlink(cur);
        return val;
    }
    @Override
    public boolean remove(Object element) {
        Node<E> current = head;
        while (current != null) {
            if ((element == null && current.value == null) ||
                    (element != null && element.equals(current.value))) {

                unlink(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }
    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E element) {
        Node<E> newNode = new Node<>(element);
        if (head == null) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }
    @Override
    public void addLast(E element) {
        Node<E> newNode = new Node<>(element);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    @Override
    public E element(){ return getFirst(); }

    @Override
    public E getFirst(){
        if(size==0) throw new NoSuchElementException();
        return head.value;
    }
    @Override
    public E getLast(){
        if(size==0) throw new NoSuchElementException();
        return tail.value;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (head == null) {
            return null;
        }
        E value = head.value;
        head = head.next;
        if (head != null) {
            head.prev = null;
        } else {
            tail = null;
        }
        size--;
        return value;
    }

    @Override
    public E pollLast() {
        if (tail == null) {
            return null;
        }
        E value = tail.value;
        tail = tail.prev;
        if (tail != null) {
            tail.next = null;
        } else {
            head = null;
        }
        size--;
        return value;
    }
    private E unlink(Node<E> node) {
        E value = node.value;

        if (node.prev == null) {
            head = node.next;
        } else {
            node.prev.next = node.next;
        }

        if (node.next == null) {
            tail = node.prev;
        } else {
            node.next.prev = node.prev;
        }

        node.value = null;
        node.prev = node.next = null;
        size--;
        return value;
    }
    @Override public boolean offerFirst(E e){ throw new UnsupportedOperationException(); }
    @Override public boolean offerLast(E e){ throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e){ throw new UnsupportedOperationException(); }
    @Override public E peek(){ throw new UnsupportedOperationException(); }
    @Override public E peekFirst(){ throw new UnsupportedOperationException(); }
    @Override public E peekLast(){ throw new UnsupportedOperationException(); }
    @Override public E remove(){ E r = pollFirst(); if(r==null) throw new NoSuchElementException(); return r; }
    @Override public E removeFirst(){ return remove(); }
    @Override public E removeLast(){ E r = pollLast(); if(r==null) throw new NoSuchElementException(); return r; }
    @Override public void push(E e){ throw new UnsupportedOperationException(); }
    @Override public E pop(){ throw new UnsupportedOperationException(); }
    @Override public boolean removeFirstOccurrence(Object o){ throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o){ throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o){ throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator(){ throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator(){ throw new UnsupportedOperationException(); }
    @Override public boolean isEmpty(){ return size==0; }
    @Override public void clear(){ head = tail = null; size = 0; }
    @Override public boolean containsAll(Collection<?> c){ throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c){ throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c){ throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c){ throw new UnsupportedOperationException(); }
    @Override public Object[] toArray(){ throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a){ throw new UnsupportedOperationException(); }
}
