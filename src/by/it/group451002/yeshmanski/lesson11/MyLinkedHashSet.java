package by.it.group451002.yeshmanski.lesson11;

import java.util.Set;
import java.util.Iterator;
import java.util.Collection;

public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;

    private Node<E>[] table;
    private int size;

    private Entry<E> head;//узлы для сохранения порядка добавления нодов
    private Entry<E> tail;

    private static class Node<E> {
        E value;
        Node<E> next;
        Entry<E> orderRef; //ссылка на элемент в "списке порядка"
        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private static class Entry<E> {
        E value;
        Entry<E> next;
        Entry<E> prev;
        Entry(E value) {
            this.value = value;
        }
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
        head = null;
        tail = null;
    }

    private int hash(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clear() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        head = tail = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        int index = hash(o);
        Node<E> current = table[index];
        while (current != null) {
            if ((o == null && current.value == null) || (o != null && o.equals(current.value)))
                return true;
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) return false;

        int index = hash(e);
        Node<E> newNode = new Node<>(e, table[index]);
        table[index] = newNode;

        //добавляем в конец "порядкового списка"
        Entry<E> newEntry = new Entry<>(e);
        if (head == null) head = tail = newEntry;
        else {
            tail.next = newEntry;
            newEntry.prev = tail;
            tail = newEntry;
        }

        newNode.orderRef = newEntry;
        size++;
        return true;
    }


    @Override
    public boolean remove(Object o) {
        int index = hash(o);
        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if ((o == null && current.value == null) || (o != null && o.equals(current.value))) {
                //удаляем из таблицы
                if (prev == null) table[index] = current.next;
                else prev.next = current.next;

                //удаляем из списка порядка
                Entry<E> entry = current.orderRef;
                if (entry.prev != null) entry.prev.next = entry.next;
                else head = entry.next;

                if (entry.next != null) entry.next.prev = entry.prev;
                else tail = entry.prev;

                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Entry<E> current = head;
        boolean first = true;
        while (current != null) {
            if (!first) sb.append(", ");
            sb.append(current.value);
            first = false;
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }


    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object el : c) {
            if (!contains(el)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E el : c) {
            if (add(el)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object el : c) {
            if (remove(el)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Entry<E> current = head;
        while (current != null) {
            Entry<E> next = current.next;
            if (!c.contains(current.value)) {
                remove(current.value);
                changed = true;
            }
            current = next;
        }
        return changed;
    }


    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}