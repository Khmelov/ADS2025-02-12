package by.it.group410902.derzhavskaya_e.lesson10;

import java.util.Deque;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    // Узел двусвязного списка
    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> head; // первый элемент
    private Node<E> tail; // последний элемент
    private int size;     // количество элементов

    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    // строковое представление в формате [a, b, c]
    @Override
    public String toString() {
        if (size == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;

        while (current != null) {
            sb.append(current.item);
            if (current.next != null) sb.append(", ");
            current = current.next;
        }

        return sb.append("]").toString();
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    // добавление в начало
    @Override
    public void addFirst(E element) {
        Node<E> newNode = new Node<>(null, element, head);

        if (head == null) {
            tail = newNode;
        } else {
            head.prev = newNode;
        }

        head = newNode;
        size++;
    }

    // добавление в конец
    @Override
    public void addLast(E element) {
        Node<E> newNode = new Node<>(tail, element, null);

        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }

        tail = newNode;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException("List is empty");
        return head.item;
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException("List is empty");
        return tail.item;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;

        E item = head.item;
        head = head.next;

        if (head == null) {
            tail = null;
        } else {
            head.prev = null;
        }

        size--;
        return item;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;

        E item = tail.item;
        tail = tail.prev;

        if (tail == null) {
            head = null;
        } else {
            tail.next = null;
        }

        size--;
        return item;
    }

    @Override
    public int size() {
        return size;
    }

    // удаление по индексу
    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();

        Node<E> current = head;

        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        E item = current.item;
        unlink(current);
        return item;
    }

    // удаление по значению
    @Override
    public boolean remove(Object element) {
        Node<E> current = head;

        while (current != null) {
            if ((element == null && current.item == null) ||
                    (element != null && element.equals(current.item))) {
                unlink(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // вспомогательный метод удаления узла
    private void unlink(Node<E> node) {
        Node<E> prev = node.prev;
        Node<E> next = node.next;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
        }
        size--;
    }

    @Override public boolean offerFirst(E e) { return false; }
    @Override public boolean offerLast(E e) { return false; }
    @Override public E removeFirst() { return null; }
    @Override public E removeLast() { return null; }
    @Override public E peekFirst() { return null; }
    @Override public E peekLast() { return null; }
    @Override public boolean removeFirstOccurrence(Object o) { return false; }
    @Override public boolean removeLastOccurrence(Object o) { return false; }
    @Override public boolean offer(E e) { return false; }
    @Override public E remove() { return null; }
    @Override public E peek() { return null; }
    @Override public java.util.Iterator<E> iterator() { return null; }
    @Override public java.util.Iterator<E> descendingIterator() { return null; }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { return false; }
    @Override public void push(E e) {}
    @Override public E pop() { return null; }
    @Override public boolean contains(Object o) { return false; }
    @Override public void clear() {}
    @Override public boolean containsAll(java.util.Collection<?> c) { return false; }
    @Override public boolean removeAll(java.util.Collection<?> c) { return false; }
    @Override public boolean retainAll(java.util.Collection<?> c) { return false; }
    @Override public boolean isEmpty() { return false; }
    @Override public Object[] toArray() { return null; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}