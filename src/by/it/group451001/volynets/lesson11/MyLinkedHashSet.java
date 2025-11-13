package by.it.group451001.volynets.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    // Узел хеш-цепочки + узел двусвязного списка порядка вставки
    private static class Node<E> {
        final int hash;
        final E key;
        Node<E> next;
        Node<E> before;
        Node<E> after;
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

    // Двусвязный список порядка вставки
    private Node<E> head; // первый добавленный
    private Node<E> tail; // последний добавленный

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        size = 0;
        head = tail = null;
    }

    private int hash(Object key) {
        int h = (key == null) ? 0 : key.hashCode();
        return h ^ (h >>> 16);
    }

    private int indexFor(int hash, int length) {
        return (length - 1) & hash;
    }

    private boolean keysEqual(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        // Обнуляем таблицу
        for (int i = 0; i < table.length; i++) table[i] = null;
        // Обнуляем список вставки
        head = tail = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
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
    public boolean add(E e) {
        int h = hash(e);
        int idx = indexFor(h, table.length);
        Node<E> first = table[idx];

        // Проверка на существование
        for (Node<E> n = first; n != null; n = n.next) {
            if (n.hash == h && keysEqual(n.key, e)) {
                return false; // уже есть
            }
        }

        // Вставка в голову цепочки
        Node<E> newNode = new Node<>(h, e, first);
        table[idx] = newNode;

        // Подвязываем в список вставки (в конец)
        linkLast(newNode);

        size++;
        if (size > threshold) resize();
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int h = hash(o);
        int idx = indexFor(h, table.length);
        Node<E> prev = null;
        Node<E> cur = table[idx];
        while (cur != null) {
            if (cur.hash == h && keysEqual(cur.key, o)) {
                if (prev == null) table[idx] = cur.next;
                else prev.next = cur.next;
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
        Node<E> n = head;
        while (n != null) {
            Node<E> nextInOrder = n.after;
            if (collectionContains(c, n.key)) {
                deleteFromBucket(n);
                unlink(n);
                size--;
                changed = true;
            }
            n = nextInOrder;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> n = head;
        while (n != null) {
            Node<E> nextInOrder = n.after;
            if (!collectionContains(c, n.key)) {
                deleteFromBucket(n);
                unlink(n);
                size--;
                changed = true;
            }
            n = nextInOrder;
        }
        return changed;
    }

    private void linkLast(Node<E> e) {
        Node<E> t = tail;
        e.after = null;
        e.before = t;
        if (t == null) {
            head = e;
        } else {
            t.after = e;
        }
        tail = e;
    }

    private void unlink(Node<E> e) {
        Node<E> b = e.before;
        Node<E> a = e.after;
        if (b == null) {
            head = a;
        } else {
            b.after = a;
        }
        if (a == null) {
            tail = b;
        } else {
            a.before = b;
        }
        e.before = e.after = null;
    }

    private void deleteFromBucket(Node<E> node) {
        int idx = indexFor(node.hash, table.length);
        Node<E> prev = null;
        Node<E> cur = table[idx];
        while (cur != null) {
            if (cur == node) {
                if (prev == null) table[idx] = cur.next;
                else prev.next = cur.next;
                return;
            }
            prev = cur;
            cur = cur.next;
        }
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

        for (int i = 0; i < oldCap; i++) {
            Node<E> e = oldTab[i];
            if (e == null) continue;

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

    private boolean collectionContains(Collection<?> c, Object key) {
        for (Object x : c) {
            if (x == key || (x != null && x.equals(key))) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;
        for (Node<E> n = head; n != null; n = n.after) {
            if (!first) sb.append(", ");
            sb.append(n.key);
            first = false;
        }
        sb.append(']');
        return sb.toString();
    }

    @Override  public Iterator<E> iterator() {throw new UnsupportedOperationException();}
    @Override  public Object[] toArray() { throw new UnsupportedOperationException();}
    @Override  public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException();}
    @Override  public boolean equals(Object o) { return super.equals(o);}
    @Override  public int hashCode() { return super.hashCode();}
}