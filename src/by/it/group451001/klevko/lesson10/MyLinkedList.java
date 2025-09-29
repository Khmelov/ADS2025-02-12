package by.it.group451001.klevko.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

/*
                toString()
                add(E element)
                remove(int)
                remove(E element)
                size()

                addFirst(E element)
                addLast(E element)

                element()
                getFirst()
                getLast()

                poll()
                pollFirst()
                pollLast()*/

public class MyLinkedList<E> implements Deque<E> {
    class Node<E>{
        E data;
        Node<E> next;
        Node<E> last;
        Node(){
            this.data = null;
            this.next = null;
            this.last = null;
        }
        Node(E e){
            this.data = e;
            this.next = null;
            this.last = null;
        }
        Node(E e, Node<E> next, Node<E> last){
            this.data = e;
            this.next = next;
            this.last = last;
        }
    }
    Node<E> head = new Node<>();
    Node<E> tail = new Node<>();

    MyLinkedList(){
        head.last = tail;
        tail.next = head;
    }

    private void delNode(Node<E> node){
        if (node == head && node == tail) return;
        Node<E> temp = node.next;
        temp.last = node.last;
        temp = node.last;
        temp.next = node.next;
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder("[");
        Node<E> temp = head.last;
        if (temp != tail) {
            ans.append(temp.data);
            temp = temp.last;
        }
        while (temp != tail){
            ans.append(", ").append(temp.data);
            temp = temp.last;
        }
        ans.append("]");
        return ans.toString();
    }

    @Override
    public int size() {
        Node<E> temp = head.last;
        int ans = 0;
        while (temp != tail){
            ans++;
            temp = temp.last;
        }
        return ans;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    public E remove(int index){
        if (size() <= index) return null;
        Node<E> temp = head.last;
        for (int i = 0; i < index; i++) temp = temp.last;
        delNode(temp);
        return temp.data;
    }

    @Override
    public boolean remove(Object o) {
        Node<E> temp = head.last;
        while (temp != tail && temp.data != o){
            temp = temp.last;
        }
        if (temp == tail) return false;
        else {
            delNode(temp);
            return true;
        }
    }

    @Override
    public void addFirst(E e) {
        Node<E> node = new Node<>(e, head, head.last);
        head.last.next = node;
        head.last = node;
    }

    @Override
    public void addLast(E e) {
        Node<E> node = new Node<>(e, tail.next, tail);
        tail.next.last = node;
        tail.next = node;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        return head.last.data;
    }

    @Override
    public E getLast() {
        return tail.next.data;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        E temp = head.last.data;
        delNode(head.last);
        return temp;
    }

    @Override
    public E pollLast() {
        E temp = tail.next.data;
        delNode(tail.next);
        return temp;
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
