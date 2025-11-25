package by.it.group410901.zdanovich.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

// Реализация приоритетной очереди (минимальной кучи) через массив
public class MyPriorityQueue<E> implements Deque<E> {

    private int size = 0;     // текущее количество элементов в очереди
    private E[] heap = null;  // массив, реализующий структуру min-heap

    // -------------------- Поддержка структуры кучи --------------------

    // Метод "просеивания вниз" — восстанавливает свойство кучи после удаления элемента
    private void siftDown(int i) {
        int left = 2 * i + 1;   // индекс левого потомка
        int right = 2 * i + 2;  // индекс правого потомка
        int j = left;           // предполагаем, что минимальный потомок — левый

        // если правый потомок существует и меньше левого — выбираем правого
        if (right < size && ((Comparable<? super E>) heap[right]).compareTo(heap[j]) < 0)
            j = right;

        // если выбранный потомок меньше родителя — меняем их местами
        if (j < size && ((Comparable<? super E>) heap[j]).compareTo(heap[i]) < 0) {
            E temp = heap[i];   // сохраняем родителя во временную переменную
            heap[i] = heap[j];  // потомок поднимается вверх
            heap[j] = temp;     // родитель опускается вниз
            siftDown(j);        // рекурсивно продолжаем просеивание вниз
        }
    }

    // Метод "просеивания вверх" — восстанавливает свойство кучи после добавления нового элемента
    private void siftUp(int i) {
        int parent = (i - 1) / 2; // индекс родителя текущего элемента
        // если элемент меньше родителя — поднимаем его вверх
        if (i > 0 && ((Comparable<? super E>) heap[i]).compareTo(heap[parent]) < 0) {
            E temp = heap[i];         // сохраняем текущий элемент
            heap[i] = heap[parent];   // опускаем родителя вниз
            heap[parent] = temp;      // поднимаем текущий элемент
            siftUp(parent);           // продолжаем подниматься вверх
        }
    }

    // -------------------- Основные методы --------------------

    // Возвращает текущее количество элементов в очереди
    @Override
    public int size() {
        return size;
    }

    // Полностью очищает очередь
    @Override
    public void clear() {
        size = 0;     // обнуляем размер
        heap = null;  // удаляем ссылку на массив
    }

    // Добавление нового элемента в очередь
    @Override
    public boolean add(E e) {
        if (e == null) return false; // не допускаем добавления null
        size++;                      // увеличиваем размер кучи

        // создаем новый массив на один элемент больше
        E[] newHeap = (E[]) new Object[size];
        // копируем старые элементы (если есть)
        for (int i = 0; i < size - 1; i++) {
            newHeap[i] = heap == null ? null : heap[i];
        }
        newHeap[size - 1] = e; // вставляем новый элемент в конец массива
        heap = newHeap;        // заменяем старый массив новым

        siftUp(size - 1);      // восстанавливаем свойство кучи после вставки
        return true;           // элемент успешно добавлен
    }

    // Удаление и возвращение минимального элемента (корня кучи)
    @Override
    public E remove() {
        if (size == 0) return null; // если очередь пуста — возвращаем null
        E min = heap[0];            // сохраняем минимальный элемент (корень)
        size--;                     // уменьшаем количество элементов

        // создаем новый массив уменьшенного размера
        E[] newHeap = (E[]) new Object[size];
        if (size > 0) {
            newHeap[0] = heap[heap.length - 1]; // последний элемент переносим в корень
            // копируем все элементы кроме последнего
            for (int i = 1; i < size; i++) {
                newHeap[i] = heap[i];
            }
            heap = newHeap;  // обновляем массив
            siftDown(0);     // восстанавливаем свойство min-heap
        } else {
            heap = null;     // если очередь опустела — очищаем ссылку
        }
        return min;          // возвращаем удалённый минимальный элемент
    }

    // Просмотр минимального элемента без удаления
    @Override
    public E peek() {
        if (size == 0) return null; // если очередь пуста
        return heap[0];             // возвращаем корень (минимум)
    }

    // Аналог peek() — возвращает первый (наименьший) элемент
    @Override
    public E element() {
        return peek();
    }

    // Удаляет и возвращает минимальный элемент (синоним remove())
    @Override
    public E poll() {
        return remove();
    }

