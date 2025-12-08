package by.it.group410902.yarmashuk.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {


    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
            this.next = null;
        }
    }

    private static final int DEFAULT_INITIAL_CAPACITY = 16; // Емкость по умолчанию
    private static final float DEFAULT_LOAD_FACTOR = 0.75f; // Фактор загрузки для автоматического расширения

    private Node<E>[] table; // Массив бакетов (корзин), каждый из которых - голова односвязного списка
    private int size; // Текущее количество элементов в наборе
    private int capacity; // количество бакетов
    private float loadFactor; // Фактор загрузки, при превышении которого происходит расширение

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        this.capacity = DEFAULT_INITIAL_CAPACITY;
        this.table = (Node<E>[]) new Node[capacity];
        this.size = 0;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }

    @SuppressWarnings("unchecked")
    public MyHashSet(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive.");
        }
        this.capacity = initialCapacity;
        this.table = (Node<E>[]) new Node[capacity];
        this.size = 0;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }
    private int getBucketIndex(Object o) {
        int hash = (o == null) ? 0 : o.hashCode();
        return (hash & 0x7fffffff) % capacity; // Ensures positive index
    }



    @Override
    public int size() {
        return size;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {

        this.table = (Node<E>[]) new Node[capacity];
        this.size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) {
            return false;
        }


        if (size >= capacity * loadFactor) {
            resize();
        }

        int bucketIndex = getBucketIndex(e);
        Node<E> newNode = new Node<>(e);
        newNode.next = table[bucketIndex]; // Добавляем новый узел в начало списка
        table[bucketIndex] = newNode;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int bucketIndex = getBucketIndex(o);
        Node<E> current = table[bucketIndex];
        Node<E> previous = null;

        while (current != null) {
            boolean found = false;
            if (o == null) {
                if (current.data == null) {
                    found = true;
                }
            } else {
                if (o.equals(current.data)) {
                    found = true;
                }
            }

            if (found) {
                if (previous == null) {
                    // Удаляем головной узел
                    table[bucketIndex] = current.next;
                } else {
                    // Удаляем узел из середины или конца списка
                    previous.next = current.next;
                }
                size--;
                return true;
            }

            previous = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        int bucketIndex = getBucketIndex(o);
        Node<E> current = table[bucketIndex];
        while (current != null) {
            if (o == null) {
                if (current.data == null) {
                    return true;
                }
            } else {
                if (o.equals(current.data)) {
                    return true;
                }
            }
            current = current.next;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int oldCapacity = capacity;
        capacity *= 2;
        Node<E>[] oldTable = table;
        table = (Node<E>[]) new Node[capacity];
        size = 0;

        for (int i = 0; i < oldCapacity; i++) {
            Node<E> current = oldTable[i];
            while (current != null) {
                add(current.data);
                current = current.next;
            }
        }
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean firstElement = true;

        for (int i = 0; i < capacity; i++) {
            Node<E> current = table[i];
            while (current != null) {
                if (!firstElement) {
                    sb.append(", ");
                }
                sb.append(current.data);
                firstElement = false;
                current = current.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }
    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }
    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

}

