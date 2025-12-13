package by.it.group451004.levkovich.lesson11;

import java.util.*;

public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private static class Node<E> {
        E data;
        Node<E> next;      // для цепочки коллизий
        Node<E> before;    // предыдущий в порядке добавления
        Node<E> after;     // следующий в порядке добавления

        Node(E data) {
            this.data = data;
        }
    }

    private Node<E>[] buckets;
    private Node<E> head;  // первый добавленный элемент
    private Node<E> tail;  // последний добавленный элемент
    private int size;

    public MyLinkedHashSet() {
        buckets = new Node[DEFAULT_CAPACITY];
        head = null;
        tail = null;
        size = 0;
    }

    // Вспомогательные методы
    private int hash(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % buckets.length;
    }

    private boolean equals(Object a, Object b) {
        if (a == b) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }

    // Добавление в двусвязный список (в конец)
    private void linkLast(Node<E> node) {
        if (tail == null) {
            head = tail = node;
        } else {
            tail.after = node;
            node.before = tail;
            tail = node;
        }
    }

    // Удаление из двусвязного списка
    private void unlink(Node<E> node) {
        Node<E> prev = node.before;
        Node<E> next = node.after;

        if (prev == null) {
            head = next;
        } else {
            prev.after = next;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.before = prev;
        }

        node.before = node.after = null;
    }

    private void resize() {
        if (size < LOAD_FACTOR * buckets.length) return;

        Node<E>[] oldBuckets = buckets;
        buckets = new Node[oldBuckets.length * 2];

        // Перехеширование всех элементов
        Node<E> current = head;
        while (current != null) {
            int newIndex = hash(current.data);

            // Вставляем в начало цепочки новой корзины
            current.next = buckets[newIndex];
            buckets[newIndex] = current;

            current = current.after;
        }
    }

    // Обязательные методы
    @Override
    public String toString() {
        if (size == 0) return "[]";

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
    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = null;
        }
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
        int index = hash(element);
        Node<E> current = buckets[index];

        // Проверяем, есть ли уже такой элемент
        while (current != null) {
            if (equals(current.data, element)) {
                return false;
            }
            current = current.next;
        }

        // Добавляем новый элемент
        Node<E> newNode = new Node<>(element);

        // Добавляем в хеш-таблицу (в начало цепочки)
        newNode.next = buckets[index];
        buckets[index] = newNode;

        linkLast(newNode);
        size++;

        resize();
        return true;
    }

    @Override
    public boolean remove(Object element) {
        int index = hash(element);
        Node<E> current = buckets[index];
        Node<E> prev = null;

        while (current != null) {
            if (equals(current.data, element)) {
                // Удаляем из цепочки коллизий
                if (prev == null) {
                    buckets[index] = current.next;
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
    public boolean contains(Object element) {
        int index = hash(element);
        Node<E> current = buckets[index];

        while (current != null) {
            if (equals(current.data, element)) {
                return true;
            }
            current = current.next;
        }
        return false;
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
    public boolean removeAll(Collection<?> c) {
        // Оптимизированная версия O(n)
        if (c.isEmpty() || size == 0) {
            return false;
        }

        // Создаем временный массив для элементов, которые нужно сохранить
        Node<E>[] newBuckets = new Node[buckets.length];
        Node<E> newHead = null;
        Node<E> newTail = null;
        int newSize = 0;

        // Проходим по всем элементам в порядке добавления
        Node<E> current = head;
        while (current != null) {
            if (!c.contains(current.data)) {
                // Сохраняем элемент
                Node<E> newNode = new Node<>(current.data);

                // Добавляем в хеш-таблицу
                int newIndex = hash(newNode.data);
                newNode.next = newBuckets[newIndex];
                newBuckets[newIndex] = newNode;

                // Добавляем в двусвязный список
                if (newTail == null) {
                    newHead = newTail = newNode;
                } else {
                    newTail.after = newNode;
                    newNode.before = newTail;
                    newTail = newNode;
                }
                newSize++;
            }
            current = current.after;
        }

        if (newSize == size) {
            return false; // Ничего не изменилось
        }

        // Заменяем старые структуры новыми
        this.buckets = newBuckets;
        this.head = newHead;
        this.tail = newTail;
        this.size = newSize;
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // Оптимизированная версия O(n)
        if (size == 0) {
            return false;
        }

        // Создаем временный массив для элементов, которые нужно сохранить
        Node<E>[] newBuckets = new Node[buckets.length];
        Node<E> newHead = null;
        Node<E> newTail = null;
        int newSize = 0;

        // Проходим по всем элементам в порядке добавления
        Node<E> current = head;
        while (current != null) {
            if (c.contains(current.data)) {
                // Сохраняем элемент
                Node<E> newNode = new Node<>(current.data);

                // Добавляем в хеш-таблицу
                int newIndex = hash(newNode.data);
                newNode.next = newBuckets[newIndex];
                newBuckets[newIndex] = newNode;

                // Добавляем в двусвязный список
                if (newTail == null) {
                    newHead = newTail = newNode;
                } else {
                    newTail.after = newNode;
                    newNode.before = newTail;
                    newTail = newNode;
                }
                newSize++;
            }
            current = current.after;
        }

        if (newSize == size) {
            return false; // Ничего не изменилось
        }

        // Заменяем старые структуры новыми
        this.buckets = newBuckets;
        this.head = newHead;
        this.tail = newTail;
        this.size = newSize;
        return true;
    }

    // Остальные методы интерфейса Set - не реализованы
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