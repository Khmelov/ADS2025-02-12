package by.it.group410902.bobrovskaya.lesson10;


import java.util.*;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    private int size;
    private Object[] MyQueue;

    public MyPriorityQueue(){
        this.size = 10;
        this.MyQueue = new Object[this.size];
    }

    public String toString(){
        if(!this.isEmpty()){
            String val = "[";
            int size = this.size();
            for (int i = 0; i < size-1; i++) {
                if(this.MyQueue[i] != null)
                    val+=this.MyQueue[i].toString()+", ";
                else
                    val+="null, ";
            }
            if(this.MyQueue[size-1] != null){
                val+=this.MyQueue[size-1].toString()+"]";
            }else{
                val+="null]";
            }
            return val;
        }return "[]";
    }

    @Override
    public boolean add(E e) {
        try{
            int size = this.size();
            if(size == this.size) this.resize();
            this.MyQueue[size] = e;
            this.shiftUp(size);
            return true;
        }catch(IndexOutOfBoundsException ex){
            return false;
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if(c.isEmpty()) return false;
        int newSize = c.size(), size = this.size();
        while(size+newSize >= this.size) this.resize();
        for(E ele : c){
            this.MyQueue[size] = ele;
            this.shiftUp(size);
            size++;
        }
        return true;
    }

    @Override
    public E remove() {
        try{
            int size= this.size();
            E ret = (E) this.MyQueue[0];
            this.MyQueue[0] = this.MyQueue[size-1];
            this.MyQueue[size - 1] = null;
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
        Object[] new_arr = new Object[this.size];
        for (int i = 0; i < size; i++) {
            if (!c.contains(this.MyQueue[i])){
                new_arr[j] = this.MyQueue[i];
                j++;
            }else modified = true;
        }
        this.MyQueue = new_arr;
        for (int i = j/2-1; i >=0 ; i--) {
            this.shiftDown(i);
        }
        return modified;
    }

    @Override
    public E poll() {
        if(!this.isEmpty()) {
            int size = this.size();
            E ret = (E) this.MyQueue[0];
            this.MyQueue[0] = this.MyQueue[size-1];
            this.MyQueue[size-1] = null;
            this.shiftDown(0);
            return ret;
        }return null;
    }

    @Override
    public E element() {
        try{
            return (E) this.MyQueue[0];
        }catch (NoSuchElementException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public E peek() {
        if(!this.isEmpty()) {
            return (E) this.MyQueue[0];
        }return null;
    }

    @Override
    public boolean contains(Object o) {
        int size = this.size();
        for (int i = 0; i < size; i++) {
            if(o.equals(this.MyQueue[i])) return true;
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
                    if(this.MyQueue[i].equals(ele)){
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
        for (int i = 0; i < this.size; i++) {
            if(this.MyQueue[i] != null){
                size++;
            }
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        return this.MyQueue[0] == null;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if(c.isEmpty()){
            this.MyQueue = new Object[this.size];
            return true;
        }
        int size = this.size(), j = 0;
        for (int i = 0; i < size;i++) {
            if(c.contains(this.MyQueue[i])){
                this.MyQueue[j++] = this.MyQueue[i];
            }
        }
        Arrays.fill(this.MyQueue, j, size, null);
        for (int i = j/2-1; i >=0 ; i--) {
            this.shiftDown(i);
        }
        return j < size;

    }

    @Override
    public void clear() {
        Object[] clear_mass = new Object[this.size];
        this.MyQueue = clear_mass;
    }

    private void resize(){
        int size = this.size;
        this.size = (int) (this.size *1.5);
        Object[] temp = new Object[this.size];
        System.arraycopy(this.MyQueue,0,temp,0,size);
        this.MyQueue = temp;
    }

    private boolean isLess(Object f, Object s){
        E fir = (E) f;
        E sec = (E) s;
        return fir.compareTo(sec) < 0;
    }
    @Override
    public boolean offer(E e) {
        int size = this.size();
        if(size == this.size) this.resize();
        this.MyQueue[size] = e;
        this.shiftUp(size);
        return true;
    }

    //перемещение элемента вверх по куче
    public void shiftUp(int i){
        while(i > 0){
            int parent = (i-1)/2;
            if(isLess(this.MyQueue[i],this.MyQueue[parent])){
                Object temp = this.MyQueue[i];
                this.MyQueue[i] = this.MyQueue[parent];
                this.MyQueue[parent] = temp;
                i = parent;
            }else {
                break;
            }
        }
    }
    //перемещение элемента вниз по куче
    private void shiftDown(int i){
        int smallest = i;
        int left = 2*i+1;
        int right = 2*i+2;
        int size = this.size();

        if(left < size && isLess(this.MyQueue[left],this.MyQueue[smallest])) {
            smallest = left;
        }
        if(right < size && isLess(this.MyQueue[right],this.MyQueue[smallest])){
            smallest = right;
        }
        if(smallest != i){
            Object temp = this.MyQueue[smallest];
            this.MyQueue[smallest] = this.MyQueue[i];
            this.MyQueue[i] = temp;
            this.shiftDown(smallest);
        }
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
    public boolean remove(Object o) {
        return false;
    }


}

