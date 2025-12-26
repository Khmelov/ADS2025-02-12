package by.it.group451001.yarkovich.lesson10;

import java.util.Deque;
import java.util.Iterator;

public class MyArrayDeque<E> implements Deque<E> {

    // Внутренний массив для хранения элементов дека
    // Дек - это двусторонняя очередь, которая поддерживает добавление/удаление с обоих концов
    private E[] elements;

    // Текущее количество элементов в деке (не путать с емкостью массива)
    // size показывает сколько фактически элементов хранится в деке
    private int size;

    // Индекс головы дека (первого элемента)
    // Голова - это элемент, который будет удален при pollFirst()
    private int head;

    // Индекс хвоста дека (следующая позиция для добавления)
    // Хвост - это позиция, куда будет добавлен следующий элемент при addLast()
    private int tail;

    // Начальная емкость массива по умолчанию
    // Выбрана 10 как стандартный размер для многих коллекций Java
    private static final int DEFAULT_CAPACITY = 10;

    // Конструктор класса MyArrayDeque
    // Аннотация подавляет предупреждение о непроверяемом приведении типа
    // Это необходимо потому что мы создаем массив Object[] и приводим к E[]
    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        // Создаем массив объектов и приводим к типу E[]
        // Это стандартный подход в Java дженериках при работе с массивами
        elements = (E[]) new Object[DEFAULT_CAPACITY];

        // Инициализируем размер дека как 0 (дек пуст)
        size = 0;

        // Начальная позиция головы - 0 (начало массива)
        head = 0;

        // Начальная позиция хвоста - 0 (следующий элемент добавится в позицию 0)
        tail = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    // Метод для строкового представления дека
    // Возвращает строку в формате [element1, element2, element3, ...]
    @Override
    public String toString() {
        // Если дек пуст, возвращаем пустые квадратные скобки
        if (size == 0) {
            return "[]";
        }

        // StringBuilder используется для эффективного построения строки
        // (лучше чем конкатенация строк через "+")
        StringBuilder sb = new StringBuilder();
        sb.append("["); // Начинаем с открывающей скобки

        // Проходим по всем элементам в правильном порядке (от головы к хвосту)
        // i - это относительный индекс в логическом представлении дека
        for (int i = 0; i < size; i++) {
            // Вычисляем физический индекс в массиве с учетом цикличности
            // Формула (head + i) % elements.length обеспечивает циклический обход массива
            // % - оператор modulo, который "заворачивает" индекс когда он достигает конца массива
            int index = (head + i) % elements.length;

            // Добавляем строковое представление элемента
            sb.append(elements[index]);

            // Добавляем запятую и пробел после всех элементов кроме последнего
            if (i < size - 1) {
                sb.append(", ");
            }
        }

        sb.append("]"); // Закрывающая скобка
        return sb.toString(); // Преобразуем StringBuilder в String и возвращаем
    }

    // Метод возвращает текущее количество элементов в деке
    // Время выполнения: O(1) - просто возвращаем значение поля
    @Override
    public int size() {
        return size;
    }

    // Метод add добавляет элемент в конец дека (эквивалентно addLast)
    // Возвращает true если элемент успешно добавлен (всегда true для этой реализации)
    // Это стандартное поведение для коллекций в Java
    @Override
    public boolean add(E element) {
        // ИСПРАВЛЕНИЕ: addLast() возвращает void, поэтому не можем возвращать его результат
        // Вместо этого вызываем addLast() и возвращаем true
        addLast(element);
        return true;
    }

    // Метод добавляет элемент в начало дека (в голову)
    // В деке можно добавлять элементы с обоих концов
    @Override
    public void addFirst(E element) {
        // Проверяем, заполнен ли массив полностью
        // Если size == elements.length, значит нет свободного места
        if (size == elements.length) {
            // Вызываем метод resize() для увеличения емкости массива
            resize();
        }

        // Перемещаем указатель head на предыдущую позицию с учетом цикличности
        // (head - 1 + elements.length) % elements.length обеспечивает:
        // - уменьшение head на 1
        // - если head был 0, то после вычисления станет elements.length - 1 (циклический переход)
        // + elements.length нужно чтобы избежать отрицательных чисел при операции modulo
        head = (head - 1 + elements.length) % elements.length;

        // Помещаем новый элемент в новую позицию head
        elements[head] = element;

        // Увеличиваем счетчик элементов
        size++;
    }

    // Метод добавляет элемент в конец дека (в хвост)
    @Override
    public void addLast(E element) {
        // Проверяем, заполнен ли массив полностью
        if (size == elements.length) {
            // Увеличиваем емкость если нужно
            resize();
        }

        // Помещаем новый элемент в текущую позицию tail
        elements[tail] = element;

        // Перемещаем указатель tail на следующую позицию с учетом цикличности
        // (tail + 1) % elements.length обеспечивает:
        // - увеличение tail на 1
        // - если tail был в конце массива, переход на позицию 0
        tail = (tail + 1) % elements.length;

        // Увеличиваем счетчик элементов
        size++;
    }

