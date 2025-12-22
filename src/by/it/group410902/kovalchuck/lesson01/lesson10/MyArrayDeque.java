package by.it.group410902.kovalchuck.lesson01.lesson10;

import java.util.Deque;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {
    private static final int DEFAULT_CAPACITY = 10; // Начальная емкость
    private E[] elements;                           // Массив для хранения элементов
    private int size;                               // Текущее количество элементов
    private int head;                               // Индекс первого элемента
    private int tail;                               // Индекс следующей позиции для добавления

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        // Инициализация массива с начальной емкостью
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
        head = 0;
        tail = 0;
    }

    //Возвращает строковое представление дека
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        // Обход элементов
        for (int i = 0; i < size; i++) {
            int index = (head + i) % elements.length; // Расчет индекса
            sb.append(elements[index]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    //Возвращает количество элементов в деке
    @Override
    public int size() {
        return size;
    }

    //Добавляет элемент в конец дека
    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    //Добавляет элемент в начало дека
    @Override
    public void addFirst(E element) {
        if (element == null) {
            throw new NullPointerException();
        }

        ensureCapacity(); // Проверка и увеличение емкости при необходимости

        // Сдвиг назад
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;

        // Если это первый элемент, синхронизируем
        if (size == 1) {
            tail = head;
        }
    }

    //Добавляет элемент в конец дека
    @Override
    public void addLast(E element) {
        if (element == null) {
            throw new NullPointerException();
        }

        ensureCapacity(); // Проверка и увеличение емкости при необходимости

        // Добавление элемента в текущую позицию
        elements[tail] = element;
        // Сдвиг tail вперед с учетом кольцевой структуры
        tail = (tail + 1) % elements.length;
        size++;

        // Если это первый элемент, синхронизируем head
        if (size == 1) {
            head = 0;
        }
    }

    //Возвращает первый элемент без удаления
    @Override
    public E element() {
        return getFirst();
    }

    //Возвращает первый элемент без удаления
    @Override
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return elements[head];
    }

    //Возвращает последний элемент без удаления
    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        // Расчет индекса последнего элемента
        return elements[(tail - 1 + elements.length) % elements.length];
    }

    //Удаляет и возвращает первый элемент
    @Override
    public E poll() {
        return pollFirst();
    }

    //Удаляет и возвращает первый элемент
    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }

        E element = elements[head];
        elements[head] = null; // Очистка ссылки для
        head = (head + 1) % elements.length; // Сдвиг вперед
        size--;

        // Если дек стал пустым, сброс индексов
        if (size == 0) {
            head = tail = 0;
        }

        return element;
    }

    //Удаляет и возвращает последний элемент
    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        // Сдвиг назад
        tail = (tail - 1 + elements.length) % elements.length;
        E element = elements[tail];
        elements[tail] = null; // Очистка ссылки для
        size--;

        // Если дек стал пустым, сброс индексов
        if (size == 0) {
            head = tail = 0;
        }

        return element;
    }

    //Увеличивает емкость массива при необходимости
    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = elements.length * 2; // Удвоение емкости
            E[] newElements = (E[]) new Object[newCapacity];

            // Копирование элементов в новый массив в правильном порядке
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[(head + i) % elements.length];
            }

            elements = newElements;
            head = 0;    // Сброс в начало
            tail = size; // указывает на следующую пустую позицию
        }
    }

    //Проверяет, пуст ли дек
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    //Добавляет элемент в конец дека
    @Override
    public boolean offer(E e) {
        return add(e);
    }

    //Добавляет элемент в начало дека
    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    //Добавляет элемент в конец дека
    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    //Удаляет и возвращает первый элемент
    @Override
    public E remove() {
        return removeFirst();
    }

    //Удаляет и возвращает первый элемент
    @Override
    public E removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return pollFirst();
    }

    //Удаляет и возвращает последний элемент
    @Override
    public E removeLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return pollLast();
    }

    //Возвращает первый элемент без удаления
    @Override
    public E peek() {
        return peekFirst();
    }

    //Возвращает первый элемент
    @Override
    public E peekFirst() {
        return (size == 0) ? null : getFirst();
    }

    //Возвращает последний элемент
    @Override
    public E peekLast() {
        return (size == 0) ? null : getLast();
    }

    //Удаляет первое вхождение элемента
    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    //Удаляет последнее вхождение элемента
    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    //Добавляет все элементы коллекции
    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    //Добавляет элемент в начало дека
    @Override
    public void push(E e) {
        addFirst(e);
    }

    //Удаляет и возвращает первый элемент
    @Override
    public E pop() {
        return removeFirst();
    }

    //Удаляет первое вхождение элемента
    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    //Проверяет наличие элемента
    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    //Возвращает итератор
    @Override
    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    //Возвращает обратный итератор
    @Override
    public java.util.Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException();
    }

    //Очищает дек, удаляя все элементы
    @Override
    public void clear() {
        // Очистка всех ссылок
        for (int i = 0; i < elements.length; i++) {
            elements[i] = null;
        }
        size = 0;
        head = 0;
        tail = 0;
    }

    //Проверяет наличие всех элементов коллекции
    @Override
    public boolean containsAll(java.util.Collection<?> c) {

        throw new UnsupportedOperationException();
    }

    //Удаляет все элементы, содержащиеся в коллекции
    @Override
    public boolean removeAll(java.util.Collection<?> c) {

        throw new UnsupportedOperationException();
    }

    //Сохраняет только элементы, содержащиеся в коллекции
    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    //Возвращает массив всех элементов в правильном порядке
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        // Копирование элементов в массив
        for (int i = 0; i < size; i++) {
            array[i] = elements[(head + i) % elements.length];
        }
        return array;
    }

    //Возвращает типизированный массив
    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}