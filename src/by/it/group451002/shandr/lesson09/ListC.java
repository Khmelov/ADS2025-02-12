package by.it.group451002.shandr.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class ListC<E> implements List<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements; // Массив для хранения элементов
    private int size; // Текущее количество элементов

    // Конструктор по умолчанию - создает список с начальной емкостью 10
    public ListC() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    // Конструктор с начальной емкостью - позволяет задать начальный размер массива
    public ListC(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elements = new Object[initialCapacity];
        } else {
            this.elements = new Object[DEFAULT_CAPACITY];
        }
        this.size = 0;
    }

    /**
     * Увеличивает емкость массива при необходимости
     * @param minCapacity минимальная требуемая емкость
     */
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length * 2; // Удваиваем емкость
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity; // Если нужно больше - используем minCapacity
            }
            // Создаем новый массив и копируем элементы
            Object[] newElements = new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
    }

    /**
     * Проверяет корректность индекса для операций доступа
     * @param index проверяемый индекс
     * @throws IndexOutOfBoundsException если индекс некорректен
     */
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /**
     * Проверяет корректность индекса для операций добавления
     * @param index проверяемый индекс
     * @throws IndexOutOfBoundsException если индекс некорректен
     */
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

    /**
     * Возвращает строковое представление списка в формате [element1, element2, ...]
     * @return строковое представление списка
     */
    @Override
    public String toString() {
        if (size == 0) {
            return "[]"; // Пустой список
        }

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", "); // Добавляем запятую между элементами
            }
        }
        sb.append(']');
        return sb.toString();
    }

    /**
     * Добавляет элемент в конец списка
     * @param e элемент для добавления
     * @return true (как указано в контракте Collection)
     */
    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1); // Гарантируем достаточную емкость
        elements[size] = e;       // Добавляем элемент в конец
        size++;                   // Увеличиваем счетчик
        return true;
    }

    /**
     * Удаляет элемент по указанному индексу
     * @param index индекс удаляемого элемента
     * @return удаленный элемент
     * @throws IndexOutOfBoundsException если индекс выходит за границы
     */
    @Override
    public E remove(int index) {
        checkIndex(index); // Проверяем корректность индекса

        @SuppressWarnings("unchecked")
        E removedElement = (E) elements[index]; // Сохраняем удаляемый элемент

        // Сдвигаем все элементы после удаляемого на одну позицию влево
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        elements[size - 1] = null; // Очищаем последнюю позицию
        size--;                    // Уменьшаем счетчик

        return removedElement;
    }

    /**
     * Возвращает количество элементов в списке
     * @return количество элементов
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Вставляет элемент в указанную позицию
     * @param index позиция для вставки
     * @param element элемент для вставки
     * @throws IndexOutOfBoundsException если индекс выходит за границы
     */
    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index); // Проверяем корректность индекса
        ensureCapacity(size + 1); // Гарантируем достаточную емкость

        // Сдвигаем элементы вправо чтобы освободить место для нового элемента
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }

        elements[index] = element; // Вставляем новый элемент
        size++;                   // Увеличиваем счетчик
    }

    /**
     * Удаляет первое вхождение указанного элемента
     * @param o элемент для удаления
     * @return true если элемент был найден и удален, false в противном случае
     */
    @Override
    public boolean remove(Object o) {
        // Ищем элемент в списке (поддерживает null)
        for (int i = 0; i < size; i++) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                remove(i); // Удаляем по индексу
                return true;
            }
        }
        return false; // Элемент не найден
    }

    /**
     * Заменяет элемент в указанной позиции
     * @param index индекс заменяемого элемента
     * @param element новый элемент
     * @return старый элемент
     * @throws IndexOutOfBoundsException если индекс выходит за границы
     */
    @Override
    public E set(int index, E element) {
        checkIndex(index); // Проверяем корректность индекса

        @SuppressWarnings("unchecked")
        E oldValue = (E) elements[index]; // Сохраняем старое значение
        elements[index] = element;        // Устанавливаем новое значение

        return oldValue;
    }

    /**
     * Проверяет, пуст ли список
     * @return true если список пуст, false в противном случае
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Очищает список, удаляя все элементы
     */
    @Override
    public void clear() {
        // Очищаем все ссылки для помощи сборщику мусора
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0; // Сбрасываем счетчик
    }

    /**
     * Возвращает индекс первого вхождения указанного элемента
     * @param o искомый элемент
     * @return индекс элемента или -1 если не найден
     */
    @Override
    public int indexOf(Object o) {
        // Линейный поиск с начала списка (поддерживает null)
        for (int i = 0; i < size; i++) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                return i;
            }
        }
        return -1; // Элемент не найден
    }

    /**
     * Возвращает элемент по указанному индексу
     * @param index индекс элемента
     * @return элемент по указанному индексу
     * @throws IndexOutOfBoundsException если индекс выходит за границы
     */
    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        checkIndex(index); // Проверяем корректность индекса
        return (E) elements[index];
    }

    /**
     * Проверяет, содержит ли список указанный элемент
     * @param o искомый элемент
     * @return true если элемент найден, false в противном случае
     */
    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1; // Используем indexOf для поиска
    }

    /**
     * Возвращает индекс последнего вхождения указанного элемента
     * @param o искомый элемент
     * @return индекс последнего вхождения или -1 если не найден
     */
    @Override
    public int lastIndexOf(Object o) {
        // Линейный поиск с конца списка (поддерживает null)
        for (int i = size - 1; i >= 0; i--) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                return i;
            }
        }
        return -1; // Элемент не найден
    }

    /**
     * Проверяет, содержит ли список все элементы указанной коллекции
     * @param c коллекция для проверки
     * @return true если все элементы содержатся, false в противном случае
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        // Проверяем каждый элемент коллекции
        for (Object element : c) {
            if (!contains(element)) { // Если хотя бы один не найден
                return false;
            }
        }
        return true; // Все элементы найдены
    }

    /**
     * Добавляет все элементы указанной коллекции в конец списка
     * @param c коллекция для добавления
     * @return true если список изменился, false в противном случае
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false; // Нечего добавлять
        }

        ensureCapacity(size + c.size()); // Гарантируем достаточную емкость
        // Добавляем все элементы по очереди
        for (E element : c) {
            add(element);
        }

        return true; // Список изменился
    }

    /**
     * Вставляет все элементы указанной коллекции в указанную позицию
     * @param index позиция для вставки
     * @param c коллекция для вставки
     * @return true если список изменился, false в противном случае
     * @throws IndexOutOfBoundsException если индекс выходит за границы
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        checkIndexForAdd(index); // Проверяем корректность индекса

        if (c.isEmpty()) {
            return false; // Нечего добавлять
        }

        ensureCapacity(size + c.size()); // Гарантируем достаточную емкость

        // Сдвигаем существующие элементы вправо чтобы освободить место
        int numToMove = size - index;
        if (numToMove > 0) {
            for (int i = size + c.size() - 1; i >= index + c.size(); i--) {
                elements[i] = elements[i - c.size()];
            }
        }

        // Вставляем новые элементы
        int i = index;
        for (E element : c) {
            elements[i++] = element;
        }

        size += c.size(); // Обновляем счетчик
        return true;      // Список изменился
    }

    /**
     * Удаляет все элементы, содержащиеся в указанной коллекции
     * @param c коллекция элементов для удаления
     * @return true если список изменился, false в противном случае
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false; // Флаг изменения списка

        // Проходим по всем элементам списка
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) { // Если элемент нужно удалить
                remove(i);     // Удаляем элемент
                i--;           // Корректируем индекс из-за сдвига
                modified = true; // Устанавливаем флаг изменения
            }
        }

        return modified;
    }

    /**
     * Сохраняет только те элементы, которые содержатся в указанной коллекции
     * @param c коллекция элементов для сохранения
     * @return true если список изменился, false в противном случае
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false; // Флаг изменения списка

        // Проходим по всем элементам списка
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) { // Если элемент НЕ нужно сохранять
                remove(i);     // Удаляем элемент
                i--;           // Корректируем индекс из-за сдвига
                modified = true; // Устанавливаем флаг изменения
            }
        }

        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Возвращает представление части списка между указанными индексами
     * @param fromIndex начальный индекс (включительно)
     * @param toIndex конечный индекс (исключительно)
     * @return подсписок
     * @throws IndexOutOfBoundsException если индексы некорректны
     */
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        // Проверяем корректность индексов
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException("fromIndex: " + fromIndex + ", toIndex: " + toIndex + ", size: " + size);
        }

        // Создаем новый список для подсписка
        ListC<E> subList = new ListC<>(toIndex - fromIndex);
        // Копируем элементы из указанного диапазона
        for (int i = fromIndex; i < toIndex; i++) {
            subList.add(get(i));
        }
        return subList;
    }

    /**
     * Возвращает итератор списка начиная с указанной позиции
     * @param index начальная позиция итератора
     * @return list iterator
     * @throws IndexOutOfBoundsException если индекс некорректен
     */
    @Override
    public ListIterator<E> listIterator(int index) {
        checkIndexForAdd(index); // Проверяем корректность индекса

        return new ListIterator<E>() {
            private int currentIndex = index; // Текущая позиция итератора
            private int lastReturned = -1;    // Индекс последнего возвращенного элемента

            /**
             * Проверяет, есть ли следующий элемент
             * @return true если есть следующий элемент
             */
            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            /**
             * Возвращает следующий элемент
             * @return следующий элемент
             * @throws NoSuchElementException если нет следующего элемента
             */
            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                lastReturned = currentIndex; // Запоминаем индекс
                return (E) elements[currentIndex++]; // Возвращаем и двигаемся вперед
            }

            /**
             * Проверяет, есть ли предыдущий элемент
             * @return true если есть предыдущий элемент
             */
            @Override
            public boolean hasPrevious() {
                return currentIndex > 0;
            }

            /**
             * Возвращает предыдущий элемент
             * @return предыдущий элемент
             * @throws NoSuchElementException если нет предыдущего элемента
             */
            @Override
            @SuppressWarnings("unchecked")
            public E previous() {
                if (!hasPrevious()) {
                    throw new NoSuchElementException();
                }
                lastReturned = --currentIndex; // Двигаемся назад и запоминаем индекс
                return (E) elements[currentIndex];
            }

            /**
             * Возвращает индекс следующего элемента
             * @return индекс следующего элемента
             */
            @Override
            public int nextIndex() {
                return currentIndex;
            }

            /**
             * Возвращает индекс предыдущего элемента
             * @return индекс предыдущего элемента
             */
            @Override
            public int previousIndex() {
                return currentIndex - 1;
            }

            /**
             * Удаляет последний возвращенный элемент
             * @throws IllegalStateException если next или previous не были вызваны
             */
            @Override
            public void remove() {
                if (lastReturned == -1) {
                    throw new IllegalStateException();
                }
                ListC.this.remove(lastReturned); // Удаляем элемент из списка
                currentIndex = lastReturned;     // Корректируем текущую позицию
                lastReturned = -1;               // Сбрасываем lastReturned
            }

            /**
             * Заменяет последний возвращенный элемент
             * @param e новый элемент
             * @throws IllegalStateException если next или previous не были вызваны
             */
            @Override
            public void set(E e) {
                if (lastReturned == -1) {
                    throw new IllegalStateException();
                }
                ListC.this.set(lastReturned, e); // Заменяем элемент в списке
            }

            /**
             * Добавляет элемент перед текущей позицией
             * @param e добавляемый элемент
             */
            @Override
            public void add(E e) {
                ListC.this.add(currentIndex++, e); // Добавляем и двигаемся вперед
                lastReturned = -1; // Сбрасываем lastReturned
            }
        };
    }

    /**
     * Возвращает итератор списка начиная с начала
     * @return list iterator
     */
    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0); // Итератор с начала списка
    }

    /**
     * Преобразует список в массив указанного типа
     * @param a массив для заполнения (если достаточно большой)
     * @return массив элементов списка
     * @throws ArrayStoreException если тип массива несовместим
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            // Создаем новый массив нужного типа
            T[] result = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
            // Копируем элементы
            for (int i = 0; i < size; i++) {
                result[i] = (T) elements[i];
            }
            return result;
        }

        // Копируем элементы в переданный массив
        for (int i = 0; i < size; i++) {
            a[i] = (T) elements[i];
        }
        // Устанавливаем null после последнего элемента (по контракту)
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    /**
     * Преобразует список в массив Object[]
     * @return массив элементов списка
     */
    @Override
    public Object[] toArray() {
        // Создаем новый массив и копируем элементы
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

    /**
     * Возвращает итератор для последовательного обхода списка
     * @return iterator
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0; // Текущая позиция
            private int lastReturned = -1; // Индекс последнего возвращенного элемента

            /**
             * Проверяет, есть ли следующий элемент
             * @return true если есть следующий элемент
             */
            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            /**
             * Возвращает следующий элемент
             * @return следующий элемент
             * @throws NoSuchElementException если нет следующего элемента
             */
            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                lastReturned = currentIndex; // Запоминаем индекс
                return (E) elements[currentIndex++]; // Возвращаем и двигаемся
            }

            /**
             * Удаляет последний возвращенный элемент
             * @throws IllegalStateException если next не был вызван
             */
            @Override
            public void remove() {
                if (lastReturned == -1) {
                    throw new IllegalStateException();
                }
                ListC.this.remove(lastReturned); // Удаляем из списка
                currentIndex = lastReturned;     // Корректируем позицию
                lastReturned = -1;               // Сбрасываем lastReturned
            }
        };
    }
}