package by.it.group410901.evtuhovskaya.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

//двусвязный список

public class MyLinkedList<E> implements Deque<E> {
                             //реализовать методы списка
    
    private static class Node<E> { //вложенный класс структуры узла
        E item; //значение узла

        //ссылки
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    //первый, последний элементы, количество
    private Node<E> head;
    private Node<E> tail;
    private int size;

    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head; //начиная с головы
        while (current != null) { //пока не закончатся элементы
            sb.append(current.item); //записывать значение
            if (current.next != null) sb.append(", "); //через запятую
            current = current.next; //и двигаться дальше с голоы к концу
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void addFirst(E e) {
        Node<E> newNode = new Node<>(null, e, head); //новый первый узел
        if (head == null) {
            tail = newNode; //если списка не было, то он и последний
        } else {
            head.prev = newNode; //если список был, то связываем новую голову состарой
        }
        head = newNode; //обновляем ссылку головы
        size++;
    }

    @Override
    public void addLast(E e) {
        Node<E> newNode = new Node<>(tail, e, null); //новый последний узел
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode; //связываем старый хвост с новым
        }
        tail = newNode; //обновляем ссылку на хвост
        size++;
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return head.item;
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return head.item;
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return tail.item;
    }

    @Override
    public E poll() { // вызов метода, удаляющего первый элемент
        return pollFirst();  //если очередь пуста, то нулл, а не исключение
    }

    @Override
    public E pollFirst() { //удалить и вернуть первый элемент
        if (size == 0) return null;
        E value = head.item; //сохраняем удаляемое значение
        head = head.next; //определяем новую голову
        if (head != null) head.prev = null; //обнуляем предшествующее значение
        else tail = null;
        size--;
        return value;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        E value = tail.item;
        tail = tail.prev;
        if (tail != null) tail.next = null;
        else head = null;
        size--;
        return value;
    }

    @Override
    public E remove() {
        return pollFirst();
    }

    //удаление по индексу
    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException(); //проверка местонахождения
        Node<E> current;

        //откуда легче идти
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) current = current.next; //проход с головы
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) current = current.prev;
        }
        E value = current.item; //сохраняем значение
        unlink(current); //убираем связь со списком
        return value;
    }

    //удаление по значению
    @Override
    public boolean remove(Object o) {
        Node<E> current = head; //проход с головы
        while (current != null) { //и до конца
            if ((o == null && current.item == null) || (o != null && o.equals(current.item))) { //если нашли совпадение
                unlink(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) { //проверка содержания коллекции
        return false;
    }

    private void unlink(Node<E> node) { //пересвязывание
        Node<E> prev = node.prev;
        Node<E> next = node.next;

        if (prev == null) head = next; //для первого
        else prev.next = next; //

        if (next == null) tail = prev; //для последнего
        else next.prev = prev;

        size--;
    }

    /////////////////////////////////////////////////////////////////////////
    //////              Опциональные к реализации методы              ///////
    /////////////////////////////////////////////////////////////////////////

    @Override public boolean offerFirst(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean offerLast(E e) { throw new UnsupportedOperationException(); }
    @Override public E removeFirst() { throw new UnsupportedOperationException(); }
    @Override public E removeLast() { throw new UnsupportedOperationException(); }
    @Override public E peek() { throw new UnsupportedOperationException(); }
    @Override public E peekFirst() { throw new UnsupportedOperationException(); }
    @Override public E peekLast() { throw new UnsupportedOperationException(); }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) {return null;}
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) {return false;}
    @Override public boolean retainAll(Collection<?> c) {return false;}
    @Override public void push(E e) {}
    @Override public E pop() {return null;}
    @Override public void clear() { throw new UnsupportedOperationException(); }
}