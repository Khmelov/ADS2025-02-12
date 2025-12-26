package by.it.group410901.galitskiy.lesson09;

import java.util.*;

public class ListB<E> implements List<E> {

    int size = 0;
    E[] arr;

    ListB()
    {
        this.arr =(E[]) new Object[this.size];
    }
    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
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
    public boolean add(E e) {
        E[] Temp = (E[]) new Object[this.size + 1];
        System.arraycopy(this.arr, 0, Temp, 0, this.size);
        Temp[this.size] = e;
        this.arr = Temp;
        this.size++;
        return true;
    }

    @Override
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
    public int size() {
        return this.size;
    }

    @Override
    // удаляет первое вхождение объекта o, если оно есть в списке
    public boolean remove(Object o) {
        for(int i = 0; i < this.arr.length; i++){
            if(this.arr[i] != null && this.arr[i].equals(o)){
                this.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    // добавляет элемент element на определенную позицию index
    public void add(int index, E element) {
        if(this.size == this.arr.length){
            E[] temp = (E[]) new Object[this.arr.length * 2];
            System.arraycopy(this.arr, 0, temp, 0, this.arr.length);
            this.arr = temp;
        }
        for(int i = this.size; i > index; i--){
            this.arr[i] = this.arr[i - 1];
        }
        this.arr[index] = element;
        this.size++;
    }

    @Override
    // заменяет элемент на позиции index новым элементом element
    public E set(int index, E element) {
        E data = this.arr[index];
        this.arr[index] = element;
        return data;
    }


    @Override
    // проверяет, пуст ли список
    public boolean isEmpty() {
        return this.size == 0;
    }


    @Override
    // очищает список
    public void clear() {
        this.size = 0;
        this.arr = (E[]) new Object[this.size];
    }

    @Override
    // возвращает индекс первого вхождения объекта o
    public int indexOf(Object o) {
        for(int i = 0; i < this.arr.length; i++){
            if(this.arr[i] != null && this.arr[i].equals(o)){
                return i;
            }
        }
        return -1;
    }

    @Override
    // возвращает элемент на позиции index
    public E get(int index) {
        return this.arr[index];
    }

    @Override
    // проверяет, содержится ли объект o в списке
    public boolean contains(Object o) {
        for(int i = 0; i < this.arr.length; i++){
            if(this.arr[i] != null && this.arr[i].equals(o)){
                return true;
            }
        }
        return false;
    }

    @Override
    // возвращает индекс последнего вхождения объекта o
    public int lastIndexOf(Object o) {
        for(int i = this.arr.length - 1; i >= 0; i--){
            if(this.arr[i] != null && this.arr[i].equals(o)){
                return i;
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
