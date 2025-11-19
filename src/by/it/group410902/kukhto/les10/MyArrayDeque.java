package by.it.group410902.kukhto.les10;

/*              toString()
                size()

                add(E element)
                addFirst(E element)
                addLast(E element)

                element()
                getFirst()
                getLast()

                poll()
                pollFirst()
                pollLast()
*/

import java.util.Deque;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private E[] elements;
    private int head;
    private int tail;
    private int size;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[16];
        head = 0;
        tail = 0;
        size = 0;
    }
    //двунаправленная очередь позволяет создавать элементы с обоих концоы

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        int current = head;
        for (int i = 0; i < size; i++) {
            sb.append(elements[current]);
            if (i < size - 1) {
                sb.append(", ");
            }
            current = (current + 1) % elements.length;
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
        if (size == elements.length) {

            E[] newElements = (E[]) new Object[elements.length * 2];

            if (head < tail) {
                for (int i = 0; i < size; i++) {
                    newElements[i] = elements[head + i];
                }
            } else {
                int firstPart = elements.length - head;
                for (int i = 0; i < firstPart; i++) {
                    newElements[i] = elements[head + i];
                }
                for (int i = 0; i < tail; i++) {
                    newElements[firstPart + i] = elements[i];
                }
            }

            elements = newElements;
            head = 0;
            tail = size;
        }

        // Двигаем head назад с учетом кругового буфера
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        if (size == elements.length) {

            E[] newElements = (E[]) new Object[elements.length * 2];


            if (head < tail) {
                for (int i = 0; i < size; i++) {
                    newElements[i] = elements[head + i];
                }
            } else {
                int firstPart = elements.length - head;
                for (int i = 0; i < firstPart; i++) {
                    newElements[i] = elements[head + i];
                }
                for (int i = 0; i < tail; i++) {
                    newElements[firstPart + i] = elements[i];
                }
            }

            elements = newElements;
            head = 0;
            tail = size;
        }

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
        return elements[(tail - 1 + elements.length) % elements.length];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }

        E element = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return element;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }

        tail = (tail - 1 + elements.length) % elements.length;
        E element = elements[tail];
        elements[tail] = null;
        size--;
        return element;
    }

    // Остальные методы Deque
    @Override
    public boolean offerFirst(E e) { return false; }

    @Override
    public boolean offerLast(E e) { return false; }

    @Override
    public E removeFirst() { return null; }

    @Override
    public E removeLast() { return null; }

    @Override
    public E peekFirst() { return null; }

    @Override
    public E peekLast() { return null; }

    @Override
    public boolean removeFirstOccurrence(Object o) { return false; }

    @Override
    public boolean removeLastOccurrence(Object o) { return false; }

    @Override
    public boolean offer(E e) { return false; }

    @Override
    public E remove() { return null; }

    @Override
    public E peek() { return null; }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) { return false; }

    @Override
    public void push(E e) {}

    @Override
    public E pop() { return null; }

    @Override
    public boolean remove(Object o) { return false; }

    @Override
    public boolean contains(Object o) { return false; }

    @Override
    public java.util.Iterator<E> iterator() { return null; }

    @Override
    public java.util.Iterator<E> descendingIterator() { return null; }

    @Override
    public void clear() {}

    @Override
    public boolean containsAll(java.util.Collection<?> c) { return false; }

    @Override
    public boolean removeAll(java.util.Collection<?> c) { return false; }

    @Override
    public boolean retainAll(java.util.Collection<?> c) { return false; }

    @Override
    public boolean isEmpty() { return false; }

    @Override
    public Object[] toArray() { return null; }

    @Override
    public <T> T[] toArray(T[] a) { return null; }
}