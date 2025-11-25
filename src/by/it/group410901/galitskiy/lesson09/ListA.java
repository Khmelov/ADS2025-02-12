package by.it.group410901.galitskiy.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    int size = 0;  //  хранит текущий размер списка
    E[] arr;  // массив, который хранит элементы списка

    ListA() {
        this.arr =(E[]) new Object[this.size];  // инициализирует массив arr начального размера (пустой массив)
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    // возвращает строковое представление списка, с элементами через запятую в квадратных скобках.
    public String toString() {
        StringBuilder res = new StringBuilder();

        for(E element : this.arr) {
            if(element != null)
                res.append(element).append(", ");
        }

        if(!res.isEmpty()) res.delete(res.length() - 2, res.length());
        res.insert(0, "[");
        res.append("]");
        return res.toString();
    }

    @Override
    // добавляет элемент e в конец списка, увеличивает size и создает новый массив с увеличенным размером
    public boolean add(E e) {
        E[] Temp = (E[]) new Object[this.size + 1];
        System.arraycopy(this.arr, 0, Temp, 0, this.size);
        Temp[this.size] = e;
        this.arr = Temp;
        this.size++;
        return true;
    }

    @Override
    // удаляет элемент по заданному индексу, сдвигая элементы после него влево. Возвращает удаленный элемент
    public E remove(int index) {
        E[] Temp = (E[]) new Object[this.size - 1];
        E data = this.arr[index];
        System.arraycopy(this.arr, 0, Temp, 0, index);
        System.arraycopy(this.arr, index + 1, Temp, index, this.size - index - 1);
        this.arr = Temp;
        this.size--;
        return data;
    }

    @Override
    // возвращает текущий размер списка (size)
    public int size() {
        return this.size;
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
