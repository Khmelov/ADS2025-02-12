package by.it.group451002.kita.lesson09;

import java.util.*;

public class ListC<E> implements List<E> {
    // реализация списка на основе массива
    private E[] list = (E[]) new Object[0];
    private int size = 0;
    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    // автоматическое расширение на 1.5
    private void checkCapacity(){
        if (size+1 > list.length){
            E[] newList = (E[]) new Object[(int)(list.length*1.5) + 1];

            for (int i = 0; i < size; i++)
                newList[i] = list[i];

            list = newList;//копия не создается, создается ссылка
        }
    }

    public void changeCapacity(int value){
        if (size+value > list.length){
            E[] newList = (E[]) new Object[list.length + value];

            for (int i = 0; i < size; i++)
                newList[i] =  list[i];

            list = newList;
        }

    }
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    // преобразует список в строку
    @Override
    public String toString() {
        // создаем динамическую сроку
        StringBuilder str = new StringBuilder("[");
        Iterator<E> iter = iterator();

        // пока существует след. элемент
        while (iter.hasNext()){
            // добавляем текущее значение
            str.append(iter.next());
            if (iter.hasNext())
                str.append(", ");
        }
        str.append("]");
        return str.toString();
    }

    // добавление элемента в конец
    @Override
    public boolean add(E e) {
        checkCapacity();
        list[size++] = e;
        return true;
    }

    // удаление по индексу
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        // запоминаем элемент
        E elemToDel = list[index];

        // удаляем путем сдвига
        for (int i = index; i < size-1; i++)
            list[i] = list[i+1];
        // посдедний удаляем
        list[--size] = null;

        return elemToDel;
    }

    @Override
    public int size() {
        return size;
    }

    // добавление по индексу
    @Override
    public void add(int index, E element) {
        checkCapacity();
        for (int i = size; i > index; i--){
            list[i] = list[i-1];
        }
        list[index] = element;
        size++;
    }

    // удаление
    @Override
    public boolean remove(Object o) {
        // удаление по индексу
        int index = indexOf(o);
        if (index != -1){
            remove(index);
            return true;
        }
        return false;
    }

    // элементу с индексом index присваиваем значение element
    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();

        E oldElem = list[index];
        list[index] = element;
        // возр. пред.
        return oldElem;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // очищение списка
    @Override
    public void clear() {
        for (int i = 0; i < size; i++)
            list[i] = null;
        size = 0;
    }

    // поиск первого вхождения
    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++){
            if (Objects.equals(o,list[i]))
                return i;
        }
        return -1;
    }

    // получить элемент по индексу
    @Override
    public E get(int index) {
        if (index < 0 || index >= size )
            throw new IndexOutOfBoundsException();

        return list[index];
    }


    // проверяет, есть ли элемент в списке
    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    // поиск последнего вхождения
    @Override
    public int lastIndexOf(Object o) {
        for (int i = size-1; i >= 0; i--){
            if (Objects.equals(o, list[i]))
                return i;
        }
        return -1;
    }

    // проверяем, содержит ли список всю коллекцию
    // передается коллекция (набор элементов, который можно перебрать)
    @Override
    public boolean containsAll(Collection<?> c) {
        Iterator<?> iter = c.iterator();
        while (iter.hasNext()){
            Object currElem = iter.next();
            if (indexOf(currElem) == -1)
                return false;
        }

        return true;
    }

    // добавление коллекции
    @Override
    // extends E - приведение типов
    public boolean addAll(Collection<? extends E> c) {
        changeCapacity(c.size());
        int i = size;
        size += c.size();
        Iterator<? extends E> iter = c.iterator();
        if (c.isEmpty())
            return false;

        while (iter.hasNext())
            list[i++] = iter.next();

        return true;
    }

    // добавление коллекции по индексу
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException();

        changeCapacity(c.size());
        if (c.isEmpty())
            return false;

        //сдвигаем все элементы списка, чтобы освободить место для элементов коллекции
        for (int i = size-1; i >= index ; i--)
            list[i+c.size()] = list[i];

        int i = index;
        Iterator<? extends E> iter = c.iterator();
        //вставляем элементы коллекции в список
        while (iter.hasNext())
            list[i++] = iter.next();

        size += c.size();
        return true;
    }

    // удаляем коллекцию
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean isChanged = false;
        int index = 0;


        for (int i = 0; i < size; i++){
            // если в коллекции нет элемента
            if (!c.contains(list[i]))
                list[index++] = list[i];

            else
                isChanged = true;
        }

        for (int j = index; j < size; j++)
            list[j] = null;

        size = index;
        return isChanged;
    }

    // оставляем пересечения
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean isChanged = false;
        int i = 0;

        while (i < size){
            if (!c.contains(list[i])){
                remove(i--);
                isChanged = true;
            }
            i++;
        }

        return isChanged;
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
        // создаем анонимный класс
        return new Iterator<E>() {
            private int index = 0;
            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public E next() {
                return list[index++];
            }
        };
    }

}