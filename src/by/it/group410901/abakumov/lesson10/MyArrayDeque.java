package by.it.group410901.abakumov.lesson10;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class MyArrayDeque<E> implements Deque<E> {

    // Внутренний массив для хранения элементов
    private E[] elements;

    // Индекс первого элемента в деке (голова)
    private int head;

    // Индекс позиции после последнего элемента (хвост)
    private int tail;

    // Текущее количество элементов в деке
    private int size;

    // Начальная емкость массива по умолчанию
    private static final int DEFAULT_CAPACITY = 10;

    // Конструктор: инициализирует пустой дек с начальной емкостью
    public MyArrayDeque() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    // Увеличивает емкость массива при необходимости
    // Если массив полон, создаем новый в 2 раза больше и копируем элементы линейно
    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = elements.length * 2;
            E[] newArr = (E[]) new Object[newCapacity];
            // Переносим элементы в новый массив, учитывая кольцевую структуру (wrap-around)
            // Начинаем с head и идем по модулю, чтобы собрать элементы в последовательном порядке
            for (int i = 0; i < size; i++) {
                newArr[i] = elements[(head + i) % elements.length];
            }
            elements = newArr;
            // После переноса голова в начале, хвост в конце нового линейного массива
            head = 0;
            tail = size;
        }
    }

    // Возвращает строковое представление дека в формате [elem1, elem2, ...]
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        // Перебираем элементы от головы к хвосту с учетом кольцевой структуры
        for (int i = 0; i < size; i++) {
            sb.append(elements[(head + i) % elements.length]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // Возвращает текущее количество элементов в деке
    @Override
    public int size() {
        return size;
    }

    // Добавляет элемент в конец дека (эквивалентно addLast)
    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    // Добавляет элемент в начало дека
    // Сдвигаем голову назад по модулю (чтобы обработать wrap-around в кольце)
    public void addFirst(E element) {
        ensureCapacity();
        head = (head - 1 + elements.length) % elements.length; // (head - 1) может быть отрицательным, +length нормализует
        elements[head] = element;
        size++;
    }

    // Добавляет элемент в конец дека
    // Записываем в tail, затем сдвигаем tail вперед по модулю
    @Override
    public void addLast(E element) {
        ensureCapacity();
        elements[tail] = element;
        tail = (tail + 1) % elements.length; // Модуль для кольцевой структуры
        size++;
    }

    // Возвращает первый элемент без удаления
    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return elements[head];
    }

    // Возвращает последний элемент без удаления
    // tail - это позиция после последнего, так что (tail - 1) по модулю
    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return elements[(tail - 1 + elements.length) % elements.length]; // +length для обработки tail=0
    }

    // Эквивалент getFirst: возвращает элемент без удаления
    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return getFirst();
    }

    // Удаляет и возвращает первый элемент (эквивалентно pollFirst)
    @Override
    public E poll() {
        return pollFirst();
    }

    // Удаляет и возвращает первый элемент, возвращает null если пуст
    // Обнуляем ячейку, сдвигаем голову вперед по модулю
    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E value = elements[head];
        elements[head] = null; // Помогает сборщику мусора
        head = (head + 1) % elements.length;
        size--;
        return value;
    }

    // Удаляет и возвращает последний элемент, возвращает null если пуст
    // Сдвигаем tail назад по модулю, обнуляем ячейку
    @Override
    public E pollLast() {
        if (size == 0) return null;
        tail = (tail - 1 + elements.length) % elements.length; // Нормализация для tail=0
        E value = elements[tail];
        elements[tail] = null; // Помогает сборщику мусора
        size--;
        return value;
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
        return null;
    }

    @Override
    public boolean offerFirst(E e) {
        return false;
    }

    @Override
    public boolean offerLast(E e) {
        return false;
    }

    @Override
    public E removeFirst() {
        return null;
    }

    @Override
    public E removeLast() {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public void push(E e) {

    }

    @Override
    public E pop() {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public E peekFirst() {
        return null;
    }

    @Override
    public E peekLast() {
        return null;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }


}