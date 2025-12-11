package by.it.group410902.shahov.lesson10;

import java.util.*;
import java.lang.reflect.Array;

public class MyPriorityQueue<E> implements Queue<E> {

    // Базовая емкость массива по умолчанию
    private static final int DEFAULT_CAPACITY = 10;
    // Массив для хранения элементов в виде двоичной кучи
    private E[] heap;
    // Текущее количество элементов в очереди
    private int size;
    // Компаратор для сравнения элементов (может быть null для натурального порядка)
    private final Comparator<? super E> comparator;

    // Конструктор без компаратора - использует натуральный порядок элементов
    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        this.heap = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = null;
    }

    // Возвращает строковое представление очереди
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(heap[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    // Возвращает количество элементов в очереди
    @Override
    public int size() {
        return size;
    }

    // Очищает очередь, удаляя все элементы
    @Override
    public void clear(){
        Arrays.fill(heap, 0, size, null);
        size = 0;
    }

    // Добавляет элемент в очередь (аналогично offer)
    @Override
    public boolean add(E element) {
        return offer(element);
    }

    // Удаляет и возвращает головной элемент, выбрасывает исключение если очередь пуста
    @Override
    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return poll();
    }

    // Проверяет, содержится ли указанный элемент в очереди
    @Override
    public boolean contains(Object element) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(element, heap[i])) {
                return true;
            }
        }
        return false;
    }

    // Добавляет элемент в очередь
    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException();
        }

        // Увеличиваем массив если необходимо
        if (size >= heap.length) {
            resize();
        }

        // Добавляем элемент в конец и поднимаем его на нужную позицию
        heap[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    // Удаляет и возвращает головной элемент (минимальный) или null если очередь пуста
    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }

        E result = heap[0];
        removeAt(0);
        return result;
    }

    // Возвращает головной элемент без удаления или null если очередь пуста
    @Override
    public E peek() {
        return (size == 0) ? null : heap[0];
    }

    // Возвращает головной элемент без удаления, выбрасывает исключение если очередь пуста
    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return heap[0];
    }

    // Проверяет, пуста ли очередь
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Проверяет, содержатся ли все элементы указанной коллекции в очереди
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    // Добавляет все элементы из указанной коллекции в очередь
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c) {
            if (offer(element)) {
                modified = true;
            }
        }
        return modified;
    }

    // Удаляет все элементы, содержащиеся в указанной коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;

        // Фильтруем массив, оставляя только элементы не из коллекции c
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                heap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        // Очищаем оставшуюся часть массива
        Arrays.fill(heap, newSize, size, null);
        size = newSize;

        // Перестраиваем кучу после изменений
        if (modified) {
            heapify();
        }

        return modified;
    }

    // Сохраняет только элементы, содержащиеся в указанной коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                heap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }
        Arrays.fill(heap, newSize, size, null);
        size = newSize;
        if (modified) {
            heapify();
        }
        return modified;
    }

    // Поднимает элемент на нужную позицию в куче для сохранения свойства кучи
    private void siftUp(int index) {
        E element = heap[index];
        while (index > 0) {
            int parentIndex = (index - 1) >>> 1; // Индекс родителя
            E parent = heap[parentIndex];
            // Если элемент больше или равен родителю, останавливаемся
            if (compare(element, parent) >= 0) {
                break;
            }
            heap[index] = parent;
            index = parentIndex;
        }
        heap[index] = element;
    }

    // Опускает элемент на нужную позицию в куче для сохранения свойства кучи
    private void siftDown(int index) {
        E element = heap[index];
        int half = size >>> 1; // Индекс последнего родительского узла
        while (index < half) {
            int childIndex = (index << 1) + 1; // Левый потомок
            E child = heap[childIndex];
            int rightIndex = childIndex + 1; // Правый потомок

            // Выбираем меньшего из потомков
            if (rightIndex < size && compare(child, heap[rightIndex]) > 0) {
                childIndex = rightIndex;
                child = heap[rightIndex];
            }

            // Если элемент меньше или равен меньшему потомку, останавливаемся
            if (compare(element, child) <= 0) {
                break;
            }

            heap[index] = child;
            index = childIndex;
        }
        heap[index] = element;
    }

    // Перестраивает кучу для сохранения свойства кучи
    private void heapify() {
        // Начинаем с последнего родительского узла и идем к корню
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    // Удаляет элемент по указанному индексу
    private void removeAt(int index) {
        size--;
        if (index == size) {
            // Если удаляем последний элемент, просто обнуляем
            heap[index] = null;
        } else {
            // Заменяем удаляемый элемент последним и перестраиваем кучу
            E moved = heap[size];
            heap[size] = null;
            heap[index] = moved;
            siftDown(index);
            // Если элемент не опустился, пробуем поднять его
            if (heap[index] == moved) {
                siftUp(index);
            }
        }
    }

    // Сравнивает два элемента с использованием компаратора или натурального порядка
    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        } else {
            Comparable<? super E> comparable = (Comparable<? super E>) a;
            return comparable.compareTo(b);
        }
    }

    // Увеличивает емкость массива
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = heap.length * 2;
        E[] newHeap = (E[]) new Object[newCapacity];
        System.arraycopy(heap, 0, newHeap, 0, size);
        heap = newHeap;
    }

    // Не реализованные методы интерфейса Queue
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean equals(Object o) { return super.equals(o); }
    @Override public int hashCode() { return super.hashCode(); }
}