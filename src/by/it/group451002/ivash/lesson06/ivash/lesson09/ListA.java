package by.it.group451002.ivash.lesson06.ivash.lesson09;

import java.lang.reflect.Array;
import java.util.*;

public class ListA<E> implements List<E> {

    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 10;

    public ListA() {
        elements = new Object[DEFAULT_CAPACITY];
    }

    public ListA(int initialCapacity) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        elements = new Object[Math.max(DEFAULT_CAPACITY, initialCapacity)];
    }

    private void ensureCapacity(int minCapacity) {
        if (elements.length >= minCapacity) return;
        int newCapacity = elements.length + (elements.length >> 1) + 1; // 1.5x + 1
        if (newCapacity < minCapacity) newCapacity = minCapacity;
        Object[] newArr = new Object[newCapacity];
        for (int i = 0; i < size; i++) newArr[i] = elements[i];
        elements = newArr;
    }

    private void rangeCheck(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    private void rangeCheckForAdd(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            Object e = elements[i];
            sb.append(e == this ? "(this Collection)" : String.valueOf(e));
            if (i != size - 1) sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1);
        elements[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        rangeCheck(index);
        @SuppressWarnings("unchecked")
        E old = (E) elements[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            for (int i = index; i < index + numMoved; i++)
                elements[i] = elements[i + 1];
        }
        elements[--size] = null; // help GC
        return old;
    }

    @Override
    public int size() {
        return size;
    }

    /* ----- Optional / additional methods ----- */

    @Override
    public void add(int index, E element) {
        rangeCheckForAdd(index);
        ensureCapacity(size + 1);
        for (int i = size; i > index; i--) elements[i] = elements[i - 1];
        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexOf(o);
        if (idx == -1) return false;
        remove(idx);
        return true;
    }

    @Override
    public E set(int index, E element) {
        rangeCheck(index);
        @SuppressWarnings("unchecked")
        E old = (E) elements[index];
        elements[index] = element;
        return old;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) elements[i] = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) if (elements[i] == null) return i;
        } else {
            for (int i = 0; i < size; i++) if (o.equals(elements[i])) return i;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        rangeCheck(index);
        @SuppressWarnings("unchecked")
        E e = (E) elements[index];
        return e;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) if (elements[i] == null) return i;
        } else {
            for (int i = size - 1; i >= 0; i--) if (o.equals(elements[i])) return i;
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        int numNew = c.size();
        if (numNew == 0) return false;
        ensureCapacity(size + numNew);
        for (E e : c) elements[size++] = e;
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        rangeCheckForAdd(index);
        if (c == null) throw new NullPointerException();
        int numNew = c.size();
        if (numNew == 0) return false;
        ensureCapacity(size + numNew);
        // shift tail
        for (int i = size - 1; i >= index; i--) elements[i + numNew] = elements[i];
        // copy new
        int i = index;
        for (E e : c) elements[i++] = e;
        size += numNew;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        int w = 0;
        boolean modified = false;
        for (int r = 0; r < size; r++) {
            if (!c.contains(elements[r])) {
                elements[w++] = elements[r];
            } else {
                modified = true;
            }
        }
        // clear tail
        for (int i = w; i < size; i++) elements[i] = null;
        if (modified) size = w;
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        int w = 0;
        boolean modified = false;
        for (int r = 0; r < size; r++) {
            if (c.contains(elements[r])) {
                elements[w++] = elements[r];
            } else {
                modified = true;
            }
        }
        for (int i = w; i < size; i++) elements[i] = null;
        if (modified) size = w;
        return modified;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex)
            throw new IndexOutOfBoundsException("fromIndex: " + fromIndex + ", toIndex: " + toIndex + ", Size: " + size);
        ListA<E> sub = new ListA<>(toIndex - fromIndex);
        for (int i = fromIndex; i < toIndex; i++) sub.add(get(i));
        return sub;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        rangeCheckForAdd(index);
        return new ListAListIterator(index);
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ListAListIterator(0);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a == null) throw new NullPointerException();
        if (a.length < size) {
            @SuppressWarnings("unchecked")
            T[] newArr = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
            for (int i = 0; i < size; i++) newArr[i] = (T) elements[i];
            return newArr;
        } else {
            for (int i = 0; i < size; i++) a[i] = (T) elements[i];
            if (a.length > size) a[size] = null;
            return a;
        }
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) arr[i] = elements[i];
        return arr;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    /* ===== Inner classes: iterator and listIterator ===== */

    private class Itr implements Iterator<E> {
        int cursor = 0;       // index of next element to return
        int lastRet = -1;     // index of last element returned; -1 if none

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            if (cursor >= size) throw new NoSuchElementException();
            @SuppressWarnings("unchecked")
            E e = (E) elements[cursor];
            lastRet = cursor++;
            return e;
        }

        @Override
        public void remove() {
            if (lastRet < 0) throw new IllegalStateException();
            ListA.this.remove(lastRet);
            if (lastRet < cursor) cursor--;
            lastRet = -1;
        }
    }

    private class ListAListIterator implements ListIterator<E> {
        int cursor;       // index of next element
        int lastRet = -1; // index of last returned element, -1 if none

        ListAListIterator(int index) {
            this.cursor = index;
        }

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            @SuppressWarnings("unchecked")
            E e = (E) elements[cursor];
            lastRet = cursor++;
            return e;
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0;
        }

        @Override
        public E previous() {
            if (!hasPrevious()) throw new NoSuchElementException();
            lastRet = --cursor;
            @SuppressWarnings("unchecked")
            E e = (E) elements[cursor];
            return e;
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void remove() {
            if (lastRet < 0) throw new IllegalStateException();
            ListA.this.remove(lastRet);
            if (lastRet < cursor) cursor--;
            lastRet = -1;
        }

        @Override
        public void set(E e) {
            if (lastRet < 0) throw new IllegalStateException();
            elements[lastRet] = e;
        }

        @Override
        public void add(E e) {
            int i = cursor;
            ListA.this.add(i, e);
            cursor = i + 1;
            lastRet = -1;
        }
    }

    /* ===== Unsupported (not required) =====
       Note: All List methods are implemented above. If some rarely-used
       List methods were omitted intentionally, they'd throw UnsupportedOperationException.
    ====== */
}
