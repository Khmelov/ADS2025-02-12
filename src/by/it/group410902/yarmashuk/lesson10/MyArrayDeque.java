package by.it.group410902.yarmashuk.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private E[] elements;
    private int head; // Индекс первого элемента
    private int tail; // Индекс следующей свободной ячейки после последнего элемента
    private int size; // Текущее количество элементов


    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }


    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[(head + i) % elements.length]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }


    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = elements.length * 2;
            E[] newElements = (E[]) new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[(head + i) % elements.length];
            }
            elements = newElements;
            head = 0;
            tail = size;
        }
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        ensureCapacity();
        head = (head - 1 + elements.length) % elements.length; // Сдвигаем head назад (кольцевой сдвиг)
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        ensureCapacity();
        elements[tail] = element;
        tail = (tail + 1) % elements.length; // Сдвигаем tail вперед (кольцевой сдвиг)
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        return elements[head];
    }

    @Override
    public E getLast() {
        if (isEmpty()) {
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
        if (isEmpty()) {
            return null;
        }
        E element = elements[head];
        elements[head] = null; // Освобождаем ссылку на объект
        head = (head + 1) % elements.length; // Сдвигаем head вперед
        size--;
        return element;
    }

    @Override
    public E pollLast() {
        if (isEmpty()) {
            return null;
        }
        tail = (tail - 1 + elements.length) % elements.length; // Сдвигаем tail назад
        E element = elements[tail];
        elements[tail] = null; // Освобождаем ссылку на объект
        size--;
        return element;
    }









    @Override
    public boolean offerFirst(E e) {
        if (e == null) return false; // null не поддерживается в некоторых реализациях Deque
        addFirst(e);
        return true;
    }
    @Override
    public boolean offerLast(E e) {
        if (e == null) return false; // null не поддерживается в некоторых реализациях Deque
        addLast(e);
        return true;
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
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public E remove() {
        return null;
    }


    @Override
    public E peekFirst() {
        if (isEmpty()) {
            return null;
        }
        return elements[head];
    }
    @Override
    public E peekLast() {
        if (isEmpty()) {
            return null;
        }
        return elements[(tail - 1 + elements.length) % elements.length];
    }
    @Override
    public E peek() {
        return peekFirst();
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
    public boolean remove(Object o) {
        if (o == null) {
            return false; // null не поддерживается
        }
        for (int i = 0; i < size; i++) {
            int currentIndex = (head + i) % elements.length;
            if (o.equals(elements[currentIndex])) {
                removeElementAt(currentIndex);
                return true;
            }
        }
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
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }
    @Override
    public boolean removeLastOccurrence(Object o) {
        if (o == null) {
            return false; // null не поддерживается
        }
        for (int i = size - 1; i >= 0; i--) {
            int currentIndex = (head + i) % elements.length;
            if (o.equals(elements[currentIndex])) {
                removeElementAt(currentIndex);
                return true;
            }
        }
        return false;
    }
    @Override
    public void push(E e) {
        addFirst(e);
    }
    @Override
    public E pop() {
        return pollFirst();
    }



    private E removeElementAt(int index) {
        // Этот метод используется для внутренней реализации remove().
        // Он предполагает, что индекс действителен и соответствует существующему элементу.

        E removedElement = elements[index];
        elements[index] = null; // Освобождаем ссылку

        // Если удаляемый элемент находится в начале, просто двигаем head
        if (index == head) {
            head = (head + 1) % elements.length;
        }
        // Если удаляемый элемент находится в конце, просто двигаем tail
        else if ((index + 1) % elements.length == tail) {
            tail = (tail - 1 + elements.length) % elements.length;
        }
        // Если удаляемый элемент находится посередине, нужно сдвинуть элементы,
        // чтобы заполнить пустоту.
        else {
            // Сдвигаем элементы после удаленного на одну позицию назад.
            // Важно правильно определить направление сдвига, чтобы минимизировать
            // количество перемещаемых элементов.
            int distanceToHead = (index - head + elements.length) % elements.length;
            int distanceToTail = (tail - index - 1 + elements.length) % elements.length;

            if (distanceToHead <= distanceToTail) {
                // Сдвигаем элементы "вперед" (к head)
                for (int i = 0; i < distanceToHead; i++) {
                    int current = (index - i + elements.length) % elements.length;
                    int prev = (current - 1 + elements.length) % elements.length;
                    elements[prev] = elements[current];
                }
                head = (head + 1) % elements.length; // head сдвигается
            } else {
                // Сдвигаем элементы "назад" (к tail)
                for (int i = 0; i < distanceToTail; i++) {
                    int current = (index + 1 + i) % elements.length;
                    int next = (current + 1) % elements.length;
                    elements[current] = elements[next];
                }
                tail = (tail - 1 + elements.length) % elements.length; // tail сдвигается
            }
        }
        size--;
        return removedElement;
    }

    public E set(int index, E element) {
        throw new UnsupportedOperationException("Index-based set not supported");
    }

    public E remove(int index) {
        throw new UnsupportedOperationException("Index-based remove not supported");
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
