package by.it.group410902.harkavy.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    // узел: цепочка коллизий + двусвязный порядок добавления
    private static class Node<E> {
        E key;
        Node<E> next;      // в бакете
        Node<E> prevOrd;   // в порядке добавления
        Node<E> nextOrd;   // в порядке добавления
        Node(E key, Node<E> next) { this.key = key; this.next = next; }
    }

    private Node<E>[] table;     // бакеты (длина = степень двойки)
    private int size = 0;
    private int threshold;       // порог роста (loadFactor = 0.75)

    // голова/хвост порядка добавления
    private Node<E> headOrd;
    private Node<E> tailOrd;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[16];
        threshold = (int)(table.length * 0.75f);
    }

    // хеш со спредом
    private int hash(Object key) {
        int h = (key == null) ? 0 : key.hashCode();
        return h ^ (h >>> 16);
    }

    // индекс бакета для текущей таблицы
    private int index(int h) {
        return h & (table.length - 1);
    }

    // вставка в конец порядка добавления
    private void linkOrderLast(Node<E> x) {
        x.prevOrd = tailOrd;
        x.nextOrd = null;
        if (tailOrd == null) headOrd = x; else tailOrd.nextOrd = x;
        tailOrd = x;
    }

    // удаление узла из порядка добавления
    private void unlinkOrder(Node<E> x) {
        Node<E> p = x.prevOrd, n = x.nextOrd;
        if (p == null) headOrd = n; else p.nextOrd = n;
        if (n == null) tailOrd = p; else n.prevOrd = p;
        x.prevOrd = x.nextOrd = null;
    }

    @SuppressWarnings("unchecked")
    private void resizeIfNeeded() {
        if (size < threshold) return;
        Node<E>[] old = table;
        Node<E>[] newTab = (Node<E>[]) new Node[old.length << 1];
        int newMask = newTab.length - 1;
        // перенос узлов в новые бакеты; порядок добавления не меняем
        for (Node<E> bucket : old) {
            for (Node<E> x = bucket; x != null; ) {
                Node<E> next = x.next;
                int idx = hash(x.key) & newMask; // ВАЖНО: используем новую длину
                x.next = newTab[idx];
                newTab[idx] = x;
                x = next;
            }
        }
        table = newTab;
        threshold = (int)(table.length * 0.75f);
    }

    // =================== обязательные методы ===================

    @Override
    public String toString() {
        // печать в порядке добавления
        StringBuilder sb = new StringBuilder("[");
        Node<E> x = headOrd;
        while (x != null) {
            sb.append(x.key);
            if (x.nextOrd != null) sb.append(", ");
            x = x.nextOrd;
        }
        return sb.append("]").toString();
    }

    @Override public int size() { return size; }

    @Override
    public void clear() {
        // обнуляем бакеты и порядок
        for (int i = 0; i < table.length; i++) table[i] = null;
        headOrd = tailOrd = null;
        size = 0;
    }

    @Override public boolean isEmpty() { return size == 0; }

    @Override
    public boolean add(E e) {
        resizeIfNeeded();
        int h = hash(e);
        int i = index(h);
        // проверяем наличие в цепочке
        for (Node<E> cur = table[i]; cur != null; cur = cur.next) {
            if (e == null ? cur.key == null : e.equals(cur.key)) return false;
        }
        // вставка в голову бакета и в конец порядка
        Node<E> x = new Node<>(e, table[i]);
        table[i] = x;
        linkOrderLast(x);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int h = hash(o);
        int i = index(h);
        Node<E> prev = null, cur = table[i];
        while (cur != null) {
            if (o == null ? cur.key == null : o.equals(cur.key)) {
                if (prev == null) table[i] = cur.next; else prev.next = cur.next;
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
    public boolean contains(Object o) {
        int h = hash(o);
        int i = index(h);
        for (Node<E> cur = table[i]; cur != null; cur = cur.next) {
            if (o == null ? cur.key == null : o.equals(cur.key)) return true;
        }
        return false;
    }

    // =================== массовые операции ===================

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
        // идём по порядку добавления, удаляем совпавшие
        for (Node<E> x = headOrd; x != null; ) {
            Node<E> next = x.nextOrd;
            if (c.contains(x.key)) { remove(x.key); changed = true; }
            x = next;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (Node<E> x = headOrd; x != null; ) {
            Node<E> next = x.nextOrd;
            if (!c.contains(x.key)) { remove(x.key); changed = true; }
            x = next;
        }
        return changed;
    }

    // ========= остальное не требуется заданием =========
    private void unsupported() { throw new UnsupportedOperationException("Not required by the assignment"); }
    @Override public Iterator<E> iterator() { unsupported(); return null; }
    @Override public Object[] toArray() { unsupported(); return null; }
    @Override public <T> T[] toArray(T[] a) { unsupported(); return null; }
}
