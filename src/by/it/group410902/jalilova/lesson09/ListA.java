package by.it.group410902.jalilova.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    private Object[] elements = new Object[10];
    //массив для хранения элементов
    private int size = 0;//текущее кол-во элементов
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {//прохожу по всем элементам и собираю их в строку [,,]
        if (size == 0) {//если список пуст
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
        return sb.toString();//возвращаем строку

    }

    @Override
    public boolean add(E e) {
        if (size == elements.length) {//если массив заполнен, создаем новый
            Object[] newElements = new Object[elements.length * 2];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
        elements[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {//удаляем элемент по индексу, все последующие сдвигаем влево
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        @SuppressWarnings("unchecked")
        E removedElement = (E) elements[index];// сохраняем удаляемый элемент

        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1]; // сдвигаем элементы влево на 1 позицию
        }

        elements[--size] = null;
        return removedElement;
    }

    @Override
    public int size() {
        return size;
    } //возвращение текущего кол-ва элементов

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public void add(int index, E element) {//вставляем элемент на конкретную позицию
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (size == elements.length) {
            Object[] newElements = new Object[elements.length * 2];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }

        for (int i = size; i > index; i--) {//сдвигаем элементы вправо
            elements[i] = elements[i - 1];
        }

        elements[index] = element;
        size++;

    }

    @Override
    public boolean remove(Object o) {//ищет первый элемент, который равен переданному объекту, удаляет его по индексу
        for (int i = 0; i < size; i++) {
            if ((o == null && elements[i] == null) || (o != null && o.equals(elements[i]))) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {//заменяю элемент на указанной позиции и возвращаю старое значение
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        @SuppressWarnings("unchecked")
        E oldValue = (E) elements[index];
        elements[index] = element;
        return oldValue;
    }


    @Override
    public boolean isEmpty() { //проверяю равен ли размер нулю
        return size == 0;
    }


    @Override
    public void clear() {//обнуляю все элементы в массиве, сбрасываю размер к нулю
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;

    }

    @Override
    public int indexOf(Object o) {//иду с начала списка, ищу первый подходящий элемент
        for (int i = 0; i < size; i++) {
            if ((o == null && elements[i] == null) || (o != null && o.equals(elements[i]))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {//возвращаю элемент по указанному индексу
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        @SuppressWarnings("unchecked")
        E element = (E) elements[index];
        return element;
    }

    @Override
    public boolean contains(Object o) { //использую indexOf для проверки наличия элемента
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) { //иду с конца списка и ищу последний подходящий элемент
        for (int i = size - 1; i >= 0; i--) {
            if ((o == null && elements[i] == null) || (o != null && o.equals(elements[i]))) {
                return i;
            }
        }
        return -1;

    }

    @Override
    public boolean containsAll(Collection<?> c) { //для каждого элемента коллекции проверяю, есть ли он в списке
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {//поочерёдно добавляю все элементы из коллекции в конец списка
        if (c.isEmpty()) {
            return false;
        }

        for (E element : c) {
            add(element);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {//вставляю все элементы из коллекции, начиная с позиции
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (c.isEmpty()) {
            return false;
        }

        int i = index;
        for (E element : c) {
            add(i++, element);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {//удаляю из списка все элементы, которые есть в коллекции
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                remove(i);
                i--;
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {//оставляю в списке только элементы, которые есть в коллекции
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(i);
                i--;
                modified = true;
            }
        }
        return modified;
    }


    @Override
    public List<E> subList(int fromIndex, int toIndex) { //создаю новый список и копирую в него элементы из диапазона
        if (fromIndex < 0  || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }

        ListA<E> subList = new ListA<>();
        for (int i = fromIndex; i < toIndex; i++) {
            @SuppressWarnings("unchecked")
            E element = (E) elements[i];
            subList.add(element);
        }
        return subList;
    }

    @Override
    public ListIterator<E> listIterator(int index) {//возвращает итератор, который обходит с позиции index
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return new ListItr(index);
    }

    @Override
    public ListIterator<E> listIterator() {//возвращает итератор, который начинает обход с начала списка
        return new ListItr(0);
    }

    @Override
    public <T> T[] toArray(T[] a) {//если массив достаточно большой, копирую в него элементы, нет - создаю новый
        if (a.length < size) {
            T[] newArray = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
            for (int i = 0; i < size; i++) {
                newArray[i] = (T) elements[i];
            }
            return newArray;
        }

        for (int i = 0; i < size; i++) {
            a[i] = (T) elements[i];
        }

        if (a.length > size) {
            a[size] = null;
        }

        return a;
    }

    @Override
    public Object[] toArray() { //создаю новый массив и копирую в него все элементы списка
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
    public Iterator<E> iterator() {//возвращаю объект, который позволяет последовательно проходить по списку
        return new Itr();
    }
    private class Itr implements Iterator<E> {
        int cursor = 0;
        int lastRet = -1;

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
            cursor = lastRet;
            lastRet = -1;
        }
    }

    private class ListItr extends Itr implements ListIterator<E> {
        ListItr(int index) {
            cursor = index;
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
            cursor++;
            lastRet = -1;
        }
    }

}