package by.it.group451001.romeyko.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    // -----------------------------
    // Внутренний класс узла списка
    // -----------------------------
    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(E item, Node<E> prev, Node<E> next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> head; // первый элемент
    private Node<E> tail; // последний элемент
    private int size = 0;

    // -----------------------------
    // Обязательные методы
    // -----------------------------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        while (current != null) {
            sb.append(current.item);
            if (current.next != null) sb.append(", ");
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        Node<E> newNode = new Node<>(element, null, head);
        if (head != null) {
            head.prev = newNode;
        } else {
            tail = newNode; // если список был пуст
        }
        head = newNode;
        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> newNode = new Node<>(element, tail, null);
        if (tail != null) {
            tail.next = newNode;
        } else {
            head = newNode; // если список был пуст
        }
        tail = newNode;
        size++;
    }

    @Override
    public E element() {
        if (head == null) throw new NoSuchElementException();
        return head.item;
    }

    @Override
    public E getFirst() {
        if (head == null) throw new NoSuchElementException();
        return head.item;
    }

    @Override
    public E getLast() {
        if (tail == null) throw new NoSuchElementException();
        return tail.item;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (head == null) return null;
        E value = head.item;
        head = head.next;
        if (head != null) head.prev = null;
        else tail = null; // если список опустел
        size--;
        return value;
    }

    @Override
    public E pollLast() {
        if (tail == null) return null;
        E value = tail.item;
        tail = tail.prev;
        if (tail != null) tail.next = null;
        else head = null;
        size--;
        return value;
    }

    @Override
    public E remove() {
        E e = poll();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    // Удаление по индексу
    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<E> current = head;
        for (int i = 0; i < index; i++) current = current.next;

        E value = current.item;
        unlink(current);
        return value;
    }

    // Удаление по значению
    @Override
    public boolean remove(Object o) {
        Node<E> current = head;
        while (current != null) {
            if ((o == null && current.item == null) || (o != null && o.equals(current.item))) {
                unlink(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    // -----------------------------
    // Вспомогательный метод unlink
    // -----------------------------
    private void unlink(Node<E> node) {
        Node<E> prev = node.prev;
        Node<E> next = node.next;

        if (prev != null) prev.next = next;
        else head = next;

        if (next != null) next.prev = prev;
        else tail = prev;

        size--;
    }

    // -----------------------------
    // Необязательные методы интерфейса
    // -----------------------------
    @Override public boolean offer(E e) { return add(e); }
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }

    @Override
    public E removeFirst() {
        return null;
    }

    @Override
    public E removeLast() {
        return null;
    }

    @Override public E peek() { return head == null ? null : head.item; }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void push(E e) {

    }

    @Override
    public E pop() {
        return null;
    }

    @Override public E peekFirst() { return peek(); }
    @Override public E peekLast() { return tail == null ? null : tail.item; }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override public void clear() { while (poll() != null); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override public java.util.Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
}
