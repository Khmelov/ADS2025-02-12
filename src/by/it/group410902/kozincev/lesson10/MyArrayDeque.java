package by.it.group410902.kozincev.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private E[] elements;
    private int head; // Индекс первого элемента
    private int tail; // Индекс, куда будет добавлен следующий элемент (пустой)
    private int size; // Текущее количество элементов

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        // Создание обобщенного массива через Object и кастинг
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    private void grow() {
        int oldCapacity = elements.length;
        int newCapacity = oldCapacity * 2;
        E[] newElements = (E[]) new Object[newCapacity];

        // Копирование элементов в новый массив с учетом кольцевой структуры
        if (head < tail) {
            // Элементы идут по порядку: [head, ..., tail-1]
            System.arraycopy(elements, head, newElements, 0, size);
        } else {
            // Элементы "обернуты": [head, ..., capacity-1, 0, ..., tail-1]
            // 1. Копируем часть от head до конца массива
            int firstPartLength = oldCapacity - head;
            System.arraycopy(elements, head, newElements, 0, firstPartLength);
            // 2. Копируем часть от начала массива до tail
            System.arraycopy(elements, 0, newElements, firstPartLength, tail);
        }

        elements = newElements;
        head = 0;
        tail = size; // Новый tail будет сразу после последнего скопированного элемента
    }



    @Override
    public int size() {
        return size;
    }

    private int prev(int index) {
        return (index - 1 + elements.length) % elements.length;
    }

    private int next(int index) {
        return (index + 1) % elements.length;
    }

    @Override
    public void addFirst(E element) {
        if (size == elements.length) {
            grow();
        }
        head = prev(head);
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        if (size == elements.length) {
            grow();
        }
        elements[tail] = element;
        tail = next(tail);
        size++;
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }


    private void checkEmpty() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public E getFirst() { //
        checkEmpty();
        return elements[head];
    }

    @Override
    public E getLast() { //
        checkEmpty();
        return elements[prev(tail)];
    }

    @Override
    public E element() { //
        return getFirst();
    }


    @Override
    public E pollFirst() { //
        if (size == 0) {
            return null;
        }
        E element = elements[head];
        elements[head] = null; // Освобождаем ссылку для GC
        head = next(head);
        size--;
        return element;
    }

    @Override
    public E pollLast() { //
        if (size == 0) {
            return null;
        }
        tail = prev(tail);
        E element = elements[tail];
        elements[tail] = null; // Освобождаем ссылку для GC
        size--;
        return element;
    }

    @Override
    public E poll() { //
        return pollFirst();
    }


    @Override
    public String toString() { //
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');

        for (int i = 0; i < size; i++) {
            int index = (head + i) % elements.length;
            sb.append(elements[index]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
        return sb.toString();
    }

    @Override public boolean isEmpty() { return size == 0; }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean offerFirst(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean offerLast(E e) { throw new UnsupportedOperationException(); }
    @Override public E remove() { throw new UnsupportedOperationException(); }
    @Override public E removeFirst() { throw new UnsupportedOperationException(); }
    @Override public E removeLast() { throw new UnsupportedOperationException(); }
    @Override public E peek() { throw new UnsupportedOperationException(); }
    @Override public E peekFirst() { throw new UnsupportedOperationException(); }
    @Override public E peekLast() { throw new UnsupportedOperationException(); }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public void push(E e) { throw new UnsupportedOperationException(); }
    @Override public E pop() { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
}
