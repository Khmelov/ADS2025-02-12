package by.it.group451004.rak.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    MyLinkedList<E>[] basket;
    int size = 0;
    static final int DEFAULT_CAPACITY = 256;
    private Node<E> head = null;
    private Node<E> tail = null;


    //================================================================================================================//

    public MyLinkedHashSet() {
        this(DEFAULT_CAPACITY);
    }

    public MyLinkedHashSet(int capacity) {
        basket = (MyLinkedList<E>[]) new MyLinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            basket[i] = new MyLinkedList<>();
        }
    }

    private int getIndex(Object o) {
        int hash = (o == null) ? 0 : o.hashCode();
        int index = hash % basket.length;
        if (index < 0) index += basket.length;
        return index;
    }

    //================================================================================================================//

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        boolean first = true;
        while (current != null) {
            if (!first) sb.append(", ");
            sb.append(current.value);
            first = false;
            current = current.after;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (MyLinkedList<E> list : basket) {
            list.removeAll();
        }
        head = tail = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private void rehash() {
        int newCapacity = basket.length * 2;
        MyLinkedList<E>[] newBasket = (MyLinkedList<E>[]) new MyLinkedList[newCapacity];
        for (int i = 0; i < newCapacity; i++) {
            newBasket[i] = new MyLinkedList<>();
        }

        Node<E> current = head;
        while (current != null) {
            int index = (current.value == null ? 0 : current.value.hashCode()) % newCapacity;
            if (index < 0) index += newCapacity;
            newBasket[index].add(current.value);
            current = current.after;
        }
        basket = newBasket;
    }

    @Override
    public boolean add(E e) {
        if (size >= basket.length) {
            rehash();
        }
        int index = getIndex(e);
        boolean added = basket[index].add(e);
        if (added) {
            Node<E> newNode = new Node<>(e);
            if (head == null) {
                head = tail = newNode;
            } else {
                tail.after = newNode;
                newNode.before = tail;
                tail = newNode;
            }
            size++;
        }
        return added;
    }

    @Override
    public boolean remove(Object o) {
        int index = getIndex(o);
        boolean removed = basket[index].remove(o);
        if (removed) {
            Node<E> current = head;
            while (current != null) {
                if (o == null ? current.value == null : o.equals(current.value)) {
                    if (current.before != null) current.before.after = current.after;
                    else head = current.after;

                    if (current.after != null) current.after.before = current.before;
                    else tail = current.before;
                    break;
                }
                current = current.after;
            }
            size--;
        }
        return removed;
    }

    @Override
    public boolean contains(Object o) {
        int index = getIndex(o);
        return basket[index].contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            if (add(e)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            if (remove(o)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.after;
            if (!c.contains(current.value)) {
                remove(current.value);
                changed = true;
            }
            current = next;
        }
        return changed;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    private static class Node<E> {
        E value;
        Node<E> before;
        Node<E> after;

        Node(E value) { this.value = value; }
    }
}

class MyLinkedList<E> {
    class Node {
        E value;
        Node next;
        Node prev;

        Node(E value) { this.value = value; }
    }

    Node first = null;
    Node last = null;
    int size = 0;

    boolean contains(Object o) {
        Node current = first;
        while (current != null) {
            if (o == null ? current.value == null : o.equals(current.value)) return true;
            current = current.next;
        }
        return false;
    }

    boolean add(E e) {
        if (contains(e)) return false;
        Node newNode = new Node(e);
        if (first == null) {
            first = last = newNode;
        } else {
            newNode.prev = last;
            last.next = newNode;
            last = newNode;
        }
        size++;
        return true;
    }

    boolean remove(Object o) {
        Node current = first;
        while (current != null) {
            if (o == null ? current.value == null : o.equals(current.value)) {
                if (current.prev != null) current.prev.next = current.next;
                else first = current.next;

                if (current.next != null) current.next.prev = current.prev;
                else last = current.prev;

                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    void removeAll() {
        Node current = first;
        while (current != null) {
            Node next = current.next;
            current.value = null;
            current.next = null;
            current.prev = null;
            current = next;
        }
        first = last = null;
        size = 0;
    }
}
