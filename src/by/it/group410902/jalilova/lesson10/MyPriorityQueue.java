package by.it.group410902.jalilova.lesson10;
import java.util.*;
import java.lang.reflect.Array;

//реализация приоритетной очереди на основе минимальной кучи
public class MyPriorityQueue<E> implements Queue<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private E[] heap;       //массив-куча для хранения элементов
    private int size;       //текущее количество элементов
    private final Comparator<? super E> comparator; //компаратор для сравнения элементов

    //конструктор по умолчанию
    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        this.heap = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = null;
    }

    //строковое представление в формате [a, b, c]
    @Override
    public String toString() {
        if (size == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(heap[i]);
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() {
        return size;
    }

    //очистка очереди
    @Override
    public void clear() {
        Arrays.fill(heap, 0, size, null);
        size = 0;
    }

    //добавление элемента
    @Override
    public boolean add(E element) {
        return offer(element);
    }

    //удаление конкретного элемента
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, heap[i])) {
                removeAt(i); //удаление по индексу с перестройкой кучи
                return true;
            }
        }
        return false;
    }

    //удаление и возврат головного элемента (с исключением если пусто)
    @Override
    public E remove() {
        if (size == 0) throw new NoSuchElementException();
        return poll();
    }

    //проверка наличия элемента
    @Override
    public boolean contains(Object element) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(element, heap[i])) return true;
        }
        return false;
    }

    //добавление элемента в кучу
    @Override
    public boolean offer(E element) {
        if (element == null) throw new NullPointerException();

        if (size >= heap.length) resize(); //увеличение размера при необходимости

        heap[size] = element;
        siftUp(size); //просеивание вверх для сохранения свойств кучи
        size++;
        return true;
    }

    //извлечение минимального элемента без исключения
    @Override
    public E poll() {
        if (size == 0) return null;

        E result = heap[0];
        removeAt(0); //удаление корня с перестройкой кучи
        return result;
    }

    //просмотр минимального элемента без удаления
    @Override
    public E peek() {
        return (size == 0) ? null : heap[0];
    }

    //просмотр минимального элемента с исключением если пусто
    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return heap[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    //проверка наличия всех элементов коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) return false;
        }
        return true;
    }

    //добавление всех элементов коллекции
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c) {
            if (offer(element)) modified = true;
        }
        return modified;
    }

    //удаление всех элементов, присутствующих в коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;

        //фильтрация элементов
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                heap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        Arrays.fill(heap, newSize, size, null);
        size = newSize;

        if (modified) heapify(); //перестройка кучи после изменений
        return modified;
    }

    //удаление всех элементов, кроме присутствующих в коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;

        //фильтрация элементов
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

        if (modified) heapify(); //перестройка кучи после изменений
        return modified;
    }

    //увеличение размера массива в 2 раза
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = heap.length * 2;
        E[] newHeap = (E[]) new Object[newCapacity];
        System.arraycopy(heap, 0, newHeap, 0, size);
        heap = newHeap;
    }

    //сравнение двух элементов (с использованием компаратора или natural ordering)
    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        } else {
            Comparable<? super E> comparable = (Comparable<? super E>) a;
            return comparable.compareTo(b);
        }
    }

    //просеивание элемента вверх для сохранения свойств минимальной кучи
    private void siftUp(int index) {
        E element = heap[index];
        while (index > 0) {
            int parentIndex = (index - 1) >>> 1; //индекс родителя
            E parent = heap[parentIndex];
            if (compare(element, parent) >= 0) break; //условие кучи выполнено

            heap[index] = parent; //перемещаем родителя вниз
            index = parentIndex;
        }
        heap[index] = element; //устанавливаем элемент на правильную позицию
    }

    //просеивание элемента вниз для сохранения свойств кучи
    private void siftDown(int index) {
        E element = heap[index];
        int half = size >>> 1; //последний нелистовой узел

        while (index < half) {
            int childIndex = (index << 1) + 1; //левый потомок
            E child = heap[childIndex];
            int rightIndex = childIndex + 1;   //правый потомок

            // Выбор меньшего потомка
            if (rightIndex < size && compare(child, heap[rightIndex]) > 0) {
                childIndex = rightIndex;
                child = heap[rightIndex];
            }

            if (compare(element, child) <= 0) break; //условие кучи выполнено

            heap[index] = child; //перемещаем потомка вверх
            index = childIndex;
        }
        heap[index] = element; //устанавливаем элемент на правильную позицию
    }

    //построение кучи из произвольного массива
    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i); //просеивание всех нелистовых узлов
        }
    }

    //удаление элемента по индексу с сохранением свойств кучи
    private void removeAt(int index) {
        size--;
        if (index == size) {
            heap[index] = null; //удаление последнего элемента
        } else {
            E moved = heap[size]; //последний элемент
            heap[size] = null;
            heap[index] = moved;
            siftDown(index); //просеивание вниз
            if (heap[index] == moved) {
                siftUp(index); //просеивание вверх если необходимо
            }
        }
    }

    // методы интерфейса, не требующиеся для основной функциональности
    @Override
    public Iterator<E> iterator() {
        return Arrays.asList(Arrays.copyOf(heap, size)).iterator();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(heap, size);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) Arrays.copyOf(heap, size, a.getClass());
        }
        System.arraycopy(heap, 0, a, 0, size);
        if (a.length > size) a[size] = null;
        return a;
    }
}