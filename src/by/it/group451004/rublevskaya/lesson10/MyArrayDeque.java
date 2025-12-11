package by.it.group451004.rublevskaya.lesson10;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Collection<E> {
    private E[] data;
    private int currentSize;
    private int head;
    private int tail;
    private static final int CAPACITY = 10;
    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        data = (E[]) new Object[CAPACITY];
        currentSize = 0;
        head = 0;
        tail = 0;
    }

    @Override
    public String toString() {
        if (currentSize == 0) {
            return "[]";
        }
        StringBuilder resultStr = new StringBuilder("[");
        int currentIndex = head;
        for (int i = 0; i < currentSize; i++) {
            resultStr.append(data[currentIndex]);
            if (i < currentSize - 1) {
                resultStr.append(", ");
            }
            currentIndex = (currentIndex + 1) % data.length;
        }
        resultStr.append("]");
        return resultStr.toString();
    }

    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public boolean isEmpty() {
        return currentSize == 0;
    }

    @Override
    public boolean contains(Object obj) {
        if (obj == null) {
            for (int i = 0; i < currentSize; i++) {
                int currentIndex = head + i;
                if (data[(currentIndex) % data.length] == null) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < currentSize; i++) {
                int currentIndex = head + i;
                if (obj.equals(data[(currentIndex) % data.length])) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = head;
            private int count = 0;

            @Override
            public boolean hasNext() {
                return count < currentSize;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                E e = data[currentIndex];
                currentIndex = (currentIndex + 1) % data.length;
                count++;
                return e;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] obj = new Object[currentSize];
        int index = head;
        for (int i = 0; i < currentSize; i++) {
            obj[i] = data[index];
            index = (index + 1) % data.length;
        }
        return obj;
    }

    @Override
    public <T> T[] toArray(T[] arr) {
        if (arr.length < currentSize) {
            arr = (T[]) Array.newInstance(arr.getClass().getComponentType(), currentSize);
        }
        int index = head;
        for (int i = 0; i < currentSize; i++) {
            arr[i] = (T) data[index];
            index = (index + 1) % data.length;
        }
        if (arr.length > currentSize) {
            arr[currentSize] = null;
        }
        return arr;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            if (o.equals(iterator.next())) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            add(e);
        }
        return !c.isEmpty();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean isCorrect = false;
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            if (c.contains(iterator.next())) {
                iterator.remove();
                isCorrect = true;
            }
        }
        return isCorrect;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean isCorrect = false;
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            if (!c.contains(iterator.next())) {
                iterator.remove();
                isCorrect = true;
            }
        }
        return isCorrect;
    }

    @Override
    public void clear() {
        for (int i = 0; i < data.length; i++) {
            data[i] = null;
        }
        currentSize = head =  tail = 0;
    }

    public void addFirst(E e) {
        if (currentSize == data.length) {
            resize();
        }
        head = (head - 1 + data.length) % data.length;
        data[head] = e;
        currentSize++;
    }

    public void addLast(E e) {
        if (currentSize == data.length) {
            resize();
        }
        data[tail] = e;
        tail = (tail + 1) % data.length;
        currentSize++;
    }

    public E element() {
        if (currentSize == 0) {
            throw new NoSuchElementException();
        }
        return data[head];
    }

    public E getFirst() {
        if (currentSize == 0) {
            throw new NoSuchElementException();
        }
        return data[head];
    }

    public E getLast() {
        if (currentSize == 0) {
            throw new NoSuchElementException();
        }
        return data[(tail - 1 + data.length) % data.length];
    }

    public E poll() {
        return pollFirst();
    }

    public E pollFirst() {
        if (currentSize == 0) {
            return null;
        }
        E e = data[head];
        data[head] = null;
        head = (head + 1) % data.length;
        currentSize--;
        return e;
    }

    public E pollLast() {
        if (currentSize == 0) {
            return null;
        }
        tail = (tail - 1 + data.length) % data.length;
        E e = data[tail];
        data[tail] = null;
        currentSize--;
        return e;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        E[] newArray = (E[]) new Object[data.length * 2];
        int index = head;
        for (int i = 0; i < currentSize; i++) {
            newArray[i] = data[index];
            index = (index + 1) % data.length;
        }
        data = newArray;
        head = 0;
        tail = currentSize;
    }
}