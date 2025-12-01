package by.it.group451002.jasko.lesson10;

import java.util.Deque;
import java.util.NoSuchElementException;

// Реализовать двустороннюю очередь (Deque) на основе массива с кольцевой буферизацией
// Функции:
//- addFirst/Last - добавление в начало/конец
//- pollFirst/Last - удаление из начала/конца
//- getFirst/Last - получение первого/последнего
//- element/peek - просмотр первого элемента

public class MyArrayDeque<E> implements Deque<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private E[] elements;    // Основной массив для хранения элементов
    private int head;        // Индекс первого элемента в деке
    private int tail;        // Индекс последнего элемента в деке
    private int size;        // Текущее количество элементов

    // Конструктор по умолчанию
    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    // Конструктор с заданной начальной емкостью
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

    // Увеличивает емкость массива при необходимости (динамическое расширение)
    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = elements.length * 2;
            E[] newElements = (E[]) new Object[newCapacity];

            // Перекладываем элементы в новый массив, начиная с индекса 0
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[(head + i) % elements.length];
            }

            elements = newElements;
            head = 0;
            tail = size - 1; // tail теперь указывает на последний элемент
        }
    }

    // Строковое представление дека в формате [element1, element2, ...]
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        // Обходим элементы в порядке от head к tail
        for (int i = 0; i < size; i++) {
            E element = elements[(head + i) % elements.length];
            sb.append(element);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // Текущий размер дека
    @Override
    public int size() {
        return size;
    }

    // Добавление элемента в конец (аналогично addLast)
    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    // Добавление элемента в начало дека
    @Override
    public void addFirst(E element) {
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }

        ensureCapacity(); // Проверяем, нужно ли расширять массив

        if (size == 0) {
            // Если дек пуст, просто кладем элемент
            elements[head] = element;
            tail = head; // head и tail указывают на один элемент
        } else {
            // Сдвигаем head назад по кольцу и добавляем элемент
            head = (head - 1 + elements.length) % elements.length;
            elements[head] = element;
        }
        size++;
    }

    // Добавление элемента в конец дека
    @Override
    public void addLast(E element) {
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }

        ensureCapacity(); // Проверяем, нужно ли расширять массив

        if (size == 0) {
            // Если дек пуст, просто кладем элемент
            elements[tail] = element;
        } else {
            // Сдвигаем tail вперед по кольцу и добавляем элемент
            tail = (tail + 1) % elements.length;
            elements[tail] = element;
        }
        size++;
    }

    // Получение первого элемента без удаления (бросает исключение если пусто)
    @Override
    public E element() {
        return getFirst();
    }

    // Получение первого элемента без удаления
    @Override
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        return elements[head];
    }

    // Получение последнего элемента без удаления
    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        return elements[tail];
    }

    // Удаление и возврат первого элемента (возвращает null если пусто)
    @Override
    public E poll() {
        return pollFirst();
    }

    // Удаление и возврат первого элемента
    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }

        E element = elements[head];
        elements[head] = null; // Помощь сборщику мусора

        if (size == 1) {
            // Если был один элемент, сбрасываем индексы
            head = 0;
            tail = 0;
        } else {
            // Сдвигаем head вперед по кольцу
            head = (head + 1) % elements.length;
        }
        size--;

        return element;
    }

    // Удаление и возврат последнего элемента
    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }

        E element = elements[tail];
        elements[tail] = null; // Помощь сборщику мусора

        if (size == 1) {
            // Если был один элемент, сбрасываем индексы
            head = 0;
            tail = 0;
        } else {
            // Сдвигаем tail назад по кольцу
            tail = (tail - 1 + elements.length) % elements.length;
        }
        size--;

        return element;
    }

    /////////////////////////////////////////////////////////////////////////
    //////     Остальные методы интерфейса Deque (необязательные)    ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
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
        return remove();
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
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
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
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
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
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        while (size > 0) {
            pollFirst();
        }
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
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
    public Object[] toArray() {
        Object[] array = new Object[size];
        for (int i = 0; i < size; i++) {
            array[i] = elements[(head + i) % elements.length];
        }
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        for (int i = 0; i < size; i++) {
            a[i] = (T) elements[(head + i) % elements.length];
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}