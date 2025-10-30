package by.it.group410902.bobrovskaya.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyLinkedList<E> implements Deque<E>{

    Linked_list_Note<E> head;
    Linked_list_Note<E> tail;

    public MyLinkedList(){
        this.head = null;
        this.tail = null;
    }

    public String toString(){
        String exp = "";
        if(this.head != null){
            exp+="[";
            Linked_list_Note<E> temp = this.head;
            int size = this.size();
            for (int i = 0; i < size-1; i++){
                exp += temp.value.toString();
                exp += ", ";
                temp = temp.next;
            }
            exp+=this.tail.value.toString() + "]";
        }
        return exp;
    }

    @Override
    public boolean add(E e) {
        if(this.head == null){
            this.head = new Linked_list_Note<>(e);
            this.tail = this.head;
        }else{
            this.tail.next = new Linked_list_Note<>(e,null,this.tail);
            this.tail = this.tail.next;
        }
        return true;
    }

    @Override
    public void addFirst(E e) {
        if(this.head == null){
            this.head = new Linked_list_Note<>(e);
            this.tail = this.head;
        } else {
            this.head.behind = new Linked_list_Note<>(e,this.head,null);
            this.head = this.head.behind;
        }
    }

    @Override
    public void addLast(E e) {
        if(this.tail == null){
            this.head = new Linked_list_Note<>(e);
            this.tail = this.head;
        }else{
            this.tail.next = new Linked_list_Note<>(e,null,this.tail);
            this.tail = this.tail.next;
        }
    }

//удалить первый элемент из списка и вернуть его значение
    @Override
    public E poll() {
        if(this.head != null) {
            Linked_list_Note<E> val = this.head;
            if(val.next != null) {
                this.head.next.behind = null;
                this.head = this.head.next;
            }else{
                this.head = null;
                this.tail = null;
            }
            return val.value;
        }
        return null;
    }

    @Override
    public E pollFirst() {
        if(this.head != null) {
            Linked_list_Note<E> re_val = this.head;
            if(re_val.next != null) {
                this.head.next.behind = null;
                this.head = this.head.next;
            }else{
                this.head = null;
                this.tail = null;
            }
            return re_val.value;
        }
        return null;
    }

    @Override
    public E pollLast() {
        if(this.head != null) {
            Linked_list_Note<E> re_val = this.tail;
            if(re_val.behind != null) {
                this.tail.behind.next = null;
                this.tail = this.tail.behind;
            }else{
                this.head = null;
                this.tail = null;
            }
            return re_val.value;
        }
        return null;
    }


    @Override
    public E getFirst() {
        if(this.head != null) {
            return this.head.value;
        }return null;
    }

    @Override
    public E getLast() {
        if(this.tail != null) {
            return this.tail.value;
        }return null;
    }

    @Override
    public E element() {
        if(this.head != null) {
            return this.head.value;
        }return null;
    }
    //удаление по индексу
    public E remove(int ind) {
        int size = this.size();
        if(this.head != null && ind < size && ind >=0){
            if(ind == 0){
                this.head.next.behind = null;
                E ret_val = this.head.value;
                this.head = this.head.next;
                return ret_val;
            }else if(ind == size-1){
                this.tail.behind.next = null;
                E ret_val = this.tail.value;
                this.tail = this.tail.behind;
                return ret_val;
            }
            Linked_list_Note<E> temp = this.head;
            for (int i = 0; i < ind; i++) {
                temp = temp.next;
            }
            temp.behind.next = temp.next;
            temp.next.behind = temp.behind;
            return temp.value;
        }
        return null;
    }
//удаление по элементу
    @Override
    public boolean remove(Object o) {
        if(this.head != null){
            if(o.equals(this.head.value)){
                this.head.next.behind = null;
                this.head = this.head.next;
                return true;
            }else if(o.equals(this.tail.value)){
                this.tail.behind.next = null;
                this.tail = this.tail.behind;
                return true;
            }
            Linked_list_Note<E> temp = this.head.next;
            while(temp.next.next != null && !o.equals(temp.value)){
                temp = temp.next;
            }
            if(!temp.value.equals(o)){
                return false;
            }
            temp.next.behind = temp.behind;
            temp.behind.next = temp.next;
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        if(this.head != null){
            int size = 1;
            Linked_list_Note<E> temp = this.head;
            while(temp.next != null){
                size++;
                temp = temp.next;
            }
            return size;
        }
        return 0;
    }







    @Override
    public E remove(){
        return null;
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
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
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
}

class Linked_list_Note<E>{
    E value = null;
    Linked_list_Note<E> next = null;
    Linked_list_Note<E> behind = null;

    Linked_list_Note(E val){
        this.value = val;
    }

    Linked_list_Note(E val,Linked_list_Note<E> next,Linked_list_Note<E> behind){
        this.value = val;
        this.next = next;
        this.behind = behind;
    }

}

