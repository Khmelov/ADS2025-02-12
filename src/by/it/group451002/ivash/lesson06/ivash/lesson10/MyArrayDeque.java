package by.it.group451002.ivash.lesson06.ivash.lesson10;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {
    private E[] data;
    private int head; // индекс первого элемента
    private int tail; // индекс следующей позиции после последнего
    private int size;
    private static final int DEFAULT_CAPACITY = 8;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        data = (E[]) new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    // увеличивает ёмкость при заполнении
    private void ensureCapacity() {
        if (size == data.length) {
            int newCapacity = data.length * 2;
            @SuppressWarnings("unchecked")
            E[] newData = (E[]) new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newData[i] = data[(head + i) % data.length];
            }
            data = newData;
            head = 0;
            tail = size;
        }
    }

    // ------------------ Добавление элементов ------------------

    @Override
    public void addFirst(E element) {
        ensureCapacity();
        head = (head - 1 + data.length) % data.length;
        data[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        ensureCapacity();
        data[tail] = element;
        tail = (tail + 1) % data.length;
        size++;
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    // ------------------ Получение элементов ------------------

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return data[head];
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return data[(tail - 1 + data.length) % data.length];
    }

    @Override
    public E element() {
        return getFirst();
    }

    // ------------------ Удаление элементов ------------------

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E value = data[head];
        data[head] = null;
        head = (head + 1) % data.length;
        size--;
        return value;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        tail = (tail - 1 + data.length) % data.length;
        E value = data[tail];
        data[tail] = null;
        size--;
        return value;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E remove() {
        E value = pollFirst();
        if (value == null) throw new NoSuchElementException();
        return value;
    }

    @Override
    public E removeFirst() {
        E value = pollFirst();
        if (value == null) throw new NoSuchElementException();
        return value;
    }

    @Override
    public E removeLast() {
        E value = pollLast();
        if (value == null) throw new NoSuchElementException();
        return value;
    }

    // ------------------ Прочее ------------------

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < data.length; i++) data[i] = null;
        head = 0;
        tail = 0;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[(head + i) % data.length]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // ------------------ Ненужные по заданию методы ------------------

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { return add(e); }
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E peek() { return size == 0 ? null : data[head]; }
    @Override public E peekFirst() { return size == 0 ? null : data[head]; }
    @Override public E peekLast() { return size == 0 ? null : data[(tail - 1 + data.length) % data.length]; }
    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return removeFirst(); }

    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}
