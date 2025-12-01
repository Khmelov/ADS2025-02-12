package by.it.group451001.sobol.lesson10;

import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.Collection;
import java.util.Iterator;

public class MyLinkedList<E> implements Deque<E> {
    private static class Node<E> {
        E v;
        Node<E> prev, next;
        Node(E v){ this.v=v; }
    }

    private Node<E> head, tail;
    private int size;

    public MyLinkedList(){}

    @Override
    public String toString(){
        if(size==0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node<E> n = head;
        while(n!=null){
            sb.append(String.valueOf(n.v));
            n = n.next;
            if(n!=null) sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
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
        E val = cur.v;
        unlink(cur);
        return val;
    }

    @Override
    public boolean remove(Object o){
        Node<E> n = head;
        while(n!=null){
            if(o==null ? n.v==null : o.equals(n.v)){
                unlink(n);
                return true;
            }
            n = n.next;
        }
        return false;
    }

    @Override
    public int size(){ return size; }

    @Override
    public void addFirst(E element){
        if(element==null) throw new NullPointerException();
        Node<E> n = new Node<>(element);
        if(size==0){
            head = tail = n;
        } else {
            n.next = head;
            head.prev = n;
            head = n;
        }
        size++;
    }

    @Override
    public void addLast(E element){
        if(element==null) throw new NullPointerException();
        Node<E> n = new Node<>(element);
        if(size==0){
            head = tail = n;
        } else {
            tail.next = n;
            n.prev = tail;
            tail = n;
        }
        size++;
    }

    @Override
    public E element(){ return getFirst(); }

    @Override
    public E getFirst(){
        if(size==0) throw new NoSuchElementException();
        return head.v;
    }

    @Override
    public E getLast(){
        if(size==0) throw new NoSuchElementException();
        return tail.v;
    }

    @Override
    public E poll(){ return pollFirst(); }

    @Override
    public E pollFirst(){
        if(size==0) return null;
        E v = head.v;
        unlink(head);
        return v;
    }

    @Override
    public E pollLast(){
        if(size==0) return null;
        E v = tail.v;
        unlink(tail);
        return v;
    }

    private void unlink(Node<E> n){
        Node<E> p = n.prev;
        Node<E> q = n.next;
        if(p==null) head = q; else p.next = q;
        if(q==null) tail = p; else q.prev = p;
        n.prev = n.next = null;
        n.v = null;
        size--;
        if(size==0){ head = tail = null; }
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
