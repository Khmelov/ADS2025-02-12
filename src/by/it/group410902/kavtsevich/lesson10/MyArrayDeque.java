package by.it.group410902.kavtsevich.lesson10;


import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyArrayDeque<E> implements Deque<E> { //  очередь
    private int sizeOfArr = 10;
    private Object[] elements_arrays;

    public MyArrayDeque(){ // Конструктор - создает массив начального размера
        elements_arrays = new Object[sizeOfArr];
    }


    public String toString(){
        int size = this.size();
        String ret_val="";
        if(size != 0) {
            ret_val += "[";
            for (int i = 0; i < size; i++) {
                ret_val += elements_arrays[i].toString();
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
            this.elements_arrays[size] = element;
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
            Object[] temp_ar = new Object[this.sizeOfArr];
            temp_ar[0] = element;
            System.arraycopy(this.elements_arrays, 0, temp_ar, 1, size);
            this.elements_arrays = temp_ar;
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
            this.elements_arrays[this.size()] = element;
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
            return (E)this.elements_arrays[0];
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
            return (E)this.elements_arrays[0];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public E getLast(){
        try{
            return (E) this.elements_arrays[this.size()-1];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public E poll(){  // Извлечение и удаление первого элемента
        int size = this.size();
        if(size == 0) return null;
        Object[] temp_ar = new Object[sizeOfArr];
        System.arraycopy(this.elements_arrays,1,temp_ar,0,size-1);
        E ret = (E)this.elements_arrays[0];
        this.elements_arrays = temp_ar;
        return ret;
    }

    public E pollFirst(){  // Извлечение и удаление первого элемента
        int size = this.size();
        if(size == 0) return null;
        Object[] temp_ar = new Object[sizeOfArr];
        System.arraycopy(this.elements_arrays,1,temp_ar,0,size-1);
        E ret = (E)this.elements_arrays[0];
        this.elements_arrays = temp_ar;
        return ret;
    }

    public E pollLast(){ // Извлечение и удаление второго элемента
        int size = this.size();
        if(size == 0) return null;
        E ret = (E) this.elements_arrays[size-1];
        this.elements_arrays[size-1] = null;
        return ret;
    }

    public void resize(){
        int size = sizeOfArr;
        this.sizeOfArr = (int)(this.sizeOfArr *1.5);
        Object[] new_el_arr = new Object[sizeOfArr];
        System.arraycopy(this.elements_arrays, 0, new_el_arr, 0, size);
        this.elements_arrays = new_el_arr;
    }

    private boolean is_need_to_resize(){
        return this.sizeOfArr == this.size();
    }

    public int size(){
        for (int i = 0; i < sizeOfArr; i++) {
            if(elements_arrays[i] == null){
                return i;
            }
        }
        return sizeOfArr;
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
