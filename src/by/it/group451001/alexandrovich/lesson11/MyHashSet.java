package by.it.group451001.alexandrovich.lesson11;

import java.util.*;

public class MyHashSet<E> implements Set<E> {

    class Node<E> {
        E data;
        Node next;
        Node(E e){
            data = e;
            next = null;
        }
    }

    int TABLE_SIZE = 256;

    int size = 0;

    Node[] arr = new Node[TABLE_SIZE];

    int Hash(E e){
        return e.hashCode() >>> 24;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < TABLE_SIZE; i++) {
            Node node = arr[i];
            while (node != null){
                sb.append(node.data.toString() + ", ");
                node = node.next;
            }
        }
        if (size > 0) {
            sb.deleteCharAt(sb.lastIndexOf(","));
            sb.deleteCharAt(sb.lastIndexOf(" "));
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
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        if (arr[Hash(e)] == null) arr[Hash(e)] = new Node(e);
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

    @Override
    public boolean remove(Object o) {
        E e = (E) o;
        if (arr[Hash(e)] == null) return false;
        else if (arr[Hash(e)].data.equals(e)) {
            arr[Hash(e)] = arr[Hash(e)].next;
        }
        else {
            Node node = arr[Hash(e)];
            while (!node.next.data.equals(e)) {
                node = node.next;
                if (node.next == null) return false;
            }
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
