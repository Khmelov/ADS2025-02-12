package by.it.group410902.kukhto.les11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private Node<E>[] table;
    private Node<E> head;
    private Node<E> tail;
    private int size;

    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> prev;
        Node<E> orderNext;//поле хранящее ссылку на след элемент при добавлении

        Node(E data) {
            this.data = data;
        }
    }

    public MyLinkedHashSet() {
        table = new Node[DEFAULT_CAPACITY];
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
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean add(E element) {
        int index = Math.abs(element == null ? 0 : element.hashCode()) % table.length;
        Node<E> current = table[index];

        while (current != null) {
            if (Objects.equals(current.data, element)) {
                return false;
            }
            current = current.next;
        }

        Node<E> newNode = new Node<>(element);
        newNode.next = table[index];
        table[index] = newNode;

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.orderNext = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
        return true;
    }

    @Override
    public boolean remove(Object element) {
        int index = Math.abs(element == null ? 0 : element.hashCode()) % table.length;
        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if (Objects.equals(current.data, element)) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }

                if (current.prev == null) {
                    head = current.orderNext;
                } else {
                    current.prev.orderNext = current.orderNext;
                }
                if (current.orderNext == null) {
                    tail = current.prev;
                } else {
                    current.orderNext.prev = current.prev;
                }
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
        int index = Math.abs(element == null ? 0 : element.hashCode()) % table.length;
        Node<E> current = table[index];

        while (current != null) {
            if (Objects.equals(current.data, element)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

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
        boolean modified = false;
        for (Object element : c) {
            if (remove(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> current = head;
        while (current != null) {
            if (!c.contains(current.data)) {
                Node<E> next = current.orderNext;
                remove(current.data);
                current = next;
                modified = true;
            } else {
                current = current.orderNext;
            }
        }
        return modified;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        int count = 0;
        while (current != null) {
            sb.append(current.data);
            count++;
            if (count < size) {
                sb.append(", ");
            }
            current = current.orderNext;
        }
        sb.append("]");
        return sb.toString();
    }

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