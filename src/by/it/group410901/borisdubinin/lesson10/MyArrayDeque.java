package by.it.group410901.borisdubinin.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {
    private E[] array;    // Массив для хранения элементов
    private int head;        // Индекс первого элемента
    private int tail;        // Индекс следующего за последним элементом
    private int size;        // Текущее количество элементов
    private static final int DEFAULT_CAPACITY = 16;  // Начальная емкость по умолчанию

    // Конструктор с заданной емкостью
    @SuppressWarnings("unchecked")
    public MyArrayDeque(int capacity) {
        if (capacity < 1) capacity = DEFAULT_CAPACITY;  // Минимальная емкость
        array = (E[]) new Object[capacity];
        head = 0;
        tail = 0;
        size = 0;
    }

    // Конструктор по умолчанию
    public MyArrayDeque() {
        this(DEFAULT_CAPACITY);
    }

    // Возвращает текущую емкость массива
    private int cap() { return array.length; }

    // Проверяет необходимость увеличения массива
    private void ensureCapacity() {
        if (size == cap()) {
            grow();  // Увеличиваем массив при заполнении
        }
    }

    // Увеличивает размер массива в 2 раза
    @SuppressWarnings("unchecked")
    private void grow() {
        int old = cap();      // Текущая емкость
        int n = old << 1;     // Новая емкость = старая * 2 (битовый сдвиг влево)
        E[] nArr = (E[]) new Object[n];

        // Копируем элементы в новый массив, сохраняя порядок
        for (int i = 0; i < size; i++) {
            nArr[i] = array[(head + i) % old];  // Учитываем циклический буфер
        }
        array = nArr;
        head = 0;            // Голова теперь в начале
        tail = size % n;     // Хвост после последнего элемента
    }

    // Преобразование дека в строку для вывода
    @Override
    public String toString() {
        if (size == 0) return "[]";  // Пустой дек
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        // Проходим по всем элементам в порядке от head к tail
        for (int i = 0; i < size; i++) {
            E e = array[(head + i) % cap()];  // Учитываем циклический буфер
            sb.append(String.valueOf(e));
            if (i != size - 1) sb.append(", ");  // Запятая между элементами
        }
        sb.append(']');
        return sb.toString();
    }

    // Возвращает количество элементов в деке
    @Override
    public int size() {
        return size;
    }

    // Добавление элемента в конец дека
    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    // Добавление элемента в начало дека
    @Override
    public void addFirst(E element) {
        if (element == null) throw new NullPointerException();  // Null не допускается
        ensureCapacity();  // Проверяем/увеличиваем емкость
        // Сдвигаем head назад по кругу (циклический буфер)
        head = (head - 1 + cap()) % cap();
        array[head] = element;
        size++;
    }

    // Добавление элемента в конец дека
    @Override
    public void addLast(E element) {
        if (element == null) throw new NullPointerException();  // Null не допускается
        ensureCapacity();  // Проверяем/увеличиваем емкость
        array[tail] = element;
        // Сдвигаем tail вперед по кругу (циклический буфер)
        tail = (tail + 1) % cap();
        size++;
    }

    // Получение первого элемента без удаления (с исключением если пусто)
    @Override
    public E element() {
        return getFirst();
    }

    // Получение первого элемента
    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return array[head];
    }

    // Получение последнего элемента
    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        // tail указывает на следующую позицию, поэтому берем предыдущую
        int idx = (tail - 1 + cap()) % cap();
        return array[idx];
    }

    // Извлечение первого элемента с удалением (возвращает null если пусто)
    @Override
    public E poll() {
        return pollFirst();
    }

    // Извлечение первого элемента
    @Override
    public E pollFirst() {
        if (size == 0) return null;  // Пустой дек - возвращаем null
        E r = array[head];        // Сохраняем элемент
        array[head] = null;       // Очищаем ячейку (помощь GC)
        // Сдвигаем head вперед по кругу
        head = (head + 1) % cap();
        size--;
        // Если дек стал пустым, сбрасываем индексы
        if (size == 0) { head = tail = 0; }
        return r;
    }

    // Извлечение последнего элемента
    @Override
    public E pollLast() {
        if (size == 0) return null;  // Пустой дек - возвращаем null
        // Сдвигаем tail назад (tail указывает на следующую позицию)
        tail = (tail - 1 + cap()) % cap();
        E r = array[tail];        // Сохраняем элемент
        array[tail] = null;       // Очищаем ячейку (помощь GC)
        size--;
        // Если дек стал пустым, сбрасываем индексы
        if (size == 0) { head = tail = 0; }
        return r;
    }

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