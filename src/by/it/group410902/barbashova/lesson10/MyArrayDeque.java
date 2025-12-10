package by.it.group410902.barbashova.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyArrayDeque<E> implements Deque<E> {
    // Начальный размер массива по умолчанию
    final static int InitialSize = 8;
    // Индекс первого элемента в очереди
    int _front;
    // Индекс последнего элемента в очереди
    int _rear;
    // Текущее количество элементов в очереди
    int _size;
    // Массив для хранения элементов очереди
    E[] _elements;

    // Конструктор по умолчанию
    MyArrayDeque() {
        this(InitialSize);
    }

    // Конструктор с заданным размером
    MyArrayDeque(int size) {
        // Начинаем с середины массива чтобы было место для добавления в обе стороны
        _front = size / 2;
        _rear = _front - 1;
        _size = 0;
        _elements = (E[]) new Object[size];
    }

    // Метод для увеличения размера массива когда он заполняется
    private void Resize(int size) {
        // Создаем новый массив большего размера
        E[] temp = (E[]) new Object[size];
        // Вычисляем новую позицию для элементов (по центру нового массива)
        int k = (size - _size) / 2;

        // Копируем все элементы из старого массива в новый
        for (int i = 0; i < _size; i++) {
            temp[i + k] = _elements[_front + i];
        }

        // Обновляем индексы начала и конца
        _front = k;
        _rear = k + _size - 1;
        // Заменяем старый массив новым
        _elements = temp;
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for(int i=0; i<_size;i++){
            sb.append(_elements[_front+1]);
            if(i < _size-1){
                sb.append(',');
            }
        }
        sb.append(']');
        return sb.toString();

    }

    // Возвращает количество элементов в очереди
    @Override
    public int size() {
        return _size;
    }

    // Добавляет элемент в конец очереди
    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    // Добавляет элемент в начало очереди
    @Override
    public void addFirst(E e) {
        // Если нет места в начале массива, увеличиваем его
        if (_front == 0) {
            Resize(_elements.length * 2);
        }

        // Сдвигаем указатель начала и добавляем элемент
        _front--;
        _size++;
        _elements[_front] = e;
    }

    // Добавляет элемент в конец очереди
    @Override
    public void addLast(E e) {
        // Если нет места в конце массива, увеличиваем его
        if (_rear == _elements.length - 1) {
            Resize(_elements.length * 2);
        }

        // Сдвигаем указатель конца и добавляем элемент
        _rear++;
        _size++;
        _elements[_rear] = e;
    }

    // Возвращает первый элемент без удаления
    @Override
    public E element() {
        return getFirst();
    }

    // Возвращает первый элемент без удаления
    @Override
    public E getFirst() {
        // Если очередь пуста, возвращаем null
        if (_size == 0) {
            return null;
        }

        return _elements[_front];
    }

    // Возвращает последний элемент без удаления
    @Override
    public E getLast() {
        // Если очередь пуста, возвращаем null
        if (_size == 0) {
            return null;
        }

        return _elements[_rear];
    }

    // Удаляет и возвращает первый элемент
    @Override
    public E poll() {
        return pollFirst();
    }

    // Удаляет и возвращает первый элемент
    @Override
    public E pollFirst() {
        // Если очередь пуста, возвращаем null
        if (_size == 0) {
            return null;
        }

        // Сдвигаем указатель начала и уменьшаем размер
        _front++;
        _size--;
        // Возвращаем элемент, который был первым
        return _elements[_front - 1];
    }

    // Удаляет и возвращает последний элемент
    @Override
    public E pollLast() {
        // Если очередь пуста, возвращаем null
        if (_size == 0) {
            return null;
        }

        // Сдвигаем указатель конца и уменьшаем размер
        _rear--;
        _size--;
        // Возвращаем элемент, который был последним
        return _elements[_rear + 1];
    }

    // =========================================================================
    // Следующие методы не реализованы и оставлены как заглушки
    // =========================================================================

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
    public boolean remove(Object o) {
        return false;
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