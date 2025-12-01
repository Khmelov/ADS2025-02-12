package by.it.group410901.getmanchuk.lesson11;

import java.util.*;

// Элементы хранятся в массиве data, который всегда отсортирован.
//
//При добавлении выполняется бинарный поиск для нахождения позиции (findIndex).
//
//Если элемент найден — не добавляем (уникальность).
//
//Если не найден — вставляем в правильное место с помощью System.arraycopy.
//
//Для сортировки используется либо переданный Comparator

// создаём шаблонный класс, реализующий интерфейс Set

public class MyTreeSet<E> implements Set<E> {

    // храним компаратор, массив данных и размер множества.
    private final Comparator<? super E> comparator;
    private Object[] data = new Object[0];
    private int size = 0;

    // Конструктор без компаратора (используется Comparable)
    public MyTreeSet() { this.comparator = null; }

    // Конструктор с компаратором
    public MyTreeSet(Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    // универсальный метод сравнения (по компаратору или Comparable).
    @SuppressWarnings("unchecked")
    private int cmp(E a, E b) {
        if (comparator != null) return comparator.compare(a, b);
        return ((Comparable<E>)a).compareTo(b);
    }

    // бинарный поиск: ищем элемент и возвращаем его индекс,
    private int findIndex(E e) {
        int left = 0, right = size - 1;
        while (left <= right) {
            int mid = (left + right) >>> 1;
            E val = (E) data[mid];
            int cmp = cmp(e, val);
            if (cmp == 0) return mid;
            if (cmp < 0) right = mid - 1;
            else left = mid + 1;
        }
        return -left - 1;
    }

    // Добавление элемента
    @Override
    public boolean add(E e) {
        int pos = findIndex(e);
        if (pos >= 0) return false;
        pos = -pos - 1;
        data = Arrays.copyOf(data, size + 1);
        System.arraycopy(data, pos, data, pos + 1, size - pos);
        data[pos] = e;
        size++;
        return true;
    }

    // Проверка наличия элемента
    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        E e = (E) o;
        return findIndex(e) >= 0;
    }

    // Удаление элемента
    @Override
    public boolean remove(Object o) {
        @SuppressWarnings("unchecked")
        E e = (E) o;
        int pos = findIndex(e);
        if (pos < 0) return false;
        System.arraycopy(data, pos + 1, data, pos, size - pos - 1);
        size--;
        data = Arrays.copyOf(data, size);
        return true;
    }

    // Возврат количества элементов
    @Override
    public int size() { return size; }

    // Проверка на пустоту
    @Override
    public boolean isEmpty() { return size == 0; }

    // Очистка множества
    @Override
    public void clear() {
        data = new Object[0];
        size = 0;
    }

    // Итератор по возрастанию
    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            int i = 0;
            public boolean hasNext() { return i < size; }
            @SuppressWarnings("unchecked")
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                return (E) data[i++];
            }
        };
    }

    // Преобразование в строку
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    // Остальные методы Set
    @Override public Object[] toArray() { return Arrays.copyOf(data, size); }

    @SuppressWarnings("unchecked")
    @Override public <T> T[] toArray(T[] a) {
        if (a.length < size) return (T[]) Arrays.copyOf(data, size, a.getClass());
        System.arraycopy(data, 0, a, 0, size);
        return a;
    }

    @Override public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) changed |= add(e);
        return changed;
    }

    @Override public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) changed |= remove(o);
        return changed;
    }

    @Override public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : toArray()) if (!c.contains(o)) { remove(o); changed = true; }
        return changed;
    }
}