package by.it.group451004.rublevskaya.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    private static final int CAPACITY = 10;
    private Object[] data;
    private int currentSize;

    public ListC() {
        data = new Object[CAPACITY];
        currentSize = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder resultStr = new StringBuilder();
        resultStr.append("[");
        for (int i = 0; i < currentSize; i++) {
            resultStr.append(data[i]);
            if (i < currentSize - 1) {
                resultStr.append(", ");
            }
        }
        resultStr.append("]");
        return resultStr.toString();
    }

    @Override
    public boolean add(E e) {
        if (currentSize == data.length) {
            resize(null);
        }
        data[currentSize++] = e;
        return true;
    }

    @Override
    public E remove(int index) {

        isCorrectIndex(index);
        E delElm = (E) data[index];
        System.arraycopy(data, index + 1, data, index, currentSize - index - 1);
        currentSize--;
        data[currentSize] = null;
        return delElm;

    }

    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > currentSize) {
            throw new IndexOutOfBoundsException();
        }
        if (currentSize == data.length) {
            resize(null);
        }
        System.arraycopy(data, index, data, index + 1, currentSize - index);
        data[index] = element;
        currentSize++;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index >= 0) {
            remove(index);
            return true;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        isCorrectIndex(index);
        E oldElement = (E) data[index];
        data[index] = element;
        return oldElement;
    }


    @Override
    public boolean isEmpty() {
        return currentSize == 0;
    }


    @Override
    public void clear() {
        for (int i = 0; i < currentSize; i++) {
            data[i] = null;
        }
        currentSize = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < currentSize; i++) {
            if (data[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        isCorrectIndex(index);
        return (E) data[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = currentSize - 1; i >= 0; i--) {
            if (data[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            modified |= add(e);
        }
        return modified;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > currentSize) {
            throw new IndexOutOfBoundsException();
        }
        if (c.isEmpty()) {
            return false;
        }
        resize(currentSize + c.size());
        int numNew = c.size();
        System.arraycopy(data, index, data, index + numNew, currentSize - index);
        int i = index;
        for (E e : c) {
            data[i++] = e;
        }
        currentSize += numNew;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
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
        return new Iterator<E>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < currentSize;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new IndexOutOfBoundsException();
                }
                return (E) data[currentIndex++];
            }

            @Override
            public void remove() {
                if (currentIndex <= 0 || currentIndex > currentSize) {
                    throw new IllegalStateException();
                }
                ListC.this.remove(--currentIndex);
            }
        };
    }

    private void resize(Integer Capacity) {
        if (Capacity == null) {
            Capacity = data.length * 2;
        }
        if (Capacity > data.length) {
            data = java.util.Arrays.copyOf(data, Capacity);
        }
    }


    private void isCorrectIndex(int currentIndex) {
        if (currentIndex < 0 || currentIndex >= currentSize) {
            throw new IndexOutOfBoundsException("Index: " + currentIndex + ", Size: " + currentSize);
        }
    }
}