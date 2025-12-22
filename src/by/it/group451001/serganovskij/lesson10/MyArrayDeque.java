package by.it.group451001.serganovskij.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    // ВНУТРЕННИЙ МАССИВ для хранения элементов дека
    private E[] elements;

    // ИНДЕКСЫ: head - первый элемент, tail - позиция для следующего добавления
    private int head;
    private int tail;

    // ТЕКУЩЕЕ КОЛИЧЕСТВО элементов в деке
    private int size;

    // СТАНДАРТНАЯ ВМЕСТИМОСТЬ по умолчанию
    private static final int DEFAULT_CAP = 16;

    // КОНСТРУКТОР с указанием вместимости
    @SuppressWarnings("unchecked")
    public MyArrayDeque(int capacity) {
        // Проверка на минимальную вместимость
        if (capacity < 1) capacity = DEFAULT_CAP;

        // Создание массива нужного размера
        elements = (E[]) new Object[capacity];

        // Инициализация индексов и размера
        head = 0;
        tail = 0;
        size = 0;
    }

    // КОНСТРУКТОР по умолчанию
    public MyArrayDeque() {
        this(DEFAULT_CAP);
    }

    // ПОЛУЧЕНИЕ ТЕКУЩЕЙ ВМЕСТИМОСТИ массива
    private int cap() {
        return elements.length;
    }

    // ПРОВЕРКА И УВЕЛИЧЕНИЕ ВМЕСТИМОСТИ при необходимости
    private void ensureCapacity() {
        if (size == cap()) {
            grow(); // Увеличиваем массив если он заполнен
        }
    }

    // УВЕЛИЧЕНИЕ РАЗМЕРА МАССИВА в 2 раза
    @SuppressWarnings("unchecked")
    private void grow() {
        int old = cap(); // Текущая вместимость
        int n = old << 1; // Новая вместимость (в 2 раза больше)

        // Создание нового массива
        E[] nArr = (E[]) new Object[n];

        // Копирование элементов с сохранением порядка
        for (int i = 0; i < size; i++) {
            nArr[i] = elements[(head + i) % old];
        }

        // Замена старого массива новым
        elements = nArr;

        // Обновление индексов
        head = 0;
        tail = size % n;
    }

    /////////////////////////////////////////////////////////////////////////
    // ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ ДЛЯ РЕАЛИЗАЦИИ
    /////////////////////////////////////////////////////////////////////////

    // СТРОКОВОЕ ПРЕДСТАВЛЕНИЕ дека в формате [элемент1, элемент2, ...]
    @Override
    public String toString() {
        if (size == 0) return "[]"; // Пустой дек

        StringBuilder sb = new StringBuilder();
        sb.append('[');

        // Обход всех элементов в порядке от head к tail
        for (int i = 0; i < size; i++) {
            E e = elements[(head + i) % cap()];
            sb.append(String.valueOf(e));
            if (i != size - 1) sb.append(", "); // Запятая между элементами
        }

        sb.append(']');
        return sb.toString();
    }

    // ПОЛУЧЕНИЕ РАЗМЕРА дека
    @Override
    public int size() {
        return size;
    }

    // ДОБАВЛЕНИЕ ЭЛЕМЕНТА В КОНЕЦ (аналогично addLast)
    @Override
    public boolean add(E element) {
        addLast(element);
        return true; // Всегда возвращает true для деков
    }

    // ДОБАВЛЕНИЕ ЭЛЕМЕНТА В НАЧАЛО дека
    @Override
    public void addFirst(E element) {
        if (element == null) throw new NullPointerException(); // Null не допускается

        ensureCapacity(); // Проверка вместимости

        // Сдвиг head назад (с учетом кругового массива)
        head = (head - 1 + cap()) % cap();
        elements[head] = element;
        size++;
    }

    // ДОБАВЛЕНИЕ ЭЛЕМЕНТА В КОНЕЦ дека
    @Override
    public void addLast(E element) {
        if (element == null) throw new NullPointerException(); // Null не допускается

        ensureCapacity(); // Проверка вместимости

        elements[tail] = element;
        // Сдвиг tail вперед (с учетом кругового массива)
        tail = (tail + 1) % cap();
        size++;
    }

    // ПОЛУЧЕНИЕ ПЕРВОГО ЭЛЕМЕНТА (без удаления)
    @Override
    public E element() {
        return getFirst(); // Синоним getFirst
    }

    // ПОЛУЧЕНИЕ ПЕРВОГО ЭЛЕМЕНТА
    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException(); // Пустой дек
        return elements[head];
    }

    // ПОЛУЧЕНИЕ ПОСЛЕДНЕГО ЭЛЕМЕНТА
    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException(); // Пустой дек

        // Вычисление индекса последнего элемента
        int idx = (tail - 1 + cap()) % cap();
        return elements[idx];
    }

    // ИЗВЛЕЧЕНИЕ ПЕРВОГО ЭЛЕМЕНТА (с удалением)
    @Override
    public E poll() {
        return pollFirst(); // Синоним pollFirst
    }

    // ИЗВЛЕЧЕНИЕ ПЕРВОГО ЭЛЕМЕНТА
    @Override
    public E pollFirst() {
        if (size == 0) return null; // Пустой дек - возвращаем null

        E r = elements[head]; // Сохраняем элемент
        elements[head] = null; // Очищаем ячейку

        // Сдвиг head вперед
        head = (head + 1) % cap();
        size--;

        // Сброс индексов если дек пуст
        if (size == 0) {
            head = tail = 0;
        }

        return r;
    }

    // ИЗВЛЕЧЕНИЕ ПОСЛЕДНЕГО ЭЛЕМЕНТА
    @Override
    public E pollLast() {
        if (size == 0) return null; // Пустой дек - возвращаем null

        // Сдвиг tail назад
        tail = (tail - 1 + cap()) % cap();
        E r = elements[tail]; // Сохраняем элемент
        elements[tail] = null; // Очищаем ячейку

        size--;

        // Сброс индексов если дек пуст
        if (size == 0) {
            head = tail = 0;
        }

        return r;
    }

    /////////////////////////////////////////////////////////////////////////
    // НЕРЕАЛИЗОВАННЫЕ МЕТОДЫ (заглушки)
    /////////////////////////////////////////////////////////////////////////

    @Override public boolean offerFirst(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean offerLast(E e)  { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e)      { throw new UnsupportedOperationException(); }
    @Override public E peek()                { throw new UnsupportedOperationException(); }
    @Override public E peekFirst()           { throw new UnsupportedOperationException(); }
    @Override public E peekLast()            { throw new UnsupportedOperationException(); }
    @Override public E remove()              { throw new UnsupportedOperationException(); }
    @Override public E removeFirst()         { throw new UnsupportedOperationException(); }
    @Override public E removeLast()          { throw new UnsupportedOperationException(); }
    @Override public void push(E e)          { throw new UnsupportedOperationException(); }
    @Override public E pop()                 { throw new UnsupportedOperationException(); }

    @Override public boolean remove(Object o)                       { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o)                     { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> iterator()                         { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator()               { throw new UnsupportedOperationException(); }
    @Override public boolean isEmpty()                              { throw new UnsupportedOperationException(); }
    @Override public void clear()                                   { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c)           { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c)      { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c)             { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c)             { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray()                             { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a)                         { throw new UnsupportedOperationException(); }
    @Override public boolean removeFirstOccurrence(Object o)        { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o)         { throw new UnsupportedOperationException(); }
}
