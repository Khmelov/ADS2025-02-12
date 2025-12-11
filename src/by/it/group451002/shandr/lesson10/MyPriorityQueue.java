package by.it.group451002.shandr.lesson10;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Queue;
import java.util.NoSuchElementException;

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
    public MyPriorityQueue(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        this.heap = (E[]) new Object[initialCapacity];
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
    public MyPriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        this.heap = (E[]) new Object[initialCapacity];
        this.size = 0;
        this.comparator = comparator;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Возвращает строковое представление очереди
     * @return строка в формате [element1, element2, ...]
     */
    @Override
    public String toString() {
        if (size == 0) {
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

    /**
     * Возвращает количество элементов в очереди
     * @return размер очереди
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Удаляет все элементы из очереди
     */
    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null; // Помогаем сборщику мусора
        }
        size = 0;
    }

    /**
     * Добавляет элемент в очередь
     * @param element элемент для добавления
     * @return true если элемент успешно добавлен
     */
    @Override
    public boolean add(E element) {
        return offer(element);
    }

    /**
     * Удаляет и возвращает головной элемент очереди
     * @return удаленный элемент
     * @throws NoSuchElementException если очередь пуста
     */
    @Override
    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException("Queue is empty");
        }
        return poll();
    }

    /**
     * Проверяет наличие элемента в очереди
     * @param element элемент для поиска
     * @return true если элемент найден
     */
    @Override
    public boolean contains(Object element) {
        for (int i = 0; i < size; i++) {
            if (element == null ? heap[i] == null : element.equals(heap[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Добавляет элемент в очередь
     * @param element элемент для добавления
     * @return true если элемент успешно добавлен
     */
    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        ensureCapacity();

        // Добавляем элемент в конец кучи
        heap[size] = element;
        // Восстанавливаем свойства кучи, поднимая элемент
        siftUp(size);
        size++;

        return true;
    }

    /**
     * Удаляет и возвращает головной элемент очереди
     * @return головной элемент или null если очередь пуста
     */
    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }

        E result = heap[0]; // Сохраняем корневой элемент
        size--;

        if (size > 0) {
            // Перемещаем последний элемент в корень
            heap[0] = heap[size];
            heap[size] = null; // Очищаем последнюю позицию
            // Восстанавливаем свойства кучи, опуская корневой элемент
            siftDown(0);
        } else {
            heap[0] = null;
        }

        return result;
    }

    /**
     * Возвращает головной элемент очереди без удаления
     * @return головной элемент или null если очередь пуста
     */
    @Override
    public E peek() {
        return (size == 0) ? null : heap[0];
    }

    /**
     * Возвращает головной элемент очереди без удаления
     * @return головной элемент
     * @throws NoSuchElementException если очередь пуста
     */
    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException("Queue is empty");
        }
        return heap[0];
    }

    /**
     * Проверяет, пуста ли очередь
     * @return true если очередь пуста
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Проверяет наличие всех элементов коллекции в очереди
     * @param c коллекция для проверки
     * @return true если все элементы присутствуют
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Добавляет все элементы коллекции в очередь
     * @param c коллекция для добавления
     * @return true если очередь изменилась
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }
        if (c.isEmpty()) {
            return false;
        }

        boolean modified = false;
        for (E element : c) {
            if (offer(element)) {
                modified = true;
            }
        }
        return modified;
    }

    /**
     * Удаляет все элементы, присутствующие в указанной коллекции
     * @param c коллекция элементов для удаления
     * @return true если очередь изменилась
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }
        if (c.isEmpty() || size == 0) {
            return false;
        }

        boolean modified = false;
        // Создаем временную копию кучи
        @SuppressWarnings("unchecked")
        E[] tempHeap = (E[]) new Object[size];
        int newSize = 0;

        // Копируем только те элементы, которые не входят в коллекцию c
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                tempHeap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            // Заменяем старую кучу новой
            this.heap = tempHeap;
            this.size = newSize;
            // Перестраиваем кучу
            buildHeap();
        }

        return modified;
    }

    /**
     * Сохраняет только элементы, присутствующие в указанной коллекции
     * @param c коллекция элементов для сохранения
     * @return true если очередь изменилась
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }
        if (size == 0) {
            return false;
        }

        boolean modified = false;
        // Создаем временную копию кучи
        @SuppressWarnings("unchecked")
        E[] tempHeap = (E[]) new Object[size];
        int newSize = 0;

        // Копируем только те элементы, которые входят в коллекцию c
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                tempHeap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            // Заменяем старую кучу новой
            this.heap = tempHeap;
            this.size = newSize;
            // Перестраиваем кучу
            buildHeap();
        }

        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы кучи                 ///////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Восстанавливает свойства кучи, поднимая элемент
     * @param index индекс элемента для подъема
     */
    private void siftUp(int index) {
        E element = heap[index];
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            E parent = heap[parentIndex];

            // Если элемент уже в правильной позиции, выходим
            if (compare(element, parent) >= 0) {
                break;
            }

            // Перемещаем родителя вниз
            heap[index] = parent;
            index = parentIndex;
        }
        heap[index] = element;
    }

    /**
     * Восстанавливает свойства кучи, опуская элемент
     * @param index индекс элемента для опускания
     */
    private void siftDown(int index) {
        E element = heap[index];
        int half = size / 2; // Только нелистовые узлы имеют детей

        while (index < half) {
            int childIndex = (index * 2) + 1; // Левый ребенок
            E child = heap[childIndex];
            int rightChildIndex = childIndex + 1;

            // Выбираем меньшего из детей (для min-heap)
            if (rightChildIndex < size && compare(heap[rightChildIndex], child) < 0) {
                childIndex = rightChildIndex;
                child = heap[childIndex];
            }

            // Если элемент уже в правильной позиции, выходим
            if (compare(element, child) <= 0) {
                break;
            }

            // Перемещаем ребенка вверх
            heap[index] = child;
            index = childIndex;
        }
        heap[index] = element;
    }

    /**
     * Перестраивает кучу из текущего массива
     */
    private void buildHeap() {
        // Начинаем с последнего нелистового узла
        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    /**
     * Сравнивает два элемента с использованием компаратора или естественного порядка
     * @param a первый элемент
     * @param b второй элемент
     * @return результат сравнения
     */
    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        } else {
            return ((Comparable<? super E>) a).compareTo(b);
        }
    }

    /**
     * Увеличивает емкость массива при необходимости
     */
    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == heap.length) {
            int newCapacity = heap.length * 2;
            E[] newHeap = (E[]) new Object[newCapacity];
            System.arraycopy(heap, 0, newHeap, 0, size);
            heap = newHeap;
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы Queue (заглушки)          ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Not implemented");
    }
}