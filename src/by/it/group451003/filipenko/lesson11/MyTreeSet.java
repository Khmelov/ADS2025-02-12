package by.it.group451003.filipenko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E extends Comparable<E>> implements Set<E> {

    private E[] heap;
    private int size = 0;
    private static int capacity = 16;

    void heapify(int size, int i)
    {
        int max = i;
        int left = 2*i + 1;
        int right = 2*i + 2;

        if (left < size && heap[left].compareTo(heap[max]) > 0)
            max = left;

        if (right < size && heap[right].compareTo(heap[max]) > 0)
            max = right;

        if (max != i) {
            E element = heap[i];
            heap[i] = heap[max];
            heap[max] = element;

            heapify(size, max);
        }
    }

    void heapsort() {

        for (int i = (size / 2) - 1; i >= 0; i--)
            heapify(size, i);

        for (int i = size - 1; i >= 0; i--) {
            E element = heap[0];
            heap[0] = heap[i];
            heap[i] = element;

            heapify(i, 0);
        }
    }

    MyTreeSet(int capacity) {
        heap = (E[]) new Comparable[capacity];
        this.capacity = capacity;
    }

    MyTreeSet() {
        this(capacity);
    }

    public String toString() {
        StringBuilder strbldr = new StringBuilder();
        strbldr.append("[");
        for (int i = 0; i < size; i++)
            strbldr.append(heap[i] + ", ");

        int sbLength = strbldr.length();
        if (sbLength > 1)
            strbldr.delete(sbLength - 2, sbLength);

        strbldr.append("]");

        return strbldr.toString();
    }

    @Override
    public int size () {
        return size;
    }

    @Override
    public void clear () {
        capacity = 16;
        size = 0;
        heap = (E[]) new Comparable[capacity];
    }

    @Override
    public boolean isEmpty () {
        return size() == 0;
    }

    @Override
    public boolean add (E e) {
        if (e == null || contains(e))
            return false;

        size++;
        if (size >= capacity) {
            E[] tempHeap = heap;
            heap = (E[]) new Comparable[capacity *= 2];
            for (int i = 0; i < size - 1; i++)
                heap[i] = tempHeap[i];
        }
        heap[size - 1] = e;
        if (size != 1)
            heapsort();


        return true;
    }

    @Override
    public boolean remove (Object o){
        if (o == null)
            return false;

        for (int i = 0; i < size; i++)
            if (heap[i].equals(o)) {
                for (int j = i; j < size - 1; j++)
                    heap[j] = heap[j+1];
                size--;
                return true;
            }

        return false;
    }

    @Override
    public boolean contains (Object o){
        if (o == null)
            return false;

        for (int i = 0; i < size; i++)
            if (heap[i].equals(o))
                return true;

        return false;
    }

    @Override
    public boolean containsAll (Collection < ? > c){
        for (Object o : c)
            if (!contains(o))
                return false;
        return true;
    }

    @Override
    public boolean addAll (Collection < ? extends E > c){
        if (c.size() == 0)
            return false;


        for (Object o : c) {
            if (o == null)
                return false;
            add((E) o);
        }
        return true;
    }

    @Override
    public boolean retainAll (Collection < ? > c){
        Boolean changed = false;
        int i = 0;
        while(i < size) {
            if (!c.contains(heap[i])) {
                remove(heap[i]);
                changed = true;
                continue;
            }
            i++;
        }
        return changed;
    }

    @Override
    public boolean removeAll (Collection < ? > c) {
        if (c.size() == 0)
            return false;
        for (Object o : c) {
            if (o == null)
                return false;
            remove(o);
        }
        return true;
    }

    ///////////////////
    //// необязательные
    ///////////////////

    @Override
    public Iterator<E> iterator () {
        return null;
    }

    @Override
    public Object[] toArray () {
        return new Object[0];
    }

    @Override
    public <T > T[]toArray(T[]a){
        return null;
    }
}
