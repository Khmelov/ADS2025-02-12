package by.it.group410902.sinyutin.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Реализация Set<E> на основе отсортированного массива.
 * Подходит только для элементов, реализующих Comparable.
 * Запрещено использование классов стандартной библиотеки, кроме базовых.
 */
public class MyTreeSet<E extends Comparable<E>> implements Set<E> {

    // Поля MyTreeSet

    private static final int DEFAULT_CAPACITY = 10;

    // Массив для хранения элементов. Используем Object[] для обхода ограничений дженериков.
    private Object[] elements;
    private int size;

    //  Конструктор

    public MyTreeSet() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    // Вспомогательные методы

    /**
     * Увеличивает размер массива, если достигнута текущая вместимость.
     */
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length * 2;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }

            // Ручное копирование массива
            Object[] newElements = new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
    }

    /**
     * Выполняет бинарный поиск (Binary Search) для нахождения элемента или
     * места его вставки, используя интерфейс Comparable.
     * @return Индекс элемента (если найден) или (-индекс вставки - 1), если не найден.
     */
    private int binarySearch(Object key) {
        int low = 0;
        int high = size - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            E midVal = (E) elements[mid];

            // Используем compareTo для сравнения
            int cmp = midVal.compareTo((E) key);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid; // Элемент найден
            }
        }
        return -(low + 1); // Элемент не найден, возвращаем место вставки
    }

    // --- Обязательные базовые методы ---

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        // Очищаем массив, оставляя старую вместимость
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        this.size = 0;
    }

    @Override
    public boolean contains(Object o) {
        if (size == 0) return false;

        // Проверка типа и null-значений
        if (o == null || !(o instanceof Comparable)) {
            // TreeSet не поддерживает null, а также требует Comparable
            return false;
        }

        return binarySearch(o) >= 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            // TreeSet не поддерживает null
            throw new NullPointerException("MyTreeSet не поддерживает null-элементы.");
        }

        int insertionPoint = binarySearch(e);

        if (insertionPoint >= 0) {
            return false; // Элемент уже существует
        }

        // Вычисляем фактический индекс вставки
        int index = -(insertionPoint + 1);

        // Обеспечиваем вместимость
        ensureCapacity(size + 1);

        // Сдвиг элементов вправо для освобождения места
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }

        // Вставка элемента
        elements[index] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;

        int index = binarySearch(o);

        if (index < 0) {
            return false; // Элемент не найден
        }

        // Сдвиг элементов влево
        int numMoved = size - 1 - index;
        if (numMoved > 0) {
            // Ручное копирование: elements[index+1] -> elements[index] и т.д.
            for (int i = index; i < size - 1; i++) {
                elements[i] = elements[i + 1];
            }
        }

        // Обнуляем последний элемент и уменьшаем размер
        elements[--size] = null;
        return true;
    }

    // --- Методы Set<E>, работающие с коллекциями ---

    // *Поскольку мы не можем использовать стандартные коллекции, предполагается,
    // что входной Iterator коллекции 'c' предоставляется тестовой системой.*

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) return false;

        Iterator<?> it = c.iterator();
        while (it.hasNext()) {
            if (!contains(it.next())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) return false;
        boolean modified = false;

        Iterator<? extends E> it = c.iterator();
        while (it.hasNext()) {
            if (add(it.next())) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) return false;
        boolean modified = false;

        Iterator<?> it = c.iterator();
        while (it.hasNext()) {
            if (remove(it.next())) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) return false;
        boolean modified = false;

        // Эффективная реализация требует временного набора.
        // Мы итерируем по MyTreeSet, удаляя элементы, не содержащиеся в 'c'.

        // Итерируем в обратном порядке, чтобы избежать проблем со сдвигом индексов при удалении.
        for (int i = size - 1; i >= 0; i--) {
            E element = (E) elements[i];

            // Мы должны вызвать c.contains(element), что требует рабочего contains в Collection c.
            if (!c.contains(element)) {
                // Ручное удаление без вызова remove(Object), чтобы избежать повторного бинарного поиска

                // Сдвиг элементов влево
                int index = i;
                int numMoved = size - 1 - index;
                if (numMoved > 0) {
                    for (int j = index; j < size - 1; j++) {
                        elements[j] = elements[j + 1];
                    }
                }

                elements[--size] = null;
                modified = true;
            }
        }
        return modified;
    }

    // --- Метод toString() (вывод в порядке возрастания) ---

    @Override
    public String toString() {
        // Поскольку массив elements уже отсортирован, просто обходим его.
        StringBuilder sb = new StringBuilder();
        sb.append('[');

        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", "); // Разделитель: запятая с пробелом
            }
            sb.append(elements[i]);
        }

        sb.append(']');
        return sb.toString();
    }

    // --- Неподдерживаемые методы Set<E> ---

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Метод не реализован в соответствии с заданием");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Метод не реализован в соответствии с заданием");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Метод не реализован в соответствии с заданием");
    }
}
