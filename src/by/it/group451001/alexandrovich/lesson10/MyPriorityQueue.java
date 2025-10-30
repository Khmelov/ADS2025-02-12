package by.it.group451001.alexandrovich.lesson10;

import java.util.*;

public class MyPriorityQueue<E> implements Queue<E> {
    private Comparable[] heap = new Comparable[10];

    int size = 0;
    int capacity = 10;

    void siftDown(int i) { //просеивание вверх
        int left;
        int right;
        int j;
        Comparable temp;
        while (2 * i + 1 < size) {
            left = 2 * i + 1;
            right = 2 * i + 2;
            j = left;

            if ((right < size) && (heap[right].compareTo(heap[left]) < 0)) {
                j = right;
            }
            if (!(heap[i].compareTo(heap[j]) > 0)) {
                break;
            }
            temp = heap[i];
            heap[i] = heap[j];
            heap[j] = temp;
            i = j;
        }
    }

    void siftUp(int i) { //просеивание вниз
        Comparable temp;
        while ((i != 0) && (heap[i].compareTo(heap[(i-1)/2]) < 0)){
            temp = heap[i];
            heap[i] = heap[(i-1)/2];
            heap[(i-1)/2] = temp;
            i = (i-1)/2;
        }
    }

    void insert(Comparable value) {
        if (size == capacity)
        {
            Comparable[] temp = heap;
            heap = new Comparable[capacity*2];
            System.arraycopy(temp, 0, heap, 0, capacity);
            capacity*=2;
        }
        heap[size++] = value;
        siftUp(size-1);
    }

    Comparable extractMin() { //Пока что макс
        Comparable result;
        result = heap[0];
        heap[0] = heap[size - 1];
        size--;
        siftDown(0);
        return result;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++){
            sb.append(heap[i]);
            if (i != size-1) sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
    }

    @Override
    public boolean add(E e) {
        insert((Comparable)e);
        return true;
    }

    @Override
    public E remove() {
        return (E) extractMin();
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++){
            if (heap[i].equals(o)) return true;
        }
        return false;
    }

    @Override
    public boolean offer(E e) {
        add(e);
        return true;
    }

    @Override
    public E poll() {
        return (E) extractMin();
    }

    @Override
    public E element() {
        return (E) heap[0];
    }

    @Override
    public E peek() {
        return (E) heap[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c)
        {
            if (!contains(e)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean res = false;
        for (E e : c)
        {
            if (add(e)) res = true;
        }
        return res;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (isEmpty()) return false;

        int newSize = 0;
        Comparable[] temp = new Comparable[size];

        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                temp[newSize++] = heap[i];
            }
        }
        boolean changed = newSize != size;
        size = newSize;
        heap = new Comparable[capacity];
        for (int i = 0; i < size; i++) heap[i] = temp[i];
        for (int i = size / 2; i >= 0; i--) siftDown(i);

        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (isEmpty()) return false;

        int newSize = 0;
        Comparable[] temp = new Comparable[size];

        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                temp[newSize++] = heap[i];
            }
        }
        boolean changed = newSize != size;
        size = newSize;
        heap = new Comparable[capacity];
        for (int i = 0; i < size; i++) heap[i] = temp[i];
        for (int i = size / 2; i >= 0; i--) siftDown(i);

        return changed;
    }

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
