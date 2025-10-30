package by.it.group410901.borisdubinin.lesson10;

import java.util.*;

public class MyLinkedList<E>{

    private int size;
    private Node head;
    private Node tail;

    private class Node{
        private E elem;

        public Node prev;
        public Node next;

        public Node(E elem){
            this.elem = elem;
        }
        public Node(E elem, Node prev, Node next){
            this.elem = elem;
            this.next = next;
            this.prev = prev;
        }
    }

    private Node getNode(int index){
        Node ptr;
        if (index <= size /2) {
            ptr = head;
            for (; index > 0; index--)
                ptr = ptr.next;
        } else {
            ptr = tail;
            for (int tail_index = size - 1; tail_index > index; tail_index--)
                ptr = ptr.prev;
        }
        return ptr;
    }

    public MyLinkedList(){
        size = 0;
        head = null;
        tail = null;
    }
    
    /// /////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////////////////////////////////////////////
    
    //@Override
    public String toString(){
        StringBuilder sb = new StringBuilder(size * 16);
        sb.append("[");

        for(Node ptr = head; ptr != null; ptr = ptr.next){
            sb.append(ptr.elem);
            if(ptr.next != null)
                sb.append(", ");
        }
        sb.append("]");

        return sb.toString();
    }
    //@Override
    public int size(){
        return size;
    }

    //@Override
    public boolean add(E element){
        addLast(element);
        return true;
    }
    //@Override
    public void addFirst(E element){
        if(size == 0){
            head = new Node(element, null, null);
            tail = head;
        }
        else {
            head = new Node(element, null, head);
            head.next.prev = head;
        }
        size++;
    }
    //@Override
    public void addLast(E element){
        if (size == 0){
            head = new Node(element, null, null);
            tail = head;
        }
        else {
            tail = new Node(element, tail, null);
            tail.prev.next = tail;
        }
        size++;
    }

    public E remove(int index){
        if(index < 0 || index >= size)
            return null;

        Node ptr;
        if(size == 1){
            ptr = head;
            head = tail = null;
        }
        else if(index == 0){
            ptr = head;
            head = head.next;
            head.prev = null;
        }
        else if(index == size -1){
            ptr = tail;
            tail = tail.prev;
            tail.next = null;
        }
        else {
            ptr = getNode(index);
            ptr.prev.next = ptr.next;
            ptr.next.prev = ptr.prev;
        }

        size--;
        return ptr.elem;
    }
    //@Override
    public boolean remove(Object o){
        Node ptr = head;

        while (ptr != null) {
            if (Objects.equals(o, ptr.elem)) {
                if (ptr == head) {
                    head = head.next;
                    if (head != null) head.prev = null;
                    else tail = null;
                }
                else if (ptr == tail) {
                    tail = tail.prev;
                    tail.next = null;
                }
                else {
                    ptr.prev.next = ptr.next;
                    ptr.next.prev = ptr.prev;
                }
                size--;
                return true;
            }
            ptr = ptr.next;
        }
        return false;
    }

    //@Override
    public E element() {
        if(size == 0)
            return null;
        return getFirst();
    }
   // @Override
    public E getFirst() {
        return head.elem;
    }
    //@Override
    public E getLast() {
        return tail.elem;
    }

    //@Override
    public E poll() {
        return pollFirst();
    }
    //@Override
    public E pollFirst() {
        if(size == 0)
            throw new NoSuchElementException("Коллекция пуста");

        E first = head.elem;
        remove(0);
        return first;
    }
    //@Override
    public E pollLast() {
        if(size == 0)
            throw new NoSuchElementException("Коллекция пуста");

        E last = tail.elem;
        remove(size-1);
        return last;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean offerFirst(E e){
        return false;
    }
    public boolean offerLast(E e){
        return false;
    }
    public E removeFirst(){
        return null;
    }
    public E removeLast(){
        return null;
    }
    public E peekFirst(){
        return null;
    }
    public E peekLast(){
        return null;
    }
    public boolean removeFirstOccurrence(Object o){
        return false;
    }
    public boolean removeLastOccurrence(Object o){
        return false;
    }
    public boolean offer(E e){
        return false;
    }
    public E remove(){
        return null;
    }
    public E peek(){
        return null;
    }
    public boolean addAll(Collection<? extends E> c){
        return false;
    }

    //@Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    //@Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    //@Override
    public void clear() {
        if(isEmpty())
            return;

        Node ptr1 = head;
        Node ptr2 = head.next;
        while(ptr2 != null){
            ptr1.prev = null;
            ptr1.next = null;
            ptr1 = ptr2;
            ptr2 = ptr2.next;
        }
        ptr1.next = null;
        ptr1.prev = null;
        head = null;
        tail = null;
        size = 0;
    }

    public E get(int index){
        return getNode(index).elem;
    }

    public void push(E e){

    }
    public E pop(){
        return null;
    }
    public boolean contains(Object o){
        Node ptr = head;
        while(ptr != null){
            if(Objects.equals(o, ptr.elem))
                return true;
            ptr = ptr.next;
        }
        return false;
    }

    public Iterator<E> iterator(){
        return new MyIterator();
    }
    private class MyIterator implements Iterator<E>{
        private Node next;
        private Node lastReturned;
        public MyIterator(){
            next = head;
            lastReturned = null;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public E next() {
            lastReturned = next;
            next = next.next;
            return lastReturned.elem;
        }

        @Override
        public void remove(){
            if (lastReturned == null) {
                throw new IllegalStateException("next() has not been called, or remove() has already been called after the last next()");
            }

            if (lastReturned == head) {
                head = head.next;
                if (head != null) {
                    head.prev = null;
                } else {
                    // Если список стал пустым
                    tail = null;
                }
            }
            // Если удаляем хвост
            else if (lastReturned == tail) {
                tail = tail.prev;
                tail.next = null;
            }
            // Если удаляем элемент в середине
            else {
                lastReturned.prev.next = lastReturned.next;
                lastReturned.next.prev = lastReturned.prev;
            }

            size--;
            lastReturned = null; // Запрещаем повторный вызов remove()
        }
    }

    public Iterator<E> descendingIterator(){
        return null;
    }

    public boolean isEmpty(){
        return size < 1;
    }

    public E[] toArray(){
        return null;
    }

    //@Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    //@Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

}
