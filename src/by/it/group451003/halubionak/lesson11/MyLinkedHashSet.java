package by.it.group451003.halubionak.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;    // для цепочек хеш-таблицы
        Node<E> before, after; // для порядка вставки
        Node(E value) { this.value = value; }
    }

    private Node<E>[] table;
    private int size = 0;
    private static final int INITIAL_CAPACITY = 16;
    private Node<E> head, tail; // для порядка вставки

    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[INITIAL_CAPACITY];
    }

    private int index(Object o) {
        return (o == null ? 0 : o.hashCode() & 0x7fffffff) % table.length;
    }

    private void resizeIfNeeded() {
        if (size > table.length * 0.75) {
            Node<E>[] oldTable = table;
            table = (Node<E>[]) new Node[table.length * 2];
            Node<E> curr = head;
            size = 0;
            head = tail = null;
            while (curr != null) {
                add(curr.value);
                curr = curr.after;
            }
        }
    }

    @Override
    public boolean add(E e) {
        resizeIfNeeded();
        int idx = index(e);
        Node<E> curr = table[idx];
        while (curr != null) {
            if ((e == null && curr.value == null) || (e != null && e.equals(curr.value))) {
                return false; // уже есть
            }
            curr = curr.next;
        }
        Node<E> newNode = new Node<>(e);
        newNode.next = table[idx];
        table[idx] = newNode;

        // добавляем в конец связного списка для сохранения порядка
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode;
        }

        size++;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        int idx = index(o);
        Node<E> curr = table[idx];
        while (curr != null) {
            if ((o == null && curr.value == null) || (o != null && o.equals(curr.value))) {
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        int idx = index(o);
        Node<E> curr = table[idx];
        Node<E> prev = null;
        while (curr != null) {
            if ((o == null && curr.value == null) || (o != null && o.equals(curr.value))) {
                // удалить из хеш-цепочки
                if (prev == null) table[idx] = curr.next;
                else prev.next = curr.next;

                // удалить из связного списка для порядка
                if (curr.before != null) curr.before.after = curr.after;
                else head = curr.after;

                if (curr.after != null) curr.after.before = curr.before;
                else tail = curr.before;

                size--;
                return true;
            }
            prev = curr;
            curr = curr.next;
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) table[i] = null;
        head = tail = null;
        size = 0;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = head;
        boolean first = true;
        while (curr != null) {
            if (!first) sb.append(", ");
            sb.append(curr.value);
            first = false;
            curr = curr.after;
        }
        sb.append("]");
        return sb.toString();
    }

    // =========== Методы Collection ======================
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) if (add(e)) changed = true;
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) if (remove(o)) changed = true;
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> curr = head;
        while (curr != null) {
            Node<E> next = curr.after;
            if (!c.contains(curr.value)) {
                remove(curr.value);
                changed = true;
            }
            curr = next;
        }
        return changed;
    }

    @Override
    public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override
    public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override
    public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}
