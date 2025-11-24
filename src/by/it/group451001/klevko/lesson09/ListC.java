package by.it.group451001.klevko.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {
    private final int startSize = 10;
    private Object[] data = new Object[startSize];
    private int capacity = startSize, size = 0;

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        if (this.isEmpty()) return "[]";
        StringBuilder ans = new StringBuilder("[");
        for (int i = 0; i < size-1; ++i){
            ans.append(data[i]).append(", ");
        }
        ans.append(data[size-1]) .append("]");
        return ans.toString();
    }

    @Override
    public boolean add(E e) {
        if (size >= capacity) {
            capacity *= 2;
            Object newData[] = new Object[capacity];
            System.arraycopy(data, 0, newData, 0, size);
            data = newData;
        }
        data[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        E ans = null;
        if (index >= 0 && index < size) {
            ans = (E) data[index];
            for (int i = index; i < size-1; i++) {
                data[i] = data[i+1];
            }
            --size;
        }
        return ans;
    }

    @Override
    public int size() { return size; }

    @Override
    public void add(int index, E element) {
        if (size >= capacity) {
            capacity *= 2;
            Object newData[] = new Object[capacity];
            System.arraycopy(data, 0, newData, 0, size);
            data = newData;
        }
        ++size;
        if (index >= 0 && index < size) {
            for (int i = size-1; i > index; i--) {
                data[i] = data[i-1];
            }
            data[index] = element;
        }
    }

    @Override
    public boolean remove(Object o) {
        boolean ans = this.contains(o);
        this.remove(this.indexOf(o));
        return ans;
    }

    @Override
    public E set(int index, E element) {
        E temp = null;
        if (index >= 0 && index < size) {
            temp = (E) data[index];
            data[index] = element;
        }
        return temp;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; ++i){
            if (data[i].equals(o)) return i;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if (index >= 0 && index < size) {
            return (E) data[index];
        }
        return null;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; ++i){
            if (data[i].equals(o)) return true;
        }
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size-1; i >= 0 ; --i){
            if (data[i].equals(o)) return i;
        }
        return -1;
    }





    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element: c) {
            if (!this.contains(element)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean ans = false;
        for (E element: c) {
            ans = true;
            this.add(element);
        }
        return ans;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean ans = false;
        if (index >= 0 && index < size) {
            for (E element: c) {
                ans = true;
                this.add(index++, element);
            }
        }
        return ans;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean ans = false;
        for (Object element: c) {
            while (this.remove(element)) ans = true;
        }
        return ans;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean ans = false;
        for (int i = 0; i < size; ++i){
            if (!c.contains(data[i])) {
                ans = true;
                this.remove(i--);
            }
        }
        return ans;
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
