package by.it.group410901.zdanovich.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

// Класс MyArrayDeque реализует интерфейс Deque<E> на основе массива
public class MyArrayDeque<E> implements Deque<E> {
    // начальный размер массива
    final static int InitialSize = 8;
    // индекс первого элемента (головы)
    int _front;
    // индекс последнего элемента (хвоста)
    int _rear;
    // текущее количество элементов
    int _size;
    // массив для хранения элементов
    E[] _elements;

    // конструктор без параметров, использует размер по умолчанию
    MyArrayDeque() {
        this(InitialSize);
    }

    // конструктор с заданным размером массива
    MyArrayDeque(int size) {
        _front = size / 2;      // ставим голову в середину массива
        _rear = _front - 1;     // хвост сразу слева от головы
        _size = 0;              // пока пусто
        _elements = (E[]) new Object[size]; // создаём массив
    }

    // метод увеличивает массив при нехватке места
    private void Resize(int size) {
        E[] temp = (E[]) new Object[size]; // создаём новый массив большего размера
        int k = (size - _size) / 2;        // сдвигаем элементы к середине

        // копируем элементы из старого массива в новый
        for (int i = 0; i < _size; i++) {
            temp[i + k] = _elements[_front + i];
        }

        // обновляем индексы головы и хвоста
        _front = k;
        _rear = k + _size - 1;
        _elements = temp; // заменяем массив
    }

    // метод возвращает строковое представление deque
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append('[');
        for (int i = 0; i < _size; i++) {
            sb.append(_elements[_front + i]); // добавляем элементы по порядку
            if (i < _size - 1) {
                sb.append(", "); // ставим запятую между элементами
            }
        }
        sb.append(']');

        return sb.toString();
    }

    // возвращает количество элементов
    @Override
    public int size() {
        return _size;
    }

    // добавление элемента (по умолчанию в конец)
    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    // добавление элемента в начало
    @Override
    public void addFirst(E e) {
        if (_front == 0) { // если места слева нет
            Resize(_elements.length * 2); // увеличиваем массив
        }

        _front--;          // двигаем голову влево
        _size++;           // увеличиваем размер
        _elements[_front] = e; // вставляем элемент
    }

    // добавление элемента в конец
    @Override
    public void addLast(E e) {
        if (_rear == _elements.length - 1) { // если места справа нет
            Resize(_elements.length * 2); // увеличиваем массив
        }

        _rear++;            // двигаем хвост вправо
        _size++;            // увеличиваем размер
        _elements[_rear] = e; // вставляем элемент
    }

    // возвращает первый элемент, не удаляя
    @Override
    public E element() {
        return getFirst();
    }

    // возвращает первый элемент
    @Override
    public E getFirst() {
        if (_size == 0) { // если пусто
            return null;
        }
        return _elements[_front]; // возвращаем голову
    }

    // возвращает последний элемент
    @Override
    public E getLast() {
        if (_size == 0) { // если пусто
            return null;
        }
        return _elements[_rear]; // возвращаем хвост
    }

    // удаляет и возвращает первый элемент
    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (_size == 0) { // если пусто
            return null;
        }
        _front++;         // двигаем голову вправо
        _size--;          // уменьшаем количество
        return _elements[_front - 1]; // возвращаем удалённый элемент
    }

    // удаляет и возвращает последний элемент
    @Override
    public E pollLast() {
        if (_size == 0) { // если пусто
            return null;
        }
        _rear--;          // двигаем хвост влево
        _size--;          // уменьшаем количество
        return _elements[_rear + 1]; // возвращаем удалённый элемент
    }

    // ----------------- методы-заглушки -----------------

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
    public boolean remove(Object o) { return false; }

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
