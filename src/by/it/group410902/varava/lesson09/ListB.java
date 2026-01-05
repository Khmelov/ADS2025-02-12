package by.it.group410902.varava.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size;

    // Конструктор
    public ListB() {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    // Вспомогательный метод для расширения массива
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length * 2;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            Object[] newArray = new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newArray[i] = elements[i];
            }
            elements = newArray;
        }
    }

    // Вспомогательный метод для сравнения объектов
    private boolean objectsEqual(Object o1, Object o2) {
        if (o1 == o2) return true;
        if (o1 == null || o2 == null) return false;
        return o1.equals(o2);
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1);
        elements[size++] = e;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        E removedElement = (E) elements[index];

        // Сдвигаем элементы влево
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        elements[--size] = null;
        return removedElement;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        ensureCapacity(size + 1);

        // Сдвигаем элементы вправо
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }

        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (objectsEqual(o, elements[i])) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        E oldElement = (E) elements[index];
        elements[index] = element;
        return oldElement;
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
            if (objectsEqual(o, elements[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (E) elements[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (objectsEqual(o, elements[i])) {
                return i;
            }
        }
        return -1;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean containsAll(Collection<?> c) {
        // Создаем итератор вручную чтобы не использовать for-each
        Iterator<?> iterator = c.iterator();
        while (iterator.hasNext()) {
            if (!contains(iterator.next())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) return false;

        // Подсчитываем размер коллекции
        int collectionSize = 0;
        Iterator<? extends E> iterator = c.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            collectionSize++;
        }

        ensureCapacity(size + collectionSize);

        // Добавляем элементы
        iterator = c.iterator();
        while (iterator.hasNext()) {
            elements[size++] = iterator.next();
        }

        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (c.isEmpty()) return false;

        // Подсчитываем размер коллекции
        int collectionSize = 0;
        Iterator<? extends E> sizeIterator = c.iterator();
        while (sizeIterator.hasNext()) {
            sizeIterator.next();
            collectionSize++;
        }

        ensureCapacity(size + collectionSize);

        // Сдвигаем существующие элементы вправо
        for (int i = size - 1; i >= index; i--) {
            elements[i + collectionSize] = elements[i];
        }

        // Вставляем новые элементы
        Iterator<? extends E> iterator = c.iterator();
        int i = index;
        while (iterator.hasNext()) {
            elements[i++] = iterator.next();
        }

        size += collectionSize;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            // Проверяем содержится ли элемент в коллекции
            boolean contains = false;
            Iterator<?> iterator = c.iterator();
            while (iterator.hasNext()) {
                if (objectsEqual(elements[i], iterator.next())) {
                    contains = true;
                    break;
                }
            }

            if (contains) {
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
            // Проверяем содержится ли элемент в коллекции
            boolean contains = false;
            Iterator<?> iterator = c.iterator();
            while (iterator.hasNext()) {
                if (objectsEqual(elements[i], iterator.next())) {
                    contains = true;
                    break;
                }
            }

            if (!contains) {
                remove(i);
                i--; // Уменьшаем счетчик т.к. элементы сдвинулись
                modified = true;
            }
        }
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
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
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = elements[i];
        }
        return result;
    }

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
                if (!hasNext()) {
                    throw new RuntimeException("No such element");
                }
                return (E) elements[currentIndex++];
            }
        };
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////// Метод для тестирования //////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        // Тестирование основных методов
        ListB<String> list = new ListB<>();

        System.out.println("=== Тестирование ListB ===");

        // Добавление элементов
        list.add("A");
        list.add("B");
        list.add("C");
        System.out.println("После добавления A, B, C: " + list);

        // Вставка по индексу
        list.add(1, "X");
        System.out.println("После вставки X на позицию 1: " + list);

        // Замена элемента
        list.set(2, "Y");
        System.out.println("После замены элемента на позиции 2 на Y: " + list);

        // Получение элемента
        System.out.println("Элемент на позиции 0: " + list.get(0));

        // Удаление по индексу
        list.remove(0);
        System.out.println("После удаления элемента на позиции 0: " + list);

        // Удаление по значению
        list.remove("Y");
        System.out.println("После удаления элемента 'Y': " + list);

        // Поиск элементов
        System.out.println("Индекс элемента 'X': " + list.indexOf("X"));
        System.out.println("Содержит 'Z'? " + list.contains("Z"));

        // Размер и пустота
        System.out.println("Размер списка: " + list.size());
        System.out.println("Список пустой? " + list.isEmpty());
    }
}