package by.it.group451004.matyrka.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Objects;

/**
 * Простая реализация HashSet на основе массива бакетов + односвязные списки для коллизий.
 * Не использует другие коллекции стандартной библиотеки.
 *
 * Поддерживаются методы:
 * size(), clear(), isEmpty(), add(E), remove(Object), contains(Object), toString()
 *
 * Прочие методы Set<E> бросают UnsupportedOperationException.
 */
public class MyHashSet<E> implements Set<E> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size;
    private final float loadFactor;

    @SuppressWarnings("unchecked")
    public MyHashSet(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) initialCapacity = DEFAULT_INITIAL_CAPACITY;
        this.loadFactor = loadFactor > 0 ? loadFactor : DEFAULT_LOAD_FACTOR;
        table = (Node<E>[]) new Node[initialCapacity];
        size = 0;
    }

    public MyHashSet(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public MyHashSet() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    private static final class Node<E> {
        final E value;
        Node<E> next;

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private int indexFor(Object key, int length) {
        int h = (key == null) ? 0 : key.hashCode();
        // unsigned mod
        return (h & 0x7fffffff) % length;
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
        int idx = indexFor(o, table.length);
        Node<E> cur = table[idx];
        while (cur != null) {
            if (Objects.equals(cur.value, o)) return true;
            cur = cur.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) return false;
        ensureCapacityForAdd();
        int idx = indexFor(e, table.length);
        // add at head (order may be arbitrary)
        Node<E> newNode = new Node<>(e, table[idx]);
        table[idx] = newNode;
        size++;
        return true;
    }

    private void ensureCapacityForAdd() {
        if ((float) (size + 1) / table.length > loadFactor) {
            resize(table.length * 2);
        }
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        Node<E>[] old = table;
        Node<E>[] nextTable = (Node<E>[]) new Node[newCapacity];
        for (int i = 0; i < old.length; i++) {
            Node<E> cur = old[i];
            while (cur != null) {
                Node<E> next = cur.next;
                int idx = indexFor(cur.value, newCapacity);
                // move node to new table (preserve node objects)
                cur.next = nextTable[idx];
                nextTable[idx] = cur;
                cur = next;
            }
        }
        table = nextTable;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexFor(o, table.length);
        Node<E> cur = table[idx];
        Node<E> prev = null;
        while (cur != null) {
            if (Objects.equals(cur.value, o)) {
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

    @Override
    public void clear() {
        @SuppressWarnings("unchecked")
        Node<E>[] newTable = (Node<E>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        table = newTable;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;
        for (int i = 0; i < table.length; i++) {
            Node<E> cur = table[i];
            while (cur != null) {
                if (!first) {
                    sb.append(", ");
                } else {
                    first = false;
                }
                sb.append(String.valueOf(cur.value));
                cur = cur.next;
            }
        }
        sb.append(']');
        return sb.toString();
    }

    // --- Остальные методы интерфейса Set<E> ---
    // Для упрощения (и поскольку в задании требуемые методы выше) — бросаем UnsupportedOperationException
    // если они не реализованы. При необходимости можно реализовать дополнительно.

    @Override
    public Iterator<E> iterator() {
        // Простая реализация итератора по бакетам
        return new Iterator<E>() {
            int bucket = 0;
            Node<E> current = null;
            Node<E> lastReturned = null;

            {
                advanceToNext();
            }

            private void advanceToNext() {
                if (current != null && current.next != null) {
                    current = current.next;
                    return;
                }
                current = null;
                while (bucket < table.length) {
                    if (table[bucket] != null) {
                        current = table[bucket++];
                        return;
                    }
                    bucket++;
                }
            }

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null) throw new java.util.NoSuchElementException();
                lastReturned = current;
                E val = current.value;
                advanceToNext();
                return val;
            }

            @Override
            public void remove() {
                if (lastReturned == null) throw new IllegalStateException();
                MyHashSet.this.remove(lastReturned.value);
                lastReturned = null;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;
        for (E e : this) arr[i++] = e;
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        int s = size;
        T[] arr = a.length >= s ? a :
                (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), s);
        int i = 0;
        for (E e : this) {
            arr[i++] = (T) e;
        }
        if (arr.length > s) arr[s] = null;
        return arr;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
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
        boolean changed = false;
        for (int i = 0; i < table.length; i++) {
            Node<E> cur = table[i];
            Node<E> prev = null;
            while (cur != null) {
                if (!c.contains(cur.value)) {
                    if (prev == null) {
                        table[i] = cur.next;
                        cur = table[i];
                    } else {
                        prev.next = cur.next;
                        cur = prev.next;
                    }
                    size--;
                    changed = true;
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
        for (Object o : c) {
            while (remove(o)) {
                changed = true;
            }
        }
        return changed;
    }

    // --- unsupported operations could also be left throwing directly ---
    // equals and hashCode default from Object are acceptable unless strict Set equality required

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Set)) return false;
        Set<?> s = (Set<?>) o;
        if (s.size() != this.size()) return false;
        return containsAll(s);
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (E e : this) {
            if (e != null) h += e.hashCode();
        }
        return h;
    }
}
