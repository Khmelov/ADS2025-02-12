package by.it.group451004.bortsevich.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {

    // Внутренний массив для хранения элементов
    private E[] elements;
    // Текущее количество элементов в списке
    private int size;
    // Начальная емкость массива
    private static final int INITIAL_CAPACITY = 10;

    // Конструктор - создает пустой список
    @SuppressWarnings("unchecked")
    public ListB() {
        elements = (E[]) new Object[INITIAL_CAPACITY];
        size = 0;
    }

    // Вспомогательный метод для увеличения емкости при необходимости
    @SuppressWarnings("unchecked")
    private void ensureCapacity(int minCapacity) {
        // Если текущей емкости недостаточно, увеличиваем массив
        if (minCapacity > elements.length) {
            // Увеличиваем емкость в 1.5 раза, но не меньше требуемой
            int newCapacity = Math.max(elements.length * 3 / 2 + 1, minCapacity);
            E[] newElements = (E[]) new Object[newCapacity];
            // Копируем все элементы из старого массива в новый
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
    }

    /////////////////////////////////////////////////////////////////////////
    ////////////////// Обязательные к реализации методы /////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        // Если список пустой, возвращаем пустые скобки
        if (size == 0) {
            return "[]";
        }

        // Используем StringBuilder для эффективного построения строки
        StringBuilder sb = new StringBuilder("[");
        // Добавляем первый элемент
        sb.append(elements[0]);

        // Добавляем остальные элементы через запятую
        for (int i = 1; i < size; i++) {
            sb.append(", ").append(elements[i]);
        }

        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        // Проверяем, нужно ли увеличить емкость массива
        ensureCapacity(size + 1);
        // Добавляем элемент в конец списка
        elements[size] = e;
        // Увеличиваем счетчик размера
        size++;
        return true; // Всегда возвращаем true для совместимости с Collection
    }

    @Override
    public E remove(int index) {
        // Проверяем корректность индекса
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        // Сохраняем удаляемый элемент для возврата
        E removedElement = elements[index];

        // Сдвигаем все элементы после удаляемого на одну позицию влево
        // Это перезаписывает удаляемый элемент
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        // Очищаем последнюю ячейку и уменьшаем размер
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
        // Проверяем корректность индекса (можно добавить в конец - index == size)
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        // Увеличиваем емкость если необходимо
        ensureCapacity(size + 1);

        // Сдвигаем все элементы от конца до позиции index вправо
        // Начинаем с конца чтобы не перезаписать элементы
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }

        // Вставляем новый элемент на освободившуюся позицию
        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        // Ищем первый элемент, равный переданному объекту
        for (int i = 0; i < size; i++) {
            // Проверяем равенство с учетом null
            if ((o == null && elements[i] == null) ||
                    (o != null && o.equals(elements[i]))) {
                // Нашли элемент - удаляем его по индексу
                remove(i);
                return true;
            }
        }
        // Элемент не найден
        return false;
    }

    @Override
    public E set(int index, E element) {
        // Проверяем корректность индекса
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        // Сохраняем старый элемент для возврата
        E oldElement = elements[index];
        // Заменяем элемент на новый
        elements[index] = element;
        return oldElement;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        // Очищаем все ссылки на элементы (помогает сборщику мусора)
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        // Сбрасываем размер до 0
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        // Проходим по списку от начала до конца
        for (int i = 0; i < size; i++) {
            // Проверяем равенство с учетом null
            if ((o == null && elements[i] == null) ||
                    (o != null && o.equals(elements[i]))) {
                return i; // Нашли - возвращаем индекс
            }
        }
        return -1; // Не нашли
    }

    @Override
    public E get(int index) {
        // Проверяем корректность индекса
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return elements[index];
    }

    @Override
    public boolean contains(Object o) {
        // Используем метод indexOf для проверки наличия элемента
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        // Проходим по списку от конца к началу
        for (int i = size - 1; i >= 0; i--) {
            // Проверяем равенство с учетом null
            if ((o == null && elements[i] == null) ||
                    (o != null && o.equals(elements[i]))) {
                return i; // Нашли - возвращаем индекс
            }
        }
        return -1; // Не нашли
    }

    /////////////////////////////////////////////////////////////////////////
    ////////////////// Опциональные к реализации методы /////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean containsAll(Collection<?> c) {
        // Проверяем, что все элементы коллекции c содержатся в этом списке
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // Если переданная коллекция пустая, ничего не делаем
        if (c.isEmpty()) {
            return false;
        }

        // Увеличиваем емкость чтобы вместить все новые элементы
        ensureCapacity(size + c.size());

        // Добавляем все элементы из коллекции в конец списка
        for (E element : c) {
            add(element);
        }

        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        // Проверяем корректность индекса
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        // Если переданная коллекция пустая, ничего не делаем
        if (c.isEmpty()) {
            return false;
        }

        // Увеличиваем емкость чтобы вместить все новые элементы
        ensureCapacity(size + c.size());

        // Сдвигаем существующие элементы вправо чтобы освободить место
        int numNew = c.size();
        for (int i = size - 1; i >= index; i--) {
            elements[i + numNew] = elements[i];
        }

        // Вставляем новые элементы
        int i = index;
        for (E element : c) {
            elements[i++] = element;
        }

        size += numNew;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        // Проходим по списку и удаляем все элементы, содержащиеся в коллекции c
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
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
        // Удаляем все элементы, которые НЕ содержатся в коллекции c
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(i);
                i--; // Уменьшаем счетчик т.к. элементы сдвинулись
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        // Проверяем корректность индексов
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException("fromIndex: " + fromIndex + ", toIndex: " + toIndex + ", size: " + size);
        }

        // Создаем новый список для подсписка
        ListB<E> subList = new ListB<>();
        // Копируем элементы из заданного диапазона
        for (int i = fromIndex; i < toIndex; i++) {
            subList.add(elements[i]);
        }
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
        // Создаем новый массив Object и копируем в него элементы
        Object[] array = new Object[size];
        for (int i = 0; i < size; i++) {
            array[i] = elements[i];
        }
        return array;
    }

    /////////////////////////////////////////////////////////////////////////
    ////////// Методы для итератора (нужны для корректной отладки) //////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        // Возвращаем простой итератор для прохода по элементам
        return new Iterator<E>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public E next() {
                return elements[currentIndex++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove not supported");
            }
        };
    }
}