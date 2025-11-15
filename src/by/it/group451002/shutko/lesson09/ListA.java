package by.it.group451002.shutko.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class ListA<E> implements List<E> {
    private Object[] array;  // array - массив, где фактически хранятся элементы списка
    private int size;  // size - отслеживает текущее количество элементов
    private final int DEFAULT_CAPACITY = 16;  // DEFAULT_CAPACITY - начальная емкость массива
    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    // Задача заключается в том, чтобы реализовать свой собственный класс списка
    // Реализовать внутреннее хранилище: Обычно это массив (Object[]), так как именно так устроен ArrayList.
    //
    // Реализовать логику управления размером: Нужно следить за количеством элементов (size) и вместимостью массива (capacity).
    // Когда массив заполняется, нужно создавать новый массив большего размера и копировать в него элементы (динамическое расширение).
    //
    // Реализовать обязательные методы: add, remove, size, toString.
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        if (array == null || size == 0){
            return null;
        }
        String result = "[";
        for (Object object : array) {
            if (object != null){
                result = result + object.toString() + ", ";;
            }

        }
        result = result.substring(0, result.length() - 2);
        result += "]";
        return result;
    }

    public ListA() {
        this.array = new Object[DEFAULT_CAPACITY]; // Создает массив начального размера по умолчанию.
    }

    @Override
    public boolean add(E e) {  // метод добавляет элемент в конец списка, автоматически расширяя массив при необходимости.
        if (size == array.length){
            Object[] array = new Object[this.array.length * 2];
            for (int i = 0; i < size; i++) {
                array[i] = this.array[i];
            }
            this.array = array;
        }
        array[size] = e;
        size++;
        return true;
    }

    @Override
    public E remove(int index) {  // метод удаляет элемент по индексу, сдвигая все последующие элементы на одну позицию влево.
        if (size == 0){
            return null;
        }
        size--;
        Object[] array = new Object[size + 1];
        for (int i = 0; i < index; i++){
            array[i] = this.array[i];
        }
        for (int i = index + 1; i <= size; i++){
            array[i - 1] = this.array[i];
        }
        E oldValue = (E) this.array[index];
        this.array = array;
        return oldValue;
    }

    @Override
    public int size() {  // Просто возвращает текущее количество элементов.
        return size;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public void add(int index, E element) {

    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public E set(int index, E element) {
        return null;
    }


    @Override
    public boolean isEmpty() {
        return false;
    }


    @Override
    public void clear() {

    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
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
