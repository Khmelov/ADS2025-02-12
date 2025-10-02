package by.it.group451001.shymko.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

//ArrayDeque
public class MyArrayDeque<E> implements Deque<E> {
    private Object[] elements;
    private int size;
    private final int BaseCapacity = 10;
    private int head;
    private int tail;
    public MyArrayDeque() {
        elements = new Object[BaseCapacity];
        size = 0;
        head = tail = 0;
        elements[0] = null;
    }

    public String toString(){
        String s = "[";
        if(size == 0){
            return "[]";
        }
        s += elements[head].toString();
        for(int i = head + 1; i != tail; i++){
            if(i == elements.length){
                i = 0;
            }
            s += ", " + elements[i].toString();
        }
        return s + ", " + elements[tail].toString() + "]";
    }

    public int size(){
        return size;
    }

    private void grow(int newCapacity) {
       Object[] newE = new Object[newCapacity];
       if(head <= tail) {
           System.arraycopy(elements, head, newE, 0, size);
           head = 0;
           tail = size - 1;
       }
       else {
           System.arraycopy(elements, head, newE, 0, elements.length - head);
           System.arraycopy(elements, 0, newE, elements.length - head, tail + 1);
           head = 0;
           tail = size - 1;
       }
        elements = newE;
    }

    public void addFirst(E e) {
        if(head - 1 == tail || (head == 0 && tail == elements.length - 1)) {
            grow(elements.length + (elements.length >> 1) + 1);
        }
        if(size == 0){
            elements[head] = e;
            size++;
            return;
        }
        head--;
        if(head == -1){
            head = elements.length - 1;
        }
        elements[head] = e;
        size++;
    }

    public void addLast(E e) {
        if(head - 1 == tail || (head == 0 && tail == elements.length - 1)) {
            grow(elements.length + (elements.length >> 1) + 1);
        }
        if(size == 0){
            elements[head] = e;
            size++;
            return;
        }
        tail++;
        if(tail == elements.length){
            tail = 0;
        }
        elements[tail] = e;
        size++;
    }

    public boolean add(E e) {
        addLast(e);
        return true;
    }

    public E element() throws NoSuchElementException {
        if(size == 0){
            throw new NoSuchElementException();
        }
        return (E)elements[head];
    }

    public E getFirst() throws NoSuchElementException {
        return element();
    }
    public E getLast() throws NoSuchElementException {
        if(size == 0){
            throw new NoSuchElementException();
        }
        return (E)elements[tail];
    }

    public E poll() {
        if(size == 0){
            return null;
        }
        E e = (E)elements[head];
        if(size == 1){
            elements[head] = null;
        }
        else{
            head++;
            if(head == elements.length){
                head = 0;
            }
        }
        size--;
        return e;
    }

    public E pollFirst() {
        return poll();
    }

    public E pollLast() {
        if(size == 0){
            return null;
        }
        E e = (E)elements[tail];
        if(size == 1){
            elements[head] = null;
        }
        else{
            elements[tail] = null;
            tail--;
            if(tail == -1){
                tail = elements.length - 1;
            }
        }
        size--;
        return e;
    }

    public boolean offerFirst(E e) {
        return true;
    }
    public boolean offerLast(E e) {
        return true;
    }
    public E removeFirst() {return null;}
    public E removeLast() {return null;}
    public E peekFirst() {return null;}
    public E peekLast() {return null;}
    public boolean removeFirstOccurrence(Object o) {return false;}
    public boolean removeLastOccurrence(Object o) {return false;}
    public boolean offer(E e) {return false;}
    public E remove() {return null;}
    public E peek() {return null;}
    public boolean addAll(Collection<? extends E> c) {return false;}
    public void push(E e) {}
    public E pop() {return null;}
    @Override
    public boolean remove(Object o){return false;}
    public boolean contains(Object o) {return false;}
    public boolean isEmpty() {return size == 0;}
    public Iterator<E> iterator(){
        return null;
    }
    public Iterator<E> descendingIterator(){
        return null;
    }
    public Object[] toArray(){
        Object[] a = new Object[size()];
        if(size == 0){
            return a;
        }
        int j = 0;
        for(int i = head; i != tail; i++){
            if(i == elements.length){
                i = 0;
            }
            a[j] = elements[i];
            j++;
        }
        a[size() - 1] = elements[tail];
        return a;
    }
    public <T> T[] toArray(T[] a) {return a;}
    public boolean containsAll(Collection<?> c) {return false;}
    public boolean removeAll(Collection<?> c) {return false;}
    public boolean retainAll(Collection<?> c) {return false;}
    public void clear() {}

}
