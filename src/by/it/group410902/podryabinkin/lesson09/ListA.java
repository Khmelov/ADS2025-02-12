package by.it.group410902.podryabinkin.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {

    private class Node<E> {
        E data;
        Node<E> next;

        public boolean has_n(){
            if(next == null) return false;
            else return true;
        }
    }
    Node<E> first;
    Node<E> last;

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        String res = "[";
        if(isEmpty()) return "[]";
        Iterator<E> iter = iterator();

        while (iter.hasNext()){
            res += iter.next() + ", ";
        }
        res += iter.next() + "]";
        return res;
    }

    @Override
    public boolean add(E e) {
        if(isEmpty()){
            first = new Node<E>();
            first.data = e;
            last = first;
        }
        else{
            Node<E> cur = new Node<E>();
            cur.data = e;
            last.next = cur;
            last = cur;
        }
        return true;
    }

    @Override
    public E remove(int index) {
        int cur = 0;
        boolean next_ex = true;
        Node<E> cur_node = first;
        Node<E> prev_node = first;
        Node<E> next_node = null;
        if(first.has_n()) next_node = first.next;
        while(cur != index && cur_node != null){
            prev_node = cur_node;
            cur_node = next_node;
            cur++;

            if(next_node.has_n()) next_node = next_node.next;
            else next_ex = false;
            if(cur == index){
                if(next_ex){
                    prev_node.next = next_node;
                }
                else prev_node.next = null;
            }
        }
        return cur_node.data;
    }

    @Override
    public int size() {
        int size = 0;
        Iterator<E> iter = iterator();
        if(isEmpty()) return 0;
        else{
            while(iter.hasNext()){
                size++;
                iter.next();
            }
            size++;
        }
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
        return first == null;
    }


    @Override
    public void clear() {
        first = null;
        last = null;

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
        return new Iterator<E>() {
            private Node<E> current = first; // текущая позиция

            @Override
            public boolean hasNext() {
                if(current != null){
                    if(current.next != null) return true;
                }
                return false;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    return  current.data;
                }
                E data = current.data;
                current = current.next;
                return data;
            }
        };
    }

}
