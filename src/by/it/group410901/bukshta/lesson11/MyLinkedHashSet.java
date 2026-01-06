package by.it.group410901.bukshta.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;      // для коллизий в хеш-таблице
        Node<E> orderNext; // для связного списка всех элементов
        Node(E value) {
            this.value = value;
        }
    }

    private Node<E>[] table;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<E> head;
    private Node<E> tail;

    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
        head = null;
        tail = null;
    }

    private int hash(Object key) {
        return key == null ? 0 : key.hashCode() & 0x7FFFFFFF;
    }

    private void resizeIfNeeded() {
        if (size >= table.length * LOAD_FACTOR) {
            Node<E>[] oldTable = table;
            table = (Node<E>[]) new Node[table.length * 2];
            size = 0;
            head = null;
            tail = null;

            for (Node<E> bucket : oldTable) {
                Node<E> node = bucket;
                while (node != null) {
                    add(node.value);
                    node = node.next;
                }
            }
        }
    }

    @Override
    public boolean add(E e) {
        resizeIfNeeded();
        int index = hash(e) % table.length;
        Node<E> node = table[index];
        while (node != null) {
            if ((e == null && node.value == null) || (e != null && e.equals(node.value))) {
                return false; // элемент уже есть
            }
            node = node.next;
        }
        Node<E> newNode = new Node<>(e);
        // вставка в хеш-таблицу
        newNode.next = table[index];
        table[index] = newNode;

        // добавление в связный список по порядку вставки
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.orderNext = newNode;
            tail = newNode;
        }

        size++;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        int index = hash(o) % table.length;
        Node<E> node = table[index];
        while (node != null) {
            if ((o == null && node.value == null) || (o != null && o.equals(node.value))) {
                return true;
            }
            node = node.next;
        }
        return false;
    }


    @Override
    public boolean remove(Object o) {
        int index = hash(o) % table.length;
        Node<E> node = table[index];
        Node<E> prev = null;
        while (node != null) {
            if ((o == null && node.value == null) || (o != null && o.equals(node.value))) {
                // удаляем из хеш-таблицы
                if (prev == null) {
                    table[index] = node.next;
                } else {
                    prev.next = node.next;
                }

                // удаляем из связного списка по порядку добавления
                if (node == head) {
                    head = head.orderNext;
                } else {
                    Node<E> tmp = head;
                    while (tmp != null && tmp.orderNext != node) tmp = tmp.orderNext;
                    if (tmp != null) tmp.orderNext = node.orderNext;
                }
                if (node == tail) {
                    // найти новый tail
                    if (head == null) tail = null;
                    else {
                        Node<E> tmp = head;
                        while (tmp.orderNext != null) tmp = tmp.orderNext;
                        tail = tmp;
                    }
                }

                size--;
                return true;
            }
            prev = node;
            node = node.next;
        }
        return false;
    }


    private Node<E> findPrevTail() {
        if (head == null || head == tail) return null;
        Node<E> tmp = head;
        while (tmp.orderNext != tail) tmp = tmp.orderNext;
        return tmp;
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) table[i] = null;
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<E> node = head;
        boolean first = true;
        while (node != null) {
            if (!first) sb.append(", ");
            sb.append(node.value);
            first = false;
            node = node.orderNext;
        }
        sb.append("]");
        return sb.toString();
    }

    // -----------------------------
    // Методы коллекций
    // -----------------------------
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) changed |= add(e);
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) changed |= remove(o);
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> node = head;
        while (node != null) {
            Node<E> nextNode = node.orderNext;
            if (!c.contains(node.value)) {
                remove(node.value);
                changed = true;
            }
            node = nextNode;
        }
        return changed;
    }

    // Методы интерфейса Set/Collection, не обязательные для уровня B
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}
