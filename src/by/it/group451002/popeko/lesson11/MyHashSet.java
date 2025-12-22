package by.it.group451002.popeko.lesson11;

import java.util.*;

public class MyHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size;
    private final float loadFactor;
    private int threshold;

    // Узел односвязного списка для разрешения коллизий
    private static class Node<E> {
        final E data;
        final int hash;
        Node<E> next;

        Node(E data, int hash, Node<E> next) {
            this.data = data;
            this.hash = hash;
            this.next = next;
        }
    }

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
        loadFactor = DEFAULT_LOAD_FACTOR;
        threshold = (int)(DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
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
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;

        int hash = hash(o);
        int index = (table.length - 1) & hash;

        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash && Objects.equals(o, current.data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        int hash = hash(e);
        int index = (table.length - 1) & hash;

        // Проверяем, есть ли уже такой элемент
        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash && Objects.equals(e, current.data)) {
                return false; // Элемент уже существует
            }
            current = current.next;
        }

        // Добавляем новый элемент в начало цепочки
        table[index] = new Node<>(e, hash, table[index]);
        size++;

        // Проверяем необходимость расширения таблицы
        if (size > threshold) {
            resize();
        }

        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;

        int hash = hash(o);
        int index = (table.length - 1) & hash;

        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if (current.hash == hash && Objects.equals(o, current.data)) {
                if (prev == null) {
                    table[index] = current.next;
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
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        boolean first = true;

        for (Node<E> node : table) {
            Node<E> current = node;
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

    // Вспомогательные методы
    private int hash(Object key) {
        int h = key.hashCode();
        return h ^ (h >>> 16); // Распределение битов для лучшего хеширования
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * 2;
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];

        // Перехеширование всех элементов
        for (Node<E> node : table) {
            Node<E> current = node;
            while (current != null) {
                Node<E> next = current.next;
                int newIndex = (newCapacity - 1) & current.hash;
                current.next = newTable[newIndex];
                newTable[newIndex] = current;
                current = next;
            }
        }

        table = newTable;
        threshold = (int)(newCapacity * loadFactor);
    }

    // Остальные методы интерфейса Set (не реализованы)
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