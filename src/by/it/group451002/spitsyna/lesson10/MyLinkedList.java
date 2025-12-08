package by.it.group451002.spitsyna.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.Objects;

public class MyLinkedList<E> implements Deque<E> {
    class Node<E>{
        E value = null;
        Node<E> next = null;
        Node<E> prev = null;

        Node(E val, Node<E> next, Node<E> prev){
            this.value = val;
            this.next = next;
            this.prev = prev;
        }
    }
    Node<E> head = null;
    Node<E> tail = null;

    //////ОБЯЗАТЕЛЬНЫЕ ДЛЯ РЕАЛИЗАЦИИ МЕТОДЫ/////////////////
    public String toString(){
        StringBuilder str =  new StringBuilder("[");
        Node<E> currElem = this.head;
        while (currElem != null){
            str.append(currElem.value.toString());
            if (currElem.next != null)
                str.append(", ");
            currElem = currElem.next;
        }
        str.append("]");
        return str.toString();
    }

    @Override
    public boolean add(E e) {
        if(this.head == null){
            // Если список пуст, создаем первый элемент
            this.head = new Node<>(e,null,null);
            this.tail = this.head;
        }else{
            // Добавляем новый элемент в конец
            this.tail.next = new Node<>(e, null,this.tail);
            this.tail = this.tail.next;
        }
        return true;
    }

    // Удаление элемента по индексу
    public E remove(int ind) {
        if (ind < 0 || ind >= size())
            throw new IndexOutOfBoundsException();

        Node<E> currElem = this.head;
        for (int i = 1; i <= ind; i++){
            currElem = currElem.next;
        }
        if (currElem == this.head && currElem == this.tail){
            this.head = null;
            this.tail = null;
        }
        else if (currElem == this.head){
            this.head.next.prev = null;
            this.head = this.head.next;
        }
        else if (currElem == this.tail){
            this.tail.prev.next = null;
            this.tail = this.tail.prev;
        }
        else {
            currElem.prev.next = currElem.next;
            currElem.next.prev = currElem.prev;
        }
        return currElem.value;
    }

    @Override
    public boolean remove(Object o) { // Удаление элемента по значению
        boolean flag = false;
        Node<E> currElem = this.head;
        while (!flag && currElem != null){
            if (Objects.equals(currElem.value, o))
                flag = true;
            else
                currElem = currElem.next;
        }
        if (flag){
            if (currElem == this.head && currElem == this.tail){
                this.head = null;
                this.tail = null;
            }
            else if (currElem == this.head){
                this.head.next.prev = null;
                this.head = this.head.next;
            }
            else if (currElem == this.tail){
                this.tail.prev.next = null;
                this.tail = this.tail.prev;
            }
            else {
                currElem.prev.next = currElem.next;
                currElem.next.prev = currElem.prev;
            }
        }
        return flag;
    }

    @Override
    public int size() {
        int size = 0;
        Node<E> currElem = this.head;
        while (currElem != null){
            size++;
            currElem = currElem.next;
        }
        return size;
    }

    @Override
    public void addFirst(E e) {
        if(this.head == null){
            this.head = new Node<>(e,null,null);
            this.tail = this.head;
        }
        else {
            this.head.prev = new Node<>(e,this.head,null);
            this.head = this.head.prev;
        }
    }

    @Override
    public void addLast(E e) {
        if(this.tail == null){
            this.head = new Node<>(e,null,null);
            this.tail = this.head;
        }
        else{
            this.tail.next = new Node<>(e,null,this.tail);
            this.tail = this.tail.next;
        }
    }

    @Override
    public E element() {
        if(this.head != null) {
            return this.head.value;
        }
        return null;
    }

    // Получение первого элемента без удаления
    @Override
    public E getFirst() {
        if (this.head != null) {
            return this.head.value;
        }
        return null;
    }

    // Получение второго элемента без удаления
    @Override
    public E getLast() {
        if (this.tail != null) {
            return this.tail.value;
        }
        return null;
    }

    @Override
    public E poll() { // Извлечение и удаление первого элемента
       if (this.head != null){
           Node<E> elem = this.head;
           if (this.head == this.tail){
               this.head = null;
               this.tail = null;
           }
           else {
               this.head.next.prev = null;
               this.head = this.head.next;
           }
           return elem.value;
       }
       return null;
    }

    @Override
    public E pollFirst() {
       if (this.head != null){
           Node<E> elem = this.head;
           if (this.head == this.tail){
               this.head = null;
               this.tail = null;
           }
           else {
               this.head.next.prev = null;
               this.head = this.head.next;
           }
           return elem.value;
       }
       return null;
    }

    @Override
    public E pollLast() {
        if (this.head != null){
            Node<E> elem = this.tail;
            if (this.head == this.tail){
                this.head = null;
                this.tail = null;
            }
            else {
                this.tail.prev.next = null;
                this.tail = this.tail.prev;
            }
            return elem.value;
        }
        return null;
    }

    /////////////НЕОБЯЗАТЕЛЬНЫЕ ДЛЯ РЕАЛИЗАЦИИ МЕТОДЫ//////////////////////

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
