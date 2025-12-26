package by.it.group451003.rashchenya.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private E[] elements = (E[]) new Object[10]; // основной массив (начальный размер 10)
    private int size = 0; //текущее количество элементов

    private void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            int newCap = elements.length * 3 / 2 + 1;  // Шаг 1: +50% + 1
            if (newCap < capacity) newCap = capacity;  // Шаг 2: проверка достаточности
            E[] newArr = (E[]) new Object[newCap];     // Шаг 3: новый массив
            System.arraycopy(elements, 0, newArr, 0, size); // Шаг 4: копирование
            elements = newArr;                         // Шаг 5: замена ссылки
        }
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы            ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i=0;i<size;i++){
            sb.append(elements[i]);
            if (i < size-1) sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1);  // Гарантируем, что место есть
        elements[size++] = e;      // Добавляем и увеличиваем счетчик
        return true;               // Всегда true для совместимости
    }

    @Override
    public E remove(int index) {
        if (index<0 || index>=size) throw new IndexOutOfBoundsException();
        E old = elements[index];
        int numMoved = size - index - 1;
        if (numMoved > 0){
            System.arraycopy(elements, index+1, elements, index, numMoved);
        }
        elements[--size] = null;
        return old;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        ensureCapacity(size + 1);                          // Шаг 1: место есть?
        int numMoved = size - index;                       // Шаг 2: сколько сдвигать?
        if (numMoved > 0) {
            // Шаг 3: сдвигаем "хвост" вправо
            System.arraycopy(elements, index, elements, index + 1, numMoved);
        }
        elements[index] = element;                         // Шаг 4: вставляем элемент
        size++;                                            // Шаг 5: обновляем размер
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexOf(o);
        if (idx == -1) return false;
        remove(idx);
        return true;
    }

    @Override
    public E set(int index, E element) {
        if (index<0 || index>=size) throw new IndexOutOfBoundsException();
        E old = elements[index];
        elements[index] = element;
        return old;
    }


    @Override
    public boolean isEmpty() {
        return size==0;
    }


    @Override
    public void clear() {
        for (int i=0;i<size;i++) elements[i]=null;
        size=0;
    }

    @Override
    public int indexOf(Object o) {
        if (o==null){
            for (int i=0;i<size;i++){
                if (elements[i]==null) return i;
            }
        } else {
            for (int i=0;i<size;i++){
                if (o.equals(elements[i])) return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if (index<0 || index>=size) throw new IndexOutOfBoundsException();
        return elements[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o)>=0;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o==null){
            for (int i=size-1;i>=0;i--){
                if (elements[i]==null) return i;
            }
        } else {
            for (int i=size-1;i>=0;i--){
                if (o.equals(elements[i])) return i;
            }
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c==null) return false;
        for (Object x : c) {
            if (!contains(x)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c==null || c.isEmpty()) return false;
        ensureCapacity(size + c.size());
        boolean modified = false;
        for (E x : c) {
            elements[size++] = x;
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index<0 || index>size) throw new IndexOutOfBoundsException();
        if (c==null || c.isEmpty()) return false;
        int addCount = c.size();
        ensureCapacity(size + addCount);
        int numMoved = size - index;
        if (numMoved > 0){
            System.arraycopy(elements, index, elements, index + addCount, numMoved);
        }
        int i = index;
        for (E x : c){
            elements[i++] = x;
        }
        size += addCount;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c==null || c.isEmpty()) return false;
        boolean modified = false;
        int newSize = 0;
        for (int i=0;i<size;i++){
            if (!c.contains(elements[i])){
                elements[newSize++] = elements[i];
            } else {
                modified = true;
            }
        }
        for (int i=newSize;i<size;i++) elements[i]=null;
        size = newSize;
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c==null) {
            if (size==0) return false;
            clear();
            return true;
        }
        boolean modified = false;
        int newSize = 0;
        for (int i=0;i<size;i++){
            if (c.contains(elements[i])){
                elements[newSize++] = elements[i];
            } else {
                modified = true;
            }
        }
        for (int i=newSize;i<size;i++) elements[i]=null;
        size = newSize;
        return modified;
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