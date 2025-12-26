package by.it.group410901.danilova.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<E>[] table;
    private Node<E> head;
    private Node<E> tail;
    private int size;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        Node(E data) {
            this.data = data;
        }
    }

    private int getIndex(Object key) {
        if (key == null) return 0;
        return Math.abs(key.hashCode() % table.length);
    }

    private boolean objectsEqual(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
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
        int index = getIndex(o);
        Node<E> current = table[index];

        while (current != null) {
            if (objectsEqual(o, current.data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) {
            return false;
        }

        if (size + 1 > table.length * LOAD_FACTOR) {
            resize();
        }

        int index = getIndex(e);
        Node<E> newNode = new Node<>(e);

        // Добавляем в хеш-таблицу
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<E> current = table[index];
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }

        // Добавляем в linked list для сохранения порядка
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode;
        }

        size++;
        return true;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        table = new Node[oldTable.length * 2];

        // Сохраняем текущий linked list
        Node<E> currentInList = head;
        head = null;
        tail = null;
        size = 0;

        // Перестраиваем таблицу, сохраняя порядок linked list
        while (currentInList != null) {
            // Создаем новую ноду с теми же данными
            Node<E> newNode = new Node<>(currentInList.data);

            // Добавляем в хеш-таблицу
            int index = getIndex(newNode.data);
            if (table[index] == null) {
                table[index] = newNode;
            } else {
                Node<E> current = table[index];
                while (current.next != null) {
                    current = current.next;
                }
                current.next = newNode;
            }

            // Добавляем в linked list
            if (head == null) {
                head = newNode;
                tail = newNode;
            } else {
                tail.after = newNode;
                newNode.before = tail;
                tail = newNode;
            }

            size++;
            currentInList = currentInList.after;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (!contains(o)) {
            return false;
        }

        int index = getIndex(o);
        Node<E> nodeToRemove = null;

        // Находим ноду в хеш-таблице
        if (objectsEqual(o, table[index].data)) {
            nodeToRemove = table[index];
            table[index] = table[index].next;
        } else {
            Node<E> prev = table[index];
            Node<E> current = prev.next;

            while (current != null) {
                if (objectsEqual(o, current.data)) {
                    nodeToRemove = current;
                    prev.next = current.next;
                    break;
                }
                prev = current;
                current = current.next;
            }
        }

        // Удаляем из linked list
        if (nodeToRemove.before != null) {
            nodeToRemove.before.after = nodeToRemove.after;
        } else {
            head = nodeToRemove.after;
        }

        if (nodeToRemove.after != null) {
            nodeToRemove.after.before = nodeToRemove.before;
        } else {
            tail = nodeToRemove.before;
        }

        size--;
        return true;
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
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        boolean first = true;

        while (current != null) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(current.data);
            first = false;
            current = current.after;
        }

        sb.append("]");
        return sb.toString();
    }

    // Реализация методов для работы с коллекциями

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
        Iterator<E> iterator = new LinkedIterator();
        while (iterator.hasNext()) {
            E element = iterator.next();
            if (c.contains(element)) {
                iterator.remove();
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
            // Используем основной метод remove для корректного удаления
            MyLinkedHashSet.this.remove(lastReturned.data);
            lastReturned = null;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedIterator();
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
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}