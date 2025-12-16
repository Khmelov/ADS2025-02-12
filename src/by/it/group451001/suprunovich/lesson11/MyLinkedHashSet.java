package by.it.group451001.suprunovich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private Node<E>[] table;
    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[16];
    }

    private static class Node<E> {
        final E value;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private int hash(Object o) {
        return (o == null) ? 0 : (o.hashCode() & 0x7FFFFFFF) % table.length;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        table = (Node<E>[]) new Node[oldTable.length * 2];
        for (int i = 0; i < oldTable.length; i++) {
            Node<E> curr = oldTable[i];
            while (curr != null) {
                Node<E> next = curr.next;
                int newIndex = hash(curr.value);
                curr.next = table[newIndex];
                table[newIndex] = curr;
                curr = next;
            }
        }
    }

    private void linkLast(Node<E> node) {
        if (tail == null) head = tail = node;
        else {
            tail.after = node;
            node.before = tail;
            tail = node;
        }
    }

    private void unlink(Node<E> node) {
        Node<E> before = node.before;
        Node<E> after = node.after;

        if (before == null) head = after;
        else before.after = after;
        if (after == null) tail = before;
        else after.before = before;
        node.before = node.after = null;
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
        for (int i = 0; i < table.length; i++) table[i] = null;
        head = tail = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        int index = hash(o);
        Node<E> curr = table[index];

        while (curr != null) {
            if (o == null ? curr.value == null : o.equals(curr.value)) return true;
            curr = curr.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        int index = hash(e);
        Node<E> curr = table[index];

        while (curr != null) {
            if (e == null ? curr.value == null : e.equals(curr.value)) return false;
            curr = curr.next;
        }

        Node<E> newNode = new Node<>(e, table[index]);
        table[index] = newNode;
        linkLast(newNode);
        size++;
        if (size >= table.length * 0.75) resize();
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = hash(o);
        Node<E> curr = table[index];
        Node<E> prev = null;

        while (curr != null) {
            if (o == null ? curr.value == null : o.equals(curr.value)) {
                if (prev == null) table[index] = curr.next;
                else prev.next = curr.next;
                unlink(curr);
                size--;
                return true;
            }
            prev = curr;
            curr = curr.next;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;

        Node<E> curr = head;
        while (curr != null) {
            if (!first) sb.append(", ");
            sb.append(curr.value);
            first = false;
            curr = curr.after;
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> curr = head;

            @Override
            public boolean hasNext() {
                return curr != null;
            }

            @Override
            public E next() {
                if (curr == null) throw new NoSuchElementException();
                E value = curr.value;
                curr = curr.after;
                return value;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int i = 0;
        for (E element : this) array[i++] = element;
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (E element : this) a[i++] = (T) element;
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) if (!contains(element)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c) if (add(element)) modified = true;
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> curr = head;
        while (curr != null) {
            Node<E> next = curr.after;
            if (!c.contains(curr.value)) {
                remove(curr.value);
                modified = true;
            }
            curr = next;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object element : c) if (remove(element)) modified = true;
        return modified;
    }
}