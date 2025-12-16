package by.it.group451003.sorokin.lesson10;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Queue;
import java.util.NoSuchElementException;

public class MyPriorityQueue<E> implements Queue<E> {
    private E[] heap;
    private int size;
    private static final int INITIAL_CAPACITY = 10;
    private final Comparator<? super E> comparator;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = (E[]) new Object[INITIAL_CAPACITY];
        size = 0;
        comparator = null;
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(Comparator<? super E> comparator) {
        heap = (E[]) new Object[INITIAL_CAPACITY];
        size = 0;
        this.comparator = comparator;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

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

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public boolean remove(Object o) {
        // Поиск элемента в куче
        for (int i = 0; i < size; i++) {
            if (equals(o, heap[i])) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        // Линейный поиск в массиве
        for (int i = 0; i < size; i++) {
            if (equals(o, heap[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException("Cannot add null to priority queue");
        }

        // Увеличиваем массив если нужно
        if (size == heap.length) {
            resize();
        }

        // Добавляем элемент в конец
        heap[size] = element;
        // Просеиваем вверх для восстановления свойств кучи
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }

        E result = heap[0]; // корень кучи (минимальный элемент)
        // Перемещаем последний элемент в корень
        heap[0] = heap[size - 1];
        heap[size - 1] = null; // пометка для GC
        size--;

        // Просеиваем вниз для восстановления свойств кучи
        if (size > 0) {
            siftDown(0);
        }

        return result;
    }

    @Override
    public E peek() {
        return (size == 0) ? null : heap[0];
    }

    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException("Queue is empty");
        }
        return heap[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        }

        // Увеличиваем массив если нужно
        int neededCapacity = size + c.size();
        if (neededCapacity > heap.length) {
            resize(Math.max(heap.length * 2, neededCapacity));
        }

        // Добавляем все элементы
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
        boolean modified = false;
        // Создаем временный массив для элементов, которые нужно сохранить
        Object[] temp = new Object[size];
        int newSize = 0;

        // Собираем элементы, которых нет в коллекции c
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                temp[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            // Заменяем старую кучу новой и перестраиваем ее
            System.arraycopy(temp, 0, heap, 0, newSize);
            size = newSize;
            // Перестраиваем кучу
            heapify();
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        // Создаем временный массив для элементов, которые нужно сохранить
        Object[] temp = new Object[size];
        int newSize = 0;

        // Собираем элементы, которые есть в коллекции c
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                temp[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            // Заменяем старую кучу новой и перестраиваем ее
            System.arraycopy(temp, 0, heap, 0, newSize);
            size = newSize;
            // Перестраиваем кучу
            heapify();
        }

        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы кучи                ///////
    /////////////////////////////////////////////////////////////////////////

    // Сравнение двух элементов
    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        } else {
            return ((Comparable<? super E>) a).compareTo(b);
        }
    }

    // Безопасное сравнение объектов
    private boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    // Просеивание вверх (при добавлении)
    private void siftUp(int index) {
        E element = heap[index];
        while (index > 0) {
            int parentIndex = (index - 1) >>> 1; // деление на 2
            E parent = heap[parentIndex];

            if (compare(element, parent) >= 0) {
                break; // свойство кучи восстановлено
            }

            // Меняем местами с родителем
            heap[index] = parent;
            index = parentIndex;
        }
        heap[index] = element;
    }

    // Просеивание вниз (при удалении)
    private void siftDown(int index) {
        E element = heap[index];
        int half = size >>> 1; // пока есть потомки

        while (index < half) {
            int leftChild = (index << 1) + 1;
            int rightChild = leftChild + 1;
            int smallest = leftChild;

            // Выбираем наименьшего потомка
            if (rightChild < size && compare(heap[rightChild], heap[leftChild]) < 0) {
                smallest = rightChild;
            }

            // Если текущий элемент меньше наименьшего потомка - выходим
            if (compare(element, heap[smallest]) <= 0) {
                break;
            }

            // Меняем местами с наименьшим потомком
            heap[index] = heap[smallest];
            index = smallest;
        }
        heap[index] = element;
    }

    // Перестроение всей кучи
    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    // Удаление элемента по индексу
    private void removeAt(int index) {
        if (index == size - 1) {
            // Удаляем последний элемент
            heap[size - 1] = null;
            size--;
        } else {
            // Заменяем удаляемый элемент последним
            heap[index] = heap[size - 1];
            heap[size - 1] = null;
            size--;

            // Восстанавливаем свойства кучи
            if (index > 0 && compare(heap[index], heap[(index - 1) >>> 1]) < 0) {
                siftUp(index);
            } else {
                siftDown(index);
            }
        }
    }

    // Увеличение массива
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = heap.length * 2;
        E[] newHeap = (E[]) new Object[newCapacity];
        System.arraycopy(heap, 0, newHeap, 0, size);
        heap = newHeap;
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        E[] newHeap = (E[]) new Object[newCapacity];
        System.arraycopy(heap, 0, newHeap, 0, size);
        heap = newHeap;
    }

    /////////////////////////////////////////////////////////////////////////
    //////       Остальные методы Queue - можно оставить пустыми     ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public E remove() {
        E result = poll();
        if (result == null) {
            throw new NoSuchElementException("Queue is empty");
        }
        return result;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return heap[currentIndex++];
            }
        };
    }

    @Override
    public Object[] toArray() {
        return java.util.Arrays.copyOf(heap, size);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return java.util.Arrays.copyOf(heap, size, (Class<? extends T[]>) a.getClass());
        }
        System.arraycopy(heap, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
}