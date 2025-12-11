package by.it.group410901.kovalevich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyHashSet<E> implements Set<E> {

    private static final class Node<T> {
        final T key;
        Node<T> next;
        Node(T key, Node<T> next) {
            this.key = key;
            this.next = next;
        }
    }

    private Node<E>[] table;     // бакеты
    private int size;            // текущее количество элементов
    private int threshold;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16; // степень двойки

    public MyHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        size = 0;
    }

    private static int spreadHash(Object key) {
        int h = (key == null) ? 0 : key.hashCode();
        return h ^ (h >>> 16);
    }

    private int indexFor(int hash, int capacity) {
        return hash & (capacity - 1);
    }

    private void resizeIfNeeded() {
        if (size <= threshold) return;
        int oldCap = table.length;
        int newCap = oldCap << 1; // *2
        Node<E>[] newTable = (Node<E>[]) new Node[newCap];

        // пере-хеширование всех узлов
        for (int i = 0; i < oldCap; i++) {
            Node<E> node = table[i];
            while (node != null) {
                Node<E> next = node.next;
                int idx = indexFor(spreadHash(node.key), newCap);
                node.next = newTable[idx];
                newTable[idx] = node;
                node = next;
            }
        }
        table = newTable;
        threshold = (int) (newCap * LOAD_FACTOR);
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        // занулим бакеты
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        resizeIfNeeded();
        int idx = indexFor(spreadHash(e), table.length);
        Node<E> head = table[idx];

        // проверка есть ли элемент
        for (Node<E> n = head; n != null; n = n.next) {
            if (eq(n.key, e)) {
                return false;
            }
        }

        // вставка в голову списка бакета
        table[idx] = new Node<>(e, head);
        size++;
        return true;
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
    public boolean remove(Object o) {
        int idx = indexFor(spreadHash(o), table.length);
        Node<E> prev = null;
        Node<E> cur = table[idx];

        while (cur != null) {
            if (eq(cur.key, o)) {
                if (prev == null) {
                    table[idx] = cur.next;
                } else {
                    prev.next = cur.next;
                }
                cur.next = null;
                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }

    private boolean eq(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }


    @Override
    public String toString() {
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


    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object x : c) {
            if (!contains(x)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E x : c) {
            if (add(x)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        // пройдём по всем бакетам и выкинем всё, чего нет в c
        for (int i = 0; i < table.length; i++) {
            Node<E> prev = null;
            Node<E> cur = table[i];
            while (cur != null) {
                Node<E> next = cur.next;
                if (!c.contains(cur.key)) {
                    // удалить cur
                    if (prev == null) table[i] = next; else prev.next = next;
                    size--;
                    changed = true;
                    cur.next = null;
                } else {
                    prev = cur;
                }
                cur = next;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object x : c) {
            if (remove(x)) changed = true;
        }
        return changed;
    }

    // Итератор
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int bucket = 0;
            Node<E> node = advanceToNextNode();

            private Node<E> advanceToNextNode() {
                while (bucket < table.length) {
                    if (table[bucket] != null) return table[bucket];
                    bucket++;
                }
                return null;
            }

            @Override
            public boolean hasNext() {
                return node != null;
            }

            @Override
            public E next() {
                if (node == null) throw new NoSuchElementException();
                E val = node.key;
                node = node.next;
                if (node == null) {
                    bucket++;
                    node = advanceToNextNode();
                }
                return val;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] a = new Object[size];
        int k = 0;
        for (int i = 0; i < table.length; i++) {
            for (Node<E> n = table[i]; n != null; n = n.next) {
                a[k++] = n.key;
            }
        }
        return a;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        int n = size;
        T[] out = a.length >= n ? a
                : (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), n);
        int k = 0;
        for (int i = 0; i < table.length; i++) {
            for (Node<E> nd = table[i]; nd != null; nd = nd.next) {
                out[k++] = (T) nd.key;
            }
        }
        if (out.length > n) out[n] = null;
        return out;
    }

}
