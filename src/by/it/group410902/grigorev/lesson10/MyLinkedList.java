package by.it.group410902.grigorev.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.Objects;

public class MyLinkedList<E> implements Deque<E> {
    static class DoubleNode<E> {
        public E data;
        public DoubleNode<E> next;
        public DoubleNode<E> prev;
        public DoubleNode(E data) {
            this.data = data;
        }
    }
    DoubleNode<E> head;  // первый элемент списка
    DoubleNode<E> tail; // последний элемент списка
    int Count; // текущее количество элементов в списке

    @Override
    // возвращает строковое представление списка, перечисляя элементы через запятую в квадратных скобках
    public String toString() {
        String tempStr = "[";
        DoubleNode<E> tempHead = head;
        while (tempHead.next != null) {
            tempStr += tempHead.data;
            tempStr += ", ";
            tempHead = tempHead.next;
        }
        tempStr += tempHead.data;
        tempStr += "]";
        return tempStr;
    }

    @Override
    // добавляет элемент в конец списка. Создает новый узел, добавляет его в конец.
    public boolean add(E e) {
        DoubleNode<E> node = new DoubleNode<>(e);

        if (head == null)
            head = node;
        else
        {
            tail.next = node;
            node.prev = tail;
        }
        tail = node;
        Count++;
        return true;
    }

    @Override
    // добавляет элемент в начало списка.
    public void addFirst(E e) {
        DoubleNode<E> node = new DoubleNode<>(e);
        node.next = head;
        if (Count == 0) {
            head = tail = node;
        }
        else {
            head.prev = node;
            head = node;
        }
        Count++;
    }

    @Override
    // добавляет элемент в конец через вызов метода add
    public void addLast(E e) {
        add(e);
    }

    @Override
    // удаляет и возвращает первый элемент списка.
    public E pollFirst() {
        if (Count == 0)
            return null;
        E output = head.data;
        if (Count == 1)
            head = tail = null;
        else
        {
            head = head.next;
            head.prev = null;
        }
        Count--;
        return output;
    }

    @Override
    // удаляет и возвращает последний элемент (tail)
    public E pollLast() {
        if (Count == 0)
            return null;
        E output = tail.data;
        if (Count == 1)
            head = tail = null;
        else
        {
            tail = tail.prev;
            tail.next = null;
        }
        Count--;
        return output;
    }

    @Override
    // удаляет и возвращает первый элемент списка, эквивалентно pollFirst
    public E poll() {
        return pollFirst();
    }

    @Override
    // возвращает первый элемент (head.data), или null если список пуст
    public E getFirst() {
        if (Count == 0)
            return null;
        return head.data;
    }

    @Override
    // возвращает последний элемент (tail.data), или null если список пуст
    public E getLast() {
        if (Count == 0)
            return null;
        return tail.data;
    }
    @Override
    // возвращает первый элемент списка, аналогично getFirst
    public E element() {
        return getFirst();
    }

    @Override
    // возвращает количество элементов (Count)
    public int size() {
        return Count;
    }

    @Override
    // находит и удаляет первый элемент, равный o.
    public boolean remove(Object o) {
        DoubleNode<E> tempHead = head;
        while (tempHead != null) {
            if (Objects.equals(o, tempHead.data)) {
                if (tempHead.next != null)
                    tempHead.next.prev = tempHead.prev;
                else
                    tail = tempHead.prev;

                if (tempHead.prev != null)
                    tempHead.prev.next = tempHead.next;
                else
                    head = tempHead.next;
                Count--;
                return true;
            }
            tempHead = tempHead.next;
        }
        return false;
    }

    // удаляет элемент по индексу, если он в пределах списка.
    public E remove(int index) {
        DoubleNode<E> tempHead = head;
        if (index > -1 && index < Count) {
            int count = 0;
            while (count++ < index) {
                tempHead = tempHead.next;
            }
            if (tempHead.next != null)
                tempHead.next.prev = tempHead.prev;
            else
                tail = tempHead.prev;

            if (tempHead.prev != null)
                tempHead.prev.next = tempHead.next;
            else
                head = tempHead.next;
            Count--;
            return tempHead.data;
        }
        return null;
    }

    //////////////////////////////////////////

    @Override

    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
        return null;
    }

    @Override
    public E peek() {
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
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Iterator<E> descendingIterator() {
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
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public void push(E e) {

    }

    @Override
    public E pop() {
        return null;
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
}
