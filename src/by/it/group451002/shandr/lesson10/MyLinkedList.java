package by.it.group451002.shandr.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    // Внутренний класс для узлов списка
    private static class Node<E> {
        E data;        // Данные, хранящиеся в узле
        Node<E> next;  // Ссылка на следующий узел
        Node<E> prev;  // Ссылка на предыдущий узел

        // Конструктор для создания узла с данными
        Node(E data) {
            this.data = data;
        }

        // Конструктор для создания узла с данными и ссылками
        Node(E data, Node<E> prev, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> head; // Первый элемент списка
    private Node<E> tail; // Последний элемент списка
    private int size;     // Количество элементов в списке

    // Конструктор по умолчанию
    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Возвращает строковое представление списка
     * @return строка в формате [element1, element2, ...]
     */
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;

        // Проходим по всем элементам списка
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

    /**
     * Добавляет элемент в конец списка
     * @param element элемент для добавления
     * @return true если элемент успешно добавлен
     */
    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    /**
     * Удаляет элемент по указанному индексу
     * @param index индекс удаляемого элемента
     * @return данные удаленного элемента
     * @throws IndexOutOfBoundsException если индекс выходит за границы
     */
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node<E> nodeToRemove = getNode(index);
        return unlink(nodeToRemove);
    }

    /**
     * Удаляет первое вхождение указанного элемента
     * @param element элемент для удаления
     * @return true если элемент был найден и удален
     */
    @Override
    public boolean remove(Object element) {
        Node<E> current = head;

        // Поиск элемента в списке
        while (current != null) {
            if (element == null ? current.data == null : element.equals(current.data)) {
                unlink(current);
                return true;
            }
            current = current.next;
        }

        return false;
    }

    /**
     * Возвращает количество элементов в списке
     * @return размер списка
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Добавляет элемент в начало списка
     * @param element элемент для добавления
     */
    @Override
    public void addFirst(E element) {
        Node<E> newNode = new Node<>(element);

        if (head == null) {
            // Если список пуст, новый элемент становится и головой и хвостом
            head = tail = newNode;
        } else {
            // Добавляем новый элемент перед текущей головой
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }

        size++;
    }

    /**
     * Добавляет элемент в конец списка
     * @param element элемент для добавления
     */
    @Override
    public void addLast(E element) {
        Node<E> newNode = new Node<>(element);

        if (tail == null) {
            // Если список пуст, новый элемент становится и головой и хвостом
            head = tail = newNode;
        } else {
            // Добавляем новый элемент после текущего хвоста
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }

        size++;
    }

    /**
     * Возвращает первый элемент списка без удаления
     * @return первый элемент списка
     * @throws NoSuchElementException если список пуст
     */
    @Override
    public E element() {
        return getFirst();
    }

    /**
     * Возвращает первый элемент списка без удаления
     * @return первый элемент списка
     * @throws NoSuchElementException если список пуст
     */
    @Override
    public E getFirst() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        return head.data;
    }

    /**
     * Возвращает последний элемент списка без удаления
     * @return последний элемент списка
     * @throws NoSuchElementException если список пуст
     */
    @Override
    public E getLast() {
        if (tail == null) {
            throw new NoSuchElementException();
        }
        return tail.data;
    }

    /**
     * Удаляет и возвращает первый элемент списка
     * @return первый элемент списка или null если список пуст
     */
    @Override
    public E poll() {
        return pollFirst();
    }

    /**
     * Удаляет и возвращает первый элемент списка
     * @return первый элемент списка или null если список пуст
     */
    @Override
    public E pollFirst() {
        if (head == null) {
            return null;
        }

        E data = head.data;
        head = head.next;

        if (head != null) {
            head.prev = null;
        } else {
            // Если список стал пустым, обнуляем хвост
            tail = null;
        }

        size--;
        return data;
    }

    /**
     * Удаляет и возвращает последний элемент списка
     * @return последний элемент списка или null если список пуст
     */
    @Override
    public E pollLast() {
        if (tail == null) {
            return null;
        }

        E data = tail.data;
        tail = tail.prev;

        if (tail != null) {
            tail.next = null;
        } else {
            // Если список стал пустым, обнуляем голову
            head = null;
        }

        size--;
        return data;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы                       ///////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Возвращает узел по указанному индексу
     * @param index индекс узла
     * @return узел по указанному индексу
     * @throws IndexOutOfBoundsException если индекс выходит за границы
     */
    private Node<E> getNode(int index) {
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

    /**
     * Удаляет узел из списка и возвращает его данные
     * @param node узел для удаления
     * @return данные удаленного узла
     */
    private E unlink(Node<E> node) {
        E data = node.data;
        Node<E> prev = node.prev;
        Node<E> next = node.next;

        // Обновляем ссылки соседних узлов
        if (prev == null) {
            // Удаляем голову
            head = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            // Удаляем хвост
            tail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        // Очищаем данные и уменьшаем размер
        node.data = null;
        size--;
        return data;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы Deque (заглушки)           ///////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Проверяет, пуст ли список
     * @return true если список пуст
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Добавляет элемент в начало списка
     * @param e элемент для добавления
     * @return true если элемент успешно добавлен
     */
    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    /**
     * Добавляет элемент в конец списка
     * @param e элемент для добавления
     * @return true если элемент успешно добавлен
     */
    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    /**
     * Удаляет и возвращает первый элемент списка
     * @return первый элемент списка
     * @throws NoSuchElementException если список пуст
     */
    @Override
    public E removeFirst() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        return pollFirst();
    }

    /**
     * Удаляет и возвращает последний элемент списка
     * @return последний элемент списка
     * @throws NoSuchElementException если список пуст
     */
    @Override
    public E removeLast() {
        if (tail == null) {
            throw new NoSuchElementException();
        }
        return pollLast();
    }

    /**
     * Возвращает первый элемент списка без удаления
     * @return первый элемент списка или null если список пуст
     */
    @Override
    public E peek() {
        return peekFirst();
    }

    /**
     * Возвращает первый элемент списка без удаления
     * @return первый элемент списка или null если список пуст
     */
    @Override
    public E peekFirst() {
        return head == null ? null : head.data;
    }

    /**
     * Возвращает последний элемент списка без удаления
     * @return последний элемент списка или null если список пуст
     */
    @Override
    public E peekLast() {
        return tail == null ? null : tail.data;
    }

    /**
     * Удаляет первое вхождение указанного элемента
     * @param o элемент для удаления
     * @return true если элемент был найден и удален
     */
    @Override
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    /**
     * Удаляет последнее вхождение указанного элемента
     * @param o элемент для удаления
     * @return true если элемент был найден и удален
     */
    @Override
    public boolean removeLastOccurrence(Object o) {
        Node<E> current = tail;

        // Поиск с конца списка
        while (current != null) {
            if (o == null ? current.data == null : o.equals(current.data)) {
                unlink(current);
                return true;
            }
            current = current.prev;
        }

        return false;
    }

    /**
     * Добавляет элемент в конец списка
     * @param e элемент для добавления
     * @return true если элемент успешно добавлен
     */
    @Override
    public boolean offer(E e) {
        return add(e);
    }

    /**
     * Удаляет и возвращает первый элемент списка
     * @return первый элемент списка
     * @throws NoSuchElementException если список пуст
     */
    @Override
    public E remove() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        return pollFirst();
    }

    /**
     * Добавляет элемент в начало списка (аналогично addFirst)
     * @param e элемент для добавления
     */
    @Override
    public void push(E e) {
        addFirst(e);
    }

    /**
     * Удаляет и возвращает первый элемент списка (аналогично removeFirst)
     * @return первый элемент списка
     */
    @Override
    public E pop() {
        return removeFirst();
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Нереализованные методы                       ///////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Возвращает итератор для обхода списка
     * @return итератор
     * @throws UnsupportedOperationException метод не реализован
     */
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    /**
     * Возвращает итератор для обратного обхода списка
     * @return итератор для обратного обхода
     * @throws UnsupportedOperationException метод не реализован
     */
    @Override
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException();
    }

    /**
     * Проверяет наличие элемента в списке
     * @param o элемент для поиска
     * @return true если элемент найден
     * @throws UnsupportedOperationException метод не реализован
     */
    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * Преобразует список в массив объектов
     * @return массив элементов списка
     * @throws UnsupportedOperationException метод не реализован
     */
    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    /**
     * Преобразует список в массив указанного типа
     * @param a массив для заполнения
     * @return массив элементов списка
     * @throws UnsupportedOperationException метод не реализован
     */
    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    /**
     * Проверяет наличие всех элементов коллекции в списке
     * @param c коллекция для проверки
     * @return true если все элементы присутствуют
     * @throws UnsupportedOperationException метод не реализован
     */
    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * Добавляет все элементы коллекции в список
     * @param c коллекция для добавления
     * @return true если список изменился
     * @throws UnsupportedOperationException метод не реализован
     */
    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * Удаляет все элементы, присутствующие в указанной коллекции
     * @param c коллекция элементов для удаления
     * @return true если список изменился
     * @throws UnsupportedOperationException метод не реализован
     */
    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * Сохраняет только элементы, присутствующие в указанной коллекции
     * @param c коллекция элементов для сохранения
     * @return true если список изменился
     * @throws UnsupportedOperationException метод не реализован
     */
    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * Очищает список, удаляя все элементы
     * @throws UnsupportedOperationException метод не реализован
     */
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}