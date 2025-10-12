package by.it.group451002.shandr.lesson09;

import java.util.Deque;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private E[] elements;
    private int size;
    private int head; // Индекс первого элемента
    private int tail; // Индекс следующего за последним элементом

    // Конструктор по умолчанию
    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.head = 0;
        this.tail = 0;
    }

    // Конструктор с начальной емкостью
    @SuppressWarnings("unchecked")
    public MyArrayDeque(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        this.elements = (E[]) new Object[initialCapacity];
        this.size = 0;
        this.head = 0;
        this.tail = 0;
    }

    /**
     * Увеличивает емкость массива при необходимости
     * @param minCapacity минимальная требуемая емкость
     */
    @SuppressWarnings("unchecked")
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = Math.max(elements.length * 2, minCapacity);
            E[] newElements = (E[]) new Object[newCapacity];
            
            // Копируем элементы с учетом циклической природы
            if (head < tail) {
                // Элементы расположены последовательно
                System.arraycopy(elements, head, newElements, 0, size);
            } else {
                // Элементы разорваны (head > tail) - циклический случай
                int firstPart = elements.length - head;
                System.arraycopy(elements, head, newElements, 0, firstPart);
                System.arraycopy(elements, 0, newElements, firstPart, tail);
            }
            
            elements = newElements;
            head = 0;
            tail = size;
        }
    }

    /**
     * Вычисляет следующий индекс с учетом циклической природы массива
     * @param index текущий индекс
     * @return следующий индекс
     */
    private int nextIndex(int index) {
        return (index + 1) % elements.length;
    }

    /**
     * Вычисляет предыдущий индекс с учетом циклической природы массива
     * @param index текущий индекс
     * @return предыдущий индекс
     */
    private int prevIndex(int index) {
        return (index - 1 + elements.length) % elements.length;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Возвращает строковое представление дека в формате [element1, element2, ...]
     * @return строковое представление дека
     */
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        
        // Обходим элементы в порядке от head к tail
        int current = head;
        for (int i = 0; i < size; i++) {
            sb.append(elements[current]);
            if (i < size - 1) {
                sb.append(", ");
            }
            current = nextIndex(current);
        }
        
        sb.append(']');
        return sb.toString();
    }

    /**
     * Возвращает количество элементов в деке
     * @return количество элементов
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Добавляет элемент в конец дека (эквивалент addLast)
     * @param element элемент для добавления
     * @return true (как указано в контракте Collection)
     */
    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    /**
     * Добавляет элемент в начало дека
     * @param element элемент для добавления
     */
    @Override
    public void addFirst(E element) {
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }
        
        ensureCapacity(size + 1);
        
        // Сдвигаем head назад (с учетом цикличности)
        head = prevIndex(head);
        elements[head] = element;
        size++;
    }

    /**
     * Добавляет элемент в конец дека
     * @param element элемент для добавления
     */
    @Override
    public void addLast(E element) {
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }
        
        ensureCapacity(size + 1);
        
        // Добавляем в tail и сдвигаем tail вперед
        elements[tail] = element;
        tail = nextIndex(tail);
        size++;
    }

    /**
     * Возвращает первый элемент дека без удаления (эквивалент getFirst)
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
        return elements[prevIndex(tail)];
    }

    /**
     * Извлекает и удаляет первый элемент дека (эквивалент pollFirst)
     * @return первый элемент дека или null если дек пуст
     */
    @Override
    public E poll() {
        return pollFirst();
    }

    /**
     * Извлекает и удаляет первый элемент дека
     * @return первый элемент дека или null если дек пуст
     */
    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }
        
        E element = elements[head];
        elements[head] = null; // Помогаем сборщику мусора
        head = nextIndex(head);
        size--;
        
        return element;
    }

    /**
     * Извлекает и удаляет последний элемент дека
     * @return последний элемент дека или null если дек пуст
     */
    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        
        tail = prevIndex(tail);
        E element = elements[tail];
        elements[tail] = null; // Помогаем сборщику мусора
        size--;
        
        return element;
    }

    /////////////////////////////////////////////////////////////////////////
    //////      Остальные методы интерфейса Deque (необязательные)    ///////
    /////////////////////////////////////////////////////////////////////////

    // Для остальных методов возвращаем значения по умолчанию или бросаем исключения

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        return pollFirst();
    }

    @Override
    public E removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        return pollFirst();
    }

    @Override
    public E removeLast() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        return pollLast();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public E peekFirst() {
        return (size == 0) ? null : getFirst();
    }

    @Override
    public E peekLast() {
        return (size == 0) ? null : getLast();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean push(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    // Методы интерфейса Collection (необязательные)

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}