package by.it.group451001.serganovskij.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {

    // ВНУТРЕННЯЯ СТРУКТУРА ДАННЫХ
    private static final int INITIAL_CAPACITY = 10; // Начальный размер массива
    private Object[] elements; // Массив для хранения элементов
    private int size; // Текущее количество элементов

    // КОНСТРУКТОР - инициализация пустого списка
    public ListB() {
        elements = new Object[INITIAL_CAPACITY];
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    // ОСНОВНЫЕ МЕТОДЫ ИНТЕРФЕЙСА LIST (ОБЯЗАТЕЛЬНЫЕ)
    /////////////////////////////////////////////////////////////////////////

    // ПРЕОБРАЗОВАНИЕ В СТРОКУ - формат [элемент1, элемент2, ...]
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

    // ДОБАВЛЕНИЕ ЭЛЕМЕНТА В КОНЕЦ - с автоматическим расширением массива
    @Override
    public boolean add(E e) {
        ensureCapacity(); // Проверка и увеличение емкости при необходимости
        elements[size++] = e;
        return true;
    }

    // УДАЛЕНИЕ ЭЛЕМЕНТА ПО ИНДЕКСУ - со сдвигом оставшихся элементов
    @Override
    public E remove(int index) {
        checkIndex(index); // Проверка корректности индекса
        E removedElement = (E) elements[index];

        // Сдвиг элементов влево для заполнения пустого места
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null; // Очистка последней ссылки
        return removedElement;
    }

    // ПОЛУЧЕНИЕ РАЗМЕРА СПИСКА
    @Override
    public int size() {
        return size;
    }

    // ВСТАВКА ЭЛЕМЕНТА ПО УКАЗАННОМУ ИНДЕКСУ
    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index); // Проверка индекса для вставки
        ensureCapacity();

        // Сдвиг элементов вправо для освобождения места
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
    }

    // УДАЛЕНИЕ ПЕРВОГО ВХОЖДЕНИЯ ОБЪЕКТА (с поддержкой null)
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            // Безопасное сравнение с учетом null значений
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    // ЗАМЕНА ЭЛЕМЕНТА ПО ИНДЕКСУ - возвращает старый элемент
    @Override
    public E set(int index, E element) {
        checkIndex(index);
        E oldElement = (E) elements[index];
        elements[index] = element;
        return oldElement;
    }

    // ПРОВЕРКА НА ПУСТОЙ СПИСОК
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // ПОЛНАЯ ОЧИСТКА СПИСКА - обнуление всех элементов
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    // ПОИСК ИНДЕКСА ПЕРВОГО ВХОЖДЕНИЯ ЭЛЕМЕНТА
    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

    // ПОЛУЧЕНИЕ ЭЛЕМЕНТА ПО ИНДЕКСУ
    @Override
    public E get(int index) {
        checkIndex(index);
        return (E) elements[index];
    }

    // ПРОВЕРКА НАЛИЧИЯ ЭЛЕМЕНТА В СПИСКЕ
    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    // ПОИСК ИНДЕКСА ПОСЛЕДНЕГО ВХОЖДЕНИЯ ЭЛЕМЕНТА
    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

    /////////////////////////////////////////////////////////////////////////
    // МЕТОДЫ ДЛЯ РАБОТЫ С КОЛЛЕКЦИЯМИ (ОПЦИОНАЛЬНЫЕ)
    /////////////////////////////////////////////////////////////////////////

    // ПРОВЕРКА СОДЕРЖАНИЯ ВСЕХ ЭЛЕМЕНТОВ КОЛЛЕКЦИИ
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) return false;
        }
        return true;
    }

    // ДОБАВЛЕНИЕ ВСЕХ ЭЛЕМЕНТОВ КОЛЛЕКЦИИ В КОНЕЦ
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) return false;
        for (E item : c) add(item);
        return true;
    }

    // ДОБАВЛЕНИЕ ВСЕХ ЭЛЕМЕНТОВ КОЛЛЕКЦИИ ПО УКАЗАННОМУ ИНДЕКСУ
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        checkIndexForAdd(index);
        if (c.isEmpty()) return false;

        int i = index;
        for (E item : c) add(i++, item);
        return true;
    }

    // УДАЛЕНИЕ ВСЕХ ЭЛЕМЕНТОВ, ПРИСУТСТВУЮЩИХ В КОЛЛЕКЦИИ
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                remove(i);
                i--; // Коррекция индекса после сдвига элементов
                modified = true;
            }
        }
        return modified;
    }

    // СОХРАНЕНИЕ ТОЛЬКО ЭЛЕМЕНТОВ, ПРИСУТСТВУЮЩИХ В КОЛЛЕКЦИИ
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(i);
                i--; // Коррекция индекса после сдвига элементов
                modified = true;
            }
        }
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ДЛЯ УПРАВЛЕНИЯ ВНУТРЕННИМ МАССИВОМ
    /////////////////////////////////////////////////////////////////////////

    // УВЕЛИЧЕНИЕ ЕМКОСТИ МАССИВА ПРИ НЕОБХОДИМОСТИ (в 2 раза)
    private void ensureCapacity() {
        if (size == elements.length) {
            Object[] newElements = new Object[elements.length * 2];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    // ПРОВЕРКА КОРРЕКТНОСТИ ИНДЕКСА ДЛЯ ОПЕРАЦИЙ ДОСТУПА
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    // ПРОВЕРКА КОРРЕКТНОСТИ ИНДЕКСА ДЛЯ ОПЕРАЦИЙ ВСТАВКИ
    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    // ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ (ЗАГЛУШКИ И ПРОСТЫЕ РЕАЛИЗАЦИИ)
    /////////////////////////////////////////////////////////////////////////

    // ПРОСТОЙ ИТЕРАТОР ДЛЯ ОБХОДА ЭЛЕМЕНТОВ
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public E next() {
                return (E) elements[currentIndex++];
            }
        };
    }

    // ПРЕОБРАЗОВАНИЕ В МАССИВ ОБЪЕКТОВ
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        System.arraycopy(elements, 0, array, 0, size);
        return array;
    }

    // МЕТОДЫ-ЗАГЛУШКИ (НЕ РЕАЛИЗОВАНЫ)

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
}