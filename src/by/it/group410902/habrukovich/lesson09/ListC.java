package by.it.group410902.habrukovich.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {
    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    private E[] el;
    private int size;
    public ListC() {
        el = (E[]) new Object[1]; //начальная емкость 1 эелемент
        size = 0;
    }
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder("[");
        for (int i=0; i<size; i++) {
            sb.append(el[i]);
            if (i<size-1) sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        if (size==el.length) {
            int newsize=el.length*2;
            E[] newel=(E[]) new Object[newsize];
            System.arraycopy(el, 0, newel, 0, size);
            el=newel;
        }
        el[size]=e;
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        E removed=el[index];
        for (int i=index; i<size-1; i++){
            el[i]=el[i+1];  // сдвигаем элементы влево
        }
        el[size-1]=null;
        size--;
        return removed;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (size==el.length) {
            int newsize=el.length*2;
            E[] newel=(E[]) new Object[newsize];
            System.arraycopy(el, 0, newel, 0, size);
            el=newel;
        }
        System.arraycopy(el, index, el, index + 1, size - index);// сдвиг вправо
        el[index]=element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        int index=indexOf(o);
        if (index>=0) {
            remove(index);
            return true;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0  || index >= size) throw new IndexOutOfBoundsException();
        E ele=el[index];
        el[index]=element;
        return ele;
    }


    @Override
    public boolean isEmpty() {
        return size==0;
    }


    @Override
    public void clear() {
        size=0;// сбрасываем счетчик
        E[] newel=(E[]) new Object[1]; //создаем новый
        el=newel; //заменяем
    }

    @Override
    public int indexOf(Object o) {
        for (int i=0; i<size; i++){
            if (o.equals(el[i])) return i; //сравнение
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return el[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o)>=0;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i=size-1; i>=0; i--){ //с конца
            if (o.equals(el[i])) return i;
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element: c) {
            if (!(contains(element))) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean f=false;
        for (E e: c){
            add(e); //добавляем каждый элемент
            f=true;
        }
        return f;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        boolean f = false;
        for (E e : c) {
            add(index++, e);
            f = true;
        }
        return f;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean f = false;
        for (int i = 0; i < size; ) {
            if (c.contains(el[i])) {
                remove(i); //удалить если есть в коллекции
                f = true;
            } else {
                i++;
            }
        }
        return f;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean f = false;
        for (int i = 0; i < size; ) {
            if (!c.contains(el[i])) { //если нет в коллекции
                remove(i); //удаляем
                f = true;
            } else {
                i++;
            }
        }
        return f;
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
