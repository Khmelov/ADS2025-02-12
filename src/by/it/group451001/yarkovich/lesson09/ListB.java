package by.it.group451001.yarkovich.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    // Внутренний массив для хранения элементов
    private E[] elements;
    // Текущее количество элементов в списке
    private int size;
    // Начальная емкость массива
    private static final int DEFAULT_CAPACITY = 10;

    // Конструктор
    @SuppressWarnings("unchecked")
    public ListB() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

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

    @Override
    public boolean add(E e) {
        // Проверяем, нужно ли увеличивать массив
        if (size == elements.length) {
            resize();
        }
        // Добавляем элемент в конец
        elements[size] = e;
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        // Проверяем корректность индекса
        checkIndex(index);

        // Сохраняем удаляемый элемент
        E removedElement = elements[index];

        // Сдвигаем все элементы после удаляемого на одну позицию влево
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        // Очищаем последний элемент и уменьшаем размер
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
        // Проверяем корректность индекса (допустим index == size для добавления в конец)
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        // Проверяем, нужно ли увеличивать массив
        if (size == elements.length) {
            resize();
        }

        // Сдвигаем элементы вправо, чтобы освободить место
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }

        // Вставляем новый элемент
        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        // Ищем индекс элемента
        int index = indexOf(o);
        if (index == -1) {
            return false;
        }

        // Удаляем элемент по найденному индексу
        remove(index);
        return true;
    }

    @Override
    public E set(int index, E element) {
        // Проверяем корректность индекса
        checkIndex(index);

        // Сохраняем старый элемент
        E oldElement = elements[index];

        // Заменяем элемент
        elements[index] = element;

        return oldElement;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        // Очищаем все элементы
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        // Ищем первое вхождение элемента
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

    @Override
    public E get(int index) {
        // Проверяем корректность индекса
        checkIndex(index);
        return elements[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        // Ищем последнее вхождение элемента
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
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

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

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // Добавляем все элементы коллекции в конец списка
        if (c.isEmpty()) {
            return false;
        }

        // Увеличиваем массив при необходимости
        if (size + c.size() > elements.length) {
            resize(Math.max(elements.length * 2, size + c.size()));
        }

        // Добавляем все элементы
        for (E element : c) {
            elements[size++] = element;
        }

        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        // Проверяем корректность индекса
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (c.isEmpty()) {
            return false;
        }

        // Увеличиваем массив при необходимости
        if (size + c.size() > elements.length) {
            resize(Math.max(elements.length * 2, size + c.size()));
        }

        // Сдвигаем элементы вправо для освобождения места
        int shift = c.size();
        for (int i = size + shift - 1; i >= index + shift; i--) {
            elements[i] = elements[i - shift];
        }

        // Вставляем новые элементы
        int i = index;
        for (E element : c) {
            elements[i++] = element;
        }

        size += shift;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // Удаляем все элементы, содержащиеся в коллекции
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                remove(i);
                i--; // Уменьшаем индекс, так как элементы сдвинулись
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // Удаляем все элементы, НЕ содержащиеся в коллекции
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(i);
                i--; // Уменьшаем индекс, так как элементы сдвинулись
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

    // Вспомогательный метод для проверки индекса
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    // Вспомогательный метод для увеличения емкости массива (в 2 раза)
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = elements.length * 2;
        E[] newElements = (E[]) new Object[newCapacity];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    // Вспомогательный метод для увеличения емкости массива до указанного размера
    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        E[] newElements = (E[]) new Object[newCapacity];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    // Необязательные методы (заглушки)

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
        Object[] array = new Object[size];
        System.arraycopy(elements, 0, array, 0, size);
        return array;
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
            public E next() {
                return elements[currentIndex++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        };
    }
}