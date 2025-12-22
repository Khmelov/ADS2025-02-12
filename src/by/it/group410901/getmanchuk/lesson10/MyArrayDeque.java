package by.it.group410901.getmanchuk.lesson10;

import java.util.*;

public class MyArrayDeque<E> implements Deque<E> {

    private static final int DEFAULT_CAPACITY = 8;
    private E[] data;
    private int head = 0; // индекс первого элемента
    private int tail = 0; // индекс следующего после последнего элемента
    private int size = 0;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        data = (E[]) new Object[DEFAULT_CAPACITY];
    }

    // ================= ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ =================

    private int idx(int i) {
        return (head + i) % data.length;
    }

    private void grow() {
        int newCap = data.length * 2;
        @SuppressWarnings("unchecked")
        E[] newData = (E[]) new Object[newCap];
        for (int i = 0; i < size; i++) newData[i] = data[idx(i)];
        data = newData;
        head = 0;
        tail = size;
    }

    private void checkNotEmpty() {
        if (size == 0) throw new NoSuchElementException();
    }

    // ================= ОСНОВНЫЕ МЕТОДЫ =================

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        Arrays.fill(data, null);
        head = tail = size = 0;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    // <-- ВАЖНО: реализуем offer(E e)
    @Override
    public boolean offer(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void addFirst(E e) {
        if (size == data.length) grow();
        head = (head - 1 + data.length) % data.length;
        data[head] = e;
        size++;
    }

    @Override
    public void addLast(E e) {
        if (size == data.length) grow();
        data[tail] = e;
        tail = (tail + 1) % data.length;
        size++;
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
        checkNotEmpty();
        E val = data[head];
        data[head] = null;
        head = (head + 1) % data.length;
        size--;
        return val;
    }

    @Override
    public E removeLast() {
        checkNotEmpty();
        tail = (tail - 1 + data.length) % data.length;
        E val = data[tail];
        data[tail] = null;
        size--;
        return val;
    }

    @Override
    public E pollFirst() {
        return isEmpty() ? null : removeFirst();
    }

    @Override
    public E pollLast() {
        return isEmpty() ? null : removeLast();
    }

    @Override
    public E getFirst() {
        checkNotEmpty();
        return data[head];
    }

    @Override
    public E getLast() {
        checkNotEmpty();
        return data[(tail - 1 + data.length) % data.length];
    }

    @Override
    public E peekFirst() {
        return isEmpty() ? null : data[head];
    }

    @Override
    public E peekLast() {
        return isEmpty() ? null : data[(tail - 1 + data.length) % data.length];
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return removeOccurrence(o, true);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return removeOccurrence(o, false);
    }

    private boolean removeOccurrence(Object o, boolean fromStart) {
        if (isEmpty()) return false;
        int found = -1;

        if (fromStart) {
            for (int i = 0; i < size; i++) {
                if (Objects.equals(data[idx(i)], o)) {
                    found = i;
                    break;
                }
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (Objects.equals(data[idx(i)], o)) {
                    found = i;
                    break;
                }
            }
        }

        if (found == -1) return false;

        for (int j = found; j < size - 1; j++) {
            data[idx(j)] = data[idx(j + 1)];
        }
        data[idx(size - 1)] = null;
        tail = (tail - 1 + data.length) % data.length;
        size--;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    // ================= ДОПОЛНИТЕЛЬНЫЕ ОПЕРАЦИИ =================

    @Override
    public boolean contains(Object o) {
        for (E e : this) {
            if (Objects.equals(e, o)) return true;
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) arr[i] = data[idx(i)];
        return arr;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            return (T[]) Arrays.copyOf(toArray(), size, a.getClass());
        System.arraycopy(toArray(), 0, a, 0, size);
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int cursor = 0;
            public boolean hasNext() { return cursor < size; }
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                return data[idx(cursor++)];
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            int cursor = size - 1;
            public boolean hasNext() { return cursor >= 0; }
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                return data[idx(cursor--)];
            }
        };
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) add(e);
        return !c.isEmpty();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) while (remove(o)) changed = true;
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        int oldSize = size;
        for (int i = 0; i < oldSize; i++) {
            E e = data[idx(i)];
            if (!c.contains(e)) {
                remove(e);
                changed = true;
            }
        }
        return changed;
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
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[idx(i)]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    // Дополнительный метод без @Override
    public E get(int i) {
        if (i < 0 || i >= size) throw new IndexOutOfBoundsException();
        return data[idx(i)];
    }
}