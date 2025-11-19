package by.it.group410901.zubchonak.lesson11;

import java.util.*;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> nextInBucket;     // для хеш-таблицы
        Node<E> nextInOrder;      // для порядка вставки
        Node(E value) {
            this.value = value;
        }
    }

    private static final int INITIAL_CAPACITY = 16;
    private Node<E>[] buckets;
    private Node<E> firstOrder = null;
    private Node<E> lastOrder = null;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        buckets = new Node[INITIAL_CAPACITY];
    }

    private int hash(Object key) {
        int h = key.hashCode();
        return (h ^ (h >>> 16)) & (buckets.length - 1);
    }

    private void resize() {
        Node<E>[] oldBuckets = buckets;
        @SuppressWarnings("unchecked")
        Node<E>[] newBuckets = new Node[oldBuckets.length * 2];
        buckets = newBuckets;

        // Сохраняем порядок: перебираем по цепочке вставки
        Node<E> current = firstOrder;
        firstOrder = null;
        lastOrder = null;
        size = 0;

        while (current != null) {
            add(current.value);
            current = current.nextInOrder;
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
        if (o == null) return false;
        int idx = hash(o);
        Node<E> node = buckets[idx];
        while (node != null) {
            if (o.equals(node.value)) {
                return true;
            }
            node = node.nextInBucket;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if (e == null) return false;
        if (contains(e)) {
            return false; // ← дубликат → false
        }

        if (size >= buckets.length * 0.75) {
            resize();
        }

        int idx = hash(e);
        Node<E> newNode = new Node<>(e);
        // Вставка в начало bucket'а
        newNode.nextInBucket = buckets[idx];
        buckets[idx] = newNode;

        // Вставка в конец цепочки порядка
        if (lastOrder == null) {
            firstOrder = lastOrder = newNode;
        } else {
            lastOrder.nextInOrder = newNode;
            lastOrder = newNode;
        }

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        int idx = hash(o);
        Node<E> current = buckets[idx];
        Node<E> prevInBucket = null;

        while (current != null) {
            if (o.equals(current.value)) {
                // Удаление из bucket
                if (prevInBucket == null) {
                    buckets[idx] = current.nextInBucket;
                } else {
                    prevInBucket.nextInBucket = current.nextInBucket;
                }

                // Удаление из цепочки порядка
                if (firstOrder == current) {
                    firstOrder = current.nextInOrder;
                } else {
                    Node<E> prevInOrder = firstOrder;
                    while (prevInOrder != null && prevInOrder.nextInOrder != current) {
                        prevInOrder = prevInOrder.nextInOrder;
                    }
                    if (prevInOrder != null) {
                        prevInOrder.nextInOrder = current.nextInOrder;
                    }
                }
                if (lastOrder == current) {
                    lastOrder = (firstOrder == null) ? null : findNewLast();
                }

                size--;
                return true;
            }
            prevInBucket = current;
            current = current.nextInBucket;
        }
        return false;
    }

    private Node<E> findNewLast() {
        if (firstOrder == null) return null;
        Node<E> node = firstOrder;
        while (node.nextInOrder != null) {
            node = node.nextInOrder;
        }
        return node;
    }

    @Override
    public void clear() {
        buckets = new Node[buckets.length];
        firstOrder = null;
        lastOrder = null;
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = firstOrder;
        boolean first = true;
        while (current != null) {
            if (!first) sb.append(", ");
            sb.append(current.value);
            first = false;
            current = current.nextInOrder;
        }
        return sb.append("]").toString();
    }

    // === Collection methods ===

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        Object[] toRemove = new Object[size];
        int count = 0;
        Node<E> current = firstOrder;
        while (current != null) {
            if (c.contains(current.value)) {
                toRemove[count++] = current.value;
            }
            current = current.nextInOrder;
        }
        for (int i = 0; i < count; i++) {
            if (remove(toRemove[i])) modified = true;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Object[] toRemove = new Object[size];
        int count = 0;
        Node<E> current = firstOrder;
        while (current != null) {
            if (!c.contains(current.value)) {
                toRemove[count++] = current.value;
            }
            current = current.nextInOrder;
        }
        for (int i = 0; i < count; i++) {
            if (remove(toRemove[i])) modified = true;
        }
        return modified;
    }

    // === Остальные методы Set ===
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
}