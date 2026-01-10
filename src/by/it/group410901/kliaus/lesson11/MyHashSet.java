package by.it.group410901.kliaus.lesson11;

import java.util.*;

/**
 * Простейшая реализация HashSet для Number
 * Поддерживает: size(), clear(), isEmpty(), add(Number), remove(Object), contains(Object), toString()
 *
 * Не использует поля типа Collection (только собственный массив Node[]).
 */
public class MyHashSet implements Collection<Number> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node[] table;
    private int size = 0;
    private int threshold;

    public MyHashSet() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    private static class Node {
        final Number value;
        Node next;

        Node(Number value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    private int indexFor(Object o, int length) {
        int h = (o == null) ? 0 : o.hashCode();
        return (h & 0x7fffffff) % length;
    }

    private void ensureCapacity() {
        if (size >= threshold) {
            int newCap = table.length * 2;
            Node[] newTable = new Node[newCap];
            for (Node node : table) {
                while (node != null) {
                    Node next = node.next;
                    int idx = indexFor(node.value, newCap);
                    node.next = newTable[idx];
                    newTable[idx] = node;
                    node = next;
                }
            }
            table = newTable;
            threshold = (int) (newCap * LOAD_FACTOR);
        }
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
        Node node = table[idx];
        while (node != null) {
            if (Objects.equals(node.value, o)) return true;
            node = node.next;
        }
        return false;
    }

    @Override
    public Iterator<Number> iterator() {
        return new Iterator<>() {
            int bucket = 0;
            Node current = null;
            Node lastReturned = null;

            {
                advanceToNext();
            }

            private void advanceToNext() {
                while (current == null && bucket < table.length) {
                    current = table[bucket++];
                }
            }

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Number next() {
                if (current == null) throw new NoSuchElementException();
                Number val = current.value;
                lastReturned = current;
                current = current.next;
                if (current == null) advanceToNext();
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
        Object[] res = new Object[size];
        int i = 0;
        for (Node bucket : table) {
            Node node = bucket;
            while (node != null) {
                res[i++] = node.value;
                node = node.next;
            }
        }
        return res;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        Object[] arr = toArray();
        if (a.length < arr.length) {
            //noinspection unchecked
            return (T[]) Arrays.copyOf(arr, arr.length, a.getClass());
        }
        System.arraycopy(arr, 0, a, 0, arr.length);
        if (a.length > arr.length) a[arr.length] = null;
        //noinspection unchecked
        return a;
    }

    @Override
    public boolean add(Number number) {
        ensureCapacity();
        int idx = indexFor(number, table.length);
        Node node = table[idx];
        while (node != null) {
            if (Objects.equals(node.value, number)) return false; // already present
            node = node.next;
        }
        table[idx] = new Node(number, table[idx]);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexFor(o, table.length);
        Node node = table[idx];
        Node prev = null;
        while (node != null) {
            if (Objects.equals(node.value, o)) {
                if (prev == null) {
                    table[idx] = node.next;
                } else {
                    prev.next = node.next;
                }
                size--;
                return true;
            }
            prev = node;
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) if (!contains(e)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Number> c) {
        boolean changed = false;
        for (Number n : c) {
            if (add(n)) changed = true;
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
        List<Number> toRemove = new ArrayList<>();
        for (Node bucket : table) {
            Node node = bucket;
            while (node != null) {
                if (!c.contains(node.value)) toRemove.add(node.value);
                node = node.next;
            }
        }
        for (Number r : toRemove) {
            if (remove(r)) changed = true;
        }
        return changed;
    }

    @Override
    public void clear() {
        Arrays.fill(table, null);
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node bucket : table) {
            Node node = bucket;
            while (node != null) {
                if (!first) sb.append(", ");
                sb.append(node.value);
                first = false;
                node = node.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // Optional operations not supported: we implemented the common ones above.
}
