package by.it.group451004.struts.lesson10;

import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.function.Consumer;

public class MyArrayDeque<E> implements Deque<E> {
    private E[] storage;
    private int size;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        storage = (E[]) new Object[0];
    }

    @SuppressWarnings("unchecked")
    public MyArrayDeque(int capacity) {
        storage = (E[]) new Object[capacity];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        iterator().forEachRemaining(e -> {
            sb.append(e).append(", ");
        });
        sb.replace(sb.length() - 2, sb.length(), "]");

        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        try {
            if (storage.length <= size)
                resizeStorage(0, storage.length + 1);

            storage[size++] = e;
        } catch (OutOfMemoryError | StackOverflowError ex) {
            return false;
        }

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

    @Override
    public void addFirst(E e) {
        if (storage.length <= size)
            resizeStorage(1, storage.length + 1);
        else
            System.arraycopy(storage, 0, storage, 1, size);

        storage[0] = e;
        size++;
    }

    @Override
    public void addLast(E e) {
        if (storage.length <= size)
            resizeStorage(0, storage.length + 1);

        storage[size++] = e;
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
    public E element() {
        return storage[0];
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
    public E getFirst() {
        return storage[0];
    }

    @Override
    public E getLast() {
        return storage[size - 1];
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
    public E poll() {
        E item = storage[0];
        System.arraycopy(storage, 1, storage, 0, --size);

        return item;
    }

    @Override
    public E pollFirst() {
        return poll();
    }

    @Override
    public E pollLast() {
        return storage[--size];
    }

    @SuppressWarnings("unchecked")
    private void resizeStorage(int destBegin, int newCapacity) throws OutOfMemoryError, StackOverflowError {
        E[] newStorage = (E[]) new Object[newCapacity];
        System.arraycopy(storage, 0, newStorage, destBegin, storage.length);
        storage = newStorage;
    }

    @Override
    public Iterator<E> iterator() {
        return this.createIterator();
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

    private Iterator<E> createIterator() {
        return new Iterator<E>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    return null;
                }

                return storage[index++];
            }

            @Override
            public void remove() {
                storage[index] = null;
                System.arraycopy(storage, index + 1, storage, index, size - index);
                size--;
            }

            @Override
            public void forEachRemaining(Consumer<? super E> action) {
                for (int i = index; i < size; i++) {
                    action.accept(storage[i]);
                }
            }
        };
    }
}
