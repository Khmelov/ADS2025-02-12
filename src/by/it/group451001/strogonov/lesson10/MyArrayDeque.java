package by.it.group451001.strogonov.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

///  Создайте class MyArrayDeque<E>, который реализует интерфейс Deque<E>
///  и работает на основе приватного массива типа E[]
///  БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

public class MyArrayDeque<E> implements Deque<E> {

    private int head, tail, len, virlen;
    private E[] arr;

    @SuppressWarnings("unchecked")
    private void extend_left_vir_len(){
        E[] new_arr = (E[]) new Object[virlen << 1];
        System.arraycopy(arr,0, new_arr, virlen, virlen);
        tail += virlen;
        head = virlen;
        arr = new_arr;
        virlen <<= 1;
    }

    @SuppressWarnings("unchecked")
    private void extend_right_vir_len(){
        E[] new_arr = (E[]) new Object[virlen << 1];
        System.arraycopy(arr, 0, new_arr, 0, virlen);
        arr = new_arr;
        virlen <<= 1;
    }


    @SuppressWarnings("unchecked")
    public MyArrayDeque(){
        final int startLen = 8;
        len = 0;
        head = (startLen >> 1) - 1;
        tail = head;
        arr = (E[]) new Object[startLen];
        virlen = startLen;
    }

    @Override
    public String toString(){
        if (len == 0)
            return "[]";
        StringBuilder res = new StringBuilder("[");
        for (int i = head; i < tail; i++){
            res.append(arr[i].toString()).append(", ");
        }
        res.append(arr[tail]);
        res.append("]");
        return res.toString();
    }

    @Override
    public int size() {
        return len;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void addFirst(E e) {
        len++;
        if (len == 1)
            arr[head] = e;
        else {
            if (head == 0)
                extend_left_vir_len();
            arr[--head] = e;
        }
    }

    @Override
    public void addLast(E e) {
        len++;
        if (len == 1)
            arr[tail] = e;
        else {
            if (tail == virlen - 1)
                extend_right_vir_len();
            arr[++tail] = e;
        }
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        return arr[head];
    }

    @Override
    public E getLast() {
        return arr[tail];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        len--;
        return arr[head++];
    }

    @Override
    public E pollLast() {
        len--;
        return arr[tail--];
    }
    /// ////////////////////////////////////////////////////////////////////////////////////////////////////


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
