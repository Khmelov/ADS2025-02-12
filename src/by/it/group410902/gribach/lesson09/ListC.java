package by.it.group410902.gribach.lesson09;

import java.util.*;

public class ListC<E> implements List<E> {
    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    private E[] elements;   // массив для хранения
    private int size = 0;   // количество элементов

    @SuppressWarnings("unchecked")
    public ListC() {
        elements = (E[]) new Object[10]; // стартовая ёмкость
    }

    private void ensureCapacity(int newSize) {
        if (newSize > elements.length) {
            int newCapacity = Math.max(elements.length * 2, newSize);
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1);
        elements[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        E old = elements[index];
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        elements[--size] = null;
        return old;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        ensureCapacity(size + 1);
        System.arraycopy(elements, index, elements, index + 1, size - index);
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
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        E old = elements[index];
        elements[index] = element;
        return old;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        Arrays.fill(elements, 0, size, null);
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
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return elements[index];
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

    // --- Методы для простоты оставим пустыми или базовыми ---
    @Override public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override public boolean addAll(Collection<? extends E> c) {
        ensureCapacity(size + c.size());
        for (E e : c) add(e);
        return !c.isEmpty();
    }

    @Override public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        if (c.isEmpty()) return false;
        ensureCapacity(size + c.size());
        System.arraycopy(elements, index, elements, index + c.size(), size - index);
        int i = index;
        for (E e : c) elements[i++] = e;
        size += c.size();
        return true;
    }

    @Override public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) while (remove(o)) changed = true;
        return changed;
    }

    @Override public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(i--);
                changed = true;
            }
        }
        return changed;
    }

    // --- Опциональные методы ---
    @Override public List<E> subList(int fromIndex, int toIndex) { throw new UnsupportedOperationException(); }
    @Override public ListIterator<E> listIterator(int index) { throw new UnsupportedOperationException(); }
    @Override public ListIterator<E> listIterator() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { return Arrays.copyOf(elements, size); }
    @Override public Iterator<E> iterator() {
        return new Iterator<E>() {
            int cursor = 0;
            @Override public boolean hasNext() { return cursor < size; }
            @Override public E next() { return elements[cursor++]; }
        };
    }
}
