package by.it.group451001.smalian.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {


    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private static class Node<E> {
        E value;
        ListA.Node<E> next;
        Node(E val) { value = val; }
    }
    private ListA.Node<E> head;
    private ListA.Node<E> tail;
    private int size = 0;
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        ListA.Node<E> cur = head;
        while (cur != null) {
            sb.append(cur.value);
            if (cur.next != null) sb.append(", ");
            cur = cur.next;
        }
        sb.append(']');
        return sb.toString();
    }


    @Override
    public boolean add(E e) {
        ListA.Node<E> node = new ListA.Node<>(e);
        if (head == null) {
            head = tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        ListA.Node<E> cur = head;
        ListA.Node<E> prev = null;
        for (int i = 0; i < index; i++) {
            prev = cur;
            cur = cur.next;
        }
        E val = cur.value;
        if (prev == null) {
            head = cur.next;
            if (head == null) tail = null;
        } else {
            prev.next = cur.next;
            if (prev.next == null) tail = prev;
        }
        size--;
        return val;
    }

    @Override
    public int size(){
        return size;
    }

    @Override
    public void add(int index, E element) {
        ListA.Node<E> newNode = new ListA.Node<>(element);
        if (index == 0) {
            newNode.next = head;
            head = newNode;
            if (tail == null) tail = newNode;
        } else if (index == size) {
            add(element);
            return;
        } else {
            ListA.Node<E> cur = head;
            for (int i = 0; i < index - 1; i++) cur = cur.next;
            newNode.next = cur.next;
            cur.next = newNode;
        }
        size++;
    }

    @Override
    public boolean remove(Object o) {

        ListA.Node<E> cur = head;
        ListA.Node<E> prev = null;
        while (cur != null) {
            if (o == null ? cur.value == null : o.equals(cur.value)) {
                if (prev == null) {
                    head = cur.next;
                    if (head == null) tail = null;
                } else {
                    prev.next = cur.next;
                    if (prev.next == null) tail = prev;
                }
                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        ListA.Node<E> cur = head;
        for (int i = 0; i < index; i++) cur = cur.next;
        E old = cur.value;
        cur.value = element;
        return old;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        head = tail = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {

        ListA.Node<E> cur = head;
        int idx = 0;
        while (cur != null) {
            if (o == null ? cur.value == null : o.equals(cur.value)) return idx;
            cur = cur.next;
            idx++;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        ListA.Node<E> cur = head;
        for (int i = 0; i < index; i++) cur = cur.next;
        return cur.value;
    }

    @Override
    public boolean contains(Object o){
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        ListA.Node<E> cur = head;
        int idx = 0;
        int last = -1;
        while (cur != null) {
            if (o == null ? cur.value == null : o.equals(cur.value)) last = idx;
            cur = cur.next;
            idx++;
        }
        return last;
    }


    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////


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
