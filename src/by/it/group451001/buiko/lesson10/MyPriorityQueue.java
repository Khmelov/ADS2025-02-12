package by.it.group451001.buiko.lesson10;

import java.util.*;

public class MyPriorityQueue<E> implements Queue<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private E[] heap;
    private int size;
    private final Comparator<? super E> comparator;

    /*на основе кучи, где корень всегда содержит элемент с наивысшим приоритетом (наименьший).*/
    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
        comparator = null;
    }

    /* Для добавления элементов используется метод offer(),
   который помещает элемент в конец и просеивает вверх, восстанавливая свойства кучи.*/
    @SuppressWarnings("unchecked")
    public MyPriorityQueue(int initialCapacity) {
        if (initialCapacity < 1) throw new IllegalArgumentException();
        heap = (E[]) new Object[initialCapacity];
        size = 0;
        comparator = null;
    }

    /*Для извлечения (poll()) удаляется корневой элемент,
    а последний элемент перемещается на его место и просеивается вниз.*/
    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        }
        return ((Comparable<? super E>) a).compareTo(b);
    }


    @SuppressWarnings("unchecked")
    private void grow() {
        // Вычисляем новую емкость: увеличиваем на 100% для маленьких массивов, на 50% для больших
        // Эта стратегия балансирует между производительностью и использованием памяти
        int newCapacity = heap.length + (heap.length < 64 ? heap.length + 2 : heap.length >> 1);
        E[] newHeap = (E[]) new Object[newCapacity];
        // Копируем элементы в новый массив - сохраняем существующие данные
        System.arraycopy(heap, 0, newHeap, 0, heap.length);
        heap = newHeap;
    }

    // Просеивание вверх (восстановление свойства кучи при добавлении элемента)
    // Поднимает элемент на правильную позицию, сохраняя свойство минимальной кучи
    private void siftUp(int index) {
        // Запоминаем элемент, который нужно просеить - сохраняем значение для перемещения
        E element = heap[index];
        // Пока не дошли до корня - двигаемся вверх по дереву
        while (index > 0) {
            // Вычисляем индекс родителя - формула для бинарной кучи
            int parent = (index - 1) >>> 1;
            E parentElement = heap[parent];
            // Если элемент больше или равен родителю - свойство кучи восстановлено
            // В минимальной куче родитель всегда <= потомков
            if (compare(element, parentElement) >= 0) break;
            // Перемещаем родителя вниз - освобождаем место для элемента
            heap[index] = parentElement;
            index = parent;
        }
        // Помещаем элемент на найденную позицию - финальное размещение
        heap[index] = element;
    }

    // Просеивание вниз (восстановление свойства кучи при удалении элемента)
    // Опускает элемент на правильную позицию, сохраняя свойство минимальной кучи
    private void siftDown(int index) {
        // Запоминаем элемент, который нужно просеить - сохраняем значение для перемещения
        E element = heap[index];
        // Вычисляем индекс первого листового узла - узлы после этого индекса не имеют потомков
        int half = size >>> 1;
        // Пока узел не является листом - у него есть хотя бы один потомок
        while (index < half) {
            // Вычисляем индекс левого потомка - формула для бинарной кучи
            int child = (index << 1) + 1;
            E minChild = heap[child];
            int right = child + 1;

            // Если есть правый потомок и он меньше левого - выбираем минимального потомка
            if (right < size && compare(minChild, heap[right]) > 0) {
                child = right;
                minChild = heap[right];
            }

            // Если текущий элемент меньше или равен минимальному потомку - свойство кучи восстановлено
            if (compare(element, minChild) <= 0) break;

            // Перемещаем минимального потомка вверх - поднимаем более приоритетный элемент
            heap[index] = minChild;
            index = child;
        }
        // Помещаем элемент на найденную позицию - финальное размещение
        heap[index] = element;
    }

    // Построение кучи из произвольного массива - преобразует массив в валидную кучу
    private void heapify() {
        // Начинаем с последнего нелистового узла и идем до корня
        // Просеиваем каждый узел вниз для установления свойства кучи
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";

        // Для совместимости с тестами создаем копию и выводим в порядке массива
        // Это гарантирует, что вывод будет детерминированным и предсказуемым
        MyPriorityQueue<E> copy = new MyPriorityQueue<>();
        copy.heap = Arrays.copyOf(heap, heap.length);
        copy.size = size;

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(copy.heap[i]);
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        // Очищаем массив и сбрасываем размер - помогаем сборщику мусора
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
    public E remove() {
        E e = poll();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override
    public boolean contains(Object element) {
        // Линейный поиск элемента в массиве - O(n) сложность
        // В приоритетной очереди нет более эффективного способа поиска
        for (int i = 0; i < size; i++) {
            if (Objects.equals(element, heap[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E element) {
        if (element == null) throw new NullPointerException();

        // Увеличиваем массив при необходимости - динамическое расширение
        if (size >= heap.length) {
            grow();
        }

        // Добавляем элемент в конец и просеиваем вверх
        // Новый элемент добавляется в конец и "всплывает" на свою позицию
        heap[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) return null;

        // Извлекаем корневой элемент (минимальный) - всегда минимальный элемент в корне
        E result = heap[0];
        // Удаляем его и восстанавливаем кучу
        removeAt(0);
        return result;
    }

    @Override
    public E peek() {
        // Возвращаем корневой элемент без удаления - O(1) операция
        return (size == 0) ? null : heap[0];
    }

    @Override
    public E element() {
        E e = peek();
        if (e == null) throw new NoSuchElementException();
        return e;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // Проверяем, что все элементы коллекции содержатся в очереди
        // O(n*m) сложность в худшем случае
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // Добавляем все элементы коллекции - многократное использование offer
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
        if (c.isEmpty()) return false;

        boolean modified = false;
        // Создаем новый массив без элементов из коллекции c
        // Фильтруем элементы и перестраиваем кучу
        E[] newHeap = Arrays.copyOf(heap, heap.length);
        int newSize = 0;

        // Копируем только те элементы, которых нет в коллекции c
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                newHeap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        if (!modified) return false;

        // Обновляем кучу и перестраиваем ее - необходимо восстановить свойства кучи
        heap = newHeap;
        size = newSize;
        heapify();
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c.isEmpty()) {
            // Если коллекция пуста - очищаем очередь
            if (size > 0) {
                clear();
                return true;
            }
            return false;
        }

        boolean modified = false;
        // Создаем новый массив только с элементами из коллекции c
        // Сохраняем только элементы, присутствующие в коллекции c
        E[] newHeap = Arrays.copyOf(heap, heap.length);
        int newSize = 0;

        // Копируем только те элементы, которые есть в коллекции c
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                newHeap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        if (!modified) return false;

        // Обновляем кучу и перестраиваем ее - необходимо восстановить свойства кучи
        heap = newHeap;
        size = newSize;
        heapify();
        return true;
    }

    // Удаление элемента по индексу - внутренний метод для удаления из произвольной позиции
    private void removeAt(int index) {
        if (index >= size) return;

        // Уменьшаем размер и запоминаем последний элемент
        int last = --size;
        if (last == index) {
            // Если удаляем последний элемент - просто обнуляем
            heap[index] = null;
        } else {
            // Заменяем удаляемый элемент последним - последний элемент перемещается на место удаленного
            E moved = heap[last];
            heap[last] = null;
            heap[index] = moved;
            // Восстанавливаем свойства кучи - просеиваем вниз
            siftDown(index);
            // Если элемент не опустился вниз, пытаемся поднять его вверх
            // Это происходит если перемещенный элемент должен быть выше своей текущей позиции
            if (heap[index] == moved) {
                siftUp(index);
            }
        }
    }

    // Остальные методы интерфейса (не требуются для тестов)
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(heap, size);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        // Линейный поиск и удаление элемента - O(n) сложность
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, heap[i])) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }
}

/*
* Основные принципы работы:
Минимальная куча - родитель всегда меньше или равен потомкам

Динамическое расширение - массив увеличивается при заполнении

Эффективные операции - добавление и удаление за O(log n)

Свойство кучи - поддерживается операциями просеивания

Две стратегии сравнения - естественный порядок или компаратор
* */