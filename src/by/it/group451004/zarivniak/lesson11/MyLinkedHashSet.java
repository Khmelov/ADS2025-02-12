package by.it.group451004.zarivniak.lesson11;

import java.util.Collection;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private static class Node<E> {
        E data;
        Node<E> next; // для цепочки в хеш-таблице
        Node<E> after; // для поддержания порядка добавления
        Node<E> before; // для поддержания порядка добавления

        Node(E data) {
            this.data = data;
        }
    }

    private Node<E>[] table;
    private Node<E> head; // первый добавленный элемент
    private Node<E> tail; // последний добавленный элемент
    private int size;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        head = null;
        tail = null;
        size = 0;
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        table = (Node<E>[]) new Node[initialCapacity];
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        Node<E> current = head;
        boolean firstElement = true;
        while (current != null) {
            if (!firstElement) {
                sb.append(", ");
            }
            sb.append(current.data);
            firstElement = false;
            current = current.after;
        }

        sb.append("]");
        return sb.toString();
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
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean contains(Object element) {
        if (element == null) {
            return false;
        }

        int index = getIndex(element);
        Node<E> current = table[index];

        while (current != null) {
            if (element.equals(current.data)) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        if (contains(element)) {
            return false;
        }

        if (size + 1 > table.length * LOAD_FACTOR) {
            resize();
        }

        int index = getIndex(element);
        Node<E> newNode = new Node<>(element);

        // Добавляем в хеш-таблицу (в начало цепочки)
        newNode.next = table[index];
        table[index] = newNode;

        // Добавляем в двусвязный список для сохранения порядка
        linkToEnd(newNode);
        size++;

        return true;
    }

    @Override
    public boolean remove(Object element) {
        if (element == null) {
            return false;
        }

        int index = getIndex(element);
        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if (element.equals(current.data)) {
                // Удаляем из хеш-таблицы
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }

                // Удаляем из двусвязного списка
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
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object element : c) {
            if (remove(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;

        // Создаем копию списка элементов в порядке добавления
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.after;
            if (!c.contains(current.data)) {
                remove(current.data);
                modified = true;
            }
            current = next;
        }

        return modified;
    }

    private int getIndex(Object element) {
        int hashCode = element.hashCode();
        return (hashCode & 0x7FFFFFFF) % table.length;
    }

    private void linkToEnd(Node<E> node) {
        if (head == null) {
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
        if (node.before == null) {
            // Удаляем первый элемент
            head = node.after;
        } else {
            node.before.after = node.after;
        }

        if (node.after == null) {
            // Удаляем последний элемент
            tail = node.before;
        } else {
            node.after.before = node.before;
        }

        // Очищаем ссылки
        node.before = null;
        node.after = null;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        table = (Node<E>[]) new Node[oldTable.length * 2];

        // Перехеширование всех элементов
        Node<E> current = head;
        while (current != null) {
            int newIndex = getIndex(current.data);
            // Временно сохраняем ссылки на следующий в порядке добавления
            Node<E> afterInOrder = current.after;

            // Перемещаем в новую таблицу (сохраняем порядок в цепочке)
            current.next = table[newIndex];
            table[newIndex] = current;

            current = afterInOrder;
        }
    }

    @Override
    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException("iterator");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("toArray");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("toArray");
    }
}