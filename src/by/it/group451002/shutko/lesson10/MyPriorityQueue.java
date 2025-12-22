package by.it.group451002.shutko.lesson10;

import java.util.*;


// Создайте class ,который реализует интерфейс очередь
// и работает на основе кучи, построенной на приватном массиве
// БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
public class MyPriorityQueue<T> implements Queue<T> {

    // Начальная емкость массива
    private static final int DEFAULT_CAPACITY = 10;

    // Основной массив для хранения элементов кучи
    private Object[] queue;
    // Текущее количество элементов в очереди
    private int size;

    // Конструктор - создает пустую очередь с начальной емкостью
    public MyPriorityQueue() {
        queue = new Object[DEFAULT_CAPACITY];
    }

    // Преобразование очереди в строку для вывода
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";  // Пустая очередь
        }
        StringBuilder sb = new StringBuilder().append("[");
        // Добавляем все элементы кроме последнего с запятыми
        for (int i = 0; i < size - 1; i++) {
            sb.append(queue[i]).append(", ");
        }
        // Добавляем последний элемент без запятой
        return sb.append(queue[size - 1]).append("]").toString();
    }

    @Override
    // Возвращает количество элементов в очереди
    public int size() {
        return size;
    }

    @Override
    // Очищает очередь - удаляет все элементы
    public void clear() {
        for (int i = 0; i < size; i++) {
            queue[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(T t) {
        return offer(t);
    }

    @Override
    // Удаление и возврат головного элемента (минимальный) (с исключением если очередь пуста)
    public T remove() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return poll();
    }

    // Проверка наличия элемента в очереди
    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
        // Используем вспомогательный метод поиска
    }

    // Основной метод добавления элемента в очередь
    @Override
    public boolean offer(T t) {
        // Проверка на null
        if (t == null) {
            throw new NullPointerException();
        }

        // Увеличиваем массив если нужно
        if (size == queue.length) {
            grow();
        }

        // Добавляем элемент в конец и "всплываем" его на нужную позицию
        this.siftUp(size, t);
        size++;

        return true;
    }

    // Удаление и возврат минимального элемента (головы очереди)
    @Override
    public T poll() {
        T element = (T) queue[0];
        // Минимальный элемент всегда в корне
        if (element != null) {
            size--;
            T last = (T) queue[size];
            // Берем последний элемент
            queue[size] = null;
            // Очищаем последнюю позицию
            queue[0] = last;
            // Ставим последний элемент в корень
            if (size > 0) {
                siftDown(0);
                // "Погружаем" его на нужную позицию
            }
        }
        return element;
    }

    // Просмотр головного элемента без удаления
    @Override
    public T peek() {
        return (T) queue[0];
        // Просто возвращаем корневой элемент
    }

    @Override
    public T element() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return (T) queue[0];
    }

    @Override
    // Проверка пустоты очереди
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    // Проверка наличия ВСЕХ элементов коллекции в очереди
    // Если хотя бы одного нет - false
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    // Добавление всех элементов коллекции
    public boolean addAll(Collection<? extends T> c) {
        boolean modified = false;
        for (T e : c)
            // Добавляем каждый элемент
            if (add(e))
                modified = true;
        return modified;
    }

    @Override
    // Удаление всех элементов, которые есть в переданной коллекции
    public boolean removeAll(Collection<?> c) {
        // Создаем новый массив только с элементами, которых НЕТ в коллекции c
        Object[] newData = new Object[size];
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(queue[i])) {
                newData[newSize] = queue[i];
                newSize++;
            }
        }
        // Проверяем изменился ли размер
        boolean modified = newSize != size;
        queue = newData;
        size = newSize;
        // Перестраиваем кучу так как порядок мог нарушиться
        for (int i = size / 2; i >= 0; i--)
            siftDown(i);
        return modified;
    }

    @Override
    // Удаление всех элементов, которых НЕТ в переданной коллекции
    public boolean retainAll(Collection<?> c) {
        // Создаем новый массив только с элементами, которые ЕСТЬ в коллекции c
        Object[] newData = new Object[size];
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(queue[i])) {
                newData[newSize] = queue[i];
                newSize++;
            }
        }

        boolean modified = newSize != size;
        queue = newData;
        size = newSize;
        // Перестраиваем кучу
        for (int i = size / 2; i >= 0; i--)
            siftDown(i);
        return modified;
    }

    // Увеличение размера массива при необходимости
    private void grow() {
        queue = Arrays.copyOf(queue, queue.length * 3 / 2);
    }

    // Поиск индекса элемента в массиве
    private int indexOf(Object o) {
        if (o != null) {
            for (int i = 0; i < size; i++) {
                if (o.equals(queue[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    // Всплытие элемента - восстановление свойства кучи снизу вверх
    private void siftUp(int index, T item) {

        Comparable<? super T> comparable = (Comparable<? super T>) item;
        // Поднимаем элемент пока не дойдем до корня или не найдем правильное место
        while (index > 0) {
            // Нахождение родителя данного элемента
            int parentIndex = (index - 1) / 2;
            Object parent = queue[parentIndex];
            // Если текущий элемент >= родителя - место найдено
            if (comparable.compareTo((T) parent) >= 0) {
                break;
            }

            // Перемещаем родительский элемент ВНИЗ на позицию текущего элемента
            queue[index] = parent;
            // Переходим на позицию родителя для следующей итерации
            index = parentIndex;
        }

        // Помещаем элемент на найденную позицию
        queue[index] = item;
    }

    // Погружение элемента - восстановление свойства кучи сверху вниз
    private void siftDown(int index) {
        // Находим левый потомок
        int left = 2 * index + 1;
        // И правый
        int right = left + 1;

        // Предполагаем, что текущий элемент наименьший
        int largest = index;

        // Сравниваем с левым потомком (проверяем, что левый потомок существует (не выходит за
        // границы массива) и левый потомок МЕНЬШЕ текущего элемента)
        if (left < size && ((Comparable<? super T>) queue[left]).compareTo((T) queue[index]) < 0) {
            // Левый потомок меньше
            largest = left;
        }

        // Сравниваем с правым потомком
        if (right < size && ((Comparable<? super T>) queue[right]).compareTo((T) queue[largest]) < 0) {
            // Правый потомок меньше
            largest = right;
        }

        // Если нашли потомка меньше текущего элемента - меняем местами
        if (largest != index) {
            swap(index, largest);
            // Рекурсивно продолжаем погружение
            siftDown(largest);
        }


    }

    // Обмен двух элементов в массиве
    private void swap(int index1, int index2) {
        Object temp = queue[index1];
        queue[index1] = queue[index2];
        queue[index2] = temp;
    }

    //————————————————————

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    // Удаление конкретного элемента по значению
    @Override
    public boolean remove(Object o) {
        // Находим индекс элемента
        int index = indexOf(o);
        if (index == -1) {
            // Элемент не найден
            return false;
        }

        size--;
        // Берем последний элемент
        T last = (T) queue[size];
        // Очищаем последнюю позицию
        queue[size] = null;
        // Заменяем удаляемый элемент последним
        queue[index] = last;
        if (size > 0) {
            // Восстанавливаем свойства кучи
            siftDown(index);
        }

        return true;
    }
}
