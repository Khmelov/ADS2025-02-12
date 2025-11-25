package by.it.group410901.gutseva.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    // внутренний массив для хранения элементов
    private Object[] elements = new Object[10];
    private int size = 0;

    // метод для увеличения массива при необходимости
    private void ensureCapacity(int newSize) {
        if (newSize > elements.length) {
            Object[] newArr = new Object[elements.length * 2];
            System.arraycopy(elements, 0, newArr, 0, size);
            elements = newArr;
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i=0; i<size; i++) {
            sb.append(elements[i]);
            if (i < size-1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        ensureCapacity(size+1);
        elements[size++] = e;
        return true;
    }

    @Override
    public void add(int index, E element) {
        if (index<0 || index>size) throw new IndexOutOfBoundsException();
        ensureCapacity(size+1);
        System.arraycopy(elements, index, elements, index+1, size-index);
        elements[index] = element;
        size++;
    }

    @Override
    public E remove(int index) {
        if (index<0 || index>=size) throw new IndexOutOfBoundsException();
        @SuppressWarnings("unchecked")
        E old = (E) elements[index];
        System.arraycopy(elements, index+1, elements, index, size-index-1);
        elements[--size] = null;
        return old;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexOf(o);
        if (idx>=0) {
            remove(idx);
            return true;
        }
        return false;
    }

    // заменяет элемент в указанной позиции новым элементом
    // возвращает старый элемент
    @Override
    public E set(int index, E element) {
        if (index<0 || index>=size) throw new IndexOutOfBoundsException();
        @SuppressWarnings("unchecked")
        E old = (E) elements[index];
        elements[index] = element;
        return old;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public void clear() {
        for (int i=0; i<size; i++) elements[i] = null;
        size = 0;
    }

    // возвращает индекс первого вхождения указанного элемента
    @Override
    public int indexOf(Object o) {
        for (int i=0; i<size; i++) {
            if (o==null ? elements[i]==null : o.equals(elements[i])) return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i=size-1; i>=0; i--) {
            if (o==null ? elements[i]==null : o.equals(elements[i])) return i;
        }
        return -1;
    }

    // возвращает элемент по указанному индексу
    @Override
    public E get(int index) {
        if (index<0 || index>=size) throw new IndexOutOfBoundsException();
        @SuppressWarnings("unchecked")
        E val = (E) elements[index];
        return val;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o)>=0;
    }

    // проверяет, содержатся ли все элементы коллекции c в данном списке
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) if (!contains(e)) return false;
        return true;
    }

    // добавляет все элементы коллекции c в конец списка
    @Override
    public boolean addAll(Collection<? extends E> c) {
        ensureCapacity(size+c.size());
        for (E e : c) add(e);
        return !c.isEmpty();
    }

    // вставляет все элементы коллекции c, начиная с указанной позиции
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index<0 || index>size) throw new IndexOutOfBoundsException();
        int count = c.size();
        ensureCapacity(size+count);
        System.arraycopy(elements, index, elements, index+count, size-index);
        int i=index;
        for (E e : c) elements[i++] = e;
        size += count;
        return count>0;
    }

    // удаляет все элементы списка, которые содержатся в коллекции c
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed=false;
        for (Object e : c) {
            while (remove(e)) changed=true;
        }
        return changed;
    }

    // сохраняет только те элементы списка, которые содержатся в коллекции c
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed=false;
        for (int i=0; i<size; i++) {
            if (!c.contains(elements[i])) {
                remove(i);
                i--;
                changed=true;
            }
        }
        return changed;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }
}