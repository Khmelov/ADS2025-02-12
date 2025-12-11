package by.it.group451001.alexandrovich.lesson11;

import java.util.*;

public class MyLinkedHashSet<E> implements Set<E> {
    class Node<E> {
        E data;
        Node next;
        Node ordNext;
        Node(E e){
            data = e;
            next = null;
            ordNext = null;
            if (last != null) last.ordNext = this;
            last = this;
        }
    }

    int TABLE_SIZE = 256;

    int size = 0;
    Node first = null;
    Node last = null;

    Node[] arr = new Node[TABLE_SIZE];

    int Hash(E e){
        return e.hashCode() >>> 24;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node node = first;
        while (node != last){
            sb.append(node.data.toString() + ", ");
            node = node.next;
        }
        if (node != null){
            sb.append(node.data.toString());
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
        Arrays.fill(arr, null);
        size = 0;
        first = null;
        last = null;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        if (arr[Hash(e)] == null) {
            arr[Hash(e)] = new Node(e);
            if (first == null) first = arr[Hash(e)];
        }
        else {
            Node node = arr[Hash(e)];
            if (node.data.equals(e)) return false;
            while (node.next != null) {
                node = node.next;
                if (node.data.equals(e)) return false;
            }
            node.next = new Node(e);
        }
        size++;
        return true;
    }

    void chainRemove(Node node, Node target){
        if (target == first) first = node.ordNext;
        else
        {
            while (node.ordNext != null && node.ordNext != target) {
                node = node.ordNext;
            }
            node.ordNext = node.ordNext.ordNext;
        }
        if (first == null) last = null;
        else if (node.ordNext == null) last = node;
    }

    @Override
    public boolean remove(Object o) {
        E e = (E) o;
        if (arr[Hash(e)] == null) return false;
        else if (arr[Hash(e)].data.equals(e)) {
            chainRemove(first, arr[Hash(e)]);
            arr[Hash(e)] = arr[Hash(e)].next;
        }
        else {
            Node node = arr[Hash(e)];
            while (!node.next.data.equals(e)) {
                node = node.next;
                if (node.next == null) return false;
            }
            chainRemove(node, node.next);
            node.next = node.next.next;
        }
        size--;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        E e = (E) o;
        Node node = arr[Hash(e)];
        if (node == null) return false;
        while (!node.data.equals(e)){
            node = node.next;
            if (node == null) return false;
        }
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object el : c){
            if (!contains(el)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean ch = false;
        for (E e : c){
            if (add(e)) ch = true;
        }
        return ch;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean ch = false;
        Node node = first;
        while (node != null)
        {
            if (!c.contains(node.data)) if(remove(node.data)) ch = true;
            node = node.ordNext;
        }
        return ch;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean ch = false;
        for (Object o : c){
            if (remove(o)) ch = true;
        }
        return ch;
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
