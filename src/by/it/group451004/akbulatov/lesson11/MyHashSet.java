package by.it.group451004.akbulatov.lesson11;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E>
{
    private static class Node<E>
    {
        E item;
        Node<E> next;

        Node(E element, Node<E> next)
        {
            this.item = element;
            this.next = next;
        }
    }

    private static final int DEFAULT_CAPACITY = 16;
    private final Node<E>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashSet()
    {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
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
        size = 0;
    }

    @Override
    public boolean contains(Object o)
    {
        int index = getIndex(o);
        Node<E> current = table[index];

        while (current != null)
        {
            if (o == null)
            {
                if (current.item == null) return true;
            }
            else
            {
                if (o.equals(current.item)) return true;
            }

            current = current.next;
        }

        return false;
    }

    @Override
    public boolean add(E e)
    {
        int index = getIndex(e);
        if (this.contains(e)) return false;

        Node<E> newNode = new Node<>(e, table[index]);
        table[index] = newNode;
        size++;

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
            boolean found = false;
            if (o == null)
            {
                if (current.item == null)
                    found = true;
            }
            else
            {
                if (o.equals(current.item))
                    found = true;
            }

            if (found)
            {
                if (prev == null)
                    table[index] = current.next;
                else
                    prev.next = current.next;

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

        boolean isFirst = true;
        for (Node<E> eNode : table)
        {
            Node<E> current = eNode;
            while (current != null)
            {
                if (isFirst)
                    isFirst = false;
                else
                    sb.append(", ");

                sb.append(current.item == null ? "null" : current.item.toString());
                current = current.next;
            }
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

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
}