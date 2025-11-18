package by.it.group451001.volynets.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    // Узел односвязного списка для цепочек
    private static class Node<E> {
        final int hash;
        final E key;
        Node<E> next;
        Node(int hash, E key, Node<E> next) {
            this.hash = hash;
            this.key = key;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size;
    private int threshold;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private static final int MAX_CAPACITY = 1 << 30;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) table[i] = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private int hash(Object key) {
        int h = (key == null) ? 0 : key.hashCode();
        return h ^ (h >>> 16);
    }

    private int indexFor(int hash, int length) {
        return (length - 1) & hash;
    }

    @Override
    public boolean add(E e) {
        return putIfAbsent(e);
    }

    private boolean keysEqual(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    private boolean putIfAbsent(E key) {
        int h = hash(key);
        int idx = indexFor(h, table.length);
        Node<E> head = table[idx];

        // Проверка наличия
        for (Node<E> n = head; n != null; n = n.next) {
            if (n.hash == h && keysEqual(n.key, key)) {
                return false; // уже есть
            }
        }

        // Вставка в голову цепочки
        Node<E> newNode = new Node<>(h, key, head);
        table[idx] = newNode;
        size++;

        if (size > threshold) {
            resize();
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int oldCap = table.length;
        if (oldCap >= MAX_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }
        int newCap = oldCap << 1;
        Node<E>[] oldTab = table;
        Node<E>[] newTab = (Node<E>[]) new Node[newCap];

        // Перераспределение узлов без перерасчёта hash
        for (int i = 0; i < oldCap; i++) {
            Node<E> e = oldTab[i];
            if (e == null) continue;

            // Разнесём на две цепочки: в тот же индекс и индекс+oldCap
            Node<E> loHead = null, loTail = null;
            Node<E> hiHead = null, hiTail = null;

            while (e != null) {
                Node<E> next = e.next;
                if ((e.hash & oldCap) == 0) {
                    if (loTail == null) loHead = e;
                    else loTail.next = e;
                    loTail = e;
                } else {
                    if (hiTail == null) hiHead = e;
                    else hiTail.next = e;
                    hiTail = e;
                }
                e = next;
            }

            if (loTail != null) {
                loTail.next = null;
                newTab[i] = loHead;
            }
            if (hiTail != null) {
                hiTail.next = null;
                newTab[i + oldCap] = hiHead;
            }
        }

        table = newTab;
        threshold = (int) (newCap * LOAD_FACTOR);
    }

    @Override
    public boolean contains(Object o) {
        int h = hash(o);
        int idx = indexFor(h, table.length);
        for (Node<E> n = table[idx]; n != null; n = n.next) {
            if (n.hash == h && keysEqual(n.key, o)) return true;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        int h = hash(o);
        int idx = indexFor(h, table.length);
        Node<E> prev = null;
        Node<E> cur = table[idx];
        while (cur != null) {
            if (cur.hash == h && keysEqual(cur.key, o)) {
                if (prev == null) {
                    table[idx] = cur.next;
                } else {
                    prev.next = cur.next;
                }
                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }

    // Дополнительно (не обязательно, но полезно для отладки/тестов)
    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;
        for (int i = 0; i < table.length; i++) {
            for (Node<E> n = table[i]; n != null; n = n.next) {
                if (!first) sb.append(", ");
                sb.append(n.key);
                first = false;
            }
        }
        sb.append(']');
        return sb.toString();
    }

    // Заглушки/минимальные реализации оставшихся методов Set

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException();}
    @Override public Object[] toArray() { throw new UnsupportedOperationException();}
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException();}
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
    public boolean retainAll(Collection<?> c) {
        // Без использования стандартных коллекций — сделаем через проход по таблице
        boolean changed = false;
        for (int i = 0; i < table.length; i++) {
            Node<E> prev = null;
            Node<E> cur = table[i];
            while (cur != null) {
                if (!collectionContains(c, cur.key)) {
                    if (prev == null) table[i] = cur.next;
                    else prev.next = cur.next;
                    size--;
                    changed = true;
                    cur = (prev == null) ? table[i] : prev.next;
                } else {
                    prev = cur;
                    cur = cur.next;
                }
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < table.length; i++) {
            Node<E> prev = null;
            Node<E> cur = table[i];
            while (cur != null) {
                if (collectionContains(c, cur.key)) {
                    if (prev == null) table[i] = cur.next;
                    else prev.next = cur.next;
                    size--;
                    changed = true;
                    cur = (prev == null) ? table[i] : prev.next;
                } else {
                    prev = cur;
                    cur = cur.next;
                }
            }
        }
        return changed;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o); // можно оставить стандартное
    }

    @Override
    public int hashCode() {
        return super.hashCode(); // можно оставить стандартное
    }

    private boolean collectionContains(Collection<?> c, Object key) {
        for (Object x : c) {
            if (x == key || (x != null && x.equals(key))) return true;
        }
        return false;
    }
}
