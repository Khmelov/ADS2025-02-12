package by.it.group451001.khomenkov.lesson11;

import java.util.Set;

public class MyHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<E>[] buckets;
    private int size;
    private final float loadFactor;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        buckets = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
        loadFactor = DEFAULT_LOAD_FACTOR;
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
    @SuppressWarnings("unchecked")
    public void clear() {
        buckets = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            throw new NullPointerException("Cannot check for null element");
        }

        int bucketIndex = getBucketIndex(o);
        Node<E> current = buckets[bucketIndex];

        while (current != null) {
            if (o.equals(current.data)) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException("Cannot add null element");
        }

        if (contains(e)) {
            return false;
        }

        ensureCapacity();

        int bucketIndex = getBucketIndex(e);
        Node<E> newNode = new Node<>(e);

        // Вставляем в начало списка
        newNode.next = buckets[bucketIndex];
        buckets[bucketIndex] = newNode;
        size++;

        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            throw new NullPointerException("Cannot remove null element");
        }

        int bucketIndex = getBucketIndex(o);
        Node<E> current = buckets[bucketIndex];
        Node<E> prev = null;

        while (current != null) {
            if (o.equals(current.data)) {
                if (prev == null) {
                    // Удаляем первый элемент в списке
                    buckets[bucketIndex] = current.next;
                } else {
                    // Удаляем из середины или конца
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

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        int count = 0;

        for (Node<E> bucket : buckets) {
            Node<E> current = bucket;
            while (current != null) {
                sb.append(current.data);
                count++;
                if (count < size) {
                    sb.append(", ");
                }
                current = current.next;
            }
        }

        sb.append("]");
        return sb.toString();
    }

    private int getBucketIndex(Object o) {
        int hashCode = o.hashCode();
        return Math.abs(hashCode) % buckets.length;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if ((float) size / buckets.length > loadFactor) {
            // Увеличиваем емкость в 2 раза
            Node<E>[] oldBuckets = buckets;
            buckets = (Node<E>[]) new Node[oldBuckets.length * 2];
            size = 0;

            // Перехешируем все элементы
            for (Node<E> bucket : oldBuckets) {
                Node<E> current = bucket;
                while (current != null) {
                    add(current.data);
                    current = current.next;
                }
            }
        }
    }

    // Остальные методы интерфейса Set (необязательные для реализации)
    @Override
    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }
}
