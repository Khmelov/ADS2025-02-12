package by.it.group451003.sorokin.lesson10;

import java.util.Deque;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        E data;
        Node<E> prev;
        Node<E> next;

        Node(E data) {
            this.data = data;
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

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<E> current = first;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<E> toRemove = getNode(index);
        return unlink(toRemove);
    }

    @Override
    public boolean remove(Object o) {
        Node<E> current = first;
        while (current != null) {
            if (equals(o, current.data)) {
                unlink(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E element) {
        Node<E> newNode = new Node<>(element);

        if (size == 0) {
            first = newNode;
            last = newNode;
        } else {
            newNode.next = first;
            first.prev = newNode;
            first = newNode;
        }
        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> newNode = new Node<>(element);

        if (size == 0) {
            first = newNode;
            last = newNode;
        } else {
            newNode.prev = last;
            last.next = newNode;
            last = newNode;
        }
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException("List is empty");
        }
        return first.data;
    }

    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException("List is empty");
        }
        return last.data;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }
        return unlink(first);
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        return unlink(last);
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы                      ///////
    /////////////////////////////////////////////////////////////////////////

    // Безопасное сравнение объектов (аналог Objects.equals)
    private boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    // Получение узла по индексу
    private Node<E> getNode(int index) {
        // Оптимизация: выбираем с какого конца идти
        if (index < (size >> 1)) {
            // Идем с начала
            Node<E> current = first;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            return current;
        } else {
            // Идем с конца
            Node<E> current = last;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
            return current;
        }
    }

    // Удаление узла из списка
    private E unlink(Node<E> node) {
        E data = node.data;
        Node<E> prev = node.prev;
        Node<E> next = node.next;

        // Обновляем связи соседних узлов
        if (prev == null) {
            // Удаляем первый элемент
            first = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            // Удаляем последний элемент
            last = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        node.data = null; // пометка для GC
        size--;
        return data;
    }

    /////////////////////////////////////////////////////////////////////////
    //////       Остальные методы Deque - можно оставить пустыми     ///////
    /////////////////////////////////////////////////////////////////////////

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

    @Override
    public E removeFirst() {
        if (size == 0) throw new NoSuchElementException();
        return pollFirst();
    }

    @Override
    public E removeLast() {
        if (size == 0) throw new NoSuchElementException();
        return pollLast();
    }

    @Override
    public E peekFirst() {
        return (size == 0) ? null : getFirst();
    }

    @Override
    public E peekLast() {
        return (size == 0) ? null : getLast();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        // Ищем с конца
        Node<E> current = last;
        while (current != null) {
            if (equals(o, current.data)) {
                unlink(current);
                return true;
            }
            current = current.prev;
        }
        return false;
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        // Удаляем все ссылки для помощи GC
        Node<E> current = first;
        while (current != null) {
            Node<E> next = current.next;
            current.data = null;
            current.prev = null;
            current.next = null;
            current = next;
        }
        first = null;
        last = null;
        size = 0;
    }

    // Остальные методы можно оставить пустыми
    @Override
    public boolean addAll(java.util.Collection<? extends E> c) { return false; }
    @Override
    public boolean removeAll(java.util.Collection<?> c) { return false; }
    @Override
    public boolean retainAll(java.util.Collection<?> c) { return false; }
    @Override
    public boolean containsAll(java.util.Collection<?> c) { return false; }
    @Override
    public boolean contains(Object o) { return false; }
    @Override
    public java.util.Iterator<E> iterator() { return null; }
    @Override
    public java.util.Iterator<E> descendingIterator() { return null; }
    @Override
    public Object[] toArray() { return new Object[0]; }
    @Override
    public <T> T[] toArray(T[] a) { return null; }
}