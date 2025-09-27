package by.it.group410902.menshikov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private Node<E>[] table;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[16];
    }

    private static class Node<E> {
        final E value;
        Node<E> next;
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
        int oldSize = size;
        size = 0;
        for (Node<E> head : oldTable) {
            while (head != null) {
                add(head.value);
                head = head.next;
            }
        }
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
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        int index = hash(o);
        Node<E> current = table[index];
        while (current != null) {
            if (o == null ? current.value == null : o.equals(current.value)) return true;
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        int index = hash(e);
        Node<E> current = table[index];
        while (current != null) {
            if (e == null ? current.value == null : e.equals(current.value)) return false;
            current = current.next;
        }
        table[index] = new Node<>(e, table[index]);
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
        for (Node<E> head : table) {
            Node<E> curr = head;
            while (curr != null) {
                if (!first) sb.append(", ");
                sb.append(curr.value);
                first = false;
                curr = curr.next;
            }
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int bucketIndex = 0;
            private Node<E> curr = null;
            private void advance() {
                while (curr == null && bucketIndex < table.length) curr = table[bucketIndex++];
            }

            @Override
            public boolean hasNext() {
                advance();
                return curr != null;
            }

            @Override
            public E next() {
                advance();
                if (curr == null) throw new NoSuchElementException();
                E value = curr.value;
                curr = curr.next;
                return value;
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
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            if (!c.contains(iterator.next())) {
                iterator.remove();
                modified = true;
            }
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