package by.it.group410901.abakumov.lesson10;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;


@SuppressWarnings("unchecked")
public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {
    private E[] heap; // Массив, представляющий мин-кучу (бинарную кучу с минимальным элементом в корне)
    private int size; // Текущее количество элементов в очереди

    public MyPriorityQueue() {
        heap = (E[]) new Comparable[16]; // Инициализация массива кучи
        size = 0; // Установка начального размера в 0
    }

    //================ ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ===================
    private void ensureCapacity() {
        // Проверяем, нужно ли расширять массив
        if (size >= heap.length) {
            E[] newHeap = (E[]) new Comparable[heap.length * 2]; // Создаем новый массив вдвое большего размера
            for (int i = 0; i < size; i++) newHeap[i] = heap[i]; // Копируем существующие элементы
            heap = newHeap; // Заменяем старый массив на новый
        }
    }

    private void swap(int i, int j) {
        // Меняем местами два элемента в куче по индексам i и j
        E tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    private void heapifyUp(int index) {
        // Восстанавливаем свойство кучи вверх: поднимаем элемент, если он меньше родителя
        while (index > 0) {
            int parent = (index - 1) / 2; // Вычисляем индекс родителя
            if (heap[index].compareTo(heap[parent]) >= 0) break; // Если элемент не меньше родителя, останавливаемся
            swap(index, parent); // Меняем местами с родителем
            index = parent; // Переходим к родителю
        }
    }

    private void heapifyDown(int index) {
        // Восстанавливаем свойство кучи вниз: опускаем элемент, если он больше наименьшего потомка
        while (true) {
            int left = index * 2 + 1; // Индекс левого потомка
            int right = index * 2 + 2; // Индекс правого потомка
            int smallest = index; // Предполагаем, что текущий - наименьший
            if (left < size && heap[left].compareTo(heap[smallest]) < 0) smallest = left; // Сравниваем с левым
            if (right < size && heap[right].compareTo(heap[smallest]) < 0) smallest = right; // Сравниваем с правым
            if (smallest == index) break; // Если текущий наименьший, останавливаемся
            swap(index, smallest); // Меняем с наименьшим потомком
            index = smallest; // Переходим к потомку
        }
    }

    //================= ОСНОВНЫЕ МЕТОДЫ ======================
    @Override
    public int size() {
        return size; // Возвращаем текущий размер очереди
    }

    @Override
    public boolean isEmpty() {
        return size == 0; // Проверяем, пуста ли очередь
    }

    @Override
    public boolean add(E element) {
        if (element == null) throw new NullPointerException(); // Запрещаем null-элементы
        ensureCapacity(); // Убеждаемся, что есть место в массиве
        heap[size] = element; // Добавляем элемент в конец кучи
        heapifyUp(size); // Восстанавливаем кучу вверх
        size++; // Увеличиваем размер
        return true; // add всегда возвращает true по контракту Queue
    }

    @Override
    public boolean offer(E element) {
        return add(element); // offer аналогичен add
    }

    @Override
    public E remove() {
        if (isEmpty()) throw new NoSuchElementException(); // Если пусто, бросаем исключение
        return poll(); // Иначе удаляем и возвращаем минимум
    }

    @Override
    //Извлекает и удаляет головной элемент очереди.
    public E poll() {
        if (isEmpty()) return null; // Если пусто, возвращаем null
        E result = heap[0]; // Сохраняем корень (минимальный элемент)
        heap[0] = heap[size - 1]; // Перемещаем последний элемент в корень
        heap[size - 1] = null; // Очищаем последний слот
        size--; // Уменьшаем размер
        if (size > 0) heapifyDown(0); // Восстанавливаем кучу вниз, если не пусто
        return result; // Возвращаем удаленный элемент
    }

    @Override
    public E element() {
        if (isEmpty()) throw new NoSuchElementException(); // Если пусто, бросаем исключение
        return heap[0]; // Возвращаем корень без удаления
    }

    @Override
    public E peek() {
        return isEmpty() ? null : heap[0]; // Возвращаем корень или null, если пусто
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) heap[i] = null; // Обнуляем все элементы
        size = 0; // Сбрасываем размер
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (heap[i].equals(o)) return true; // Линейный поиск по куче
        }
        return false;
    }

    //================= КОЛЛЕКЦИОННЫЕ МЕТОДЫ ====================
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false; // Проверяем наличие каждого элемента коллекции
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            add(e); // Добавляем каждый элемент по отдельности
            modified = true; // Если коллекция не пуста, будет модификация
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                heap[newSize++] = heap[i]; // Сохраняем элементы, не входящие в c
            } else {
                modified = true; // Отмечаем удаление
            }
        }
        for (int i = newSize; i < size; i++) heap[i] = null; // Очищаем хвост
        size = newSize; // Обновляем размер
        // Восстановим свойство кучи: просеиваем вниз от последних родителей
        for (int i = size / 2 - 1; i >= 0; i--) {
            heapifyDown(i); // Это строит кучу заново для оставшихся элементов
        }
        return modified;
    }

    @Override
    //Пересечение множеств
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                heap[newSize++] = heap[i]; // Сохраняем только элементы из c
            } else {
                modified = true; // Отмечаем удаление
            }
        }
        for (int i = newSize; i < size; i++) heap[i] = null; // Очищаем хвост
        size = newSize; // Обновляем размер
        // Перестраиваем кучу: аналогично removeAll, просеиваем вниз
        for (int i = size / 2 - 1; i >= 0; i--) {
            heapifyDown(i);
        }
        return modified;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]); // Добавляем элементы в строку
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString(); // Возвращаем строковое представление кучи
    }

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
        return false;
    }
}