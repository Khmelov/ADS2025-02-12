package by.it.group410902.derzhavskaya_ludmila.lesson10;

import java.util.Deque;
import java.util.NoSuchElementException;

// двунаправленный связный список
public class MyLinkedList<E> implements Deque<E> {

    // Внутренний класс для представления узла списка
    private static class Node<E> {
        E data;           // Данные узла
        Node<E> next;     // Ссылка на следующий узел
        Node<E> prev;     // Ссылка на предыдущий узел

        // Конструктор узла
        Node(E data, Node<E> prev, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> head;    // Первый узел списка
    private Node<E> tail;    // Последний узел списка
    private int size;        // Количество элементов в списке

    // Конструктор пустого списка
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
    }

    // Добавляет элемент в конец списка
    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    // Удаляет элемент по индексу и возвращает удаленный элемент
    public E remove(int index) {
        // Проверяем корректность индекса
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        // Если удаляем первый элемент
        if (index == 0) {
            E removedData = head.data;
            head = head.next;
            if (head != null) {
                head.prev = null;
            } else {
                tail = null; // Если список стал пустым
            }
            size--;
            return removedData;
        }
        // Если удаляем последний элемент
        else if (index == size - 1) {
            E removedData = tail.data;
            tail = tail.prev;
            if (tail != null) {
                tail.next = null;
            } else {
                head = null; // Если список стал пустым
            }
            size--;
            return removedData;
        } else {
            // Находим узел для удаления
            Node<E> nodeToRemove = getNode(index);
            E removedData = nodeToRemove.data;

            // Перестраиваем связи
            nodeToRemove.prev.next = nodeToRemove.next;
            nodeToRemove.next.prev = nodeToRemove.prev;

            size--;
            return removedData;
        }
    }

    // Удаляет первое вхождение указанного элемента
    @Override
    public boolean remove(Object element) {
        Node<E> current = head;
        int index = 0;

        // Ищем элемент в списке
        while (current != null) {
            if (element.equals(current.data)) {
                remove(index); // Удаляем по найденному индексу
                return true;
            }
            current = current.next;
            index++;
        }
        return false; // Элемент не найден
    }

    // Возвращает количество элементов в списке
    @Override
    public int size() {
        return size;
    }

    // Добавляет элемент в начало списка
    @Override
    public void addFirst(E element) {
        Node<E> newNode = new Node<>(element, null, head);

        if (head != null) {
            head.prev = newNode;
        } else {
            // Если список был пуст, новый узел становится и хвостом
            tail = newNode;
        }
        head = newNode;
        size++;
    }

    // Добавляет элемент в конец списка
    @Override
    public void addLast(E element) {
        Node<E> newNode = new Node<>(element, tail, null);

        if (tail != null) {
            tail.next = newNode;
        } else {
            // Если список был пуст, новый узел становится и головой
            head = newNode;
        }
        tail = newNode;
        size++;
    }

    // Возвращает первый элемент без удаления (аналогично getFirst)
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

    // Удаляет и возвращает первый элемент (или null если список пуст)
    @Override
    public E poll() {
        return pollFirst();
    }

    // Удаляет и возвращает первый элемент (или null если список пуст)
    @Override
    public E pollFirst() {
        if (head == null) {
            return null;
        }
        E removedData = head.data;
        head = head.next;
        if (head != null) {
            head.prev = null;
        } else {
            tail = null; // Если список стал пустым
        }
        size--;
        return removedData;
    }

    // Удаляет и возвращает последний элемент (или null если список пуст)
    @Override
    public E pollLast() {
        if (tail == null) {
            return null;
        }
        E removedData = tail.data;
        tail = tail.prev;
        if (tail != null) {
            tail.next = null;
        } else {
            head = null; // Если список стал пустым
        }
        size--;
        return removedData;
    }

    /////////////////////////////////////////////////////////////////////////
    //////       Вспомогательные методы для реализации списка       ///////
    /////////////////////////////////////////////////////////////////////////


    // Возвращает узел по индексу
    private Node<E> getNode(int index) {
        // Проверяем корректность индекса
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node<E> current;
        if (index < size / 2) {
            // Ищем с начала если индекс в первой половине
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            // Ищем с конца если индекс во второй половине
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }



    //////   Остальные методы интерфейса Deque тоже должны быть реализованы

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
        return removeFirst();
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
        return removeLast();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public E peekFirst() {
        return head == null ? null : head.data;
    }

    @Override
    public E peekLast() {
        return tail == null ? null : tail.data;
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    // Нереализованные методы (по условию задания не обязательны)
    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public java.util.Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not implemented");
    }
}