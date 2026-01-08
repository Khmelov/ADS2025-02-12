package by.it.group451002.spizharnaya.lesson11;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private class List_Item<E> {
        E data;
        List_Item<E> next;

        List_Item(){
            this.data = null;
            this.next = null;
        }

        List_Item(E data){
            this.data = data;
            this.next = null;
        }

        List_Item(E data, List_Item<E> next){
            this.data = data;
            this.next = next;
        }
    }

    private int arr_size = 25;
    private int n_items = 0;
    private List_Item[] arr; //array of list headers

    public MyHashSet() {
        arr = new List_Item[arr_size];
        for (int i=0; i<arr_size; i++)
            arr[i] = new List_Item();
    }

    @Override
    public String toString(){
        if (n_items == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int i=0; i<arr_size; i++){
            List_Item curr = arr[i].next;
            while (curr!=null){
                sb.append(curr.data);
                sb.append(", ");
                curr = curr.next;
            }
        }
        sb.delete(sb.length()-2,sb.length());
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return n_items;
    }

    // clear the list (except of the first element)
    private void clear_rec(List_Item<E> curr){
        if (curr == null) return;
        clear_rec(curr.next);
        curr.next = null;
    }

    @Override
    public void clear() {
        for (int i=0; i<arr_size; i++)
            clear_rec(arr[i]); //clear the list
        n_items = 0;
    }

    @Override
    public boolean isEmpty() {
        return n_items == 0;
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) return false;

        int hash = e.hashCode()%arr_size;
        List_Item curr = new List_Item(e);
        curr.next = arr[hash].next;
        arr[hash].next = curr;
        n_items++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hash = o.hashCode()%arr_size;

        List_Item curr = arr[hash];
        while (curr.next != null){
            if (curr.next.data.equals(o)){
                curr.next = curr.next.next;
                n_items--;
                return true;
            }
            curr = curr.next;
        }

        return false;
    }

    @Override
    public boolean contains(Object o) {
        int hash = o.hashCode()%arr_size;

        List_Item curr = arr[hash].next;
        while (curr != null){
            if (curr.data.equals(o))
                return true;
            curr = curr.next;
        }

        return false;
    }

    //---------------------------------------------------------------------------------------------

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
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }
}
