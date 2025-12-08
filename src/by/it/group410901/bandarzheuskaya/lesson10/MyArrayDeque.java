package by.it.group410901.bandarzheuskaya.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {
    private E[] elements;
    private int head;
    private int tail;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            int index = (head + i) % elements.length;
            sb.append(elements[index]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        ensureCapacity();
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        ensureCapacity();
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException("Deque is empty");
        return elements[head];
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException("Deque is empty");
        return elements[(tail - 1 + elements.length) % elements.length];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E element = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return element;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        tail = (tail - 1 + elements.length) % elements.length;
        E element = elements[tail];
        elements[tail] = null;
        size--;
        return element;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Реализация итераторов                       ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return new ArrayDequeIterator();
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new ArrayDequeDescendingIterator();
    }

    private class ArrayDequeIterator implements Iterator<E> {
        private int currentIndex = 0;
        private int returnedCount = 0;

        @Override
        public boolean hasNext() {
            return returnedCount < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int index = (head + currentIndex) % elements.length;
            E element = elements[index];
            currentIndex++;
            returnedCount++;
            return element;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class ArrayDequeDescendingIterator implements Iterator<E> {
        private int currentIndex = size - 1;
        private int returnedCount = 0;

        @Override
        public boolean hasNext() {
            return returnedCount < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int index = (head + currentIndex) % elements.length;
            E element = elements[index];
            currentIndex--;
            returnedCount++;
            return element;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////          Дополнительные методы для реализации Deque        ///////
    /////////////////////////////////////////////////////////////////////////

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
    public E removeFirst() {
        if (size == 0) throw new NoSuchElementException("Deque is empty");
        return pollFirst();
    }

    @Override
    public E removeLast() {
        if (size == 0) throw new NoSuchElementException("Deque is empty");
        return pollLast();
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
    public boolean removeFirstOccurrence(Object o) {
        for (int i = 0; i < size; i++) {
            int index = (head + i) % elements.length;
            if ((elements[index] == null && o == null) ||
                    (elements[index] != null && elements[index].equals(o))) {
                removeAtIndex((head + i) % elements.length);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            int index = (head + i) % elements.length;
            if ((elements[index] == null && o == null) ||
                    (elements[index] != null && elements[index].equals(o))) {
                removeAtIndex((head + i) % elements.length);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            int index = (head + i) % elements.length;
            if ((elements[index] == null && o == null) ||
                    (elements[index] != null && elements[index].equals(o))) {
                return true;
            }
        }
        return false;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы                      ///////
    /////////////////////////////////////////////////////////////////////////

    private void removeAtIndex(int index) {
        if (index == head) {
            pollFirst();
        } else if (index == (tail - 1 + elements.length) % elements.length) {
            pollLast();
        } else {
            // Удаление из середины - сложный случай
            int current = index;
            while (current != (tail - 1 + elements.length) % elements.length) {
                int next = (current + 1) % elements.length;
                elements[current] = elements[next];
                current = next;
            }
            tail = (tail - 1 + elements.length) % elements.length;
            elements[tail] = null;
            size--;
        }
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == elements.length) {
            E[] newElements = (E[]) new Object[elements.length * 2 + 1];

            // Копируем элементы от head до конца массива
            int firstPart = Math.min(size, elements.length - head);
            System.arraycopy(elements, head, newElements, 0, firstPart);

            // Копируем оставшиеся элементы из начала массива
            if (firstPart < size) {
                System.arraycopy(elements, 0, newElements, firstPart, size - firstPart);
            }

            elements = newElements;
            head = 0;
            tail = size;
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////     Методы, которые не поддерживаются (Unsupported)       ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    // Остальные методы Collection - не реализуем для упрощения
    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
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
        for (int i = 0; i < elements.length; i++) {
            elements[i] = null;
        }
        head = 0;
        tail = 0;
        size = 0;
    }
}