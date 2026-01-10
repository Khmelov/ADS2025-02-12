package by.it.group410901.bukshta.lesson10;


import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
//Массив, организованный как бинарная куча, где для каждого
// узла выполняется свойство кучи (родитель меньше или равен детям).
@SuppressWarnings("unchecked")
public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    private E[] heap;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public MyPriorityQueue() {
        heap = (E[]) new Comparable[DEFAULT_CAPACITY];
        size = 0;
    }

    private void ensureCapacity() {
        if (size >= heap.length) {
            E[] newHeap = (E[]) new Comparable[heap.length * 2];
            for (int i = 0; i < heap.length; i++) {
                newHeap[i] = heap[i];
            }
            heap = newHeap;
        }
    }
//Восстанавливает свойство кучи, поднимая элемент с индекса index вверх, если он меньше своего родителя.
    private void heapifyUp(int index) {
        E temp = heap[index];
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap[parent].compareTo(temp) <= 0) break;
            heap[index] = heap[parent];
            index = parent;
        }
        heap[index] = temp;
    }
//Восстанавливает свойство кучи, опуская элемент с индекса index вниз, если он больше одного из своих дочерних узлов.
    private void heapifyDown(int index) {
        E temp = heap[index];
        int child;
        while ((child = 2 * index + 1) < size) {
            if (child + 1 < size && heap[child + 1].compareTo(heap[child]) < 0) {
                child++;
            }
            if (temp.compareTo(heap[child]) <= 0) break;
            heap[index] = heap[child];
            index = child;
        }
        heap[index] = temp;
    }

    // Основные методы Queue
    @Override
    public boolean add(E element) {
        if (element == null) throw new NullPointerException();
        ensureCapacity();
        heap[size] = element;
        heapifyUp(size);
        size++;
        return true;
    }

    @Override
    public boolean offer(E element) {
        return add(element);
    }

    @Override
    public E remove() {
        if (size == 0) throw new NoSuchElementException();
        return poll();
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E result = heap[0];
        heap[0] = heap[size - 1];
        size--;
        heapifyDown(0);
        return result;
    }
//Возвращает минимальный элемент
    @Override
    public E peek() {
        return size == 0 ? null : heap[0];
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return heap[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) heap[i] = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (heap[i].equals(o)) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size - 1; i++) sb.append(heap[i]).append(", ");
        sb.append(heap[size - 1]).append("]");
        return sb.toString();
    }

    // Методы работы с коллекциями
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
        for (E e : c) changed |= add(e);
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                heap[newSize++] = heap[i];
            } else {
                changed = true;
            }
        }
        size = newSize;
        // перестроить кучу после массового удаления
        for (int i = size / 2 - 1; i >= 0; i--) {
            heapifyDown(i);
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                heap[newSize++] = heap[i];
            } else {
                changed = true;
            }
        }
        size = newSize;
        // перестроить кучу после массового удаления
        for (int i = size / 2 - 1; i >= 0; i--) {
            heapifyDown(i);
        }
        return changed;
    }


    // Удаление конкретного элемента
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (heap[i].equals(o)) {
                heap[i] = heap[size - 1];
                size--;
                heapifyDown(i);
                return true;
            }
        }
        return false;
    }

    // Методы toArray для Collection
    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) result[i] = heap[i];
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        for (int i = 0; i < size; i++) a[i] = (T) heap[i];
        if (a.length > size) a[size] = null;
        return a;
    }

    // Iterator не реализован
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }
}
