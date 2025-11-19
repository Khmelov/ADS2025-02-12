package by.it.group451004.akbulatov.lesson11;

import java.util.*;

public class MyLinkedHashSet<E> implements Set<E>
{
    private static class Node<E>
    {
        E item;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        Node(E element, Node<E> next)
        {
            this.item = element;
            this.next = next;
        }
    }

    private static final int DEFAULT_CAPACITY = 16;
    private final Node<E>[] table;
    private Node<E> head;
    private Node<E> tail;
    private int size;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet()
    {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        head = null;
        tail = null;
        size = 0;
    }

    private int getIndex(Object obj)
    {
        if (obj == null) return 0;
        return (obj.hashCode() & 0x7FFFFFFF) % table.length;
    }

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public boolean isEmpty()
    {
        return size == 0;
    }

    @Override
    public void clear()
    {
        Arrays.fill(table, null);
        head = tail = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o)
    {
        int index = getIndex(o);
        Node<E> current = table[index];

        while (current != null)
        {
            if (Objects.equals(o, current.item)) return true;

            current = current.next;
        }
        return false;
    }

    @Override
    public boolean add(E e)
    {
        int index = getIndex(e);
        if (contains(e)) return false;

        Node<E> newNode = new Node<>(e, table[index]);
        table[index] = newNode;
        size++;

        if (head == null)
            head = tail = newNode;
        else
        {
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode;
        }

        return true;
    }

    @Override
    public boolean remove(Object o)
    {
        int index = getIndex(o);
        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null)
        {
            if (Objects.equals(o, current.item))
            {
                if (prev == null)
                    table[index] = current.next;
                else
                    prev.next = current.next;

                if (current.before != null)
                    current.before.after = current.after;
                else
                    head = current.after;

                if (current.after != null)
                    current.after.before = current.before;
                else
                    tail = current.before;

                size--;
                return true;
            }

            prev = current;
            current = current.next;
        }

        return false;
    }

    @Override
    public String toString()
    {
        if (isEmpty()) return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        Node<E> current = head;
        while (current != null)
        {
            sb.append(current.item == null ? "null" : current.item.toString());
            current = current.after;
            if (current != null) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        for (Object o : c)
            if (!contains(o))
                return false;

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c)
    {
        boolean modified = false;

        for (E e : c)
            if (add(e))
                modified = true;

        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        boolean modified = false;

        for (Object o : c)
            if (remove(o))
                modified = true;

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        boolean modified = false;
        Node<E> current = head;

        while (current != null)
        {
            Node<E> next = current.after;
            if (!c.contains(current.item))
            {
                remove(current.item);
                modified = true;
            }
            current = next;
        }
        return modified;
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