package by.it.group451003.plyushchevich.lesson10;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private E[] elements;
    private int size;
    private static final int DEFAULT_CAP = 16;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(int initialCapacity) {
        if (initialCapacity <= 0) initialCapacity = DEFAULT_CAP;
        elements = (E[]) new Object[initialCapacity];
        size = 0;
    }

    public MyPriorityQueue() {
        this(DEFAULT_CAP);
    }

    // Вспомогательные методы для кучи

    private int capacity() {
        return elements.length;
    }

    @SuppressWarnings("unchecked")
    private void grow() {
        int newCap = capacity() * 2 + 1;
        E[] newArr = (E[]) new Object[newCap];
        for (int i = 0; i < size; i++) newArr[i] = elements[i];
        elements = newArr;
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (a == null || b == null) throw new NullPointerException();
        // runtime cast — элементы должны быть Comparable
        return ((Comparable<? super E>) a).compareTo(b);
    }

    private void siftUp(int idx) {
        E x = elements[idx];
        while (idx > 0) {
            int parent = (idx - 1) >>> 1;
            if (compare(x, elements[parent]) >= 0) break;
            elements[idx] = elements[parent];
            idx = parent;
        }
        elements[idx] = x;
    }

    private void siftDown(int idx) {
        E x = elements[idx];
        int half = size >>> 1;
        while (idx < half) {
            int left = (idx << 1) + 1;
            int right = left + 1;
            int smallest = left;
            if (right < size && compare(elements[right], elements[left]) < 0) smallest = right;
            if (compare(elements[smallest], x) >= 0) break;
            elements[idx] = elements[smallest];
            idx = smallest;
        }
        elements[idx] = x;
    }

    private void removeAt(int idx) {
        if (idx < 0 || idx >= size) return;
        int last = size - 1;
        if (idx == last) {
            elements[last] = null;
            size--;
            return;
        }
        E moved = elements[last];
        elements[last] = null;
        size--;
        elements[idx] = moved;
        // попытаться сначала просеять вниз, если не получилось — вверх
        siftDown(idx);
        if (elements[idx] == moved) { // не сместился вниз
            siftUp(idx);
        }
    }

    private int indexOf(Object o) {
        if (o == null) return -1;
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) return i;
        }
        return -1;
    }

    //  Обязательные методы

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(elements[i]);
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) elements[i] = null;
        size = 0;
    }

    @Override
    public boolean add(E element) {
        if (element == null) throw new NullPointerException("null elements not supported");
        if (size >= capacity()) grow();
        elements[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E remove() {
        E e = poll();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E res = elements[0];
        int last = size - 1;
        elements[0] = elements[last];
        elements[last] = null;
        size--;
        if (size > 0) siftDown(0);
        return res;
    }

    @Override
    public E peek() {
        return size == 0 ? null : elements[0];
    }

    @Override
    public E element() {
        E e = peek();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    //  Методы для работы с коллекциями

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;
        for (E e : c) {
            add(e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        int newSize = 0;
        boolean changed = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                elements[newSize++] = elements[i];
            } else {
                changed = true;
            }
        }
        for (int i = newSize; i < size; i++) {
            elements[i] = null;
        }
        size = newSize;
        heapify();
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        int newSize = 0;
        boolean changed = false;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                elements[newSize++] = elements[i];
            } else {
                changed = true;
            }
        }
        for (int i = newSize; i < size; i++) {
            elements[i] = null;
        }
        size = newSize;
        heapify();
        return changed;
    }


    //  Доп. методы интерфейса Collection

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    @Override public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cur = 0;
            @Override
            public boolean hasNext() { return cur < size; }
            @Override
            public E next() {
                if (cur >= size) throw new NoSuchElementException();
                return elements[cur++];
            }
            @Override
            public void remove() { throw new UnsupportedOperationException(); }
        };
    }
    @Override public boolean remove(Object o) {
        int idx = indexOf(o);
        if (idx == -1) return false;
        removeAt(idx);
        return true;
    }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }

}

