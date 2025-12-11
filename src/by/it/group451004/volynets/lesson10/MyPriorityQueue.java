package by.it.group451004.volynets.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] heap;
    private int size;

    // конструктор по умолчанию
    public MyPriorityQueue() {
        heap = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    // конструктор с начальной емкостью
    public MyPriorityQueue(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException();
        }
        heap = new Object[initialCapacity];
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    // строковое представление очереди
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // количество элементов в очереди
    @Override
    public int size() {
        return size;
    }

    // очистка очереди
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    // добавление элемента (аналогично offer)
    @Override
    public boolean add(E e) {
        return offer(e);
    }

    // удаление и возврат головного элемента (бросает исключение если пусто)
    @Override
    public E remove() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }
        return poll();
    }

    // проверка наличия элемента в очереди
    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? heap[i] == null : o.equals(heap[i])) {
                return true;
            }
        }
        return false;
    }

    // добавление элемента в очередь
    @Override
    public boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        ensureCapacity(size + 1);
        heap[size] = e;
        siftUp(size);
        size++;
        return true;
    }

    // удаление и возврат головного элемента (возвращает null если пусто)
    @Override
    @SuppressWarnings("unchecked")
    public E poll() {
        if (size == 0) {
            return null;
        }
        E result = (E) heap[0];
        heap[0] = heap[size - 1];
        heap[--size] = null;
        if (size > 0) {
            siftDown(0);
        }
        return result;
    }

    // просмотр головного элемента без удаления (возвращает null если пусто)
    @Override
    @SuppressWarnings("unchecked")
    public E peek() {
        return (size == 0) ? null : (E) heap[0];
    }

    // просмотр головного элемента без удаления (бросает исключение если пусто)
    @Override
    public E element() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }
        return peek();
    }

    // проверка пустоты очереди
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // содержит ли очередь все элементы коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    // добавление всех элементов коллекции
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        }
        for (E element : c) {
            offer(element);
        }
        return true;
    }

    // удаление всех элементов, которые есть в коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        // Создаем временный массив для элементов, которые нужно сохранить
        Object[] temp = new Object[size];
        int newSize = 0;

        // Собираем элементы, которые НЕ нужно удалять (в исходном порядке)
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                temp[newSize++] = heap[i];
            }
        }

        // Если размер изменился - перестраиваем кучу
        if (newSize != size) {
            modified = true;
            // Очищаем текущую кучу
            clear();

            // Восстанавливаем кучу из временного массива
            heap = new Object[Math.max(DEFAULT_CAPACITY, newSize)];
            for (int i = 0; i < newSize; i++) {
                @SuppressWarnings("unchecked")
                E element = (E) temp[i];
                heap[size] = element;
                size++;
            }
            // Перестраиваем кучу из массива
            buildHeap();
        }
        return modified;
    }

    // оставить только элементы, которые есть в коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        // Создаем временный массив для элементов, которые нужно сохранить
        Object[] temp = new Object[size];
        int newSize = 0;

        // Собираем элементы, которые нужно сохранить (в исходном порядке)
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                temp[newSize++] = heap[i];
            }
        }

        // Если размер изменился - перестраиваем кучу
        if (newSize != size) {
            modified = true;
            // Очищаем текущую кучу
            clear();

            // Восстанавливаем кучу из временного массива
            heap = new Object[Math.max(DEFAULT_CAPACITY, newSize)];
            for (int i = 0; i < newSize; i++) {
                @SuppressWarnings("unchecked")
                E element = (E) temp[i];
                heap[size] = element;
                size++;
            }
            // Перестраиваем кучу из массива
            buildHeap();
        }
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы кучи               ///////
    /////////////////////////////////////////////////////////////////////////

    // подъем элемента вверх по куче для восстановления свойств
    @SuppressWarnings("unchecked")
    private void siftUp(int index) {
        E element = (E) heap[index];
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            E parent = (E) heap[parentIndex];
            if (compare(element, parent) >= 0) {
                break;
            }
            heap[index] = parent;
            index = parentIndex;
        }
        heap[index] = element;
    }

    // опускание элемента вниз по куче для восстановления свойств
    @SuppressWarnings("unchecked")
    private void siftDown(int index) {
        E element = (E) heap[index];
        int half = size / 2;
        while (index < half) {
            int childIndex = (index * 2) + 1;
            E child = (E) heap[childIndex];
            int rightIndex = childIndex + 1;
            if (rightIndex < size && compare(child, (E) heap[rightIndex]) > 0) {
                childIndex = rightIndex;
                child = (E) heap[childIndex];
            }
            if (compare(element, child) <= 0) {
                break;
            }
            heap[index] = child;
            index = childIndex;
        }
        heap[index] = element;
    }

    // построение кучи из произвольного массива
    private void buildHeap() {
        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    // сравнение двух элементов (использует Comparable)
    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        return ((Comparable<? super E>) a).compareTo(b);
    }

    // увеличение емкости массива при необходимости
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > heap.length) {
            int newCapacity = heap.length * 2;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            Object[] newHeap = new Object[newCapacity];
            System.arraycopy(heap, 0, newHeap, 0, size);
            heap = newHeap;
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы Queue - заглушки        ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }
}