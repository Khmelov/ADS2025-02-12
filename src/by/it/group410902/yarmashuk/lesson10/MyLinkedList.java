package by.it.group410902.yarmashuk.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    private Node<E> head; // Голова списка
    private Node<E> tail; // Хвост списка
    private int size;     // Количество элементов в списке


    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(E element, Node<E> next, Node<E> prev) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }
    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<E> current = head;
        while (current != null) {
            sb.append(current.item);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E element) {addLast(element); return true;
    }

    public boolean remove(Object element) {
        if (element == null) {

            return false;
        }
        Node<E> current = head;
        while (current != null) {
            if (element.equals(current.item)) {
                removeNode(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }
    private void removeNode(Node<E> node) {
        Node<E> prevNode = node.prev;
        Node<E> nextNode = node.next;

        if (prevNode != null) {
            prevNode.next = nextNode;
        } else { // Удаляем голову
            head = nextNode;
        }

        if (nextNode != null) {
            nextNode.prev = prevNode;
        } else { // Удаляем хвост
            tail = prevNode;
        }

        node.item = null; // Обнуляем ссылку
        node.next = null; // Освобождаем узел
        node.prev = null;
        size--;
    }


    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<E> current = head;
        int i = 0;
        while (i < index) {
            current = current.next;
            i++;
        }
        E item = current.item;
        removeNode(current);
        return item;
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E element) {
        Node<E> newNode = new Node<>(element, head, null);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }
    @Override
    public void addLast(E element) {
        Node<E> newNode = new Node<>(element, null, tail);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }


    @Override
    public E getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        return head.item;
    }
    @Override
    public E getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        return tail.item;
    }

    @Override
    public E poll() {
        return pollFirst();
    }
    @Override
    public E pollFirst() {
        if (isEmpty()) {
            return null;
        }
        E item = head.item;
        Node<E> oldHead = head;
        head = head.next;
        oldHead.item = null; // Обнуляем ссылку для сборщика мусора
        oldHead.next = null; // Освобождаем узел
        if (head != null) {
            head.prev = null;
        } else {
            tail = null; // Если список стал пустым
        }
        size--;
        return item;
    }
    @Override
    public E pollLast() {
        if (isEmpty()) {
            return null;
        }
        E item = tail.item;
        Node<E> oldTail = tail;
        tail = tail.prev;
        oldTail.item = null; // Обнуляем ссылку
        oldTail.prev = null; // Освобождаем узел
        if (tail != null) {
            tail.next = null;
        } else {
            head = null; // Если список стал пустым
        }
        size--;
        return item;
    }




    @Override
    public boolean offer(E e) {
        return false;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
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

    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }

    @Override
    public boolean offerFirst(E e) {
        return false;
    }

    @Override
    public boolean offerLast(E e) {
        return false;
    }

    @Override
    public E removeFirst() {
        return null;
    }

    @Override
    public E removeLast() {
        return null;
    }
    @Override
    public E peek() {
        return null;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public void push(E e) {

    }

    @Override
    public E pop() {
        return null;
    }



    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }
    @Override
    public E peekFirst() {
        return null;
    }

    @Override
    public E peekLast() {
        return null;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;

    }
    @Override
    public E remove() {
        return null;
    }
}
