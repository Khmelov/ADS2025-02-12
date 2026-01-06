package by.it.group451004.matyrka.lesson10;

import java.util.Deque;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> prev;

        Node(E data) {
            this.data = data;
        }

        Node(E data, Node<E> prev, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> first;
    private Node<E> last;
    private int size;

    public MyLinkedList() {
        first = null;
        last = null;
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = first;
        while (curr != null) {
            sb.append(curr.data);
            if (curr.next != null) sb.append(", ");
            curr = curr.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        Node<E> curr = first;
        if (o == null) {
            while (curr != null) {
                if (curr.data == null) {
                    unlink(curr);
                    return true;
                }
                curr = curr.next;
            }
        } else {
            while (curr != null) {
                if (o.equals(curr.data)) {
                    unlink(curr);
                    return true;
                }
                curr = curr.next;
            }
        }
        return false;
    }

    public E remove(int index) {
        checkElementIndex(index);
        return unlink(node(index));
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E element) {
        if (element == null) throw new NullPointerException();
        linkFirst(element);
    }

    @Override
    public void addLast(E element) {
        if (element == null) throw new NullPointerException();
        linkLast(element);
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (first == null) throw new NoSuchElementException();
        return first.data;
    }

    @Override
    public E getLast() {
        if (last == null) throw new NoSuchElementException();
        return last.data;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (first == null) return null;
        return unlinkFirst();
    }

    @Override
    public E pollLast() {
        if (last == null) return null;
        return unlinkLast();
    }

    private void linkFirst(E element) {
        Node<E> newNode = new Node<>(element);
        if (first == null) {
            first = last = newNode;
        } else {
            newNode.next = first;
            first.prev = newNode;
            first = newNode;
        }
        size++;
    }

    private void linkLast(E element) {
        Node<E> newNode = new Node<>(element);
        if (last == null) {
            first = last = newNode;
        } else {
            newNode.prev = last;
            last.next = newNode;
            last = newNode;
        }
        size++;
    }

    private E unlinkFirst() {
        E data = first.data;
        Node<E> next = first.next;

        first.data = null;
        first.next = null;

        first = next;
        if (next == null) last = null;
        else next.prev = null;

        size--;
        return data;
    }

    private E unlinkLast() {
        E data = last.data;
        Node<E> prev = last.prev;

        last.data = null;
        last.prev = null;

        last = prev;
        if (prev == null) first = null;
        else prev.next = null;

        size--;
        return data;
    }

    private E unlink(Node<E> node) {
        E data = node.data;
        Node<E> prev = node.prev;
        Node<E> next = node.next;

        if (prev == null) first = next;
        else prev.next = next;

        if (next == null) last = prev;
        else next.prev = prev;

        node.prev = node.next = null;
        node.data = null;

        size--;
        return data;
    }

    private Node<E> node(int index) {
        if (index < (size >> 1)) {
            Node<E> curr = first;
            for (int i = 0; i < index; i++) curr = curr.next;
            return curr;
        } else {
            Node<E> curr = last;
            for (int i = size - 1; i > index; i--) curr = curr.prev;
            return curr;
        }
    }

    private void checkElementIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public E get(int index) {
        checkElementIndex(index);
        return node(index).data;
    }

    @Override public boolean offer(E e) { return add(e); }
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }

    @Override public E remove() { return removeFirst(); }
    @Override public E removeFirst() { if (first == null) throw new NoSuchElementException(); return unlinkFirst(); }
    @Override public E removeLast() { if (last == null) throw new NoSuchElementException(); return unlinkLast(); }

    @Override public E peek() { return peekFirst(); }
    @Override public E peekFirst() { return first == null ? null : first.data; }
    @Override public E peekLast() { return last == null ? null : last.data; }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        Node<E> curr = last;
        if (o == null) {
            while (curr != null) {
                if (curr.data == null) {
                    unlink(curr);
                    return true;
                }
                curr = curr.prev;
            }
        } else {
            while (curr != null) {
                if (o.equals(curr.data)) {
                    unlink(curr);
                    return true;
                }
                curr = curr.prev;
            }
        }
        return false;
    }

    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return removeFirst(); }

    @Override public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }

    @Override
    public boolean contains(Object o) {
        Node<E> curr = first;
        if (o == null) {
            while (curr != null) {
                if (curr.data == null) return true;
                curr = curr.next;
            }
        } else {
            while (curr != null) {
                if (o.equals(curr.data)) return true;
                curr = curr.next;
            }
        }
        return false;
    }

    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override
    public void clear() {
        Node<E> curr = first;
        while (curr != null) {
            Node<E> next = curr.next;
            curr.data = null;
            curr.next = null;
            curr.prev = null;
            curr = next;
        }
        first = last = null;
        size = 0;
    }

    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        Node<E> curr = first;
        int i = 0;
        while (curr != null) {
            arr[i++] = curr.data;
            curr = curr.next;
        }
        return arr;
    }

    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}
