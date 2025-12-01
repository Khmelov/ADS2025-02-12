package by.it.group410902.kovalchuck.lesson01.lesson10;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Objects;
import java.util.Arrays;

/**
 * Реализация интерфейса Queue на основе двоичной минимальной кучи.
 * Элементы хранятся в массиве и упорядочиваются согласно естественному порядку
 */
public class MyPriorityQueue<E> implements Queue<E> {
    private static final int DEFAULT_CAPACITY = 10; // Начальная емкость по умолчанию
    private E[] heap;                               // Массив для хранения элементов кучи
    private int size;                               // Текущее количество элементов
    private Comparator<? super E> comparator;       // Компаратор для сравнения элементов

    //Создает пустую очередь с приоритетами с начальной емкостью
    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
        comparator = null;
    }

    //Создает пустую очередь с приоритетами с начальной емкостью
    @SuppressWarnings("unchecked")
    public MyPriorityQueue(Comparator<? super E> comparator) {
        heap = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
        this.comparator = comparator;
    }

    //Возвращает строковое представление очереди
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

    //Возвращает количество элементов в очереди
    @Override
    public int size() {
        return size;
    }

    //Удаляет все элементы из очереди
    @Override
    public void clear() {
        // Очистка всех ссылок
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    //Добавляет элемент в очередь
    @Override
    public boolean add(E element) {
        return offer(element);
    }

    //Удаляет и возвращает головной элемент очереди
    @Override
    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return poll();
    }

    //Проверяет, содержит ли очередь указанный элемент
    @Override
    public boolean contains(Object element) {
        // Линейный поиск по массиву
        for (int i = 0; i < size; i++) {
            if (Objects.equals(element, heap[i])) {
                return true;
            }
        }
        return false;
    }

    //Добавляет элемент в очередь
    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException();
        }

        ensureCapacity(); // Проверка и увеличение емкости при необходимости
        heap[size] = element; // Добавление элемента в конец
        siftUp(size);        // Восстановление свойств кучи
        size++;
        return true;
    }

    //Удаляет и возвращает головной элемент очереди
    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }

        E result = heap[0];  // Сохранение результата
        size--;
        heap[0] = heap[size]; // Перемещение последнего элемента в корень
        heap[size] = null;    // Очистка ссылки
        siftDown(0);         // Восстановление свойств кучи
        return result;
    }

    //Возвращает головной элемент без удаления
    @Override
    public E peek() {
        return (size == 0) ? null : heap[0];
    }

    //Возвращает головной элемент без удаления
    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return heap[0];
    }

    //Проверяет, пуста ли очередь
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    //Проверяет, содержит ли очередь все элементы указанной коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    //Добавляет все элементы указанной коллекции в очередь
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

    //Удаляет из очереди все элементы, содержащиеся в указанной коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;

        // Создаем новый массив только с элементами, которые НЕ нужно удалять
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                heap[newSize++] = heap[i]; // Сохраняем элемент
            } else {
                modified = true;           // Элемент будет удален
            }
        }

        // Очищаем оставшиеся элементы (от newSize до конца)
        Arrays.fill(heap, newSize, size, null);
        size = newSize;

        // Перестраиваем кучу если были изменения
        if (modified) {
            heapify();
        }

        return modified;
    }

    //Сохраняет в очереди только элементы, содержащиеся в указанной коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;

        // Создаем новый массив только с элементами, которые нужно сохранить
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                heap[newSize++] = heap[i]; // Сохраняем элемент
            } else {
                modified = true;           // Элемент будет удален
            }
        }

        // Очищаем оставшиеся элементы
        Arrays.fill(heap, newSize, size, null);
        size = newSize;

        // Перестраиваем кучу если были изменения
        if (modified) {
            heapify();
        }

        return modified;
    }

    //Восстанавливает свойства кучи для всего массива
    private void heapify() {
        // Начинаем с последнего нелистового узла и движемся к корню
        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    //Увеличивает емкость массива при необходимости
    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == heap.length) {
            int newCapacity = heap.length * 2; // Удвоение емкости
            E[] newHeap = (E[]) new Object[newCapacity];
            System.arraycopy(heap, 0, newHeap, 0, size); // Копирование элементов
            heap = newHeap;
        }
    }

    //Сравнивает два элемента с использованием компаратора или естественного порядка
    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        // Защита от null (хотя в корректной куче null быть не должно)
        if (a == null || b == null) {
            if (a == null && b == null) return 0;
            if (a == null) return -1;
            return 1;
        }

        if (comparator != null) {
            return comparator.compare(a, b); // Использование компаратора
        } else {
            return ((Comparable<? super E>) a).compareTo(b); // Естественный порядок
        }
    }

    //Просеивание элемента вверх для восстановления свойств кучи
    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2; // Индекс родителя
            // Если элемент больше или равен родителю - остановка
            if (compare(heap[index], heap[parent]) >= 0) {
                break;
            }
            swap(index, parent); // Обмен с родителем
            index = parent;      // Переход к родителю
        }
    }

    //Просеивание элемента вниз для восстановления свойств кучи
    private void siftDown(int index) {
        while (true) {
            int left = 2 * index + 1;   // Индекс левого потомка
            int right = 2 * index + 2;  // Индекс правого потомка
            int smallest = index;       // Предполагаем, что текущий элемент наименьший

            // Сравнение с левым потомком
            if (left < size && compare(heap[left], heap[smallest]) < 0) {
                smallest = left;
            }
            // Сравнение с правым потомком
            if (right < size && compare(heap[right], heap[smallest]) < 0) {
                smallest = right;
            }
            // Если текущий элемент наименьший - остановка
            if (smallest == index) {
                break;
            }
            swap(index, smallest); // Обмен с наименьшим потомком
            index = smallest;      // Переход к потомку
        }
    }

    //Обмен двух элементов в куче
    private void swap(int i, int j) {
        E temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    //Удаление элемента по индексу с сохранением свойств кучи
    private void removeAt(int index) {
        if (index < 0 || index >= size) {
            return; // Некорректный индекс
        }

        size--;
        heap[index] = heap[size]; // Замена удаляемого элемента последним
        heap[size] = null;        // Очистка ссылки

        // Если удален последний элемент - больше ничего делать не нужно
        if (size == 0 || index == size) {
            return;
        }

        // Восстановление свойств кучи просеиванием вниз
        siftDown(index);

        // Если после просеивания вниз элемент все еще нарушает свойства кучи
        // (меньше своего родителя), то просеиваем вверх
        if (index > 0 && compare(heap[index], heap[(index - 1) / 2]) < 0) {
            siftUp(index);
        }
    }

    //Удаляет первое вхождение указанного элемента из очереди
    @Override
    public boolean remove(Object element) {
        // Линейный поиск элемента
        for (int i = 0; i < size; i++) {
            if (Objects.equals(element, heap[i])) {
                removeAt(i); // Удаление по индексу
                return true;
            }
        }
        return false;
    }

    //Возвращает итератор
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    //Возвращает массив всех элементов очереди
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        System.arraycopy(heap, 0, array, 0, size);
        return array;
    }

    //Возвращает типизированный массив
    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}