package by.it.group410901.kovalevich.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<T> {
        T value;
        Node<T> prev;
        Node<T> next;

        Node(T value, Node<T> prev, Node<T> next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> head; // первый элемент
    private Node<E> tail; // последний элемент
    private int size;

    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }


    private Node<E> nodeAt(int index) {
        // быстрый доступ: если index ближе к head, идём слева, иначе справа
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index=" + index);
        }
        if (index < size / 2) {
            Node<E> cur = head;
            for (int i = 0; i < index; i++) {
                cur = cur.next;
            }
            return cur;
        } else {
            Node<E> cur = tail;
            for (int i = size - 1; i > index; i--) {
                cur = cur.prev;
            }
            return cur;
        }
    }

    private void linkFirst(E e) {
        Node<E> newNode = new Node<>(e, null, head);
        if (head == null) {
            // список был пуст
            head = newNode;
            tail = newNode;
        } else {
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    private void linkLast(E e) {
        Node<E> newNode = new Node<>(e, tail, null);
        if (tail == null) {
            // список был пуст
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    private E unlink(Node<E> node) {
        E val = node.value;
        Node<E> p = node.prev;
        Node<E> n = node.next;

        if (p == null) {
            // удаляем голову
            head = n;
        } else {
            p.next = n;
        }

        if (n == null) {
            // удаляем хвост
            tail = p;
        } else {
            n.prev = p;
        }

        node.prev = null;
        node.next = null;
        node.value = null;

        size--;
        return val;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node<E> cur = head;
        int i = 0;
        while (cur != null) {
            if (i > 0) sb.append(", ");
            sb.append(cur.value);
            cur = cur.next;
            i++;
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean add(E element) {
        // add() = addLast()
        addLast(element);
        return true;
    }

    //  удалить по индексу и вернуть удалённый элемент
    public E remove(int index) {
        Node<E> target = nodeAt(index);
        return unlink(target);
    }




    @Override
    public boolean remove(Object o) {
        Node<E> cur = head;
        while (cur != null) {
            if (o == null ? cur.value == null : o.equals(cur.value)) {
                unlink(cur);
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    // addFirst / addLast
    @Override
    public void addFirst(E e) {
        linkFirst(e);
    }

    @Override
    public void addLast(E e) {
        linkLast(e);
    }

    // element / getFirst / getLast
    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return head.value;
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return tail.value;
    }

    // poll / pollFirst / pollLast
    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        return unlink(head);
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        return unlink(tail);
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    //  исключение если пусто
    @Override
    public E removeFirst() {
        E v = pollFirst();
        if (v == null) throw new NoSuchElementException();
        return v;
    }

    @Override
    public E removeLast() {
        E v = pollLast();
        if (v == null) throw new NoSuchElementException();
        return v;
    }

    @Override
    public E peekFirst() {
        return size == 0 ? null : head.value;
    }

    @Override
    public E peekLast() {
        return size == 0 ? null : tail.value;
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public E remove() {
        E v = poll();
        if (v == null) throw new NoSuchElementException();
        return v;
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        Node<E> cur = head;
        while (cur != null) {
            if (o == null ? cur.value == null : o.equals(cur.value)) {
                unlink(cur);
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        Node<E> cur = tail;
        while (cur != null) {
            if (o == null ? cur.value == null : o.equals(cur.value)) {
                unlink(cur);
                return true;
            }
            cur = cur.prev;
        }
        return false;
    }

    // contains
    @Override
    public boolean contains(Object o) {
        Node<E> cur = head;
        while (cur != null) {
            if (o == null ? cur.value == null : o.equals(cur.value)) {
                return true;
            }
            cur = cur.next;
        }
        return false;
    }


    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> descendingIterator() {
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

    // методы из Collection

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E el : c) {
            addLast(el);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object el : c) {
            if (!contains(el)) return false;
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.next;
            if (c.contains(cur.value)) { // тут коллекцию c уже можно, она стандартная
                unlink(cur);
                changed = true;
            }
            cur = next;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.next;
            if (!c.contains(cur.value)) {
                unlink(cur);
                changed = true;
            }
            cur = next;
        }
        return changed;
    }

    @Override
    public void clear() {
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.next;
            cur.prev = null;
            cur.next = null;
            cur.value = null;
            cur = next;
        }
        head = null;
        tail = null;
        size = 0;
    }
}
