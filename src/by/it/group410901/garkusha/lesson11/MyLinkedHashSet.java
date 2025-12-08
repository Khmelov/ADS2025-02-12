package by.it.group410901.garkusha.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private Node<E> head; // Первый добавленный элемент
    private Node<E> tail; // Последний добавленный элемент
    private int size;
    private int capacity;
    private final float loadFactor; // Коэффициент загрузки

    // Внутренний класс для узлов
    private static class Node<E> {
        E element;
        int hash;
        Node<E> nextInBucket; // Следующий элемент в цепочке коллизий
        Node<E> nextInOrder; // Следующий элемент в порядке добавления

        Node(E element, int hash, Node<E> nextInBucket) {
            this.element = element;
            this.hash = hash;
            this.nextInBucket = nextInBucket;
        }
    }

    // Конструкторы
    public MyLinkedHashSet() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyLinkedHashSet(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }

        this.capacity = initialCapacity > 0 ? initialCapacity : DEFAULT_CAPACITY;
        this.loadFactor = loadFactor;
        this.table = (Node<E>[]) new Node[capacity];
        this.size = 0;
    }

    // Вспомогательные методы
    private int hash(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private void ensureCapacity() {
        // capacity * loadFactor - максимальное количество элементов перед расширением
        if (size >= capacity * loadFactor) {
            resize();
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = capacity * 2;
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];

        // Перехеширование всех элементов
        for (int i = 0; i < capacity; i++) {
            Node<E> current = table[i];
            while (current != null) {
                Node<E> next = current.nextInBucket;
                int newIndex = Math.abs(current.hash) % newCapacity;
                current.nextInBucket = newTable[newIndex];
                newTable[newIndex] = current;
                current = next;
            }
        }

        table = newTable;
        capacity = newCapacity;
    }

    private void linkLast(Node<E> node) {
        // Добавление в конец односвязного списка порядка добавления
        if (tail == null) {
            head = tail = node;
        } else {
            tail.nextInOrder = node;
            tail = node;
        }
    }

    private Node<E> findPreviousInOrder(Node<E> node) {
        // Поиск предыдущего узла в порядке добавления (для удаления)
        if (node == head) {
            return null;
        }

        Node<E> current = head;
        while (current != null && current.nextInOrder != node) {
            current = current.nextInOrder;
        }
        return current;
    }

    private void unlink(Node<E> node) {
        // Удаление из односвязного списка порядка добавления
        Node<E> prev = findPreviousInOrder(node);

        if (prev == null) {
            // Удаляем голову
            head = node.nextInOrder;
        } else {
            prev.nextInOrder = node.nextInOrder;
        }

        if (node == tail) {
            tail = prev;
        }

        node.nextInOrder = null;
    }

    // Реализация методов интерфейса Set

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
        Node<E> current = table[index];

        while (current != null) {
            if (o == null ? current.element == null : o.equals(current.element)) {
                return true;
            }
            current = current.nextInBucket;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        ensureCapacity();
        int index = hash(e);
        int hash = e == null ? 0 : e.hashCode();

        // Проверка на существующий элемент
        Node<E> current = table[index];
        while (current != null) {
            if (e == null ? current.element == null : e.equals(current.element)) {
                return false; // Элемент уже существует
            }
            current = current.nextInBucket;
        }

        // Добавление нового элемента
        Node<E> newNode = new Node<>(e, hash, table[index]);
        table[index] = newNode;
        linkLast(newNode);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = hash(o);
        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if (o == null ? current.element == null : o.equals(current.element)) {
                // Удаление из цепочки коллизий
                if (prev == null) {
                    table[index] = current.nextInBucket;
                } else {
                    prev.nextInBucket = current.nextInBucket;
                }

                // Удаление из списка порядка добавления
                unlink(current);
                size--;
                return true;
            }
            prev = current;
            current = current.nextInBucket;
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            table[i] = null;
        }
        head = tail = null;
        size = 0;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        String result = "[";
        Node<E> current = head;
        while (current != null) {
            result += current.element;
            if (current.nextInOrder != null) {
                result += ", ";
            }
            current = current.nextInOrder;
        }
        result += "]";
        return result;
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
            Node<E> next = current.nextInOrder;
            if (!c.contains(current.element)) {
                remove(current.element);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Iterator not implemented");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("toArray() not implemented");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("toArray(T[] a) not implemented");
    }

    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException("equals() not implemented");
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("hashCode() not implemented");
    }
}