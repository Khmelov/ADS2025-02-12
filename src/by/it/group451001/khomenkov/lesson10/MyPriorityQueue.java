package by.it.group451001.khomenkov.lesson10;

import java.util.Collection;
import java.util.Queue;
import java.util.NoSuchElementException;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private E[] heap;
    private int size;

    // Конструктор
    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = (E[]) new Comparable[DEFAULT_CAPACITY];
        size = 0;
    }

    // Конструктор с коллекцией
    @SuppressWarnings("unchecked")
    public MyPriorityQueue(Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }
        heap = (E[]) new Comparable[Math.max(DEFAULT_CAPACITY, c.size() + 1)];
        size = 0;
        addAll(c);
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 1; i <= size; i++) {
            sb.append(heap[i]);
            if (i < size) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
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
        for (int i = 1; i <= size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException("Cannot add null element");
        }
        return offer(element);
    }

    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException("Cannot offer null element");
        }

        ensureCapacity();

        // Добавляем элемент в конец
        size++;
        heap[size] = element;

        // Просеиваем вверх
        siftUp(size);
        return true;
    }

    @Override
    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException("Priority queue is empty");
        }
        return poll();
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }

        E root = heap[1];

        // Перемещаем последний элемент в корень
        heap[1] = heap[size];
        heap[size] = null;
        size--;

        // Просеиваем вниз
        if (size > 0) {
            siftDown(1);
        }

        return root;
    }

    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException("Priority queue is empty");
        }
        return heap[1];
    }

    @Override
    public E peek() {
        if (size == 0) {
            return null;
        }
        return heap[1];
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            throw new NullPointerException("Cannot check for null element");
        }

        for (int i = 1; i <= size; i++) {
            if (o.equals(heap[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }

        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }

        boolean modified = false;
        for (E element : c) {
            if (offer(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }

        // Подсчитываем, сколько элементов нужно сохранить
        int keepCount = 0;
        for (int i = 1; i <= size; i++) {
            if (!c.contains(heap[i])) {
                keepCount++;
            }
        }

        if (keepCount == size) {
            return false; // Ничего не изменилось
        }

        // Создаем временный массив для сохраненных элементов
        @SuppressWarnings("unchecked")
        E[] temp = (E[]) new Comparable[keepCount];
        int index = 0;

        // Собираем элементы для сохранения
        for (int i = 1; i <= size; i++) {
            if (!c.contains(heap[i])) {
                temp[index++] = heap[i];
            }
        }

        // Очищаем и перестраиваем кучу
        clear();
        for (int i = 0; i < keepCount; i++) {
            offer(temp[i]);
        }

        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }

        // Просто удаляем элементы, которые не в коллекции c
        // Это сохранит исходный порядок массива
        boolean modified = false;
        int writeIndex = 1;

        for (int readIndex = 1; readIndex <= size; readIndex++) {
            if (c.contains(heap[readIndex])) {
                heap[writeIndex++] = heap[readIndex];
            } else {
                modified = true;
            }
        }

        // Обнуляем оставшиеся элементы
        for (int i = writeIndex; i <= size; i++) {
            heap[i] = null;
        }

        size = writeIndex - 1;

        // Восстанавливаем свойства кучи
        for (int i = size / 2; i >= 1; i--) {
            siftDown(i);
        }

        return modified;
    }

    // Вспомогательные методы для работы с кучей
    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size >= heap.length - 1) {
            int newCapacity = heap.length * 2;
            E[] newHeap = (E[]) new Comparable[newCapacity];
            System.arraycopy(heap, 1, newHeap, 1, size);
            heap = newHeap;
        }
    }

    private void siftUp(int index) {
        E element = heap[index];
        while (index > 1) {
            int parentIndex = index / 2;
            if (element.compareTo(heap[parentIndex]) >= 0) {
                break;
            }
            heap[index] = heap[parentIndex];
            index = parentIndex;
        }
        heap[index] = element;
    }

    private void siftDown(int index) {
        E element = heap[index];
        int childIndex;

        while (2 * index <= size) {
            childIndex = 2 * index;

            // Выбираем меньшего потомка
            if (childIndex < size && heap[childIndex].compareTo(heap[childIndex + 1]) > 0) {
                childIndex++;
            }

            if (element.compareTo(heap[childIndex]) <= 0) {
                break;
            }

            heap[index] = heap[childIndex];
            index = childIndex;
        }
        heap[index] = element;
    }

    // Методы, которые не поддерживаются в данной реализации
    @Override
    public boolean remove(Object o) {
        if (o == null) {
            throw new NullPointerException("Cannot remove null element");
        }

        // Находим индекс элемента
        int index = -1;
        for (int i = 1; i <= size; i++) {
            if (o.equals(heap[i])) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return false;
        }

        // Заменяем удаляемый элемент последним
        heap[index] = heap[size];
        heap[size] = null;
        size--;

        // Восстанавливаем свойства кучи
        if (index <= size) {
            siftDown(index);
            siftUp(index);
        }

        return true;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        System.arraycopy(heap, 1, array, 0, size);
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        }
        System.arraycopy(heap, 1, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    // Неподдерживаемые методы (очередь с приоритетом не поддерживает итерацию в порядке очереди)
    @Override
    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }
}
