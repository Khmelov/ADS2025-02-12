package by.it.group451001.yarkovich.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    // Внутренний массив для хранения элементов
    private E[] elements;
    // Текущее количество элементов в списке
    private int size;
    // Начальная емкость массива
    private static final int DEFAULT_CAPACITY = 10;

    // Конструктор
    @SuppressWarnings("unchecked")
    public ListA() {
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

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

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

    // Вспомогательный метод для увеличения емкости массива
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = elements.length * 2;
        E[] newElements = (E[]) new Object[newCapacity];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    // Необязательные методы (заглушки)

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

    @Override
    public Object[] toArray() {
        return new Object[0];
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
        };
    }
}