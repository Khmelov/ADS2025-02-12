package by.it.group410902.derzhavskaya_ludmila.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    // Константы для начальной емкости массива
    private static final int DEFAULT_CAPACITY = 10;

    // Внутренний массив для хранения элементов
    private Object[] elements;

    // Текущее количество элементов в списке
    private int size;

    // Конструктор по умолчанию
    public ListA() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }


    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString(){
        // Если список пуст, возвращаем пустые скобки
        if (size == 0) {
            return "[]";
        }

        // Строим строковое представление списка
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
    public boolean add(E e){
        // Проверяем, нужно ли увеличивать емкость массива
        if (size + 1 > elements.length) {
            // Увеличиваем емкость в 2 раза
            int newCapacity = elements.length * 2;
            Object[] newElements = new Object[newCapacity];
            // Копируем элементы в новый массив
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
        // Добавляем элемент в конец и увеличиваем размер
        elements[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        // Проверяем корректность индекса
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        // Сохраняем удаляемый элемент
        E removedElement = (E) elements[index];

        // Сдвигаем элементы влево, начиная с позиции после удаляемого
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        // Обнуляем последний элемент и уменьшаем размер
        elements[--size] = null;
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
        // Проверяем корректность индекса для добавления
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        // Проверяем, нужно ли увеличивать емкость
        if (size + 1 > elements.length) {
            // Увеличиваем емкость в 2 раза
            int newCapacity = elements.length * 2;
            Object[] newElements = new Object[newCapacity];
            // Копируем элементы в новый массив
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }

        // Сдвигаем элементы вправо, начиная с позиции добавления
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        // Вставляем новый элемент
        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        // Поиск элементов по equals()
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        // Проверяем корректность индекса
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        // Сохраняем старое значение и устанавливаем новое
        E oldValue = (E) elements[index];
        elements[index] = element;
        return oldValue;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        // Обнуляем все ссылки
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        // Поиск первого вхождения элемента
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        // Проверяем корректность индекса
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        // Возвращаем элемент по индексу
        E element = (E) elements[index];
        return element;
    }

    @Override
    public boolean contains(Object o) {
        // Проверяем наличие элемента в списке
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        // Поиск последнего вхождения элемента
        for (int i = size - 1; i >= 0; i--) {
            if (o.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }



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

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return null;
    }

}
