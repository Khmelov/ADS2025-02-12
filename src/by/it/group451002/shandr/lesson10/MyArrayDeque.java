package by.it.group451002.shandr.lesson10;

import java.util.Deque;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {
    private static final int DEFAULT_CAPACITY = 10; // Начальная емкость по умолчанию
    private E[] elements; // Массив для хранения элементов дека
    private int head;     // Индекс первого элемента в деке
    private int tail;     // Индекс последнего элемента в деке
    private int size;     // Количество элементов в деке

    /**
     * Конструктор по умолчанию
     * Создает дек с начальной емкостью DEFAULT_CAPACITY
     */
    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }

    /**
     * Конструктор с указанием начальной емкости
     * @param initialCapacity начальная емкость дека
     * @throws IllegalArgumentException если initialCapacity <= 0
     */
    @SuppressWarnings("unchecked")
    public MyArrayDeque(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        this.elements = (E[]) new Object[initialCapacity];
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }

    /**
     * Возвращает строковое представление дека
     * @return строка в формате [element1, element2, ...]
     */
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            // Вычисляем индекс с учетом циклического массива
            int index = (head + i) % elements.length;
            sb.append(elements[index]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Возвращает количество элементов в деке
     * @return размер дека
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Добавляет элемент в конец дека
     * @param element элемент для добавления
     * @return true если элемент успешно добавлен
     */
    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    /**
     * Добавляет элемент в начало дека
     * @param element элемент для добавления
     * @throws NullPointerException если элемент равен null
     */
    @Override
    public void addFirst(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        ensureCapacity(); // Проверяем и увеличиваем емкость при необходимости

        // Вычисляем новую позицию для head (с учетом циклического массива)
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;

        // Если это первый элемент, устанавливаем tail равным head
        if (size == 1) {
            tail = head;
        }
    }

    /**
     * Добавляет элемент в конец дека
     * @param element элемент для добавления
     * @throws NullPointerException если элемент равен null
     */
    @Override
    public void addLast(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        ensureCapacity(); // Проверяем и увеличиваем емкость при необходимости

        // Вычисляем новую позицию для tail (с учетом циклического массива)
        tail = (tail + 1) % elements.length;
        elements[tail] = element;
        size++;

        // Если это первый элемент, устанавливаем head равным tail
        if (size == 1) {
            head = tail;
        }
    }

    /**
     * Возвращает первый элемент дека без удаления
     * @return первый элемент дека
     * @throws NoSuchElementException если дек пуст
     */
    @Override
    public E element() {
        return getFirst();
    }

    /**
     * Возвращает первый элемент дека без удаления
     * @return первый элемент дека
     * @throws NoSuchElementException если дек пуст
     */
    @Override
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        return elements[head];
    }

    /**
     * Возвращает последний элемент дека без удаления
     * @return последний элемент дека
     * @throws NoSuchElementException если дек пуст
     */
    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        return elements[tail];
    }

    /**
     * Удаляет и возвращает первый элемент дека
     * @return первый элемент дека или null если дек пуст
     */
    @Override
    public E poll() {
        return pollFirst();
    }

    /**
     * Удаляет и возвращает первый элемент дека
     * @return первый элемент дека или null если дек пуст
     */
    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }

        E element = elements[head];
        elements[head] = null; // Помогаем сборщику мусора
        // Сдвигаем head вперед (с учетом циклического массива)
        head = (head + 1) % elements.length;
        size--;

        // Если дек стал пустым, сбрасываем позиции
        if (size == 0) {
            head = 0;
            tail = 0;
        }

        return element;
    }

    /**
     * Удаляет и возвращает последний элемент дека
     * @return последний элемент дека или null если дек пуст
     */
    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }

        E element = elements[tail];
        elements[tail] = null; // Помогаем сборщику мусора
        // Сдвигаем tail назад (с учетом циклического массива)
        tail = (tail - 1 + elements.length) % elements.length;
        size--;

        // Если дек стал пустым, сбрасываем позиции
        if (size == 0) {
            head = 0;
            tail = 0;
        }

        return element;
    }

    /**
     * Вспомогательный метод для увеличения емкости при необходимости
     * Удваивает размер массива когда он заполнен
     */
    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = elements.length * 2;
            E[] newElements = (E[]) new Object[newCapacity];

            // Копируем элементы в новый массив в правильном порядке
            for (int i = 0; i < size; i++) {
                int index = (head + i) % elements.length;
                newElements[i] = elements[index];
            }

            elements = newElements;
            head = 0;
            tail = size - 1;
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////    Остальные методы интерфейса Deque - необязательные     ///////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Проверяет, пуст ли дек
     * @return true если дек пуст
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Добавляет элемент в конец дека (аналогично add)
     * @param e элемент для добавления
     * @return true если элемент успешно добавлен
     */
    @Override
    public boolean offer(E e) {
        return add(e);
    }

    /**
     * Добавляет элемент в начало дека (аналогично addFirst)
     * @param e элемент для добавления
     * @return true если элемент успешно добавлен
     */
    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    /**
     * Добавляет элемент в конец дека (аналогично addLast)
     * @param e элемент для добавления
     * @return true если элемент успешно добавлен
     */
    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    /**
     * Удаляет и возвращает первый элемент дека
     * @return первый элемент дека
     * @throws NoSuchElementException если дек пуст
     */
    @Override
    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        return pollFirst();
    }

    /**
     * Удаляет и возвращает первый элемент дека
     * @return первый элемент дека
     * @throws NoSuchElementException если дек пуст
     */
    @Override
    public E removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        return pollFirst();
    }

    /**
     * Удаляет и возвращает последний элемент дека
     * @return последний элемент дека
     * @throws NoSuchElementException если дек пуст
     */
    @Override
    public E removeLast() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        return pollLast();
    }

    /**
     * Возвращает первый элемент дека без удаления
     * @return первый элемент дека или null если дек пуст
     */
    @Override
    public E peek() {
        return peekFirst();
    }

    /**
     * Возвращает первый элемент дека без удаления
     * @return первый элемент дека или null если дек пуст
     */
    @Override
    public E peekFirst() {
        return (size == 0) ? null : getFirst();
    }

    /**
     * Возвращает последний элемент дека без удаления
     * @return последний элемент дека или null если дек пуст
     */
    @Override
    public E peekLast() {
        return (size == 0) ? null : getLast();
    }

    /**
     * Удаляет первое вхождение указанного элемента
     * @param o элемент для удаления
     * @return true если элемент был найден и удален
     * @throws UnsupportedOperationException метод не реализован
     */
    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Проверяет наличие элемента в деке
     * @param o элемент для поиска
     * @return true если элемент найден
     * @throws UnsupportedOperationException метод не реализован
     */
    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Возвращает итератор для обхода дека
     * @return итератор
     * @throws UnsupportedOperationException метод не реализован
     */
    @Override
    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Возвращает итератор для обратного обхода дека
     * @return итератор для обратного обхода
     * @throws UnsupportedOperationException метод не реализован
     */
    @Override
    public java.util.Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Добавляет элемент в начало дека (аналогично addFirst)
     * @param e элемент для добавления
     */
    @Override
    public void push(E e) {
        addFirst(e);
    }

    /**
     * Удаляет и возвращает первый элемент дека (аналогично removeFirst)
     * @return первый элемент дека
     */
    @Override
    public E pop() {
        return removeFirst();
    }

    /**
     * Удаляет первое вхождение указанного элемента
     * @param o элемент для удаления
     * @return true если элемент был найден и удален
     * @throws UnsupportedOperationException метод не реализован
     */
    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Удаляет последнее вхождение указанного элемента
     * @param o элемент для удаления
     * @return true если элемент был найден и удален
     * @throws UnsupportedOperationException метод не реализован
     */
    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /////////////////////////////////////////////////////////////////////////
    //////    Остальные методы Collection - не реализованы          ///////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Добавляет все элементы коллекции в дек
     * @param c коллекция для добавления
     * @return true если дек изменился
     * @throws UnsupportedOperationException метод не реализован
     */
    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Очищает дек, удаляя все элементы
     * @throws UnsupportedOperationException метод не реализован
     */
    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Проверяет наличие всех элементов коллекции в деке
     * @param c коллекция для проверки
     * @return true если все элементы присутствуют
     * @throws UnsupportedOperationException метод не реализован
     */
    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Удаляет все элементы, присутствующие в указанной коллекции
     * @param c коллекция элементов для удаления
     * @return true если дек изменился
     * @throws UnsupportedOperationException метод не реализован
     */
    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Сохраняет только элементы, присутствующие в указанной коллекции
     * @param c коллекция элементов для сохранения
     * @return true если дек изменился
     * @throws UnsupportedOperationException метод не реализован
     */
    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Преобразует дек в массив объектов
     * @return массив элементов дека
     * @throws UnsupportedOperationException метод не реализован
     */
    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Преобразует дек в массив указанного типа
     * @param a массив для заполнения
     * @return массив элементов дека
     * @throws UnsupportedOperationException метод не реализован
     */
    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not implemented");
    }
}