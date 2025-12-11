package by.it.group451003.yepanchuntsau.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    @SuppressWarnings("unchecked")
    private E[] data = (E[]) new Object[8];
    private int head = 0;
    private int tail = 0;
    private int size = 0;

    private int inc(int i) { return (++i == data.length) ? 0 : i; }
    private int dec(int i) { return (i == 0) ? data.length - 1 : i - 1; }

    @SuppressWarnings("unchecked")
    private void ensureCapacity(int need) {
        if (need <= data.length) return;
        int newCap = data.length + (data.length >> 1) + 1;
        if (newCap < need) newCap = need;
        E[] nd = (E[]) new Object[newCap];
        for (int i = 0, idx = head; i < size; i++, idx = inc(idx)) nd[i] = data[idx];
        data = nd;
        head = 0;
        tail = size;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0, idx = head; i < size; i++, idx = inc(idx)) {
            sb.append(data[idx]);
            if (i + 1 < size) sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean add(E e) { addLast(e); return true; }

    @Override
    public void addFirst(E e) {
        ensureCapacity(size + 1);
        head = dec(head);
        data[head] = e;
        size++;
    }

    @Override
    public void addLast(E e) {
        ensureCapacity(size + 1);
        data[tail] = e;
        tail = inc(tail);
        size++;
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return data[head];
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return data[head];
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return data[dec(tail)];
    }

    @Override
    public E poll() { return pollFirst(); }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E v = data[head];
        data[head] = null;
        head = inc(head);
        size--;
        return v;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        tail = dec(tail);
        E v = data[tail];
        data[tail] = null;
        size--;
        return v;
    }


    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e)  { addLast(e);  return true; }
    @Override public boolean offer(E e)      { return add(e); }

    @Override public E removeFirst() { E x = pollFirst(); if (x==null) throw new NoSuchElementException(); return x; }
    @Override public E removeLast()  { E x = pollLast();  if (x==null) throw new NoSuchElementException(); return x; }
    @Override public E remove()      { E x = poll();      if (x==null) throw new NoSuchElementException(); return x; }

    @Override public E peekFirst() { return size==0 ? null : data[head]; }
    @Override public E peekLast()  { return size==0 ? null : data[dec(tail)]; }
    @Override public E peek()      { return peekFirst(); }

    @Override public void push(E e) { addFirst(e); }
    @Override public E pop()        { return removeFirst(); }

    @Override public boolean removeFirstOccurrence(Object o) {
        boolean removed = false;
        int n = size;
        for (int i = 0; i < n; i++) {
            E v = pollFirst();
            if (!removed && (o == null ? v == null : o.equals(v))) { removed = true; }
            else addLast(v);
        }
        return removed;
    }

    @Override public boolean removeLastOccurrence(Object o) {
        boolean removed = false;
        int n = size;
        for (int i = 0; i < n; i++) {
            E v = pollLast();
            if (!removed && (o == null ? v == null : o.equals(v))) { removed = true; }
            else addFirst(v);
        }
        return removed;
    }

    @Override public boolean isEmpty() { return size == 0; }

    @Override
    public boolean contains(Object o) {
        for (int i = 0, idx = head; i < size; i++, idx = inc(idx)) {
            if (o == null ? data[idx] == null : o.equals(data[idx])) return true;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int seen = 0, idx = head;
            @Override public boolean hasNext() { return seen < size; }
            @Override public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                E v = data[idx];
                idx = inc(idx);
                seen++;
                return v;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] a = new Object[size];
        int i = 0;
        for (E e : this) a[i++] = e;
        return a;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (E e : this) a[i++] = (T) e;
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) { addLast(e); changed = true; }
        return changed;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object x : c) if (!contains(x)) return false;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        int n = size;
        for (int i = 0; i < n; i++) {
            E v = pollFirst();
            if (c.contains(v)) changed = true;
            else addLast(v);
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        int n = size;
        for (int i = 0; i < n; i++) {
            E v = pollFirst();
            if (c.contains(v)) addLast(v);
            else changed = true;
        }
        return changed;
    }

    @Override
    public boolean remove(Object o) { return removeFirstOccurrence(o); }

    @Override
    public void clear() {
        for (int i = 0; i < data.length; i++) data[i] = null;
        head = tail = size = 0;
    }

    @Override public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            int seen = 0, idx = dec(tail);
            @Override public boolean hasNext() { return seen < size; }
            @Override public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                E v = data[idx];
                idx = dec(idx);
                seen++;
                return v;
            }
        };
    }
}
