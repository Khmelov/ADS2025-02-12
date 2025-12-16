package by.it.group451002.koltsov.lesson10;

import java.util.*;

public class MyPriorityQueue<E> implements Queue<E> {
    E[] elems = (E[]) new Object[0];
    int size = 0;
    //    HashMap<E, Integer> map = new HashMap<>();

    private  <T extends Comparable<T>> int cmp(T o1, T o2) {
        return o1.compareTo(o2);
    }

    void grow(int elemNum) {
        if (size + elemNum >= elems.length) {
            E[] newElems = (E[]) new Object[(size() + elemNum) * 2];
            System.arraycopy(elems, 0, newElems, 0, size);
            elems = newElems;
        }
    }

    // Метод для просеивания элемента вниз
    void siftDown(int Index) {
        if (Index * 2 + 1 < size) {
            if (Index * 2 + 2 < size) {
                // Значит у узла есть оба потомка
                // Получаем индекс меньшего потомка
                int minElemIndex = ((Comparable<E>) elems[Index * 2 + 1]).compareTo(elems[Index * 2 + 2]) <= 0 ? Index * 2 + 1 : Index * 2 + 2;
                // Сравниваем этого потомка с предком
                if (((Comparable<E>) elems[Index]).compareTo(elems[minElemIndex]) >= 0) {
                    E temp = elems[Index];
                    elems[Index] = elems[minElemIndex];
                    elems[minElemIndex] = temp;

                    siftDown(minElemIndex);
                }
            } else {
                // Значит у узла есть только левый потомок
                if (((Comparable<E>) elems[Index]).compareTo(elems[Index * 2 + 1]) >= 0) {
                    E temp = elems[Index];
                    elems[Index] = elems[Index * 2 + 1];
                    elems[Index * 2 + 1] = temp;
                }
            }
        }
    }

    void siftUp(int Index) {
        if (Index > 0) {
            if (((Comparable<E>) elems[(Index - 1) / 2]).compareTo(elems[Index]) >= 0) {
                E temp = elems[Index];
                elems[Index] = elems[(Index - 1) / 2];
                elems[((Index - 1) / 2)] = temp;
                siftUp((Index - 1) / 2);
            }
        }
    }

    // Методы, которые необходимо реализовать
    @Override
    public String toString() {
        StringBuilder resStr = new StringBuilder("[");
        for (int i = 0; i < size; i++)
        {
            resStr.append(elems[i]);
            if (i + 1 < size)
                resStr.append(", ");
        }
        return resStr + "]";
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        elems = (E[]) new Object[0];
        size = 0;
    }

    @Override
    public boolean add(E e) {
        grow(1);
        elems[size++] = e;
        siftUp(size - 1);
        return true;
    }

    @Override
    public E remove() {
        if (size == 0)
            throw new IndexOutOfBoundsException();
        return poll();
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E poll() {
        if (size == 0)
            return null;
        E tempVal = elems[0];

        elems[0] = elems[--size];
        siftDown(0);
        return tempVal;
    }

    @Override
    public E element() {
        if (size == 0)
            throw new NoSuchElementException();
        return elems[0];
    }

    @Override
    public E peek() {
        if (size == 0)
            return null;
        return elems[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object elem : c)
            if (!contains(elem))
                return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        grow(c.size());
        boolean res = false;
        for (E element : c) {
            add(element);
            res = true;
        }
        return res;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        List<Object> newColl = new ArrayList<Object>();
        int tempSize = size;
        for (int i = 0; i < size; i++)
        {
            if (!c.contains(elems[i]))
                newColl.add(elems[i]);
        }

        elems = (E[]) new Object[newColl.size()];
        size = newColl.size();
        for (int i = 0; i < newColl.size(); i++)
            elems[i] = (E)newColl.get(i);
        heapify();
        return tempSize != size;
    }

    public void heapify()
    {
        for (int i = size - 1; i >= 0; i--)
            siftDown(i);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        List<Object> newColl = new ArrayList<Object>();
        int tempSize = size;
        for (int i = 0; i < size; i++)
        {
            if (c.contains(elems[i]))
                newColl.add(elems[i]);
        }

        elems = (E[]) new Object[newColl.size()];
        size = newColl.size();
        for (int i = 0; i < newColl.size(); i++)
            elems[i] = (E)newColl.get(i);
        heapify();
        return tempSize != size;
    }

    // Методы, которые можно не реализовывать

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index == -1)
            return false;
        elems[index] = elems[--size];
        siftDown(index);
        return true;
    }

    int indexOf(Object o) {
        for (int i = 0; i < size; i++)
            if (elems[i].equals(o))
                return i;
        return -1;
    }
}