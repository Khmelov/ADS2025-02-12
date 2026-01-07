package by.it.group410902.andala.lesson10;
import java.util.Deque;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {
    // похоже на С, но тут кольцевая очередь. элементы подряд в массиве с индексами head и tail. этот хорош для быстрого поиска

    // Минимальная начальная ёмкость
    private static final int DEFAULT_CAPACITY = 10;

    // Внутренний массив для хранения элементов
    private E[] elements;

    // Индекс первого (головного) элемента в массиве
    private int head;

    // Индекс "свободной" позиции сразу после последнего элемента (tail указывает на место для следующего addLast)
    private int tail;

    // Текущее количество элементов в дека
    private int size;

    // Конструктор: создаём массив DEFAULT_CAPACITY и инициализируем индексы
    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    // --------------------
    // РАСШИРЕНИЕ/СЖАТИЕ МАССИВА
    // --------------------

    // Увеличиваем ёмкость вдвое, когда массив полностью заполнен
    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == elements.length) { // если нет свободного места
            int newCapacity = elements.length * 2;
            E[] newElements = (E[]) new Object[newCapacity];

            // Копируем элементы в новый массив подряд начиная с head.
            // Учитываем два случая: данные идут подряд (head < tail) или "разорваны" по кругу (head >= tail).
            if (head < tail) {
                // Простое копирование одного блока
                System.arraycopy(elements, head, newElements, 0, size);
            } else {
                // Копируем сначала от head до конца массива, затем от начала до tail
                System.arraycopy(elements, head, newElements, 0, elements.length - head);
                System.arraycopy(elements, 0, newElements, elements.length - head, tail);
            }

            // Заменяем массив и восстанавливаем инвариант: элементы начинаются с 0, tail = size
            elements = newElements;
            head = 0;
            tail = size;
        }
    }

    // Уменьшаем ёмкость вдвое, когда размер упал до четверти длины массива
    @SuppressWarnings("unchecked")
    private void trimToSize() {
        // Сжимаем только если в массиве достаточно пустого места и размер > 0
        if (size > 0 && size == elements.length / 4) {
            int newCapacity = Math.max(DEFAULT_CAPACITY, elements.length / 2);
            E[] newElements = (E[]) new Object[newCapacity];

            // Копирование как в ensureCapacity
            if (head < tail) {
                System.arraycopy(elements, head, newElements, 0, size);
            } else {
                System.arraycopy(elements, head, newElements, 0, elements.length - head);
                System.arraycopy(elements, 0, newElements, elements.length - head, tail);
            }

            elements = newElements;
            head = 0;
            tail = size;
        }
    }

    // --------------------
    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
    // --------------------

    // Корректно переводит индекс в диапазон [0, elements.length)
    private int wrapIndex(int index) {
        return (index + elements.length) % elements.length;
    }

    // --------------------
    // ВЫВОД, РАЗМЕР
    // --------------------

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        // Пробегаем от head по size элементов, корректируя индекс через wrapIndex
        StringBuilder sb = new StringBuilder("[");
        int current = head;
        for (int i = 0; i < size; i++) {
            sb.append(elements[current]);
            if (i < size - 1) {
                sb.append(", ");
            }
            current = wrapIndex(current + 1);
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    // --------------------
    // ДОБАВЛЕНИЕ
    // --------------------

    @Override
    public boolean add(E element) {
        addLast(element); // по контракту Deque.add добавляет в конец
        return true;
    }

    // Добавить в начало дека
    @Override
    public void addFirst(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        ensureCapacity();              // расширяем при необходимости
        head = wrapIndex(head - 1);    // сдвигаем head назад по кругу
        elements[head] = element;      // записываем элемент в новую голову
        size++;
    }

    // Добавить в конец дека
    @Override
    public void addLast(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        ensureCapacity();          // расширяем при необходимости
        elements[tail] = element;  // записываем в позицию tail
        tail = wrapIndex(tail + 1);// перемещаем tail на следующую свободную позицию
        size++;
    }

    // --------------------
    // ДОСТУП К ЭЛЕМЕНТАМ (ПЕРВЫЙ/ПОСЛЕДНИЙ)
    // --------------------

    @Override
    public E element() {
        return getFirst(); // кидает исключение если пусто
    }

    @Override
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        return elements[head];
    }

    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        // tail указывает на следующую свободную позицию, поэтому последний элемент — tail-1
        return elements[wrapIndex(tail - 1)];
    }

    // --------------------
    // УДАЛЕНИЕ (POLL / REMOVE)
    // --------------------

    @Override
    public E poll() {
        return pollFirst(); // возвращает null при пустой структуре
    }

    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }

        E element = elements[head];      // сохраняем элемент головы
        elements[head] = null;          // убираем ссылку для GC
        head = wrapIndex(head + 1);     // сдвигаем head вперёд
        size--;

        trimToSize();                   // возможно сжимаем массив
        return element;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }

        tail = wrapIndex(tail - 1);     // перемещаем tail назад к последнему элементу
        E element = elements[tail];     // сохраняем последний элемент
        elements[tail] = null;          // убираем ссылку для GC
        size--;

        trimToSize();                   // возможно сжимаем массив
        return element;
    }

    // --------------------
    // ПРОЧИЕ ПРИВЫЧНЫЕ МЕТОДЫ Deque
    // --------------------

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // offer/offerFirst/offerLast — безопасные версии add* (обычно возвращают false при переполнении)
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

    // remove / removeFirst / removeLast — кидают NoSuchElementException, если пусто
    @Override
    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return pollFirst();
    }

    @Override
    public E removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return pollFirst();
    }

    @Override
    public E removeLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return pollLast();
    }

    // peek / peekFirst / peekLast — возвращают элемент или null, не удаляют
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

    // push / pop — стекоподобные операции: push = addFirst, pop = removeFirst
    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    // --------------------
    // МЕТОДЫ, КОТОРЫЕ ПОКА НЕ РЕАЛИЗОВАНЫ
    // --------------------
    // Здесь выбрасывается UnsupportedOperationException. Они требуют дополнительной логики:
    // - remove(Object), contains(Object) — линейный поиск по всем элементам
    // - iterator(), descendingIterator() — создание итераторов, проходящих по кругу от head
    // - removeFirstOccurrence/removeLastOccurrence — найти и удалить конкретное вхождение
    // - addAll, clear, containsAll, removeAll, retainAll, toArray — стандартные методы Collection/List
    // Их реализация возможна, но в учебном варианте можно добавить по необходимости.

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
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}
