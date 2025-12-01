package by.it.group410902.dziatko.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyArrayDeque<E> implements Deque<E> {
    private int size_of_arr = 10;
    private Object[] el_ar;

    public MyArrayDeque(){
        el_ar = new Object[size_of_arr];
    }


    public String toString(){
        int size = this.size();
        String ret_val="";
        if(size != 0) {
            ret_val += "[";
            for (int i = 0; i < size; i++) {
                ret_val += el_ar[i].toString();
                if (i != size - 1) {
                    ret_val += ", ";
                }
            }
        ret_val+="]";
        }
        return ret_val;
    }

    public boolean add(E element){
        try {
            int size = this.size();
            if (this.is_need_to_resize()) {
                this.resize();
            }
            this.el_ar[size] = element;
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void addFirst(E element){
        try {
            int size = this.size();
            if (this.is_need_to_resize()) {
                this.resize();
            }
            Object[] temp_ar = new Object[this.size_of_arr];
            temp_ar[0] = element;
            System.arraycopy(this.el_ar, 0, temp_ar, 1, size);
            this.el_ar = temp_ar;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addLast(E element){
        try{
            int size = this.size();
            if (this.is_need_to_resize()) {
                this.resize();
            }
            this.el_ar[this.size()] = element;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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


    public E element(){
        try{
            return (E)this.el_ar[0];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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


    public E getFirst(){
        try{
            return (E)this.el_ar[0];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public E getLast(){
        try{
            return (E) this.el_ar[this.size()-1];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public E poll(){
        int size = this.size();
        if(size == 0) return null;
        Object[] temp_ar = new Object[size_of_arr];
        System.arraycopy(this.el_ar,1,temp_ar,0,size-1);
        E ret = (E)this.el_ar[0];
        this.el_ar = temp_ar;
        return ret;
    }

    public E pollFirst(){
        int size = this.size();
        if(size == 0) return null;
        Object[] temp_ar = new Object[size_of_arr];
        System.arraycopy(this.el_ar,1,temp_ar,0,size-1);
        E ret = (E)this.el_ar[0];
        this.el_ar = temp_ar;
        return ret;
    }

    public E pollLast(){
        int size = this.size();
        if(size == 0) return null;
        E ret = (E) this.el_ar[size-1];
        this.el_ar[size-1] = null;
        return ret;
    }

    public void resize(){
        int size = size_of_arr;
        this.size_of_arr = (int)(this.size_of_arr*1.5);
        Object[] new_el_arr = new Object[size_of_arr];
        System.arraycopy(this.el_ar, 0, new_el_arr, 0, size);
        this.el_ar = new_el_arr;
    }

    private boolean is_need_to_resize(){
        return this.size_of_arr == this.size();
    }

    public int size(){
        for (int i = 0; i < size_of_arr; i++) {
            if(el_ar[i] == null){
                return i;
            }
        }
        return size_of_arr;
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
