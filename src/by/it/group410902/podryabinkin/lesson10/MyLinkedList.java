package by.it.group410902.podryabinkin.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyLinkedList <E> implements  Deque<E> {


    private class Node{
        E data;
        Node next;
        Node prev;
        public Node(E data, Node next, Node prev){
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
        public Node(E data) {
            this(data, null, null);
        }
    }

    Node head_Node = null;
    Node tail_Node = null;
    @Override
    public String toString(){
        if(head_Node == null) return "[]";
        Node tmp = head_Node;
        String out = "[";
        while (tmp != null){
            out += tmp.data.toString();
            if(tmp.next != null) out += ", ";
            tmp = tmp.next;

        }
        out += "]";
        return out;
    }
    @Override
    public boolean add(E element){
        if(size() == 0){
            head_Node = new Node(element);
            tail_Node = head_Node;
        }
        else{
            Node tmp = tail_Node;
            tail_Node = new Node(element,null,tmp);
            tmp.next = tail_Node;
        }
        return true;
    }

    public E remove(int index){
        if(index + 1 > size()) return null;


        Node tmp = head_Node;
        if(index == 0){
            head_Node = head_Node.next;
            head_Node.prev = null;
            return tmp.data;
        }
        else if(index == size() - 1){
            tmp = tail_Node;
            tail_Node = tail_Node.prev;
            tail_Node.next = null;
            return tmp.data;
        }

        for(int i = 0; i < index; i++){
            tmp = tmp.next;
        }
        if(tmp.prev != null) tmp.prev.next = tmp.next;
        if(tmp.next != null)tmp.next.prev = tmp.prev;

        return tmp.data;
    }

    @Override
    public boolean remove(Object o){

        Node tmp = head_Node;
        boolean found = false;
        for(int i = 0; i < size(); i++){
            if(tmp.data == o){
                found = true;
                if(i == 0){
                    head_Node = head_Node.next;
                }
                else if(i == size() - 1){
                    tail_Node = tail_Node.prev;
                }
                break;
            }
            tmp = tmp.next;

        }
        if(!found) return false;
        if(tmp.prev != null) tmp.prev.next = tmp.next;
        if(tmp.next != null) tmp.next.prev = tmp.prev;
        return true;
    }
    @Override
    public int size(){
        if(head_Node == null) return 0;
        Node tmp = head_Node;
        int counter = 0;
        while (tmp!= null){
            counter++;
            tmp = tmp.next;
        }
        return counter;
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
    public void addFirst(E element){
        if(size() == 0){
            head_Node = new Node(element);
            tail_Node = head_Node;
        }
        else{
            Node tmp = head_Node;
            head_Node = new Node(element,tmp,null);
            tmp.prev = head_Node;
        }
    }
    @Override
    public void addLast(E element){
        add(element);
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
    public E element(){
        if(size() == 0) return null;
        return head_Node.data;
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
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
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
    public E getFirst(){
        return element();
    }
    @Override
    public E getLast(){
        if(size() == 0) return null;
        return tail_Node.data;
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
        if(size() == 0) return null;
        E tmp = getFirst();
        remove(0);
        return tmp;
    }
    @Override
    public E pollFirst(){
        return poll();
    }
    @Override
    public E pollLast(){
        if(size() == 0) return null;
        E tmp = getLast();
        remove(size()-1);
        return tmp;
    }

}
