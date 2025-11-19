package by.it.group410902.jalilova.lesson10;

import java.util.Deque;
import java.util.NoSuchElementException;

//реализация двунаправленной очереди на двусвязном списке
public class MyLinkedList<E> implements Deque<E> {

    //внутренний класс узла списка
    private static class Node<E> {
        E data;        //данные узла
        Node<E> prev;  //ссылка на предыдущий узел
        Node<E> next;  //ссылка на следующий узел

        Node(E data) {
            this.data = data;
        }
    }

    private Node<E> first; //первый элемент списка
    private Node<E> last;  //последний элемент списка
    private int size;      //количество элементов

    public MyLinkedList() {
        first = null;
        last = null;
        size = 0;
    }

    //строковое представление в формате [a, b, c]
    @Override
    public String toString() {
        if (size == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        Node<E> current = first;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) sb.append(", ");
            current = current.next;
        }
        return sb.append("]").toString();
    }

    //добавление в конец
    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    //удаление по индексу
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        //поиск узла по индексу
        Node<E> current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        E removedData = current.data;

        //перелинковка узлов
        if (current.prev == null) {
            first = current.next; // удаляем первый элемент
        } else {
            current.prev.next = current.next;
        }

        if (current.next == null) {
            last = current.prev; //удаляем последний элемент
        } else {
            current.next.prev = current.prev;
        }

        size--;
        return removedData;
    }

    //удаление по значению
    public boolean remove(Object element) {
        Node<E> current = first;
        while (current != null) {
            if ((element == null && current.data == null) ||
                    (element != null && element.equals(current.data))) {

                //перелинковка узлов
                if (current.prev == null) {
                    first = current.next;
                } else {
                    current.prev.next = current.next;
                }

                if (current.next == null) {
                    last = current.prev;
                } else {
                    current.next.prev = current.prev;
                }

                size--;
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

    //добавление в начало списка
    @Override
    public void addFirst(E element) {
        Node<E> newNode = new Node<>(element);

        if (first == null) {
            first = newNode;
            last = newNode;
        } else {
            newNode.next = first;
            first.prev = newNode;
            first = newNode;
        }
        size++;
    }

    //добавление в конец списка
    @Override
    public void addLast(E element) {
        Node<E> newNode = new Node<>(element);

        if (last == null) {
            first = newNode;
            last = newNode;
        } else {
            newNode.prev = last;
            last.next = newNode;
            last = newNode;
        }
        size++;
    }

    //получение первого элемента без удаления
    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (first == null) throw new NoSuchElementException("List is empty");
        return first.data;
    }

    @Override
    public E getLast() {
        if (last == null) throw new NoSuchElementException("List is empty");
        return last.data;
    }

    //удаление и возврат первого элемента
    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (first == null) return null;

        E data = first.data;
        first = first.next;
        if (first == null) {
            last = null; //список стал пустым
        } else {
            first.prev = null; //убираем ссылку на удаленный элемент
        }
        size--;
        return data;
    }

    //удаление и возврат последнего элемента
    @Override
    public E pollLast() {
        if (last == null) return null;

        E data = last.data;
        last = last.prev;
        if (last == null) {
            first = null; //список стал пустым
        } else {
            last.next = null; //убираем ссылку на удаленный элемент
        }
        size--;
        return data;
    }

    //не реализованные методы интерфейса Deque
    @Override public boolean offerFirst(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean offerLast(E e) { throw new UnsupportedOperationException(); }
    @Override public E removeFirst() { throw new UnsupportedOperationException(); }
    @Override public E removeLast() { throw new UnsupportedOperationException(); }
    @Override public E peekFirst() { throw new UnsupportedOperationException(); }
    @Override public E peekLast() { throw new UnsupportedOperationException(); }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { throw new UnsupportedOperationException(); }
    @Override public E remove() { throw new UnsupportedOperationException(); }
    @Override public E peek() { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public void push(E e) { throw new UnsupportedOperationException(); }
    @Override public E pop() { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    @Override public void clear() { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean isEmpty() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}