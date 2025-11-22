package by.it.group451003.kharkevich.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyArrayDeque<E> implements Deque<E> {
    final static int InitialSize = 8; // Начальный размер массива
    int _front; // Индекс первого элемента в деке
    int _rear;  // Индекс последнего элемента в деке
    int _size;  // Текущее количество элементов
    E[] _elements; // Массив для хранения элементов

    // Конструктор по умолчанию
    MyArrayDeque() {
        this(InitialSize); // Вызов конструктора с начальным размером
    }

    // Конструктор с заданным размером
    MyArrayDeque(int size) {
        _front = size / 2; // Начало в середине массива
        _rear = _front - 1; // Конец перед началом (пустая очередь)
        _size = 0; // Начальный размер 0
        _elements = (E[]) new Object[size]; // Создание массива элементов
    }

    // Метод для изменения размера массива
    private void Resize(int size) {
        E[] temp = (E[]) new Object[size]; // Создание нового массива
        int k = (size - _size) / 2; // Центрирование элементов в новом массиве

        // Копирование элементов из старого массива в новый
        for (int i = 0; i < _size; i++) {
            temp[i + k] = _elements[_front + i];
        }

        // Обновление указателей
        _front = k;
        _rear = k + _size - 1;
        _elements = temp; // Замена старого массива новым
    }

    // Преобразование дека в строку
    public String toString() {
        StringBuilder sb = new StringBuilder(); // Строитель строки

        sb.append('['); // Начало массива
        // Добавление всех элементов через запятую
        for (int i = 0; i < _size; i++) {
            sb.append(_elements[_front + i]);
            if (i < _size - 1) {
                sb.append(", ");
            }
        }
        sb.append(']'); // Конец массива

        return sb.toString(); // Возврат строки
    }

    @Override
    public int size() {
        return _size; // Возврат текущего размера
    }

    @Override
    public boolean add(E e) {
        addLast(e); // Добавление в конец
        return true; // Всегда возвращает true
    }

    @Override
    public void addFirst(E e) {
        // Если массив заполнен слева - увеличиваем размер
        if (_front == 0) {
            Resize(_elements.length * 2);
        }

        _front--; // Сдвиг указателя начала влево
        _size++; // Увеличение размера
        _elements[_front] = e; // Добавление элемента
    }

    @Override
    public void addLast(E e) {
        // Если массив заполнен справа - увеличиваем размер
        if (_rear == _elements.length - 1) {
            Resize(_elements.length * 2);
        }

        _rear++; // Сдвиг указателя конца вправо
        _size++; // Увеличение размера
        _elements[_rear] = e; // Добавление элемента
    }

    @Override
    public E element() {
        return getFirst(); // Получение первого элемента
    }

    @Override
    public E getFirst() {
        if (_size == 0) {
            return null; // Если дек пуст - возврат null
        }

        return _elements[_front]; // Возврат первого элемента
    }

    @Override
    public E getLast() {
        if (_size == 0) {
            return null; // Если дек пуст - возврат null
        }

        return _elements[_rear]; // Возврат последнего элемента
    }

    @Override
    public E poll() {
        return pollFirst(); // Удаление первого элемента
    }

    @Override
    public E pollFirst() {
        if (_size == 0) {
            return null; // Если дек пуст - возврат null
        }

        _front++; // Сдвиг указателя начала вправо
        _size--; // Уменьшение размера
        return _elements[_front - 1]; // Возврат удаленного элемента
    }

    @Override
    public E pollLast() {
        if (_size == 0) {
            return null; // Если дек пуст - возврат null
        }

        _rear--; // Сдвиг указателя конца влево
        _size--; // Уменьшение размера
        return _elements[_rear + 1]; // Возврат удаленного элемента
    }

    // Остальные методы не реализованы
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