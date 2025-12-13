package by.it.group410901.korneew.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

// Двусвязный список, реализующий Deque<E>
public class MyLinkedList<E> implements Deque<E> {

    // Узел списка
    class MyLinkedListNode<E> {
        public E Data;                       // значение узла
        public MyLinkedListNode<E> Previous; // предыдущий узел
        public MyLinkedListNode<E> Next;     // следующий узел

        public MyLinkedListNode(E data) {
            Data = data;
        }
    }

    // Голова и хвост списка
    MyLinkedListNode<E> _head;
    MyLinkedListNode<E> _tail;
    // Число элементов
    int _size;

    // Пустой список
    MyLinkedList() {
        _head = null;
        _tail = null;
        _size = 0;
    }

    // Форматированный вывод вида [a, b, c]
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        MyLinkedListNode<E> temp = _head;
        for (int i = 0; i < _size; i++) {
            sb.append(temp.Data);
            if (i < _size - 1) sb.append(", ");
            temp = temp.Next;
        }
        sb.append(']');
        return sb.toString();
    }

    // Добавление в конец (возвращает true)
    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    // Удаление по индексу, возвращает удалённое значение или null
    public E remove(int index) {
        if (index < 0 || index >= _size) return null;

        MyLinkedListNode<E> temp = _head;
        for (int i = 0; i < index; i++) temp = temp.Next;

        E e = temp.Data;

        if (temp.Previous != null) temp.Previous.Next = temp.Next;
        else _head = temp.Next;

        if (temp.Next != null) temp.Next.Previous = temp.Previous;
        else _tail = temp.Previous;

        _size--;
        return e;
    }

    // Удаляет первое встретившееся значение, возвращает true если удалено
    @Override
    public boolean remove(Object o) {
        MyLinkedListNode<E> temp = _head;
        int index = 0;
        while (temp != null) {
            if (temp.Data.equals(o)) {
                remove(index);
                return true;
            }
            index++;
            temp = temp.Next;
        }
        return false;
    }

    // Текущее количество элементов
    @Override
    public int size() {
        return _size;
    }

    // Вставка в начало
    @Override
    public void addFirst(E e) {
        MyLinkedListNode<E> node = new MyLinkedListNode<>(e);
        if (_head != null) {
            node.Next = _head;
            _head.Previous = node;
        }
        _head = node;
        if (_tail == null) _tail = node;
        _size++;
    }

    // Вставка в конец
    @Override
    public void addLast(E e) {
        MyLinkedListNode<E> node = new MyLinkedListNode<>(e);
        if (_tail != null) {
            _tail.Next = node;
            node.Previous = _tail;
        }
        _tail = node;
        if (_head == null) _head = node;
        _size++;
    }

    // Возвращает первый элемент (null если пусто)
    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (_size == 0) return null;
        return _head.Data;
    }

    @Override
    public E getLast() {
        if (_size == 0) return null;
        return _tail.Data;
    }

    // Удаление и возврат первой записи (или null)
    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (_size == 0) return null;

        E e = _head.Data;
        _head = _head.Next;

        if (_head != null) _head.Previous = null;
        else _tail = null;

        _size--;
        return e;
    }

    // Удаление и возврат последней записи (или null)
    @Override
    public E pollLast() {
        if (_size == 0) return null;

        E e = _tail.Data;
        _tail = _tail.Previous;

        if (_tail != null) _tail.Next = null;
        else _head = null;

        _size--;
        return e;
    }

    // ----------------- заглушки (не реализованы) -----------------

    @Override
    public boolean offerFirst(E e) { return false; }
    @Override
    public boolean offerLast(E e) { return false; }
    @Override
    public E removeFirst() { return null; }
    @Override
    public E removeLast() { return null; }
    @Override
    public E peekFirst() { return null; }
    @Override
    public E peekLast() { return null; }
    @Override
    public boolean removeFirstOccurrence(Object o) { return false; }
    @Override
    public boolean removeLastOccurrence(Object o) { return false; }
    @Override
    public boolean offer(E e) { return false; }
    @Override
    public E remove() { return null; }
    @Override
    public E peek() { return null; }
    @Override
    public boolean addAll(Collection<? extends E> c) { return false; }
    @Override
    public boolean removeAll(Collection<?> c) { return false; }
    @Override
    public boolean retainAll(Collection<?> c) { return false; }
    @Override
    public void clear() { }
    @Override
    public void push(E e) { }
    @Override
    public E pop() { return null; }
    @Override
    public boolean containsAll(Collection<?> c) { return false; }
    @Override
    public boolean contains(Object o) { return false; }
    @Override
    public boolean isEmpty() { return false; }
    @Override
    public Iterator<E> iterator() { return null; }
    @Override
    public Object[] toArray() { return new Object[0]; }
    @Override
    public <T> T[] toArray(T[] a) { return null; }
    @Override
    public Iterator<E> descendingIterator() { return null; }
}