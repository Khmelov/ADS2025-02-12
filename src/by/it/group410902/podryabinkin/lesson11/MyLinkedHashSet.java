package by.it.group410902.podryabinkin.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    private static class Node<E> {
        E value;
        Node<E> next;   // для коллизий
        Node<E> before; // для порядка вставки
        Node<E> after;  // для порядка вставки

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }
    private Node last_node;
    private Node head;
    private Node<E>[] table;
    private int size;
    private int capacity;
    private static final int d_capacity = 16;

    public MyLinkedHashSet() {
        last_node = null;
        head = null;
        table = (Node<E>[]) new Node[d_capacity];
        capacity = d_capacity;
        size = 0;
    }

    private void h_resize(){


        if(size >= capacity - 2){

            int new_capacity = (int) (capacity * 1.5 + 3);
            E[] arr = (E[]) new Object[size + 2];
            Node cur = head;
            int c = 0;
            while (cur != null){
                arr[c] = (E) cur.value;
                cur = cur.after;
                c++;
            }
            head = null;
            last_node = null;
            capacity = new_capacity;
            table = (Node<E>[]) new Node[new_capacity];
            capacity = table.length;
            for(int i = 0; i < c; i++){
                rehashAdd(arr[i]);
            }

        }
        else if(size <= capacity / 1.5 - 2){
            int new_capacity = (int) (capacity / 1.5 + 3);
            E[] arr = (E[]) new Object[size + 2];
            Node cur = head;
            int c = 0;
            while (cur != null){
                arr[c] = (E) cur.value;
                cur = cur.after;
                c++;
            }
            head = null;
            last_node = null;
            capacity = new_capacity;
            table = (Node<E>[]) new Node[new_capacity];
            capacity = table.length;
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<E> cur = head;
        boolean first = true;
        while (cur != null) {
            if (!first) sb.append(", ");
            sb.append(cur.value);
            first = false;
            cur = cur.after;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        table = (Node<E>[]) new Node[d_capacity];
        capacity = d_capacity;
        while(head != null){
            Node<E> pr = head;
            head.before = null;
            head = head.after;
            pr.after = null;
        }
        head = null;
        last_node = null;
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
        if(last_node != null) last_node.after = newNode;
        newNode.before = last_node;
        last_node = newNode;
        if(head == null) head = newNode;
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
        newNode.before = last_node;
        if(last_node != null) last_node.after = newNode;
        last_node = newNode;
        if(head == null) head = newNode;
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


                if(cur_node.before != null){
                    cur_node.before.after = cur_node.after;
                }
                if(cur_node.after != null){
                    cur_node.after.before = cur_node.before;
                }
                if(cur_node == head){
                    head = cur_node.after;
                }
                if(cur_node == last_node){
                    last_node = cur_node.before;
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
        Boolean cont = true;
        for(Object a: c){
            if(a == null) break;
            if(!contains(a)) cont = false;
        }
        return cont;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Boolean changed = false;
        for (E a : c ) {
            if(add(a)){
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.after;
            if (!c.contains(cur.value)) {
                remove(cur.value);
                changed = true;
            }
            cur = next;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Boolean changed = false;
        for(Object a: c){
            if(remove(a)) changed = true;
        }
        return changed;
    }
}
