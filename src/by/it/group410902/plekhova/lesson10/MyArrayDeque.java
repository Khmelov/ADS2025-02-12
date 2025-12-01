package by.it.group410902.plekhova.lesson10;
import java.util.NoSuchElementException;
import java.util.Deque;
import java.util.Collection;
import java.util.Iterator;
import java.util.*;
import java.lang.reflect.Array;

/*Создайте class MyArrayDeque<E>, который реализует интерфейс Deque<E>
и работает на основе приватного массива типа E[]
БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ*/

public class MyArrayDeque<E> implements Deque<E> {
    private E[] elements;
    private int head;
    private int tail;
    private int size;
    private static final int DEFAULT_CAPACITY = 8;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }


    private int capacity() {
        return elements.length;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = elements.length * 2;
            E[] newArr = (E[]) new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newArr[i] = elements[(head + i) % elements.length];
            }
            elements = newArr;
            head = 0;
            tail = size;
        }
    }

    private void requireNonNullArg(Object o) {
        if (o == null) throw new NullPointerException();
    }

    // удаление по индексу в массиве (index в диапазоне 0..capacity-1)
    @SuppressWarnings("unchecked")
    private void removeAtIndex(int index) {
        if (size == 0) return;
        // Если удаляется первый или последний — используем готовые методы
        if (index == head) {
            pollFirst();
            return;
        }
        int lastIdx = (tail - 1 + capacity()) % capacity();
        if (index == lastIdx) {
            pollLast();
            return;
        }
        // Соберём новый массив без удаляемого элемента
        E[] newArr = (E[]) new Object[capacity()];
        int j = 0;
        for (int i = 0; i < size; i++) {
            int pos = (head + i) % capacity();
            if (pos == index) continue;
            newArr[j++] = elements[pos];
        }
        elements = newArr;
        head = 0;
        tail = j;
        size = j;
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
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public void addFirst(E e) {
        ensureCapacity();
        head = (head - 1 + capacity()) % capacity();
        elements[head] = e;
        size++;
    }

    @Override
    public void addLast(E e) {
        ensureCapacity();
        elements[tail] = e;
        tail = (tail + 1) % capacity();
        size++;
    }

    @Override
    public boolean offerFirst(E e) {
        ensureCapacity();
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        ensureCapacity();
        addLast(e);
        return true;
    }


    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return elements[head];
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        int lastIndex = (tail - 1 + capacity()) % capacity();
        return elements[lastIndex];
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    // возвращает первый элемент без удаления
    @Override
    public E peekFirst() {
        if (size == 0) return null;
        return elements[head];
    }

    @Override
    public E peekLast() {
        if (size == 0) return null;
        int lastIndex = (tail - 1 + capacity()) % capacity();
        return elements[lastIndex];
    }


    @Override
    public E poll() {
        return pollFirst();
    }
// возвращает и удаляет первый элемент
    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E value = elements[head];
        elements[head] = null;
        head = (head + 1) % capacity();
        size--;
        if (size == 0) { head = tail = 0; }
        return value;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        tail = (tail - 1 + capacity()) % capacity();
        E value = elements[tail];
        elements[tail] = null;
        size--;
        if (size == 0) { head = tail = 0; }
        return value;
    }

    @Override
    public E remove() {
        return removeFirst();
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
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        for (int i = 0; i < size; i++) {
            int pos = (head + i) % capacity();
            if (Objects.equals(elements[pos], o)) {
                removeAtIndex(pos);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            int pos = (head + i) % capacity();
            if (Objects.equals(elements[pos], o)) {
                removeAtIndex(pos);
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
        return removeFirst();
    }


    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(elements[(head + i) % capacity()], o)) return true;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0;
            @Override
            public boolean hasNext() { return cursor < size; }
            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                return elements[(head + cursor++) % capacity()];
            }
            @Override
            public void remove() { throw new UnsupportedOperationException(); }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private int cursor = 0;
            @Override
            public boolean hasNext() { return cursor < size; }
            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                int idx = (tail - 1 - cursor + capacity()) % capacity();
                cursor++;
                return elements[idx];
            }
            @Override
            public void remove() { throw new UnsupportedOperationException(); }
        };
    }


    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) arr[i] = elements[(head + i) % capacity()];
        return arr;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            T[] newArr = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
            for (int i = 0; i < size; i++) newArr[i] = (T) elements[(head + i) % capacity()];
            return newArr;
        } else {
            for (int i = 0; i < size; i++) a[i] = (T) elements[(head + i) % capacity()];
            if (a.length > size) a[size] = null;
            return a;
        }
    }


    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        if (c == this) {
            // копируем сначала содержимое в массив, затем добавляем
            Object[] tmp = toArray();
            for (Object o : tmp) addLast((E) o);
            return !isEmpty();
        } else {
            boolean modified = false;
            for (E e : c) {
                addLast(e);
                modified = true;
            }
            return modified;
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        @SuppressWarnings("unchecked")
        E[] newArr = (E[]) new Object[capacity()];
        int j = 0;
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            E val = elements[(head + i) % capacity()];
            if (c.contains(val)) {
                modified = true;
            } else {
                newArr[j++] = val;
            }
        }
        if (modified) {
            elements = newArr;
            head = 0;
            tail = j;
            size = j;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        @SuppressWarnings("unchecked")
        E[] newArr = (E[]) new Object[capacity()];
        int j = 0;
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            E val = elements[(head + i) % capacity()];
            if (c.contains(val)) {
                newArr[j++] = val;
            } else {
                modified = true;
            }
        }
        if (modified) {
            elements = newArr;
            head = 0;
            tail = j;
            size = j;
        }
        return modified;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) elements[(head + i) % capacity()] = null;
        head = tail = size = 0;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[(head + i) % capacity()]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }


}