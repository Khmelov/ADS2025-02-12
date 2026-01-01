package by.it.group451001.kazakov.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {

    // Реализация списка на основе динамического массива

    private Object[] elements = new Object[10];  // Внутренний массив для хранения элементов
    private int size = 0;                        // Текущее количество элементов

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    // Строковое представление списка в формате [элемент1, элемент2, ...]
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

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

    // Добавление элемента в конец списка
    @Override
    public boolean add(E e) {
        // Проверка необходимости расширения массива
        if (size == elements.length) {
            Object[] newElements = new Object[elements.length * 2];  // Увеличиваем в 2 раза
            // Копируем элементы в новый массив
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
        elements[size++] = e;  // Добавляем элемент и увеличиваем размер
        return true;
    }

    // Удаление элемента по индексу
    @Override
    public E remove(int index) {
        // Проверка корректности индекса
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        @SuppressWarnings("unchecked")
        E removedElement = (E) elements[index];  // Сохраняем удаляемый элемент

        // Сдвигаем элементы влево для заполнения пустоты
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        elements[--size] = null;  // Обнуляем последний элемент и уменьшаем размер
        return removedElement;
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

    // Вставка элемента на указанную позицию
    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        // Проверка необходимости расширения
        if (size == elements.length) {
            Object[] newElements = new Object[elements.length * 2];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }

        // Сдвигаем элементы вправо для освобождения места
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }

        elements[index] = element;  // Вставляем новый элемент
        size++;
    }

    // Удаление первого вхождения объекта
    @Override
    public boolean remove(Object o) {
        // Поиск объекта в массиве
        for (int i = 0; i < size; i++) {
            // Сравнение с учетом null-значений
            if ((o == null && elements[i] == null) || (o != null && o.equals(elements[i]))) {
                remove(i);  // Удаляем по найденному индексу
                return true;
            }
        }
        return false;
    }

    // Замена элемента по индексу
    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        @SuppressWarnings("unchecked")
        E oldValue = (E) elements[index];  // Сохраняем старое значение
        elements[index] = element;         // Устанавливаем новое значение
        return oldValue;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Очистка списка
    @Override
    public void clear() {
        // Обнуляем все ссылки для помощи сборщику мусора
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    // Поиск индекса первого вхождения
    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && elements[i] == null) || (o != null && o.equals(elements[i]))) {
                return i;
            }
        }
        return -1;
    }

    // Получение элемента по индексу
    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        @SuppressWarnings("unchecked")
        E element = (E) elements[index];
        return element;
    }

    // Проверка наличия элемента
    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    // Поиск индекса последнего вхождения
    @Override
    public int lastIndexOf(Object o) {
        // Поиск с конца массива
        for (int i = size - 1; i >= 0; i--) {
            if ((o == null && elements[i] == null) || (o != null && o.equals(elements[i]))) {
                return i;
            }
        }
        return -1;
    }

    // Проверка наличия всех элементов коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    // Добавление всех элементов коллекции в конец
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        }

        for (E element : c) {
            add(element);
        }
        return true;
    }

    // Вставка всех элементов коллекции начиная с позиции
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (c.isEmpty()) {
            return false;
        }

        int i = index;
        for (E element : c) {
            add(i++, element);  // Вставляем с увеличением позиции
        }
        return true;
    }

    // Удаление всех элементов, содержащихся в коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                remove(i);
                i--;        // Корректируем индекс после удаления
                modified = true;
            }
        }
        return modified;
    }

    // Удаление всех элементов, кроме содержащихся в коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {  // Если элемент НЕ нужно сохранять
                remove(i);
                i--;        // Корректируем индекс
                modified = true;
            }
        }
        return modified;
    }

    // Создание подсписка
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0  || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }

        ListA<E> subList = new ListA<>();
        // Копируем элементы из указанного диапазона
        for (int i = fromIndex; i < toIndex; i++) {
            @SuppressWarnings("unchecked")
            E element = (E) elements[i];
            subList.add(element);
        }
        return subList;
    }

    // Создание ListIterator с начальной позицией
    @Override
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return new ListItr(index);
    }

    // Создание ListIterator с начала списка
    @Override
    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    // Преобразование в массив указанного типа
    @Override
    public <T> T[] toArray(T[] a) {
        // Если массив слишком мал, создаем новый
        if (a.length < size) {
            T[] newArray = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
            for (int i = 0; i < size; i++) {
                newArray[i] = (T) elements[i];
            }
            return newArray;
        }

        // Копируем в существующий массив
        for (int i = 0; i < size; i++) {
            a[i] = (T) elements[i];
        }

        // Помечаем конец данных null'ом если есть место
        if (a.length > size) {
            a[size] = null;
        }

        return a;
    }

    // Преобразование в массив Object[]
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        for (int i = 0; i < size; i++) {
            array[i] = elements[i];
        }
        return array;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    // Простой итератор для обхода списка
    private class Itr implements Iterator<E> {
        int cursor = 0;       // Текущая позиция
        int lastRet = -1;     // Индекс последнего возвращенного элемента

        Itr() {}

        public boolean hasNext() {
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        public E next() {
            if (cursor >= size) {
                throw new java.util.NoSuchElementException();
            }
            return (E) elements[lastRet = cursor++];
        }

        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            ListA.this.remove(lastRet);
            cursor = lastRet;  // Корректируем позицию после удаления
            lastRet = -1;
        }
    }

    // Расширенный итератор с двунаправленным обходом
    private class ListItr extends Itr implements ListIterator<E> {
        ListItr(int index) {
            cursor = index;  // Устанавливаем начальную позицию
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        @SuppressWarnings("unchecked")
        public E previous() {
            if (cursor <= 0) {
                throw new java.util.NoSuchElementException();
            }
            cursor--;
            return (E) elements[lastRet = cursor];
        }

        public void set(E e) {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            ListA.this.set(lastRet, e);
        }

        public void add(E e) {
            ListA.this.add(cursor, e);
            cursor++;        // Сдвигаем курсор после вставки
            lastRet = -1;    // Сбрасываем lastRet
        }
    }
}