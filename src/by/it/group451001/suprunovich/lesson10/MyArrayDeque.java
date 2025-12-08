package by.it.group451001.suprunovich.lesson10;

public class MyArrayDeque<E> implements java.util.Deque<E> {
    private E[] array;
    private int head;
    private int tail;
    private int size;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        array = (E[]) new Object[16];
        head = 0;
        tail = 0;
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        int curr = head;
        for (int i = 0; i < size; i++) {
            sb.append(array[curr]);
            if (i < size - 1) sb.append(", ");
            curr = (curr + 1) % array.length;
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
        if (size == array.length) resize();
        head = (head - 1 + array.length) % array.length;
        array[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        if (size == array.length) resize();
        array[tail] = element;
        tail = (tail + 1) % array.length;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new java.util.NoSuchElementException("Deque is empty");
        return array[head];
    }

    @Override
    public E getLast() {
        if (size == 0) throw new java.util.NoSuchElementException("Deque is empty");
        return array[(tail - 1 + array.length) % array.length];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E element = array[head];
        array[head] = null;
        head = (head + 1) % array.length;
        size--;
        return element;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        tail = (tail - 1 + array.length) % array.length;
        E element = array[tail];
        array[tail] = null;
        size--;
        return element;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        E[] newArray = (E[]) new Object[array.length * 2];
        for (int i = 0; i < size; i++) {
            newArray[i] = array[(head + i) % array.length];
        }
        array = newArray;
        head = 0;
        tail = size;
    }

    // Остальные методы Deque остаются не реализованными:
    @Override public boolean isEmpty() { return size == 0; }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean offerFirst(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean offerLast(E e) { throw new UnsupportedOperationException(); }
    @Override public E remove() { throw new UnsupportedOperationException(); }
    @Override public E removeFirst() { throw new UnsupportedOperationException(); }
    @Override public E removeLast() { throw new UnsupportedOperationException(); }
    @Override public E peek() { throw new UnsupportedOperationException(); }
    @Override public E peekFirst() { throw new UnsupportedOperationException(); }
    @Override public E peekLast() { throw new UnsupportedOperationException(); }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public void push(E e) { throw new UnsupportedOperationException(); }
    @Override public E pop() { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
}
