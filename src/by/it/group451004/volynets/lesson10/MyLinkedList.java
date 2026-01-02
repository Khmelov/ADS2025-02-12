package by.it.group451004.volynets.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.Objects; // ДОБАВИТЬ ЭТУ СТРОЧКУ

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> prev;

        Node(E data) {
            this.data = data;
        }
    }

    private Node<E> first;
    private Node<E> last;
    private int size;

    public MyLinkedList() {
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    // строковое представление списка
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

    // добавление элемента в конец (аналогично addLast)
    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    // удаление элемента по индексу
    public E remove(int index) {
        checkIndex(index);
        Node<E> nodeToRemove = getNode(index);
        return unlink(nodeToRemove);
    }

    // удаление элемента по значению
    @Override
    public boolean remove(Object o) {
        Node<E> current = first;
        while (current != null) {
            if (Objects.equals(o, current.data)) { // ТЕПЕРЬ РАБОТАЕТ
                unlink(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // количество элементов
    @Override
    public int size() {
        return size;
    }

    // добавление элемента в начало
    @Override
    public void addFirst(E e) {
        Node<E> newNode = new Node<>(e);
        if (size == 0) {
            first = last = newNode;
        } else {
            newNode.next = first;
            first.prev = newNode;
            first = newNode;
        }
        size++;
    }

    // добавление элемента в конец
    @Override
    public void addLast(E e) {
        Node<E> newNode = new Node<>(e);
        if (size == 0) {
            first = last = newNode;
        } else {
            last.next = newNode;
            newNode.prev = last;
            last = newNode;
        }
        size++;
    }

    // получение первого элемента без удаления (бросает исключение если пусто)
    @Override
    public E element() {
        return getFirst();
    }

    // получение первого элемента без удаления
    @Override
    public E getFirst() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }
        return first.data;
    }

    // получение последнего элемента без удаления
    @Override
    public E getLast() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }
        return last.data;
    }

    // удаление и возврат первого элемента (возвращает null если пусто)
    @Override
    public E poll() {
        return pollFirst();
    }

    // удаление и возврат первого элемента
    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }
        E element = first.data;
        unlink(first);
        return element;
    }

    // удаление и возврат последнего элемента
    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        E element = last.data;
        unlink(last);
        return element;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы                     ///////
    /////////////////////////////////////////////////////////////////////////

    // получение узла по индексу
    private Node<E> getNode(int index) {
        checkIndex(index);
        Node<E> current;
        if (index < (size >> 1)) {
            // ищем с начала
            current = first;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            // ищем с конца
            current = last;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    // удаление узла из списка
    private E unlink(Node<E> node) {
        E element = node.data;
        Node<E> next = node.next;
        Node<E> prev = node.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        node.data = null;
        size--;
        return element;
    }

    // проверка индекса
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////       Остальные методы Deque - заглушки                ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean offerFirst(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean offerLast(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E removeFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E removeLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E peekFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E peekLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean offer(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E peek() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void push(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E pop() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
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
}