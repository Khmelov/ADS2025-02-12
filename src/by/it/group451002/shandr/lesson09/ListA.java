package by.it.group451002.shandr.lesson09;

import java.util.*;

public class ListA<E> implements List<E> {

    // Начальная емкость по умолчанию
    private static final int DEFAULT_CAPACITY = 10;

    // Массив для хранения элементов списка
    private Object[] elements;

    // Текущее количество элементов в списке
    private int size;

    /**
     * Конструктор по умолчанию
     * Создает список с начальной емкостью DEFAULT_CAPACITY
     */
    public ListA() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    /**
     * Конструктор с заданной начальной емкостью
     * @param initialCapacity начальная емкость списка
     */
    public ListA(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elements = new Object[initialCapacity];
        } else {
            this.elements = new Object[DEFAULT_CAPACITY];
        }
        this.size = 0;
    }

    /**
     * Увеличивает вместимость массива при необходимости
     * @param minCapacity минимальная требуемая емкость
     */
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length * 2; // Удваиваем емкость
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity; // Если удвоенной емкости недостаточно
            }
            // Копируем элементы в новый массив большей емкости
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Возвращает строковое представление списка
     * @return строка в формате [element1, element2, ...]
     */
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
        return sb.toString();
    }

    /**
     * Добавляет элемент в конец списка
     * @param e элемент для добавления
     * @return true если элемент успешно добавлен
     */
    @Override
    public boolean add(E e) {
        // Гарантируем, что массив достаточно большой
        ensureCapacity(size + 1);
        // Добавляем элемент в конец и увеличиваем размер
        elements[size++] = e;
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
        // Проверка корректности индекса
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        @SuppressWarnings("unchecked")
        E oldValue = (E) elements[index]; // Сохраняем удаляемый элемент

        // Вычисляем количество элементов для сдвига
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            // Сдвигаем все элементы после удаляемого на одну позицию влево
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        // Очищаем последнюю позицию и уменьшаем размер
        elements[--size] = null;

        return oldValue;
    }

    /**
     * Возвращает количество элементов в списке
     * @return размер списка
     */
    @Override
    public int size() {
        return size;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Вставляет элемент в указанную позицию
     * @param index индекс, по которому будет вставлен элемент
     * @param element элемент для вставки
     * @throws IndexOutOfBoundsException если индекс выходит за границы
     */
    @Override
    public void add(int index, E element) {
        // Проверка корректности индекса
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        // Гарантируем достаточную емкость
        ensureCapacity(size + 1);
        // Сдвигаем элементы для освобождения места
        System.arraycopy(elements, index, elements, index + 1, size - index);
        // Вставляем новый элемент
        elements[index] = element;
        size++;
    }

    /**
     * Удаляет первое вхождение указанного элемента
     * @param o элемент для удаления
     * @return true если элемент был найден и удален
     */
    @Override
    public boolean remove(Object o) {
        // Поиск элемента для удаления (поддерживает null)
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) {
                    remove(i);
                    return true;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) {
                    remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Заменяет элемент в указанной позиции
     * @param index индекс заменяемого элемента
     * @param element новый элемент
     * @return предыдущий элемент в этой позиции
     * @throws IndexOutOfBoundsException если индекс выходит за границы
     */
    @Override
    public E set(int index, E element) {
        // Проверка корректности индекса
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        @SuppressWarnings("unchecked")
        E oldValue = (E) elements[index]; // Сохраняем старое значение
        elements[index] = element; // Устанавливаем новое значение
        return oldValue;
    }

    /**
     * Проверяет, пуст ли список
     * @return true если список пуст
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Удаляет все элементы из списка
     */
    @Override
    public void clear() {
        // Очищаем все ссылки для помощи сборщику мусора
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    /**
     * Возвращает индекс первого вхождения указанного элемента
     * @param o элемент для поиска
     * @return индекс элемента или -1 если не найден
     */
    @Override
    public int indexOf(Object o) {
        // Поиск первого вхождения элемента (поддерживает null)
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Возвращает элемент по указанному индексу
     * @param index индекс элемента
     * @return элемент в указанной позиции
     * @throws IndexOutOfBoundsException если индекс выходит за границы
     */
    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        // Проверка корректности индекса
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (E) elements[index];
    }

    /**
     * Проверяет наличие элемента в списке
     * @param o элемент для поиска
     * @return true если элемент найден
     */
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    /**
     * Возвращает индекс последнего вхождения указанного элемента
     * @param o элемент для поиска
     * @return индекс элемента или -1 если не найден
     */
    @Override
    public int lastIndexOf(Object o) {
        // Поиск последнего вхождения элемента (поддерживает null)
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (elements[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(elements[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    /////////////////////////////////////////////////////////////////////////
    //////        Методы для работы с коллекциями                    ////////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Проверяет наличие всех элементов коллекции в списке
     * @param c коллекция для проверки
     * @return true если все элементы присутствуют
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        // Проверяем, что все элементы коллекции содержатся в списке
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Добавляет все элементы коллекции в конец списка
     * @param c коллекция для добавления
     * @return true если список изменился
     */
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

    /**
     * Добавляет все элементы коллекции начиная с указанной позиции
     * @param index индекс, с которого начинать вставку
     * @param c коллекция для добавления
     * @return true если список изменился
     * @throws IndexOutOfBoundsException если индекс выходит за границы
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        // Проверка корректности индекса
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        // Добавляем все элементы коллекции начиная с указанной позиции
        if (c.isEmpty()) {
            return false;
        }

        int i = index;
        for (E element : c) {
            add(i++, element);
        }
        return true;
    }

    /**
     * Удаляет все элементы, присутствующие в указанной коллекции
     * @param c коллекция элементов для удаления
     * @return true если список изменился
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        // Удаляем все элементы, содержащиеся в указанной коллекции
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                remove(i);
                i--; // Корректируем индекс из-за сдвига элементов
                modified = true;
            }
        }
        return modified;
    }

    /**
     * Сохраняет только элементы, присутствующие в указанной коллекции
     * @param c коллекция элементов для сохранения
     * @return true если список изменился
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        // Сохраняем только элементы, содержащиеся в указанной коллекции
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(i);
                i--; // Корректируем индекс из-за сдвига элементов
                modified = true;
            }
        }
        return modified;
    }

    /**
     * Возвращает подсписок от fromIndex (включительно) до toIndex (исключительно)
     * @param fromIndex начальный индекс (включительно)
     * @param toIndex конечный индекс (исключительно)
     * @return подсписок указанного диапазона
     * @throws IndexOutOfBoundsException если индексы некорректны
     */
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        // Проверка корректности индексов
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }

        // Создаем новый список для подсписка
        ListA<E> subList = new ListA<>(toIndex - fromIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            subList.add(get(i));
        }
        return subList;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Методы итераторов                          ////////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Возвращает итератор для последовательного обхода списка
     * @return итератор элементов списка
     */
    @Override
    public Iterator<E> iterator() {
        // Реализация простого итератора
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
                    throw new NoSuchElementException();
                }
                return (E) elements[currentIndex++];
            }
        };
    }

    /**
     * Возвращает двунаправленный итератор для списка
     * @return list iterator начинающийся с начала списка
     */
    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    /**
     * Возвращает двунаправленный итератор, начинающийся с указанной позиции
     * @param index начальная позиция итератора
     * @return list iterator начинающийся с указанной позиции
     * @throws IndexOutOfBoundsException если индекс выходит за границы
     */
    @Override
    public ListIterator<E> listIterator(int index) {
        // Проверка корректности индекса
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        // Реализация двунаправленного итератора
        return new ListIterator<E>() {
            private int currentIndex = index;
            private int lastReturned = -1; // Индекс последнего возвращенного элемента

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                lastReturned = currentIndex;
                return (E) elements[currentIndex++];
            }

            @Override
            public boolean hasPrevious() {
                return currentIndex > 0;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E previous() {
                if (!hasPrevious()) {
                    throw new NoSuchElementException();
                }
                lastReturned = --currentIndex;
                return (E) elements[currentIndex];
            }

            @Override
            public int nextIndex() {
                return currentIndex;
            }

            @Override
            public int previousIndex() {
                return currentIndex - 1;
            }

            @Override
            public void remove() {
                if (lastReturned == -1) {
                    throw new IllegalStateException();
                }
                ListA.this.remove(lastReturned);
                currentIndex = lastReturned;
                lastReturned = -1;
            }

            @Override
            public void set(E e) {
                if (lastReturned == -1) {
                    throw new IllegalStateException();
                }
                ListA.this.set(lastReturned, e);
            }

            @Override
            public void add(E e) {
                ListA.this.add(currentIndex++, e);
                lastReturned = -1;
            }
        };
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Методы преобразования в массив             ////////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Преобразует список в массив указанного типа
     * @param a массив для заполнения
     * @return массив содержащий все элементы списка
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        // Преобразуем список в массив
        if (a.length < size) {
            return (T[]) Arrays.copyOf(elements, size, a.getClass());
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null; // Завершающий null согласно контракту
        }
        return a;
    }

    /**
     * Преобразует список в массив объектов
     * @return массив содержащий все элементы списка
     */
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }
}