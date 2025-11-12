package by.it.group410901.kovalevich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyLinkedHashSet<E> implements Set<E> {

    private static final class Node<T> {
        final T key;
        Node<T> next;           // цепочка внутри(односвязная)
        Node<T> orderPrev;      // предыдущий в порядке вставки
        Node<T> orderNext;      // следующий в порядке вставки

        Node(T key, Node<T> next) {
            this.key = key;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size;
    private int threshold;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;

    private Node<E> headOrder;
    private Node<E> tailOrder;

    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        size = 0;
        headOrder = null;
        tailOrder = null;
    }

    private static int spreadHash(Object key) {
        int h = (key == null) ? 0 : key.hashCode();
        return h ^ (h >>> 16);
    }

    private static int indexFor(int hash, int capacity) {
        return hash & (capacity - 1);
    }

    private static boolean eq(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    private void linkOrderLast(Node<E> node) {
        if (tailOrder == null) {
            headOrder = tailOrder = node;
        } else {
            node.orderPrev = tailOrder;
            tailOrder.orderNext = node;
            tailOrder = node;
        }
    }

    private void unlinkOrder(Node<E> node) {
        Node<E> p = node.orderPrev, n = node.orderNext;
        if (p == null) headOrder = n; else p.orderNext = n;
        if (n == null) tailOrder = p; else n.orderPrev = p;
        node.orderPrev = node.orderNext = null;
    }

    private void resizeIfNeeded() {
        if (size <= threshold) return;

        int oldCap = table.length;
        int newCap = oldCap << 1; // *2
        Node<E>[] newTable = (Node<E>[]) new Node[newCap];

        for (Node<E> cur = headOrder; cur != null; cur = cur.orderNext) {
            int idx = indexFor(spreadHash(cur.key), newCap);
            cur.next = newTable[idx];
            newTable[idx] = cur;
        }

        table = newTable;
        threshold = (int) (newCap * LOAD_FACTOR);
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        // обнулить бакеты
        for (int i = 0; i < table.length; i++) table[i] = null;
        for (Node<E> cur = headOrder; cur != null; ) {
            Node<E> next = cur.orderNext;
            cur.next = null;
            cur.orderPrev = cur.orderNext = null;
            cur = next;
        }
        headOrder = tailOrder = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        int idx = indexFor(spreadHash(o), table.length);
        for (Node<E> n = table[idx]; n != null; n = n.next) {
            if (eq(n.key, o)) return true;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        resizeIfNeeded();
        int idx = indexFor(spreadHash(e), table.length);

        // проверка дубликата
        for (Node<E> n = table[idx]; n != null; n = n.next) {
            if (eq(n.key, e)) return false;
        }

        // вставка в голову бакета
        Node<E> node = new Node<>(e, table[idx]);
        table[idx] = node;
        size++;

        // в конец списка порядка
        linkOrderLast(node);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexFor(spreadHash(o), table.length);
        Node<E> prev = null;
        Node<E> cur = table[idx];

        while (cur != null) {
            if (eq(cur.key, o)) {
                if (prev == null) table[idx] = cur.next; else prev.next = cur.next;
                cur.next = null;

                unlinkOrder(cur);

                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
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
        for (Node<E> cur = headOrder; cur != null; ) {
            Node<E> next = cur.orderNext;
            if (c.contains(cur.key)) {
                remove(cur.key);
                changed = true;
            }
            cur = next;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (Node<E> cur = headOrder; cur != null; ) {
            Node<E> next = cur.orderNext;
            if (!c.contains(cur.key)) {
                remove(cur.key);
                changed = true;
            }
            cur = next;
        }
        return changed;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;
        for (Node<E> cur = headOrder; cur != null; cur = cur.orderNext) {
            if (!first) sb.append(", ");
            sb.append(cur.key);
            first = false;
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> cur = headOrder;
            Node<E> lastReturned = null;

            @Override public boolean hasNext() { return cur != null; }

            @Override
            public E next() {
                if (cur == null) throw new NoSuchElementException();
                lastReturned = cur;
                cur = cur.orderNext;
                return lastReturned.key;
            }

            @Override
            public void remove() {
                if (lastReturned == null) throw new IllegalStateException();
                MyLinkedHashSet.this.remove(lastReturned.key);
                lastReturned = null;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] a = new Object[size];
        int i = 0;
        for (Node<E> cur = headOrder; cur != null; cur = cur.orderNext) {
            a[i++] = cur.key;
        }
        return a;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        int n = size;
        T[] out = a.length >= n ? a
                : (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), n);
        int i = 0;
        for (Node<E> cur = headOrder; cur != null; cur = cur.orderNext) {
            out[i++] = (T) cur.key;
        }
        if (out.length > n) out[n] = null;
        return out;
    }

}

