package by.it.group451001.russu.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {
    private Object[] elements= new Object[10];
    private int size= 0;

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E e) {
        if (size == elements.length) {
            Object[] newElements = new Object[elements.length * 2];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
        elements[size] = e;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E remove() {
        if (size == 0) throw new NoSuchElementException();
        return poll();
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) {
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
    public E poll() {
        if (size == 0) return null;
        E result = (E) elements[0];
        elements[0] = elements[size - 1];
        elements[size - 1] = null;
        size--;
        if (size > 0) {
            siftDown(0);
        }
        return result;
    }

    @Override
    public E peek() {
        return (size == 0) ? null : (E) elements[0];
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return (E) elements[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E element : c) {
            if (add(element)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        // Создаем новый массив только с элементами, которых нет в коллекции c
        Object[] newElements = new Object[elements.length];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                newElements[newSize++] = elements[i];
            }
        }

        if (newSize != size) {
            changed = true;
            elements = newElements;
            size = newSize;
            // Перестраиваем кучу
            for (int i = (size - 1) / 2; i >= 0; i--) {
                siftDown(i);
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        // Создаем новый массив только с элементами, которые есть в коллекции c
        Object[] newElements = new Object[elements.length];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                newElements[newSize++] = elements[i];
            }
        }

        if (newSize != size) {
            changed = true;
            elements = newElements;
            size = newSize;
            // Перестраиваем кучу
            for (int i = (size - 1) / 2; i >= 0; i--) {
                siftDown(i);
            }
        }
        return changed;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                return (E) elements[index++];
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = elements[i];
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    // Вспомогательные методы для кучи
    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (((Comparable<E>) elements[index]).compareTo((E) elements[parent]) >= 0) {
                break;
            }
            swap(index, parent);
            index = parent;
        }
    }

    private void siftDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size && ((Comparable<E>) elements[left]).compareTo((E) elements[smallest]) < 0) {
                smallest = left;
            }
            if (right < size && ((Comparable<E>) elements[right]).compareTo((E) elements[smallest]) < 0) {
                smallest = right;
            }
            if (smallest == index) {
                break;
            }
            swap(index, smallest);
            index = smallest;
        }
    }

    private void swap(int i, int j) {
        Object temp = elements[i];
        elements[i] = elements[j];
        elements[j] = temp;
    }

    private void removeAt(int index) {
        elements[index] = elements[size - 1];
        elements[size - 1] = null;
        size--;
        if (index < size) {
            siftDown(index);
            siftUp(index);
        }
    }
}