package by.it.group410902.varava.lesson11;

import java.util.*;

/**
 * Кастомная реализация TreeSet на основе отсортированного массива.
 * Элементы хранятся в отсортированном порядке согласно компаратору или natural ordering.
 */
public class MyTreeSet<E> implements Set<E> {
    // Начальная емкость массива по умолчанию
    private static final int DEFAULT_CAPACITY = 10;

    // Массив для хранения элементов в отсортированном порядке
    private Object[] elements;

    // Количество элементов в множестве
    private int size;

    // Компаратор для определения порядка сортировки (может быть null для natural ordering)
    private final Comparator<? super E> comparator;

    // Конструкторы

    /**
     * Конструктор по умолчанию - использует natural ordering
     */
    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        this((Comparator<? super E>) Comparator.naturalOrder());
    }

    /**
     * Конструктор с компаратором
     * @param comparator компаратор для определения порядка сортировки
     */
    public MyTreeSet(Comparator<? super E> comparator) {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = comparator;
    }

    /**
     * Конструктор из коллекции
     * @param c коллекция для инициализации
     */
    @SuppressWarnings("unchecked")
    public MyTreeSet(Collection<? extends E> c) {
        this();
        addAll(c);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Проверяет наличие элемента в множестве
     * Использует бинарный поиск для эффективности O(log n)
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        return binarySearch((E) o) >= 0;
    }

    /**
     * Добавляет элемент в множество
     * Сохраняет отсортированный порядок массива
     * @return true если элемент был добавлен, false если уже существовал
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean add(E e) {
        // Ищем позицию элемента с помощью бинарного поиска
        int index = binarySearch(e);
        if (index >= 0) {
            return false; // Элемент уже существует
        }

        // Вычисляем позицию для вставки (бинарный поиск возвращает -insertIndex-1 для отсутствующих элементов)
        int insertIndex = -index - 1;

        // Увеличиваем массив при необходимости
        ensureCapacity(size + 1);

        // Сдвигаем элементы вправо чтобы освободить место для нового элемента
        System.arraycopy(elements, insertIndex, elements, insertIndex + 1, size - insertIndex);

        // Вставляем новый элемент
        elements[insertIndex] = e;
        size++;
        return true;
    }

    /**
     * Удаляет элемент из множества
     * @return true если элемент был удален, false если не найден
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        int index = binarySearch((E) o);
        if (index < 0) {
            return false; // Элемент не найден
        }

        // Сдвигаем элементы влево для удаления элемента
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        elements[--size] = null; // Очищаем последнюю ячейку и уменьшаем размер
        return true;
    }

    /**
     * Очищает множество - удаляет все элементы
     */
    @Override
    public void clear() {
        // Очищаем ссылки для помощи garbage collector
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    /**
     * Возвращает строковое представление множества
     * Элементы выводятся в отсортированном порядке
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(elements[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    // Bulk операции (операции с коллекциями)

    /**
     * Проверяет содержит ли множество все элементы коллекции
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Добавляет все элементы коллекции в множество
     * @return true если множество изменилось
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    /**
     * Удаляет все элементы коллекции из множества
     * @return true если множество изменилось
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object element : c) {
            if (remove(element)) {
                modified = true;
            }
        }
        return modified;
    }

    /**
     * Оставляет только элементы, содержащиеся в указанной коллекции
     * @return true если множество изменилось
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E element = it.next();
            if (!c.contains(element)) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    // Реализация остальных методов интерфейса Set

    /**
     * Возвращает итератор для обхода элементов в отсортированном порядке
     */
    @Override
    public Iterator<E> iterator() {
        return new TreeSetIterator();
    }

    /**
     * Итератор для обхода элементов множества
     * Поддерживает операцию remove()
     */
    private class TreeSetIterator implements Iterator<E> {
        private int currentIndex = 0;      // Текущая позиция итератора
        private boolean canRemove = false; // Флаг разрешения операции remove

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            canRemove = true; // Разрешаем remove после успешного next
            return (E) elements[currentIndex++];
        }

        @Override
        public void remove() {
            if (!canRemove) {
                throw new IllegalStateException();
            }
            // Удаляем предыдущий элемент (currentIndex указывает на следующий)
            MyTreeSet.this.remove(elements[currentIndex - 1]);
            currentIndex--; // Корректируем индекс после удаления
            canRemove = false; // Запрещаем повторный remove
        }
    }

    /**
     * Возвращает массив содержащий все элементы множества
     */
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    /**
     * Возвращает массив содержащий все элементы множества
     * @param a массив в который будут помещены элементы (если достаточно большой)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) Arrays.copyOf(elements, size, a.getClass());
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null; // Помечаем конец согласно контракту Collection
        }
        return a;
    }

    /**
     * Сравнивает это множество с другим объектом
     * Два множества равны если содержат одинаковые элементы
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Set)) {
            return false;
        }

        Collection<?> c = (Collection<?>) o;
        if (c.size() != size()) {
            return false;
        }

        try {
            return containsAll(c);
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }
    }

    /**
     * Возвращает хеш-код множества
     * Хеш-код равен сумме хеш-кодов всех элементов
     */
    @Override
    public int hashCode() {
        int hashCode = 0;
        for (Object element : this) {
            if (element != null) {
                hashCode += element.hashCode();
            }
        }
        return hashCode;
    }

    // Вспомогательные методы

    /**
     * Бинарный поиск элемента в отсортированном массиве
     * @param key искомый элемент
     * @return индекс элемента если найден, иначе -insertIndex-1
     */
    @SuppressWarnings("unchecked")
    private int binarySearch(E key) {
        int low = 0;
        int high = size - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1; // Безопасное вычисление середины
            E midVal = (E) elements[mid];
            int cmp = compare(midVal, key);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid; // ключ найден
            }
        }
        return -(low + 1); // ключ не найден, возвращаем позицию для вставки
    }

    /**
     * Сравнивает два элемента с использованием компаратора или natural ordering
     */
    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        }
        return ((Comparable<? super E>) a).compareTo(b);
    }

    /**
     * Обеспечивает достаточную емкость массива
     * Увеличивает массив в 2 раза при необходимости
     */
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length * 2;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }

    /**
     * Сравнивает два объекта на равенство
     */
    private boolean objectsEqual(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    // Дополнительные методы характерные для TreeSet

    /**
     * Возвращает первый (наименьший) элемент множества
     * @throws NoSuchElementException если множество пустое
     */
    @SuppressWarnings("unchecked")
    public E first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return (E) elements[0];
    }

    /**
     * Возвращает последний (наибольший) элемент множества
     * @throws NoSuchElementException если множество пустое
     */
    @SuppressWarnings("unchecked")
    public E last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return (E) elements[size - 1];
    }

    /**
     * Возвращает наибольший элемент строго меньше заданного
     * @param e эталонный элемент
     * @return наибольший элемент меньше e или null если такого нет
     */
    @SuppressWarnings("unchecked")
    public E lower(E e) {
        int index = binarySearch(e);
        if (index <= 0) {
            index = -index - 2; // Позиция перед точкой вставки
        } else {
            index--; // Элемент найден, берем предыдущий
        }
        return (index >= 0) ? (E) elements[index] : null;
    }

    /**
     * Возвращает наименьший элемент строго больше заданного
     * @param e эталонный элемент
     * @return наименьший элемент больше e или null если такого нет
     */
    @SuppressWarnings("unchecked")
    public E higher(E e) {
        int index = binarySearch(e);
        if (index < 0) {
            index = -index - 1; // Позиция после точки вставки
        } else {
            index++; // Элемент найден, берем следующий
        }
        return (index < size) ? (E) elements[index] : null;
    }
}