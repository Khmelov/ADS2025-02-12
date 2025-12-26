package by.it.group451003.platonova.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;   // для коллизий в бакете
        Node<E> orderNext; // для порядка вставки
        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private Node<E> head = null; // первый элемент по порядку добавления
    private Node<E> tail = null; // последний элемент по порядку добавления
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 16;

    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
    }

    private int index(Object o) {
        return (o == null ? 0 : o.hashCode() & 0x7FFFFFFF) % table.length;
    }

    @Override
    public boolean add(E e) {
        int idx = index(e);

        // Проверяем, есть ли уже элемент в бакете
        Node<E> cur = table[idx];
        while (cur != null) {
            if ((e == null && cur.value == null) || (e != null && e.equals(cur.value))) {
                return false; // элемент уже есть
            }
            cur = cur.next;
        }

        // Создаем новый узел для бакета
        Node<E> newNode = new Node<>(e, table[idx]);
        table[idx] = newNode;

        // Добавляем в связный список по порядку вставки
        if (head == null) {
            // первый элемент
            head = tail = newNode;
        } else {
            tail.orderNext = newNode;
            tail = newNode;
        }

        size++;
        return true;
    }


    @Override
    public boolean remove(Object o) {
        int idx = index(o);
        Node<E> cur = table[idx];
        Node<E> prev = null;
        while (cur != null) {
            if ((o == null && cur.value == null) || (o != null && o.equals(cur.value))) {
                // удаляем из бакета
                if (prev == null) table[idx] = cur.next;
                else prev.next = cur.next;

                // удаляем из связного списка порядка вставки
                if (cur == head) {
                    head = head.orderNext;
                } else {
                    Node<E> p = head;
                    while (p != null && p.orderNext != cur) p = p.orderNext;
                    if (p != null) p.orderNext = cur.orderNext;
                }

                // корректируем tail
                if (cur == tail) {
                    // если удалили последний, находим новый tail
                    tail = head;
                    if (tail != null) {
                        while (tail.orderNext != null) tail = tail.orderNext;
                    }
                }

                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }


    @Override
    public boolean contains(Object o) {
        int idx = index(o);
        Node<E> cur = table[idx];
        while (cur != null) {
            if ((o == null && cur.value == null) || (o != null && o.equals(cur.value))) return true;
            cur = cur.next;
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) table[i] = null;
        head = tail = null;
        size = 0;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = head;
        boolean first = true;
        while (cur != null) {
            if (!first) sb.append(", ");
            sb.append(cur.value);
            first = false;
            cur = cur.orderNext;
        }
        sb.append("]");
        return sb.toString();
    }

    // ===== Методы Collection =====

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        for (E e : c) if (add(e)) modified = true;
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        for (Object o : c) if (remove(o)) modified = true;
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.orderNext;
            if (!c.contains(cur.value)) {
                remove(cur.value);
                modified = true;
            }
            cur = next;
        }
        return modified;
    }

    // ===== Заглушки для Set / Collection =====
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}

