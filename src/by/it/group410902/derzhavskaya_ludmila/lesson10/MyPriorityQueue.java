package by.it.group410902.derzhavskaya_ludmila.lesson10;

import java.util.Collection;
import java.util.Queue;
import java.util.NoSuchElementException;
//куча

public class MyPriorityQueue<E> implements Queue<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private E[] heap;           // Массив для хранения элементов кучи
    private int size;           // Текущее количество элементов

    // Конструктор
    public MyPriorityQueue() {
        heap = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }


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

    // Возвращает количество элементов в очереди
    @Override
    public int size() {
        return size;
    }

    // Удаляет все элементы из очереди
    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;  // Помогаем сборщику мусора
        }
        size = 0;
    }

    // Добавляет элемент в очередь (аналогично offer)
    @Override
    public boolean add(E element) {
        return offer(element);
    }

    // Удаляет и возвращает головной элемент очереди (аналогично poll, но бросает исключение если очередь пуста)
    @Override
    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException("Queue is empty");
        }
        return poll();
    }

    // Проверяет, содержится ли указанный элемент в очереди
    @Override
    public boolean contains(Object element) {
        // Линейный поиск по массиву кучи
        for (int i = 0; i < size; i++) {
            if (java.util.Objects.equals(element, heap[i])) {
                return true;
            }
        }
        return false;
    }

    // Добавляет элемент в очередь
    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }

        // Увеличиваем массив если необходимо
        if (size >= heap.length) {
            int newCapacity = heap.length * 2;
            E[] newHeap = (E[]) new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newHeap[i] = heap[i];
            }
            heap = newHeap;
        }

        // Добавляем элемент в конец кучи
        heap[size] = element;
        // Восстанавливаем свойства кучи, поднимая элемент на нужную позицию
        siftUp(size);
        size++;
        return true;
    }

    // Удаляет и возвращает головной элемент очереди (минимальный элемент)
    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }

        // Сохраняем корневой элемент
        E result = heap[0];

        // Перемещаем последний элемент в корень
        heap[0] = heap[size - 1];
        heap[size - 1] = null;  // Очищаем последнюю позицию
        size--;

        // Восстанавливаем свойства кучи, опуская новый корень на нужную позицию
        if (size > 0) {
            siftDown(0);
        }

        return result;
    }

    // Возвращает головной элемент очереди
    @Override
    public E peek() {
        return (size == 0) ? null : heap[0];
    }

    // Возвращает головной элемент очереди (бросает исключение если очередь пуста)
    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException("Queue is empty");
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

    // Добавляет все элементы указанной коллекции в очередь
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean p = false;
        for (E element : c) {
            if (offer(element)) {
                p = true;
            } else{
                p=false;
            }
        }
        return p;
    }

    // Удаляет из очереди все элементы, которые содержатся в указанной коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        int removed = 0;
        for (int i = 0, j = 0; i < size; i++) {
            if (c.contains(heap[i]))
                removed++;
            else
                heap[j++] = heap[i];
        }
        if (removed == 0) {
            return false;
        }
        size -= removed;
        siftRestore();
        return true;
    }

    // Удаляет из очереди все элементы, которые НЕ содержатся в указанной коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        int removed = 0;
        for (int i = 0, j = 0; i < size; i++) {
            if (c.contains(heap[i]))
                heap[j++] = heap[i];
            else
                removed++;
        }
        if (removed == 0) {
            return false;
        }
        size -= removed;
        siftRestore();
        return true;
    }

    //////       Вспомогательные методы для работы с кучей


    private int compare(E a, E b) {
        return ((Comparable<E>) a).compareTo(b);
    }

    // Поднимает элемент на нужную позицию в куче (при добавлении)
    private void siftUp(int index) {
        int child = index;
        int parent = (child - 1) / 2;

        // Пока не дошли до корня и ребенок меньше родителя
        while (child > 0 && compare(heap[child], heap[parent]) < 0) {
            E temp = heap[parent];
            heap[parent] = heap[child];
            heap[child] = temp;

            // Переходим на уровень выше
            child = parent;
            parent = (child - 1) / 2;
        }
    }

    // Опускает элемент на нужную позицию в куче (при удалении)
    private void siftDown(int index) {
        int parent = index;

        while (true) {
            int leftChild = 2 * parent + 1;
            int rightChild = 2 * parent + 2;
            int smallest = parent;

            // Сравниваем с левым ребенком
            if (leftChild < size && compare(heap[leftChild], heap[smallest]) < 0) {
                smallest = leftChild;
            }

            // Сравниваем с правым ребенком
            if (rightChild < size && compare(heap[rightChild], heap[smallest]) < 0) {
                smallest = rightChild;
            }

            // Если родитель уже на правильной позиции, выходим
            if (smallest == parent) {
                break;
            }

            // Меняем местами родителя и наименьшего ребенка
            E temp = heap[parent];
            heap[parent] = heap[smallest];
            heap[smallest] = temp;

            // Переходим к следующему уровню
            parent = smallest;
        }
    }

    private void siftRestore() {
        for(int i = size / 2; i >= 0; i--) {
            siftDown(i);
        }
    }

    //////   Остальные методы интерфейса Queue должны быть реаизованы

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public java.util.Iterator<E> iterator() {
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
}