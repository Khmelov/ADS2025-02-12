package by.it.group410902.dziatko.lesson10;

import java.util.*;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    private int size_of_array;
    private Object[] el_ar;

    public MyPriorityQueue(){
        this.size_of_array = 10;
        this.el_ar = new Object[this.size_of_array];
    }

    public void shiftUp(int i){
        while(i > 0){
            int parent = (i-1)/2;
            if(isLess(this.el_ar[i],this.el_ar[parent])){
                Object temp = this.el_ar[i];
                this.el_ar[i] = this.el_ar[parent];
                this.el_ar[parent] = temp;
                i = parent;
            }else {
                break;
            }
        }
    }

    private void shiftDown(int i){
        int smallest = i;
        int left_ch = 2*i+1;
        int right_ch = 2*i+2;
        int size = this.size();

        if(left_ch < size && isLess(this.el_ar[left_ch],this.el_ar[smallest])) {
            smallest = left_ch;
        }
        if(right_ch < size && isLess(this.el_ar[right_ch],this.el_ar[smallest])){
            smallest = right_ch;
        }
        if(smallest != i){
            Object temp = this.el_ar[smallest];
            this.el_ar[smallest] = this.el_ar[i];
            this.el_ar[i] = temp;
            this.shiftDown(smallest);
        }
    }



    public String toString(){
        if(!this.isEmpty()){
            String ret_val = "[";
            int size = this.size();
            for (int i = 0; i < size-1; i++) {
                if(this.el_ar[i] != null)
                    ret_val+=this.el_ar[i].toString()+", ";
                else
                    ret_val+="null, ";
            }
            if(this.el_ar[size-1] != null){
                ret_val+=this.el_ar[size-1].toString()+"]";
            }else{
                ret_val+="null]";
            }
            return ret_val;
        }return "[]";
    }

    @Override
    public boolean offer(E e) {
        int size = this.size();
        if(size == this.size_of_array) this.resize();
        this.el_ar[size] = e;
        this.shiftUp(size);
        return true;
    }

    @Override
    public boolean add(E e) {
        try{
            int size = this.size();
            if(size == this.size_of_array) this.resize();
            this.el_ar[size] = e;
            this.shiftUp(size);
            return true;
        }catch(IndexOutOfBoundsException ex){
            return false;
        }
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if(c.isEmpty()) return false;
        int new_si = c.size(), size = this.size();
        while(size+new_si >= this.size_of_array) this.resize();
        for(E ele : c){
            this.el_ar[size] = ele;
            this.shiftUp(size);
            size++;
        }
        return true;
    }


    

    @Override
    public E remove() {
       try{
           int size= this.size();
            E ret = (E) this.el_ar[0];
            this.el_ar[0] = this.el_ar[size-1];
            this.el_ar[size - 1] = null;
            this.shiftDown(0);
            return ret;
        } catch (NoSuchElementException e) {
           throw new RuntimeException(e);
       }

    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int size = this.size(),j=0;
        boolean modified  =false;
        Object[] new_arr = new Object[this.size_of_array];
        for (int i = 0; i < size; i++) {
            if (!c.contains(this.el_ar[i])){
                new_arr[j] = this.el_ar[i];
                j++;
            }else modified = true;
        }
        this.el_ar = new_arr;
        for (int i = j/2-1; i >=0 ; i--) {
            this.shiftDown(i);
        }
        return modified;
    }



    @Override
    public E poll() {
    if(!this.isEmpty()) {
        int size = this.size();
        E ret = (E) this.el_ar[0];
        this.el_ar[0] = this.el_ar[size-1];
        this.el_ar[size-1] = null;
        this.shiftDown(0);
        return ret;
    }return null;
    }

    @Override
    public E element() {
        try{
            return (E) this.el_ar[0];
        }catch (NoSuchElementException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public E peek() {
        if(!this.isEmpty()) {
            return (E) this.el_ar[0];
        }return null;
    }



    @Override
    public boolean contains(Object o) {
        int size = this.size();
        for (int i = 0; i < size; i++) {
            if(o.equals(this.el_ar[i])) return true;
        }
        return false;
    }


    @Override
    public boolean containsAll(Collection<?> c) {
        if(c.isEmpty()) return true;
        if(!this.isEmpty()){
            int size = this.size();
            for(Object ele : c){
                boolean contains = false;
                for (int i = 0; i < size; i++) {
                    if(this.el_ar[i].equals(ele)){
                        contains = true;
                        break;
                    }
                }
                if(!contains) return false;
            }
            return true;
        }
        return false;
    }




    @Override
    public int size() {
        int size = 0;
        for (int i = 0; i < this.size_of_array; i++) {
            if(this.el_ar[i] != null){
                size++;
            }
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        return this.el_ar[0] == null;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if(c.isEmpty()){
            this.el_ar = new Object[this.size_of_array];
            return true;
        }
            int size = this.size(), j = 0;
            for (int i = 0; i < size;i++) {
                if(c.contains(this.el_ar[i])){
                    this.el_ar[j++] = this.el_ar[i];
                }
            }
                Arrays.fill(this.el_ar, j, size, null);
                for (int i = j/2-1; i >=0 ; i--) {
                    this.shiftDown(i);
                }
            return j < size;

    }

    @Override
    public void clear() {
        Object[] clear_mass = new Object[this.size_of_array];
        this.el_ar = clear_mass;
    }

    private void resize(){
        int size = this.size_of_array;
        this.size_of_array = (int) (this.size_of_array*1.5);
        Object[] temp = new Object[this.size_of_array];
        System.arraycopy(this.el_ar,0,temp,0,size);
        this.el_ar = temp;
    }

    private boolean isLess(Object f, Object s){
        E fir = (E) f;
        E sec = (E) s;
        return fir.compareTo(sec) < 0;
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
}
