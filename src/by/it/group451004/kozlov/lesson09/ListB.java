package by.it.group451004.kozlov.lesson09;

import java.util.*;

public class ListB<E> implements List<E> {

    //Linked list
    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> prev;

        Node(E value) {
            this.value = value;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    @Override
    public boolean add(E e) {
        Node<E> newNode = new Node<>(e);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
        return true;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        Node<E> newNode = new Node<>(element);
        if (index == size) {
            add(element);
            return;
        }
        Node<E> current = getNode(index);
        Node<E> prev = current.prev;
        newNode.next = current;
        newNode.prev = prev;
        current.prev = newNode;
        if (prev != null) {
            prev.next = newNode;
        } else {
            head = newNode;
        }
        size++;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<E> node = getNode(index);
        Node<E> prev = node.prev;
        Node<E> next = node.next;
        if (prev != null) prev.next = next;
        else head = next;
        if (next != null) next.prev = prev;
        else tail = prev;
        size--;
        return node.value;
    }

    @Override
    public boolean remove(Object o) {
        Node<E> current = head;
        while (current != null) {
            if (Objects.equals(current.value, o)) {
                Node<E> prev = current.prev;
                Node<E> next = current.next;
                if (prev != null) prev.next = next;
                else head = next;
                if (next != null) next.prev = prev;
                else tail = prev;
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        Node<E> node = getNode(index);
        E old = node.value;
        node.value = element;
        return old;
    }

    @Override
    public E get(int index) {
        return getNode(index).value;
    }

    private Node<E> getNode(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<E> current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) current = current.next;
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) current = current.prev;
        }
        return current;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        head = tail = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int indexOf(Object o) {
        Node<E> current = head;
        for (int i = 0; current != null; i++, current = current.next) {
            if (Objects.equals(current.value, o)) return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        Node<E> current = tail;
        for (int i = size - 1; current != null; i--, current = current.prev) {
            if (Objects.equals(current.value, o)) return i;
        }
        return -1;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        while (current != null) {
            sb.append(current.value);
            if (current.next != null) sb.append(", ");
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override public boolean containsAll(Collection<?> c) { return false; }
    @Override public boolean addAll(Collection<? extends E> c) { return false; }
    @Override public boolean addAll(int index, Collection<? extends E> c) { return false; }
    @Override public boolean removeAll(Collection<?> c) { return false; }
    @Override public boolean retainAll(Collection<?> c) { return false; }
    @Override public List<E> subList(int fromIndex, int toIndex) { return null; }
    @Override public ListIterator<E> listIterator(int index) { return null; }
    @Override public ListIterator<E> listIterator() { return null; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public Iterator<E> iterator() { return null; }
}
