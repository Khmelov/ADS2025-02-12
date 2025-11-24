package by.it.group451001.klevko.lesson10;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    private E[] data = (E[]) new Comparable[16];
    private int size = 0;

    // Просеивание вниз (min-heap)
    private void siftDown(int i) {
        int current = i;
        while (true) {
            int left = 2 * current + 1;
            int right = 2 * current + 2;
            int smallest = current;

            if (left < size && data[left].compareTo(data[smallest]) < 0) smallest = left;
            if (right < size && data[right].compareTo(data[smallest]) < 0) smallest = right;

            if (smallest != current) {
                E temp = data[current];
                data[current] = data[smallest];
                data[smallest] = temp;
                current = smallest;
            } else break;
        }
    }

    // Просеивание вверх
    private void siftUp(int i) {
        int current = i;
        while (current > 0) {
            int parent = (current - 1) / 2;
            if (data[current].compareTo(data[parent]) < 0) {
                E temp = data[current];
                data[current] = data[parent];
                data[parent] = temp;
                current = parent;
            } else break;
        }
    }

    // Удаление элемента по индексу
    private void removeIndex(int i) {
        if (i < 0 || i >= size) return;
        data[i] = data[--size];
        data[size] = null;
        if (i < size) {
            siftDown(i);
            siftUp(i);
        }
    }

    @Override
    public boolean add(E e) {
        if (e == null) return false;
        if (size >= data.length) {
            data = Arrays.copyOf(data, data.length * 2);
        }
        data[size++] = e;
        siftUp(size - 1);
        return true;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E ans = data[0];
        removeIndex(0);
        return ans;
    }

    @Override
    public E remove() {
        return poll();
    }

    @Override
    public E peek() {
        if (size == 0) return null;
        return data[0];
    }

    @Override
    public E element() {
        return peek();
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
    public void clear() {
        Arrays.fill(data, 0, size, null);
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (data[i].equals(o)) return true;
        }
        return false;
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
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (data[i].equals(o)) {
                removeIndex(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            changed |= add(e);
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        int j = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(data[i])) {
                data[j++] = data[i];
            } else {
                changed = true;
            }
        }
        for (int k = j; k < size; k++) data[k] = null;
        size = j;
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
        return changed;
    }


    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        int j = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(data[i])) {
                data[j++] = data[i];
            } else {
                changed = true;
            }
        }
        for (int k = j; k < size; k++) data[k] = null;
        size = j;
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
        return changed;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size - 1; i++) {
            sb.append(data[i]).append(", ");
        }
        sb.append(data[size - 1]).append("]");
        return sb.toString();
    }
}
