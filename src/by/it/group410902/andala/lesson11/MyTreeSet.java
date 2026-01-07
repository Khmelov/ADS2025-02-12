package by.it.group410902.andala.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyTreeSet<E extends Comparable<E>> implements Set<E> {

    // Массив для хранения элементов в отсортированном порядке
    private E[] elements;
    // Текущее количество элементов
    private int size;
    // Начальный размер массива
    private static final int DEFAULT_CAPACITY = 16;

    // Конструктор
    public MyTreeSet() {
        elements = (E[]) new Comparable[DEFAULT_CAPACITY];
        size = 0;
    }

    // Увеличиваем вместимость массива при необходимости
    private void ensureCapacity() {
        if (size >= elements.length) {
            // Создаем новый массив в 2 раза больше
            E[] newArray = (E[]) new Comparable[elements.length * 2];
            // Копируем старые элементы в новый массив
            System.arraycopy(elements, 0, newArray, 0, elements.length);
            elements = newArray;
        }
    }

    // Бинарный поиск элемента в отсортированном массиве
    private int binarySearch(E key) {
        int left = 0, right = size - 1;
        while (left <= right) {
            int mid = (left + right) >>> 1;  // Безопасное вычисление середины
            int cmp = elements[mid].compareTo(key);
            if (cmp == 0) return mid;        // Нашли элемент
            if (cmp < 0) left = mid + 1;     // Искомый элемент справа
            else right = mid - 1;            // Искомый элемент слева
        }
        return -left - 1; // Возвращаем позицию для вставки (отрицательное число)
    }

    // Добавление элемента с сохранением порядка
    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();  // Не поддерживаем null
        int index = binarySearch(e);
        if (index >= 0) return false; // Элемент уже существует

        // Обеспечиваем достаточно места
        ensureCapacity();
        // Преобразуем позицию вставки в положительный индекс
        index = -index - 1;
        // Сдвигаем элементы вправо чтобы освободить место
        System.arraycopy(elements, index, elements, index + 1, size - index);
        // Вставляем новый элемент
        elements[index] = e;
        size++;
        return true;
    }

    // Удаление элемента
    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        int index = binarySearch((E) o);
        if (index < 0) return false; // Элемент не найден

        // Сдвигаем элементы влево чтобы заполнить пустое место
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        // Очищаем последний элемент и уменьшаем размер
        elements[--size] = null;
        return true;
    }

    // Проверка наличия элемента
    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        return binarySearch((E) o) >= 0;
    }

    // Очистка множества
    @Override
    public void clear() {
        // Обнуляем все элементы
        for (int i = 0; i < size; i++) elements[i] = null;
        size = 0;
    }

    // Получение количества элементов
    @Override
    public int size() {
        return size;
    }

    // Проверка пустоты множества
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Строковое представление (элементы в отсортированном порядке)
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // Проверка наличия всех элементов коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    // Добавление всех элементов коллекции
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            if (add(e)) changed = true;
        }
        return changed;
    }

    // Удаление всех элементов коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object e : c) {
            if (remove(e)) changed = true;
        }
        return changed;
    }

    // Оставить только элементы, которые есть в коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        int newSize = 0;
        // Проходим по всем элементам
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                // Сохраняем элемент если он есть в коллекции
                elements[newSize++] = elements[i];
            } else {
                changed = true;  // Отмечаем что множество изменилось
            }
        }
        // Очищаем оставшиеся элементы
        for (int i = newSize; i < size; i++) elements[i] = null;
        size = newSize;
        return changed;
    }

    // Итератор для обхода элементов
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int index = 0;  // Текущая позиция

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                return elements[index++];
            }
        };
    }

    // Не реализованные методы
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}