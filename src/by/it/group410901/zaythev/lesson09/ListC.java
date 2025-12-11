package by.it.group410901.zaythev.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {
    static final int defaultSize = 0;
    private int size = 0;
    private E[] elements;
    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    public ListC() { this(defaultSize); }

    public ListC(int count) {
        elements = (E[]) new Object[count];
    }
    @Override
    public String toString() {
        String tempStr = "[";

        if (size > 0) {
            for (int i = 0; i < size - 1; i++) {
                tempStr += elements[i] + ", ";
            }
            tempStr += elements[size - 1] + "]";
        }
        else { tempStr += "]"; }

        return tempStr;
    }

    @Override
    public boolean add(E e) {
        if (size == elements.length) {
            E[] temp = (E[]) new Object[elements.length * 2 + 1];
            System.arraycopy(elements, 0, temp, 0, size);
            elements = temp;
        }
        elements[size] = e;
        ++size;

        return true;
    }

    @Override
    public E remove(int index) {
        if ( (index > size - 1) || (index < 0) )
            return null;

        E temp = elements[index];
        System.arraycopy(elements, index+1, elements, index, size - index - 1);
        elements[--size] = null;

        return temp;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if ((index < 0) || (index >= size)) {
            return;
        }
        if (size == elements.length) {
            E[] newElements = (E[]) new Object[elements.length * 2];

            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }

        size++;
        for (int i = size - 1; i > index; i--) {
            elements[i] = elements[i - 1];
        }

        elements[index] = element;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) {
                System.arraycopy(elements, i+1, elements, i, size - i - 1);
                elements[--size] = null;
                return true;
            }
        }

        return false;
    }

    @Override
    public E set(int index, E element) {
        if ((index < 0) || (index >= size))
            return null;

        E temp = elements[index];
        elements[index] = element;

        return temp;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        elements = (E[]) new Object[defaultSize];
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if ((index < 0) || (index >= size)) {
            return null;
        }
        return elements[index];
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i]))
                return true;
        }
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (o.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    // проверяет, содержатся ли все элементы коллекции c в списке
    public boolean containsAll(Collection<?> c) {
        for (Object element: c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    // добавляет все элементы из коллекции c
    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;

        for (Object element: c) {
            add((E) element);
            result = true;
        }

        return result;
    }

    @Override
    // добавляет все элементы коллекции c начиная с позиции index
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean result = false;

        for (Object element: c) {
            add(index, (E) element);
            index++;
            result = true;
        }

        return result;
    }

    @Override
    // удаляет все элементы из коллекции c, которые есть в списке
    public boolean removeAll(Collection<?> c) {
        boolean flag = false;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                remove(i);
                i--;
                flag = true;
            }
        }
        return flag;
    }

    @Override
    // оставляет в списке только те элементы, которые содержатся в коллекции c
    public boolean retainAll(Collection<?> c) {
        boolean result = false;

        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(elements[i]);
                i--;
                result = true;
            }
        }

        return result;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    // возвращает подсписок от fromIndex до toIndex
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    // возвращает ListIterator для итерации по элементам списка
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    // возвращает ListIterator для итерации по элементам списка
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    // возвращает массив, содержащий все элементы списка
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    // возвращает массив, содержащий все элементы списка
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
    // возвращает Iterator для итерации по элементам списка
    public Iterator<E> iterator() {
        return null;
    }

}
