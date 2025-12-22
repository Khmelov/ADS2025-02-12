package by.it.group410902.latipov.lesson11;

import java.util.*;

public class MyLinkedHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

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
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        table = (Node<E>[]) new Node[initialCapacity];
        head = null;
        tail = null;
        size = 0;
    }

    // Узел для хранения элементов с поддержкой порядка добавления
    private static class Node<E> {
        final E data;
        final int hash;
        Node<E> next; // следующий в цепочке коллизий
        Node<E> before; // предыдущий в порядке добавления
        Node<E> after; // следующий в порядке добавления

        Node(E data, int hash, Node<E> next) {
            this.data = data;
            this.hash = hash;
            this.next = next;
            this.before = null;
            this.after = null;
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) {
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

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        Arrays.fill(table, null);
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }

        int hash = hash(element);
        int index = indexFor(hash, table.length);

        // Проверяем, есть ли уже такой элемент
        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash && Objects.equals(current.data, element)) {
                return false; // Элемент уже существует
            }
            current = current.next;
        }

        // Создаем новый узел
        Node<E> newNode = new Node<>(element, hash, table[index]);

        // Добавляем в хеш-таблицу
        table[index] = newNode;

        // Добавляем в связный список порядка добавления
        linkNodeLast(newNode);

        size++;

        // Проверяем необходимость перехеширования
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }

        return true;
    }

    @Override
    public boolean remove(Object element) {
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }

        int hash = hash(element);
        int index = indexFor(hash, table.length);

        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if (current.hash == hash && Objects.equals(current.data, element)) {
                // Удаляем из хеш-таблицы
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }

                // Удаляем из связного списка порядка добавления
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
    public boolean contains(Object element) {
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }

        int hash = hash(element);
        int index = indexFor(hash, table.length);

        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash && Objects.equals(current.data, element)) {
                return true;
            }
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
        if (c.isEmpty()) {
            return false;
        }

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
        if (c.isEmpty() || size == 0) {
            return false;
        }

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
        if (size == 0) {
            return false;
        }
        if (c.isEmpty()) {
            clear();
            return true;
        }

        boolean modified = false;
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

    // Вспомогательные методы

    private int hash(Object element) {
        int h = element.hashCode();
        return h ^ (h >>> 16);
    }

    private int indexFor(int hash, int length) {
        return (length - 1) & hash;
    }

    // Добавляет узел в конец связного списка порядка добавления
    private void linkNodeLast(Node<E> node) {
        if (tail == null) {
            head = node;
            tail = node;
        } else {
            tail.after = node;
            node.before = tail;
            tail = node;
        }
    }

    // Удаляет узел из связного списка порядка добавления
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

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        int newCapacity = table.length * 2;
        table = (Node<E>[]) new Node[newCapacity];

        // Перехеширование всех элементов
        for (Node<E> node : oldTable) {
            Node<E> current = node;
            while (current != null) {
                Node<E> next = current.next;
                int newIndex = indexFor(current.hash, newCapacity);
                current.next = table[newIndex];
                table[newIndex] = current;
                current = next;
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////        Остальные методы - необязательные к реализации     ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                E data = current.data;
                current = current.after;
                return data;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int i = 0;
        Node<E> current = head;
        while (current != null) {
            array[i++] = current.data;
            current = current.after;
        }
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) Arrays.copyOf(toArray(), size, a.getClass());
        }
        System.arraycopy(toArray(), 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}