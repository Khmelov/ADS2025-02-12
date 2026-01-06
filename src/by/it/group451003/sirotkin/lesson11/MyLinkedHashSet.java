package by.it.group451003.sirotkin.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E element;
        Node<E> next;
        Node<E> before;
        Node<E> after;
        int hash;

        Node(E element, int hash, Node<E> next) {
            this.element = element;
            this.hash = hash;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<E> head;
    private Node<E> tail;

    private int size;
    private int threshold;
    private final float loadFactor;

    // Конструкторы
    public MyLinkedHashSet() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyLinkedHashSet(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public MyLinkedHashSet(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);

        this.loadFactor = loadFactor;
        this.threshold = (int)(initialCapacity * loadFactor);
        @SuppressWarnings("unchecked")
        Node<E>[] newTable = (Node<E>[]) new Node[initialCapacity];
        this.table = newTable;
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private int hash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    private void resize() {
        Node<E>[] oldTable = table;
        int oldCapacity = oldTable.length;
        if (oldCapacity == 0) return;

        int newCapacity = oldCapacity * 2;
        @SuppressWarnings("unchecked")
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];
        table = newTable;
        threshold = (int)(newCapacity * loadFactor);

        Node<E> current = head;
        while (current != null) {
            int index = indexFor(current.hash, newCapacity);
            Node<E> nextInChain = newTable[index];
            current.next = nextInChain;
            newTable[index] = current;
            current = current.after;
        }
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
        int h = hash(o);
        int index = indexFor(h, table.length);

        Node<E> node = table[index];
        while (node != null) {
            if (node.hash == h && (o == node.element || (o != null && o.equals(node.element)))) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        int h = hash(e);
        int index = indexFor(h, table.length);

        Node<E> node = table[index];
        while (node != null) {
            if (node.hash == h && (e == node.element || (e != null && e.equals(node.element)))) {
                return false;
            }
            node = node.next;
        }

        Node<E> newNode = new Node<>(e, h, table[index]);
        table[index] = newNode;

        if (head == null) {
            head = tail = newNode;
        } else {
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode;
        }

        size++;

        if (size > threshold) {
            resize();
        }

        return true;
    }

    @Override
    public boolean remove(Object o) {
        int h = hash(o);
        int index = indexFor(h, table.length);

        Node<E> prev = null;
        Node<E> node = table[index];

        while (node != null) {
            if (node.hash == h && (o == node.element || (o != null && o.equals(node.element)))) {
                if (prev == null) {
                    table[index] = node.next;
                } else {
                    prev.next = node.next;
                }

                if (node.before == null) {
                    head = node.after;
                } else {
                    node.before.after = node.after;
                }

                if (node.after == null) {
                    tail = node.before;
                } else {
                    node.after.before = node.before;
                }

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
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        head = tail = null;
        size = 0;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;

        while (current != null) {
            sb.append(current.element);
            if (current.after != null) {
                sb.append(", ");
            }
            current = current.after;
        }

        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object element : c) {
            if (remove(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedHashSetIterator();
    }

    private class LinkedHashSetIterator implements Iterator<E> {
        private Node<E> current = head;
        private Node<E> lastReturned = null;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturned = current;
            E element = current.element;
            current = current.after;
            return element;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            MyLinkedHashSet.this.remove(lastReturned.element);
            lastReturned = null;
        }
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int i = 0;
        for (E element : this) {
            array[i++] = element;
        }
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }

        int i = 0;
        for (E element : this) {
            a[i++] = (T) element;
        }

        if (a.length > size) {
            a[size] = null;
        }

        return a;
    }
}