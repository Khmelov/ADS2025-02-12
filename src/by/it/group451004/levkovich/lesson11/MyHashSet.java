package by.it.group451004.levkovich.lesson11;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

public class MyHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    // Внутренний класс для узла связного списка
    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
        }
    }

    private Node<E>[] buckets;
    private int size;


    public MyHashSet() {
        buckets = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    // Вспомогательные методы
    private int hash(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % buckets.length;
    }

    private boolean equals(Object a, Object b) {
        if (a == b) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }

    private void resize() {
        if (size < LOAD_FACTOR * buckets.length) return;

        Node<E>[] oldBuckets = buckets;
        buckets = new Node[oldBuckets.length * 2];
        size = 0;

        for (Node<E> bucket : oldBuckets) {
            Node<E> current = bucket;
            while (current != null) {
                add(current.data);
                current = current.next;
            }
        }
    }

    // Обязательные методы
    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        int index = hash(element);
        Node<E> current = buckets[index];

        // Проверяем, есть ли уже такой элемент
        while (current != null) {
            if (equals(current.data, element)) {
                return false;
            }
            current = current.next;
        }

        // Добавляем новый элемент в начало цепочки
        Node<E> newNode = new Node<>(element);
        newNode.next = buckets[index];
        buckets[index] = newNode;
        size++;

        // Проверяем необходимость увеличения размера
        resize();
        return true;
    }

    @Override
    public boolean remove(Object element) {
        int index = hash(element);
        Node<E> current = buckets[index];
        Node<E> prev = null;

        while (current != null) {
            if (equals(current.data, element)) {
                if (prev == null) {
                    buckets[index] = current.next;
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

    @Override
    public boolean contains(Object element) {
        int index = hash(element);
        Node<E> current = buckets[index];

        while (current != null) {
            if (equals(current.data, element)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        boolean first = true;

        for (Node<E> bucket : buckets) {
            Node<E> current = bucket;
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(current.data);
                first = false;
                current = current.next;
            }
        }

        sb.append("]");
        return sb.toString();
    }

    // остальные
    @Override
    public Iterator<E> iterator() {
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
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
}