package by.it.group451001.puzik.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    private Object[] elements = new Object[10];
    private int size = 0;

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append('[');
        for (int i = 0; i < size; i++) {
            if (i > 0) out.append(", ");
            out.append(elements[i]);
        }
        out.append(']');
        return out.toString();
    }

    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1);
        elements[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        @SuppressWarnings("unchecked")
        E old = (E) elements[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null;
        return old;
    }

    @Override
    public int size() {
        return size;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        ensureCapacity(size + 1);
        int numMoved = size - index;
        if (numMoved > 0) {
            System.arraycopy(elements, index, elements, index + 1, numMoved);
        }
        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexOf(o);
        if (idx >= 0) {
            remove(idx);
            return true;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
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
        checkIndex(index);
        @SuppressWarnings("unchecked")
        E value = (E) elements[index];
        return value;
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
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) return false;
        ensureCapacity(size + c.size());
        for (E e : c) elements[size++] = e;
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        if (c.isEmpty()) return false;
        int m = c.size();
        ensureCapacity(size + m);
        System.arraycopy(elements, index, elements, index + m, size - index);
        int k = 0;
        for (E e : c) elements[index + k++] = e;
        size += m;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int w = 0;
        boolean changed = false;
        for (int r = 0; r < size; r++) {
            Object el = elements[r];
            if (!c.contains(el)) {
                elements[w++] = el;
            } else {
                changed = true;
            }
        }
        for (int i = w; i < size; i++) elements[i] = null;
        if (changed) size = w;
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int w = 0;
        boolean changed = false;
        for (int r = 0; r < size; r++) {
            Object el = elements[r];
            if (c.contains(el)) {
                elements[w++] = el;
            } else {
                changed = true;
            }
        }
        for (int i = w; i < size; i++) elements[i] = null;
        if (changed) size = w;
        return changed;
    }


    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            // noinspection unchecked
            return (T[]) java.util.Arrays.copyOf(elements, size, a.getClass());
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public Object[] toArray() {
        return java.util.Arrays.copyOf(elements, size);
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    private void ensureCapacity(int minCapacity) {
        if (elements.length >= minCapacity) return;
        int newCapacity = elements.length + (elements.length >> 1) + 1;
        if (newCapacity < minCapacity) newCapacity = minCapacity;
        elements = java.util.Arrays.copyOf(elements, newCapacity);
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
}
