package by.it.group451001.shymko.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {
    private class Node {
        E data;
        Node next;
        Node prev;
        public Node(){
            data = null;
            next = null;
            prev = null;
        }
        public Node(E data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }
    private Node head;
    private Node tail;
    private int size;
    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }
    public String toString(){
        String s = "[";
        Node current = head;
        if(current == null){
            return "[]";
        }
        s += current.data.toString();
        current = current.next;
        while(current != null){
            s += ", " + current.data.toString();
            current = current.next;
        }
        return s + "]";
    }

    public boolean add(E e) {
        Node newNode = new Node(e);
        if(head == null){
            head = newNode;
            tail = newNode;
        }
        else{
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
        return true;
    }

    public E remove(int index){
        if(index < 0 || index >= size){
            return null;
        }
        if(index < (size >> 1)){
            if(index == 0){
                E data = head.data;
                head = head.next;
                head.prev = null;
                size--;
                return data;
            }
            Node current = head;
            for(int i = 0; i < index; i++){
                current = current.next;
            }
            current.prev.next = current.next;
            current.next.prev = current.prev;
            E data = current.data;
            current = null;
            size--;
            return data;
        }
        else{
            if(index == size-1){
                E data = tail.data;
                tail = tail.prev;
                tail.next = null;
                size--;
                return data;
            }
            Node current = tail;
            for(int i = size - 1; i > index; i--){
                current = current.prev;
            }
            current.prev.next = current.next;
            current.next.prev = current.prev;
            E data = current.data;
            current = null;

            size--;
            return data;
        }
    }
    @Override
    public boolean remove(Object o) {
        Node current = head;
        while(current != null && !current.data.equals(o)){
            current = current.next;
        }
        if(current == null){
            return false;
        }
        else{
            if(current == head){
                head = head.next;
                head.prev = null;
                size--;
                return true;
            }
            else{
                if(current == tail){
                    tail = tail.prev;
                    tail.next = null;
                    size--;
                    return true;
                }
                else{
                    current.prev.next = current.next;
                    current.next.prev = current.prev;
                    current = null;
                    size--;
                    return true;
                }
            }
        }
    }

    public boolean containsAll(Collection<?> c) {
        return false;
    }

    public boolean contains(Object o) {
        return false;
    }

    public int size(){
        return size;
    }

    public Iterator<E> iterator() {
        return null;
    }

    public Object[] toArray() {
        return new Object[0];
    }

    public <T> T[] toArray(T[] a) {
        return null;
    }

    public Iterator<E> descendingIterator() {
        return null;
    }

    public void addFirst(E e){
        Node newNode = new Node(e);
        if(head == null){
            head = newNode;
            tail = newNode;
        }
        else{
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }
    public void addLast(E e){
        add(e);
    }

    public E element() throws NoSuchElementException{
        if(size == 0){
            throw new NoSuchElementException();
        }
        return head.data;
    }

    public E getFirst() throws NoSuchElementException{
        if(size == 0){
            throw new NoSuchElementException();
        }
        return head.data;
    }
    public E getLast() throws NoSuchElementException{
        if(size == 0){
            throw new NoSuchElementException();

        }
        return tail.data;
    }

    public E poll(){
        if(size == 0){
            return null;
        }
        return remove(0);
    }
    public E pollFirst(){
        if(size == 0){
            return null;
        }
        return remove(0);
    }
    public E pollLast(){
        if(size == 0){
            return null;
        }
        return remove(size - 1);
    }
    public boolean offer(E e){return false;}
    public boolean offerFirst(E e){return false;}
    public boolean offerLast(E e){return false;}
    public E peek(){return null;}
    public E peekFirst(){return null;}
    public E peekLast(){return null;}
    public E removeFirst(){return null;}
    public E removeLast(){return null;}
    @Override
    public E remove(){return remove(0);}
    public boolean isEmpty(){return size == 0;}
    public boolean removeFirstOccurrence(Object o){return false;}
    public boolean removeLastOccurrence(Object o){return false;}
    public boolean addAll(Collection<? extends E> c){return false;}

    public boolean removeAll(Collection<?> c) {
        return false;
    }

    public boolean retainAll(Collection<?> c) {
        return false;
    }
    public void clear() {

    }

    public void push(E e){}
    public E pop(){return null;}

}
