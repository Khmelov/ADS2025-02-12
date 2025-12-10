package by.it.group451003.yepanchuntsau.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static final class Node<E> {
        final int hash;
        final E key;
        Node<E> next;
        Node<E> prevOrder, nextOrder;
        Node(int hash, E key, Node<E> next) {
            this.hash = hash; this.key = key; this.next = next;
        }
    }

    @SuppressWarnings("unchecked")
    private Node<E>[] table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
    private int size = 0;
    private int threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);

    private Node<E> headOrder = null;
    private Node<E> tailOrder = null;

    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;

    // ===== служебка =====
    private static int spreadHash(Object key) {
        int h = (key == null) ? 0 : key.hashCode();
        return h ^ (h >>> 16);
    }
    private static int indexFor(int hash, int len) { return hash & (len - 1); }

    @SuppressWarnings("unchecked")
    private void resizeIfNeeded(int needSize) {
        if (needSize <= threshold) return;
        int oldCap = table.length;
        int newCap = oldCap << 1;
        Node<E>[] newTab = (Node<E>[]) new Node[newCap];
        for (Node<E> x = headOrder; x != null; x = x.nextOrder) {
            x.next = null;
        }
        for (Node<E> x = headOrder; x != null; x = x.nextOrder) {
            int idx = indexFor(x.hash, newCap);
            x.next = newTab[idx];
            newTab[idx] = x;
        }
        table = newTab;
        threshold = (int) (newCap * LOAD_FACTOR);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;
        for (Node<E> x = headOrder; x != null; x = x.nextOrder) {
            if (!first) sb.append(", ");
            sb.append(x.key);
            first = false;
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) table[i] = null;
        headOrder = tailOrder = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean add(E e) {
        int h = spreadHash(e);
        resizeIfNeeded(size + 1);
        int idx = indexFor(h, table.length);

        for (Node<E> n = table[idx]; n != null; n = n.next) {
            if (n.hash == h && (n.key == e || (e != null && e.equals(n.key)))) {
                return false; // уже есть
            }
        }
        Node<E> newNode = new Node<>(h, e, table[idx]);
        table[idx] = newNode;

        if (tailOrder == null) {
            headOrder = tailOrder = newNode;
        } else {
            tailOrder.nextOrder = newNode;
            newNode.prevOrder = tailOrder;
            tailOrder = newNode;
        }

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (size == 0) return false;
        int h = spreadHash(o);
        int idx = indexFor(h, table.length);

        Node<E> prev = null;
        Node<E> cur = table[idx];
        while (cur != null) {
            if (cur.hash == h && (cur.key == o || (o != null && o.equals(cur.key)))) {
                if (prev == null) table[idx] = cur.next;
                else prev.next = cur.next;
                Node<E> po = cur.prevOrder, no = cur.nextOrder;
                if (po == null) headOrder = no; else po.nextOrder = no;
                if (no == null) tailOrder = po; else no.prevOrder = po;
                cur.next = cur.prevOrder = cur.nextOrder = null; // help GC
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
        int h = spreadHash(o);
        int idx = indexFor(h, table.length);
        for (Node<E> n = table[idx]; n != null; n = n.next) {
            if (n.hash == h && (n.key == o || (o != null && o.equals(n.key)))) return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object x : c) if (!contains(x)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E x : c) if (add(x)) changed = true;
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (isEmpty()) return false;
        boolean changed = false;
        for (Node<E> n = headOrder; n != null; ) {
            Node<E> next = n.nextOrder;
            if (c.contains(n.key)) { remove(n.key); changed = true; }
            n = next;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (isEmpty()) return false;
        boolean changed = false;
        for (Node<E> n = headOrder; n != null; ) {
            Node<E> next = n.nextOrder;
            if (!c.contains(n.key)) { remove(n.key); changed = true; }
            n = next;
        }
        return changed;
    }


    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean equals(Object o) { return super.equals(o); }
    @Override public int hashCode() { return super.hashCode(); }
}
