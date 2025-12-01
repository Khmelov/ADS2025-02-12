package by.it.group451001.puzik.lesson10;

import java.util.*;

public class MyArrayDeque<E> implements Deque<E> {

    private Object[] elements = new Object[8];
    private int head = 0; // index of first element
    private int tail = 0; // index after last element
    private int size = 0;

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append('[');
        for (int i = 0; i < size; i++) {
            if (i > 0) out.append(", ");
            out.append(elementAt(i));
        }
        out.append(']');
        return out.toString();
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
        ensureCapacity(size + 1);
        head = dec(head);
        elements[head] = e;
        size++;
    }

    @Override
    public void addLast(E e) {
        ensureCapacity(size + 1);
        elements[tail] = e;
        tail = inc(tail);
        size++;
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        @SuppressWarnings("unchecked") E v = (E) elements[head];
        return v;
    }

    @Override
    public E getFirst() {
        return element();
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        int lastIndex = dec(tail);
        @SuppressWarnings("unchecked") E v = (E) elements[lastIndex];
        return v;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        @SuppressWarnings("unchecked") E v = (E) elements[head];
        elements[head] = null;
        head = inc(head);
        size--;
        return v;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        tail = dec(tail);
        @SuppressWarnings("unchecked") E v = (E) elements[tail];
        elements[tail] = null;
        size--;
        return v;
    }

    // --------- Helpers ---------
    private void ensureCapacity(int minCapacity) {
        if (elements.length >= minCapacity) return;
        int newCap = elements.length * 2;
        if (newCap < minCapacity) newCap = minCapacity;
        Object[] newArr = new Object[newCap];
        for (int i = 0; i < size; i++) newArr[i] = elementAt(i);
        elements = newArr;
        head = 0;
        tail = size;
    }

    private Object elementAt(int logicalIndex) {
        int idx = (head + logicalIndex) % elements.length;
        return elements[idx];
    }

    private int inc(int i) {
        i++;
        if (i == elements.length) i = 0;
        return i;
    }

    private int dec(int i) {
        i--;
        if (i < 0) i = elements.length - 1;
        return i;
    }

    // --------- Unused/unsupported methods for this task ---------
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E removeFirst() { E v = pollFirst(); if (v==null) throw new NoSuchElementException(); return v; }
    @Override public E removeLast() { E v = pollLast(); if (v==null) throw new NoSuchElementException(); return v; }
    @Override public E peekFirst() { return size==0? null : getFirst(); }
    @Override public E peekLast() { return size==0? null : getLast(); }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { return add(e); }
    @Override public E remove() { E v = poll(); if (v==null) throw new NoSuchElementException(); return v; }
    @Override public E peek() { return size==0? null : element(); }
    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { E v = removeFirst(); return v; }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c) { boolean ch=false; for (E e: c){ addLast(e); ch=true;} return ch; }
    @Override public void clear() { while (pollFirst()!=null){} }
    @Override public boolean contains(Object o) { for (int i=0;i<size;i++){ Object v=elementAt(i); if (Objects.equals(v,o)) return true; } return false; }
    @Override public boolean containsAll(Collection<?> c) { for (Object o:c) if(!contains(o)) return false; return true; }
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { Object[] a=new Object[size]; for(int i=0;i<size;i++) a[i]=elementAt(i); return a; }
    @Override public <T> T[] toArray(T[] a) { if (a.length<size) a=Arrays.copyOf(a,size); for(int i=0;i<size;i++){ @SuppressWarnings("unchecked") T v=(T)elementAt(i); a[i]=v;} if(a.length>size) a[size]=null; return a; }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean isEmpty() { return size==0; }
}


