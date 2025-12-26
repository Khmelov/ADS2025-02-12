package by.it.group410902.harkavy.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    // узел цепочки коллизий
    private static class Node<E> {
        E key;
        Node<E> next;
        Node(E key, Node<E> next) { this.key = key; this.next = next; }
    }

    private Node<E>[] table;   // бакеты (длина — степень двойки)
    private int size = 0;
    private int threshold;     // порог resize при loadFactor=0.75

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[16];
        threshold = (int)(table.length * 0.75f);
    }

    // хеш со спредом
    private int hash(Object key) {
        int h = (key == null) ? 0 : key.hashCode();
        return h ^ (h >>> 16);
    }

    private int indexFor(int h, int len) {
        return h & (len - 1);
    }

    @SuppressWarnings("unchecked")
    private void resizeIfNeeded() {
        if (size < threshold) return;
        Node<E>[] old = table;
        Node<E>[] nt = (Node<E>[]) new Node[old.length << 1];
        // переносим узлы в новые бакеты (головой списка)
        for (Node<E> head : old) {
            for (Node<E> x = head; x != null; ) {
                Node<E> next = x.next;
                int idx = indexFor(hash(x.key), nt.length);
                x.next = nt[idx];
                nt[idx] = x;
                x = next;
            }
        }
        table = nt;
        threshold = (int)(table.length * 0.75f);
    }

    // =============== обязательные методы уровня A ===============

    @Override
    public int size() { return size; }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) table[i] = null; // обнуляем бакеты
        size = 0;
    }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean add(E e) {
        resizeIfNeeded();
        int idx = indexFor(hash(e), table.length);
        // проверяем наличие в цепочке
        for (Node<E> cur = table[idx]; cur != null; cur = cur.next) {
            if (e == null ? cur.key == null : e.equals(cur.key)) return false;
        }
        // вставка в голову бакета
        table[idx] = new Node<>(e, table[idx]);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexFor(hash(o), table.length);
        Node<E> prev = null, cur = table[idx];
        while (cur != null) {
            if (o == null ? cur.key == null : o.equals(cur.key)) {
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
    public boolean contains(Object o) {
        int idx = indexFor(hash(o), table.length);
        for (Node<E> cur = table[idx]; cur != null; cur = cur.next) {
            if (o == null ? cur.key == null : o.equals(cur.key)) return true;
        }
        return false;
    }

    // =============== минимально нужное для сравнения/обхода ===============

    @Override
    public String toString() {
        // произвольный порядок (по бакетам), формат как у коллекций
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> head : table) {
            for (Node<E> x = head; x != null; x = x.next) {
                if (!first) sb.append(", ");
                sb.append(x.key);
                first = false;
            }
        }
        return sb.append("]").toString();
    }

    @Override
    public Iterator<E> iterator() {
        // простой итератор по бакетам и цепочкам
        return new Iterator<E>() {
            int bi = 0;           // индекс бакета
            Node<E> cur = advance();

            private Node<E> advance() {
                if (cur != null && cur.next != null) return cur.next;
                while (bi < table.length) {
                    Node<E> h = table[bi++];
                    if (h != null) return h;
                }
                return null;
            }

            @Override public boolean hasNext() { return cur != null; }
            @Override public E next() {
                E v = cur.key;
                cur = advance();
                return v;
            }
        };
    }

    // =============== остальное не требуется по заданию ===============

    private void unsupported() { throw new UnsupportedOperationException("Not required by the assignment"); }
    @Override public Object[] toArray() { unsupported(); return null; }
    @Override public <T> T[] toArray(T[] a) { unsupported(); return null; }
    @Override public boolean containsAll(Collection<?> c) { unsupported(); return false; }
    @Override public boolean addAll(Collection<? extends E> c) { unsupported(); return false; }
    @Override public boolean retainAll(Collection<?> c) { unsupported(); return false; }
    @Override public boolean removeAll(Collection<?> c) { unsupported(); return false; }
}
