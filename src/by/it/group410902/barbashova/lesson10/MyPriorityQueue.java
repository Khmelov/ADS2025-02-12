package by.it.group410902.barbashova.lesson10;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyPriorityQueue<E> implements Deque<E> {

    private int size = 0;        // Текущее количество элементов в куче
    private E[] heap = null;     // Массив для хранения бинарной кучи

    // Просеивание элемента вниз для восстановления свойств кучи
    private void siftDown(int i) {
        int left = 2 * i + 1;    // Индекс левого потомка
        int right = 2 * i + 2;   // Индекс правого потомка
        int j = left;            // Начинаем с левого потомка

        // Если правый потомок существует и меньше левого, выбираем его
        if (right < size && ((Comparable<? super E>)heap[right]).compareTo(heap[j]) < 0)
            j = right;

        // Если выбранный потомок меньше текущего элемента, меняем их местами
        if (j < size && ((Comparable<? super E>)heap[j]).compareTo(heap[i]) < 0) {
            E temp = heap[i];
            heap[i] = heap[j];
            heap[j] = temp;
            siftDown(j);  // Рекурсивно продолжаем просеивание
        }
    }

    // Просеивание элемента вверх для восстановления свойств кучи
    private void siftUp(int i) {
        // Если текущий элемент меньше своего родителя
        if (((Comparable<? super E>)heap[i]).compareTo(heap[(i - 1) / 2]) < 0) {
            // Меняем местами с родителем
            E temp = heap[i];
            heap[i] = heap[(i - 1) / 2];
            heap[(i - 1) / 2] = temp;
            siftUp((i - 1) / 2);  // Рекурсивно продолжаем просеивание вверх
        }
    }

    // Представление очереди в виде строки
    public String toString() {
        String res = "[";
        for (int i = 0; i < size - 1; i++) {
            res += heap[i] + ", ";
        }
        if (size != 0) {
            res += heap[size - 1];
        }
        res += "]";
        return res;
    }

    // Возвращает количество элементов в очереди
    @Override
    public int size() {
        return size;
    }

    // Очищает очередь
    @Override
    public void clear() {
        size = 0;
        heap = null;
    }

    // Добавляет элемент в очередь
    @Override
    public boolean add(E e) {
        if (e == null) return false;
        size++;
        // Создаем новый массив на 1 элемент больше
        E[] newheap = (E[]) new Object[size];
        // Копируем старые элементы
        for (int i = 0; i < size - 1; i++) {
            newheap[i] = heap[i];
        }
        // Добавляем новый элемент в конец
        newheap[size - 1] = e;
        heap = newheap;
        // Просеиваем новый элемент вверх для восстановления кучи
        siftUp(size - 1);
        return true;
    }

    // Удаляет и возвращает минимальный элемент (корень кучи)
    @Override
    public E remove() {
        if (size == 0) return null;
        size--;
        // Создаем новый массив на 1 элемент меньше
        E[] newheap = (E[]) new Object[size];
        // Перемещаем последний элемент в корень
        newheap[0] = heap[size];
        // Копируем остальные элементы
        for (int i = 1; i < size; i++) {
            newheap[i] = heap[i];
        }
        E res = heap[0];  // Сохраняем результат (старый корень)
        heap = newheap;
        // Просеиваем новый корень вниз для восстановления кучи
        siftDown(0);
        return res;
    }

    // Проверяет, содержится ли элемент в очереди
    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (heap[i] == o) {
                return true;
            }
        }
        return false;
    }

    // Добавляет элемент (аналогично add)
    @Override
    public boolean offer(E e) {
        return add(e);
    }

    // Удаляет и возвращает минимальный элемент (аналогично remove)
    @Override
    public E poll() {
        return remove();
    }

    // Возвращает минимальный элемент без удаления
    @Override
    public E peek() {
        if (size == 0) return null;
        return heap[0];
    }

    // Возвращает минимальный элемент без удаления (аналогично peek)
    @Override
    public E element() {
        return peek();
    }

    // Проверяет, пуста ли очередь
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Проверяет, содержатся ли все элементы коллекции в очереди
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o: c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    // Добавляет все элементы коллекции в очередь
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean res = false;
        for (Object o: c) {
            res = add((E)o);
        }
        return res;
    }

    // Удаляет все элементы, которые содержатся в указанной коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        int newSize = 0;
        E[] temp = (E[]) new Object[size];
        // Копируем только те элементы, которых нет в коллекции c
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                temp[newSize++] = heap[i];
            }
        }
        boolean changed = newSize != size;
        size = newSize;
        // Создаем новую кучу
        E[] newheap = (E[]) new Object[size];
        for (int i = 0; i < size; i++) {
            newheap[i] = temp[i];
        }
        heap = newheap;
        // Восстанавливаем свойства кучи
        for (int i = size / 2; i >= 0; i--) {
            siftDown(i);
        }
        return changed;
    }

    // Удаляет все элементы, кроме тех, которые содержатся в указанной коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        int newSize = 0;
        E[] temp = (E[]) new Object[size];
        // Копируем только те элементы, которые есть в коллекции c
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                temp[newSize++] = heap[i];
            }
        }
        boolean changed = newSize != size;
        size = newSize;
        // Создаем новую кучу
        E[] newheap = (E[]) new Object[size];
        for (int i = 0; i < size; i++) {
            newheap[i] = temp[i];
        }
        heap = newheap;
        // Восстанавливаем свойства кучи
        for (int i = size / 2; i >= 0; i--) {
            siftDown(i);
        }
        return changed;
    }

    // =========================================================================
    // Следующие методы не реализованы, так как это PriorityQueue
    // а не полноценный Deque (двусторонняя очередь)
    // =========================================================================

    @Override
    public void addFirst(E e) {
        // Не поддерживается для приоритетной очереди
    }

    @Override
    public void addLast(E e) {
        // Не поддерживается для приоритетной очереди
    }

    @Override
    public boolean offerFirst(E e) {
        return false;
    }

    @Override
    public boolean offerLast(E e) {
        return false;
    }

    @Override
    public E removeFirst() {
        return null;
    }

    @Override
    public E removeLast() {
        return null;
    }

    @Override
    public E pollFirst() {
        return null;
    }

    @Override
    public E pollLast() {
        return null;
    }

    @Override
    public E getFirst() {
        return null;
    }

    @Override
    public E getLast() {
        return null;
    }

    @Override
    public E peekFirst() {
        return null;
    }

    @Override
    public E peekLast() {
        return null;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override
    public void push(E e) {
        // Не поддерживается для приоритетной очереди
    }

    @Override
    public E pop() {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }
}