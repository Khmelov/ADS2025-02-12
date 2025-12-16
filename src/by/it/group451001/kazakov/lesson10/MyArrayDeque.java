package by.it.group451001.kazakov.lesson10;

import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.Collection;
import java.util.Iterator;

public class MyArrayDeque<E> implements Deque<E> {

    private E[] elements = (E[]) (new Object[10]);  // Внутренний массив для хранения элементов
    private int size = 0;                           // Текущее количество элементов

    // Строковое представление дека в формате [элемент1, элемент2, ...]
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("[");
        var it = iterator();
        if (it.hasNext())
            res.append(it.next().toString());  // Первый элемент без запятой
        while (it.hasNext()) {
            res.append(", ");
            res.append(it.next().toString());  // Остальные элементы с запятыми
        }
        res.append("]");
        return res.toString();
    }

    @Override
    public int size() {
        return size;
    }

    // Увеличение размера массива при заполнении
    private void grow() {
        E[] newElements = (E[]) (new Object[elements.length * 2]);  // Новый массив в 2 раза больше
        System.arraycopy(elements, 0, newElements, 0, elements.length);  // Копируем элементы
        elements = newElements;  // Заменяем старый массив
    }

    // Добавление элемента в начало дека
    @Override
    public void addFirst(E e) {
        if(size == elements.length)
            grow();  // Расширяем массив если нужно
        // Сдвигаем все элементы вправо для освобождения места в начале
        System.arraycopy(elements, 0, elements, 1, size);
        elements[0] = e;  // Вставляем элемент в начало
        size++;
    }

    // Добавление элемента в конец дека
    @Override
    public void addLast(E e) {
        if(size == elements.length)
            grow();  // Расширяем массив если нужно
        elements[size++] = e;  // Добавляем элемент в конец
    }

    // Не реализованные методы (возвращают значения по умолчанию)
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

    // Удаление и возврат первого элемента (без исключения для пустого дека)
    @Override
    public E pollFirst() {
        if(size == 0)
            return null;  // Возвращаем null если дек пуст
        E e = elements[0];  // Сохраняем первый элемент
        // Сдвигаем все элементы влево для заполнения пустоты
        System.arraycopy(elements, 1, elements, 0, size - 1);
        size--;
        return e;
    }

    // Удаление и возврат последнего элемента
    @Override
    public E pollLast() {
        return elements[--size];  // Просто уменьшаем размер и возвращаем последний элемент
    }

    // Получение первого элемента с проверкой на пустоту
    @Override
    public E getFirst() {
        if(size == 0)
            throw new NoSuchElementException();  // Исключение если дек пуст
        return elements[0];
    }

    // Получение последнего элемента с проверкой на пустоту
    @Override
    public E getLast() {
        if(size == 0)
            throw new NoSuchElementException();  // Исключение если дек пуст
        return elements[size - 1];
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

    // Добавление элемента в конец (аналог addLast)
    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
        return null;
    }

    // Удаление и возврат первого элемента (аналог pollFirst)
    @Override
    public E poll() {
        if(size == 0)
            return null;
        E e = elements[0];
        System.arraycopy(elements, 1, elements, 0, size - 1);
        size--;
        return e;
    }

    // Получение первого элемента (аналог getFirst)
    @Override
    public E element() {
        if(size == 0)
            throw new NoSuchElementException();
        return elements[0];
    }

    @Override
    public E peek() {
        return null;
    }

    // Не реализованные методы работы с коллекциями
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
    public boolean isEmpty() {
        return false;
    }

    // Внутренний класс итератора для обхода дека
    private class DequeIterator implements Iterator<E> {
        int cursor = 0;  // Текущая позиция итератора

        @Override
        public boolean hasNext() {
            return cursor < size;  // Проверка наличия следующего элемента
        }

        @Override
        public E next() {
            return elements[cursor++];  // Возврат текущего элемента и переход к следующему
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new DequeIterator();  // Создание нового итератора
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