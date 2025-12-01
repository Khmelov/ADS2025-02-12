package by.it.group451002.jasko.lesson11;

import java.util.Collection;
import java.util.Iterator;

// Реализация множества на основе отсортированного массива
public class MyTreeSet<E extends Comparable<E>> implements Collection<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private Object[] elements; // Отсортированный массив
    private int size; // Количество элементов

    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        elements = new Object[DEFAULT_CAPACITY]; // Инициализация массива
        size = 0;
    }

    // Увеличивает размер массива при необходимости
    private void ensureCapacity() {
        if (size >= elements.length) { // Если массив заполнен
            Object[] newElements = new Object[elements.length * 2]; // Удваиваем размер
            System.arraycopy(elements, 0, newElements, 0, size); // Копируем элементы
            elements = newElements; // Обновляем массив
        }
    }

    // Бинарный поиск элемента
    @SuppressWarnings("unchecked")
    private int binarySearch(E e) {
        int low = 0; // Начало диапазона
        int high = size - 1; // Конец диапазона

        while (low <= high) {
            int mid = (low + high) >>> 1; // Средний индекс
            E midVal = (E) elements[mid]; // Значение в середине
            int cmp = midVal.compareTo(e); // Сравнение

            if (cmp < 0) low = mid + 1; // Ищем в правой части
            else if (cmp > 0) high = mid - 1; // Ищем в левой части
            else return mid; // Нашли элемент
        }
        return -(low + 1); // Возвращаем отрицательный индекс, куда можно вставить
    }

    @Override
    public int size() { return size; } // Возвращает текущий размер

    @Override
    public boolean isEmpty() { return size == 0; } // Проверяет, пусто ли множество

    @Override
    public boolean contains(Object o) {
        if (!(o instanceof Comparable)) return false; // Проверка на null и тип
        @SuppressWarnings("unchecked")
        int index = binarySearch((E) o); // Ищем элемент
        return index >= 0; // Если индекс >= 0 — элемент найден
    }

    // Возвращает итератор, обходящий элементы в порядке возрастания
    @Override
    public Iterator<E> iterator() {
        return new ArrayIterator();
    }

    // Итератор с поддержкой remove()
    private class ArrayIterator implements Iterator<E> {
        private int index = 0; // Индекс текущего элемента
        private int lastReturned = -1; // Индекс последнего возвращённого

        @Override
        public boolean hasNext() { return index < size; } // Проверяем, есть ли ещё элементы

        @Override
        public E next() {
            if (!hasNext()) throw new java.util.NoSuchElementException(); // Проверка на конец
            lastReturned = index; // Запоминаем индекс
            return (E) elements[index++]; // Возвращаем элемент и увеличиваем индекс
        }

        @Override
        public void remove() {
            if (lastReturned < 0) throw new IllegalStateException(); // Проверка на корректность вызова
            int removeIndex = lastReturned; // Индекс элемента для удаления
            System.arraycopy(elements, removeIndex + 1, elements, removeIndex, size - removeIndex - 1); // Сдвигаем
            elements[--size] = null; // Уменьшаем размер и ставим null
            lastReturned = -1; // Сбрасываем
            index = removeIndex; // Обновляем индекс
        }
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size]; // Создаём массив нужного размера
        System.arraycopy(elements, 0, arr, 0, size); // Копируем элементы
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) { // Если массив мал — создаём новый
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        System.arraycopy(elements, 0, a, 0, size); // Копируем элементы
        if (a.length > size) a[size] = null; // Если больше — ставим null
        return a;
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new IllegalArgumentException("Null not allowed"); // null запрещён
        int index = binarySearch(e); // Ищем элемент
        if (index >= 0) return false; // Уже есть — не добавляем

        index = -(index + 1); // Индекс для вставки
        ensureCapacity(); // Увеличиваем массив при необходимости
        System.arraycopy(elements, index, elements, index + 1, size - index); // Сдвигаем элементы
        elements[index] = e; // Вставляем элемент
        size++; // Увеличиваем счётчик
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (!(o instanceof Comparable)) return false; // Проверка
        @SuppressWarnings("unchecked")
        int index = binarySearch((E) o); // Ищем элемент
        if (index < 0) return false; // Не найден

        System.arraycopy(elements, index + 1, elements, index, size - index - 1); // Сдвигаем
        elements[--size] = null; // Уменьшаем размер и ставим null
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object obj : c) if (!contains(obj)) return false; // Если нет хотя бы одного — false
        return true; // Все есть
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false; // Флаг, был ли добавлен хотя бы один
        for (E e : c) if (add(e)) modified = true; // Добавляем
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false; // Флаг, был ли удалён хотя бы один
        for (Object obj : c) if (remove(obj)) modified = true; // Удаляем
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false; // Флаг, был ли удалён хотя бы один
        for (Iterator<E> it = iterator(); it.hasNext(); ) { // Проходим по итератору
            if (!c.contains(it.next())) { // Если нет в коллекции — удаляем
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) elements[i] = null; // Обнуляем элементы
        size = 0; // Сбрасываем счётчик
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("["); // Начинаем строку
        for (int i = 0; i < size; i++) { // Проходим по массиву
            if (i > 0) sb.append(", "); // Добавляем разделитель
            sb.append(elements[i]); // Добавляем элемент
        }
        sb.append("]"); // Закрываем строку
        return sb.toString();
    }

    // Неиспользуемые методы
    @Override
    public boolean removeIf(java.util.function.Predicate<? super E> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Spliterator<E> spliterator() {
        throw new UnsupportedOperationException();
    }
}