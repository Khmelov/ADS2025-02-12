package by.it.group451003.rashchenya.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Queue;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    final int defaultSize = 8;
    E[] _elements;
    int size;

    public MyPriorityQueue() {
        _elements = (E[]) new Comparable[defaultSize];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(_elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    void heapifyUp(int index) {
        int parent = (index - 1) / 2;

        if (parent >= 0 && _elements[index].compareTo(_elements[parent]) < 0) {
            E temp = _elements[index];
            _elements[index] = _elements[parent];
            _elements[parent] = temp;
            heapifyUp(parent);
        }
    }

    void heapifyDown(int index) {
        int left = 2 * index + 1;
        int right = 2 * index + 2;
        int largest = left;

        if (right < size && _elements[right].compareTo(_elements[largest]) < 0)
            largest = right;

        if (largest < size && _elements[largest].compareTo(_elements[index]) < 0) {
            E temp = _elements[index];
            _elements[index] = _elements[largest];
            _elements[largest] = temp;
            heapifyDown(largest);
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
            if (_elements[i].equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if (size == _elements.length) {
            resize();
        }
        _elements[size++] = e;
        heapifyUp(size - 1);
        return true;
    }

    void resize() {
        int newCapacity = _elements.length * 2;
        E[] newItems = (E[]) new Comparable[newCapacity];
        System.arraycopy(_elements, 0, newItems, 0, size);
        _elements = newItems;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E item : c) {
            if (add(item)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object item : c) {
            if (remove(item)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(_elements[i])) {
                remove(_elements[i]);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, _elements[i])) {
                _elements[i] = _elements[--size];
                heapifyDown(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E remove() {
        if (isEmpty()) {
            throw new IllegalArgumentException("PriorityQueue is empty");
        }
        E root = _elements[0];
        _elements[0] = _elements[--size];
        heapifyDown(0);
        return root;
    }

    @Override
    public E poll() {
        if (isEmpty()) {
            return null;
        }
        return remove();
    }

    @Override
    public E element() {
        if (size == 0) {
            return null;
        }
        return _elements[0];
    }

    @Override
    public E peek() {
        return element();
    }

    @Override
    public void clear() {
        size = 0;
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
}
