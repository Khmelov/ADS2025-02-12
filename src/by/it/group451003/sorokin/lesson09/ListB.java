package by.it.group451003.sorokin.lesson09;

import java.util.*;

public class ListB<E> implements List<E> {
    // Внутренний массив для хранения элементов
    private E[] elements;
    // Текущее количество элементов в списке
    private int size;
    // Начальная емкость массива
    private static final int INITIAL_CAPACITY = 10;

    // Конструктор
    @SuppressWarnings("unchecked")
    public ListB() {
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
        // Проверяем, нужно ли увеличить массив
        if (size == elements.length) {
            resize();
        }
        // Добавляем элемент в конец
        elements[size] = e;
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        // Проверяем валидность индекса
        checkIndex(index);

        // Сохраняем элемент для возврата
        E removedElement = elements[index];

        // Сдвигаем все элементы после удаленного влево
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        // Очищаем последний элемент и уменьшаем размер
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
            resize();
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

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

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
            resize(Math.max(elements.length * 2, neededCapacity));
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
            resize(Math.max(elements.length * 2, neededCapacity));
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

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        // Проверяем индексы
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException("fromIndex: " + fromIndex + ", toIndex: " + toIndex + ", size: " + size);
        }

        // Создаем новый список для подсписка
        ListB<E> subList = new ListB<>();
        subList.elements = Arrays.copyOfRange(elements, fromIndex, toIndex);
        subList.size = toIndex - fromIndex;
        return subList;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы                      ///////
    /////////////////////////////////////////////////////////////////////////

    // Увеличивает емкость массива в 2 раза
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = elements.length * 2;
        E[] newElements = (E[]) new Object[newCapacity];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

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
    //////        Методы которые можно оставить нереализованными     ///////
    /////////////////////////////////////////////////////////////////////////

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
        return Arrays.copyOf(elements, size);
    }

    @Override
    public Iterator<E> iterator() {
        // Простая реализация итератора
        return new Iterator<E>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return elements[currentIndex++];
            }
        };
    }
}