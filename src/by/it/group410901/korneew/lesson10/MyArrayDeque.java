package by.it.group410901.korneew.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

// Реализация двунаправленной очереди на массиве
public class MyArrayDeque<E> implements Deque<E> {
    // стартовый размер внутреннего массива
    final static int InitialSize = 8;
    // позиция первого элемента в массиве
    int _front;
    // позиция последнего элемента в массиве
    int _rear;
    // количество элементов в деке
    int _size;
    // внутренний контейнер для элементов
    E[] _elements;

    // конструктор по умолчанию
    MyArrayDeque() {
        this(InitialSize);
    }

    // конструктор с указанием размера массива
    MyArrayDeque(int size) {
        _front = size / 2;      // начинаем с середины массива
        _rear = _front - 1;     // хвост расположен левее головы
        _size = 0;              // пустая дека
        _elements = (E[]) new Object[size]; // выделяем массив
    }

    // расширяет внутренний массив до нового размера и центрирует данные
    private void Resize(int size) {
        E[] temp = (E[]) new Object[size]; // новый массив
        int k = (size - _size) / 2;        // сдвиг для центрирования

        // перенос существующих элементов в новый массив
        for (int i = 0; i < _size; i++) {
            temp[i + k] = _elements[_front + i];
        }

        // корректируем индексы и заменяем массив
        _front = k;
        _rear = k + _size - 1;
        _elements = temp;
    }

    // формирует строковое представление в формате [a, b, c]
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append('[');
        for (int i = 0; i < _size; i++) {
            sb.append(_elements[_front + i]); // берем элементы подряд
            if (i < _size - 1) {
                sb.append(", "); // разделитель между элементами
            }
        }
        sb.append(']');

        return sb.toString();
    }

    // текущее количество элементов
    @Override
    public int size() {
        return _size;
    }

    // добавляет элемент в конец (стандартное поведение Collection.add)
    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    // вставка в начало
    @Override
    public void addFirst(E e) {
        if (_front == 0) { // если слева нет места
            Resize(_elements.length * 2); // увеличиваем массив
        }

        _front--;          // сдвигаем указатель начала влево
        _size++;           // увеличиваем счётчик
        _elements[_front] = e; // сохраняем значение
    }

    // вставка в конец
    @Override
    public void addLast(E e) {
        if (_rear == _elements.length - 1) { // если справа места нет
            Resize(_elements.length * 2); // расширяем массив
        }

        _rear++;            // сдвигаем указатель конца вправо
        _size++;            // увеличиваем счётчик
        _elements[_rear] = e; // сохраняем значение
    }

    // возвращает первый элемент без удаления
    @Override
    public E element() {
        return getFirst();
    }

    // безопасно получает первый элемент (null если пусто)
    @Override
    public E getFirst() {
        if (_size == 0) { // дека пустая
            return null;
        }
        return _elements[_front]; // элемент в позиции head
    }

    // безопасно получает последний элемент (null если пусто)
    @Override
    public E getLast() {
        if (_size == 0) { // дека пустая
            return null;
        }
        return _elements[_rear]; // элемент в позиции tail
    }

    // удаляет и возвращает первый элемент (или null)
    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (_size == 0) { // пустая дека
            return null;
        }
        _front++;         // сдвигаем начало вправо
        _size--;          // уменьшаем счётчик
        return _elements[_front - 1]; // возвращаем удалённое значение
    }

    // удаляет и возвращает последний элемент (или null)
    @Override
    public E pollLast() {
        if (_size == 0) { // пустая дека
            return null;
        }
        _rear--;          // сдвигаем конец влево
        _size--;          // уменьшаем счётчик
        return _elements[_rear + 1]; // возвращаем удалённый элемент
    }

    // ----------------- не реализованные методы (заглушки) -----------------

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