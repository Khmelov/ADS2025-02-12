package by.it.group451001.strogonov.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    private void swap(E a, E b){
        var tmp = a;
        a = b;
        b = tmp;
    }

    private int len;
    private E[] arr;
    final int startLen = 8;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(){
        len = 0;
        arr = (E[]) new Comparable[startLen];
    }

    @SuppressWarnings("unchecked")
    private void extend_vir_len(){
        var new_arr = (E[]) new Comparable[arr.length << 1];
        System.arraycopy(arr, 0, new_arr, 0, arr.length);
        arr = new_arr;
    }

    private void siftDown(int i){
        while ((i << 1) + 1 < len){
            var left = (i << 1) + 1;
            var right = (i << 1) + 2;
            var j = left;
            if (right < len  && arr[right].compareTo(arr[left]) < 0)
                j = right;
            if (arr[i].compareTo(arr[j]) <= 0)
                break;
            var tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
            i = j;
        }
    }

    private void siftUp(int i){
        while (i != 0 && arr[i].compareTo(arr[(i - 1) >> 1]) < 0){
            var tmp = arr[i];
            arr[i] = arr[(i - 1) >> 1];
            arr[(i - 1) >> 1] = tmp;
            i = (i - 1) >> 1;
        }
    }





    @Override
    public boolean offer(E e) {
        if (e.equals(0))
        {
            var b = 0;
            var c = 0;
            b = b + c;
            b = b - c;
        }
        if (len == arr.length)
            extend_vir_len();
        arr[len++] = e;
        if (len != 1)
            siftUp(len - 1);
        return true;
    }

    @Override
    public E remove() {
        return poll();
    }



    @Override
    public E poll() {
        E min = arr[0];
        arr[0] = arr[len-- - 1];
        siftDown(0);
        return min;
    }




    @Override
    public String toString(){
        if (len == 0)
            return "[]";
        var S = new StringBuilder("[");
        for (int i = 0; i < len - 1; i++){
            S.append(arr[i]);
            S.append(", ");
        }
        S.append(arr[len - 1]);
        S.append("]");
        return S.toString();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int t = 0;
        for (int i = 0; i < len; i++){
            if (!c.contains(arr[i])){
                arr[t++] = arr[i];
            }
        }
        if (t == len)
            return false;
        for (int i = t; i < len; i++){
            arr[i] = null;
        }
        len = t;
        for (int i = t - 1; i >= 0; i--){
            if ((2 * i + 1 < t && arr[2 * i + 1] != null && arr[2 * i + 1].compareTo(arr[i]) < 0) || (2 * i + 2 < t && arr[2 * i + 2] != null && arr[2 * i + 2].compareTo(arr[i]) < 0))
                siftDown(i);
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int t = 0;
        for (int i = 0; i < len; i++){
            if (c.contains(arr[i])){
                arr[t++] = arr[i];
            }
        }
        if (t == len)
            return false;
        for (int i = t; i < len; i++){
            arr[i] = null;
        }
        len = t;
        for (int i = t - 1; i >= 0; i--){
            if ((2 * i + 1 < t && arr[2 * i + 1] != null && arr[2 * i + 1].compareTo(arr[i]) < 0) || (2 * i + 2 < t && arr[2 * i + 2] != null && arr[2 * i + 2].compareTo(arr[i]) < 0))
                siftDown(i);
        }
        return true;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < arr.length && arr[i] != null; i++)
            if (o.equals(arr[i]))
                return true;
        return false;
    }

    @Override
    public boolean add(E e) {
        return offer(e);
    }

    @Override
    public E element() {
        return arr[0];
    }

    @Override
    public E peek() {
        return arr[0];
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        boolean res = true;
        for (var i : c)
            res &= contains(i);
        return res;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty())
            return false;
        for (var i : c)
            offer(i);
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clear() {
        len = 0;
        arr = (E[]) new Comparable[startLen];
    }
    /// /////////////////////////////////////////////////////////////////////////////////////////////////////



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





    @Override
    public int size() {
        return len;
    }

    @Override
    public boolean isEmpty() {
        return len == 0;
    }
}
