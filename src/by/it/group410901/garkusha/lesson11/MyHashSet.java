package by.it.group410901.garkusha.lesson11;

import java.util.*;

class MyHashSet<E> implements Set<E> {
    private Node<E>[] table;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    // Внутренний класс для узла связного списка
    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
        }
    }

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
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
    public boolean add(E element) {
        int index = getIndex(element);
        Node<E> current = table[index];

        // Проверяем, нет ли уже такого элемента
        while (current != null) {
            if (Objects.equals(current.data, element)) {
                return false; // элемент уже есть
            }
            current = current.next;
        }

        // Добавляем новый элемент в начало списка
        Node<E> newNode = new Node<>(element);
        newNode.next = table[index];
        table[index] = newNode;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object element) {
        int index = getIndex(element);
        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if (Objects.equals(current.data, element)) {
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
    public boolean contains(Object element) {
        int index = getIndex(element);
        Node<E> current = table[index];

        while (current != null) {
            if (Objects.equals(current.data, element)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        String result = "[";
        boolean first = true;

        for (Node<E> node : table) {
            Node<E> current = node;
            while (current != null) {
                if (!first) {
                    result += ", ";
                }
                result += current.data;
                first = false;
                current = current.next;
            }
        }

        result += "]";
        return result;
    }

    private int getIndex(Object element) {
        if (element == null) {
            return 0;
        }
        return Math.abs(element.hashCode()) % table.length;
    }

    // Остальные методы интерфейса Set
    @Override
    public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override
    public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override
    public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override
    public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override
    public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override
    public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override
    public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
}