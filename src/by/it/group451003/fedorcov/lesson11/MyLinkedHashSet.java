package by.it.group451003.fedorcov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size;
    private final float loadFactor;

    private Node<E> head;
    private Node<E> tail;

    private static class Node<E> {
        final E item;
        final int hash;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        Node(E item, int hash, Node<E> next) {
            this.item = item;
            this.hash = hash;
            this.next = next;
        }
    }

    public MyLinkedHashSet() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyLinkedHashSet(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }

        this.loadFactor = loadFactor;
        this.table = new Node[initialCapacity];
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    private int hash(Object key) {
        if (key == null) return 0;
        int h = key.hashCode();
        return h ^ (h >>> 16);
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private void ensureCapacity() {
        if (size >= table.length * loadFactor) {
            resize(table.length * 2);
        }
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        Node<E>[] oldTable = table;
        Node<E>[] newTable = new Node[newCapacity];

        for (int i = 0; i < oldTable.length; i++) {
            Node<E> node = oldTable[i];
            while (node != null) {
                Node<E> next = node.next;
                int newIndex = indexFor(node.hash, newCapacity);
                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = next;
            }
        }

        table = newTable;
    }

    private void linkNodeLast(Node<E> node) {
        if (tail == null) {
            head = tail = node;
        } else {
            node.before = tail;
            tail.after = node;
            tail = node;
        }
    }

    private void unlinkNode(Node<E> node) {
        Node<E> prev = node.before;
        Node<E> next = node.after;

        if (prev == null) {
            head = next;
        } else {
            prev.after = next;
            node.before = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.before = prev;
            node.after = null;
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
        if (o == null) {
            for (Node<E> node = head; node != null; node = node.after) {
                if (node.item == null) {
                    return true;
                }
            }
            return false;
        }

        int h = hash(o);
        int index = indexFor(h, table.length);
        Node<E> node = table[index];

        while (node != null) {
            if (node.hash == h && o.equals(node.item)) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        ensureCapacity();
        int h = hash(e);
        int index = indexFor(h, table.length);

        Node<E> node = table[index];
        while (node != null) {
            if (node.hash == h && (e == null ? node.item == null : e.equals(node.item))) {
                return false;
            }
            node = node.next;
        }

        Node<E> newNode = new Node<>(e, h, table[index]);
        table[index] = newNode;

        linkNodeLast(newNode);

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (Node<E> node = head; node != null; node = node.after) {
                if (node.item == null) {
                    removeNode(node);
                    return true;
                }
            }
            return false;
        }

        int h = hash(o);
        int index = indexFor(h, table.length);
        Node<E> node = table[index];
        Node<E> prev = null;

        while (node != null) {
            if (node.hash == h && o.equals(node.item)) {
                removeNode(node);
                // Удаление из бакета
                if (prev == null) {
                    table[index] = node.next;
                } else {
                    prev.next = node.next;
                }
                return true;
            }
            prev = node;
            node = node.next;
        }

        return false;
    }

    private void removeNode(Node<E> node) {
        unlinkNode(node);
        size--;
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
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node<E> node = head;
        while (node != null) {
            sb.append(node.item);
            if (node.after != null) {
                sb.append(", ");
            }
            node = node.after;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E item : c) {
            if (add(item)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object item : c) {
            if (remove(item)) {
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
            current = current.after;
            return lastReturned.item;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            MyLinkedHashSet.this.remove(lastReturned.item);
            lastReturned = null;
        }
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> node = head; node != null; node = node.after) {
            result[i++] = node.item;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }

        int i = 0;
        for (Node<E> node = head; node != null; node = node.after) {
            a[i++] = (T) node.item;
        }

        if (a.length > size) {
            a[size] = null;
        }

        return a;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Set)) return false;

        Set<?> other = (Set<?>) o;
        if (other.size() != size()) return false;

        try {
            return containsAll(other);
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (Node<E> node = head; node != null; node = node.after) {
            if (node.item != null) {
                hashCode += node.item.hashCode();
            }
        }
        return hashCode;
    }
}
