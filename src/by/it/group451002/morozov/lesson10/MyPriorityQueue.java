package by.it.group451002.morozov.lesson10;

import java.util.*;

public class MyPriorityQueue<E> implements Queue<E> {
    E[] elems = (E[]) new Object[0];
    int size = 0;
    
    int cmpAtIndex(int ind1, int ind2) {
    	return ((Comparable<E>) elems[ind1]).compareTo(elems[ind2]);
    }

    void grow(int elemsAmount) {
        if (size + elemsAmount >= elems.length) {
            E[] newElems = (E[]) new Object[(size() + elemsAmount) * 2];
            System.arraycopy(elems, 0, newElems, 0, size);
            elems = newElems;
        }
    }

    // Просеивание вниз
    void siftDown(int ind) {
        if (ind * 2 + 1 < size) {
            if (ind * 2 + 2 < size) {
            	int minElemIndex = 0;
            	if (cmpAtIndex(ind * 2 + 1, ind * 2 + 2) <= 0)
            		minElemIndex = ind * 2 + 1;
            	else
            		minElemIndex = ind * 2 + 2;
            	if (cmpAtIndex(ind, minElemIndex) >= 0) {
                    E temp = elems[ind];
                    elems[ind] = elems[minElemIndex];
                    elems[minElemIndex] = temp;

                    siftDown(minElemIndex);
                }
            } else {
                if (cmpAtIndex(ind, ind * 2 + 1) >= 0) {
                    E temp = elems[ind];
                    elems[ind] = elems[ind * 2 + 1];
                    elems[ind * 2 + 1] = temp;
                }
            }
        }
    }

    // Просеивание вверх
    void siftUp(int ind) {
        if (ind > 0) {
            if (cmpAtIndex((ind - 1) / 2, ind) >= 0) {
                E temp = elems[ind];
                elems[ind] = elems[(ind - 1) / 2];
                elems[((ind - 1) / 2)] = temp;
                
                siftUp((ind - 1) / 2);
            }
        }
    }
    
    public void heapify() {
        for (int i = size - 1; i >= 0; i--)
            siftDown(i);
    }
    
    int indexOf(Object o) {
        for (int i = 0; i < size; i++)
            if (elems[i].equals(o))
                return i;
        return -1;
    }

    @Override
    public String toString() {
        StringBuilder resStr = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            resStr.append(elems[i]);
            if (i + 1 < size)
                resStr.append(", ");
        }
        resStr.append("]");
        return resStr.toString();
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
        if (size == 0) throw new IndexOutOfBoundsException();
        return poll();
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public boolean offer(E element) {
        return add(element);
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
        for (int i = 0; i < size; i++) {
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

    @Override
    public boolean retainAll(Collection<?> c) {
        List<Object> newColl = new ArrayList<Object>();
        int tempSize = size;
        for (int i = 0; i < size; i++) {
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

    // Необязательные к реализации методы

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
    
    @Override
    public Iterator<E> iterator() {
        return null;
    }
}