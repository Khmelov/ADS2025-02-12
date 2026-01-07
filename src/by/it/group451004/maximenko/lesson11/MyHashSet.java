package by.it.group451004.maximenko.lesson11;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size;
    private final float loadFactor;

    @SuppressWarnings("unchecked")
    public MyHashSet(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) throw new IllegalArgumentException("capacity must be > 0");
        this.table = (Node<E>[]) new Node[initialCapacity];
        this.loadFactor = loadFactor;
        this.size = 0;
    }

    public MyHashSet(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public MyHashSet() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    private static class Node<E> {
        E value;
        Node<E> next;
        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private int indexFor(Object o, Node<E>[] tab) {
        int h = (o == null) ? 0 : o.hashCode();
        // получили положительный int (избегаем Integer.MIN_VALUE проблемы)
        int positive = h & 0x7fffffff;
        return positive % tab.length;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
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
        if (needsResize()) {
            resize(table.length * 2);
        }
        int idx = indexFor(e, table);
        Node<E> current = table[idx];
        while (current != null) {
            if (equalsElement(current.value, e)) {
                return false;
            }
            current = current.next;
        }
        Node<E> node = new Node<>(e, table[idx]);
        table[idx] = node;
        size++;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        int idx = indexFor(o, table);
        Node<E> current = table[idx];
        while (current != null) {
            if (equalsElement(current.value, o)) return true;
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexFor(o, table);
        Node<E> current = table[idx];
        Node<E> prev = null;
        while (current != null) {
            if (equalsElement(current.value, o)) {
                if (prev == null) {
                    table[idx] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    private boolean equalsElement(Object a, Object b) {
        if (a == b) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }

    private boolean needsResize() {
        return size + 1 > table.length * loadFactor;
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        if (newCapacity <= 0) newCapacity = 1;
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];
        for (int i = 0; i < table.length; i++) {
            Node<E> curr = table[i];
            while (curr != null) {
                Node<E> next = curr.next;
                int idx = indexFor(curr.value, newTable);
                curr.next = newTable[idx];
                newTable[idx] = curr;
                curr = next;
            }
        }
        table = newTable;
    }


    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int bucketIndex = 0;
            Node<E> current = null;
            Node<E> lastReturned = null;
            {
                advanceToNextNonEmptyBucket();
            }

            private void advanceToNextNonEmptyBucket() {
                while (current == null && bucketIndex < table.length) {
                    current = table[bucketIndex++];
                }
            }

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null) throw new NoSuchElementException();
                E val = current.value;
                lastReturned = current;
                current = current.next;
                if (current == null) advanceToNextNonEmptyBucket();
                return val;
            }

            @Override
            public void remove() {
                if (lastReturned == null) throw new IllegalStateException();
                E toRemove = lastReturned.value;
                lastReturned = null;
                MyHashSet.this.remove(toRemove);
            }
        };
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;
        for (int i = 0; i < table.length; i++) {
            Node<E> curr = table[i];
            while (curr != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(String.valueOf(curr.value));
                first = false;
                curr = curr.next;
            }
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int k = 0;
        for (int i = 0; i < table.length; i++) {
            Node<E> curr = table[i];
            while (curr != null) {
                arr[k++] = curr.value;
                curr = curr.next;
            }
        }
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        int s = size;
        T[] arr = a.length >= s ? a :
                (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), s);
        int k = 0;
        for (int i = 0; i < table.length; i++) {
            Node<E> curr = table[i];
            while (curr != null) {
                arr[k++] = (T) curr.value;
                curr = curr.next;
            }
        }
        if (arr.length > s) arr[s] = null;
        return arr;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;
        for (E e : c) {
            if (add(e)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;
        for (Object o : c) {
            if (remove(o)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;
        for (int i = 0; i < table.length; i++) {
            Node<E> curr = table[i];
            Node<E> prev = null;
            while (curr != null) {
                if (!c.contains(curr.value)) {
                    if (prev == null) {
                        table[i] = curr.next;
                        curr = table[i];
                    } else {
                        prev.next = curr.next;
                        curr = prev.next;
                    }
                    size--;
                    changed = true;
                } else {
                    prev = curr;
                    curr = curr.next;
                }
            }
        }
        return changed;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Set)) return false;
        Set<?> other = (Set<?>) o;
        if (other.size() != this.size()) return false;
        return containsAll(other);
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (int i = 0; i < table.length; i++) {
            Node<E> curr = table[i];
            while (curr != null) {
                E val = curr.value;
                h += (val == null ? 0 : val.hashCode());
                curr = curr.next;
            }
        }
        return h;
    }

}

