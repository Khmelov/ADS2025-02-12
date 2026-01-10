package by.it.group410902.andala.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
// тоже очень похоже на С, но тут упор на операции с концами addFirst/addLast
// Двусвязный список, реализующий интерфейс Deque (двусторонняя очередь)
public class MyLinkedList<E> implements Deque<E> {

    // Внутренний узел списка
    private class Node {
        E data;    // значение узла
        Node prev; // ссылка на предыдущий узел
        Node next; // ссылка на следующий узел

        Node(E data) {
            this.data = data;
        }
    }

    private Node head; // первая нода (голова)
    private Node tail; // последняя нода (хвост)
    private int size = 0; // текущее количество элементов

    // ---------------------- ДОБАВЛЕНИЕ ----------------------

    // Добавляет элемент в начало списка (в качестве нового head)
    @Override
    public void addFirst(E element) {
        Node newNode = new Node(element);
        if (head == null) {      // список пуст -> новый узел и head, и tail
            head = tail = newNode;
        } else {                 // вставка перед старой головой
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    // Добавляет элемент в конец списка (в качестве нового tail)
    @Override
    public void addLast(E element) {
        Node newNode = new Node(element);
        if (tail == null) {      // пустой список
            head = tail = newNode;
        } else {                 // присоединяем после текущего хвоста
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    // add по контракту Collection — добавляет в конец
    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    // ---------------------- УДАЛЕНИЕ С КОНЦОВ ----------------------

    // Удаляет и возвращает элемент с начала (как removeFirst)
    @Override
    public E remove() {
        return removeFirst();
    }

    // Удаляет и возвращает первый элемент; кидает исключение, если пусто
    @Override
    public E removeFirst() {
        if (head == null) throw new NoSuchElementException();
        E data = head.data;
        head = head.next;           // сдвигаем голову вправо
        if (head != null) head.prev = null; // обрываем ссылку на старый head
        else tail = null;           // список стал пустым
        size--;
        return data;
    }

    // Удаляет и возвращает последний элемент; кидает исключение, если пусто
    @Override
    public E removeLast() {
        if (tail == null) throw new NoSuchElementException();
        E data = tail.data;
        tail = tail.prev;           // сдвигаем хвост влево
        if (tail != null) tail.next = null;
        else head = null;           // список стал пустым
        size--;
        return data;
    }

    // Удаление по индексу (вспомогательный метод, не часть Deque)
    public E remove(int index) {
        checkIndex(index);
        Node current = getNode(index); // находим узел
        E data = current.data;
        unlink(current);               // аккуратно удаляем узел
        return data;
    }

    // Удаление первого вхождения объекта; возвращает true, если удалили
    public boolean remove(Object o) {
        Node current = head;
        while (current != null) {
            if ((o == null && current.data == null) || (o != null && o.equals(current.data))) {
                unlink(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // Убирает узел из цепочки, перенастраивая ссылки соседей
    private void unlink(Node node) {
        if (node.prev != null) node.prev.next = node.next;
        else head = node.next;       // удаляли голову

        if (node.next != null) node.next.prev = node.prev;
        else tail = node.prev;       // удаляли хвост

        size--;
    }

    // ---------------------- ПОИСК ПО ИНДЕКСУ ----------------------

    // Получить ноду по индексу; оптимизировано: от head или от tail в зависимости от положения
    private Node getNode(int index) {
        Node current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) current = current.next;
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) current = current.prev;
        }
        return current;
    }

    // Проверка индекса на корректность
    private void checkIndex(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
    }

    // ---------------------- ДОСТУП К КОНЦАМ ----------------------

    // Возвращает первый элемент без удаления; кидает исключение если пусто
    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (head == null) throw new NoSuchElementException();
        return head.data;
    }

    @Override
    public E getLast() {
        if (tail == null) throw new NoSuchElementException();
        return tail.data;
    }

    // poll / pollFirst / pollLast — безопасные версии, возвращают null если пусто
    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (head == null) return null;
        return removeFirst();
    }

    @Override
    public E pollLast() {
        if (tail == null) return null;
        return removeLast();
    }

    // ---------------------- УТИЛИТЫ ----------------------

    @Override
    public int size() {
        return size;
    }

    // Строковое представление списка: [a, b, c]
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) sb.append(", ");
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    // ---------------------- НЕ РЕАЛИЗОВАНЫ (заглушки) ----------------------


    @Override public boolean offerFirst(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean offerLast(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { throw new UnsupportedOperationException(); }
    @Override public E peek() { throw new UnsupportedOperationException(); }
    @Override public E peekFirst() { throw new UnsupportedOperationException(); }
    @Override public E peekLast() { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    @Override public void push(E e) { throw new UnsupportedOperationException(); }
    @Override public E pop() { throw new UnsupportedOperationException(); }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public void clear() { throw new UnsupportedOperationException(); }
    @Override public boolean isEmpty() { return size == 0; } // реализовано
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }

}
