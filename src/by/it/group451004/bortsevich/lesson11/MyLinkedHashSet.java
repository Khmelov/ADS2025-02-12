package by.it.group451004.bortsevich.lesson11;

import java.util.Collection;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private Node<E> head; // голова двусвязного списка для сохранения порядка добавления
    private Node<E> tail; // хвост двусвязного списка
    private int size;
    private int capacity;
    private final float loadFactor;

    // Узел для хранения элементов
    private static class Node<E> {
        E element;
        int hash;
        Node<E> next; // следующий в цепочке коллизий
        Node<E> before; // предыдущий в порядке добавления
        Node<E> after; // следующий в порядке добавления

        Node(E element, int hash, Node<E> next) {
            this.element = element;
            this.hash = hash;
            this.next = next;
        }
    }

    public MyLinkedHashSet() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int capacity, float loadFactor) {
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        this.table = (Node<E>[]) new Node[capacity];
        this.size = 0;
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
        for (int i = 0; i < capacity; i++) {
            table[i] = null;
        }
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }

        int hash = hash(o);
        int index = indexFor(hash, capacity);

        Node<E> node = table[index];
        while (node != null) {
            if (node.hash == hash && (node.element == o || node.element.equals(o))) {
                return true;
            }
            node = node.next;
        }

        return false;
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        int hash = hash(e);
        int index = indexFor(hash, capacity);

        // Проверяем, существует ли элемент
        Node<E> node = table[index];
        while (node != null) {
            if (node.hash == hash && (node.element == e || node.element.equals(e))) {
                return false; // элемент уже существует
            }
            node = node.next;
        }

        // Добавляем новый элемент
        addElement(e, hash, index);

        // Проверяем необходимость расширения
        if (size >= capacity * loadFactor) {
            resize();
        }

        return true;
    }

    private void addElement(E e, int hash, int index) {
        Node<E> newNode = new Node<>(e, hash, table[index]);
        table[index] = newNode;

        // Добавляем в список порядка добавления
        linkNodeLast(newNode);
        size++;
    }

    // Добавляет узел в конец списка порядка добавления
    private void linkNodeLast(Node<E> node) {
        if (tail == null) {
            head = node;
        } else {
            tail.after = node;
            node.before = tail;
        }
        tail = node;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }

        int hash = hash(o);
        int index = indexFor(hash, capacity);

        Node<E> prev = null;
        Node<E> node = table[index];

        while (node != null) {
            if (node.hash == hash && (node.element == o || node.element.equals(o))) {
                // Удаляем из хеш-таблицы
                if (prev == null) {
                    table[index] = node.next;
                } else {
                    prev.next = node.next;
                }

                // Удаляем из списка порядка добавления
                unlinkNode(node);
                size--;
                return true;
            }
            prev = node;
            node = node.next;
        }

        return false;
    }

    // Удаляет узел из списка порядка добавления
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
        Node<E> current = head;

        while (current != null) {
            Node<E> next = current.after;
            if (!c.contains(current.element)) {
                remove(current.element);
                modified = true;
            }
            current = next;
        }

        return modified;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        Node<E> current = head;
        while (current != null) {
            sb.append(current.element);
            if (current.after != null) {
                sb.append(", ");
            }
            current = current.after;
        }

        sb.append("]");
        return sb.toString();
    }

    // Вспомогательные методы

    private int hash(Object key) {
        int h = key.hashCode();
        return h ^ (h >>> 16); // улучшаем распределение
    }

    private int indexFor(int hash, int capacity) {
        return hash & (capacity - 1);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = capacity * 2;
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];

        // Перехешируем все элементы
        Node<E> current = head;
        while (current != null) {
            int newIndex = indexFor(current.hash, newCapacity);

            // Сохраняем следующий элемент перед изменением
            Node<E> nextInOrder = current.after;

            // Вставляем в новую таблицу
            current.next = newTable[newIndex];
            newTable[newIndex] = current;

            current = nextInOrder;
        }

        table = newTable;
        capacity = newCapacity;
    }

    // Методы, которые не требуются по заданию, но необходимы для интерфейса

    @Override
    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException("Iterator not implemented");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("toArray not implemented");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("toArray not implemented");
    }

    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException("equals not implemented");
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("hashCode not implemented");
    }
}