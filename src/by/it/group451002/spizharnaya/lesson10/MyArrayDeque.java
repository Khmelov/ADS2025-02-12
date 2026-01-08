package by.it.group451002.spizharnaya.lesson10;

/////////////////////////////////////////////////////////////////////////
//////           Создайте class MyArrayDeque<E>, который реализует интерфейс Deque<E>
///              и работает на основе приватного массива типа E[]
///          Обязательные к реализации методы
//////               toString()
//////               size()
//////               add(E element)
//////               addFirst(E element)
//////               addLast(E element)
//////               element()
///////              getFirst()
//////               getLast()
///////              poll()
///////              pollFirst()
///////              pollLast()
/////////////////////////////////////////////////////////////////////////

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {
    private static final int defaultSize = 10;
    private Object[] queue;
    private int size;

    //constructor
    public MyArrayDeque(int capacity){
        queue = new Object[capacity];
        size = capacity;
    }
    public MyArrayDeque(){
        queue = new Object[defaultSize];
        size = 0;
    }

    @Override
    public String toString(){
        StringBuilder res = new StringBuilder("[");
        if (size>0)
            res.append(queue[0]);
        for (int i=1; i<size; i++){
            res.append(", ");
            res.append(queue[i]);
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
    public boolean add(E e) {
        if (size == queue.length){
            Object[] temp = new Object[size*2];
            for (int i = 0; i<size; i++){
                temp[i] = queue[i];
            }
            queue = temp;
        }
        queue[size++] = e;
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

    @Override
    public void addFirst(E e){
        if (size == queue.length){
            Object[] temp = new Object[size*2];
            for (int i = 0; i<size; i++){
                temp[i] = queue[i];
            }
            queue = temp;
        }
        for (int i = size; i>0; i--){
            queue[i] = queue[i-1];
        }
        queue[0] = e;
        size++;
    }

    @Override
    public void addLast(E e){
        add(e);
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
        if (size>0)
            return (E)queue[0];
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
    public boolean remove(Object o) {
        return false;
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
        if (size>0)
            return (E)queue[size-1];
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
        E res;
        if (size>0)
            res = (E)queue[0];
        else
            res = null;

        for (int i = 0; i<size-1; i++)
            queue[i] = queue[i+1];
        queue[--size] = 0;
        return res;
    }

    @Override
    public E pollFirst(){
        return poll();
    }

    @Override
    public E pollLast(){
        E res = null;
        if (size>0) {
            res = (E) queue[--size];
            queue[size] = 0;
        }
        return res;
    }
}