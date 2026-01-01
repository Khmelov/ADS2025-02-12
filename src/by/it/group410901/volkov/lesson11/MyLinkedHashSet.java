package by.it.group410901.volkov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Реализация LinkedHashSet на основе хеш-таблицы с поддержкой порядка добавления
 * Сочетает преимущества HashSet (быстрый доступ O(1)) с сохранением порядка вставки
 * Использует двусвязный список для поддержания порядка элементов
 */
public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    // Массив buckets для хранения элементов (хеш-таблица)
    private Node<E>[] table;
    // Текущее количество элементов
    private int size;
    // Коэффициент загрузки для определения момента расширения
    private final float loadFactor;

    // Для поддержания порядка добавления используется двусвязный список
    // head - первый добавленный элемент, tail - последний
    private Node<E> head;
    private Node<E> tail;

    // Внутренний класс для узлов связного списка
    // Каждый узел участвует в двух структурах:
    // 1. Односвязный список в bucket для разрешения коллизий (next)
    // 2. Двусвязный список для поддержания порядка добавления (before, after)
    private static class Node<E> {
        E data;                    // Хранимое значение
        int hash;                  // Кэшированный хеш-код
        Node<E> next;              // Следующий узел в цепочке коллизий (bucket)
        Node<E> before, after;     // Предыдущий и следующий узлы в порядке добавления

        Node(E data, int hash, Node<E> next) {
            this.data = data;
            this.hash = hash;
            this.next = next;
        }
    }

    // Конструкторы
    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.table = new Node[DEFAULT_CAPACITY];
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int initialCapacity) {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.table = new Node[initialCapacity];
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);

        this.loadFactor = loadFactor;
        this.table = new Node[initialCapacity];
        this.size = 0;
    }

    public MyLinkedHashSet(Collection<? extends E> c) {
        this();
        addAll(c);
    }

    // Основные методы
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        if (table != null && size > 0) {
            for (int i = 0; i < table.length; i++) {
                table[i] = null;
            }
            size = 0;
            head = tail = null;
        }
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return containsNull();
        }

        int hash = hash(o);
        int index = indexFor(hash, table.length);

        Node<E> node = table[index];
        while (node != null) {
            if (node.hash == hash && (o == node.data || o.equals(node.data))) {
                return true;
            }
            node = node.next;
        }

        return false;
    }

    private boolean containsNull() {
        Node<E> node = table[0];
        while (node != null) {
            if (node.data == null) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            return addNull();
        }

        int hash = hash(e);
        int index = indexFor(hash, table.length);

        // Проверяем, существует ли уже элемент
        Node<E> node = table[index];
        while (node != null) {
            if (node.hash == hash && (e == node.data || e.equals(node.data))) {
                return false; // элемент уже существует
            }
            node = node.next;
        }

        // Добавляем новый элемент
        addNode(e, hash, index);
        return true;
    }

    private boolean addNull() {
        int index = 0;

        // Проверяем, существует ли null
        Node<E> node = table[index];
        while (node != null) {
            if (node.data == null) {
                return false;
            }
            node = node.next;
        }

        // Добавляем null
        addNode(null, 0, index);
        return true;
    }

    @SuppressWarnings("unchecked")
    private void addNode(E e, int hash, int index) {
        // Создаем новый узел
        Node<E> newNode = new Node<>(e, hash, table[index]);
        table[index] = newNode;

        // Добавляем в связный список для порядка
        linkNodeLast(newNode);

        size++;

        // Проверяем необходимость расширения таблицы
        if (size >= table.length * loadFactor) {
            resize(table.length * 2);
        }
    }

    /**
     * Добавляет узел в конец двусвязного списка для поддержания порядка добавления
     * Это гарантирует, что элементы будут итерироваться в порядке их добавления
     * @param node узел для добавления
     */
    private void linkNodeLast(Node<E> node) {
        if (head == null) {
            // Первый элемент - становится и head, и tail
            head = tail = node;
        } else {
            // Добавляем в конец списка
            tail.after = node;
            node.before = tail;
            tail = node;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return removeNull();
        }

        int hash = hash(o);
        int index = indexFor(hash, table.length);

        Node<E> prev = null;
        Node<E> node = table[index];

        while (node != null) {
            if (node.hash == hash && (o == node.data || o.equals(node.data))) {
                removeNode(node, prev, index);
                return true;
            }
            prev = node;
            node = node.next;
        }

        return false;
    }

    private boolean removeNull() {
        int index = 0;
        Node<E> prev = null;
        Node<E> node = table[index];

        while (node != null) {
            if (node.data == null) {
                removeNode(node, prev, index);
                return true;
            }
            prev = node;
            node = node.next;
        }

        return false;
    }

    private void removeNode(Node<E> node, Node<E> prev, int index) {
        // Удаляем из хеш-таблицы
        if (prev == null) {
            table[index] = node.next;
        } else {
            prev.next = node.next;
        }

        // Удаляем из связного списка порядка
        unlinkNode(node);

        size--;
    }

    // Удаление узла из списка порядка
    private void unlinkNode(Node<E> node) {
        Node<E> before = node.before;
        Node<E> after = node.after;

        if (before == null) {
            head = after;
        } else {
            before.after = after;
            node.before = null;
        }

        if (after == null) {
            tail = before;
        } else {
            after.before = before;
            node.after = null;
        }
    }

    // Вспомогательные методы для хеширования
    private int hash(Object key) {
        if (key == null) return 0;
        int h = key.hashCode();
        return h ^ (h >>> 16); // улучшение распределения
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        Node<E>[] oldTable = table;
        table = new Node[newCapacity];

        // Перехеширование всех элементов
        Node<E> current = head;
        head = tail = null;
        size = 0;

        while (current != null) {
            int hash = current.hash;
            int index = indexFor(hash, newCapacity);

            // Создаем новый узел для новой таблицы
            Node<E> newNode = new Node<>(current.data, hash, table[index]);
            table[index] = newNode;

            // Восстанавливаем порядок
            linkNodeLast(newNode);

            size++;
            current = current.after;
        }
    }

    // Методы для работы с коллекциями
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

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

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    // Метод toString() для вывода в порядке добавления
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        Node<E> current = head;
        while (current != null) {
            sb.append(current.data == this ? "(this Collection)" : current.data);
            if (current.after != null) {
                sb.append(", ");
            }
            current = current.after;
        }

        sb.append("]");
        return sb.toString();
    }

    // Итератор для обхода в порядке добавления
    @Override
    public Iterator<E> iterator() {
        return new LinkedHashIterator();
    }

    private class LinkedHashIterator implements Iterator<E> {
        private Node<E> next = head;
        private Node<E> current = null;

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            current = next;
            next = next.after;
            return current.data;
        }

        @Override
        public void remove() {
            if (current == null) {
                throw new IllegalStateException();
            }
            MyLinkedHashSet.this.remove(current.data);
            current = null;
        }
    }

    // Остальные методы интерфейса Set
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int i = 0;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            array[i++] = it.next();
        }
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        }

        int i = 0;
        for (E element : this) {
            a[i++] = (T) element;
        }

        if (a.length > size) {
            a[size] = null;
        }

        return a;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Set)) return false;

        Set<?> other = (Set<?>) o;
        if (size != other.size()) return false;

        return containsAll(other);
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (E element : this) {
            if (element != null) {
                hashCode += element.hashCode();
            }
        }
        return hashCode;
    }
}