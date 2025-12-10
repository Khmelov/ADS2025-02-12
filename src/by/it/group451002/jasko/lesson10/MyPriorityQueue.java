package by.it.group451002.jasko.lesson10;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

// Реализовать очередь с приоритетом на основе двоичной кучи (Binary Heap)
// Функции:
//- siftUp/siftDown - просеивание вверх или вниз
//- offer/add - добавление элемента
//- poll/remove - извлечение минимального
//- peek/element - просмотр минимального
//- contains/remove - поиск и удаление элемента

public class MyPriorityQueue<E> implements Queue<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private E[] heap;       // Массив, представляющий двоичную кучу (min-heap)
    private int size;       // Текущее количество элементов в куче
    private final Comparator<? super E> comparator; // Компаратор для определения порядка элементов

    // Конструктор по умолчанию - использует натуральный порядок элементов
    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        this.heap = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = null; // Натуральный порядок (элементы должны реализовывать Comparable)
    }

    // Конструктор с заданной начальной емкостью
    @SuppressWarnings("unchecked")
    public MyPriorityQueue(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        this.heap = (E[]) new Object[initialCapacity];
        this.size = 0;
        this.comparator = null;
    }

    // Конструктор с компаратором - позволяет задать custom порядок элементов
    @SuppressWarnings("unchecked")
    public MyPriorityQueue(Comparator<? super E> comparator) {
        this.heap = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = comparator; // Используется для сравнения элементов
    }

    // Конструктор с емкостью и компаратором
    @SuppressWarnings("unchecked")
    public MyPriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        this.heap = (E[]) new Object[initialCapacity];
        this.size = 0;
        this.comparator = comparator;
    }

    // Вспомогательные методы для работы с кучей

    // Возвращает индекс родительского узла
    private int parent(int index) {
        return (index - 1) / 2;
    }

    // Возвращает индекс левого потомка
    private int leftChild(int index) {
        return 2 * index + 1;
    }

    // Возвращает индекс правого потомка
    private int rightChild(int index) {
        return 2 * index + 2;
    }

    // Меняет местами два элемента в куче
    private void swap(int i, int j) {
        E temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    // Сравнение элементов с учетом компаратора (для min-heap)
    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b); // Используем custom компаратор
        } else {
            // Используем натуральный порядок (элементы должны быть Comparable)
            Comparable<? super E> comparable = (Comparable<? super E>) a;
            return comparable.compareTo(b);
        }
    }

    // Увеличение емкости массива при заполнении (динамическое расширение)
    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == heap.length) {
            int newCapacity = heap.length * 2; // Увеличиваем в 2 раза
            E[] newHeap = (E[]) new Object[newCapacity];
            System.arraycopy(heap, 0, newHeap, 0, size); // Копируем элементы
            heap = newHeap; // Заменяем старый массив новым
        }
    }

    // Просеивание вверх (восстановление свойства кучи при добавлении)
    // Поднимаем элемент, пока он меньше своего родителя
    private void siftUp(int index) {
        while (index > 0) {
            int parent = parent(index);
            // Если элемент уже на правильной позиции (≥ родителя), выходим
            if (compare(heap[index], heap[parent]) >= 0) {
                break;
            }
            // Меняем с родителем и продолжаем проверку
            swap(index, parent);
            index = parent;
        }
    }

    // Просеивание вниз (восстановление свойства кучи при удалении)
    // Опускаем элемент, пока он больше своих потомков
    private void siftDown(int index) {
        int smallest = index; // Предполагаем, что текущий элемент наименьший
        int left = leftChild(index);
        int right = rightChild(index);

        // Сравниваем с левым потомком
        if (left < size && compare(heap[left], heap[smallest]) < 0) {
            smallest = left; // Левый потомок меньше
        }

        // Сравниваем с правым потомком
        if (right < size && compare(heap[right], heap[smallest]) < 0) {
            smallest = right; // Правый потомок меньше
        }

        // Если нашли потомка меньше текущего элемента, меняем и продолжаем
        if (smallest != index) {
            swap(index, smallest);
            siftDown(smallest); // Рекурсивно просеиваем дальше
        }
    }

    // Построение кучи из произвольного массива (heapify)
    // Просеиваем все нелистовые узлы, начиная с последнего родителя
    private void buildHeap() {
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i); // Просеиваем каждый узел вниз
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    // Строковое представление очереди в формате [element1, element2, ...]
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

    // Текущее количество элементов в очереди
    @Override
    public int size() {
        return size;
    }

    // Очистка очереди - устанавливаем размер в 0 и зануляем ссылки
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null; // Помощь сборщику мусора
        }
        size = 0;
    }

    // Добавление элемента (аналогично offer, но бросает исключение при null)
    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }
        return offer(element);
    }

    // Удаление конкретного элемента из очереди
    @Override
    public boolean remove(Object element) {
        if (element == null) {
            return false; // null элементы не поддерживаются
        }

        // Линейный поиск элемента в куче
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (element.equals(heap[i])) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return false; // Элемент не найден
        }

        // Заменяем удаляемый элемент последним элементом
        heap[index] = heap[size - 1];
        heap[size - 1] = null; // Очищаем последнюю позицию
        size--;

        // Восстанавливаем свойства кучи
        if (index < size) {
            siftDown(index); // Просеиваем вниз
            siftUp(index);   // Просеиваем вверх (на случай если нужно поднять)
        }

        return true;
    }

    // Проверка наличия элемента в очереди
    @Override
    public boolean contains(Object element) {
        if (element == null) {
            return false;
        }

        // Линейный поиск по всему массиву
        for (int i = 0; i < size; i++) {
            if (element.equals(heap[i])) {
                return true;
            }
        }
        return false;
    }

    // Добавление элемента в очередь (основной метод добавления)
    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }

        ensureCapacity(); // Проверяем, нужно ли расширять массив
        heap[size] = element;     // Добавляем элемент в конец
        siftUp(size);             // Просеиваем вверх для восстановления кучи
        size++;
        return true;
    }

    // Извлечение минимального элемента (удаление и возврат)
    @Override
    public E poll() {
        if (size == 0) {
            return null; // Очередь пуста
        }

        E result = heap[0];           // Сохраняем корень (минимальный элемент)
        heap[0] = heap[size - 1];     // Перемещаем последний элемент в корень
        heap[size - 1] = null;        // Очищаем последнюю позицию
        size--;

        if (size > 0) {
            siftDown(0);              // Просеиваем новый корень вниз
        }

        return result;
    }

    // Просмотр минимального элемента без удаления
    @Override
    public E peek() {
        return (size == 0) ? null : heap[0]; // Просто возвращаем корень
    }

    // Просмотр минимального элемента (бросает исключение если очередь пуста)
    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException("Queue is empty");
        }
        return heap[0];
    }

    // Удаление минимального элемента (бросает исключение если очередь пуста)
    @Override
    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException("Queue is empty");
        }
        return poll();
    }

    // Проверка пустоты очереди
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Проверка наличия всех элементов коллекции в очереди
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) { // Проверяем каждый элемент
                return false;
            }
        }
        return true;
    }

    // Добавление всех элементов коллекции в очередь
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }

        boolean modified = false;
        for (E element : c) {
            if (add(element)) { // Добавляем каждый элемент
                modified = true; // Отмечаем что очередь изменилась
            }
        }
        return modified;
    }

    // Удаление всех элементов коллекции из очереди
    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }

        boolean modified = false;
        // Создаем временный массив для элементов, которые нужно сохранить
        @SuppressWarnings("unchecked")
        E[] temp = (E[]) new Object[size];
        int newSize = 0;

        // Копируем элементы, которые не входят в коллекцию
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                temp[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        // Заменяем старую кучу новой
        heap = temp;
        size = newSize;

        // Перестраиваем кучу
        if (modified && size > 0) {
            buildHeap();
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }

        boolean modified = false;
        // Создаем временный массив для элементов, которые нужно сохранить
        @SuppressWarnings("unchecked")
        E[] temp = (E[]) new Object[size];
        int newSize = 0;

        // Копируем элементы, которые входят в коллекцию
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                temp[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        // Заменяем старую кучу новой
        heap = temp;
        size = newSize;

        // Перестраиваем кучу
        if (modified && size > 0) {
            buildHeap();
        }

        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    //////     Остальные методы интерфейса Queue (необязательные)    ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        System.arraycopy(heap, 0, array, 0, size);
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        System.arraycopy(heap, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}
