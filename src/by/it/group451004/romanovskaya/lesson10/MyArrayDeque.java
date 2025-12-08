package by.it.group451004.romanovskaya.lesson10;
import java.util.Deque;
import java.util.NoSuchElementException;
public class MyArrayDeque<E> implements Deque<E> {
    private static final int INITIAL_CAPACITY = 16;
    private E[] elements;
    private int head; // индекс первого элемента
    private int tail; // индекс следующей позиции после последнего элемента
    private int size;
    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[INITIAL_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;

}

    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = elements.length * 2;
            @SuppressWarnings("unchecked")
            E[] newElements = (E[]) new Object[newCapacity];
            if (head < tail) {
                System.arraycopy(elements, head, newElements, 0, size);
            } else {
                System.arraycopy(elements, head, newElements, 0, elements.length - head);
                System.arraycopy(elements, 0, newElements, elements.length - head, tail);
            }
            elements = newElements;
            head = 0;
            tail = size;
        }
    }
    private int decrement(int index) {
        return (index - 1 + elements.length) % elements.length;
    }

    private int increment(int index) {
        return (index + 1) % elements.length;
    }

    @Override
    public void addFirst(E element) {
        if (element == null) throw new NullPointerException();
        ensureCapacity();
        head = decrement(head);
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        if (element == null) throw new NullPointerException();
        ensureCapacity();
        elements[tail] = element;
        tail = increment(tail);
        size++;
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return elements[head];
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return elements[decrement(tail)];
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E element = elements[head];
        elements[head] = null;
        head = increment(head);
        size--;
        return element;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        tail = decrement(tail);
        E element = elements[tail];
        elements[tail] = null;
        size--;
        return element;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0, idx = head; i < size; i++, idx = increment(idx)) {
            sb.append(elements[idx]);
            if (i != size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // Остальные методы интерфейса Deque не реализованы
    @Override public boolean offerFirst(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean offerLast(E e) { throw new UnsupportedOperationException(); }
    @Override public E peek() { throw new UnsupportedOperationException(); }
    @Override public E peekFirst() { throw new UnsupportedOperationException(); }
    @Override public E peekLast() { throw new UnsupportedOperationException(); }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { throw new UnsupportedOperationException(); }
    @Override public E remove() { throw new UnsupportedOperationException(); }
    @Override public E removeFirst() { throw new UnsupportedOperationException(); }
    @Override public E removeLast() { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    @Override public boolean isEmpty() { return size == 0; }
    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return removeFirst(); }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() { while (pollFirst() != null); }
}


