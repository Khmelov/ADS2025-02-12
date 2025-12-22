package by.it.group451003.plyushchevich.lesson09;

import java.util.*;

public class ListC<E> implements List<E> {

    private static class Node<E> {
        E data;
        Node<E> next;
        Node(E data) { this.data = data; }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    // ---------------- ОБЯЗАТЕЛЬНЫЕ методы ---------------- //

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = head;
        while (cur != null) {
            sb.append(cur.data);
            cur = cur.next;
            if (cur != null) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        Node<E> newNode = new Node<>(e);
        if (head == null) {
            head = tail = newNode; // первый элемент
        } else {
            tail.next = newNode;   // добавляем за хвост
            tail = newNode;        // обновляем хвост
        }
        size++;
        return true;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        Node<E> newNode = new Node<>(element);
        if (index == 0) {
            newNode.next = head;
            head = newNode;
            if (size == 0) tail = newNode; // если был пустой список
        } else {
            Node<E> prev = nodeAt(index - 1);
            newNode.next = prev.next;
            prev.next = newNode;
            if (prev == tail) tail = newNode; // если вставили в конец
    }
        size++;
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        return nodeAt(index).data;
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        Node<E> node = nodeAt(index);
        E old = node.data;
        node.data = element;
        return old;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index);
        ListC.Node<E> prev = null;
        ListC.Node<E> cur = head;
        for (int i = 0; i < index; i++) {
            prev = cur;
            cur = cur.next;
        }
        E val = cur.data;
        if (prev == null) { // removing head
            head = cur.next;
            if (head == null) tail = null; // was last
        } else {
            prev.next = cur.next;
            if (prev.next == null) tail = prev; // removed last
        }
        size--;
        return val;
    }

    @Override
    public boolean remove(Object o) {
        if (head == null) return false;
        if (Objects.equals(head.data, o)) {
            head = head.next;
            if (head == null) tail = null;
            size--;
            return true;
        }
        Node<E> cur = head;
        while (cur.next != null) {
            if (Objects.equals(cur.next.data, o)) {
                cur.next = cur.next.next;
                if (cur.next == null) tail = cur; // удалили последний
                size--;
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        head = tail = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o) { return indexOf(o) >= 0; }

    @Override
    public int indexOf(Object o) {
        int i = 0;
        Node<E> cur = head;
        while (cur != null) {
            if (Objects.equals(cur.data, o)) return i;
            cur = cur.next;
            i++;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int last = -1, i = 0;
        Node<E> cur = head;
        while (cur != null) {
            if (Objects.equals(cur.data, o)) last = i;
            cur = cur.next;
            i++;
        }
        return last;
    }

    // ---------------- ВСПОМОГАТЕЛЬНЫЕ методы ---------------- //

    private void checkIndex(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
    }

    private Node<E> nodeAt(int index) {
        Node<E> cur = head;
        for (int i = 0; i < index; i++) cur = cur.next;
        return cur;
    }

    // ---------------- Остальные методы ---------------- //

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
        for (E e : c) {
            add(e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        boolean changed = false;
        for (E e : c) {
            add(index++, e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;
        Node<E> prev = null;
        Node<E> cur = head;
        while (cur != null) {
            if (c.contains(cur.data)) {
                if (prev == null) head = cur.next;
                else prev.next = cur.next;
                if (cur.next == null) tail = prev;
                size--;
                changed = true;
                cur = (prev == null) ? head : prev.next;
            } else {
                prev = cur;
                cur = cur.next;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;
        Node<E> prev = null;
        Node<E> cur = head;
        while (cur != null) {
            if (!c.contains(cur.data)) {
                if (prev == null) head = cur.next;
                else prev.next = cur.next;
                if (cur.next == null) tail = prev;
                size--;
                changed = true;
                cur = (prev == null) ? head : prev.next;
            } else {
                prev = cur;
                cur = cur.next;
            }
        }
        return changed;
    }

    // ---------------- Необязательные методы ---------------- //

    @Override
    public List<E> subList(int fromIndex, int toIndex) { return null; }

    @Override
    public ListIterator<E> listIterator(int index) { return null; }

    @Override
    public ListIterator<E> listIterator() { return null; }

    @Override
    public <T> T[] toArray(T[] a) { return null; }

    @Override
    public Object[] toArray() { return new Object[0]; }

    @Override
    public Iterator<E> iterator() { return null; }

}
