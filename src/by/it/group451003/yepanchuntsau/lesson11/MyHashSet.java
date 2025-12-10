package by.it.group451003.yepanchuntsau.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static final class Node<E> {
        final int hash;
        final E key;
        Node<E> next;
        Node(int hash, E key, Node<E> next) {
            this.hash = hash; this.key = key; this.next = next;
        }
    }

    private Node<E>[] table;
    private int size;
    private int threshold;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        size = 0;
    }

    private static int spreadHash(Object key) {
        int h = (key == null) ? 0 : key.hashCode();
        return h ^ (h >>> 16); // как в JDK: чуть "распыляем" старшие биты
    }
    private static int indexFor(int hash, int len) {
        return hash & (len - 1); // при len = степень 2
    }
    @SuppressWarnings("unchecked")
    private void resizeIfNeeded(int needSize) {
        if (needSize <= threshold) return;
        int oldCap = table.length;
        int newCap = oldCap << 1;
        Node<E>[] newTab = (Node<E>[]) new Node[newCap];
        for (int i = 0; i < oldCap; i++) {
            Node<E> e = table[i];
            while (e != null) {
                Node<E> next = e.next;
                int idx = indexFor(e.hash, newCap);
                e.next = newTab[idx];
                newTab[idx] = e;
                e = next;
            }
        }
        table = newTab;
        threshold = (int) (newCap * LOAD_FACTOR);
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) table[i] = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        int h = spreadHash(e);
        resizeIfNeeded(size + 1);
        int idx = indexFor(h, table.length);
        for (Node<E> x = table[idx]; x != null; x = x.next) {
            if (x.hash == h && (x.key == e || (e != null && e.equals(x.key)))) {
                return false;
            }
        }
        table[idx] = new Node<>(h, e, table[idx]);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int h = spreadHash(o);
        int idx = indexFor(h, table.length);
        Node<E> prev = null;
        Node<E> x = table[idx];
        while (x != null) {
            if (x.hash == h && (x.key == o || (o != null && o.equals(x.key)))) {
                if (prev == null) table[idx] = x.next;
                else prev.next = x.next;
                x.next = null;
                size--;
                return true;
            }
            prev = x;
            x = x.next;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        int h = spreadHash(o);
        int idx = indexFor(h, table.length);
        for (Node<E> x = table[idx]; x != null; x = x.next) {
            if (x.hash == h && (x.key == o || (o != null && o.equals(x.key)))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;
        for (int i = 0; i < table.length; i++) {
            for (Node<E> x = table[i]; x != null; x = x.next) {
                if (!first) sb.append(", ");
                sb.append(x.key);
                first = false;
            }
        }
        sb.append(']');
        return sb.toString();
    }


    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean equals(Object o) { return super.equals(o); }
    @Override public int hashCode() { return super.hashCode(); }
}
