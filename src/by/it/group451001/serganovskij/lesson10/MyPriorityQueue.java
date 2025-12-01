package by.it.group451001.serganovskij.lesson10;

import java.util.Queue;
import java.util.Collection;
import java.util.NoSuchElementException;

public class MyPriorityQueue<E extends Comparable<? super E>> implements Queue<E> {
    // Основной массив для хранения элементов кучи
    private E[] heap;
    // Текущее количество элементов в куче
    private int size;
    // Начальная емкость по умолчанию
    private static final int DEFAULT_CAP = 16;

    // Конструктор с указанием начальной емкости
    @SuppressWarnings("unchecked")
    public MyPriorityQueue(int capacity) {
        // Проверка на минимальную емкость
        if (capacity < 1) capacity = DEFAULT_CAP;
        // Создание массива с приведением типа (дженерики в Java)
        heap = (E[]) new Comparable[capacity];
        size = 0;
    }

    // Конструктор по умолчанию
    public MyPriorityQueue() {
        this(DEFAULT_CAP);
    }

    // Получение текущей емкости массива
    private int cap() { return heap.length; }

    // Увеличение емкости массива в 2 раза
    @SuppressWarnings("unchecked")
    private void grow() {
        int n = cap() << 1; // Удвоение емкости
        E[] nh = (E[]) new Comparable[n];
        // Копирование элементов в новый массив
        for (int i = 0; i < size; i++) nh[i] = heap[i];
        heap = nh;
    }

    // Обмен элементов по индексам
    private void swap(int i, int j) {
        E t = heap[i]; heap[i] = heap[j]; heap[j] = t;
    }

    // Просеивание вверх (для восстановления свойств кучи после добавления)
    private void siftUp(int idx) {
        E val = heap[idx];
        while (idx > 0) {
            int p = (idx - 1) >>> 1; // Индекс родителя
            // Если родитель меньше или равен текущему элементу - остановка
            if (heap[p].compareTo(val) <= 0) break;
            heap[idx] = heap[p]; // Перемещаем родителя вниз
            idx = p; // Переходим к родителю
        }
        heap[idx] = val; // Устанавливаем значение на правильную позицию
    }

    // Просеивание вниз (для восстановления свойств кучи после удаления)
    private void siftDown(int idx) {
        E val = heap[idx];
        int half = size >>> 1; // Индекс последнего нелистового узла
        while (idx < half) {
            int l = (idx << 1) + 1; // Левый потомок
            int r = l + 1;          // Правый потомок
            int smallest = l;       // Предполагаем, что левый - наименьший

            // Если правый потомок существует и меньше левого
            if (r < size && heap[r].compareTo(heap[l]) < 0) smallest = r;

            // Если наименьший потомок больше текущего значения - остановка
            if (heap[smallest].compareTo(val) >= 0) break;

            heap[idx] = heap[smallest]; // Перемещаем потомка вверх
            idx = smallest;             // Переходим к потомку
        }
        heap[idx] = val; // Устанавливаем значение на правильную позицию
    }

    // Удаление элемента по индексу с восстановлением свойств кучи
    private E removeAt(int idx) {
        E removed = heap[idx]; // Сохраняем удаляемый элемент
        int last = --size;     // Уменьшаем размер и запоминаем индекс последнего

        // Если удаляем последний элемент - просто обнуляем
        if (idx == last) {
            heap[idx] = null;
            return removed;
        }

        E moved = heap[last]; // Берем последний элемент
        heap[last] = null;    // Обнуляем последнюю позицию
        heap[idx] = moved;    // Перемещаем последний элемент на место удаляемого

        int parent = (idx - 1) >>> 1;
        // Если перемещенный элемент меньше родителя - просеиваем вверх
        if (idx > 0 && heap[idx].compareTo(heap[parent]) < 0) {
            siftUp(idx);
        } else {
            // Иначе - просеиваем вниз
            siftDown(idx);
        }
        return removed;
    }

    // Строковое представление кучи
    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            sb.append(String.valueOf(heap[i]));
            if (i != size - 1) sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    // Возвращает количество элементов
    @Override
    public int size() { return size; }

    // Очищает кучу
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) heap[i] = null;
        size = 0;
    }

    // Добавляет элемент в кучу
    @Override
    public boolean add(E element) {
        if (element == null) throw new NullPointerException();
        if (size == cap()) grow(); // Увеличиваем емкость при необходимости
        heap[size] = element;
        siftUp(size); // Восстанавливаем свойства кучи
        size++;
        return true;
    }

    // Альтернативный метод добавления (синоним add)
    @Override
    public boolean offer(E e) { return add(e); }

    // Извлекает минимальный элемент без исключения
    @Override
    public E poll() {
        if (size == 0) return null;
        E res = heap[0];
        removeAt(0); // Удаляем корневой элемент
        return res;
    }

    // Просмотр минимального элемента без извлечения
    @Override
    public E peek() { return size == 0 ? null : heap[0]; }

    // Просмотр минимального элемента с исключением
    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return heap[0];
    }

    // Проверка на пустоту
    @Override
    public boolean isEmpty() { return size == 0; }

    // Извлечение минимального элемента с исключением
    @Override
    public E remove() {
        E r = poll();
        if (r == null) throw new NoSuchElementException();
        return r;
    }

    // Проверка наличия элемента
    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        // Линейный поиск по массиву
        for (int i = 0; i < size; i++) if (o.equals(heap[i])) return true;
        return false;
    }

    // Проверка наличия всех элементов коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object e : c) if (!contains(e)) return false;
        return true;
    }

    // Добавление всех элементов коллекции
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        boolean modified = false;
        for (E e : c) { add(e); modified = true; }
        return modified;
    }

    // Удаление всех элементов, присутствующих в коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        int j = 0;
        // Фильтрация: оставляем только элементы, которых нет в коллекции c
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                heap[j++] = heap[i];
            }
        }
        if (j == size) return false; // Ничего не изменилось

        // Очистка оставшихся позиций
        for (int k = j; k < size; k++) heap[k] = null;
        size = j;

        // Восстановление свойств кучи (построение кучи за O(n))
        for (int k = (size >>> 1) - 1; k >= 0; k--) siftDown(k);
        return true;
    }

    // Удаление всех элементов, кроме присутствующих в коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        int j = 0;
        // Фильтрация: оставляем только элементы, которые есть в коллекции c
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                heap[j++] = heap[i];
            }
        }
        if (j == size) return false; // Ничего не изменилось

        // Очистка оставшихся позиций
        for (int k = j; k < size; k++) heap[k] = null;
        size = j;

        // Восстановление свойств кучи
        for (int k = (size >>> 1) - 1; k >= 0; k--) siftDown(k);
        return true;
    }

    /////////////////////////////////////////////////////////////////////////
    //////          Методы, которые не требуются для задания          ///////
    /////////////////////////////////////////////////////////////////////////

    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean equals(Object o) { return super.equals(o); }
    @Override public int hashCode() { return super.hashCode(); }
}