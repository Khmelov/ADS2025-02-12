package by.it.group410902.varava.lesson10;
import java.util.Collection;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Реализация очереди с приоритетом на основе двоичной кучи (min-heap)
 * @param <E> тип элементов в очереди (должен быть Comparable или передаваться Comparator)
 */
public class MyPriorityQueue<E> implements Queue<E> {
    // Константы
    private static final int DEFAULT_CAPACITY = 10; // Начальная емкость по умолчанию

    // Поля класса
    private E[] heap;                    // Массив для хранения элементов кучи
    private int size;                    // Текущее количество элементов
    private final Comparator<? super E> comparator; // Компаратор для сравнения элементов

    /**
     * Конструктор по умолчанию - создает очередь с естественным порядком элементов
     */
    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        this.heap = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = null;
    }

    /**
     * Конструктор с компаратором
     * @param comparator компаратор для определения порядка элементов
     */
    @SuppressWarnings("unchecked")
    public MyPriorityQueue(Comparator<? super E> comparator) {
        this.heap = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = comparator;
    }

    /**
     * Конструктор с начальной коллекцией (естественный порядок)
     * @param c коллекция элементов для добавления
     */
    @SuppressWarnings("unchecked")
    public MyPriorityQueue(Collection<? extends E> c) {
        this.heap = (E[]) new Object[Math.max(DEFAULT_CAPACITY, c.size())];
        this.size = 0;
        this.comparator = null;
        // Добавляем все элементы из коллекции
        for (E element : c) {
            offer(element);
        }
    }

    /**
     * Конструктор с начальной коллекцией и компаратором
     * @param c коллекция элементов для добавления
     * @param comparator компаратор для определения порядка элементов
     */
    @SuppressWarnings("unchecked")
    public MyPriorityQueue(Collection<? extends E> c, Comparator<? super E> comparator) {
        this.heap = (E[]) new Object[Math.max(DEFAULT_CAPACITY, c.size())];
        this.size = 0;
        this.comparator = comparator;
        // Добавляем все элементы из коллекции
        for (E element : c) {
            offer(element);
        }
    }

    /**
     * Представление очереди в виде строки
     * @return строковое представление элементов очереди в порядке массива кучи
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
     * @return текущий размер очереди
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Очищает очередь, удаляя все элементы
     */
    @Override
    public void clear() {
        // Обнуляем ссылки для помощи сборщику мусора
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    /**
     * Добавляет элемент в очередь
     * @param element добавляемый элемент
     * @return true если элемент добавлен успешно
     */
    @Override
    public boolean add(E element) {
        return offer(element);
    }

    /**
     * Удаляет указанный элемент из очереди
     * @param element элемент для удаления
     * @return true если элемент был найден и удален, false в противном случае
     */
    @Override
    public boolean remove(Object element) {
        // Линейный поиск элемента в куче
        for (int i = 0; i < size; i++) {
            if (elementsEqual(heap[i], element)) {
                removeAt(i); // Удаляем элемент по индексу
                return true;
            }
        }
        return false;
    }

    /**
     * Проверяет, содержится ли указанный элемент в очереди
     * @param element искомый элемент
     * @return true если элемент найден, false в противном случае
     */
    @Override
    public boolean contains(Object element) {
        // Линейный поиск элемента в куче
        for (int i = 0; i < size; i++) {
            if (elementsEqual(heap[i], element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Добавляет элемент в очередь с приоритетом
     * @param element добавляемый элемент
     * @return true если элемент добавлен успешно
     * @throws NullPointerException если элемент равен null
     */
    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        ensureCapacity(); // Проверяем и увеличиваем емкость при необходимости

        // Добавляем элемент в конец кучи и поднимаем его на нужную позицию
        heap[size] = element;
        siftUp(size);    // Просеивание вверх для восстановления свойств кучи
        size++;
        return true;
    }

    /**
     * Извлекает и удаляет элемент с наивысшим приоритетом (корень кучи)
     * @return элемент с наивысшим приоритетом или null, если очередь пуста
     */
    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }

        E result = heap[0]; // Корень кучи - элемент с наивысшим приоритетом
        removeAt(0);        // Удаляем корень и перестраиваем кучу
        return result;
    }

    /**
     * Возвращает элемент с наивысшим приоритетом без удаления
     * @return элемент с наивысшим приоритетом или null, если очередь пуста
     */
    @Override
    public E peek() {
        return (size == 0) ? null : heap[0];
    }

    /**
     * Возвращает элемент с наивысшим приоритетом без удаления
     * @return элемент с наивысшим приоритетом
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
     * Извлекает и удаляет элемент с наивысшим приоритетом
     * @return элемент с наивысшим приоритетом
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
     * Проверяет, пуста ли очередь
     * @return true если очередь пуста, false в противном случае
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Проверяет, содержатся ли все элементы указанной коллекции в очереди
     * @param c коллекция элементов для проверки
     * @return true если все элементы содержатся в очереди, false в противном случае
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
     * Добавляет все элементы указанной коллекции в очередь
     * @param c коллекция элементов для добавления
     * @return true если очередь изменилась в результате операции
     */
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

    /**
     * Удаляет из очереди все элементы, содержащиеся в указанной коллекции
     * @param c коллекция элементов для удаления
     * @return true если очередь изменилась в результате операции
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;

        // Создаем новый массив для элементов, которые нужно сохранить
        @SuppressWarnings("unchecked")
        E[] newHeap = (E[]) new Object[heap.length];
        int newSize = 0;

        // Проходим по всем элементам текущей кучи
        for (int i = 0; i < size; i++) {
            E element = heap[i];
            if (!collectionContains(c, element)) {
                // Сохраняем элемент, если его НЕТ в коллекции для удаления
                newHeap[newSize++] = element;
            } else {
                modified = true;
            }
        }

        // Если были изменения, заменяем кучу и перестраиваем ее
        if (modified) {
            this.heap = newHeap;
            this.size = newSize;
            heapify(); // Перестраиваем кучу
        }

        return modified;
    }

    /**
     * Сохраняет в очереди только элементы, содержащиеся в указанной коллекции
     * @param c коллекция элементов для сохранения
     * @return true если очередь изменилась в результате операции
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;

        // Создаем новый массив для элементов, которые нужно сохранить
        @SuppressWarnings("unchecked")
        E[] newHeap = (E[]) new Object[heap.length];
        int newSize = 0;

        // Проходим по всем элементам текущей кучи
        for (int i = 0; i < size; i++) {
            E element = heap[i];
            if (collectionContains(c, element)) {
                // Сохраняем элемент, если он ЕСТЬ в коллекции
                newHeap[newSize++] = element;
            } else {
                modified = true;
            }
        }

        // Если были изменения, заменяем кучу и перестраиваем ее
        if (modified) {
            this.heap = newHeap;
            this.size = newSize;
            heapify(); // Перестраиваем кучу
        }

        return modified;
    }

    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ДЛЯ РАБОТЫ С КУЧЕЙ

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

    /**
     * Просеивание элемента вверх для восстановления свойств кучи
     * @param index индекс просеиваемого элемента
     */
    private void siftUp(int index) {
        E element = heap[index];
        // Поднимаем элемент, пока он не займет правильную позицию
        while (index > 0) {
            int parentIndex = (index - 1) >>> 1; // Индекс родителя (деление на 2)
            E parent = heap[parentIndex];
            // Если элемент уже в правильной позиции относительно родителя, выходим
            if (compare(element, parent) >= 0) {
                break;
            }
            // Перемещаем родителя вниз
            heap[index] = parent;
            index = parentIndex;
        }
        // Устанавливаем элемент на найденную позицию
        heap[index] = element;
    }

    /**
     * Просеивание элемента вниз для восстановления свойств кучи
     * @param index индекс просеиваемого элемента
     */
    private void siftDown(int index) {
        E element = heap[index];
        int half = size >>> 1; // Индекс последнего узла, у которого есть дети

        // Опускаем элемент, пока он не займет правильную позицию
        while (index < half) {
            int childIndex = (index << 1) + 1; // Левый ребенок
            E child = heap[childIndex];
            int rightIndex = childIndex + 1;   // Правый ребенок

            // Выбираем наименьшего из детей
            if (rightIndex < size && compare(heap[rightIndex], child) < 0) {
                childIndex = rightIndex;
                child = heap[rightIndex];
            }

            // Если элемент уже в правильной позиции относительно детей, выходим
            if (compare(element, child) <= 0) {
                break;
            }

            // Перемещаем ребенка вверх
            heap[index] = child;
            index = childIndex;
        }
        // Устанавливаем элемент на найденную позицию
        heap[index] = element;
    }

    /**
     * Перестраивает кучу для восстановления свойств кучи
     */
    private void heapify() {
        // Просеиваем все элементы, начиная с середины (последний нелистовой узел)
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    /**
     * Сравнивает два элемента с использованием компаратора или естественного порядка
     * @param a первый элемент
     * @param b второй элемент
     * @return отрицательное число если a < b, 0 если a == b, положительное если a > b
     */
    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        } else {
            // Используем естественный порядок (Comparable)
            Comparable<? super E> comparable = (Comparable<? super E>) a;
            return comparable.compareTo(b);
        }
    }

    /**
     * Удаляет элемент по указанному индексу
     * @param index индекс удаляемого элемента
     */
    private void removeAt(int index) {
        // Заменяем удаляемый элемент последним элементом
        E lastElement = heap[size - 1];
        heap[size - 1] = null; // Помогаем сборщику мусора
        size--;

        if (index < size) {
            // Помещаем последний элемент на место удаленного
            heap[index] = lastElement;
            // Просеиваем вниз, и если элемент не опустился, то просеиваем вверх
            siftDown(index);
            if (heap[index] == lastElement) {
                siftUp(index);
            }
        }
    }

    /**
     * Проверяет равенство двух объектов с учетом null
     * @param a первый объект
     * @param b второй объект
     * @return true если объекты равны, false в противном случае
     */
    private boolean elementsEqual(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }

    /**
     * Проверяет, содержится ли элемент в коллекции (с учетом null)
     * @param c коллекция для поиска
     * @param element искомый элемент
     * @return true если элемент содержится в коллекции, false в противном случае
     */
    private boolean collectionContains(Collection<?> c, Object element) {
        for (Object item : c) {
            if (elementsEqual(item, element)) {
                return true;
            }
        }
        return false;
    }

    /////////////////////////////////////////////////////////////////////////
    //////    Остальные методы интерфейса Queue - не реализованы     ///////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Возвращает массив, содержащий все элементы очереди
     * @return массив элементов очереди
     */
    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(heap, 0, result, 0, size);
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException("Not implemented");
    }
}