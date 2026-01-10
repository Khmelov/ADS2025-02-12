package by.it.group451004.maximenko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private static class HashNode<E> {
        E value;
        HashNode<E> next;
        LinkedNode<E> linkedNode;

        HashNode(E value, HashNode<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private static class LinkedNode<E> {
        E value;
        LinkedNode<E> prev;
        LinkedNode<E> next;

        LinkedNode(E value) {
            this.value = value;
        }
    }

    private HashNode<E>[] table;
    private int size;
    private final float loadFactor;

    private LinkedNode<E> head;
    private LinkedNode<E> tail;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int initialCapacity, float loadFactor) {
        table = (HashNode<E>[]) new HashNode[initialCapacity];
        this.loadFactor = loadFactor;
        size = 0;
        head = tail = null;
    }

    public MyLinkedHashSet(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public MyLinkedHashSet() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }


    private int indexFor(Object o, HashNode<E>[] tab) {
        int h = (o == null) ? 0 : o.hashCode();
        int positive = h & 0x7fffffff;
        return positive % tab.length;
    }

    private boolean equalsElement(Object a, Object b) {
        if (a == b) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }

    private boolean needsResize() {
        return size + 1 > table.length * loadFactor;
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        HashNode<E>[] oldTable = table;
        HashNode<E>[] newTable = (HashNode<E>[]) new HashNode[newCapacity];

        for (HashNode<E> bucket : oldTable) {
            while (bucket != null) {
                HashNode<E> next = bucket.next;

                int idx = indexFor(bucket.value, newTable);
                bucket.next = newTable[idx];
                newTable[idx] = bucket;

                bucket = next;
            }
        }

        table = newTable;
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
    public boolean contains(Object o) {
        int idx = indexFor(o, table);
        HashNode<E> node = table[idx];
        while (node != null) {
            if (equalsElement(node.value, o)) return true;
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) return false;

        if (needsResize()) resize(table.length * 2);

        int idx = indexFor(e, table);
        HashNode<E> hashNode = new HashNode<>(e, table[idx]);
        table[idx] = hashNode;

        LinkedNode<E> ln = new LinkedNode<>(e);
        hashNode.linkedNode = ln;

        if (tail == null) {
            head = tail = ln;
        } else {
            tail.next = ln;
            ln.prev = tail;
            tail = ln;
        }

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexFor(o, table);
        HashNode<E> node = table[idx];
        HashNode<E> prev = null;

        while (node != null) {
            if (equalsElement(node.value, o)) {

                if (prev == null) table[idx] = node.next;
                else prev.next = node.next;

                LinkedNode<E> ln = node.linkedNode;

                if (ln.prev == null) head = ln.next;
                else ln.prev.next = ln.next;

                if (ln.next == null) tail = ln.prev;
                else ln.next.prev = ln.prev;

                size--;
                return true;
            }
            prev = node;
            node = node.next;
        }

        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) table[i] = null;
        head = tail = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');

        LinkedNode<E> cur = head;
        boolean first = true;

        while (cur != null) {
            if (!first) sb.append(", ");
            sb.append(String.valueOf(cur.value));
            first = false;
            cur = cur.next;
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
        boolean changed = false;
        for (E e : c) if (add(e)) changed = true;
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;
        for (Object o : c) if (remove(o)) changed = true;
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();

        boolean changed = false;
        LinkedNode<E> cur = head;

        while (cur != null) {
            LinkedNode<E> next = cur.next;
            if (!c.contains(cur.value)) {
                remove(cur.value);
                changed = true;
            }
            cur = next;
        }

        return changed;
    }


    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            LinkedNode<E> cur = head;
            LinkedNode<E> lastReturned = null;

            @Override
            public boolean hasNext() {
                return cur != null;
            }

            @Override
            public E next() {
                if (cur == null) throw new NoSuchElementException();
                lastReturned = cur;
                cur = cur.next;
                return lastReturned.value;
            }

            @Override
            public void remove() {
                if (lastReturned == null) throw new IllegalStateException();
                MyLinkedHashSet.this.remove(lastReturned.value);
                lastReturned = null;
            }
        };
    }


    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;

        for (LinkedNode<E> n = head; n != null; n = n.next)
            arr[i++] = n.value;

        return arr;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        int s = size;
        T[] arr = a.length >= s ? a :
                (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), s);

        int i = 0;
        for (LinkedNode<E> n = head; n != null; n = n.next)
            arr[i++] = (T) n.value;

        if (arr.length > s) arr[s] = null;

        return arr;
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (LinkedNode<E> n = head; n != null; n = n.next)
            h += (n.value == null ? 0 : n.value.hashCode());
        return h;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Set)) return false;
        Set<?> other = (Set<?>) o;

        if (other.size() != this.size) return false;
        return containsAll(other);
    }
}

