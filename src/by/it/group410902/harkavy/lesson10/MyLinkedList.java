package by.it.group410902.harkavy.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    // узел списка
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

    // голова/хвост и размер
    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    // --- помощники ---
    private void linkFirst(E e) {
        // вставка перед текущей головой
        Node<E> h = head;
        Node<E> x = new Node<>(null, e, h);
        head = x;
        if (h == null) tail = x;     // список был пуст
        else h.prev = x;
        size++;
    }

    private void linkLast(E e) {
        // вставка после текущего хвоста
        Node<E> t = tail;
        Node<E> x = new Node<>(t, e, null);
        tail = x;
        if (t == null) head = x;     // список был пуст
        else t.next = x;
        size++;
    }

    private E unlink(Node<E> x) {
        // вырезаем x, склеивая соседей
        Node<E> p = x.prev, n = x.next;
        if (p == null) head = n; else { p.next = n; x.prev = null; }
        if (n == null) tail = p; else { n.prev = p; x.next = null; }
        E item = x.item;
        x.item = null;
        size--;
        return item;
    }

    private Node<E> nodeAt(int index) {
        // поиск с ближней стороны
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index);
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

    // ================= ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ =================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> x = head;
        while (x != null) {
            sb.append(x.item);
            if (x.next != null) sb.append(", ");
            x = x.next;
        }
        return sb.append("]").toString();
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    /** Удаление по индексу (0..size-1). */
    public E remove(int index) {
        return unlink(nodeAt(index));
    }

    /** Удаление по значению первого вхождения. */
    @Override
    public boolean remove(Object o) {
        // проходим слева направо и удаляем первое совпадение
        for (Node<E> x = head; x != null; x = x.next) {
            if (o == null ? x.item == null : o.equals(x.item)) {
                unlink(x);
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() { return size; }

    @Override
    public void addFirst(E e) {
        linkFirst(e);
    }

    @Override
    public void addLast(E e) {
        linkLast(e);
    }

    @Override
    public E element() { return getFirst(); }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return head.item;
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return tail.item;
    }

    @Override
    public E poll() { return pollFirst(); }

    @Override
    public E pollFirst() {
        // снимаем голову или null
        if (size == 0) return null;
        return unlink(head);
    }

    @Override
    public E pollLast() {
        // снимаем хвост или null
        if (size == 0) return null;
        return unlink(tail);
    }

    // ================= ОСТАЛЬНЫЕ МЕТОДЫ НЕ НУЖНЫ =================

    private void unsupported() {
        throw new UnsupportedOperationException("Not required by the assignment");
    }

    @Override public boolean isEmpty() { return size == 0; }
    @Override public void clear() { head = tail = null; size = 0; }

    @Override public E remove() { unsupported(); return null; }
    @Override public E removeFirst() { unsupported(); return null; }
    @Override public E removeLast() { unsupported(); return null; }
    @Override public E peek() { unsupported(); return null; }
    @Override public E peekFirst() { unsupported(); return null; }
    @Override public E peekLast() { unsupported(); return null; }
    @Override public boolean offer(E e) { unsupported(); return false; }
    @Override public boolean offerFirst(E e) { unsupported(); return false; }
    @Override public boolean offerLast(E e) { unsupported(); return false; }
    @Override public void push(E e) { unsupported(); }
    @Override public E pop() { unsupported(); return null; }
    @Override public boolean removeFirstOccurrence(Object o) { unsupported(); return false; }
    @Override public boolean removeLastOccurrence(Object o) { unsupported(); return false; }
    @Override public Iterator<E> iterator() { unsupported(); return null; }
    @Override public Iterator<E> descendingIterator() { unsupported(); return null; }
    @Override public boolean contains(Object o) { unsupported(); return false; }
    @Override public boolean containsAll(Collection<?> c) { unsupported(); return false; }
    @Override public boolean addAll(Collection<? extends E> c) { unsupported(); return false; }
    @Override public boolean removeAll(Collection<?> c) { unsupported(); return false; }
    @Override public boolean retainAll(Collection<?> c) { unsupported(); return false; }
    @Override public Object[] toArray() { unsupported(); return null; }
    @Override public <T> T[] toArray(T[] a) { unsupported(); return null; }
}
