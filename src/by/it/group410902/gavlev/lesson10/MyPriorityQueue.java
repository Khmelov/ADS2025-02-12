package by.it.group410902.gavlev.lesson10;

import java.lang.reflect.Array;
import java.util.*;

public class MyPriorityQueue<E> implements Queue<E> {
    private E[] data;
    private int size = 0;
    private Comparator<? super E> comparator;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(int capacity, Comparator<? super E> comp) {
        data = (E[]) new Object[capacity];
        comparator = comp;
    }

    public MyPriorityQueue(int capacity) {
        this(capacity, null);
    }

    public MyPriorityQueue() {
        this(8, null);
    }

    @SuppressWarnings("unchecked")
    private void updateCapacity() {
        E[] newData = (E[]) new Object[data.length * 3 / 2 + 1];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        }
        return ((Comparable<? super E>) a).compareTo(b);
    }

    private void swap(int i, int j) {
        E temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    private void siftUp(int index) {
        if (index == 0) return;
        int indexParent = (index - 1) / 2;
        while (index > 0 && compare(data[index], data[indexParent]) < 0) {
            swap(index, indexParent);
            index = indexParent;
            indexParent = (index - 1) / 2;
        }
    }

    private void siftDown(int index) {
        while (true) {
            int left = index * 2 + 1;
            if (left >= size) break;

            int right = left + 1;
            int smallest = left;

            if (right < size && compare(data[right], data[left]) < 0) {
                smallest = right;
            }

            if (compare(data[smallest], data[index]) < 0) {
                swap(index, smallest);
                index = smallest;
            }
            else {
                break;
            }
        }
    }


    @Override
    public boolean add(E el) {
        if (size == data.length) updateCapacity();
        data[size] = el;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public boolean offer(E el) {
        return add(el);
    }

    @Override
    public E remove() {
        if (size == 0) throw new NoSuchElementException();
        E val = data[0];
        size--;
        if (size > 0) {
            data[0] = data[size];
            data[size] = null;
            siftDown(0);
        }
        else {
            data[0] = null;
        }
        return val;
    }

    @Override
    public E poll() {
        try {
            return remove();
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return data[0];
    }

    @Override
    public E peek() {
        try {
            return element();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, data[i])) {
                return true;
            }
        }
        return false;
    }

    public int findIndex(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, data[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        return data;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        return (T[]) data;
    }

    @Override
    public boolean remove(Object obj) {
        int index = findIndex(obj);
        if (index == -1) return false;

        E last = data[size - 1];
        data[size - 1] = null;
        size--;

        if (index < size) {
            data[index] = last;
            siftDown(index);
            siftUp(index);
        }
        return true;
    }


    @Override
    public boolean containsAll(Collection<?> other) {
        for (Object el : other) {
            if (!this.contains(el)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> other) {
        if (other.size() == 0) return false;
        for (E el : other) {
            this.add(el);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> other) {
        Set<?> remove = (other instanceof Set) ? (Set<?>) other : new HashSet<>(other);

        boolean modified = false;
        int write = 0;
        for (int read = 0; read < size; read++) {
            E el = data[read];
            if (!remove.contains(el)) {
                data[write++] = el;
            } else {
                modified = true;
            }
        }
        for (int i = write; i < size; i++) {
            data[i] = null;
        }
        size = write;

        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }
        return modified;
    }



    @Override
    public boolean retainAll(Collection<?> other) {
        Set<?> keep = (other instanceof Set) ? (Set<?>) other : new HashSet<>(other);

        boolean modified = false;
        int write = 0;
        for (int read = 0; read < size; read++) {
            E el = data[read];
            if (keep.contains(el)) {
                data[write++] = el;
            } else {
                modified = true;
            }
        }
        for (int i = write; i < size; i++) {
            data[i] = null;
        }
        size = write;

        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }
        return modified;
    }

    @Override
    public void clear() {
        Arrays.fill(data, null);
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

}
