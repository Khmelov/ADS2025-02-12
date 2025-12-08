package by.it.group451002.popeko.lesson11;

import java.util.*;

public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private Node<E> head; // первый элемент в порядке добавления
    private Node<E> tail; // последний элемент в порядке добавления
    private int size;
    private final float loadFactor;
    private int threshold;

    // Узел для двусвязного списка (сохраняет порядок добавления)
    private static class Node<E> {
        final E data;
        final int hash;
        Node<E> next; // следующий в цепочке коллизий
        Node<E> before, after; // ссылки для поддержания порядка добавления

        Node(E data, int hash, Node<E> next) {
            this.data = data;
            this.hash = hash;
            this.next = next;
        }
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
        loadFactor = DEFAULT_LOAD_FACTOR;
        threshold = (int)(DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.after != null) {
                sb.append(", ");
            }
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
        head = tail = null;
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

        // Создаем новый узел
        Node<E> newNode = new Node<>(e, hash, table[index]);

        // Добавляем в хеш-таблицу
        table[index] = newNode;

        // Добавляем в двусвязный список (в конец)
        linkNodeLast(newNode);
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
                // Удаляем из цепочки коллизий
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }

                // Удаляем из двусвязного списка
                unlinkNode(current);
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
        // Удаляем элементы по одному, чтобы сохранить порядок
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.after;
            if (c.contains(current.data)) {
                remove(current.data);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        // Удаляем элементы по одному, чтобы сохранить порядок
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

    // Вспомогательные методы для двусвязного списка
    private void linkNodeLast(Node<E> node) {
        if (tail == null) {
            head = tail = node;
        } else {
            tail.after = node;
            node.before = tail;
            tail = node;
        }
    }

    private void unlinkNode(Node<E> node) {
        Node<E> before = node.before;
        Node<E> after = node.after;

        if (before == null) {
            head = after;
        } else {
            before.after = after;
            node.before = null;
        }

        if (after == null) {
            tail = before;
        } else {
            after.before = before;
            node.after = null;
        }
    }

    // Вспомогательные методы для хеш-таблицы
    private int hash(Object key) {
        int h = key.hashCode();
        return h ^ (h >>> 16);
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
}