    // Метод возвращает первый элемент дека без его удаления (эквивалентно getFirst)
    // Бросает исключение если дек пуст
    @Override
    public E element() {
        // Делегируем вызов методу getFirst
        return getFirst();
    }

    // Метод возвращает первый элемент дека без его удаления
    // Бросает исключение если дек пуст
    @Override
    public E getFirst() {
        // Проверяем, пуст ли дек
        if (size == 0) {
            // Бросаем исключение NoSuchElementException как требует контракт интерфейса Deque
            throw new java.util.NoSuchElementException("Deque is empty");
        }

        // Возвращаем элемент в позиции head
        // head всегда указывает на первый элемент дека
        return elements[head];
    }

    // Метод возвращает последний элемент дека без его удаления
    // Бросает исключение если дек пуст
    @Override
    public E getLast() {
        // Проверяем, пуст ли дек
        if (size == 0) {
            throw new java.util.NoSuchElementException("Deque is empty");
        }

        // Вычисляем позицию последнего элемента
        // (tail - 1 + elements.length) % elements.length находит индекс последнего добавленного элемента
        // tail указывает на СЛЕДУЮЩУЮ позицию для добавления, поэтому последний элемент на tail-1
        int lastIndex = (tail - 1 + elements.length) % elements.length;

        // Возвращаем элемент в вычисленной позиции
        return elements[lastIndex];
    }

    // Метод удаляет и возвращает первый элемент дека (эквивалентно pollFirst)
    // Возвращает null если дек пуст (в отличие от removeFirst который бросает исключение)
    @Override
    public E poll() {
        // Делегируем вызов методу pollFirst
        return pollFirst();
    }

    // Метод удаляет и возвращает первый элемент дека
    // Возвращает null если дек пуст
    @Override
    public E pollFirst() {
        // Проверяем, пуст ли дек
        if (size == 0) {
            // Возвращаем null как требует контракт метода pollFirst
            return null;
        }

        // Сохраняем элемент который будем удалять (для возврата)
        E removedElement = elements[head];

        // Очищаем позицию head (помещаем null для помощи сборщику мусора)
        elements[head] = null;

        // Перемещаем указатель head на следующую позицию с учетом цикличности
        head = (head + 1) % elements.length;

        // Уменьшаем счетчик элементов
        size--;

        // Возвращаем удаленный элемент
        return removedElement;
    }

    // Метод удаляет и возвращает последний элемент дека
    // Возвращает null если дек пуст
    @Override
    public E pollLast() {
        // Проверяем, пуст ли дек
        if (size == 0) {
            return null;
        }

        // Вычисляем позицию последнего элемента (который будем удалять)
        // tail указывает на следующую позицию для добавления, поэтому последний элемент на tail-1
        int lastIndex = (tail - 1 + elements.length) % elements.length;

        // Сохраняем элемент который будем удалять
        E removedElement = elements[lastIndex];

        // Очищаем позицию
        elements[lastIndex] = null;

        // Перемещаем указатель tail на предыдущую позицию
        tail = lastIndex;

        // Уменьшаем счетчик элементов
        size--;

        // Возвращаем удаленный элемент
        return removedElement;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы                      ///////
    /////////////////////////////////////////////////////////////////////////

    // Приватный метод для увеличения емкости массива когда он заполнен
    // Создает новый массив в 2 раза больше и копирует все элементы в правильном порядке
    @SuppressWarnings("unchecked")
    private void resize() {
        // Вычисляем новую емкость (удваиваем текущую)
        int newCapacity = elements.length * 2;

        // Создаем новый массив увеличенного размера
        E[] newElements = (E[]) new Object[newCapacity];

        // Копируем элементы из старого массива в новый в правильном порядке
        // i - индекс в старом массиве, j - индекс в новом массиве
        for (int i = 0, j = head; i < size; i++) {
            // Копируем элемент из старого массива
            newElements[i] = elements[j];

            // Переходим к следующему элементу в старом массиве с учетом цикличности
            j = (j + 1) % elements.length;
        }

        // Заменяем старый массив новым
        elements = newElements;

        // Обновляем указатели: голова теперь в позиции 0
        head = 0;

        // Хвост теперь указывает на позицию size (следующую за последним элементом)
        tail = size;
    }

    /////////////////////////////////////////////////////////////////////////
    //////       Остальные методы интерфейса Deque (заглушки)       ///////
    /////////////////////////////////////////////////////////////////////////

    // Эти методы не требуются по заданию, но должны быть реализованы
    // поскольку класс реализует интерфейс Deque

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean offer(E e) {
        // ИСПРАВЛЕНИЕ: addLast() возвращает void, поэтому не можем возвращать его результат
        // Вместо этого вызываем addLast() и возвращаем true
        addLast(e);
        return true;
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
        if (size == 0) throw new java.util.NoSuchElementException();
        return pollFirst();
    }

    @Override
    public E removeFirst() {
        if (size == 0) throw new java.util.NoSuchElementException();
        return pollFirst();
    }

    @Override
    public E removeLast() {
        if (size == 0) throw new java.util.NoSuchElementException();
        return pollLast();
    }

    @Override
    public E peek() {
        return (size == 0) ? null : getFirst();
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
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> descendingIterator() {
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

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}