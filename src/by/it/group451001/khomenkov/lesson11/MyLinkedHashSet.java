package by.it.group451001.khomenkov.lesson11;

import java.util.Collection;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static final class Node<E> {
        E key;
        int hash;
        Node<E> nextBucket; // цепочка коллизий
        Node<E> nextOrder;  // порядок вставки (односвязный)
        Node(E key, int hash) {
            this.key = key;
            this.hash = hash;
        }
    }

    private Node<E>[] table;
    private int size;
    private int threshold;
    private final float loadFactor;
    private Node<E> head; // первый в порядке вставки
    private Node<E> tail; // последний в порядке вставки

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int MAX_CAPACITY = 1 << 30;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) throw new IllegalArgumentException();
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) throw new IllegalArgumentException();
        if (initialCapacity > MAX_CAPACITY) initialCapacity = MAX_CAPACITY;
        this.loadFactor = loadFactor;
        int cap = tableSizeFor(initialCapacity);
        this.table = (Node<E>[]) new Node[cap];
        this.threshold = (int) (cap * loadFactor);
    }

    public MyLinkedHashSet(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public MyLinkedHashSet() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    private static int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAX_CAPACITY) ? MAX_CAPACITY : n + 1;
    }

    private static int spread(int h) {
        return h ^ (h >>> 16);
    }

    private int indexFor(int hash, int length) {
        return (length - 1) & hash;
    }

    private int hashOf(Object key) {
        return spread(key == null ? 0 : key.hashCode());
    }

    private boolean keysEqual(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    private void linkOrderTail(Node<E> e) {
        if (tail == null) {
            head = tail = e;
        } else {
            tail.nextOrder = e;
            tail = e;
        }
    }

    @SuppressWarnings("unchecked")
    private void resizeIfNeeded() {
        if (size <= threshold) return;
        int oldCap = table.length;
        if (oldCap >= MAX_CAPACITY) return;
        int newCap = oldCap << 1;
        Node<E>[] newTab = (Node<E>[]) new Node[newCap];
        for (int i = 0; i < oldCap; i++) {
            Node<E> e = table[i];
            while (e != null) {
                Node<E> next = e.nextBucket;
                int idx = indexFor(e.hash, newCap);
                e.nextBucket = newTab[idx];
                newTab[idx] = e;
                e = next;
            }
        }
        table = newTab;
        threshold = (int) (newCap * loadFactor);
    }

    private Node<E> findNode(Object key, int hash) {
        Node<E> e = table[indexFor(hash, table.length)];
        while (e != null) {
            if (e.hash == hash && keysEqual(e.key, key)) return e;
            e = e.nextBucket;
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) table[i] = null;
        head = tail = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        int hash = hashOf(e);
        int idx = indexFor(hash, table.length);
        Node<E> cur = table[idx];
        while (cur != null) {
            if (cur.hash == hash && keysEqual(cur.key, e)) return false;
            cur = cur.nextBucket;
        }
        Node<E> node = new Node<>(e, hash);
        node.nextBucket = table[idx];
        table[idx] = node;
        linkOrderTail(node);
        size++;
        resizeIfNeeded();
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hash = hashOf(o);
        int idx = indexFor(hash, table.length);
        Node<E> prevB = null;
        Node<E> curB = table[idx];
        while (curB != null) {
            if (curB.hash == hash && keysEqual(curB.key, o)) {
                // удаляем из бакета
                if (prevB == null) table[idx] = curB.nextBucket;
                else prevB.nextBucket = curB.nextBucket;
                // удаляем из порядка (односвязный — ищем предыдущий)
                if (head == curB) {
                    head = head.nextOrder;
                    if (tail == curB) tail = null;
                } else {
                    Node<E> prevO = head;
                    while (prevO != null && prevO.nextOrder != curB) {
                        prevO = prevO.nextOrder;
                    }
                    if (prevO != null) {
                        prevO.nextOrder = curB.nextOrder;
                        if (tail == curB) tail = prevO;
                    }
                }
                size--;
                return true;
            }
            prevB = curB;
            curB = curB.nextBucket;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return findNode(o, hashOf(o)) != null;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<E> e = head;
        while (e != null) {
            sb.append(e.key);
            e = e.nextOrder;
            if (e != null) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
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
        boolean changed = false;
        for (Object x : c) if (remove(x)) changed = true;
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.nextOrder;
            if (!c.contains(current.key)) {
                remove(current.key);
                changed = true;
            }
            current = next;
        }
        return changed;
    }

    @Override
    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}
