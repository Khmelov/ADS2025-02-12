package by.it.group410901.kliaus.lesson11;

import java.util.*;

/**
 * Реализация LinkedHashSet без использования встроенных коллекций.
 * Сохраняет порядок вставки элементов.
 */
public class MyLinkedHashSet implements Collection<Number> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node[] table;
    private int size = 0;
    private int threshold;

    // Для порядка вставки
    private Node first;
    private Node last;

    public MyLinkedHashSet() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    private static class Node {
        final Number value;
        Node nextHash;   // для цепочек в таблице
        Node prevLink;   // для порядка вставки
        Node nextLink;

        Node(Number value, Node nextHash) {
            this.value = value;
            this.nextHash = nextHash;
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
            for (Node bucket : table) {
                while (bucket != null) {
                    Node next = bucket.nextHash;
                    int idx = indexFor(bucket.value, newCap);
                    bucket.nextHash = newTable[idx];
                    newTable[idx] = bucket;
                    bucket = next;
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
            node = node.nextHash;
        }
        return false;
    }

    @Override
    public Iterator<Number> iterator() {
        return new Iterator<>() {
            Node current = first;
            Node lastReturned = null;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Number next() {
                if (current == null) throw new NoSuchElementException();
                lastReturned = current;
                current = current.nextLink;
                return lastReturned.value;
            }

            @Override
            public void remove() {
                if (lastReturned == null) throw new IllegalStateException();
                MyLinkedHashSet.this.remove(lastReturned.value);
                lastReturned = null;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;
        for (Node n = first; n != null; n = n.nextLink) {
            arr[i++] = n.value;
        }
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        Object[] arr = toArray();
        if (a.length < arr.length)
            return (T[]) Arrays.copyOf(arr, arr.length, a.getClass());
        System.arraycopy(arr, 0, a, 0, arr.length);
        if (a.length > arr.length) a[arr.length] = null;
        return a;
    }

    @Override
    public boolean add(Number number) {
        ensureCapacity();
        int idx = indexFor(number, table.length);
        Node node = table[idx];
        while (node != null) {
            if (Objects.equals(node.value, number)) return false; // уже есть
            node = node.nextHash;
        }

        Node newNode = new Node(number, table[idx]);
        table[idx] = newNode;
        size++;

        // вставляем в конец связанного списка
        if (first == null) {
            first = last = newNode;
        } else {
            last.nextLink = newNode;
            newNode.prevLink = last;
            last = newNode;
        }

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
                    table[idx] = node.nextHash;
                } else {
                    prev.nextHash = node.nextHash;
                }

                // удаляем из связанного списка
                if (node.prevLink != null) node.prevLink.nextLink = node.nextLink;
                else first = node.nextLink;

                if (node.nextLink != null) node.nextLink.prevLink = node.prevLink;
                else last = node.prevLink;

                size--;
                return true;
            }
            prev = node;
            node = node.nextHash;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Number> c) {
        boolean changed = false;
        for (Number n : c)
            if (add(n)) changed = true;
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
        List<Number> toRemove = new ArrayList<>();
        for (Node n = first; n != null; n = n.nextLink)
            if (!c.contains(n.value)) toRemove.add(n.value);
        for (Number n : toRemove)
            if (remove(n)) changed = true;
        return changed;
    }

    @Override
    public void clear() {
        Arrays.fill(table, null);
        size = 0;
        first = last = null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean firstElem = true;
        for (Node n = first; n != null; n = n.nextLink) {
            if (!firstElem) sb.append(", ");
            sb.append(n.value);
            firstElem = false;
        }
        sb.append("]");
        return sb.toString();
    }
}
