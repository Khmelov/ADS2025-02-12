package by.it.group410901.kalach.lesson10;

import java.util.Queue;
import java.util.Collection;
import java.util.NoSuchElementException;

// Приоритетная очередь на основе минимальной двоичной кучи
// Элементы должны быть сравнимыми (Comparable)
public class MyPriorityQueue<E extends Comparable<? super E>> implements Queue<E> {
    private E[] heap;        // Массив для хранения кучи
    private int size;        // Текущее количество элементов
    private static final int DEFAULT_CAP = 16;  // Начальная емкость по умолчанию

    // Конструктор с заданной емкостью
    @SuppressWarnings("unchecked")
    public MyPriorityQueue(int capacity) {
        if (capacity < 1) capacity = DEFAULT_CAP;  // Минимальная емкость
        heap = (E[]) new Comparable[capacity];
        size = 0;
    }

    // Конструктор по умолчанию
    public MyPriorityQueue() {
        this(DEFAULT_CAP);
    }

    // Возвращает текущую емкость массива
    private int cap() { return heap.length; }

    // Увеличивает размер массива в 2 раза
    @SuppressWarnings("unchecked")
    private void grow() {
        int n = cap() << 1;  // Новая емкость = старая * 2 (битовый сдвиг влево)
        E[] nh = (E[]) new Comparable[n];
        // Копируем элементы в новый массив
        for (int i = 0; i < size; i++) nh[i] = heap[i];
        heap = nh;
    }

    // Обмен элементов по индексам
    private void swap(int i, int j) {
        E t = heap[i]; heap[i] = heap[j]; heap[j] = t;
    }

    // Просеивание вверх (восстановление свойства кучи при добавлении)
    private void siftUp(int idx) {
        E val = heap[idx];  // Сохраняем значение текущего элемента
        while (idx > 0) {
            int p = (idx - 1) >>> 1;  // Индекс родителя (беззнаковый сдвиг вправо = деление на 2)
            // Если родитель меньше или равен текущему, куча упорядочена
            if (heap[p].compareTo(val) <= 0) break;
            // Перемещаем родителя вниз
            heap[idx] = heap[p];
            idx = p;  // Переходим к родителю
        }
        heap[idx] = val;  // Устанавливаем значение на правильную позицию
    }

    // Просеивание вниз (восстановление свойства кучи при удалении)
    private void siftDown(int idx) {
        E val = heap[idx];  // Сохраняем значение текущего элемента
        int half = size >>> 1;  // Индекс последнего родителя
        while (idx < half) {
            int l = (idx << 1) + 1;  // Индекс левого потомка
            int r = l + 1;           // Индекс правого потомка
            int smallest = l;         // Предполагаем, что левый потомок наименьший

            // Если есть правый потомок и он меньше левого
            if (r < size && heap[r].compareTo(heap[l]) < 0) smallest = r;

            // Если текущий элемент меньше или равен наименьшему потомку
            if (heap[smallest].compareTo(val) >= 0) break;

            // Перемещаем наименьшего потомка вверх
            heap[idx] = heap[smallest];
            idx = smallest;  // Переходим к потомку
        }
        heap[idx] = val;  // Устанавливаем значение на правильную позицию
    }

    // Удаление элемента по индексу
    private E removeAt(int idx) {
        E removed = heap[idx];  // Сохраняем удаляемый элемент
        int last = --size;      // Уменьшаем размер и сохраняем индекс последнего

        // Если удаляем последний элемент
        if (idx == last) {
            heap[idx] = null;
            return removed;
        }

        // Перемещаем последний элемент на место удаленного
        E moved = heap[last];
        heap[last] = null;
        heap[idx] = moved;

        // Восстанавливаем свойства кучи
        int parent = (idx - 1) >>> 1;  // Индекс родителя
        if (idx > 0 && heap[idx].compareTo(heap[parent]) < 0) {
            siftUp(idx);    // Просеиваем вверх если элемент меньше родителя
        } else {
            siftDown(idx);  // Иначе просеиваем вниз
        }
        return removed;
    }

    // Преобразование очереди в строку для вывода
    @Override
    public String toString() {
        if (size == 0) return "[]";  // Пустая очередь
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            sb.append(String.valueOf(heap[i]));
            if (i != size - 1) sb.append(", ");  // Запятая между элементами
        }
        sb.append(']');
        return sb.toString();
    }

    // Возвращает количество элементов в очереди
    @Override
    public int size() { return size; }

    // Очистка очереди
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) heap[i] = null;  // Помощь сборщику мусора
        size = 0;
    }

    // Добавление элемента в очередь
    @Override
    public boolean add(E element) {
        if (element == null) throw new NullPointerException();  // Null не допускается
        if (size == cap()) grow();  // Увеличиваем массив при необходимости
        heap[size] = element;       // Добавляем в конец
        siftUp(size);               // Восстанавливаем свойства кучи
        size++;
        return true;
    }

    // Альтернативный метод добавления
    @Override
    public boolean offer(E e) { return add(e); }

    // Извлечение минимального элемента с удалением
    @Override
    public E poll() {
        if (size == 0) return null;  // Пустая очередь
        E res = heap[0];             // Минимальный элемент всегда в корне
        removeAt(0);                 // Удаляем корень
        return res;
    }

    // Просмотр минимального элемента без удаления
    @Override
    public E peek() { return size == 0 ? null : heap[0]; }

    // Просмотр минимального элемента с исключением если пусто
    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return heap[0];
    }

    // Проверка на пустоту
    @Override
    public boolean isEmpty() { return size == 0; }

    // Удаление минимального элемента с исключением если пусто
    @Override
    public E remove() {
        E r = poll();
        if (r == null) throw new NoSuchElementException();
        return r;
    }

    // Проверка наличия элемента
    @Override
    public boolean contains(Object o) {
        if (o == null) return false;  // Null не допускается
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
        for (E e : c) {
            add(e);
            modified = true;
        }
        return modified;
    }

    // Удаление всех элементов, содержащихся в коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        int j = 0;
        // Фильтруем элементы: оставляем только те, которых нет в коллекции c
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                heap[j++] = heap[i];
            }
        }
        if (j == size) return false;  // Ничего не изменилось

        // Очищаем оставшиеся ячейки
        for (int k = j; k < size; k++) heap[k] = null;
        size = j;
        // Восстанавливаем свойства кучи
        for (int k = (size >>> 1) - 1; k >= 0; k--) siftDown(k);
        return true;
    }

    // Удаление всех элементов, НЕ содержащихся в коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        int j = 0;
        // Фильтруем элементы: оставляем только те, которые есть в коллекции c
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                heap[j++] = heap[i];
            }
        }
        if (j == size) return false;  // Ничего не изменилось

        // Очищаем оставшиеся ячейки
        for (int k = j; k < size; k++) heap[k] = null;
        size = j;
        // Восстанавливаем свойства кучи
        for (int k = (size >>> 1) - 1; k >= 0; k--) siftDown(k);
        return true;
    }

    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean equals(Object o) { return super.equals(o); }
    @Override public int hashCode() { return super.hashCode(); }
}