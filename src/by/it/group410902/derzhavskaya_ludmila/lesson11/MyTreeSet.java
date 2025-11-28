package by.it.group410902.derzhavskaya_ludmila.lesson11;
import java.util.Set;
// отсортированного массива
public class MyTreeSet<E> implements Set<E> {
    private Object[] elements;

    private int size;

    private static final int INITIAL_CAPACITY = 10;

    // Конструктор по умолчанию
    public MyTreeSet() {
        elements = new Object[INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        elements = new Object[INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        // Проверяем, существует ли элемент уже в множестве
        int index = binarySearch(element);
        if (index >= 0) {
            return false;
        }

        // Вычисляем позицию для вставки (преобразуем отрицательный результат binarySearch)
        int insertIndex = -index - 1;
        // Увеличиваем емкость массива при необходимости
        if (size >= elements.length) {
            int newCapacity = elements.length *2;
            Object[] newElements = new Object[newCapacity];

            // Копируем элементы в новый массив
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }

            elements = newElements;
        }

        // Сдвигаем элементы вправо, чтобы освободить место для нового элемента
        for (int i = size; i > insertIndex; i--) {
            elements[i] = elements[i - 1];
        }

        // Вставляем новый элемент
        elements[insertIndex] = element;
        size++;

        return true;
    }

    @Override
    public boolean remove(Object element) {
        if (element == null) {
            return false;
        }

        // Ищем элемент бинарным поиском
        int index = binarySearch(element);
        if (index < 0) {
            return false;
        }

        // Сдвигаем элементы влево
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        elements[size - 1] = null;
        size--;

        return true;
    }

    @Override
    public boolean contains(Object element) {
        if (element == null) {
            return false;
        }

        return binarySearch(element) >= 0;
    }

    @Override
    public boolean containsAll(java.util.Collection<?> collection) {
        for (Object element : collection) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> collection) {
        boolean p = false;

        for (E element : collection) {
            if (add(element)) {
                p = true;
            }
        }

        return p;
    }

    @Override
    public boolean removeAll(java.util.Collection<?> collection) {
        boolean p = false;

        for (Object element : collection) {
            if (remove(element)) {
                p = true;
            }
        }

        return p;
    }

    // Сохраняет только те элементы, которые содержатся в переданной коллекции
    @Override
    public boolean retainAll(java.util.Collection<?> collection) {
        boolean p = false;

        // Создаем временный массив для элементов, которые нужно сохранить
        Object[] retainedElements = new Object[size];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            E currentElement = (E) elements[i];

            if (collection.contains(currentElement)) {
                retainedElements[newSize] = currentElement;
                newSize++;
            } else {
                p = true;
            }
        }

        if (p) {
            elements = retainedElements;
            size = newSize;
        }

        return p;
    }

    // Бинарный поиск элемента в отсортированном массиве
    private int binarySearch(Object element) {
        int left = 0;
        int right = size - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            E midElement = (E) elements[mid];

            // Сравниваем элементы
            int comparison = compare((E) element, midElement);

            if (comparison == 0) {
                return mid; // Элемент найден
            } else if (comparison < 0) {
                right = mid - 1; // Ищем в левой половине
            } else {
                left = mid + 1; // Ищем в правой половине
            }
        }

        // Элемент не найден, возвращаем -(insertion point) - 1
        return -left - 1;
    }

    // Сравнивает два элемента
    private int compare(E element1, E element2) {
        if (element1 instanceof Comparable && element2 instanceof Comparable) {
            return ((Comparable<E>) element1).compareTo(element2);
        }

        // Если элементы не Comparable, используем хеш-коды как запасной вариант
        int hash1 = element1.hashCode();
        int hash2 = element2.hashCode();

        if (hash1 < hash2) return -1;
        if (hash1 > hash2) return 1;
        return 0;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }

            sb.append(elements[i].toString());
        }

        sb.append("]");
        return sb.toString();
    }

    //////    Остальные методы интерфейса Set - не реализованы

    @Override
    public java.util.Iterator<E> iterator() {
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
}