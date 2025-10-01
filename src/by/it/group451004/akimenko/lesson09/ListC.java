package lesson09;

import java.util.*;

public class ListC<E> implements List<E> {
    private E[] elements = (E[])(new Object[10]);
    private int size = 0;
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("[");
        ArrayListIterator it = new ArrayListIterator();
        if (it.hasNext())
            res.append(it.next().toString());
        while (it.hasNext()) {
            res.append(", ");
            res.append(it.next().toString());
        }
        res.append("]");
        return res.toString();
    }
    private void grow()
    {
        E[] newElements = (E[])(new Object[elements.length * 2]);
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }
    @Override
    public boolean add(E e) {
        if (size == elements.length)
            grow();
        elements[size++] = e;
        return true;
    }
    @Override
    public E remove(int index) {
        E removed = elements[index];
        System.arraycopy(elements, index +1, elements, index, size - index -1);
        size--;
        return removed;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (size == elements.length)
            grow();
        System.arraycopy(elements, index, elements, index +1, size - index);
        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        boolean removed = false;
        for (int i = 0; i < size; i++) {
            if(Objects.equals(elements[i], o)){
                removed = true;
                remove(i);
                break;
            }
        }
        return removed;
    }

    @Override
    public E set(int index, E element) {
        E old = elements[index];
        elements[index] = element;
        return old;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        for(int i = 0; i < size; i++)
            elements[i] = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        for(int i = 0; i < size; i++)
            if(Objects.equals(elements[i], o))
                return i;
        return -1;
    }

    @Override
    public E get(int index) {
        return elements[index];
    }

    @Override
    public boolean contains(Object o) {
        for(int i = 0; i < size; i++)
            if(Objects.equals(elements[i], o))
                return true;
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        for(int i = size -1; i >= 0; i--)
            if(Objects.equals(elements[i], o))
                return i;
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        boolean res = true;
        for (Object o : c) {
            res = contains(o);
            if(!res)
                break;
        }
        return res;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        E[] newVals = (E[])(c.toArray());
        while(size + c.size() > elements.length)
            grow();
        System.arraycopy(newVals, 0, elements, size, c.size());
        size += c.size();
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        E[] newVals = (E[])(c.toArray());
        while(size + c.size() > elements.length)
            grow();
        System.arraycopy(elements, index, elements, index + c.size(), size - index);
        System.arraycopy(newVals,0, elements, index, c.size());
        size += c.size();
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean res = false;
        for (Object o : c) {
            while(remove(o))
                res = true;
        }
        return res;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean res = false;
        var it = listIterator();
        while (it.hasNext()) {
            if(!c.contains(it.next())) {
                it.remove();
                res = true;
            }
        }
        return res;
    }

//опциональные
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
        return new ArrayListIterator();
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
    class ArrayListIterator implements ListIterator<E> {
        int cursor = 0;
        int lastRet = -1;
        @Override
        public boolean hasNext() {
            return !(cursor > size - 1);
        }
        @Override
        public E next() {
            lastRet = cursor;
            return elements[cursor++];
        }

        @Override
        public boolean hasPrevious() {
            return !(cursor <= 0);
        }

        @Override
        public E previous() {
            lastRet = cursor - 1;
            return elements[--cursor];
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void remove() {
            ListC.this.remove(lastRet);
            cursor = lastRet;
        }

        @Override
        public void set(E e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(E e) {
            throw new UnsupportedOperationException();
        }
    }
    @Override
    public Iterator<E> iterator() {
        return new ArrayListIterator();
    }

}
