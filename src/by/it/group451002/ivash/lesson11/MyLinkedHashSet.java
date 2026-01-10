package by.it.group451002.ivash.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyLinkedHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<E>[] table;     // хеш-таблица
    private int size;
    private int threshold;
    private final float loadFactor;

    // голова и хвост глобального списка (для порядка добавления)
    private Entry<E> head;
    private Entry<E> tail;

    // Узел в bucket'е
    private static class Node<E> {
        final E key;
        Node<E> next;
        Entry<E> order; // связь с узлом порядка (двусвязный список)

        Node(E key, Node<E> next) {
            this.key = key;
            this.next = next;
        }
    }

    // Узлы двусвязного списка для порядка добавления
    private static class Entry<E> {
        Node<E> ref;
        Entry<E> prev, next;

        Entry(Node<E> ref) {
            this.ref = ref;
        }
    }

    public MyLinkedHashSet() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyLinkedHashSet(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }

    public MyLinkedHashSet(int capacity, float loadFactor) {
        if (capacity <= 0) capacity = DEFAULT_CAPACITY;
        this.loadFactor = loadFactor;
        table = (Node<E>[]) new Node[tableSizeFor(capacity)];
        threshold = (int)(table.length * loadFactor);
    }

    private int tableSizeFor(int cap) {
        int n = 1;
        while (n < cap) n <<= 1;
        return n;
    }

    private int indexFor(Object key) {
        int h = (key == null ? 0 : key.hashCode());
        h ^= (h >>> 16);
        return h & (table.length - 1);
    }

    private boolean eq(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    private void resize() {
        Node<E>[] old = table;
        int newCap = old.length << 1;
        Node<E>[] newTable = (Node<E>[]) new Node[newCap];

        for (int i = 0; i < old.length; i++) {
            Node<E> node = old[i];
            while (node != null) {
                Node<E> next = node.next;

                int h = (node.key == null) ? 0 : node.key.hashCode();
                h ^= (h >>> 16);
                int newIndex = h & (newCap - 1);

                node.next = newTable[newIndex];
                newTable[newIndex] = node;

                node = next;
            }
        }
        table = newTable;
        threshold = (int)(table.length * loadFactor);
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) return false;

        if (size + 1 > threshold) resize();

        int idx = indexFor(e);
        Node<E> newNode = new Node<>(e, table[idx]);
        table[idx] = newNode;

        // Добавление в конец двусвязного списка
        Entry<E> ent = new Entry<>(newNode);
        newNode.order = ent;
        if (tail == null) {
            head = tail = ent;
        } else {
            tail.next = ent;
            ent.prev = tail;
            tail = ent;
        }

        size++;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        int idx = indexFor(o);
        Node<E> node = table[idx];
        while (node != null) {
            if (eq(node.key, o)) return true;
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexFor(o);
        Node<E> node = table[idx], prev = null;

        while (node != null) {
            if (eq(node.key, o)) {
                if (prev == null) table[idx] = node.next;
                else prev.next = node.next;

                // удаляем из linked list порядка
                Entry<E> ent = node.order;
                if (ent.prev != null) ent.prev.next = ent.next;
                else head = ent.next;

                if (ent.next != null) ent.next.prev = ent.prev;
                else tail = ent.prev;

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
        for (int i = 0; i < table.length; i++)
            table[i] = null;

        head = tail = null;
        size = 0;
    }

    @Override public int size() { return size; }
    @Override public boolean isEmpty() { return size == 0; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');

        Entry<E> cur = head;
        boolean first = true;

        while (cur != null) {
            if (!first) sb.append(", ");
            first = false;

            sb.append(cur.ref.key);
            cur = cur.next;
        }

        sb.append(']');
        return sb.toString();
    }

    // ---------------------------
    // Методы из задания уровня B
    // ---------------------------

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c)
            if (add(e)) changed = true;
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c)
            if (remove(o)) changed = true;
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Entry<E> cur = head;

        while (cur != null) {
            Entry<E> next = cur.next;
            if (!c.contains(cur.ref.key)) {
                remove(cur.ref.key);
                changed = true;
            }
            cur = next;
        }
        return changed;
    }

    // Iterator не требуется в задании B → возвращаем Unsupported
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Iterator not required for level B");
    }

    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}
