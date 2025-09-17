package by.it.group451001.strogonov.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {
    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    private static class node<E>{
        node<E> next, prev;
        E data;
        public node(){
            next = null;
            prev = null;
        }
        public node(node<E> next, node<E> prev, E data){
            this.next = next;
            this.prev = prev;
            this.data = data;
        }
    }

    int len = 0;
    final node<E> head = new node<>();
    node<E> tail = head;

    @Override
    public String toString() {
        if (len == 0)
            return "[]";
        String res = "[";
        node<E> tmp = head.next;
        for (int i = 0; i < len - 1; i++){
            res += tmp.data.toString() + ", ";
            tmp = tmp.next;
        }
        res += tmp.data.toString();
        return res + ']';
    }

    @Override
    public boolean add(E e) {
        len++;
        tail.next = new node<>(null, tail, e);
        tail = tail.next;
        return true;
    }

    @Override
    public E remove(int index) {
        len--;
        node<E> tmp = head.next;
        for (int i = 0; i < index; i++)
            tmp = tmp.next;
        tmp.prev.next = tmp.next;
        tmp.next.prev = tmp.prev;
        return tmp.data;
    }

    @Override
    public int size() {
        return len;
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
