package by.it.group451001.puzik.lesson11;

import java.util.*;

public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        E key;
        Node<E> next;
        Node(E key, Node<E> next) { this.key = key; this.next = next; }
    }

    private Node<E>[] table;
    private int size;
    private int threshold;
    private static final float LOAD_FACTOR = 0.75f;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[16];
        threshold = (int)(table.length * LOAD_FACTOR);
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() { Arrays.fill(table, null); size = 0; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean add(E e) {
        if (contains(e)) return false;
        ensureCapacity(size + 1);
        int idx = index(e);
        table[idx] = new Node<>(e, table[idx]);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = index(o);
        Node<E> prev = null;
        for (Node<E> x = table[idx]; x != null; x = x.next) {
            if (Objects.equals(x.key, o)) {
                if (prev == null) table[idx] = x.next; else prev.next = x.next;
                x.next = null; x.key = null; // help GC
                size--;
                return true;
            }
            prev = x;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        int idx = index(o);
        for (Node<E> x = table[idx]; x != null; x = x.next) if (Objects.equals(x.key, o)) return true;
        return false;
    }

    // -------------- helpers --------------
    private int index(Object key) {
        int h = (key == null) ? 0 : key.hashCode();
        h ^= (h >>> 16);
        return (h & 0x7fffffff) % table.length;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity(int minSize) {
        if (minSize <= threshold) return;
        Node<E>[] old = table;
        Node<E>[] newTab = (Node<E>[]) new Node[old.length << 1];
        for (Node<E> head : old) {
            for (Node<E> x = head; x != null; ) {
                Node<E> next = x.next;
                int idx = (x.key == null ? 0 : (x.key.hashCode() ^ (x.key.hashCode() >>> 16))) & 0x7fffffff;
                idx %= newTab.length;
                x.next = newTab[idx];
                newTab[idx] = x;
                x = next;
            }
        }
        table = newTab;
        threshold = (int)(table.length * LOAD_FACTOR);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> bucket : table) {
            for (Node<E> x = bucket; x != null; x = x.next) {
                if (!first) sb.append(", ");
                sb.append(x.key);
                first = false;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // ---- Boilerplate unsupported ----
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { Object[] a=new Object[size]; int i=0; for(Node<E> b: table){ for(Node<E>x=b;x!=null;x=x.next) a[i++]=x.key;} return a; }
    @Override public <T> T[] toArray(T[] a) { if (a.length<size) a=Arrays.copyOf(a,size); int i=0; for(Node<E>b:table){ for(Node<E>x=b;x!=null;x=x.next){ @SuppressWarnings("unchecked") T v=(T)x.key; a[i++]=v; }} if(a.length>size) a[size]=null; return a; }
    @Override public boolean containsAll(Collection<?> c) { for (Object o:c) if(!contains(o)) return false; return true; }
    @Override public boolean addAll(Collection<? extends E> c) { boolean ch=false; for(E e:c) if(add(e)) ch=true; return ch; }
    @Override public boolean retainAll(Collection<?> c) { boolean changed=false; for(int i=0; i<table.length; i++){ Node<E> prev=null; Node<E> x=table[i]; while(x!=null){ if(!c.contains(x.key)){ if(prev==null) table[i]=x.next; else prev.next=x.next; size--; changed=true; x=x.next; } else { prev=x; x=x.next; } } } rehash(); return changed; }
    @Override public boolean removeAll(Collection<?> c) { boolean changed=false; for(Object o:c) if(remove(o)) changed=true; return changed; }
    private void rehash() { ensureCapacity(size+1); }
}


