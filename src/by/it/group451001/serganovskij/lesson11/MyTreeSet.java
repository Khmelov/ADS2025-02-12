package by.it.group451001.serganovskij.lesson11;

import java.util.Collection;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    // ВНУТРЕННИЙ МАССИВ для хранения элементов в отсортированном порядке
    private Object[] a;

    // ТЕКУЩЕЕ КОЛИЧЕСТВО элементов в множестве
    private int size;

    // КОНСТРУКТОР - создает пустое множество с начальной емкостью
    public MyTreeSet() {
        a = new Object[10]; // Начальная емкость массива
        size = 0;           // Начальный размер
    }

    /////////////////////////////////////////////////////////////////////////
    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
    /////////////////////////////////////////////////////////////////////////

    // СРАВНЕНИЕ ДВУХ ОБЪЕКТОВ с приведением типов
    @SuppressWarnings("unchecked")
    private int compare(Object x, Object y) {
        return ((Comparable<Object>) x).compareTo(y); // Используем Comparable интерфейс
    }

    // БИНАРНЫЙ ПОИСК элемента в отсортированном массиве
    // Возвращает индекс элемента если найден, или отрицательное значение если не найден
    private int findIndex(Object o) {
        int lo = 0, hi = size - 1; // Границы поиска

        while (lo <= hi) {
            int mid = (lo + hi) >>> 1; // Безопасное вычисление середины
            int cmp = compare(a[mid], o); // Сравнение элемента с искомым

            if (cmp == 0) return mid;     // Элемент найден
            if (cmp < 0) lo = mid + 1;    // Искомый элемент больше - ищем в правой половине
            else hi = mid - 1;            // Искомый элемент меньше - ищем в левой половине
        }

        // Элемент не найден, возвращаем позицию для вставки
        return -(lo + 1);
    }

    // УВЕЛИЧЕНИЕ ЕМКОСТИ МАССИВА при необходимости
    private void ensureCapacity(int minCapacity) {
        if (a.length >= minCapacity) return; // Емкости достаточно

        int newCap = a.length * 2; // Удваиваем емкость
        if (newCap < minCapacity) newCap = minCapacity; // Если нужно больше

        Object[] na = new Object[newCap]; // Новый массив
        System.arraycopy(a, 0, na, 0, size); // Копирование элементов
        a = na; // Замена старого массива новым
    }

    /////////////////////////////////////////////////////////////////////////
    // ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ ДЛЯ РЕАЛИЗАЦИИ
    /////////////////////////////////////////////////////////////////////////

    // СТРОКОВОЕ ПРЕДСТАВЛЕНИЕ множества в формате [элемент1, элемент2, ...]
    public String toString() {
        if (size == 0) return "[]"; // Пустое множество

        StringBuilder sb = new StringBuilder();
        sb.append('[');

        // Вывод элементов в порядке возрастания (они уже отсортированы)
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", "); // Разделитель между элементами
            sb.append(a[i]);            // Добавление элемента
        }

        sb.append(']');
        return sb.toString();
    }

    // ПОЛУЧЕНИЕ РАЗМЕРА множества
    public int size() {
        return size;
    }

    // ПОЛНАЯ ОЧИСТКА множества
    public void clear() {
        for (int i = 0; i < size; i++) a[i] = null; // Очистка ссылок
        size = 0; // Сброс размера
    }

    // ПРОВЕРКА НА ПУСТОЕ МНОЖЕСТВО
    public boolean isEmpty() {
        return size == 0;
    }

    // ДОБАВЛЕНИЕ ЭЛЕМЕНТА в множество (с сохранением порядка)
    @SuppressWarnings("unchecked")
    public boolean add(E e) {
        if (e == null) throw new NullPointerException(); // Null не допускается

        int idx = findIndex(e); // Поиск позиции элемента

        if (idx >= 0) return false; // Элемент уже существует

        // Вычисление позиции для вставки
        int pos = -idx - 1;

        ensureCapacity(size + 1); // Проверка емкости

        // Сдвиг элементов для освобождения места
        if (pos < size) {
            System.arraycopy(a, pos, a, pos + 1, size - pos);
        }

        a[pos] = e; // Вставка элемента
        size++;     // Увеличение размера
        return true;
    }

    // УДАЛЕНИЕ ЭЛЕМЕНТА из множества
    public boolean remove(Object o) {
        if (o == null) return false; // Null не обрабатывается

        int idx;
        try {
            idx = findIndex(o); // Поиск элемента
        } catch (ClassCastException ex) {
            return false; // Несовместимые типы
        }

        if (idx < 0) return false; // Элемент не найден

        // Сдвиг элементов для заполнения пустоты
        int tail = size - idx - 1;
        if (tail > 0) {
            System.arraycopy(a, idx + 1, a, idx, tail);
        }

        a[--size] = null; // Очистка последней ссылки
        return true;
    }

    // ПРОВЕРКА НАЛИЧИЯ ЭЛЕМЕНТА в множестве
    public boolean contains(Object o) {
        if (o == null) return false; // Null не обрабатывается

        try {
            return findIndex(o) >= 0; // Поиск элемента
        } catch (ClassCastException ex) {
            return false; // Несовместимые типы
        }
    }

    /////////////////////////////////////////////////////////////////////////
    // МЕТОДЫ ДЛЯ РАБОТЫ С КОЛЛЕКЦИЯМИ
    /////////////////////////////////////////////////////////////////////////

    // ПРОВЕРКА НАЛИЧИЯ ВСЕХ ЭЛЕМЕНТОВ КОЛЛЕКЦИИ
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false; // Если хотя бы один элемент отсутствует
        }
        return true;
    }

    // ДОБАВЛЕНИЕ ВСЕХ ЭЛЕМЕНТОВ КОЛЛЕКЦИИ
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false; // Флаг изменения
        for (E e : c) {
            if (add(e)) changed = true; // Добавляем каждый элемент
        }
        return changed;
    }

    // УДАЛЕНИЕ ВСЕХ ЭЛЕМЕНТОВ, ПРИСУТСТВУЮЩИХ В КОЛЛЕКЦИИ
    public boolean removeAll(Collection<?> c) {
        boolean changed = false; // Флаг изменения
        for (Object e : c) {
            if (remove(e)) changed = true; // Удаляем каждый элемент
        }
        return changed;
    }

    // СОХРАНЕНИЕ ТОЛЬКО ЭЛЕМЕНТОВ, ПРИСУТСТВУЮЩИХ В КОЛЛЕКЦИИ
    public boolean retainAll(Collection<?> c) {
        // Создание временного массива для элементов, которые нужно сохранить
        Object[] na = new Object[a.length];
        int ns = 0; // Новый размер

        // Проход по всем элементам множества
        for (int i = 0; i < size; i++) {
            Object v = a[i];
            if (c.contains(v)) { // Если элемент присутствует в коллекции
                na[ns++] = v;    // Сохраняем его
            }
        }

        // Если размер не изменился - множество не изменилось
        if (ns == size) return false;

        // Замена старого массива новым
        a = na;
        size = ns;
        return true;
    }

    /////////////////////////////////////////////////////////////////////////
    // НЕРЕАЛИЗОВАННЫЕ МЕТОДЫ (заглушки)
    /////////////////////////////////////////////////////////////////////////

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