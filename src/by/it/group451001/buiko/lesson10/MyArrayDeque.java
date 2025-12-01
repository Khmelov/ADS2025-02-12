package by.it.group451001.buiko.lesson10;

import java.util.*;

/**
 * Реализация интерфейса Deque на основе циклического массива
 * @param <E> тип элементов в деке
 */
public class MyArrayDeque<E> implements Deque<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private E[] elements;
    private int head;
    private int tail;
    private int size;

    /*Дек  двусторонняя очередь, которая позволяет эффективно добавлять
     и удалять элементы как в начале, так и в конце коллекции, сочетая возможности стека и очереди.
     */
    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    /* реализован дек на основе циклического массива, где индексы head и tail перемещаются по кругу */
    @SuppressWarnings("unchecked")
    private void resize() {
        // Увеличиваем емкость в 2 раза
        int newCapacity = elements.length * 2;
        E[] newElements = (E[]) new Object[newCapacity];
        /*При добавлении элементов в начало (addFirst) или конец (addLast) используется
    арифметика по модулю для циклического перемещения индексов.
     */
        int current = head;
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[current];
            current = (current + 1) % elements.length;
        }
    /* При заполнении массива автоматически вызывается метод resize(),
    который увеличивает емкость в 2 раза и перераспределяет элементы, начиная с головы дека для оптимального расположения.
    */
        elements = newElements;
        head = 0;
        tail = size;
    }
    /*Все ключевые операции (добавление, удаление, получение элементов с обоих концов)
     работают за амортизированное константное время O(1), а итератор корректно обходит элементы
     с учетом циклической природы массива.
     */

    // ==================== ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ ДЛЯ УРОВНЯ A ====================

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
        int current = head;
        for (int i = 0; i < size; i++) {
            sb.append(elements[current]);
            if (i < size - 1) {
                sb.append(", ");
            }
            // Переход к следующему элементу с учетом цикличности массива
            current = (current + 1) % elements.length;
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
     */
    @Override
    public void addFirst(E element) {
        // Если массив заполнен, увеличиваем его размер
        if (size == elements.length) {
            resize();
        }

        // Сдвигаем head назад с учетом цикличности
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    /**
     * Добавляет элемент в конец дека
     * @param element добавляемый элемент
     */
    @Override
    public void addLast(E element) {
        // Если массив заполнен, увеличиваем его размер
        if (size == elements.length) {
            resize();
        }

        // Добавляем элемент в текущую позицию tail и сдвигаем tail вперед
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        size++;
    }

    /**
     * Возвращает первый элемент без удаления
     * @return первый элемент дека
     * @throws NoSuchElementException если дек пуст
     */
    @Override
    public E element() {
        return getFirst();
    }

    /**
     * Возвращает первый элемент без удаления
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
     * Возвращает последний элемент без удаления
     * @return последний элемент дека
     * @throws NoSuchElementException если дек пуст
     */
    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        // tail указывает на следующую свободную позицию, поэтому последний элемент находится перед tail
        return elements[(tail - 1 + elements.length) % elements.length];
    }

    /**
     * Удаляет и возвращает первый элемент
     * @return первый элемент или null, если дек пуст
     */
    @Override
    public E poll() {
        return pollFirst();
    }

    /**
     * Удаляет и возвращает первый элемент
     * @return первый элемент или null, если дек пуст
     */
    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }

        // Сохраняем элемент для возврата
        E element = elements[head];
        // Освобождаем ссылку для сборщика мусора
        elements[head] = null;
        // Сдвигаем head вперед с учетом цикличности
        head = (head + 1) % elements.length;
        size--;
        return element;
    }

    /**
     * Удаляет и возвращает последний элемент
     * @return последний элемент или null, если дек пуст
     */
    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }

        // Сдвигаем tail назад с учетом цикличности (tail указывает на следующую свободную позицию)
        tail = (tail - 1 + elements.length) % elements.length;
        // Сохраняем элемент для возврата
        E element = elements[tail];
        // Освобождаем ссылку для сборщика мусора
        elements[tail] = null;
        size--;
        return element;
    }

    // ==================== ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ ====================

    /**
     * Добавляет элемент в конец дека (аналогично add)
     */
    @Override
    public boolean offer(E e) {
        return add(e);
    }

    /**
     * Удаляет и возвращает первый элемент
     * @return первый элемент дека
     * @throws NoSuchElementException если дек пуст
     */
    @Override
    public E remove() {
        E result = poll();
        if (result == null) {
            throw new NoSuchElementException();
        }
        return result;
    }

    /**
     * Возвращает первый элемент без удаления
     * @return первый элемент или null, если дек пуст
     */
    @Override
    public E peek() {
        return peekFirst();
    }

    /**
     * Добавляет элемент в начало дека
     */
    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    /**
     * Добавляет элемент в конец дека
     */
    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    /**
     * Удаляет и возвращает первый элемент
     * @throws NoSuchElementException если дек пуст
     */
    @Override
    public E removeFirst() {
        E result = pollFirst();
        if (result == null) {
            throw new NoSuchElementException();
        }
        return result;
    }

    /**
     * Удаляет и возвращает последний элемент
     * @throws NoSuchElementException если дек пуст
     */
    @Override
    public E removeLast() {
        E result = pollLast();
        if (result == null) {
            throw new NoSuchElementException();
        }
        return result;
    }

    /**
     * Возвращает первый элемент без удаления
     * @return первый элемент или null, если дек пуст
     */
    @Override
    public E peekFirst() {
        return (size == 0) ? null : elements[head];
    }

    /**
     * Возвращает последний элемент без удаления
     * @return последний элемент или null, если дек пуст
     */
    @Override
    public E peekLast() {
        return (size == 0) ? null : elements[(tail - 1 + elements.length) % elements.length];
    }

    /**
     * Проверяет, пуст ли дек
     * @return true если дек пуст
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // ==================== НЕРЕАЛИЗОВАННЫЕ МЕТОДЫ ====================

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * Возвращает итератор для обхода элементов дека от первого к последнему
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int current = head;    // Текущая позиция в массиве
            private int count = 0;         // Количество пройденных элементов

            @Override
            public boolean hasNext() {
                return count < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                E element = elements[current];
                // Переход к следующему элементу с учетом цикличности
                current = (current + 1) % elements.length;
                count++;
                return element;
            }
        };
    }

    /**
     * Преобразует дек в массив объектов
     */
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int current = head;
        for (int i = 0; i < size; i++) {
            array[i] = elements[current];
            current = (current + 1) % elements.length;
        }
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException();
    }

    /**
     * Добавляет элемент в начало дека (аналогично addFirst)
     */
    @Override
    public void push(E e) {
        addFirst(e);
    }

    /**
     * Удаляет и возвращает первый элемент (аналогично removeFirst)
     */
    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}

/**
 * Исключение для случаев, когда дек пуст
 * Используется вместо java.util.NoSuchElementException для соответствия условиям задания
 */
class NoSuchElementException extends RuntimeException {
    public NoSuchElementException() {
        super();
    }

    public NoSuchElementException(String message) {
        super(message);
    }
}