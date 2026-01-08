package by.it.group451002.spizharnaya.lesson10;

/////////////////////////////////////////////////////////////////////////
//////           Создайте class MyLinkedList<E>, который реализует интерфейс Deque<E>
///              и работает на основе двунаправленного связного списка
///          Обязательные к реализации методы
//////               toString()
//////               size()
//////               add(E element)
//////               addFirst(E element)
//////               addLast(E element)
///                  remove(int)
///                  remove(E element)
//////               element()
///////              getFirst()
//////               getLast()
///////              poll()
///////              pollFirst()
///////              pollLast()
/////////////////////////////////////////////////////////////////////////

import java.util.*;

public class MyLinkedList<E> implements Deque<E> {
    private class Node<E>{
        //fields
        E data;
        Node<E> prev;
        Node<E> next;

        //constructor
        Node() {
            this.data = null;
            this.prev = null;
            this.next = null;
        }
        Node(E data) {
            this.data = data;
            this.prev = null;
            this.next = null;
        }
        Node(E data, Node<E> prev, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> header, tail;
    private int size = 0;

    public MyLinkedList(){
        header = new Node<>();
        size = 0;
        tail = header;
    }

    @Override
    public String toString(){
        StringBuilder res = new StringBuilder("[");
        Node<E> curr = header;
        if (curr.next != null) {
            res.append(curr.next.data);
            curr = curr.next;
        }
        while (curr.next != null){
            res.append(", ");
            res.append(curr.next.data);
            curr = curr.next;
        }
        res.append("]");
        return res.toString();
    }

    @Override
    public int size(){
        return size;
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

    @Override
    public void addFirst(E e) {
        Node<E> curr = new Node<>(e, header, header.next);
        header.next = curr;
        if (size>0)
            curr.next.prev = curr;
        size++;
    }

    @Override
    public void addLast(E e) {
        tail.next = new Node<>(e, tail, null);
        tail = tail.next;
        size++;
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
    public boolean add(E e){
        addLast(e);
        return true;
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
        return null;
    }

    public E remove(int ind){
        if (ind >= size) return null;

        //после цикла элемент который надо удалить
        Node<E> curr = header.next;
        for (int i=0; i<ind; i++)
            curr = curr.next;

        curr.prev.next = curr.next;
        if (curr.next != null)
            curr.next.prev = curr.prev;
        size--;
        return curr.data;
    }

    @Override
    public boolean remove(Object o) {
        Node<E> curr = header.next;
        while (!Objects.equals(curr.data, o))
            if (curr.next != null)
                curr = curr.next;
            else
                return false;


        curr.prev.next = curr.next;
        if (curr.next != null)
            curr.next.prev = curr.prev;
        size--;
        return true;
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
    public E element(){
        if (size>0)
            return header.next.data;
        else
            throw new NoSuchElementException();
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
    public E getFirst(){
        return element();
    }

    @Override
    public E getLast(){
        if (size>0)
            return tail.data;
        else
            throw new NoSuchElementException();
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
    public E poll(){
        return remove(0);
    }

    @Override
    public E pollFirst(){
        return remove(0);
    }

    @Override
    public E pollLast(){
        E res = tail.data;
        tail = tail.prev;
        tail.next = null;
        size--;
        return res;
    }
}
