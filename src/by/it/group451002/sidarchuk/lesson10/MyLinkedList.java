package by.it.group451002.sidarchuk.lesson10;

import java.util.Deque;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {
    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> prev;

        Node(E data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }

        Node(E data, Node<E> prev, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    // Конструктор
    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    // Возвращает строковое представление списка
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
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

    // Добавляет элемент в конец списка
    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    // Удаляет элемент по индексу
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node<E> nodeToRemove = getNode(index);
        return unlink(nodeToRemove);
    }

    // Удаляет первое вхождение указанного элемента
    @Override
    public boolean remove(Object element) {
        Node<E> current = head;
        while (current != null) {
            if (element == null) {
                if (current.data == null) {
                    unlink(current);
                    return true;
                }
            } else {
                if (element.equals(current.data)) {
                    unlink(current);
                    return true;
                }
            }
            current = current.next;
        }
        return false;
    }

    // Возвращает количество элементов в списке
    @Override
    public int size() {
        return size;
    }

    // Добавляет элемент в начало списка
    @Override
    public void addFirst(E element) {
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }

        Node<E> newNode = new Node<>(element);
        if (head == null) {
            // Список пуст
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    // Добавляет элемент в конец списка
    @Override
    public void addLast(E element) {
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }

        Node<E> newNode = new Node<>(element);
        if (tail == null) {
            // Список пуст
            head = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
        }
        tail = newNode;
        size++;
    }

    // Возвращает первый элемент без удаления
    @Override
    public E element() {
        return getFirst();
    }

    // Возвращает первый элемент без удаления
    @Override
    public E getFirst() {
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }
        return head.data;
    }

    // Возвращает последний элемент без удаления
    @Override
    public E getLast() {
        if (tail == null) {
            throw new NoSuchElementException("List is empty");
        }
        return tail.data;
    }

    // Удаляет и возвращает первый элемент
    @Override
    public E poll() {
        return pollFirst();
    }

    // Удаляет и возвращает первый элемент
    @Override
    public E pollFirst() {
        if (head == null) {
            return null;
        }
        return unlink(head);
    }

    // Удаляет и возвращает последний элемент
    @Override
    public E pollLast() {
        if (tail == null) {
            return null;
        }
        return unlink(tail);
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы                       ///////
    /////////////////////////////////////////////////////////////////////////

    // Возвращает узел по индексу
    private Node<E> getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node<E> current;
        if (index < size / 2) {
            // Ищем с начала
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            // Ищем с конца
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    // Удаляет узел из списка и возвращает его данные
    private E unlink(Node<E> node) {
        E data = node.data;

        if (node.prev == null) {
            // Удаляем голову
            head = node.next;
        } else {
            node.prev.next = node.next;
        }

        if (node.next == null) {
            // Удаляем хвост
            tail = node.prev;
        } else {
            node.next.prev = node.prev;
        }

        // Помогаем сборщику мусора
        node.data = null;
        node.next = null;
        node.prev = null;

        size--;
        return data;
    }

    /////////////////////////////////////////////////////////////////////////
    //////     Остальные методы интерфейса Deque (необязательные)    ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
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

    @Override
    public E remove() {
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }
        return pollFirst();
    }

    @Override
    public E removeFirst() {
        return remove();
    }

    @Override
    public E removeLast() {
        if (tail == null) {
            throw new NoSuchElementException("List is empty");
        }
        return pollLast();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public E peekFirst() {
        return (head == null) ? null : head.data;
    }

    @Override
    public E peekLast() {
        return (tail == null) ? null : tail.data;
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
    public boolean contains(Object o) {
        Node<E> current = head;
        while (current != null) {
            if (o == null) {
                if (current.data == null) {
                    return true;
                }
            } else {
                if (o.equals(current.data)) {
                    return true;
                }
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        Node<E> current = tail;
        while (current != null) {
            if (o == null) {
                if (current.data == null) {
                    unlink(current);
                    return true;
                }
            } else {
                if (o.equals(current.data)) {
                    unlink(current);
                    return true;
                }
            }
            current = current.prev;
        }
        return false;
    }

    // Методы, которые не реализованы для этого задания
    @Override
    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
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
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}
