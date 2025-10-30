package by.it.group410901.borisdubinin.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

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

    public ListA(){
        size = 0;
        head = null;
        tail = null;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
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

    @Override
    public boolean add(E e) {
        if (size == 0){
            head = new Node(e, null, null);
            tail = head;
        }
        else {
            tail = new Node(e, tail, null);
            tail.prev.next = tail;
        }
        size++;

        return true;
    }

    @Override
    public E remove(int index) {
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
            if (index <= size /2) {
                ptr = head;
                for (; index > 0; index--)
                    ptr = ptr.next;
            } else {
                ptr = tail;
                for (int tail_index = size - 1; tail_index > index; tail_index--)
                    ptr = ptr.prev;
            }
            ptr.prev.next = ptr.next;
            ptr.next.prev = ptr.prev;
        }

        size--;
        return ptr.elem;
    }

    @Override
    public int size() {
        return size;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public void add(int index, E element) {

    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public E set(int index, E element) {
        return null;
    }


    @Override
    public boolean isEmpty() {
        return false;
    }


    @Override
    public void clear() {

    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
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
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return null;
    }

}
