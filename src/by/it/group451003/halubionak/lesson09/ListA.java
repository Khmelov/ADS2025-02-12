package by.it.group451003.halubionak.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {

    // Внутренний класс для узла списка
    private class Node {
        E data;
        Node next;

        Node(E data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;    // Первый элемент списка
    private int size;     // Размер списка

    // Конструктор
    public ListA() {
        head = null;
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node current = head;
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
    public boolean add(E e) {
        Node newNode = new Node(e);

        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }

        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        E removedData;

        if (index == 0) {
            // Удаляем первый элемент
            removedData = head.data;
            head = head.next;
        } else {
            // Ищем элемент перед удаляемым
            Node previous = head;
            for (int i = 0; i < index - 1; i++) {
                previous = previous.next;
            }

            removedData = previous.next.data;
            previous.next = previous.next.next;
        }

        size--;
        return removedData;
    }

    @Override
    public int size() {
        return size;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node newNode = new Node(element);

        if (index == 0) {
            // Вставка в начало
            newNode.next = head;
            head = newNode;
        } else {
            // Ищем элемент перед позицией вставки
            Node previous = head;
            for (int i = 0; i < index - 1; i++) {
                previous = previous.next;
            }

            newNode.next = previous.next;
            previous.next = newNode;
        }

        size++;
    }

    @Override
    public boolean remove(Object o) {
        if (head == null) {
            return false;
        }

        // Если удаляем первый элемент
        if (o == null ? head.data == null : o.equals(head.data)) {
            head = head.next;
            size--;
            return true;
        }

        // Ищем элемент для удаления
        Node current = head;
        while (current.next != null) {
            if (o == null ? current.next.data == null : o.equals(current.next.data)) {
                current.next = current.next.next;
                size--;
                return true;
            }
            current = current.next;
        }

        return false;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        E oldData = current.data;
        current.data = element;
        return oldData;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        Node current = head;
        int index = 0;

        while (current != null) {
            if (o == null ? current.data == null : o.equals(current.data)) {
                return index;
            }
            current = current.next;
            index++;
        }

        return -1;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current.data;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        Node current = head;
        int index = 0;
        int lastIndex = -1;

        while (current != null) {
            if (o == null ? current.data == null : o.equals(current.data)) {
                lastIndex = index;
            }
            current = current.next;
            index++;
        }

        return lastIndex;
    }

    // Остальные методы оставлены без реализации, так как они опциональны
    // и для базовой функциональности списка не требуются

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
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
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                E data = current.data;
                current = current.next;
                return data;
            }
        };
    }
}