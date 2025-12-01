package by.it.group410902.plekhova.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {


    private static class Node<E> {
        final E key;
        final int hash;

        Node<E> next;       // в цепочке бакета
        Node<E> before;     // в порядке вставки
        Node<E> after;      // в порядке вставки

        Node(E key, int hash) {
            this.key = key;
            this.hash = hash;
        }
    }

    private Node<E>[] table;      // массив бакетов
    private int size;
    private int threshold;
    private final float loadFactor;

    private Node<E> head;  // первый элемент в порядке вставки
    private Node<E> tail;  // последний элемент в порядке вставки

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) initialCapacity = DEFAULT_CAPACITY;
        this.loadFactor = loadFactor > 0 ? loadFactor : DEFAULT_LOAD_FACTOR;
        this.table = (Node<E>[]) new Node[initialCapacity];
        this.threshold = (int) (initialCapacity * this.loadFactor);
        this.size = 0;
    }

    public MyLinkedHashSet(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public MyLinkedHashSet() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }



    private int hash(Object o) {
        return (o == null) ? 0 : (o.hashCode() & 0x7fffffff);
    }

    private int indexForHash(int hash, int len) {
        return hash % len;
    }

    @SuppressWarnings("unchecked")
    private void resizeIfNeeded() {
        if (size < threshold) return;
        int newCap = table.length * 2;
        Node<E>[] newTable = (Node<E>[]) new Node[newCap];
        // переносим только цепочки (порядок вставки уже хранится в before/after)
        for (int i = 0; i < table.length; i++) {
            Node<E> node = table[i];
            while (node != null) {
                Node<E> next = node.next;
                int idx = indexForHash(node.hash, newCap);
                node.next = newTable[idx];
                newTable[idx] = node;
                node = next;
            }
        }
        table = newTable;
        threshold = (int) (newCap * loadFactor);
    }

    private void linkLast(Node<E> node) {
        if (tail == null) {
            head = tail = node;
        } else {
            tail.after = node;
            node.before = tail;
            tail = node;
        }
    }

    private void unlink(Node<E> node) {
        if (node.before != null) {
            node.before.after = node.after;
        } else {
            head = node.after;
        }
        if (node.after != null) {
            node.after.before = node.before;
        } else {
            tail = node.before;
        }
        node.before = node.after = null;
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
        int h = hash(e);
        int idx = indexForHash(h, table.length);
        for (Node<E> n = table[idx]; n != null; n = n.next) {
            if (n.hash == h && (e == n.key || (e != null && e.equals(n.key)))) {
                return false; // уже есть
            }
        }
        Node<E> newNode = new Node<>(e, h);
        newNode.next = table[idx];
        table[idx] = newNode;
        linkLast(newNode);
        size++;
        resizeIfNeeded();
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int h = hash(o);
        int idx = indexForHash(h, table.length);
        Node<E> prev = null;
        Node<E> cur = table[idx];
        while (cur != null) {
            if (cur.hash == h && (o == cur.key || (o != null && o.equals(cur.key)))) {
                if (prev == null) {
                    table[idx] = cur.next;
                } else {
                    prev.next = cur.next;
                }
                unlink(cur);
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
        int h = hash(o);
        int idx = indexForHash(h, table.length);
        for (Node<E> n = table[idx]; n != null; n = n.next) {
            if (n.hash == h && (o == n.key || (o != null && o.equals(n.key)))) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> n = head;
        while (n != null) {
            sb.append(n.key);
            n = n.after;
            if (n != null) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }



    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) if (!contains(e)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) if (add(e)) changed = true;
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object e : c) {
            while (remove(e)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> n = head;
        while (n != null) {
            Node<E> next = n.after;
            if (!c.contains(n.key)) {
                remove(n.key);
                changed = true;
            }
            n = next;
        }
        return changed;
    }


    @Override public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> cur = head;
            Node<E> lastReturned = null;
            @Override public boolean hasNext() { return cur != null; }
            @Override public E next() {
                if (cur == null) throw new NoSuchElementException();
                lastReturned = cur;
                cur = cur.after;
                return lastReturned.key;
            }
            @Override public void remove() {
                if (lastReturned == null) throw new IllegalStateException();
                MyLinkedHashSet.this.remove(lastReturned.key);
                lastReturned = null;
            }
        };
    }

    @Override public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;
        for (E e : this) arr[i++] = e;
        return arr;
    }

    @Override public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            @SuppressWarnings("unchecked")
            T[] newArr = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
            a = newArr;
        }
        int i = 0;
        for (E e : this) a[i++] = (T) e;
        if (a.length > size) a[size] = null;
        return a;
    }





}