package by.it.group451002.sidarchuk.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private Node<E> head; // голова связного списка для порядка добавления
    private Node<E> tail; // хвост связного списка для порядка добавления
    private int size;
    private int capacity;
    private final float loadFactor;

    // Внутренний класс для узлов связного списка
    private static class Node<E> {
        E data;
        Node<E> next; // следующий в цепочке коллизий
        Node<E> after; // следующий в порядке добавления
        Node<E> before; // предыдущий в порядке добавления
        final int hash;

        Node(E data, int hash, Node<E> next) {
            this.data = data;
            this.hash = hash;
            this.next = next;
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
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private int indexFor(int hash, int length) {
        return hash % length;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = capacity * 2;
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];

        // Перехеширование всех элементов
        for (int i = 0; i < capacity; i++) {
            Node<E> node = table[i];
            while (node != null) {
                Node<E> next = node.next;
                int newIndex = indexFor(node.hash, newCapacity);
                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = next;
            }
        }

        table = newTable;
        capacity = newCapacity;
    }

    // Основные методы
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
    public boolean contains(Object o) {
        if (o == null) {
            return containsNull();
        }

        int hash = hash(o);
        int index = indexFor(hash, capacity);
        Node<E> node = table[index];

        while (node != null) {
            if (node.hash == hash && o.equals(node.data)) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    private boolean containsNull() {
        Node<E> node = table[0];
        while (node != null) {
            if (node.data == null) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            return addNull();
        }

        int hash = hash(e);
        int index = indexFor(hash, capacity);

        // Проверка на существующий элемент
        Node<E> node = table[index];
        while (node != null) {
            if (node.hash == hash && e.equals(node.data)) {
                return false; // элемент уже существует
            }
            node = node.next;
        }

        // Добавление нового элемента
        if (size + 1 > capacity * loadFactor) {
            resize();
            index = indexFor(hash, capacity);
        }

        Node<E> newNode = new Node<>(e, hash, table[index]);
        table[index] = newNode;

        // Добавление в связный список порядка
        linkNodeLast(newNode);
        size++;
        return true;
    }

    private boolean addNull() {
        // Проверка на существующий null
        Node<E> node = table[0];
        while (node != null) {
            if (node.data == null) {
                return false;
            }
            node = node.next;
        }

        // Добавление null
        if (size + 1 > capacity * loadFactor) {
            resize();
        }

        Node<E> newNode = new Node<>(null, 0, table[0]);
        table[0] = newNode;

        linkNodeLast(newNode);
        size++;
        return true;
    }

    private void linkNodeLast(Node<E> node) {
        if (head == null) {
            head = node;
            tail = node;
        } else {
            tail.after = node;
            node.before = tail;
            tail = node;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        if (o == null) {
            return removeNull();
        }

        int hash = hash(o);
        int index = indexFor(hash, capacity);
        Node<E> node = table[index];
        Node<E> prev = null;

        while (node != null) {
            if (node.hash == hash && o.equals(node.data)) {
                // Удаление из цепочки коллизий
                if (prev == null) {
                    table[index] = node.next;
                } else {
                    prev.next = node.next;
                }

                // Удаление из связного списка порядка
                unlinkNode(node);
                size--;
                return true;
            }
            prev = node;
            node = node.next;
        }
        return false;
    }

    private boolean removeNull() {
        int index = 0;
        Node<E> node = table[index];
        Node<E> prev = null;

        while (node != null) {
            if (node.data == null) {
                if (prev == null) {
                    table[index] = node.next;
                } else {
                    prev.next = node.next;
                }

                unlinkNode(node);
                size--;
                return true;
            }
            prev = node;
            node = node.next;
        }
        return false;
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

    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            table[i] = null;
        }
        head = null;
        tail = null;
        size = 0;
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
            sb.append(current.data);
            if (current.after != null) {
                sb.append(", ");
            }
            current = current.after;
        }

        sb.append("]");
        return sb.toString();
    }

    // Методы для работы с коллекциями
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
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E element = it.next();
            if (!c.contains(element)) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    // Остальные методы интерфейса Set (необязательные, но требуемые)
    @Override
    public Iterator<E> iterator() {
        return new LinkedHashSetIterator();
    }

    private class LinkedHashSetIterator implements Iterator<E> {
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

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int i = 0;
        for (E element : this) {
            array[i++] = element;
        }
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        }

        int i = 0;
        for (E element : this) {
            a[i++] = (T) element;
        }

        if (a.length > size) {
            a[size] = null;
        }

        return a;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Set)) {
            return false;
        }

        Set<?> other = (Set<?>) o;
        if (size != other.size()) {
            return false;
        }

        return containsAll(other);
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (E element : this) {
            if (element != null) {
                hashCode += element.hashCode();
            }
        }
        return hashCode;
    }
}
