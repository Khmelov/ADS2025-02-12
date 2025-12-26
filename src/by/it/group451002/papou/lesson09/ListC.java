package by.it.group451002.papou.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    // Стандартная вместимость массива при создании без параметров
    private static final int DEFAULT_CAPACITY = 10;
    // Внутренний массив для хранения элементов
    private Object[] elements;
    // Текущее количество элементов
    private int size;

    // Конструктор по умолчанию — создаёт массив стандартного размера
    public ListC() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    // Конструктор с начальной вместимостью
    public ListC(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
        this.elements = new Object[initialCapacity];
        this.size = 0;
    }

    // Конструктор от коллекции — добавляем все элементы
    public ListC(Collection<? extends E> c) {
        this();
        if (c != null) {
            addAll(c);
        }
    }

    // Преобразование списка в строку
    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // Добавление элемента в конец
    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1);
        elements[size++] = e;
        return true;
    }

    // Удаление по индексу
    @Override
    public E remove(int index) {
        checkIndex(index);
        @SuppressWarnings("unchecked")
        E removedElement = (E) elements[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null;
        return removedElement;
    }

    // Размер списка
    @Override
    public int size() {
        return size;
    }

    // Вставка по индексу
    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index);
        ensureCapacity(size + 1);
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }

    // Удаление по объекту
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && elements[i] == null) || (o != null && o.equals(elements[i]))) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    // Замена по индексу
    @Override
    public E set(int index, E element) {
        checkIndex(index);
        @SuppressWarnings("unchecked")
        E oldValue = (E) elements[index];
        elements[index] = element;
        return oldValue;
    }

    // Проверка на пустоту
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Очистка списка
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) elements[i] = null;
        size = 0;
    }

    // Поиск первого вхождения
    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && elements[i] == null) || (o != null && o.equals(elements[i]))) {
                return i;
            }
        }
        return -1;
    }

    // Получение по индексу
    @Override
    public E get(int index) {
        checkIndex(index);
        @SuppressWarnings("unchecked")
        E element = (E) elements[index];
        return element;
    }

    // Проверка, есть ли элемент
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    // Поиск последнего вхождения
    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if ((o == null && elements[i] == null) || (o != null && o.equals(elements[i]))) {
                return i;
            }
        }
        return -1;
    }

    // Проверка containsAll
    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException("Collection cannot be null");
        for (Object element : c) {
            if (!contains(element)) return false;
        }
        return true;
    }

    // Добавление всех элементов
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException("Collection cannot be null");
        if (c.isEmpty()) return false;
        ensureCapacity(size + c.size());
        for (E element : c) elements[size++] = element;
        return true;
    }

    // Вставка коллекции по индексу
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        checkIndexForAdd(index);
        if (c == null) throw new NullPointerException("Collection cannot be null");
        if (c.isEmpty()) return false;
        int numNew = c.size();
        ensureCapacity(size + numNew);
        int numMoved = size - index;
        if (numMoved > 0) {
            System.arraycopy(elements, index, elements, index + numNew, numMoved);
        }
        int i = index;
        for (E element : c) elements[i++] = element;
        size += numNew;
        return true;
    }

    // Удаление всех элементов, которые есть в коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException("Collection cannot be null");
        if (c.isEmpty()) return false;
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                remove(i);
                i--;
                modified = true;
            }
        }
        return modified;
    }

    // Оставляем только те элементы, которые есть в коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException("Collection cannot be null");
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(i);
                i--;
                modified = true;
            }
        }
        return modified;
    }

    // Возвращает подсписок [fromIndex, toIndex)
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        checkIndex(fromIndex);
        checkIndexForAdd(toIndex);
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
        }
        ListC<E> subList = new ListC<>(toIndex - fromIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            @SuppressWarnings("unchecked")
            E element = (E) elements[i];
            subList.add(element);
        }
        return subList;
    }

    // Итератор с позиции
    @Override
    public ListIterator<E> listIterator(int index) {
        checkIndexForAdd(index);
        return new ListCListIterator(index);
    }

    // Итератор с начала
    @Override
    public ListIterator<E> listIterator() {
        return new ListCListIterator(0);
    }

    // Копирование в массив
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a == null) throw new NullPointerException("Array cannot be null");
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) a[size] = null;
        return a;
    }

    // Возвращает массив объектов
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        System.arraycopy(elements, 0, array, 0, size);
        return array;
    }

    // Вспомогательные методы
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length * 2;
            if (newCapacity < minCapacity) newCapacity = minCapacity;
            Object[] newElements = new Object[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    // Внутренний класс: реализация ListIterator
    private class ListCListIterator implements ListIterator<E> {
        private int currentIndex;
        private int lastReturnedIndex = -1;

        public ListCListIterator(int index) {
            this.currentIndex = index;
        }

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            lastReturnedIndex = currentIndex;
            return (E) elements[currentIndex++];
        }

        @Override
        public boolean hasPrevious() {
            return currentIndex > 0;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E previous() {
            if (!hasPrevious()) throw new java.util.NoSuchElementException();
            lastReturnedIndex = --currentIndex;
            return (E) elements[currentIndex];
        }

        @Override
        public int nextIndex() {
            return currentIndex;
        }

        @Override
        public int previousIndex() {
            return currentIndex - 1;
        }

        @Override
        public void remove() {
            if (lastReturnedIndex == -1) throw new IllegalStateException();
            ListC.this.remove(lastReturnedIndex);
            if (lastReturnedIndex < currentIndex) currentIndex--;
            lastReturnedIndex = -1;
        }

        @Override
        public void set(E e) {
            if (lastReturnedIndex == -1) throw new IllegalStateException();
            ListC.this.set(lastReturnedIndex, e);
        }

        @Override
        public void add(E e) {
            ListC.this.add(currentIndex, e);
            currentIndex++;
            lastReturnedIndex = -1;
        }
    }

    // Реализация простого Iterator (например, для foreach)
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;
            private int lastReturnedIndex = -1;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (!hasNext()) throw new java.util.NoSuchElementException();
                lastReturnedIndex = currentIndex;
                return (E) elements[currentIndex++];
            }

            @Override
            public void remove() {
                if (lastReturnedIndex == -1) throw new IllegalStateException();
                ListC.this.remove(lastReturnedIndex);
                currentIndex = lastReturnedIndex;
                lastReturnedIndex = -1;
            }
        };
    }
}