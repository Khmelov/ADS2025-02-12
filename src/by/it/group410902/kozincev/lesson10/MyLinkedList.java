package by.it.group410902.kozincev.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    // Вспомогательный класс для узлов двунаправленного списка
    private static class Node<E> {
        E element;
        Node<E> prev;
        Node<E> next;

        Node(E element, Node<E> prev, Node<E> next) {
            this.element = element;
            this.prev = prev;
            this.next = next;
        }
    }

    // Поля класса MyLinkedList
    private Node<E> first;
    private Node<E> last;
    private int size;

    public MyLinkedList() {
        first = null;
        last = null;
        size = 0;
    }

    @Override
    public boolean add(E element) { //
        addLast(element);
        return true;
    }

    @Override
    public void addLast(E element) { //
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(element, l, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        size++;
    }

    @Override
    public void addFirst(E element) { //
        final Node<E> f = first;
        final Node<E> newNode = new Node<>(element, null, f);
        first = newNode;
        if (f == null) {
            last = newNode;
        } else {
            f.prev = newNode;
        }
        size++;
    }

    private void checkEmpty() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
    }

    // Вспомогательный метод для извлечения первого элемента
    private E unlinkFirst() {
        final E element = first.element;
        final Node<E> next = first.next;
        first.element = null; // Освобождаем для GC
        first.next = null;
        first = next;
        if (next == null) {
            last = null;
        } else {
            next.prev = null;
        }
        size--;
        return element;
    }

    // Вспомогательный метод для извлечения последнего элемента
    private E unlinkLast() {
        final E element = last.element;
        final Node<E> prev = last.prev;
        last.element = null; // Освобождаем для GC
        last.prev = null;
        last = prev;
        if (prev == null) {
            first = null;
        } else {
            prev.next = null;
        }
        size--;
        return element;
    }

    // === Poll/Remove ===
    @Override
    public E poll() { //
        return (first == null) ? null : unlinkFirst();
    }

    @Override
    public E pollFirst() { //
        return (first == null) ? null : unlinkFirst();
    }

    @Override
    public E pollLast() { //
        return (last == null) ? null : unlinkLast();
    }

    // === Get/Element ===
    @Override
    public E getFirst() { //
        checkEmpty();
        return first.element;
    }

    @Override
    public E getLast() { //
        checkEmpty();
        return last.element;
    }

    @Override
    public E element() { //
        return getFirst();
    }

    @Override
    public int size() { //
        return size;
    }

    // Ищет узел по индексу с оптимизацией (начинает с начала или конца)
    private Node<E> node(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();

        if (index < (size >> 1)) { // size >> 1 это size / 2
            Node<E> x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            Node<E> x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }

    // Вспомогательный метод для разрыва связей узла
    private E unlink(Node<E> x) {
        final E element = x.element;
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        if (prev == null) {
            first = next; // Узел был первым
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            last = prev; // Узел был последним
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.element = null;
        size--;
        return element;
    }


    public E remove(int index) { // remove(int)
        return unlink(node(index));
    }

    @Override
    public boolean remove(Object o) { // remove(E element)
        if (o == null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.element == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.element)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() { //
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node<E> current = first;
        while (current != null) {
            sb.append(current.element);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append(']');
        return sb.toString();
    }

    @Override public boolean isEmpty() { return size == 0; }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean offerFirst(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean offerLast(E e) { throw new UnsupportedOperationException(); }
    @Override public E remove() { throw new UnsupportedOperationException(); }
    @Override public E removeFirst() { throw new UnsupportedOperationException(); }
    @Override public E removeLast() { throw new UnsupportedOperationException(); }
    @Override public E peek() { throw new UnsupportedOperationException(); }
    @Override public E peekFirst() { throw new UnsupportedOperationException(); }
    @Override public E peekLast() { throw new UnsupportedOperationException(); }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public void push(E e) { throw new UnsupportedOperationException(); }
    @Override public E pop() { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
}