    // Добавление элемента (синоним add())
    @Override
    public boolean offer(E e) {
        return add(e);
    }

    // Проверка, пуста ли очередь
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Проверка, содержит ли очередь заданный элемент
    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {                  // проходим по всем элементам
            if (heap[i] != null && heap[i].equals(o))     // сравниваем по equals()
                return true;                              // если нашли — возвращаем true
        }
        return false;                                     // иначе — false
    }

    // -------------------- Операции с коллекциями --------------------

    // Проверка, содержит ли очередь все элементы другой коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {            // для каждого элемента из коллекции
            if (!contains(o))           // если хотя бы один отсутствует
                return false;           // возвращаем false
        }
        return true;                    // все найдены — возвращаем true
    }

    // Добавление всех элементов из другой коллекции
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;             // флаг, изменилось ли множество
        for (E e : c) {                       // для каждого элемента
            if (add(e)) modified = true;      // добавляем его и отмечаем изменение
        }
        return modified;                      // возвращаем результат
    }

    // Удаление всех элементов, которые есть в коллекции c
    @Override
    public boolean removeAll(Collection<?> c) {
        if (isEmpty()) return false;           // если очередь пуста — ничего не делаем

        int newSize = 0;                      // новый размер после удаления
        E[] temp = (E[]) new Object[size];    // временный массив

        // Копируем только элементы, которых нет в коллекции c
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                temp[newSize++] = heap[i];
            }
        }

        boolean changed = newSize != size;    // проверяем, были ли изменения
        size = newSize;                       // обновляем размер

        heap = (E[]) new Object[size];        // создаем новый массив под кучу
        for (int i = 0; i < size; i++) heap[i] = temp[i]; // копируем оставшиеся элементы
        for (int i = size / 2; i >= 0; i--) siftDown(i);  // пересобираем кучу

        return changed;                       // возвращаем true, если были изменения
    }

    // Оставляет только те элементы, которые присутствуют в коллекции c
    @Override
    public boolean retainAll(Collection<?> c) {
        if (isEmpty()) return false;          // если пусто — ничего не делаем

        int newSize = 0;                      // новый размер
        E[] temp = (E[]) new Object[size];    // временный массив

        // сохраняем только элементы, которые есть в коллекции c
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                temp[newSize++] = heap[i];
            }
        }

        boolean changed = newSize != size;    // проверяем, изменился ли размер
        size = newSize;                       // обновляем размер

        heap = (E[]) new Object[size];        // создаем новый массив
        for (int i = 0; i < size; i++) heap[i] = temp[i]; // копируемэлементы
        for (int i = size / 2; i >= 0; i--) siftDown(i);  // пересобираем кучу

        return changed;                       // возвращаем true, если были изменения
    }

    // -------------------- Дополнительные методы --------------------

    // Преобразует очередь в строку формата [a, b, c]
    public String toString() {
        StringBuilder sb = new StringBuilder("["); // начинаем формировать строку
        for (int i = 0; i < size; i++) {           // проходим по всем элементам
            sb.append(heap[i]);                    // добавляем элемент
            if (i < size - 1) sb.append(", ");     // добавляем запятую между элементами
        }
        sb.append("]");                            // закрываем скобку
        return sb.toString();                      // возвращаем итоговую строку
    }

    // -------------------- Методы-заглушки --------------------
    // (не используются в данной реализации Deque)
    @Override public void addFirst(E e) {}
    @Override public void addLast(E e) {}
    @Override public boolean offerFirst(E e) { return false; }
    @Override public boolean offerLast(E e) { return false; }
    @Override public E removeFirst() { return null; }
    @Override public E removeLast() { return null; }
    @Override public E pollFirst() { return null; }
    @Override public E pollLast() { return null; }
    @Override public E getFirst() { return null; }
    @Override public E getLast() { return null; }
    @Override public E peekFirst() { return null; }
    @Override public E peekLast() { return null; }
    @Override public boolean removeFirstOccurrence(Object o) { return false; }
    @Override public boolean removeLastOccurrence(Object o) { return false; }
    @Override public void push(E e) {}
    @Override public E pop() { return null; }
    @Override public boolean remove(Object o) { return false; }
    @Override public Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return new Object[0]; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public Iterator<E> descendingIterator() { return null; }
}
