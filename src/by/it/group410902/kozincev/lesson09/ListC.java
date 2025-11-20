package by.it.group410902.kozincev.lesson09;

import java.util.*;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private E[] elements;
    private int size;

    public ListC(){
        this.elements = (E[]) new Object[10];
        this.size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        if (size == 0){
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for(int i = 0; i < size; i++){
            sb.append(elements[i]);
            if(i < size - 1){
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        if(size >= elements.length){
            E[] newElements = (E[]) new Object[elements.length*2];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
        elements[size] = e;
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        if(index < 0 || index >= size){
            return null;
        }

        E removed = elements[index];

        for(int i = index; i < size; i++){
            elements[i] = elements[i + 1];
        }
        elements[size - 1] = null;
        size--;

        return removed;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if(index < 0 || index >= size){
            return;
        }
        if(size >= elements.length){
            E[] newElements = (E[]) new Object[elements.length*2];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
        // [0, 1, 2, 3, 4, 5]
        // [0, 1, 2, 6, 3, 4, 5]
        for(int i = size; i > index; i--){
            elements[i] = elements[i - 1];
        }
        size++;

        elements[index] = element;
    }

    @Override
    public boolean remove(Object o) {
        for(int i = 0; i < size; i++){
            if(o != null && o.equals(elements[i])){
                remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        E prev = elements[index];
        elements[index] = element;
        return prev;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        for(int i = 0; i < size; i++){
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        for(int i = 0; i < size; i++){
            if(Objects.equals(o, elements[i])){
                return i;
            }
        }
        return -1;

    }

    @Override
    public E get(int index) {
        return index >= 0 && index < size ? elements[index] : null;
    }

    @Override
    public boolean contains(Object o) {
        for(int i = 0; i < size; i++){
            if(Objects.equals(o, elements[i])){
                return true;
            }
        }
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        for(int i = size - 1; i >= 0; i--){
            if(o != null && o.equals(elements[i])){
                return i;
            }
        }
        return -1;

    }

    @Override
    public boolean containsAll(Collection<?> c) {

        for(Object o: c){
            if(!contains(o)){
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {

        if(c.isEmpty()){
            return false;
        }

        for(E el: c){
            add(el);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {

        int currIndex = index;
        if(c.isEmpty()){
            return false;
        }
        for(E el: c){
            add(currIndex++, el);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean res = false;
        for(int i = 0; i < size; i++){
            if(c.contains(elements[i])){
                remove(i);
                i--;
                res = true;

            }
        }
        return res;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean res = false;
        for(int i = 0; i < size; i++){
            if(!c.contains(elements[i])){
                remove(i);
                i--;
                res = true;

            }
        }
        return res;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

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
