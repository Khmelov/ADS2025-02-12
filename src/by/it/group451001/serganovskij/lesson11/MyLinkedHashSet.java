package by.it.group451001.serganovskij.lesson11;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

public class MyLinkedHashSet<E> implements Set<E> {

    // Внутренний класс Node для представления элементов
    private static class Node<E> {
        final E value;          // Хранимое значение
        Node<E> next;           // Ссылка на следующий узел в цепочке коллизий
        Node<E> before, after;  // Ссылки для поддержания порядка добавления

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] table;    // Массив бакетов (корзин) для хеш-таблицы
    private Node<E> head, tail; // Указатели на начало и конец списка порядка добавления
    private int size;           // Общее количество элементов
    private boolean hasNull;    // Флаг наличия null элемента
    private static final int DEFAULT_CAP = 16;    // Начальная емкость
    private static final float LOAD_FACTOR = 0.75f; // Коэффициент загрузки

    // Конструктор с указанием начальной емкости
    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int capacity) {
        if (capacity < 1) capacity = DEFAULT_CAP;
        table = (Node<E>[]) new Node[capacity]; // Создание массива бакетов
        head = tail = null;     // Инициализация двусвязного списка
        size = 0;
        hasNull = false;
    }

    // Конструктор по умолчанию
    public MyLinkedHashSet() { this(DEFAULT_CAP); }

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

        // Перехеширование всех элементов (порядок в двусвязном списке сохраняется)
        for (int i = 0; i < cap(); i++) {
            Node<E> n = table[i];
            while (n != null) {
                Node<E> next = n.next; // Сохраняем ссылку на следующий в цепочке
                int idx = indexFor(n.value, newCap); // Новый индекс
                n.next = newTable[idx]; // Вставляем в начало новой цепочки
                newTable[idx] = n;
                n = next;
            }
        }
        table = newTable;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    // Возвращает количество элементов
    @Override
    public int size() { return size; }

    // Полная очистка множества
    @Override
    public void clear() {
        for (int i = 0; i < cap(); i++) table[i] = null; // Обнуляем все бакеты
        head = tail = null;     // Очищаем двусвязный список
        size = 0;
        hasNull = false;
    }

    // Проверка на пустоту
    @Override
    public boolean isEmpty() { return size == 0; }

    // Добавление элемента в множество с сохранением порядка
    @Override
    public boolean add(E e) {
        if (e == null) {
            // Обработка null элемента отдельно
            if (hasNull) return false; // Уже есть null
            hasNull = true;
            linkNode(null); // Добавляем null в список порядка
            size++;
            return true;
        }

        int idx = indexFor(e, cap());

        // Проверка на дубликат в цепочке
        for (Node<E> n = table[idx]; n != null; n = n.next) {
            if (e.equals(n.value)) return false; // Элемент уже существует
        }

        // Добавление нового элемента в начало цепочки коллизий
        Node<E> node = new Node<>(e, table[idx]);
        table[idx] = node;
        linkNode(node); // Добавление в двусвязный список порядка
        size++;

        // Проверка необходимости увеличения размера таблицы
        if (size > cap() * LOAD_FACTOR) resize();
        return true;
    }

    // Добавление узла в конец двусвязного списка (для поддержания порядка)
    private void linkNode(Node<E> node) {
        if (node == null) {
            // Специальная обработка для null - создаем фиктивный узел
            Node<E> fake = new Node<>(null, null);
            fake.before = tail;
            if (tail != null) tail.after = fake;
            tail = fake;
            if (head == null) head = fake;
            return;
        }

        // Стандартное добавление узла в конец списка
        node.before = tail;
        if (tail != null) tail.after = node;
        tail = node;
        if (head == null) head = node;
    }

    // Удаление узла из двусвязного списка
    private void unlinkNode(Node<E> node) {
        // Обновление ссылок соседних узлов
        if (node.before != null) node.before.after = node.after;
        else head = node.after; // Если удаляем голову

        if (node.after != null) node.after.before = node.before;
        else tail = node.before; // Если удаляем хвост
    }

    // Удаление элемента из множества
    @Override
    public boolean remove(Object o) {
        if (o == null) {
            // Удаление null элемента
            if (!hasNull) return false;
            hasNull = false;

            // Поиск и удаление фиктивного узла null из списка порядка
            Node<E> cur = head;
            while (cur != null) {
                if (cur.value == null) {
                    unlinkNode(cur);
                    break;
                }
                cur = cur.after;
            }
            size--;
            return true;
        }

        int idx = indexFor(o, cap());
        Node<E> prev = null;
        Node<E> cur = table[idx];

        // Поиск элемента в цепочке коллизий
        while (cur != null) {
            if (o.equals(cur.value)) {
                // Удаление из цепочки коллизий
                if (prev == null) table[idx] = cur.next;
                else prev.next = cur.next;

                unlinkNode(cur); // Удаление из списка порядка
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
        // Линейный поиск в цепочке коллизий
        for (Node<E> n = table[idx]; n != null; n = n.next) {
            if (o.equals(n.value)) return true;
        }
        return false;
    }

    // Строковое представление множества в порядке добавления
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;

        // Обход двусвязного списка в порядке добавления
        Node<E> cur = head;
        while (cur != null) {
            if (!first) sb.append(", ");
            sb.append(String.valueOf(cur.value));
            first = false;
            cur = cur.after;
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

        // Обход двусвязного списка в порядке добавления
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.after; // Сохраняем ссылку перед возможным удалением
            if (!c.contains(cur.value)) {
                remove(cur.value);
                changed = true;
            }
            cur = next;
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