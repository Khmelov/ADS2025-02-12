package by.it.group410902.podryabinkin.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

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
        if(index == 0){
            E cur_v = first.data;
            first = first.next;
            return cur_v;
        }
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
                else{
                    prev_node.next = null;
                    last = prev_node;
                    return cur_node.data;
                }
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
            size += 1;
        }
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }

        Node<E> newNode = new Node<>();
        newNode.data = element;

        if (index == 0) { // вставка в начало
            newNode.next = first;
            first = newNode;
            if (last == null) last = newNode; // если список был пуст
            return;
        }

        Node<E> prev = first;
        for (int i = 0; i < index - 1; i++) {
            prev = prev.next;
        }
        newNode.next = prev.next;
        prev.next = newNode;

        if (newNode.next == null) last = newNode; // если вставка в конец
    }

    @Override
    public boolean remove(Object o) {
        if(indexOf(o) == -1) return false;
        remove(indexOf(o));
        return true;
    }


    @Override
    public E set(int index, E element) {
        Node<E> cur = first;
        int ind  = 0;
        while(cur != null){
            if(ind == index){
                E prev_dat = cur.data;
                cur.data = element;
                return prev_dat;
            }
            ind++;
            cur = cur.next;
        }
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
        Node<E> cur = first;
        int ind  = 0;
        while(cur != null){
            if(o == null ? cur.data == null : o.equals(cur.data)) return ind;
            ind++;
            cur = cur.next;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        Node<E> cur = first;
        int ind  = 0;
        while(cur != null){
            if(ind == index) return cur.data;
            ind++;
            cur = cur.next;
        }
        return null;
    }

    @Override
    public boolean contains(Object o) {
        Node<E> cur = first;
        while(cur != null){
            if(o == null ? cur.data == null : o.equals(cur.data)) return true;
            cur = cur.next;
        }
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        Node<E> cur = first;
        int ind  = 0;
        int res = -1;
        while(cur != null){
            if(o == null ? cur.data == null : o.equals(cur.data)) res = ind;
            ind++;
            cur = cur.next;
        }
        return res;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        int n = c.size();
        Iterator<?> iter_c = c.iterator();
        while (iter_c.hasNext()){
            if(contains(iter_c.next())){
                continue;
            }
            else return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) return false;
        for (E element : c) {
            add(element);
        }
        return true;
    }


    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (c.isEmpty()) return false;
        for( E el : c){
            add(index, el);
            index++;
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c.isEmpty()) return false;

        boolean modified = false;
        Node<E> prev = null;
        Node<E> cur = first;

        while (cur != null) {
            if (c.contains(cur.data)) { // если элемент есть в коллекции — удаляем
                if (prev == null) { // удаляем первый
                    first = cur.next;
                    if (cur == last) last = null;
                } else {
                    prev.next = cur.next;
                    if (cur == last) last = prev; // если удаляем последний
                }
                modified = true;
            } else {
                prev = cur;
            }
            cur = cur.next;
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (isEmpty()) return false;

        boolean modified = false;
        Node<E> prev = null;
        Node<E> cur = first;

        while (cur != null) {
            if (!c.contains(cur.data)) { // если элемента нет в коллекции — удаляем
                if (prev == null) {
                    first = cur.next;
                    if (cur == last) last = null;
                } else {
                    prev.next = cur.next;
                    if (cur == last) last = prev;
                }
                modified = true;
            } else {
                prev = cur;
            }
            cur = cur.next;
        }

        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

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
