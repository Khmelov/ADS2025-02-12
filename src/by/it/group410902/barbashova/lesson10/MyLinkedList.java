package by.it.group410902.barbashova.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyLinkedList<E> implements Deque<E> {
    // Внутренний класс для узла связного списка
    class MyLinkedListNode<E> {
        public E Data;                    // Данные узла
        public MyLinkedListNode<E> Previous; // Ссылка на предыдущий узел
        public MyLinkedListNode<E> Next;     // Ссылка на следующий узел

        public MyLinkedListNode(E data) {
            Data = data;
        }
    }

    // Голова списка (первый элемент)
    MyLinkedListNode<E> _head;
    // Хвост списка (последний элемент)
    MyLinkedListNode<E> _tail;
    // Количество элементов в списке
    int _size;

    // Конструктор - создает пустой список
    MyLinkedList() {
        _head = null;
        _tail = null;
        _size = 0;
    }

    // Метод для представления списка в виде строки
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        MyLinkedListNode<E> temp = _head;
        // Проходим по всем элементам списка
        for (int i = 0; i < _size; i++) {
            sb.append(temp.Data);
            // Добавляем запятую между элементами, кроме последнего
            if (i < _size - 1) {
                sb.append(", ");
            }
            temp = temp.Next;
        }
        sb.append(']');
        return sb.toString();
    }

    // Добавляет элемент в конец списка
    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    // Удаляет элемент по индексу
    public E remove(int index) {
        // Проверка корректности индекса
        if (index < 0 || index >= _size) {
            return null;
        }

        // Находим узел по индексу
        MyLinkedListNode<E> temp = _head;
        for (int i = 0; i < index; i++) {
            temp = temp.Next;
        }
        E e = temp.Data;

        // Обновляем ссылки для удаления узла из списка
        if (temp.Previous != null) {
            // Если есть предыдущий узел, связываем его со следующим
            temp.Previous.Next = temp.Next;
        } else {
            // Если удаляем голову, обновляем _head
            _head = temp.Next;
        }

        if (temp.Next != null) {
            // Если есть следующий узел, связываем его с предыдущим
            temp.Next.Previous = temp.Previous;
        } else {
            // Если удаляем хвост, обновляем _tail
            _tail = temp.Previous;
        }

        _size--;

        return e;
    }

    // Удаляет первое вхождение указанного элемента
    @Override
    public boolean remove(Object o) {
        MyLinkedListNode<E> temp = _head;
        int index = 0;
        // Ищем элемент в списке
        while (temp != null) {
            if (temp.Data.equals(o)) {
                // Нашли - удаляем по индексу
                remove(index);
                return true;
            }
            index++;
            temp = temp.Next;
        }
        return false;
    }

    // Возвращает количество элементов в списке
    @Override
    public int size() {
        return _size;
    }

    // Добавляет элемент в начало списка
    @Override
    public void addFirst(E e) {
        MyLinkedListNode<E> node = new MyLinkedListNode<>(e);
        if (_head != null) {
            // Если список не пуст, новый узел становится перед головой
            node.Next = _head;
            _head.Previous = node;
        }
        _head = node;

        // Если список был пуст, хвост тоже указывает на новый узел
        if (_tail == null) {
            _tail = node;
        }

        _size++;
    }

    // Добавляет элемент в конец списка
    @Override
    public void addLast(E e) {
        MyLinkedListNode<E> node = new MyLinkedListNode<>(e);
        if (_tail != null) {
            // Если список не пуст, новый узел становится после хвоста
            _tail.Next = node;
            node.Previous = _tail;
        }
        _tail = node;

        // Если список был пуст, голова тоже указывает на новый узел
        if (_head == null) {
            _head = node;
        }

        _size++;
    }

    // Возвращает первый элемент без удаления
    @Override
    public E element() {
        return getFirst();
    }

    // Возвращает первый элемент без удаления
    @Override
    public E getFirst() {
        if (_size == 0) {
            return null;
        }
        return _head.Data;
    }

    // Возвращает последний элемент без удаления
    @Override
    public E getLast() {
        if (_size == 0) {
            return null;
        }
        return _tail.Data;
    }

    // Удаляет и возвращает первый элемент
    @Override
    public E poll() {
        return pollFirst();
    }

    // Удаляет и возвращает первый элемент
    @Override
    public E pollFirst() {
        if (_size == 0) {
            return null;
        }
        E e = _head.Data;
        _head = _head.Next;

        // Обновляем ссылки
        if (_head != null) {
            _head.Previous = null;
        }
        else {
            // Если список стал пустым, обнуляем хвост
            _tail = null;
        }

        _size--;
        return e;
    }

    // Удаляет и возвращает последний элемент
    @Override
    public E pollLast() {
        if (_size == 0) {
            return null;
        }
        E e = _tail.Data;
        _tail = _tail.Previous;

        // Обновляем ссылки
        if (_tail != null) {
            _tail.Next = null;
        }
        else {
            // Если список стал пустым, обнуляем голову
            _head = null;
        }

        _size--;
        return e;
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
    public boolean addAll(Collection<? extends E> c) {
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
    public void clear() {

    }

    @Override
    public void push(E e) {

    }

    @Override
    public E pop() {
        return null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
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
    public Iterator<E> descendingIterator() {
        return null;
    }
}