package by.it.group451003.mihlin.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

@SuppressWarnings("unchecked")
public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    private E[] data;
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 8;

    public MyPriorityQueue() {
        data = (E[]) new Comparable[DEFAULT_CAPACITY];
    }

    private void ensureCapacity() {
        if (size < data.length) return;
        E[] newArr = (E[]) new Comparable[data.length * 2];
        for (int i = 0; i < size; i++) newArr[i] = data[i];
        data = newArr;
    }

    // ====== Вспомогательные методы для кучи (min-heap) ======

    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (data[index].compareTo(data[parent]) >= 0) break;
            E tmp = data[index];
            data[index] = data[parent];
            data[parent] = tmp;
            index = parent;
        }
    }

    private void siftDown(int index) {
        while (true) {
            int left = index * 2 + 1;
            int right = index * 2 + 2;
            int smallest = index;

            if (left < size && data[left].compareTo(data[smallest]) < 0) smallest = left;
            if (right < size && data[right].compareTo(data[smallest]) < 0) smallest = right;

            if (smallest == index) break;

            E tmp = data[index];
            data[index] = data[smallest];
            data[smallest] = tmp;
            index = smallest;
        }
    }

    // ====== Обязательные методы ======

    @Override
    public boolean add(E e) {
        ensureCapacity();
        data[size] = e;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E remove() {
        E e = poll();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E result = data[0];
        data[0] = data[size - 1];
        data[size - 1] = null;
        size--;
        if (size > 0) siftDown(0);
        return result;
    }

    @Override
    public E peek() {
        return size == 0 ? null : data[0];
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return data[0];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) data[i] = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && data[i] == null) || (o != null && o.equals(data[i]))) return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        for (E e : c) {
            add(e);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        int writeIndex = 0;

        for (int readIndex = 0; readIndex < size; readIndex++) {
            if (!c.contains(data[readIndex])) {
                data[writeIndex++] = data[readIndex];
            } else {
                modified = true;
            }
        }

        for (int i = writeIndex; i < size; i++) data[i] = null;
        size = writeIndex;

        for (int i = (size / 2) - 1; i >= 0; i--) siftDown(i);

        return modified;
    }


    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        int writeIndex = 0;
        for (int readIndex = 0; readIndex < size; readIndex++) {
            if (c.contains(data[readIndex])) {
                data[writeIndex++] = data[readIndex];
            } else {
                modified = true;
            }
        }
        for (int i = writeIndex; i < size; i++) data[i] = null;
        size = writeIndex;

        for (int i = (size / 2) - 1; i >= 0; i--) siftDown(i);

        return modified;
    }




    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // ===== Заглушки для Queue =====

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && data[i] == null) || (o != null && o.equals(data[i]))) {
                data[i] = data[size - 1];
                data[size - 1] = null;
                size--;
                siftDown(i);
                return true;
            }
        }
        return false;
    }
}
