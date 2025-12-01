package by.it.group410902.bobrovskaya.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyArrayDeque<E> implements Deque<E> {
    private int size = 10;
    private Object[] MyArray;

    public MyArrayDeque(){
        MyArray = new Object[size];
    }

    public int size(){
        for (int i = 0; i < size; i++) {
            if(MyArray[i] == null){
                return i;
            }
        }
        return size;
    }

    public void resize(){
        int size = this.size;
        this.size = (int)(this.size *1.5);
        Object[] new_el_arr = new Object[this.size];
        System.arraycopy(this.MyArray, 0, new_el_arr, 0, size);
        this.MyArray = new_el_arr;
    }

    public String toString(){
        int size = this.size();
        String exp = "";
        if(size != 0) {
            exp += "[";
            for (int i = 0; i < size; i++) {
                exp += MyArray[i].toString();
                if (i != size - 1) {
                    exp += ", ";
                }
            }
            exp+= "]";
        }
        return exp;
    }

    public boolean add(E element){
        try {
            int size = this.size();
            if (this.isNeedToResize()) {
                this.resize();
            }
            this.MyArray[size] = element;
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addFirst(E element){
        try {
            int size = this.size();
            if (this.isNeedToResize()) {
                this.resize();
            }
            Object[] tempArray = new Object[this.size];
            tempArray[0] = element;
            System.arraycopy(this.MyArray, 0, tempArray, 1, size);
            this.MyArray = tempArray;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addLast(E element){
        try{
            int size = this.size();
            if (this.isNeedToResize()) {
                this.resize();
            }
            this.MyArray[this.size()] = element;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public E getFirst(){
        try{
            return (E)this.MyArray[0];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public E getLast(){
        try{
            return (E) this.MyArray[this.size()-1];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
//удаляет и возвращает элемент из начала
    public E poll(){
        int size = this.size();
        if(size == 0) return null;
        Object[] temp_ar = new Object[this.size];
        System.arraycopy(this.MyArray,1,temp_ar,0,size-1);
        E ret = (E)this.MyArray[0];
        this.MyArray = temp_ar;
        return ret;
    }
//удаляет и возвращает последний элемент
    public E pollLast(){
        int size = this.size();
        if(size == 0) return null;
        E ret = (E) this.MyArray[size-1];
        this.MyArray[size-1] = null;
        return ret;
    }

    private boolean isNeedToResize(){
        return this.size == this.size();
    }

    public E element(){
        try{
            return (E)this.MyArray[0];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public E pollFirst(){
        int size = this.size();
        if(size == 0) return null;
        Object[] temp_ar = new Object[this.size];
        System.arraycopy(this.MyArray,1,temp_ar,0,size-1);
        E ret = (E)this.MyArray[0];
        this.MyArray = temp_ar;
        return ret;
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

}

