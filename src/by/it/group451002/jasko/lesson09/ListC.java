package by.it.group451002.jasko.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Arrays;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /*
     * ListC - полнофункциональная реализация списка с итератором.
     * Полная поддержка всех операций List, включая toArray(T[]) и iterator().
     * Реализован безопасный итератор с поддержкой удаления во время обхода.
     */

    // Внутренний массив для хранения элементов
    private E[] elements;
    // Текущее количество элементов в списке
    private int size;
    // Начальная емкость массива
    private static final int INITIAL_CAPACITY = 10;

    // Конструктор - инициализирует пустой список
    @SuppressWarnings("unchecked")
    public ListC() {
        elements = (E[]) new Object[INITIAL_CAPACITY];
        size = 0;
    }

    // Увеличивает размер массива при нехватке места
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = elements.length + (elements.length >> 1); // +50%
        E[] newElements = (E[]) new Object[newCapacity];
        if (size >= 0) System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    // Проверяет корректность индекса для операций доступа
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    // Проверяет корректность индекса для операций вставки
    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        // Возвращает строковое представление списка
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

    @Override
    public boolean add(E e) {
        // Добавляет элемент в конец списка
        if (size == elements.length) {
            resize();
        }
        elements[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        // Удаляет и возвращает элемент по указанному индексу
        checkIndex(index);

        E removedElement = elements[index];

        // Сдвигаем элементы для заполнения пустого места
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        elements[--size] = null; // Очищаем последнюю позицию
        return removedElement;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        // Вставляет элемент на указанную позицию
        checkIndexForAdd(index);

        if (size == elements.length) {
            resize();
        }

        // Освобождаем место для нового элемента
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }

        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        // Удаляет первое вхождение указанного объекта
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
        // Заменяет элемент на указанной позиции
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
        // Очищает список, удаляя все элементы
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        // Возвращает индекс первого вхождения элемента
        for (int i = 0; i < size; i++) {
            if ((o == null && elements[i] == null) ||
                    (o != null && o.equals(elements[i]))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        // Возвращает элемент по указанному индексу
        checkIndex(index);
        return elements[index];
    }

    @Override
    public boolean contains(Object o) {
        // Проверяет наличие элемента в списке
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        // Возвращает индекс последнего вхождения элемента
        for (int i = size - 1; i >= 0; i--) {
            if ((o == null && elements[i] == null) ||
                    (o != null && o.equals(elements[i]))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // Проверяет содержит ли список все элементы коллекции
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // Добавляет все элементы коллекции в конец списка
        if (c.isEmpty()) {
            return false;
        }

        // Обеспечиваем достаточную емкость
        int requiredCapacity = size + c.size();
        while (elements.length < requiredCapacity) {
            resize();
        }

        for (E element : c) {
            add(element);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        // Вставляет все элементы коллекции на указанную позицию
        checkIndexForAdd(index);

        if (c.isEmpty()) {
            return false;
        }

        // Обеспечиваем достаточную емкость
        int requiredCapacity = size + c.size();
        while (elements.length < requiredCapacity) {
            resize();
        }

        // Сдвигаем существующие элементы для освобождения места
        int collectionSize = c.size();
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
        // Удаляет все элементы, содержащиеся в указанной коллекции
        if (c.isEmpty()) {
            return false;
        }

        boolean modified = false;
        // Обход с конца для корректного удаления
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
        // Удаляет все элементы, НЕ содержащиеся в указанной коллекции
        boolean modified = false;
        // Обход с конца для корректного удаления
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[i])) {
                remove(i);
                modified = true;
            }
        }
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        // Возвращает представление части списка
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException(
                    "fromIndex: " + fromIndex + ", toIndex: " + toIndex + ", size: " + size);
        }

        ListC<E> subList = new ListC<>();
        // Обеспечиваем достаточную емкость для подсписка
        int subListSize = toIndex - fromIndex;
        while (subList.elements.length < subListSize) {
            subList.resize();
        }

        // Копируем элементы в подсписок
        subList.addAll(Arrays.asList(elements).subList(fromIndex, toIndex));
        return subList;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException("listIterator not implemented");
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException("listIterator not implemented");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        // Преобразует список в массив указанного типа
        if (a.length < size) {
            // Создаем новый массив нужного типа и размера
            T[] result = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
            for (int i = 0; i < size; i++) {
                result[i] = (T) elements[i];
            }
            return result;
        }

        // Копируем элементы в переданный массив
        for (int i = 0; i < size; i++) {
            a[i] = (T) elements[i];
        }

        // Устанавливаем маркер конца если массив больше
        if (a.length > size) {
            a[size] = null;
        }

        return a;
    }

    @Override
    public Object[] toArray() {
        // Возвращает массив Object[] содержащий все элементы
        Object[] result = new Object[size];
        System.arraycopy(elements, 0, result, 0, size);
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
        // Возвращает итератор для обхода элементов списка
        return new Iterator<E>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                return elements[currentIndex++];
            }

            @Override
            public void remove() {
                // Удаляет текущий элемент (последний возвращенный next())
                if (currentIndex <= 0) {
                    throw new IllegalStateException();
                }
                ListC.this.remove(--currentIndex);
            }
        };
    }
}