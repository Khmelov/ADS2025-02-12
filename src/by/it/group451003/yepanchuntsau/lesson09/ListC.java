package by.it.group451003.yepanchuntsau.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    private E[] data;     // буфер
    private int size;     // текущее количество элементов

    @SuppressWarnings("unchecked")
    public ListC() {
        // стартовая емкость — 10 (как у ArrayList)
        data = (E[]) new Object[10];
        size = 0;
    }

    // Увеличение емкости при необходимости (рост ~1.5x + 1)
    @SuppressWarnings("unchecked")
    private void ensureCapacity(int minCapacity) {
        if (data.length >= minCapacity) return;
        int newCap = data.length + (data.length >> 1) + 1; // 1.5x + 1
        if (newCap < minCapacity) newCap = minCapacity;
        E[] newArr = (E[]) new Object[newCap];
        System.arraycopy(data, 0, newArr, 0, size);
        data = newArr;
    }

    // Проверка индекса для чтения/записи существующего элемента
    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
    }

    // Проверка индекса для вставки (index может быть == size)
    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
    }

    // --------------------------------------
    // Обязательные к реализации методы
    // --------------------------------------

    @Override
    public String toString() {
        // формат: [a, b, c]
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            E el = data[i];
            sb.append(el);
            if (i != size - 1) sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1);
        data[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        E old = data[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(data, index + 1, data, index, numMoved);
        }
        data[--size] = null; // help GC
        return old;
    }

    @Override
    public int size() {
        return size;
    }

    // --------------------------------------
    // Опциональные (реализованы для полноты)
    // --------------------------------------

    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index);
        ensureCapacity(size + 1);
        int numMoved = size - index;
        if (numMoved > 0) {
            System.arraycopy(data, index, data, index + 1, numMoved);
        }
        data[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        // удаляет первый найденный
        for (int i = 0; i < size; i++) {
            if (o == null ? data[i] == null : o.equals(data[i])) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        E old = data[index];
        data[index] = element;
        return old;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        // обнуляем ссылки
        for (int i = 0; i < size; i++) data[i] = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? data[i] == null : o.equals(data[i])) return i;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        return data[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (o == null ? data[i] == null : o.equals(data[i])) return i;
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object x : c) {
            if (!contains(x)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        int addCount = c.size();
        if (addCount == 0) return false;
        ensureCapacity(size + addCount);
        for (E x : c) {
            data[size++] = x;
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        checkIndexForAdd(index);
        int addCount = c.size();
        if (addCount == 0) return false;
        ensureCapacity(size + addCount);
        int numMoved = size - index;
        if (numMoved > 0) {
            System.arraycopy(data, index, data, index + addCount, numMoved);
        }
        int i = index;
        for (E x : c) data[i++] = x;
        size += addCount;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(data[i])) {
                data[newSize++] = data[i];
            } else {
                changed = true;
            }
        }
        // null out tail
        for (int i = newSize; i < size; i++) data[i] = null;
        size = newSize;
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(data[i])) {
                data[newSize++] = data[i];
            } else {
                changed = true;
            }
        }
        for (int i = newSize; i < size; i++) data[i] = null;
        size = newSize;
        return changed;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return null;
    }

}
