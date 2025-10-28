package by.it.group451001.shymko.lesson10;

import java.util.*;
//import java.util.PriorityQueue;




public class MyPriorityQueue<E> implements Queue<E> {

    private  Object[] elements;
    private int size;
    final private int baseCapacity = 8;
    private final Comparator<? super E> comparator;


    public MyPriorityQueue(Comparator<? super E> comparator) {
        elements = new Object[baseCapacity];
        size = 0;
        this.comparator = comparator;
    }
    public MyPriorityQueue() {

        this(null);
    }


    private void resize(int newCapacity) {
        Object[] newElements = new Object[newCapacity];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public String toString(){
        String s = "[";
        if(size == 0){
            return s + ']';
        }
        s += elements[0].toString();
        for(int i = 1; i < size; i++){
            s += ", " + elements[i].toString();
        }
        return s + "]";
    }

    public boolean contains(Object o) {
        for(int i = 0; i < size; i++){
            if(elements[i].equals(o)){
                return true;
            }
        }
        return false;
    }

    public Iterator<E> iterator() {
        return null;
    }

    public Object[] toArray() {
        return new Object[0];
    }

    public <T> T[] toArray(T[] a) {
        return null;
    }

    private void siftUp(int k){
        int i = k;
        while(i > 0 && ((Comparable<? super E>) elements[i]).compareTo((E)elements[(i - 1) >> 1]) < 0){
            E temp = (E)elements[i];
            elements[i] = elements[(i - 1) >> 1];
            elements[(i - 1) >> 1] = temp;
            i = (i - 1) >> 1;
        }
    }

    public boolean add(E e) {
        if(size == elements.length){
            resize(elements.length << 1);
        }
        elements[size++] = e;
        siftUp(size - 1);
        return true;
    }

    private void siftDown(int k){
        int half = size >> 1;
        E x = (E)elements[k];
        Comparable<? super E> key = (Comparable<? super E>) x;
        while(k < half){
            int child = (k << 1) + 1;
            Object c = elements[child];
            int right = child + 1;
            if(right < size){
                if(((Comparable<? super E>) c).compareTo((E)elements[right]) > 0) {
                    c = elements[child = right];
                }
            }
            if(key.compareTo((E)c) <= 0){
                break;
            }
            elements[k] = c;
            k = child;
            elements[k] = key;
        }
    }

    public boolean remove(Object o) {
        for(int i = 0; i < size; i++){
            if(elements[i].equals(o)){
                elements[i] = elements[size - 1];
                elements[size - 1] = null;
                size--;
                siftDown(i);
                return true;
            }
        }
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        for(var el: c){
            if(!contains(el)){
                return false;
            }
        }
        return true;
    }

    public boolean addAll(Collection<? extends E> c) {
        if(c.isEmpty()){
            return false;
        }
        if(size + c.size() > elements.length){
            resize(size + c.size());
        }
        for(var el: c){
            add(el);
        }
        return true;
    }

    private void heapify(){
        for(int i = (size >> 1) - 1; i >= 0; i--){
            siftDown(i);
        }
    }

    private long[] nBits(int n) {
        return new long[((n - 1) >> 6) + 1];
    }

    private void setBit(long[] bits, int i) {
        bits[i >> 6] |= 1L << i;
    }
    private boolean isClear(long[] bits, int i) {
        return (bits[i >> 6] & (1L << i)) == 0;
    }

    public boolean removeAll(Collection<?> c) {
        int i;
        for(i = 0; i < size && !c.contains(elements[i]); ++i)
            ;
        if(i == size){
            return false;
        }
        int st = i;
        final long[] deathRow = nBits(size - i);
        setBit(deathRow, 0);
        for(i = st + 1; i < size; ++i){
            if(c.contains(elements[i])){
                setBit(deathRow, i - st);
            }
        }
        int w = st;
        for(i = st; i < size; ++i){
            if(isClear(deathRow, i - st)){
                elements[w++] = elements[i];
            }
        }
        int end = size;
        size = w;
        for(i = w; i < end; ++i){
            elements[i] = null;
        }
        heapify();
        return true;
    }

    public boolean retainAll(Collection<?> c) {
        int i;
        for(i = 0; i < size && c.contains(elements[i]); ++i)
            ;
        if(i == size){
            return false;
        }
        int st = i;
        final long[] deathRow = nBits(size - i);
        setBit(deathRow, 0);
        for(i = st + 1; i < size; ++i){
            if(!c.contains(elements[i])){
                setBit(deathRow, i - st);
            }
        }
        int w = st;
        for(i = st; i < size; ++i){
            if(isClear(deathRow, i - st)){
                elements[w++] = elements[i];
            }
        }
        int end = size;
        size = w;
        for(i = w; i < end; ++i){
            elements[i] = null;
        }
        heapify();
        return true;
    }

    public void clear() {
        for(int i = 0; i < size; i++){
            elements[i] = null;
        }
        size = 0;
    }

    public boolean offer(E e) {
        return add(e);
    }

    public E remove() {
        E old = (E)elements[0];
        elements[0] = elements[size - 1];
        elements[size - 1] = null;
        size--;
        siftDown(0);
        return old;
    }

    public E poll() {
        E e = (E)elements[0];
        elements[0] = elements[size - 1];
        elements[size - 1] = null;
        size--;
        siftDown(0);
        return e;
    }

    public E element() {
        return peek();
    }

    public E peek() {
        return (E) elements[0];
    }
}
