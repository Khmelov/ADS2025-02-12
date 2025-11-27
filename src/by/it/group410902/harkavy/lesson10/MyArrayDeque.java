package by.it.group410902.harkavy.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    // кольцевой массив
    @SuppressWarnings("unchecked")
    private E[] a = (E[]) new Object[8];

    // head указывает на первый элемент, tail — на место вставки после последнего
    private int head = 0;
    private int tail = 0;
    private int size = 0;


    private int cap() { return a.length; }

    // переход вперёд по кольцу
    private int inc(int i) { return (i + 1) % cap(); }

    // переход назад по кольцу
    private int dec(int i) { return (i - 1 + cap()) % cap(); }

    // увеличение массива при заполнении
    @SuppressWarnings("unchecked")
    private void growIfNeeded() {
        if (size != cap()) return;
        E[] b = (E[]) new Object[cap() * 2];
        for (int i = 0; i < size; i++) {
            b[i] = a[(head + i) % cap()];
        }
        a = b;
        head = 0;
        tail = size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(a[(head + i) % cap()]);
            if (i + 1 < size) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() {
        return size;
    }


    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void addFirst(E e) {
        growIfNeeded();
        head = dec(head);      // двигаем голову назад
        a[head] = e;           // кладём элемент в новое место головы
        size++;
    }

    @Override
    public void addLast(E e) {
        growIfNeeded();
        a[tail] = e;           // кладём элемент по tail
        tail = inc(tail);      // сдвигаем tail вперёд
        size++;
    }


    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return a[head];
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return a[dec(tail)];
    }


    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E e = a[head];         // берём первый элемент
        a[head] = null;        // освобождаем ссылку
        head = inc(head);      // смещаем голову
        size--;
        return e;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        tail = dec(tail);      // двигаем tail назад, теперь там последний элемент
        E e = a[tail];
        a[tail] = null;
        size--;
        return e;
    }


    // ======================= всё остальное не нужно =======================

    private void unsupported() {
        throw new UnsupportedOperationException("Not required by the assignment");
    }

    @Override public boolean isEmpty() { return size == 0; }
    @Override public void clear() { head = tail = size = 0; a = (E[]) new Object[8]; }

    @Override public boolean remove(Object o) { unsupported(); return false; }
    @Override public E remove() { unsupported(); return null; }
    @Override public E removeFirst() { unsupported(); return null; }
    @Override public E removeLast() { unsupported(); return null; }
    @Override public E peek() { unsupported(); return null; }
    @Override public E peekFirst() { unsupported(); return null; }
    @Override public E peekLast() { unsupported(); return null; }
    @Override public boolean offer(E e) { unsupported(); return false; }
    @Override public boolean offerFirst(E e) { unsupported(); return false; }
    @Override public boolean offerLast(E e) { unsupported(); return false; }
    @Override public void push(E e) { unsupported(); }
    @Override public E pop() { unsupported(); return null; }
    @Override public boolean removeFirstOccurrence(Object o) { unsupported(); return false; }
    @Override public boolean removeLastOccurrence(Object o) { unsupported(); return false; }
    @Override public Iterator<E> iterator() { unsupported(); return null; }
    @Override public Iterator<E> descendingIterator() { unsupported(); return null; }
    @Override public boolean contains(Object o) { unsupported(); return false; }
    @Override public boolean containsAll(Collection<?> c) { unsupported(); return false; }
    @Override public boolean addAll(Collection<? extends E> c) { unsupported(); return false; }
    @Override public boolean removeAll(Collection<?> c) { unsupported(); return false; }
    @Override public boolean retainAll(Collection<?> c) { unsupported(); return false; }
    @Override public Object[] toArray() { unsupported(); return null; }
    @Override public <T> T[] toArray(T[] a) { unsupported(); return null; }
}
