package by.it.group451004.momotyuk.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<E>[] table;
    private Node<E> head; // первый добавленный элемент
    private Node<E> tail; // последний добавленный элемент
    private int size;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = new Node[DEFAULT_CAPACITY];
        head = null;
        tail = null;
        size = 0;
    }

    // Внутренний класс для узла двусвязного списка (для сохранения порядка)
    private static class Node<E> {
        E data;
        Node<E> next; // следующий в хеш-таблице (для коллизий)
        Node<E> after; // следующий в порядке добавления
        Node<E> before; // предыдущий в порядке добавления

        Node(E data) {
            this.data = data;
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
    @SuppressWarnings("unchecked")
    public void clear() {
        table = new Node[DEFAULT_CAPACITY];
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }

        int index = getIndex(o);
        Node<E> current = table[index];

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
            throw new NullPointerException("Null elements are not allowed");
        }

        if (contains(e)) {
            return false;
        }

        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }

        int index = getIndex(e);
        Node<E> newNode = new Node<>(e);

        // Добавляем в хеш-таблицу
        newNode.next = table[index];
        table[index] = newNode;

        // Добавляем в конец списка порядка
        linkLast(newNode);
        size++;

        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }

        int index = getIndex(o);
        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if (o.equals(current.data)) {
                // Удаляем из хеш-таблицы
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }

                // Удаляем из списка порядка
                unlink(current);
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
        Node<E> current = head;
        int count = 0;

        while (current != null) {
            sb.append(current.data);
            count++;
            if (count < size) {
                sb.append(", ");
            }
            current = current.after;
        }

        sb.append("]");
        return sb.toString();
    }

    // Методы для работы с порядком элементов

    private void linkLast(Node<E> node) {
        if (tail == null) {
            // Первый элемент
            head = node;
            tail = node;
        } else {
            // Добавляем в конец
            tail.after = node;
            node.before = tail;
            tail = node;
        }
    }

    private void unlink(Node<E> node) {
        Node<E> prev = node.before;
        Node<E> next = node.after;

        if (prev == null) {
            // Удаляем первый элемент
            head = next;
        } else {
            prev.after = next;
            node.before = null;
        }

        if (next == null) {
            // Удаляем последний элемент
            tail = prev;
        } else {
            next.before = prev;
            node.after = null;
        }
    }

    // Вспомогательные методы

    private int getIndex(Object o) {
        return Math.abs(o.hashCode()) % table.length;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        size = 0;

        // Сохраняем порядок при ресайзе
        Node<E> current = head;
        head = null;
        tail = null;

        while (current != null) {
            add(current.data);
            current = current.after;
        }
    }

    // Методы работы с коллекциями

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> iterator = new LinkedIterator();
        while (iterator.hasNext()) {
            E element = iterator.next();
            if (!c.contains(element)) {
                iterator.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object element : c) {
            if (remove(element)) {
                modified = true;
            }
        }
        return modified;
    }

    // Итератор для сохранения порядка

    private class LinkedIterator implements Iterator<E> {
        private Node<E> current = head;
        private Node<E> lastReturned = null;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            lastReturned = current;
            current = current.after;
            return lastReturned.data;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            MyLinkedHashSet.this.remove(lastReturned.data);
            lastReturned = null;
        }
    }

    // Методы интерфейса Set, которые не требуются по заданию

    @Override
    public Iterator<E> iterator() {
        return new LinkedIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int index = 0;
        Node<E> current = head;
        while (current != null) {
            array[index++] = current.data;
            current = current.after;
        }
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) toArray();
        }
        int index = 0;
        Node<E> current = head;
        while (current != null) {
            a[index++] = (T) current.data;
            current = current.after;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}