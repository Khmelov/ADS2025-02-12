package by.it.group410902.derzhavskaya_e.lesson11;

// Реализация бинарного дерева на основе отсортированного массива
@SuppressWarnings("unchecked")
public class MyTreeSet<E> implements java.util.Set<E> {

    private E[] elements; // массив для хранения элементов
    private int size;     // текущее количество элементов

    public MyTreeSet() {
        elements = (E[]) new Object[16]; // начальная емкость
        size = 0;
    }

    // Количество элементов
    @Override
    public int size() {
        return size;
    }

    // Проверка на пустоту
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Очистка множества
    @Override
    public void clear() {
        for (int i = 0; i < size; i++)
            elements[i] = null;
        size = 0;
    }

    // Добавление элемента с сохранением сортировки
    @Override
    public boolean add(E value) {
        if (value == null)
            throw new NullPointerException("Null values not allowed");

        int index = binarySearch(value);

        // если элемент уже есть — не добавляем
        if (index >= 0)
            return false;

        // позиция вставки = -(index + 1)
        int insertPos = -(index + 1);

        // увеличиваем размер массива при необходимости
        if (size == elements.length)
            resizeArray();

        // сдвигаем элементы вправо
        for (int i = size; i > insertPos; i--)
            elements[i] = elements[i - 1];

        elements[insertPos] = value;
        size++;
        return true;
    }

    // Проверка, содержится ли элемент
    @Override
    public boolean contains(Object value) {
        return binarySearch((E) value) >= 0;
    }

    // Удаление элемента
    @Override
    public boolean remove(Object value) {
        int index = binarySearch((E) value);
        if (index < 0)
            return false;

        // сдвиг влево, чтобы удалить элемент
        for (int i = index; i < size - 1; i++)
            elements[i] = elements[i + 1];

        elements[--size] = null;
        return true;
    }

    // Преобразование множества в строку (по возрастанию)
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1)
                sb.append(", ");
        }
        return sb.append("]").toString();
    }

    // === Методы работы с коллекциями ===

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        for (Object o : c)
            if (!contains(o))
                return false;
        return true;
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c)
            if (add(e))
                modified = true;
        return modified;
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        boolean modified = false;
        for (Object o : c)
            if (remove(o))
                modified = true;
        return modified;
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        boolean modified = false;
        int i = 0;
        while (i < size) {
            if (!c.contains(elements[i])) {
                remove(elements[i]);
                modified = true;
            } else {
                i++;
            }
        }
        return modified;
    }

    // Вспомогательные методы

    // Бинарный поиск (возвращает индекс найденного или -(позиция + 1))
    private int binarySearch(E value) {
        int left = 0, right = size - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = compare(value, elements[mid]);
            if (cmp == 0)
                return mid;
            else if (cmp < 0)
                right = mid - 1;
            else
                left = mid + 1;
        }
        return -(left + 1);
    }

    // Сравнение элементов (через Comparable)
    private int compare(E a, E b) {
        return ((Comparable<E>) a).compareTo(b);
    }

    // Увеличение размера массива
    private void resizeArray() {
        E[] newArr = (E[]) new Object[elements.length * 2];
        for (int i = 0; i < size; i++)
            newArr[i] = elements[i];
        elements = newArr;
    }

    @Override public java.util.Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return null; }
    @Override public <T> T[] toArray(T[] a) { return null; }
}
