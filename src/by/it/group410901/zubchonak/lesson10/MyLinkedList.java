package by.it.group410901.zubchonak.lesson10;

import java.util.*;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> prev;
        Node(E data, Node<E> prev, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> first;
    private Node<E> last;
    private int size = 0;

    public MyLinkedList() {}
//Метод toString — возвращает строку вида [элем1, элем2, ...]
    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = first;
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(cur.data);
            cur = cur.next;
        }
        return sb.append("]").toString();
    }
//Метод size — возвращает текущее количество элементов
    @Override
    public int size() {
        return size;
    }
//Метод add — добавляет элемент в конец
    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }
//Метод addFirst — добавляет элемент в начало deque
    @Override
    public void addFirst(E e) {
        if (e == null) throw new NullPointerException();
        Node<E> newNode = new Node<>(e, null, first);
        if (first == null) {
            last = newNode;
        } else {
            first.prev = newNode;
        }
        first = newNode;
        size++;
    }
//Метод addLast — добавляет элемент в конец deque
    @Override
    public void addLast(E e) {
        if (e == null) throw new NullPointerException();
        Node<E> newNode = new Node<>(e, last, null);
        if (last == null) {
            first = newNode;
        } else {
            last.next = newNode;
        }
        last = newNode;
        size++;
    }
//Метод element — возвращает первый элемент
    @Override
    public E element() {
        return getFirst();
    }
//Метод getFirst — возвращает данные первого узла.
    @Override
    public E getFirst() {
        if (first == null) throw new NoSuchElementException();
        return first.data;
    }
//Метод getLast — возвращает данные последнего узла.
    @Override
    public E getLast() {
        if (last == null) throw new NoSuchElementException();
        return last.data;
    }
//Метод poll — удаляет и возвращает первый элемент
    @Override
    public E poll() {
        return pollFirst();
    }
//Метод pollFirst — удаляет первый узел.
    @Override
    public E pollFirst() {
        if (first == null) return null;
        E data = first.data;
        first = first.next;
        if (first == null) {
            last = null;
        } else {
            first.prev = null;
        }
        size--;
        return data;
    }
//Метод pollLast — удаляет последний узел.
    @Override
    public E pollLast() {
        if (last == null) return null;
        E data = last.data;
        last = last.prev;
        if (last == null) {
            first = null;
        } else {
            last.next = null;
        }
        size--;
        return data;
    }

    //обязательные дополнительные методы
    @Override
    public boolean remove(Object o) {
        Node<E> cur = first;
        for (int i = 0; i < size; i++) {
            if (o == null ? cur.data == null : o.equals(cur.data)) {
                unlink(cur);
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> cur = first;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        E data = cur.data;
        unlink(cur);
        return data;
    }

    private void unlink(Node<E> node) {
        Node<E> prev = node.prev;
        Node<E> next = node.next;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        node.data = null;
        size--;
    }

    // === Остальные методы Deque ===
    @Override public boolean offerFirst(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean offerLast(E e) { throw new UnsupportedOperationException(); }
    @Override public E peek() { throw new UnsupportedOperationException(); }
    @Override public E peekFirst() { throw new UnsupportedOperationException(); }
    @Override public E peekLast() { throw new UnsupportedOperationException(); }
    @Override public E remove() { throw new UnsupportedOperationException(); }
    @Override public E removeFirst() { throw new UnsupportedOperationException(); }
    @Override public E removeLast() { throw new UnsupportedOperationException(); }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean isEmpty() { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public E pop() { throw new UnsupportedOperationException(); }
    @Override public void push(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
}