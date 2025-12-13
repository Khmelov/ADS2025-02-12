package by.it.group451001.tsurko.lesson11;

import java.util.*;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E item;
        Node<E> hashNext; // связь в хэш-цепочке
        Node<E> next;     // следующий в порядке вставки
        Node<E> prev;     // предыдущий в порядке вставки

        Node(E item, Node<E> next, Node<E> prev, Node<E> hashNext) {
            this.item = item;
            this.next = next;
            this.prev = prev;
            this.hashNext = hashNext;
        }
    }

    private Node<E>[] table;
    private int capacity = 16;
    private int size = 0;
    private Node<E> head;
    private Node<E> tail;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[capacity];
    }

    private int hash(Object key) {
        return (key == null) ? 0 : (key.hashCode() & 0x7fffffff) % capacity;
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
        int index = hash(o);
        Node<E> current = table[index];
        while (current != null) {
            if (Objects.equals(current.item, o))
                return true;
            current = current.hashNext;
        }
        return false;
    }

    @Override
    public String toString() {
        Iterator<E> it = iterator();
        if (!it.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        while (true) {
            E e = it.next();
            sb.append(e == this ? "(this Set)" : e);
            if (!it.hasNext())
                return sb.append(']').toString();
            sb.append(',').append(' ');
        }
    }


    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;
        for (E e : this)
            arr[i++] = e;
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (E e : this)
            a[i++] = (T) e;
        if (a.length > size)
            a[size] = null;
        return a;
    }

    @Override
    public boolean add(E e) {
        int index = hash(e);
        Node<E> current = table[index];
        Node<E> newNode = new Node<>(e, null, tail, null);

        if (current != null) {
            // проверяем все узлы, включая последний
            while (true) {
                if (Objects.equals(current.item, e))
                    return false;
                if (current.hashNext == null)
                    break;
                current = current.hashNext;
            }
            current.hashNext = newNode;
        } else {
            table[index] = newNode;
        }

        // добавляем в связный список вставки
        if (tail != null)
            tail.next = newNode;
        else
            head = newNode;

        tail = newNode;
        size++;
        return true;
    }


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
                if (current == null)
                    throw new NoSuchElementException();
                E item = current.item;
                current = current.next;
                return item;
            }
        };
    }


    @Override
    public boolean remove(Object o) {
        int index = hash(o);
        Node<E> current = table[index];
        Node<E> prevHash = null;

        while (current != null) {
            if (Objects.equals(current.item, o)) {

                // Удаляем из hash-цепочки
                if (prevHash == null)
                    table[index] = current.hashNext;
                else
                    prevHash.hashNext = current.hashNext;

                // Удаляем из связного списка
                if (current.prev != null)
                    current.prev.next = current.next;
                else
                    head = current.next;

                if (current.next != null)
                    current.next.prev = current.prev;
                else
                    tail = current.prev;

                size--;
                return true;
            }
            prevHash = current;
            current = current.hashNext;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c)
            if (!contains(e)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c)
            if (add(e)) modified = true;
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.next;
            if (!c.contains(current.item)) {
                remove(current.item);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object e : c)
            if (remove(e)) modified = true;
        return modified;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clear() {
        // Полный сброс всех связей
        if (head != null) {
            Node<E> current = head;
            while (current != null) {
                Node<E> next = current.next;
                current.item = null;
                current.next = null;
                current.prev = null;
                current.hashNext = null;
                current = next;
            }
        }

        // Полная пересозданная таблица и обнуление состояния
        Arrays.fill(table, null);  // ← добавь это!
        head = null;
        tail = null;
        size = 0;
    }

}
