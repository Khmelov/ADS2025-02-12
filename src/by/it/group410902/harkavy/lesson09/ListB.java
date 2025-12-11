package by.it.group410902.harkavy.lesson09;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {

    // Узел двусвязного списка
    private static class Node<E> {
        E item;
        Node<E> prev;
        Node<E> next;
        Node(Node<E> prev, E item, Node<E> next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }

    // Голова/хвост и размер
    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    // ================= Обязательные к реализации методы =================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = head;
        while (cur != null) {
            sb.append(cur.item);
            if (cur.next != null) sb.append(", ");
            cur = cur.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        linkLast(e);
        return true;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        return unlink(nodeAt(index));
    }

    @Override
    public int size() {
        return size;
    }

    // ================= Доп. обязательные для полноценной работы =================

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index);
        if (index == size) {
            linkLast(element);
        } else {
            linkBefore(element, nodeAt(index));
        }
    }

    @Override
    public boolean remove(Object o) {
        for (Node<E> x = head; x != null; x = x.next) {
            if (o == null ? x.item == null : o.equals(x.item)) {
                unlink(x);
                return true;
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        Node<E> x = nodeAt(index);
        E old = x.item;
        x.item = element;
        return old;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        // разрываем все ссылки для помощи GC
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.next;
            cur.item = null;
            cur.prev = null;
            cur.next = null;
            cur = next;
        }
        head = tail = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        int i = 0;
        for (Node<E> x = head; x != null; x = x.next, i++) {
            if (o == null ? x.item == null : o.equals(x.item)) return i;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        return nodeAt(index).item;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        int i = size - 1;
        for (Node<E> x = tail; x != null; x = x.prev, i--) {
            if (o == null ? x.item == null : o.equals(x.item)) return i;
        }
        return -1;
    }

    // ================= Вспомогательная логика двусвязного списка =================

    private void linkLast(E e) {
        Node<E> l = tail;
        Node<E> newNode = new Node<>(l, e, null);
        tail = newNode;
        if (l == null) head = newNode;
        else l.next = newNode;
        size++;
    }

    private void linkBefore(E e, Node<E> succ) {
        Node<E> pred = succ.prev;
        Node<E> newNode = new Node<>(pred, e, succ);
        succ.prev = newNode;
        if (pred == null) head = newNode;
        else pred.next = newNode;
        size++;
    }

    private E unlink(Node<E> x) {
        E element = x.item;
        Node<E> next = x.next;
        Node<E> prev = x.prev;

        if (prev == null) head = next;
        else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) tail = prev;
        else {
            next.prev = prev;
            x.next = null;
        }

        x.item = null;
        size--;
        return element;
    }

    // Быстрый доступ к узлу по индексу: с головы или с хвоста
    private Node<E> nodeAt(int index) {
        if (index < (size >> 1)) {
            Node<E> x = head;
            for (int i = 0; i < index; i++) x = x.next;
            return x;
        } else {
            Node<E> x = tail;
            for (int i = size - 1; i > index; i--) x = x.prev;
            return x;
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index);
    }

    // ================= Опциональные к реализации методы (минимальные заглушки) =================

    @Override
    public boolean containsAll(Collection<?> c) { return false; }

    @Override
    public boolean addAll(Collection<? extends E> c) { return false; }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) { return false; }

    @Override
    public boolean removeAll(Collection<?> c) { return false; }

    @Override
    public boolean retainAll(Collection<?> c) { return false; }

    @Override
    public List<E> subList(int fromIndex, int toIndex) { return null; }

    @Override
    public ListIterator<E> listIterator(int index) { return null; }

    @Override
    public ListIterator<E> listIterator() { return null; }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a == null) throw new NullPointerException();
        int sz = size;
        if (a.length < sz) {
            @SuppressWarnings("unchecked")
            T[] r = (T[]) Array.newInstance(a.getClass().getComponentType(), sz);
            a = r;
        }
        int i = 0;
        Node<E> x = head;
        while (x != null) {
            @SuppressWarnings("unchecked")
            T v = (T) x.item;
            a[i++] = v;
            x = x.next;
        }
        if (a.length > sz) a[sz] = null;
        return a;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;
        for (Node<E> x = head; x != null; x = x.next) {
            arr[i++] = x.item;
        }
        return arr;
    }

    // ================= Итератор (для удобной отладки, не обязателен) =================
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> next = head;
            private Node<E> lastReturned = null;

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public E next() {
                lastReturned = next;
                next = next.next;
                return lastReturned.item;
            }

            @Override
            public void remove() {
                if (lastReturned == null) throw new IllegalStateException();
                unlink(lastReturned);
                lastReturned = null;
            }
        };
    }
}
