package by.it.group410901.garkusha.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size;

    public ListA() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public ListA(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
        this.elements = new Object[initialCapacity];
        this.size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        String result = "[" + elements[0];
        for (int i = 1; i < size; i++) {
            result += ", " + elements[i];
        }
        result += "]";
        return result;
    }

    @Override
    public boolean add(E e) {
        // Проверяем, нужно ли расширить массив
        if (size == elements.length) {
            // Увеличиваем массив в 2 раза
            Object[] newElements = new Object[elements.length * 2];
            // Копируем старые элементы
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
        elements[size] = e;
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        @SuppressWarnings("unchecked")
        E removed = (E) elements[index];

        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;
        return removed;
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

        if (size == elements.length) {
            Object[] newArray = new Object[elements.length * 2];
            for (int i = 0; i < size; i++) {
                newArray[i] = elements[i];
            }
            elements = newArray;
        }

        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        @SuppressWarnings("unchecked")
        E oldValue = (E) elements[index];
        elements[index] = element;
        return oldValue;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        @SuppressWarnings("unchecked")
        E element = (E) elements[index];
        return element;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        }
        for (E item : c) {
            add(item);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        int i = index;
        for (E item : c) {
            add(i, item);
            i++;
        }

        return !c.isEmpty();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                remove(i);
                i--; // Уменьшаем счетчик т.к. элементы сдвинулись
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(i);
                i--; // Уменьшаем счетчик т.к. элементы сдвинулись
                modified = true;
            }
        }
        return modified;
    }


    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }

        // Создаем новый список для подсписка
        ListA<E> subList = new ListA<>(toIndex - fromIndex);

        // Копируем элементы из диапазона
        for (int i = fromIndex; i < toIndex; i++) {
            subList.add(this.get(i));
        }

        return subList;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return new ListIteratorImpl(index);
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ListIteratorImpl(0);
    }

    // Внутренний класс ListIteratorImpl
    private class ListIteratorImpl implements ListIterator<E> {
        private int cursor; // текущая позиция итератора
        private int lastRet = -1; // индекс последнего возвращенного элемента

        public ListIteratorImpl(int index) {
            this.cursor = index;
        }

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                return null;
            }
            lastRet = cursor;
            @SuppressWarnings("unchecked")
            E element = (E) elements[cursor++];
            return element;
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0;
        }

        @Override
        public E previous() {
            if (!hasPrevious()) {
                return null;
            }
            cursor--;
            lastRet = cursor;
            @SuppressWarnings("unchecked")
            E element = (E) elements[cursor];
            return element;
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
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            ListA.this.remove(lastRet);
            cursor = lastRet;
            lastRet = -1;
        }

        @Override
        public void set(E e) {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            ListA.this.set(lastRet, e);
        }

        @Override
        public void add(E e) {
            ListA.this.add(cursor, e);
            cursor++;
            lastRet = -1;
        }
    }

    @Override
    public <T> T[] toArray(T[] a) {
        // Создаем новый массив если переданный слишком мал
        if (a.length < size) {
            a = (T[]) new Object[size];
        }

        // Копируем элементы
        for (int i = 0; i < size; i++) {
            a[i] = (T) elements[i];
        }

        // Заполняем оставшиеся позиции null
        for (int i = size; i < a.length; i++) {
            a[i] = null;
        }

        return a;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = elements[i];
        }
        return result;
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
                return currentIndex < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                return (E) elements[currentIndex++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove not supported");
            }
        };
    }

}
