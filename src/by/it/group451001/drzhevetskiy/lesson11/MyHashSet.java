package by.it.group451001.drzhevetskiy.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {


    private static class Node<E> {
        E value;
        Node<E> next;

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size;
    private int capacity;
    private int threshold;
    private final float loadFactor = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        this.capacity = DEFAULT_INITIAL_CAPACITY;
        this.table = (Node<E>[]) new Node[capacity];
        this.size = 0;
        this.threshold = (int) (capacity * loadFactor);
    }

    private int hash(Object o) {
        int h = (o == null) ? 0 : o.hashCode();
        return h & 0x7FFFFFFF;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = capacity * 2;
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];

        for (int i = 0; i < capacity; i++) {
            Node<E> node = table[i];
            while (node != null) {
                Node<E> next = node.next;
                int idx = hash(node.value) % newCapacity;
                // вставляем в голову списка нового бакета
                node.next = newTable[idx];
                newTable[idx] = node;
                node = next;
            }
        }

        table = newTable;
        capacity = newCapacity;
        threshold = (int) (capacity * loadFactor);
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
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            table[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        int idx = hash(o) % capacity;
        Node<E> node = table[idx];
        while (node != null) {
            if ((o == null && node.value == null) || (o != null && o.equals(node.value))) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        int idx = hash(e) % capacity;
        Node<E> node = table[idx];
        while (node != null) {
            if ((e == null && node.value == null) || (e != null && e.equals(node.value))) {
                return false;
            }
            node = node.next;
        }

        Node<E> newNode = new Node<>(e, table[idx]);
        table[idx] = newNode;
        size++;

        if (size > threshold) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = hash(o) % capacity;
        Node<E> node = table[idx];
        Node<E> prev = null;
        while (node != null) {
            if ((o == null && node.value == null) || (o != null && o.equals(node.value))) {
                if (prev == null) {
                    table[idx] = node.next;
                } else {
                    prev.next = node.next;
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (int i = 0; i < capacity; i++) {
            Node<E> node = table[i];
            while (node != null) {
                if (!first) sb.append(", ");
                sb.append(node.value);
                first = false;
                node = node.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int pos = 0;
        for (int i = 0; i < capacity; i++) {
            Node<E> node = table[i];
            while (node != null) {
                arr[pos++] = node.value;
                node = node.next;
            }
        }
        return arr;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            T[] arr = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
            Object[] objects = toArray();
            System.arraycopy(objects, 0, arr, 0, size);
            return arr;
        } else {
            Object[] objects = toArray();
            System.arraycopy(objects, 0, a, 0, size);
            if (a.length > size) a[size] = null;
            return a;
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object el : c) {
            if (!contains(el)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E el : c) {
            if (add(el)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < capacity; i++) {
            Node<E> node = table[i];
            Node<E> prev = null;
            while (node != null) {
                if (!c.contains(node.value)) {
                    if (prev == null) {
                        table[i] = node.next;
                        node = table[i];
                    } else {
                        prev.next = node.next;
                        node = prev.next;
                    }
                    size--;
                    changed = true;
                } else {
                    prev = node;
                    node = node.next;
                }
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object el : c) {
            while (remove(el)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeIf(java.util.function.Predicate<? super E> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Set)) return false;
        Set<?> other = (Set<?>) o;
        if (other.size() != this.size) return false;
        return containsAll(other);
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (int i = 0; i < capacity; i++) {
            Node<E> node = table[i];
            while (node != null) {
                h += (node.value == null ? 0 : node.value.hashCode());
                node = node.next;
            }
        }
        return h;
    }
}
