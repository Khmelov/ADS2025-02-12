package by.it.group451003.shepetuyk.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    private int size = 0;
    private static int capacity = 16;
    private E[] heap;

    public void heapifyUp(int child) {
        int parent = (child - 1) / 2;

        while (child > 0 && heap[parent].compareTo(heap[child]) > 0) {
            E tempElement = heap[parent];
            heap[parent] = heap[child];
            heap[child] = tempElement;

            child = parent;
            parent = (child - 1) / 2;
        }
    }

    public void heapifyDown(int parent) {
        int leftChild = parent * 2 + 1;
        int rightChild = parent * 2 + 2;
        int min;

        while (leftChild < size) {
            if (heap[parent].compareTo(heap[leftChild]) > 0 || heap[parent].compareTo(heap[rightChild]) > 0 && rightChild < size) {
                min = leftChild;
                if (rightChild < size && heap[leftChild].compareTo(heap[rightChild]) > 0)
                    min = rightChild;

                E tempElement = heap[parent];
                heap[parent] = heap[min];
                heap[min] = tempElement;

                parent = min;
                leftChild = parent * 2 + 1;
                rightChild = leftChild + 1;
            }
            else break;
        }
    }


    public MyPriorityQueue(int size) {
        heap = (E[]) new Comparable[capacity += size];
    }

    public MyPriorityQueue() {
        this(capacity);
    }

    ////////////////
    // обязательные
    ////////////////

    public String toString() {
        StringBuilder strbldr = new StringBuilder();
        strbldr.append("[");

        for(int i = 0; i < size; i++)
            strbldr.append(heap[i] + ", ");

        if (strbldr.length() > 1)
            strbldr.delete(strbldr.length() - 2, strbldr.length());
        strbldr.append("]");

        return strbldr.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        capacity = 16;
        heap = (E[]) new Comparable[capacity];
        size = 0;
    }

    @Override
    public boolean add(E element) {
        size++;

        if (size == capacity) {
            E[] tempHeap = (E[]) new Comparable[capacity += 16];
            for (int i = 0; i < size - 1; i++)
                tempHeap[i] = heap[i];

            heap = tempHeap;
        }

        heap[size - 1] = element;
        heapifyUp(size - 1);

        return true;
    }

    @Override
    public E remove() {
        return poll();
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++)
            if (heap[i].equals(o))
                return true;

        return false;
    }

    @Override
    public boolean offer(E element) {
        return add(element);
    }

    @Override
    public E poll() {
        if (size == 0)
            return null;

        E element = heap[0];
        heap[0] = heap[size - 1];
        size--;
        heapifyDown(0);

        return element;
    }

    @Override
    public E peek() {
        if (size == 0)
            return null;
        return heap[0];
    }

    @Override
    public E element() {
        if (size == 0)
            throw new NullPointerException();
        return heap[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c)
            if (!contains(element))
                return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        int tempSize = size;

        for (E element : c)
            add(element);

        return tempSize != size;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int i = 0, j = 0, tempSize = size;

        for (; i < tempSize; i++) {
            if (!c.contains(heap[i]))
                heap[j++] = heap[i];
            else size--;
        }

        if (tempSize == size)
            return false;

        for (i = size / 2; i >= 0; i--)
            heapifyDown(i);

        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int i = 0, j = 0, tempSize = size;

        for (; i < tempSize; i++) {
            if (c.contains(heap[i]))
                heap[j++] = heap[i];
            else size--;
        }

        if (tempSize == size)
            return false;

        for (i = size / 2; i >= 0; i--)
            heapifyDown(i);

        return true;
    }

    /////////////////
    // необязательные
    /////////////////

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
        return false;
    }
}
