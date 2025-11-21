package by.it.group451001.tsurko.lesson11;

import java.util.*;

@SuppressWarnings("unchecked")
public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        E item;
        Node<E> next;

        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int capacity = 16;
    private int size = 0;

    public MyHashSet() {
        table = (Node<E>[]) new Node[capacity];
    }

    private int hash(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;
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
        int index = hash(o);
        Node<E> node = table[index];
        while (node != null) {
            if (Objects.equals(node.item, o))
                return true;
            node = node.next;
        }
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

    @Override
    public boolean add(Object o) {
        if (size > capacity * 0.75) resize();

        int index = hash(o);
        Node<E> node = table[index];
        while (node != null) {
            if (Objects.equals(node.item, o))
                return false;
            node = node.next;
        }
        table[index] = new Node<>((E) o, table[index]);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = hash(o);
        Node<E> node = table[index];
        Node<E> prev = null;

        while (node != null) {
            if (Objects.equals(node.item, o)) {
                if (prev == null)
                    table[index] = node.next;
                else
                    prev.next = node.next;
                size--;
                return true;
            }
            prev = node;
            node = node.next;
        }
        return false;
    }

    @Override
    public void clear() {
        table = (Node<E>[]) new Node[capacity];
        size = 0;
    }

    private void resize() {
        capacity *= 2;
        Node<E>[] newTable = (Node<E>[]) new Node[capacity];
        for (Node<E> oldNode : table) {
            while (oldNode != null) {
                Node<E> next = oldNode.next;
                int newIndex = hash(oldNode.item);
                oldNode.next = newTable[newIndex];
                newTable[newIndex] = oldNode;
                oldNode = next;
            }
        }
        table = newTable;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ", "[", "]");
        for (Node<E> node : table) {
            while (node != null) {
                sj.add(String.valueOf(node.item));
                node = node.next;
            }
        }
        return sj.toString();
    }

    // Методы Set, не проверяемые тестом
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
}
