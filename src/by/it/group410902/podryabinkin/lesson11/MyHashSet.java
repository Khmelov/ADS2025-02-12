package by.it.group410902.podryabinkin.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size;
    private int capacity;
    private static final int d_capacity = 16;

    public MyHashSet() {
        table = (Node<E>[]) new Node[d_capacity];
        capacity = d_capacity;
        size = 0;
    }

    private void h_resize(){

        if(size >= capacity - 2){
            int cpacity_old = capacity;
            capacity = (int) (capacity * 1.5 + 1);
            E[] arr = (E[]) new Object[size + 3];
            Node cur = null;
            int c = 0;
            for(int i = 0; i < cpacity_old; i++){
                cur = table[i];
                while(cur != null){
                    arr[c] = (E) cur.value;
                    cur = cur.next;
                    c++;
                }

            }

            table = (Node<E>[]) new Node[capacity];
            for(int i = 0; i < c; i++){
                rehashAdd(arr[i]);
            }

        }
        else if(size <= capacity / 1.5 - 2){
            int cpacity_old = capacity;
            capacity = (int) (capacity / 1.5 + 1);
            E[] arr = (E[]) new Object[size + 3];
            Node cur = null;
            int c = 0;
            for(int i = 0; i < cpacity_old; i++){
                cur = table[i];
                while(cur != null){
                    arr[c] = (E) cur.value;
                    cur = cur.next;
                    c++;
                }

            }
            table = (Node<E>[]) new Node[capacity];
            for(int i = 0; i < c; i++){
                rehashAdd(arr[i]);
            }
        }

    }

    private int hash(Object key) {
        int h = key.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12);
        h ^= (h >>> 7) ^ (h >>> 4);
        return Math.abs(h % capacity);
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        table = (Node<E>[]) new Node[d_capacity];
        capacity = d_capacity;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    public void rehashAdd(E e) {
        int hash_code = hash(e);
        Node<E> cur_node = table[hash_code];
        Node<E> prev_node = null;

        while (cur_node != null) {
            if (cur_node.value.equals(e)) {
                return;
            }
            prev_node = cur_node;
            cur_node = cur_node.next;
        }

        Node<E> newNode = new Node<>(e, null);
        if (prev_node == null) {
            table[hash_code] = newNode;
        } else {
            prev_node.next = newNode;
        }
    }

    @Override
    public boolean add(E e) {
        if (size + 2 >= capacity) {
            h_resize();
        }
        int hash_code = hash(e);
        Node<E> cur_node = table[hash_code];
        Node<E> prev_node = null;

        while (cur_node != null) {
            if (cur_node.value.equals(e)) {
                return false;
            }
            prev_node = cur_node;
            cur_node = cur_node.next;
        }

        Node<E> newNode = new Node<>(e, null);
        if (prev_node == null) {
            table[hash_code] = newNode;
        } else {
            prev_node.next = newNode;
        }
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hash_code = hash(o);
        Node<E> cur_node = table[hash_code];
        Node<E> prev_node = null;

        while (cur_node != null) {
            if (cur_node.value.equals(o)) {
                if (prev_node == null) {
                    table[hash_code] = cur_node.next;
                } else {
                    prev_node.next = cur_node.next;
                }
                size--;

                if (size <= capacity / 1.5 - 2) {
                    h_resize();
                }

                return true;
            }
            prev_node = cur_node;
            cur_node = cur_node.next;
        }

        return false;
    }


    @Override
    public boolean contains(Object o) {
        int hash_code = hash(o);
        Node prev_node = null;
        Node cur_node = table[hash_code];
        while (cur_node != null){
            if(cur_node.value.equals(o)){
                return true;
            }
            prev_node = cur_node;
            cur_node = cur_node.next;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Node<E> bucket : table) {
            Node<E> cur = bucket;
            while (cur != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(cur.value);
                first = false;
                cur = cur.next;
            }
        }
        sb.append("]");
        return sb.toString();
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