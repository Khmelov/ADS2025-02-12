package by.it.group410902.sulimov.lesson10;
import java.util.*;

//Задание на уровень B

//Создайте class MyLinkedList<E>, который реализует интерфейс Deque<E>
//и работает на основе двунаправленного связного списка
//БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
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

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

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
        // возврат в виде"[data1,data2,data3,...]"
    }

    @Override
    public boolean add(E element) {
        addLast(element); // добавление в конец
        return true;
    }

    public E remove(int index) { // удаление по индексу
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node<E> nodeToRemove = getNode(index);
        return unlink(nodeToRemove);
    }

    @Override
    public boolean remove(Object element) { // удаление по значению
        Node<E> current = head;
        while (current != null) {
            if (element == null ? current.data == null : element.equals(current.data)) {
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
        if (head == null) { // если пустой
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> newNode = new Node<>(element);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
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
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }
        return head.data;
    }

    @Override
    public E getLast() {
        if (tail == null) {
            throw new NoSuchElementException("List is empty");
        }
        return tail.data;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (head == null) {
            return null;
        }
        E data = head.data;
        head = head.next; // перемещение головы вперед
        if (head != null) {
            head.prev = null; // удаление ссылки на голову
        } else {
            tail = null;
        }
        size--;
        return data;
    }

    @Override
    public E pollLast() {
        if (tail == null) {
            return null;
        }
        E data = tail.data;
        tail = tail.prev; // сдвиг хвоста назад
        if (tail != null) {
            tail.next = null;
        } else {
            head = null;
        }
        size--;
        return data;
    }

    //вспомогательные
    private Node<E> getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node<E> current;
        if (index < size / 2) { // с начала
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else { // с конца
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    private E unlink(Node<E> node) {
        E data = node.data;
        Node<E> prev = node.prev;
        Node<E> next = node.next;

        if (prev == null) {
            head = next; //удалить голову
        } else {
            prev.next = next; // пропустить узел
            node.prev = null;
        }

        if (next == null) {
            tail = prev; //
        } else {
            next.prev = prev; //
            node.next = null;
        }

        node.data = null;
        size--;
        return data;
    }

    // нереализованные
    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean offer(E e) { throw new UnsupportedOperationException(); }

    @Override
    public E remove() { throw new UnsupportedOperationException(); }

    @Override
    public E peek() { throw new UnsupportedOperationException(); }

    @Override
    public boolean offerFirst(E e) { throw new UnsupportedOperationException(); }

    @Override
    public boolean offerLast(E e) { throw new UnsupportedOperationException(); }

    @Override
    public E removeFirst() { throw new UnsupportedOperationException(); }

    @Override
    public E removeLast() { throw new UnsupportedOperationException(); }

    @Override
    public E peekFirst() { throw new UnsupportedOperationException(); }

    @Override
    public E peekLast() { throw new UnsupportedOperationException(); }

    @Override
    public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }

    @Override
    public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }

    @Override
    public boolean contains(Object o) { throw new UnsupportedOperationException(); }

    @Override
    public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }

    @Override
    public java.util.Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }

    @Override
    public Object[] toArray() { throw new UnsupportedOperationException(); }

    @Override
    public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }

    @Override
    public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }

    @Override
    public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override
    public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }

    @Override
    public void clear() { throw new UnsupportedOperationException(); }

    @Override
    public void push(E e) { throw new UnsupportedOperationException(); }

    @Override
    public E pop() { throw new UnsupportedOperationException(); }
}
