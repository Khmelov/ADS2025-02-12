package by.it.group451003.sorokin.lesson09;

import java.util.*;

public class ListC<E> implements List<E> {
    private E[] elements;
    private int size;
    private static final int INITIAL_CAPACITY = 10;
    private static final int GROW_FACTOR = 2;

    // Конструктор
    @SuppressWarnings("unchecked")
    public ListC() {
        elements = (E[]) new Object[INITIAL_CAPACITY];
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        if (size == elements.length) {
            resize(elements.length * GROW_FACTOR);
        }
        elements[size] = e;
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);

        E removedElement = elements[index];

        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        elements[size - 1] = null;
        size--;

        return removedElement;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        // Проверяем индекс (допустим index == size для добавления в конец)
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        // Увеличиваем массив если нужно
        if (size == elements.length) {
            resize(elements.length * GROW_FACTOR);
        }

        // Сдвигаем элементы вправо чтобы освободить место
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }

        // Вставляем элемент
        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        // Ищем первый элемент равный o
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, elements[i])) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        E oldElement = elements[index];
        elements[index] = element;
        return oldElement;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        // Очищаем все ссылки для помощи GC
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        // Ищем первое вхождение элемента
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, elements[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        return elements[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        // Ищем последнее вхождение элемента
        for (int i = size - 1; i >= 0; i--) {
            if (Objects.equals(o, elements[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // Проверяем что все элементы коллекции содержатся в списке
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // Добавляем все элементы коллекции в конец списка
        if (c.isEmpty()) {
            return false;
        }

        // Увеличиваем массив если нужно
        int neededCapacity = size + c.size();
        if (neededCapacity > elements.length) {
            resize(Math.max(elements.length * GROW_FACTOR, neededCapacity));
        }

        // Добавляем все элементы
        for (E element : c) {
            elements[size++] = element;
        }

        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        // Проверяем индекс
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (c.isEmpty()) {
            return false;
        }

        int collectionSize = c.size();
        int neededCapacity = size + collectionSize;

        // Увеличиваем массив если нужно
        if (neededCapacity > elements.length) {
            resize(Math.max(elements.length * GROW_FACTOR, neededCapacity));
        }

        // Сдвигаем существующие элементы вправо
        for (int i = size - 1; i >= index; i--) {
            elements[i + collectionSize] = elements[i];
        }

        // Вставляем новые элементы
        int i = index;
        for (E element : c) {
            elements[i++] = element;
        }

        size += collectionSize;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // Удаляем все элементы, которые содержатся в коллекции c
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (c.contains(elements[i])) {
                remove(i);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // Удаляем все элементы, которые НЕ содержатся в коллекции c
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[i])) {
                remove(i);
                modified = true;
            }
        }
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        // Проверяем индексы
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException("fromIndex: " + fromIndex + ", toIndex: " + toIndex + ", size: " + size);
        }

        // Создаем новый список для подсписка
        ListC<E> subList = new ListC<>();
        int subListSize = toIndex - fromIndex;

        // Увеличиваем емкость подсписка если нужно
        if (subListSize > subList.elements.length) {
            subList.resize(subListSize);
        }

        // Копируем элементы
        for (int i = fromIndex; i < toIndex; i++) {
            subList.elements[i - fromIndex] = elements[i];
        }
        subList.size = subListSize;

        return subList;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        // Базовая реализация ListIterator
        checkIndex(index);
        return new ListCListIterator(index);
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        // Если массив слишком маленький, создаем новый
        if (a.length < size) {
            return (T[]) Arrays.copyOf(elements, size, a.getClass());
        }

        // Копируем элементы в переданный массив
        System.arraycopy(elements, 0, a, 0, size);

        // Если в массиве остались элементы после size, устанавливаем null
        if (a.length > size) {
            a[size] = null;
        }

        return a;
    }

    @Override
    public Object[] toArray() {
        // Возвращаем копию массива элементов
        return Arrays.copyOf(elements, size);
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы                      ///////
    /////////////////////////////////////////////////////////////////////////

    // Увеличивает емкость массива до указанного значения
    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        E[] newElements = (E[]) new Object[newCapacity];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    // Проверяет валидность индекса
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Реализация ListIterator                    ///////
    /////////////////////////////////////////////////////////////////////////

    private class ListCListIterator implements ListIterator<E> {
        private int currentPosition;
        private int lastReturned = -1;

        public ListCListIterator(int index) {
            this.currentPosition = index;
        }

        @Override
        public boolean hasNext() {
            return currentPosition < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturned = currentPosition;
            return elements[currentPosition++];
        }

        @Override
        public boolean hasPrevious() {
            return currentPosition > 0;
        }

        @Override
        public E previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            lastReturned = currentPosition - 1;
            return elements[--currentPosition];
        }

        @Override
        public int nextIndex() {
            return currentPosition;
        }

        @Override
        public int previousIndex() {
            return currentPosition - 1;
        }

        @Override
        public void remove() {
            if (lastReturned == -1) {
                throw new IllegalStateException();
            }
            ListC.this.remove(lastReturned);
            currentPosition = lastReturned;
            lastReturned = -1;
        }

        @Override
        public void set(E e) {
            if (lastReturned == -1) {
                throw new IllegalStateException();
            }
            ListC.this.set(lastReturned, e);
        }

        @Override
        public void add(E e) {
            ListC.this.add(currentPosition, e);
            currentPosition++;
            lastReturned = -1;
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Реализация Iterator                        ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;
            private int lastReturned = -1;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                lastReturned = currentIndex;
                return elements[currentIndex++];
            }

            @Override
            public void remove() {
                if (lastReturned == -1) {
                    throw new IllegalStateException();
                }
                ListC.this.remove(lastReturned);
                currentIndex = lastReturned;
                lastReturned = -1;
            }
        };
    }
}