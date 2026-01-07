package by.it.group410901.bukshta.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {

    // Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    // Стандартная начальная вместимость массива
    private static final int DEFAULT_CAPACITY = 10;

    // Внутренний массив для хранения элементов
    private Object[] elements;

    // Текущее количество элементов в списке
    private int size;

    // Конструктор по умолчанию - создает список с начальной вместимостью
    public ListA() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    // Конструктор с заданной начальной вместимостью
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

    // Преобразование списка в строку для вывода
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

    // Добавление элемента в конец списка
    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1);  // Увеличиваем вместимость при необходимости
        elements[size++] = e;      // Добавляем элемент и увеличиваем размер
        return true;
    }

    // Удаление элемента по индексу
    @Override
    public E remove(int index) {
        checkIndex(index);  // Проверка корректности индекса

        @SuppressWarnings("unchecked")
        E removedElement = (E) elements[index];  // Сохраняем удаляемый элемент

        // Сдвигаем элементы влево, чтобы заполнить пустоту
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }

        elements[--size] = null;  // Обнуляем последний элемент и уменьшаем размер
        return removedElement;
    }

    // Возвращает текущий размер списка
    @Override
    public int size() {
        return size;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    // Вставка элемента по указанному индексу
    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index);  // Проверка корректности индекса для вставки
        ensureCapacity(size + 1); // Увеличиваем вместимость при необходимости

        // Сдвигаем элементы вправо, чтобы освободить место
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;  // Вставляем новый элемент
        size++;                     // Увеличиваем размер
    }

    // Удаление первого вхождения указанного объекта
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && elements[i] == null) ||
                    (o != null && o.equals(elements[i]))) {
                remove(i);  // Удаляем найденный элемент
                return true;
            }
        }
        return false;  // Элемент не найден
    }

    // Замена элемента по указанному индексу
    @Override
    public E set(int index, E element) {
        checkIndex(index);  // Проверка корректности индекса

        @SuppressWarnings("unchecked")
        E oldValue = (E) elements[index];  // Сохраняем старое значение
        elements[index] = element;         // Устанавливаем новое значение
        return oldValue;                   // Возвращаем старое значение
    }

    // Проверка, пуст ли список
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Очистка списка - удаление всех элементов
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;  // Обнуляем все элементы
        }
        size = 0;  // Сбрасываем размер
    }

    // Поиск индекса первого вхождения объекта
    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && elements[i] == null) ||
                    (o != null && o.equals(elements[i]))) {
                return i;  // Возвращаем индекс найденного элемента
            }
        }
        return -1;  // Элемент не найден
    }

    // Получение элемента по индексу
    @Override
    public E get(int index) {
        checkIndex(index);  // Проверка корректности индекса

        @SuppressWarnings("unchecked")
        E element = (E) elements[index];
        return element;
    }

    // Проверка наличия элемента в списке
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;  // Используем уже реализованный indexOf
    }

    // Поиск индекса последнего вхождения объекта
    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if ((o == null && elements[i] == null) ||
                    (o != null && o.equals(elements[i]))) {
                return i;  // Возвращаем индекс найденного элемента
            }
        }
        return -1;  // Элемент не найден
    }

    /////////////////////////////////////////////////////////////////////////
    // Вспомогательные методы для внутренней реализации
    /////////////////////////////////////////////////////////////////////////

    // Обеспечение достаточной вместимости массива
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length * 2;  // Удваиваем вместимость
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;  // Но не меньше требуемой
            }
            Object[] newElements = new Object[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);  // Копируем элементы
            elements = newElements;  // Заменяем старый массив новым
        }
    }

    // Проверка корректности индекса для операций доступа
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    // Проверка корректности индекса для операций добавления
    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    // Оставшиеся методы интерфейса List - реализованы как заглушки
    // или минимально необходимой функциональности

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

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

    // Преобразование списка в массив объектов
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        System.arraycopy(elements, 0, array, 0, size);  // Копируем элементы
        return array;
    }

    // Реализация итератора для foreach-циклов
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
        };
    }
}