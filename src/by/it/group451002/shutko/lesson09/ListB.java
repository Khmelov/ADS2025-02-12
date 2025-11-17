package by.it.group451002.shutko.lesson09;

import java.util.*;


public class ListB<E> implements List<E> {
    private Object[] array;
    private int size;
    private final int DEFAULT_CAPACITY = 16;
    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    // Нужно было реализовать более полную версию списка с дополнительными методами интерфейса List<E>
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        if (array == null || size == 0){
            return "[]";
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

    public ListB() {
        this.array = new Object[DEFAULT_CAPACITY];
    }

    @Override
    public boolean add(E e) {
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
    public E remove(int index) {
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
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {  // add(int index, E element) - вставка по индексу
        if (size == array.length){
            Object[] array = new Object[this.array.length * 2];
            for (int i = 0; i < size; i++) {
                array[i] = this.array[i];
            }
            this.array = array;
        }
        Object[] ostArray = new Object[this.array.length - index];
        for (int i = 0; i < ostArray.length; i++){
            ostArray[i] = this.array[index + i];
        }
        this.array[index] = element;
        size++;
        for(int i = index + 1; i < size; i++){
            this.array[i] = ostArray[i - index - 1];
        }
    }

    @Override
    public boolean remove(Object o) {   // remove(Object o) - удаление по значению
        boolean result = false;
        for (int i = 0; i < size; i++){
            if (array[i].equals(o)){
                result = true;
                size--;
                Object[] array = new Object[size + 1];
                for (int j = 0; j < i; j++){
                    array[j] = this.array[j];
                }
                for (int j = i + 1; j <= size; j++){
                    array[j - 1] = this.array[j];
                }
                this.array = array;
                break;
            }
        }
        return result;
    }

    @Override
    public E set(int index, E element) {  // set(int index, E element) - замена элемента
        Object oldValue = array[index];
        array[index] = element;
        return (E) oldValue;
    }


    @Override
    public boolean isEmpty() {  // isEmpty() - проверка на пустоту
        return size == 0;
    }


    @Override
    public void clear() {  // clear() - очистка списка
        array = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public int indexOf(Object o) {  // indexOf(Object o) - поиск первого вхождения
        for (int i = 0; i < size; i++){
            if (array[i].equals(o)){
                return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {  // get(int index) - получение элемента по индексу
        return (E) array[index];
    }

    @Override
    public boolean contains(Object o) {  // contains(Object o) - проверка наличия элемента
        for (int i = 0; i < size; i++){
            if (array[i].equals(o)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {  // lastIndexOf(Object o) - поиск последнего вхождения
        int lastIndex = -1;
        for (int i = 0; i < size; i++){
            if (array[i].equals(o)){
                lastIndex = i;
            }
        }
        return lastIndex;
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
