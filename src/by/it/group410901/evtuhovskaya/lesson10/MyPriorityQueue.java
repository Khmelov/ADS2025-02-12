package by.it.group410901.evtuhovskaya.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

//приоритетная очередь
public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    private static final int DEF_CAPACITY = 10;
    private Object[] heap; //массив, в котором хранится куча
    private int size;

    //конструктор
    public MyPriorityQueue() {
        heap = new Object[DEF_CAPACITY]; //очередь 10 элементов
        size = 0;
    }

    public MyPriorityQueue(int capacity) {
        heap = new Object[Math.max(capacity, DEF_CAPACITY)]; //очередь не меньше чем с 10 элементами
        size = 0;
    }


    private void ensureCapacity() {
        if (size == heap.length) { //если массив полность заполнен
            Object[] newHeap = new Object[heap.length * 2]; //создать новый 2х
            System.arraycopy(heap, 0, newHeap, 0, size); //копирует туда элементы
            heap = newHeap;
        }
    }

    private void swap(int i, int j) {
        Object tmp = heap[i]; //присваиваем
        heap[i] = heap[j]; //меняем
        heap[j] = tmp; //возвращаем
    }

    private void siftUp(int index) { //просеивание вверх при добавлении элемента
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (((E) heap[index]).compareTo((E) heap[parent]) >= 0) break; //если родитель в куче больше добавляемого элемента
            swap(index, parent);                                            //меняем местами и так пока не встанет на место
            index = parent;
        }
    }

    private void siftDown(int index) { //просеивание вниз при удалении элемента
        while (true) {
            //2 потомка у каждой вершины
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            //если потомок меньше родителя, меняем местами с меньших из двух
            if (left < size && ((E) heap[left]).compareTo((E) heap[smallest]) < 0)
                smallest = left;
            if (right < size && ((E) heap[right]).compareTo((E) heap[smallest]) < 0)
                smallest = right;

            if (smallest == index) break; //пока не встанет на место
            swap(index, smallest);
            index = smallest;
        }
    }

    private void heapify() {
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    private void rebuildHeap() {
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i != size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
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
    public boolean add(E e) {
        return offer(e);
    }

    @Override
    public boolean offer(E e) {
        if (e == null) throw new NullPointerException(); //если массив не пустой
        ensureCapacity();
        heap[size] = e; //добавляем элемент
        siftUp(size); //передвигаем на нужное место
        size++;
        return true;
    }

    @Override
    public E peek() { //показывает минимум либо нулл
        if (size == 0) return null;
        return (E) heap[0];
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return (E) heap[0];
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E result = (E) heap[0]; //сохраняем корень
        heap[0] = heap[--size]; //переносим туда последний элемент
        heap[size] = null; //удаляем
        siftDown(0); //просеиваем
        return result;
    }

    @Override
    public E remove() {
        E e = poll();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) { //проход с начала до конца на наличие элемента
            if ((o == null && heap[i] == null) || (o != null && o.equals(heap[i]))) return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) { //содержит ли коллекцию
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) { //добавить коллекцию
        boolean modified = false;
        for (E e : c) {
            if (offer(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) { //удалить коллекцию
        boolean modified = false;
        Object[] newHeap = new Object[heap.length];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                newHeap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            heap = newHeap;
            size = newSize;
            rebuildHeap();
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) { //удалить все кроме коллекции
        boolean modified = false;
        Object[] newHeap = new Object[heap.length];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                newHeap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            Object[] tmp = new Object[heap.length];
            int tmpSize = 0;
            for (int i = 0; i < newSize; i++) {
                tmp[tmpSize++] = newHeap[i];
            }
            heap = tmp;
            size = newSize;
            rebuildHeap();
        }

        return modified;
    }

    private boolean removeElement(Object o) { //удаляет элемент по значению
        for (int i = 0; i < size; i++) {
            if ((o == null && heap[i] == null) || (o != null && o.equals(heap[i]))) { //поиск совпадения
                heap[i] = heap[--size]; //уменьшить кучу
                heap[size] = null;
                siftDown(i);
                return true;
            }
        }
        return false;
    }

    /////////////////////////////////////////////////////////////////////////
    //////                    Остальные методы                        ///////
    /////////////////////////////////////////////////////////////////////////

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean remove(Object o) { return removeElement(o); }
}