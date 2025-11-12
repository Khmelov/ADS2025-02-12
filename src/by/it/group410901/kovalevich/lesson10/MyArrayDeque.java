package by.it.group410901.kovalevich.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class MyArrayDeque<E> implements Deque<E> {

    private E[] data;
    private int head; // индекс первого элемента
    private int tail; // индекс next свободной позиции
    private int size;

    public MyArrayDeque() {
        data = (E[]) new Object[8]; // стартовая ёмкость
        head = 0;
        tail = 0;
        size = 0;
    }


    private int capacity() {
        return data.length;
    }

    private int dec(int idx) {
        // шаг назад по кольцу
        return (idx - 1 + capacity()) % capacity();
    }

    private int inc(int idx) {
        // шаг вперёд по кольцу
        return (idx + 1) % capacity();
    }

    private void ensureCapacity() {
        if (size < capacity()) {
            return;
        }
        int oldCap = capacity();
        int newCap = oldCap * 2;
        E[] newData = (E[]) new Object[newCap];

        // перекладываем элементы от head по порядку
        for (int i = 0; i < size; i++) {
            newData[i] = data[(head + i) % oldCap];
        }

        data = newData;
        head = 0;
        tail = size; // следующий свободный
    }

    private int lastIndex() {
        return (tail - 1 + capacity()) % capacity();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(data[(head + i) % capacity()]);
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // add == addLast
    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void addFirst(E e) {
        ensureCapacity();
        head = dec(head);
        data[head] = e;
        size++;
    }

    @Override
    public void addLast(E e) {
        ensureCapacity();
        data[tail] = e;
        tail = inc(tail);
        size++;
    }

    //  голова, с исключением если пусто
    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return data[head];
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return data[lastIndex()];
    }

    // poll() = снять первый или null
    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E value = data[head];
        data[head] = null;
        head = inc(head);
        size--;
        return value;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        tail = dec(tail);
        E value = data[tail];
        data[tail] = null;
        size--;
        return value;
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
    public E removeFirst() {
        E v = pollFirst();
        if (v == null) throw new NoSuchElementException();
        return v;
    }

    @Override
    public E removeLast() {
        E v = pollLast();
        if (v == null) throw new NoSuchElementException();
        return v;
    }

    @Override
    public E peekFirst() {
        return size == 0 ? null : data[head];
    }

    @Override
    public E peekLast() {
        return size == 0 ? null : data[lastIndex()];
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public E remove() {
        E v = poll();
        if (v == null) throw new NoSuchElementException();
        return v;
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public void push(E e) {
        addFirst(e);
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
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            E cur = data[(head + i) % capacity()];
            if (cur == null && o == null) return true;
            if (cur != null && cur.equals(o)) return true;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        // не требуют по заданию
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> descendingIterator() {
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

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // реализация линейной проверки
        for (Object el : c) {
            if (!contains(el)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // мы можем просто добавить по одному
        boolean changed = false;
        for (E el : c) {
            addLast(el);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            data[(head + i) % capacity()] = null;
        }
        head = 0;
        tail = 0;
        size = 0;
    }
}
