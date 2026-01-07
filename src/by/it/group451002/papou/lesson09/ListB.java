package by.it.group451002.papou.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {

    // Стандартная вместимость массива при создании без параметров
    private static final int DEFAULT_CAPACITY = 10;
    // Внутренний массив для хранения элементов списка
    private Object[] elements;
    // Текущее количество элементов
    private int size;

    // Конструктор по умолчанию — создаём массив стандартного размера
    public ListB() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    // Конструктор с заданной начальной вместимостью
    public ListB(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
        this.elements = new Object[initialCapacity];
        this.size = 0;
    }

    // Преобразование списка в строку (например, [1, 2, 3])
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
        ensureCapacity(size + 1); // проверка места
        elements[size++] = e;     // вставка и увеличение размера
        return true;
    }

    // Удаление элемента по индексу
    @Override
    public E remove(int index) {
        checkIndex(index); // проверка индекса
        @SuppressWarnings("unchecked")
        E removedElement = (E) elements[index]; // сохраняем удаляемый элемент
        // сдвигаем элементы влево
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null; // уменьшаем размер и очищаем хвост
        return removedElement;
    }

    // Возвращает количество элементов
    @Override
    public int size() {
        return size;
    }

    // Вставка элемента по индексу
    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index); // допускается index == size
        ensureCapacity(size + 1);
        // сдвигаем элементы вправо
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }

    // Удаление по объекту (удаляется первое совпадение)
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && elements[i] == null) || (o != null && o.equals(elements[i]))) {
                remove(i); // используем remove(int index)
                return true;
            }
        }
        return false;
    }

    // Замена элемента по индексу
    @Override
    public E set(int index, E element) {
        checkIndex(index);
        @SuppressWarnings("unchecked")
        E oldValue = (E) elements[index];
        elements[index] = element;
        return oldValue;
    }

    // Проверка, пустой ли список
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Очистка списка
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    // Поиск первого вхождения элемента
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
        checkIndex(index);
        @SuppressWarnings("unchecked")
        E element = (E) elements[index];
        return element;
    }

    // Проверка, содержится ли элемент в списке
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    // Поиск последнего вхождения элемента
    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if ((o == null && elements[i] == null) || (o != null && o.equals(elements[i]))) {
                return i;
            }
        }
        return -1;
    }

    // Проверка, содержит ли список все элементы из другой коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    // Добавление всех элементов другой коллекции в конец списка
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }
        if (c.isEmpty()) {
            return false;
        }
        ensureCapacity(size + c.size());
        for (E element : c) {
            elements[size++] = element;
        }
        return true;
    }

    // Вставка коллекции в список начиная с определённого индекса
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        checkIndexForAdd(index);
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }
        if (c.isEmpty()) {
            return false;
        }
        int numNew = c.size();
        ensureCapacity(size + numNew);
        // сдвигаем вправо часть массива
        int numMoved = size - index;
        if (numMoved > 0) {
            System.arraycopy(elements, index, elements, index + numNew, numMoved);
        }
        // вставляем новые элементы
        int i = index;
        for (E element : c) {
            elements[i++] = element;
        }
        size += numNew;
        return true;
    }

    // Удаление всех элементов, которые есть в другой коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }
        if (c.isEmpty()) {
            return false;
        }
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                remove(i);
                i--; // смещаем индекс после удаления
                modified = true;
            }
        }
        return modified;
    }

    // Оставляем только те элементы, которые есть в другой коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(i);
                i--; // смещаем индекс после удаления
                modified = true;
            }
        }
        return modified;
    }

    // Вспомогательный метод: проверка, хватит ли места
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length * 2; // удвоение
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            Object[] newElements = new Object[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    // Проверка индекса для методов get/set/remove
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    // Проверка индекса для метода add (разрешается index == size)
    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    // Необязательные к реализации методы
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null; // не реализовано
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null; // не реализовано
    }

    @Override
    public ListIterator<E> listIterator() {
        return null; // не реализовано
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null; // не реализовано
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        System.arraycopy(elements, 0, array, 0, size);
        return array;
    }

    // Реализация итератора для цикла foreach
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
                throw new UnsupportedOperationException("remove");
            }
        };
    }
}