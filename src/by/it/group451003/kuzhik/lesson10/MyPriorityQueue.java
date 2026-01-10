package by.it.group451003.kuzhik.lesson10;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private E[] heap;
    private int size;
    private final Comparator<? super E> comparator;

    // Конструкторы
    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        this.heap = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = null;
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(Comparator<? super E> comparator) {
        this.heap = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = comparator;
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(Collection<? extends E> c) {
        this.heap = (E[]) new Object[Math.max(DEFAULT_CAPACITY, c.size())];
        this.size = 0;
        this.comparator = null;
        addAll(c);
    }

    // Увеличиваем емкость массива при необходимости
    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == heap.length) {
            int newCapacity = heap.length * 2;
            E[] newHeap = (E[]) new Object[newCapacity];
            System.arraycopy(heap, 0, newHeap, 0, size);
            heap = newHeap;
        }
    }

    // Сравнение элементов
    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        } else {
            Comparable<? super E> comparable = (Comparable<? super E>) a;
            return comparable.compareTo(b);
        }
    }

    // Вспомогательные методы для работы с кучей
    private int parent(int index) {
        return (index - 1) / 2;
    }

    private int leftChild(int index) {
        return 2 * index + 1;
    }

    private int rightChild(int index) {
        return 2 * index + 2;
    }

    private void swap(int i, int j) {
        E temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    // Просеивание вверх (для добавления)
    private void siftUp(int index) {
        while (index > 0) {
            int parent = parent(index);
            if (compare(heap[index], heap[parent]) >= 0) {
                break;
            }
            swap(index, parent);
            index = parent;
        }
    }

    // Просеивание вниз (для удаления)
    private void siftDown(int index) {
        int smallest = index;

        while (true) {
            int left = leftChild(index);
            int right = rightChild(index);

            if (left < size && compare(heap[left], heap[smallest]) < 0) {
                smallest = left;
            }

            if (right < size && compare(heap[right], heap[smallest]) < 0) {
                smallest = right;
            }

            if (smallest == index) {
                break;
            }

            swap(index, smallest);
            index = smallest;
        }
    }

    // Построение кучи из массива
    private void buildHeap() {
        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    // Вспомогательный метод для проверки наличия элемента в коллекции
    private boolean containsInCollection(Collection<?> c, Object element) {
        for (Object item : c) {
            if (item == null ? element == null : item.equals(element)) {
                return true;
            }
        }
        return false;
    }

    // Добавление элемента
    @Override
    public boolean add(E element) {
        return offer(element);
    }

    // Предложение элемента (аналогично add, но не бросает исключение)
    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        ensureCapacity();
        heap[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    // Удаление и возврат минимального элемента (бросает исключение если пусто)
    @Override
    public E remove() {
        if (isEmpty()) {
            throw new NoSuchElementException("Priority queue is empty");
        }
        return poll();
    }

    // Удаление и возврат минимального элемента (возвращает null если пусто)
    @Override
    public E poll() {
        if (isEmpty()) {
            return null;
        }

        E result = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null; // Помогаем сборщику мусора
        size--;

        if (size > 0) {
            siftDown(0);
        }

        return result;
    }

    // Просмотр минимального элемента без удаления (бросает исключение если пусто)
    @Override
    public E element() {
        if (isEmpty()) {
            throw new NoSuchElementException("Priority queue is empty");
        }
        return heap[0];
    }

    // Просмотр минимального элемента без удаления (возвращает null если пусто)
    @Override
    public E peek() {
        if (isEmpty()) {
            return null;
        }
        return heap[0];
    }

    // Проверка наличия элемента
    @Override
    public boolean contains(Object element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        for (int i = 0; i < size; i++) {
            if (element.equals(heap[i])) {
                return true;
            }
        }
        return false;
    }

    // Проверка на пустоту
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Размер очереди
    @Override
    public int size() {
        return size;
    }

    // Очистка очереди
    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    // Проверка наличия всех элементов коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    // Добавление всех элементов коллекции
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        }

        // Увеличиваем емкость если нужно
        if (size + c.size() > heap.length) {
            ensureCapacityFor(size + c.size());
        }

        // Сохраняем старый размер
        int oldSize = size;

        // Добавляем все элементы
        for (E element : c) {
            if (element == null) {
                throw new NullPointerException("Null elements are not allowed");
            }
            heap[size++] = element;
        }

        // Перестраиваем кучу только для новых элементов
        for (int i = oldSize; i < size; i++) {
            siftUp(i);
        }

        return true;
    }

    // Удаление всех элементов коллекции - ИСПРАВЛЕННАЯ РЕАЛИЗАЦИЯ
    @Override
    public boolean removeAll(Collection<?> c) {
        if (c.isEmpty() || isEmpty()) {
            return false;
        }

        // Создаем временный массив для элементов, которые нужно сохранить
        @SuppressWarnings("unchecked")
        E[] temp = (E[]) new Object[heap.length];
        int newSize = 0;
        boolean modified = false;

        // Копируем только те элементы, которых нет в коллекции c
        for (int i = 0; i < size; i++) {
            if (!containsInCollection(c, heap[i])) {
                temp[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            // Заменяем старую кучу новой
            heap = temp;
            size = newSize;
            buildHeap();
        }

        return modified;
    }

    // Сохранение только элементов коллекции - ИСПРАВЛЕННАЯ РЕАЛИЗАЦИЯ
    @Override
    public boolean retainAll(Collection<?> c) {
        if (isEmpty()) {
            return false;
        }

        if (c.isEmpty()) {
            clear();
            return true;
        }

        // Создаем временный массив для элементов, которые нужно сохранить
        @SuppressWarnings("unchecked")
        E[] temp = (E[]) new Object[heap.length];
        int newSize = 0;
        boolean modified = false;

        // Копируем только те элементы, которые есть в коллекции c
        for (int i = 0; i < size; i++) {
            if (containsInCollection(c, heap[i])) {
                temp[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            // Заменяем старую кучу новой
            heap = temp;
            size = newSize;
            buildHeap();
        }

        return modified;
    }

    // Вспомогательный метод для увеличения емкости до нужного размера
    @SuppressWarnings("unchecked")
    private void ensureCapacityFor(int minCapacity) {
        if (minCapacity > heap.length) {
            int newCapacity = Math.max(heap.length * 2, minCapacity);
            E[] newHeap = (E[]) new Object[newCapacity];
            System.arraycopy(heap, 0, newHeap, 0, size);
            heap = newHeap;
        }
    }

    // Строковое представление
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /////////////////////////////////////////////////////////////////////////
    //////   Остальные методы интерфейса Queue - не реализованы      ///////
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