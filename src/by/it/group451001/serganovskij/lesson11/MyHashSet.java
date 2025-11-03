package by.it.group451001.serganovskij.lesson11;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyHashSet<E> implements Set<E> {

    // Внутренний класс Node для представления элементов связного списка
    private static class Node<E> {
        final E value;      // Хранимое значение
        Node<E> next;       // Ссылка на следующий узел

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] table;        // Массив бакетов (корзин)
    private int nonNullCount;       // Количество непустых бакетов (для resize)
    private int size;               // Общее количество элементов
    private boolean hasNull;        // Флаг наличия null элемента
    private static final int DEFAULT_CAP = 16;    // Начальная емкость
    private static final float LOAD_FACTOR = 0.75f; // Коэффициент загрузки

    // Конструктор с указанием начальной емкости
    @SuppressWarnings("unchecked")
    public MyHashSet(int capacity) {
        if (capacity < 1) capacity = DEFAULT_CAP;
        table = (Node<E>[]) new Node[capacity]; // Создание массива с приведением типа
        nonNullCount = 0;
        size = 0;
        hasNull = false;
    }

    // Конструктор по умолчанию
    public MyHashSet() {
        this(DEFAULT_CAP);
    }

    // Получение текущей емкости таблицы
    private int cap() { return table.length; }

    // Вычисление индекса в таблице для объекта
    private int indexFor(Object o, int len) {
        int h = (o == null) ? 0 : o.hashCode(); // Для null используем 0
        return (h & 0x7fffffff) % len; // Убираем знак и берем по модулю
    }

    // Увеличение размера таблицы при переполнении
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCap = cap() << 1; // Удваиваем емкость
        Node<E>[] newTable = (Node<E>[]) new Node[newCap];

        // Перехеширование всех элементов
        for (int i = 0; i < cap(); i++) {
            Node<E> n = table[i];
            while (n != null) {
                Node<E> next = n.next; // Сохраняем ссылку на следующий
                int idx = indexFor(n.value, newCap); // Новый индекс
                n.next = newTable[idx]; // Вставляем в начало цепочки
                newTable[idx] = n;
                n = next;
            }
        }
        table = newTable;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    // Возвращает количество элементов (включая null)
    @Override
    public int size() {
        return size;
    }

    // Полная очистка множества
    @Override
    public void clear() {
        for (int i = 0; i < cap(); i++) table[i] = null; // Обнуляем все бакеты
        nonNullCount = 0;
        size = 0;
        hasNull = false;
    }

    // Проверка на пустоту
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Добавление элемента в множество
    @Override
    public boolean add(E e) {
        if (e == null) {
            // Обработка null элемента отдельно
            if (hasNull) return false; // Уже есть null
            hasNull = true;
            size++;
            return true;
        }

        int idx = indexFor(e, cap());

        // Проверка на дубликат в цепочке
        for (Node<E> n = table[idx]; n != null; n = n.next) {
            if (e.equals(n.value)) return false; // Элемент уже существует
        }

        // Добавление нового элемента в начало цепочки
        Node<E> node = new Node<>(e, table[idx]);
        table[idx] = node;
        nonNullCount++;
        size++;

        // Проверка необходимости увеличения размера таблицы
        if (nonNullCount > (int)(cap() * LOAD_FACTOR)) resize();
        return true;
    }

    // Удаление элемента из множества
    @Override
    public boolean remove(Object o) {
        if (o == null) {
            // Удаление null элемента
            if (!hasNull) return false;
            hasNull = false;
            size--;
            return true;
        }

        int idx = indexFor(o, cap());
        Node<E> prev = null;
        Node<E> cur = table[idx];

        // Поиск элемента в цепочке
        while (cur != null) {
            if (o.equals(cur.value)) {
                // Удаление элемента из цепочки
                if (prev == null) table[idx] = cur.next; // Удаление из начала
                else prev.next = cur.next; // Удаление из середины/конца

                nonNullCount--;
                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false; // Элемент не найден
    }

    // Проверка наличия элемента в множестве
    @Override
    public boolean contains(Object o) {
        if (o == null) return hasNull; // Проверка null элемента

        int idx = indexFor(o, cap());
        // Линейный поиск в цепочке
        for (Node<E> n = table[idx]; n != null; n = n.next) {
            if (o.equals(n.value)) return true;
        }
        return false;
    }

    // Строковое представление множества
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;

        // Добавление null элемента если есть
        if (hasNull) {
            sb.append("null");
            first = false;
        }

        // Обход всех бакетов и цепочек
        for (int i = 0; i < cap(); i++) {
            for (Node<E> n = table[i]; n != null; n = n.next) {
                if (!first) sb.append(", ");
                sb.append(String.valueOf(n.value));
                first = false;
            }
        }
        sb.append(']');
        return sb.toString();
    }

    /////////////////////////////////////////////////////////////////////////
    //////          Дополнительные реализованные методы              ///////
    /////////////////////////////////////////////////////////////////////////

    // Проверка наличия всех элементов коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    // Добавление всех элементов коллекции
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        boolean mod = false;
        for (E e : c) if (add(e)) mod = true;
        return mod;
    }

    // Удаление всех элементов, присутствующих в коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean mod = false;
        for (Object o : c) if (remove(o)) mod = true;
        return mod;
    }

    // Удаление всех элементов, кроме присутствующих в коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;

        // Обработка null элемента
        if (hasNull && !c.contains(null)) {
            hasNull = false;
            size--;
            changed = true;
        }

        // Обход всех бакетов и удаление элементов, которых нет в коллекции c
        for (int i = 0; i < cap(); i++) {
            Node<E> prev = null;
            Node<E> cur = table[i];
            while (cur != null) {
                if (!c.contains(cur.value)) {
                    // Удаление элемента
                    if (prev == null) table[i] = cur.next;
                    else prev.next = cur.next;
                    cur = (prev == null) ? table[i] : prev.next;
                    nonNullCount--;
                    size--;
                    changed = true;
                } else {
                    prev = cur;
                    cur = cur.next;
                }
            }
        }
        return changed;
    }

    /////////////////////////////////////////////////////////////////////////
    //////          Методы, которые не требуются для задания          ///////
    /////////////////////////////////////////////////////////////////////////

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean equals(Object o) { return super.equals(o); }
    @Override public int hashCode() { return super.hashCode(); }
}