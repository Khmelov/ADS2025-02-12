package by.it.group451001.khokhlov.lesson11;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        final E value;
        Node<E> next;
        Node<E> before, after;
        Node(E value, Node<E> next) { this.value = value; this.next = next; }
    }

    private Node<E>[] table;
    private Node<E> head, tail;
    private int size;
    private boolean hasNull;
    private static final int DEFAULT_CAP = 16;
    private static final float LOAD_FACTOR = 0.75f;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int capacity) {
        if (capacity < 1) capacity = DEFAULT_CAP;
        table = (Node<E>[]) new Node[capacity];
        head = tail = null;
        size = 0;
        hasNull = false;
    }

    public MyLinkedHashSet() { this(DEFAULT_CAP); }

    private int cap() { return table.length; }

    private int indexFor(Object o, int len) {
        int h = (o == null) ? 0 : o.hashCode();
        return (h & 0x7fffffff) % len;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCap = cap() << 1;
        Node<E>[] newTable = (Node<E>[]) new Node[newCap];
        for (int i = 0; i < cap(); i++) {
            Node<E> n = table[i];
            while (n != null) {
                Node<E> next = n.next;
                int idx = indexFor(n.value, newCap);
                n.next = newTable[idx];
                newTable[idx] = n;
                n = next;
            }
        }
        table = newTable;
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() {
        for (int i = 0; i < cap(); i++) table[i] = null;
        head = tail = null;
        size = 0;
        hasNull = false;
    }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean add(E e) {
        if (e == null) {
            if (hasNull) return false;
            hasNull = true;
            linkNode(null);
            size++;
            return true;
        }
        int idx = indexFor(e, cap());
        for (Node<E> n = table[idx]; n != null; n = n.next)
            if (e.equals(n.value)) return false;
        Node<E> node = new Node<>(e, table[idx]);
        table[idx] = node;
        linkNode(node);
        size++;
        if (size > cap() * LOAD_FACTOR) resize();
        return true;
    }

    private void linkNode(Node<E> node) {
        if (node == null) {
            Node<E> fake = new Node<>(null, null);
            fake.before = tail;
            if (tail != null) tail.after = fake;
            tail = fake;
            if (head == null) head = fake;
            return;
        }
        node.before = tail;
        if (tail != null) tail.after = node;
        tail = node;
        if (head == null) head = node;
    }

    private void unlinkNode(Node<E> node) {
        if (node.before != null) node.before.after = node.after;
        else head = node.after;
        if (node.after != null) node.after.before = node.before;
        else tail = node.before;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            if (!hasNull) return false;
            hasNull = false;
            Node<E> cur = head;
            while (cur != null) {
                if (cur.value == null) { unlinkNode(cur); break; }
                cur = cur.after;
            }
            size--;
            return true;
        }
        int idx = indexFor(o, cap());
        Node<E> prev = null;
        Node<E> cur = table[idx];
        while (cur != null) {
            if (o.equals(cur.value)) {
                if (prev == null) table[idx] = cur.next;
                else prev.next = cur.next;
                unlinkNode(cur);
                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return hasNull;
        int idx = indexFor(o, cap());
        for (Node<E> n = table[idx]; n != null; n = n.next)
            if (o.equals(n.value)) return true;
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;
        Node<E> cur = head;
        while (cur != null) {
            if (!first) sb.append(", ");
            sb.append(String.valueOf(cur.value));
            first = false;
            cur = cur.after;
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        boolean mod = false;
        for (E e : c) if (add(e)) mod = true;
        return mod;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean mod = false;
        for (Object o : c) if (remove(o)) mod = true;
        return mod;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
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

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean equals(Object o) { return super.equals(o); }
    @Override public int hashCode() { return super.hashCode(); }
}