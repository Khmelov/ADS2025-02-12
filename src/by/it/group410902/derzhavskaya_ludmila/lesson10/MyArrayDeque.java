package by.it.group410902.derzhavskaya_ludmila.lesson10;
import java.util.Deque;
import java.util.NoSuchElementException;
// реализация массива
public class MyArrayDeque<E> implements Deque<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private E[] elements;

    private int size;

    public MyArrayDeque() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    //запись массива в формате строки
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // вывод размера массива
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E element) {
        // Добавляем элемент в конец
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        // Увеличиваем массив при необходимости
        if (size +1 > elements.length) {
            // увеличиваем емкость в 2 раза
            int newCapacity = elements.length * 2;
            E[] newElements = (E[]) new Object[newCapacity];

            // Копируем все элементы в новый массив
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }

        // Сдвигаем все элементы вправо чтобы освободить место в начале
        for (int i = size; i > 0; i--) {
            elements[i] = elements[i - 1];
        }

        // Добавляем новый элемент в начало
        elements[0] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        // Увеличиваем массив при необходимости
        if (size +1 > elements.length) {
            // увеличиваем емкость в 2 раза
            int newCapacity = elements.length * 2;
            E[] newElements = (E[]) new Object[newCapacity];

            // Копируем все элементы в новый массив
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }

        // Добавляем элемент в конец
        elements[size] = element;
        size++;
    }

    @Override
    public E element() {
        // Возвращаем первый элемент (аналогично getFirst)
        return getFirst();
    }

    @Override
    public E getFirst() {
        // Если массив пустой, возвращаем null
        if (size == 0) {
            return null;
        }
        return elements[0];
    }

    @Override
    public E getLast() {
        // Если массив пустой, возвращаем null
        if (size == 0) {
            return null;
        }
        return elements[size -1];
    }

    @Override
    public E poll() {
        // Удаляем и возвращаем первый элемент
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }

        // Сохраняем первый элемент
        E element = elements[0];

        // Сдвигаем все элементы влево
        for (int i = 0; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        // Очищаем последнюю ячейку и уменьшаем размер
        elements[size - 1] = null;
        size--;
        return element;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }

        // Сохраняем последний элемент
        E element = elements[size - 1];

        // Очищаем ячейку и уменьшаем размер
        elements[size - 1] = null;
        size--;
        return element;
    }


    //////   Остальные методы интерфейса Deque тоже должны быть реализованы

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
        return size == 0 ? null : getFirst();
    }

    @Override
    public E peekLast() {
        return size == 0 ? null : getLast();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public java.util.Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException("Not implemented");
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
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not implemented");
    }
}

