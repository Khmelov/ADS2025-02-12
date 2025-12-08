package by.it.group451001.kazakov.lesson10;

import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.Collection;
import java.util.Iterator;

public class MyLinkedList<E> implements Deque<E> {

    // Внутренний класс узла двусвязного списка
    private static class Node<E> {
        E v;           // Данные узла
        Node<E> prev;  // Ссылка на предыдущий узел
        Node<E> next;  // Ссылка на следующий узел

        Node(E v) {
            this.v = v;
        }
    }

    private Node<E> head, tail;  // Голова и хвост списка
    private int size;            // Количество элементов

    public MyLinkedList() {
    }

    // Строковое представление списка
    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node<E> n = head;
        while (n != null) {
            sb.append(String.valueOf(n.v));  // Добавляем значение узла
            n = n.next;
            if (n != null) sb.append(", ");  // Добавляем запятую между элементами
        }
        sb.append(']');
        return sb.toString();
    }

    // Добавление элемента в конец списка
    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    // Удаление элемента по индексу
    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<E> cur;
        // Оптимизация: выбираем направление обхода от ближайшего конца
        if (index < (size >> 1)) {  // Если индекс в первой половине
            cur = head;
            for (int i = 0; i < index; i++) cur = cur.next;
        } else {  // Если индекс во второй половине
            cur = tail;
            for (int i = size - 1; i > index; i--) cur = cur.prev;
        }
        E val = cur.v;
        unlink(cur);  // Удаляем узел
        return val;
    }

    // Удаление первого вхождения объекта
    @Override
    public boolean remove(Object o) {
        Node<E> n = head;
        while (n != null) {
            // Сравнение с учетом null-значений
            if (o == null ? n.v == null : o.equals(n.v)) {
                unlink(n);  // Удаляем найденный узел
                return true;
            }
            n = n.next;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    // Добавление элемента в начало списка
    @Override
    public void addFirst(E element) {
        if (element == null) throw new NullPointerException();
        Node<E> n = new Node<>(element);
        if (size == 0) {
            head = tail = n;  // Если список пуст, оба указателя на новый узел
        } else {
            n.next = head;    // Новый узел указывает на текущую голову
            head.prev = n;    // Текущая голова указывает назад на новый узел
            head = n;         // Обновляем голову
        }
        size++;
    }

    // Добавление элемента в конец списка
    @Override
    public void addLast(E element) {
        if (element == null) throw new NullPointerException();
        Node<E> n = new Node<>(element);
        if (size == 0) {
            head = tail = n;  // Если список пуст, оба указателя на новый узел
        } else {
            tail.next = n;    // Текущий хвост указывает на новый узел
            n.prev = tail;    // Новый узел указывает назад на текущий хвост
            tail = n;         // Обновляем хвост
        }
        size++;
    }

    // Получение первого элемента (аналог getFirst)
    @Override
    public E element() {
        return getFirst();
    }

    // Получение первого элемента с проверкой на пустоту
    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return head.v;
    }

    // Получение последнего элемента с проверкой на пустоту
    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return tail.v;
    }

    // Удаление и возврат первого элемента (аналог pollFirst)
    @Override
    public E poll() {
        return pollFirst();
    }

    // Удаление и возврат первого элемента (без исключения)
    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E v = head.v;
        unlink(head);  // Удаляем головной узел
        return v;
    }

    // Удаление и возврат последнего элемента (без исключения)
    @Override
    public E pollLast() {
        if (size == 0) return null;
        E v = tail.v;
        unlink(tail);  // Удаляем хвостовой узел
        return v;
    }

    // Внутренний метод удаления узла из списка
    private void unlink(Node<E> n) {
        Node<E> p = n.prev;  // Предыдущий узел
        Node<E> q = n.next;  // Следующий узел

        // Обновляем ссылки соседних узлов
        if (p == null) head = q;    // Если удаляем голову
        else p.next = q;

        if (q == null) tail = p;    // Если удаляем хвост
        else q.prev = p;

        // Очищаем ссылки удаляемого узла
        n.prev = n.next = null;
        n.v = null;  // Помогаем сборщику мусора

        size--;
        if (size == 0) {
            head = tail = null;  // Если список стал пустым
        }
    }

    // Не реализованные методы (бросят UnsupportedOperationException)
    @Override
    public boolean offerFirst(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean offerLast(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean offer(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E peek() {
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

    // Удаление и возврат первого элемента (с исключением для пустого списка)
    @Override
    public E remove() {
        E r = pollFirst();
        if (r == null) throw new NoSuchElementException();
        return r;
    }

    @Override
    public E removeFirst() {
        return remove();
    }

    // Удаление и возврат последнего элемента (с исключением для пустого списка)
    @Override
    public E removeLast() {
        E r = pollLast();
        if (r == null) throw new NoSuchElementException();
        return r;
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
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException();
    }

    // Проверка на пустоту списка
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Очистка списка
    @Override
    public void clear() {
        head = tail = null;
        size = 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
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