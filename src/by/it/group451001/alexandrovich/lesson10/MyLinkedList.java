package by.it.group451001.alexandrovich.lesson10;

import java.util.*;

public class MyLinkedList<E> implements Deque<E> {

    ListNode head;
    ListNode tail;
    int size;

    class ListNode{
        ListNode left;
        ListNode right;
        E info;
    }

    MyLinkedList(){
        head = null;
        tail = null;
        size = 0;
    }

    //////               Обязательные к реализации методы             ///////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        ListNode node = head;
        while (node != null){
            sb.append(node.info.toString());
            if (node.right != null) sb.append(", ");
            node = node.right;
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        if (size == 0){
            head = new ListNode();
            tail = head;
            head.info = e;
            size++;
            return true;
        }
        tail.right = new ListNode();
        tail.right.left = tail;
        tail = tail.right;
        tail.info = e;
        size++;
        return true;
    }

    public E remove(int index) {
        if (index > size - 1 || index < 0) return null;
        E res;
        if (index == 0){
            if (size == 1){
                res = head.info;
                head = null;
                tail = null;
                size = 0;
                return res;
            }
            res = head.info;
            head = head.right;
            head.left = null;
            size--;
            return res;
        }
        if (index == size-1){
            res = tail.info;
            tail = tail.left;
            tail.right = null;
            size--;
            return res;
        }
        ListNode node = head;
        for (int i = 0; i < index-1; i++) {
            node = node.right;
        }
        res = node.right.info;
        node.right = node.right.right;
        node.right.left = node;
        size--;
        return res;
    }

    @Override
    public boolean remove(Object o) {
        if (size == 1 && o != head.info) return false;
        if (o == head.info){
            if (size == 1){
                head = null;
                tail = null;
                size = 0;
                return true;
            }
            head = head.right;
            head.left = null;
            size--;
            return true;
        }
        if (o == tail.info){
            tail = tail.left;
            tail.right = null;
            size--;
            return true;
        }
        ListNode node = head.right;
        while (node != tail && node.info != o) {
            node = node.right;
        }
        if (node.info == o) {
            node.left.right = node.right;
            node.right.left = node.left;
            size--;
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E e) {
        if (size == 0){
            head = new ListNode();
            tail = head;
            head.info = e;
            size++;
        }
        head.left = new ListNode();
        head.left.right = head;
        head = head.left;
        head.info = e;
        size++;
    }

    @Override
    public void addLast(E e) {
        if (size == 0){
            head = new ListNode();
            tail = head;
            head.info = e;
            size++;
        }
        tail.right = new ListNode();
        tail.right.left = tail;
        tail = tail.right;
        tail.info = e;
        size++;
    }

    @Override
    public E element() {
        return head.info;
    }

    @Override
    public E getFirst() {
        return head.info;
    }

    @Override
    public E getLast() {
        return tail.info;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        if (size == 1){
            E res = head.info;
            head = null;
            tail = null;
            size--;
            return res;
        }
        E res = head.info;
        head = head.right;
        head.left = null;
        size--;
        return res;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        if (size == 1){
            E res = head.info;
            head = null;
            tail = null;
            size--;
            return res;
        }
        E res = tail.info;
        tail = tail.left;
        tail.right = null;
        size--;
        return res;
    }

    //////               Необязательные к реализации методы             ///////

    @Override
    public boolean offerFirst(E e) {return false;}

    @Override
    public boolean offerLast(E e) {return false;}

    @Override
    public E removeFirst() {return null;}

    @Override
    public boolean removeFirstOccurrence(Object o) {return false;}

    @Override
    public E removeLast() {return null;}

    @Override
    public E peekFirst() {return null;}

    @Override
    public E peekLast() { return null;}

    @Override
    public boolean removeLastOccurrence(Object o) { return false; }

    @Override
    public boolean offer(E e) { return false; }

    @Override
    public E remove() { return null; }

    @Override
    public E peek() { return null; }

    @Override
    public boolean addAll(Collection<? extends E> c) { return false; }

    @Override
    public boolean removeAll(Collection<?> c) { return false; }

    @Override
    public boolean retainAll(Collection<?> c) { return false; }

    @Override
    public void clear() { }

    @Override
    public void push(E e) { }

    @Override
    public E pop() { return null; }

    @Override
    public boolean containsAll(Collection<?> c) { return false; }

    @Override
    public boolean contains(Object o) { return false; }

    @Override
    public boolean isEmpty() { return false; }

    @Override
    public Iterator<E> iterator() { return null; }

    @Override
    public Object[] toArray() { return new Object[0]; }

    @Override
    public <T> T[] toArray(T[] a) { return null; }

    @Override
    public Iterator<E> descendingIterator() { return null; }
}
