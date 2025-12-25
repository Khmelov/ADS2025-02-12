package by.it.group451002.dirko.lesson11;

import java.util.*;

public class MyHashSet<E> implements Set<E> {
    private Node[] elements = new Node[10000];
    private int size;

    private int getHash(Object elem){
        return (int) elem % elements.length;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < elements.length; i++) {
            Node elem = elements[i];
            while (elem != null) {
                sb.append(elem.item).append(", ");
                elem = elem.next;
            }
        }
        if (sb.length() > 1) sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean add(E e) {
        if (contains(e)) return false;
        size++;
        int ind = getHash(e);
        Node elem = elements[ind];
        if (elem == null) {
            elements[ind] = new Node(e, null);
            return true;
        }
        while (elem.next != null) { elem = elem.next; }
        elem.next = new Node(e, null);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (!contains(o)) return false;
        size--;
        int ind = getHash(o);
        Node elem = elements[ind];
        if (Objects.equals(elem.item, o)) {
            elements[ind] = elements[ind].next;
            return true;
        }
        while (!Objects.equals(elem.next.item, o)) elem = elem.next;
        elem.next = elem.next.next;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        int ind = getHash(o);
        Node elem = elements[ind];
        while (elem != null) {
            if (Objects.equals(elem.item, o)) return true;
            elem = elem.next;
        }
        return false;
    }

    @Override
    public void clear() {
        elements = new Node[10000];
        size = 0;
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
