package by.it.group451002.jasko.lesson09;

import java.util.*;

public class ListB<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /*
     * ListB - расширенная реализация списка с поддержкой операций с коллекциями.
     * Добавлены методы: addAll, removeAll, retainAll, containsAll.
     * Поддерживает вставку коллекций в любую позицию.
     */

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

    // Увеличивает размер массива при нехватке места
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = elements.length + (elements.length >> 1); // Увеличиваем на 50%
        E[] newElements = (E[]) new Object[newCapacity];
        if (size >= 0) System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        // Формируем строковое представление списка в формате [element1, element2, ...]
        StringBuilder sb = new StringBuilder("[");
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
        // Добавляем элемент в конец списка
        if (size == elements.length) {
            resize(); // Увеличиваем массив если нужно
        }
        elements[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        // Удаляем элемент по индексу и возвращаем его
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        E removedElement = elements[index];

        // Сдвигаем все элементы после удаляемого на одну позицию влево
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        elements[--size] = null; // Очищаем последний элемент
        return removedElement;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        // Вставляем элемент на указанную позицию
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (size == elements.length) {
            resize();
        }

        // Сдвигаем элементы вправо чтобы освободить место
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }

        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        // Удаляем первое вхождение указанного объекта
        for (int i = 0; i < size; i++) {
            if ((o == null && elements[i] == null) ||
                    (o != null && o.equals(elements[i]))) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        // Заменяем элемент на указанной позиции
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

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
        // Очищаем список, устанавливая все элементы в null
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        // Ищем первое вхождение объекта в списке
        for (int i = 0; i < size; i++) {
            if ((o == null && elements[i] == null) ||
                    (o != null && o.equals(elements[i]))) {
                return i;
            }
        }
        return -1; // Не найдено
    }

    @Override
    public E get(int index) {
        // Возвращаем элемент по указанному индексу
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return elements[index];
    }

    @Override
    public boolean contains(Object o) {
        // Проверяем содержится ли объект в списке
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        // Ищем последнее вхождение объекта в списке
        for (int i = size - 1; i >= 0; i--) {
            if ((o == null && elements[i] == null) ||
                    (o != null && o.equals(elements[i]))) {
                return i;
            }
        }
        return -1; // Не найдено
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean containsAll(Collection<?> c) {
        // Проверяем содержит ли список все элементы коллекции
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

        for (E element : c) {
            add(element);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        // Вставляем все элементы коллекции на указанную позицию
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (c.isEmpty()) {
            return false;
        }

        // Сдвигаем элементы чтобы освободить место для новых
        int collectionSize = c.size();
        while (size + collectionSize > elements.length) {
            resize();
        }

        // Сдвигаем существующие элементы вправо
        for (int i = size - 1; i >= index; i--) {
            elements[i + collectionSize] = elements[i];
        }

        // Вставляем новые элементы
        int currentIndex = index;
        for (E element : c) {
            elements[currentIndex++] = element;
        }

        size += collectionSize;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // Удаляем все элементы, содержащиеся в указанной коллекции
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
        // Удаляем все элементы, НЕ содержащиеся в указанной коллекции
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
        // Возвращаем представление части списка
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }

        ListB<E> subList = new ListB<>();
        subList.addAll(Arrays.asList(elements).subList(fromIndex, toIndex));
        return subList;
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
        // Возвращаем массив содержащий все элементы списка
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
        return null;
    }
}