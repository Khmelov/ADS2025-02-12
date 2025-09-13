package by.it.group451001.strogonov.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {


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
        if (index == len - 1){
            var tmp = tail.data;
            tail = tail.prev;
            tail.next = null;
            len--;
            return tmp;
        }
        else{
            len--;
            node<E> tmp = head.next;
            for (int i = 0; i < index; i++)
                tmp = tmp.next;
            tmp.prev.next = tmp.next;
            tmp.next.prev = tmp.prev;
            return tmp.data;
        }
    }

    @Override
    public int size() {
        return len;
    }

    @Override
    public void add(int index, E element) {
        len++;
        var tmp = head;
        for (int i = 0; i < index; i++)
            tmp = tmp.next;
        tmp.next = new node<>(tmp.next, tmp, element);
        if (tmp.next.next != null)
            tmp.next.next.prev = tmp.next;
        else
            tail = tmp.next;
    }

    @Override
    public boolean remove(Object o) {
        for (var tmp = head.next; tmp != null; tmp = tmp.next)
            if (o.equals(tmp.data)){
                tmp.prev.next = tmp.next;
                if (tmp.next != null)
                    tmp.next.prev = tmp.prev;
                else
                    tail = tmp.prev;
                len--;
                return true;
            }
        return false;
    }

    @Override
    public E set(int index, E element) {
        var tmp = head.next;
        for (int i = 0; i < index; i++)
            tmp = tmp.next;
        var g = tmp.data;
        tmp.data = element;
        return g;
    }


    @Override
    public boolean isEmpty() {
        return len == 0;
    }


    @Override
    public void clear() {
        len = 0;
        for (; tail != head; tail = tail.prev){
            tail.data = null;
            tail.next = null;
        }
        head.next = null;
    }

    @Override
    public int indexOf(Object o) {
        var tmp = head.next;
        for (int i = 0; i < len; i++)
            if (o.equals(tmp.data))
                return i;
            else
                tmp = tmp.next;
        return -1;
    }

    @Override
    public E get(int index) {
        var tmp = head.next;
        for (int i = 0; i < index; i++)
            tmp = tmp.next;
        return tmp.data;
    }

    @Override
    public boolean contains(Object o) {
        for(var tmp = head.next; tmp != null; tmp = tmp.next)
            if (o.equals(tmp.data))
                return true;
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        var tmp = tail;
        for (int i = len - 1; i >= 0; i--)
            if (o.equals(tmp.data))
                return i;
            else tmp = tmp.prev;
        return -1;
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
