package by.it.group410901.kalach.lesson11;

import java.util.Collection;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {
    // Внутренний массив для хранения элементов в отсортированном порядке
    private Object[] a;
    // Текущее количество элементов в множестве
    private int size;

    // Конструктор по умолчанию - создает начальный массив размером 10
    public MyTreeSet() {
        a = new Object[10];
        size = 0;
    }

    // Вспомогательный метод для сравнения двух объектов
    // Использует Comparable интерфейс для определения порядка элементов
    @SuppressWarnings("unchecked")
    private int compare(Object x, Object y) {
        return ((Comparable<Object>) x).compareTo(y);
    }

    // Бинарный поиск элемента в отсортированном массиве
    // Возвращает индекс элемента если найден, или отрицательное значение если не найден
    private int findIndex(Object o) {
        int lo = 0;          // Нижняя граница поиска
        int hi = size - 1;   // Верхняя граница поиска

        // Классический бинарный поиск
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;  // Безопасное вычисление середины (без переполнения)
            int cmp = compare(a[mid], o);  // Сравнение среднего элемента с искомым

            if (cmp == 0) return mid;      // Элемент найден
            if (cmp < 0) lo = mid + 1;     // Искомый элемент больше - ищем в правой половине
            else hi = mid - 1;             // Искомый элемент меньше - ищем в левой половине
        }

        // Элемент не найден - возвращаем точку вставки в закодированном виде
        return -(lo + 1);  // Формула для декодирования: позиция вставки = -returnValue - 1
    }

    // Метод для обеспечения достаточной емкости массива
    private void ensureCapacity(int minCapacity) {
        // Если текущей емкости достаточно, ничего не делаем
        if (a.length >= minCapacity) return;

        // Увеличиваем емкость в 2 раза, но не меньше требуемой
        int newCap = a.length * 2;
        if (newCap < minCapacity) newCap = minCapacity;

        // Создаем новый массив и копируем в него элементы
        Object[] na = new Object[newCap];
        System.arraycopy(a, 0, na, 0, size);
        a = na;
    }

    // Строковое представление множества
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');

        // Проходим по всем элементам массива
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");  // Разделитель между элементами
            sb.append(a[i]);             // Добавляем строковое представление элемента
        }

        sb.append(']');
        return sb.toString();
    }

    // Возвращает количество элементов в множестве
    public int size() {
        return size;
    }

    // Очищает множество - удаляет все элементы
    public void clear() {
        // Обнуляем ссылки для помощи garbage collector
        for (int i = 0; i < size; i++) {
            a[i] = null;
        }
        size = 0;
    }

    // Проверяет, пусто ли множество
    public boolean isEmpty() {
        return size == 0;
    }

    // Добавляет элемент в множество
    @SuppressWarnings("unchecked")
    public boolean add(E e) {
        // MyTreeSet не поддерживает null элементы
        if (e == null) throw new NullPointerException();

        // Ищем позицию для вставки
        int idx = findIndex(e);

        // Если элемент уже существует (неотрицательный индекс), возвращаем false
        if (idx >= 0) return false;

        // Декодируем позицию вставки из отрицательного значения
        int pos = -idx - 1;

        // Обеспечиваем достаточную емкость массива
        ensureCapacity(size + 1);

        // Сдвигаем элементы вправо для освобождения места
        if (pos < size) {
            System.arraycopy(a, pos, a, pos + 1, size - pos);
        }

        // Вставляем новый элемент
        a[pos] = e;
        size++;
        return true;
    }

    // Удаляет элемент из множества
    public boolean remove(Object o) {
        // MyTreeSet не поддерживает null элементы
        if (o == null) return false;

        int idx;
        try {
            // Пытаемся найти индекс элемента
            idx = findIndex(o);
        } catch (ClassCastException ex) {
            // Если объект не Comparable или несовместимого типа, он не может быть в множестве
            return false;
        }

        // Если элемент не найден, возвращаем false
        if (idx < 0) return false;

        // Вычисляем количество элементов после удаляемого
        int tail = size - idx - 1;

        // Сдвигаем элементы влево для заполнения пустого места
        if (tail > 0) {
            System.arraycopy(a, idx + 1, a, idx, tail);
        }

        // Обнуляем последнюю ссылку и уменьшаем размер
        a[--size] = null;
        return true;
    }

    // Проверяет, содержится ли элемент в множестве
    public boolean contains(Object o) {
        // MyTreeSet не поддерживает null элементы
        if (o == null) return false;

        try {
            // Пытаемся найти элемент с обработкой возможных исключений сравнения
            return findIndex(o) >= 0;
        } catch (ClassCastException ex) {
            // Если объект не Comparable или несовместимого типа, он не может быть в множестве
            return false;
        }
    }

    // Проверяет, содержатся ли все элементы коллекции в множестве
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    // Добавляет все элементы из коллекции в множество
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            // Если хотя бы один элемент был добавлен, устанавливаем флаг changed
            if (add(e)) changed = true;
        }
        return changed;
    }

    // Удаляет из множества все элементы, содержащиеся в указанной коллекции
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object e : c) {
            // Если хотя бы один элемент был удален, устанавливаем флаг changed
            if (remove(e)) changed = true;
        }
        return changed;
    }

    // Удаляет из множества все элементы, НЕ содержащиеся в указанной коллекции
    // Сохраняет только элементы, которые есть в коллекции c (пересечение)
    public boolean retainAll(Collection<?> c) {
        Object[] na = new Object[a.length];  // Новый массив для результатов
        int ns = 0;                          // Новый размер

        // Проходим по всем элементам текущего множества
        for (int i = 0; i < size; i++) {
            Object v = a[i];
            // Если элемент содержится в коллекции c, сохраняем его
            if (c.contains(v)) {
                na[ns++] = v;
            }
        }

        // Если размер не изменился, множество не изменилось
        if (ns == size) return false;

        // Заменяем старый массив новым и обновляем размер
        a = na;
        size = ns;
        return true;
    }

    // Не реализованные методы интерфейса Set

    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    public <T> T[] toArray(T[] ts) {
        throw new UnsupportedOperationException();
    }

    public boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    public int hashCode() {
        throw new UnsupportedOperationException();
    }
}
