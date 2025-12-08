package by.it.group410901.zaverach.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;


public class MyLinkedHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size;
    private int threshold;

    private Node<E> headOrder;
    private Node<E> tailOrder;

    private static class Node<E> {
        final E value;
        Node<E> bucketNext;
        Node<E> prevOrder;
        Node<E> nextOrder;

        Node(E value, Node<E> bucketNext) {
            this.value = value;
            this.bucketNext = bucketNext;
        }
    }

    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        headOrder = null;
        tailOrder = null;
    }

    private int indexFor(Object o) {
        int h = (o == null) ? 0 : o.hashCode();
        return (h & 0x7FFFFFFF) % table.length;
    }

    private void resizeIfNeeded() {
        if (size < threshold) return;

        int newCap = table.length * 2;
        Node<E>[] newTable = (Node<E>[]) new Node[newCap];

        for (Node<E> n = headOrder; n != null; n = n.nextOrder) {
            int idx = ((n.value == null) ? 0 : (n.value.hashCode() & 0x7FFFFFFF) % newCap);
            n.bucketNext = newTable[idx];
            newTable[idx] = n;
        }

        table = newTable;
        threshold = (int) (newCap * LOAD_FACTOR);
    }


    private boolean equalsValue(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    @Override
    public boolean add(E e) {
        int idx = indexFor(e);

        for (Node<E> n = table[idx]; n != null; n = n.bucketNext) {
            if (equalsValue(n.value, e)) return false;
        }

        Node<E> newNode = new Node<>(e, table[idx]);
        table[idx] = newNode;

        if (tailOrder == null) {
            headOrder = tailOrder = newNode;
        } else {
            tailOrder.nextOrder = newNode;
            newNode.prevOrder = tailOrder;
            tailOrder = newNode;
        }

        size++;
        resizeIfNeeded();
        return true;
    }

    @Override
    public boolean contains(Object o) {
        int idx = indexFor(o);
        for (Node<E> n = table[idx]; n != null; n = n.bucketNext) {
            if (equalsValue(n.value, o)) return true;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexFor(o);
        Node<E> prev = null;
        Node<E> cur = table[idx];

        while (cur != null) {
            if (equalsValue(cur.value, o)) {

                if (prev == null) table[idx] = cur.bucketNext;
                else prev.bucketNext = cur.bucketNext;

                Node<E> p = cur.prevOrder;
                Node<E> n = cur.nextOrder;
                if (p == null) headOrder = n;
                else p.nextOrder = n;
                if (n == null) tailOrder = p;
                else n.prevOrder = p;

                size--;
                return true;
            }
            prev = cur;
            cur = cur.bucketNext;
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) table[i] = null;
        size = 0;
        headOrder = tailOrder = null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<E> cur = headOrder;
        boolean first = true;
        while (cur != null) {
            if (!first) sb.append(", ");
            sb.append(cur.value);
            first = false;
            cur = cur.nextOrder;
        }
        sb.append("]");
        return sb.toString();
    }


    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            if (remove(o)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> cur = headOrder;
        while (cur != null) {
            Node<E> next = cur.nextOrder;
            if (!c.contains(cur.value)) {
                remove(cur.value);
                modified = true;
            }
            cur = next;
        }
        return modified;
    }

    // Необязательные методы
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean equals(Object o) { return super.equals(o); }
    @Override public int hashCode() { return super.hashCode(); }
}
