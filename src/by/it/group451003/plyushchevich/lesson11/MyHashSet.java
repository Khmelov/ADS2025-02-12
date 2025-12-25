package by.it.group451003.plyushchevich.lesson11;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

public class MyHashSet<E> implements Set<E> {
    private static class Node<E> {
        E value;
        Node<E> next;
        Node(E value, Node<E> next) { this.value = value; this.next = next; }
    }

    @SuppressWarnings("unchecked")
    private Node<E>[] table;
    private int size;
    private boolean containsNull;

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    public MyHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
        containsNull = false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) table[i] = null;
        size = 0;
        containsNull = false;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return containsNull;
        int idx = indexFor(hash(o.hashCode()));
        Node<E> cur = table[idx];
        while (cur != null) {
            if (o.equals(cur.value)) return true;
            cur = cur.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            if (containsNull) return false;
            containsNull = true;
            size++;
            ensureCapacityIfNeeded();
            return true;
        }
        int h = hash(e.hashCode());
        int idx = indexFor(h);
        Node<E> cur = table[idx];
        while (cur != null) {
            if (e.equals(cur.value)) return false;
            cur = cur.next;
        }
        table[idx] = new Node<>(e, table[idx]);
        size++;
        ensureCapacityIfNeeded();
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            if (!containsNull) return false;
            containsNull = false;
            size--;
            return true;
        }
        int idx = indexFor(hash(o.hashCode()));
        Node<E> cur = table[idx];
        Node<E> prev = null;
        while (cur != null) {
            if (o.equals(cur.value)) {
                if (prev == null) table[idx] = cur.next;
                else prev.next = cur.next;
                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        if (containsNull) {
            sb.append("null");
            first = false;
        }
        for (int i = 0; i < table.length; i++) {
            Node<E> cur = table[i];
            while (cur != null) {
                if (!first) sb.append(", ");
                sb.append(cur.value);
                first = false;
                cur = cur.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            if (add(e)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            if (remove(o)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;

        // обработка null отдельно
        if (containsNull && !c.contains(null)) {
            containsNull = false;
            size--;
            changed = true;
        }

        // проходим по таблице
        for (int i = 0; i < table.length; i++) {
            Node<E> cur = table[i];
            Node<E> prev = null;
            while (cur != null) {
                Node<E> next = cur.next;
                if (!c.contains(cur.value)) {
                    if (prev == null) table[i] = cur.next;
                    else prev.next = cur.next;
                    size--;
                    changed = true;
                } else {
                    prev = cur;
                }
                cur = next;
            }
        }

        return changed;
    }
    // ---------- конец добавленных методов ----------

    // вспомогательные методы
    private int hash(int h) {
        h ^= (h >>> 16);
        return h & 0x7fffffff;
    }

    private int indexFor(int hash) {
        return hash % table.length;
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        Node<E>[] old = table;
        table = (Node<E>[]) new Node[newCapacity];
        for (int i = 0; i < old.length; i++) {
            Node<E> cur = old[i];
            while (cur != null) {
                Node<E> next = cur.next;
                int idx = indexFor(hash(cur.value.hashCode()));
                cur.next = table[idx];
                table[idx] = cur;
                cur = next;
            }
        }
    }

    private void ensureCapacityIfNeeded() {
        if (size > table.length * LOAD_FACTOR) {
            resize(table.length * 2);
        }
    }

    // Заглушки
    @Override public Iterator<E> iterator() {
        throw new UnsupportedOperationException("iterator() не реализован");
    }
    @Override public Object[] toArray() {
        throw new UnsupportedOperationException("toArray() не реализован");
    }
    @Override public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("toArray(T[]) не реализован");
    }
    @Override public boolean equals(Object o) {
        throw new UnsupportedOperationException("equals() не реализован");
    }
    @Override public int hashCode() {
        throw new UnsupportedOperationException("hashCode() не реализован");
    }
}

