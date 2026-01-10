package by.it.group410901.bukshta.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    public MyHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    private int hash(Object key) {
        return key == null ? 0 : key.hashCode() & 0x7FFFFFFF;
    }
//достиг ли size порога загрузки
// если да, удваивает размер хеш-таблицы, перераспределяя элементы.
    private void resizeIfNeeded() {
        if (size >= table.length * LOAD_FACTOR) {
            Node<E>[] oldTable = table;
            table = (Node<E>[]) new Node[table.length * 2];
            size = 0;

            for (Node<E> bucket : oldTable) {
                Node<E> node = bucket;
                while (node != null) {
                    add(node.value);
                    node = node.next;
                }
            }
        }
    }

    @Override
    public boolean add(E e) {
        resizeIfNeeded();
        int index = hash(e) % table.length;
        Node<E> node = table[index];
        while (node != null) {
            if ((e == null && node.value == null) || (e != null && e.equals(node.value))) {
                return false; // элемент уже есть
            }
            node = node.next;
        }
        table[index] = new Node<>(e, table[index]);
        size++;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        int index = hash(o) % table.length;
        Node<E> node = table[index];
        while (node != null) {
            if ((o == null && node.value == null) || (o != null && o.equals(node.value))) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        int index = hash(o) % table.length;
        Node<E> node = table[index];
        Node<E> prev = null;
        while (node != null) {
            if ((o == null && node.value == null) || (o != null && o.equals(node.value))) {
                if (prev == null) {
                    table[index] = node.next;
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
    public int size() { return size; }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) table[i] = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Node<E> bucket : table) {
            Node<E> node = bucket;
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

    // -----------------------------
    // Методы Set/Collection, не обязательные для уровня А
    // -----------------------------
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}

