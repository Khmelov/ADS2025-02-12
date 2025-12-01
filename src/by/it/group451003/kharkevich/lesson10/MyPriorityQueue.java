package by.it.group451003.kharkevich.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Queue;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    final int defaultSize = 8; // Начальный размер массива
    E[] _elements; // Массив для хранения элементов кучи
    int size; // Текущий размер кучи

    // Конструктор
    public MyPriorityQueue() {
        _elements = (E[]) new Comparable[defaultSize]; // Создание массива
    }

    // Преобразование кучи в строку
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(); // Строитель строки
        sb.append("["); // Начало массива

        // Добавление всех элементов через запятую
        for (int i = 0; i < size; i++) {
            sb.append(_elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]"); // Конец массива
        return sb.toString(); // Возврат строки
    }

    // Восстановление свойства кучи снизу вверх
    void heapifyUp(int index) {
        int parent = (index - 1) / 2; // Индекс родителя

        // Если родитель существует и текущий элемент меньше родителя
        if (parent >= 0 && _elements[index].compareTo(_elements[parent]) < 0) {
            // Обмен текущего элемента с родителем
            E temp = _elements[index];
            _elements[index] = _elements[parent];
            _elements[parent] = temp;
            // Рекурсивный вызов для родителя
            heapifyUp(parent);
        }
    }

    // Восстановление свойства кучи сверху вниз
    void heapifyDown(int index) {
        int left = 2 * index + 1; // Индекс левого потомка
        int right = 2 * index + 2; // Индекс правого потомка
        int smallest = index; // Индекс наименьшего элемента

        // Поиск наименьшего среди текущего элемента и его потомков
        if (left < size && _elements[left].compareTo(_elements[smallest]) < 0) {
            smallest = left; // Левый потомок меньше
        }

        if (right < size && _elements[right].compareTo(_elements[smallest]) < 0) {
            smallest = right; // Правый потомок меньше
        }

        // Если наименьший не текущий элемент
        if (smallest != index) {
            // Обмен текущего элемента с наименьшим потомком
            E temp = _elements[index];
            _elements[index] = _elements[smallest];
            _elements[smallest] = temp;
            // Рекурсивный вызов для потомка
            heapifyDown(smallest);
        }
    }

    @Override
    public int size() {
        return size; // Возврат размера кучи
    }

    @Override
    public boolean isEmpty() {
        return size == 0; // Проверка пустоты кучи
    }

    @Override
    public boolean contains(Object o) {
        // Линейный поиск элемента в куче
        for (int i = 0; i < size; i++) {
            if (_elements[i].equals(o)) {
                return true; // Элемент найден
            }
        }
        return false; // Элемент не найден
    }

    @Override
    public boolean add(E e) {
        // Если массив заполнен - увеличиваем размер
        if (size == _elements.length) {
            resize();
        }
        _elements[size++] = e; // Добавление элемента в конец
        heapifyUp(size - 1); // Восстановление свойства кучи
        return true; // Всегда возвращает true
    }

    // Увеличение размера массива
    void resize() {
        int newCapacity = _elements.length * 2; // Новый размер
        E[] newItems = (E[]) new Comparable[newCapacity]; // Новый массив
        System.arraycopy(_elements, 0, newItems, 0, size); // Копирование элементов
        _elements = newItems; // Замена старого массива новым
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // Проверка содержания всех элементов коллекции
        for (Object item : c) {
            if (!contains(item)) {
                return false; // Если хотя бы один не найден - false
            }
        }
        return true; // Все элементы найдены
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false; // Флаг изменения

        // Добавление всех элементов коллекции
        for (E item : c) {
            if (add(item)) {
                modified = true; // Если добавление успешно - устанавливаем флаг
            }
        }
        return modified; // Возврат флага изменения
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false; // Флаг изменения
        // Создаем временный массив для элементов, которые нужно сохранить
        E[] tempArray = (E[]) new Comparable[_elements.length];
        int newSize = 0;

        // Собираем элементы, которые НЕ должны быть удалены
        for (int i = 0; i < size; i++) {
            if (!c.contains(_elements[i])) {
                tempArray[newSize++] = _elements[i]; // Сохранение элемента
            }
        }

        // Если размер изменился, значит были удаления
        if (newSize != size) {
            modified = true; // Установка флага изменения
            // Заменяем массив и перестраиваем кучу
            _elements = tempArray;
            size = newSize;
            // Перестраиваем кучу полностью
            for (int i = (size - 1) / 2; i >= 0; i--) {
                heapifyDown(i); // Восстановление свойства кучи
            }
        }
        return modified; // Возврат флага изменения
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false; // Флаг изменения
        // Создаем временный массив для элементов, которые нужно сохранить
        E[] tempArray = (E[]) new Comparable[_elements.length];
        int newSize = 0;

        // Собираем элементы, которые должны остаться
        for (int i = 0; i < size; i++) {
            if (c.contains(_elements[i])) {
                tempArray[newSize++] = _elements[i]; // Сохранение элемента
            }
        }

        // Если размер изменился, значит были удаления
        if (newSize != size) {
            modified = true; // Установка флага изменения
            // Заменяем массив и перестраиваем кучу
            _elements = tempArray;
            size = newSize;
            // Перестраиваем кучу полностью
            for (int i = (size - 1) / 2; i >= 0; i--) {
                heapifyDown(i); // Восстановление свойства кучи
            }
        }
        return modified; // Возврат флага изменения
    }

    // Вспомогательный метод для удаления элемента по индексу
    private void removeAt(int index) {
        // Заменяем удаляемый элемент последним элементом
        _elements[index] = _elements[--size];
        _elements[size] = null; // очищаем ссылку

        // Восстанавливаем свойства кучи
        if (index > 0 && _elements[index].compareTo(_elements[(index - 1) / 2]) < 0) {
            heapifyUp(index); // Если элемент меньше родителя - поднимаем
        } else {
            heapifyDown(index); // Иначе опускаем
        }
    }

    @Override
    public boolean remove(Object o) {
        // Поиск и удаление элемента
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, _elements[i])) {
                removeAt(i); // Удаление по индексу
                return true; // Элемент найден и удален
            }
        }
        return false; // Элемент не найден
    }

    @Override
    public boolean offer(E e) {
        return add(e); // Добавление элемента
    }

    @Override
    public E remove() {
        if (isEmpty()) {
            throw new IllegalArgumentException("PriorityQueue is empty"); // Если очередь пуста - исключение
        }
        E root = _elements[0]; // Сохранение корня
        removeAt(0); // Удаление корня
        return root; // Возврат удаленного элемента
    }

    @Override
    public E poll() {
        if (isEmpty()) {
            return null; // Если очередь пуста - возврат null
        }
        E root = _elements[0]; // Сохранение корня
        removeAt(0); // Удаление корня
        return root; // Возврат удаленного элемента
    }

    @Override
    public E element() {
        if (size == 0) {
            throw new java.util.NoSuchElementException(); // Если очередь пуста - исключение
        }
        return _elements[0]; // Возврат корня без удаления
    }

    @Override
    public E peek() {
        if (size == 0) {
            return null; // Если очередь пуста - возврат null
        }
        return _elements[0]; // Возврат корня без удаления
    }

    @Override
    public void clear() {
        // Очистка всех элементов
        for (int i = 0; i < size; i++) {
            _elements[i] = null; // Очистка ссылок
        }
        size = 0; // Сброс размера
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0; // Текущий индекс

            @Override
            public boolean hasNext() {
                return currentIndex < size; // Проверка наличия следующего элемента
            }

            @Override
            public E next() {
                return _elements[currentIndex++]; // Возврат следующего элемента
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size]; // Создание массива
        System.arraycopy(_elements, 0, result, 0, size); // Копирование элементов
        return result; // Возврат массива
    }

    @Override
    public <T> T[] toArray(T[] a) {
        // Если массив слишком мал - создаем новый
        if (a.length < size) {
            return (T[]) java.util.Arrays.copyOf(_elements, size, a.getClass());
        }
        System.arraycopy(_elements, 0, a, 0, size); // Копирование элементов

        // Если массив больше размера кучи
        if (a.length > size) {
            a[size] = null; // Установка null после последнего элемента
        }

        return a; // Возврат массива
    }
}