package by.it.group410902.varava.lesson10;

import java.util.Deque;
import java.util.NoSuchElementException;

/**
 * Реализация интерфейса Deque на основе кольцевого массива
 * @param <E> тип элементов в деке
 */
public class MyArrayDeque<E> implements Deque<E> {
    // Константы
    private static final int DEFAULT_CAPACITY = 10; // Начальная емкость по умолчанию

    // Поля класса
    private E[] elements; // Массив для хранения элементов
    private int head;     // Индекс первого элемента в деке
    private int tail;     // Индекс последнего элемента в деке
    private int size;     // Текущее количество элементов в деке

    /**
     * Конструктор по умолчанию - создает дек с емкостью по умолчанию
     */
    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    /**
     * Конструктор с заданной начальной емкостью
     * @param initialCapacity начальная емкость дека
     * @throws IllegalArgumentException если начальная емкость не положительна
     */
    @SuppressWarnings("unchecked")
    public MyArrayDeque(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        elements = (E[]) new Object[initialCapacity];
        head = 0;
        tail = 0;
        size = 0;
    }

    /**
     * Представление дека в виде строки
     * @return строковое представление элементов дека в порядке от первого к последнему
     */
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        // Проходим по всем элементам в порядке их следования в деке
        for (int i = 0; i < size; i++) {
            int index = (head + i) % elements.length; // Вычисляем индекс с учетом кольцевой структуры
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
     * @return текущий размер дека
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Добавляет элемент в конец дека
     * @param element добавляемый элемент
     * @return true (как указано в интерфейсе Collection)
     */
    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    /**
     * Добавляет элемент в начало дека
     * @param element добавляемый элемент
     * @throws NullPointerException если элемент равен null
     */
    @Override
    public void addFirst(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        ensureCapacity(); // Проверяем и увеличиваем емкость при необходимости

        // Вычисляем новую позицию для head (двигаемся назад по кольцу)
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;

        // Если это первый элемент, синхронизируем tail с head
        if (size == 1) {
            tail = head;
        }
    }

    /**
     * Добавляет элемент в конец дека
     * @param element добавляемый элемент
     * @throws NullPointerException если элемент равен null
     */
    @Override
    public void addLast(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        ensureCapacity(); // Проверяем и увеличиваем емкость при необходимости

        // Вычисляем новую позицию для tail (двигаемся вперед по кольцу)
        tail = (tail + 1) % elements.length;
        elements[tail] = element;
        size++;

        // Если это первый элемент, синхронизируем head с tail
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
     * Извлекает и удаляет первый элемент дека
     * @return первый элемент дека или null, если дек пуст
     */
    @Override
    public E poll() {
        return pollFirst();
    }

    /**
     * Извлекает и удаляет первый элемент дека
     * @return первый элемент дека или null, если дек пуст
     */
    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }

        E element = elements[head];
        elements[head] = null; // Помогаем сборщику мусора
        head = (head + 1) % elements.length; // Двигаем head вперед
        size--;

        // Если дек стал пустым, сбрасываем индексы
        if (size == 0) {
            head = tail = 0;
        }

        return element;
    }

    /**
     * Извлекает и удаляет последний элемент дека
     * @return последний элемент дека или null, если дек пуст
     */
    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }

        E element = elements[tail];
        elements[tail] = null; // Помогаем сборщику мусора
        tail = (tail - 1 + elements.length) % elements.length; // Двигаем tail назад
        size--;

        // Если дек стал пустым, сбрасываем индексы
        if (size == 0) {
            head = tail = 0;
        }

        return element;
    }

    /**
     * Увеличивает емкость массива при необходимости
     * Удваивает размер массива и копирует элементы в новый массив
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
            head = 0;        // Новый head в начале массива
            tail = size - 1; // Новый tail в конце элементов
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////    Остальные методы интерфейса Deque - не реализованы     ///////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Проверяет, пуст ли дек
     * @return true если дек пуст, false в противном случае
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Добавляет элемент в конец дека (аналогично add)
     * @param e добавляемый элемент
     * @return true если элемент добавлен успешно
     */
    @Override
    public boolean offer(E e) {
        return add(e);
    }

    /**
     * Добавляет элемент в начало дека (аналогично addFirst)
     * @param e добавляемый элемент
     * @return true если элемент добавлен успешно
     */
    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    /**
     * Добавляет элемент в конец дека (аналогично addLast)
     * @param e добавляемый элемент
     * @return true если элемент добавлен успешно
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
     * @return первый элемент дека или null, если дек пуст
     */
    @Override
    public E peek() {
        return peekFirst();
    }

    /**
     * Возвращает первый элемент дека без удаления
     * @return первый элемент дека или null, если дек пуст
     */
    @Override
    public E peekFirst() {
        return size == 0 ? null : getFirst();
    }

    /**
     * Возвращает последний элемент дека без удаления
     * @return последний элемент дека или null, если дек пуст
     */
    @Override
    public E peekLast() {
        return size == 0 ? null : getLast();
    }

    /**
     * Добавляет элемент в начало дека (стековая операция)
     * @param e добавляемый элемент
     */
    @Override
    public void push(E e) {
        addFirst(e);
    }

    /**
     * Извлекает и удаляет первый элемент дека (стековая операция)
     * @return первый элемент дека
     */
    @Override
    public E pop() {
        return removeFirst();
    }

    // Методы, которые не реализованы и выбрасывают исключение UnsupportedOperationException

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public java.util.Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public java.util.Spliterator<E> spliterator() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not implemented");
    }
}