package by.it.group451001.yarkovich.lesson11;

import java.util.Collection;
import java.util.Iterator;

/**
 * Реализация множества на основе хеш-таблицы с сохранением порядка добавления элементов.
 * Использует двусвязный список для поддержания порядка итерации.
 */
public class MyLinkedHashSet<E> implements Collection<E> {
    // Начальные константы
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    // Основные поля
    private Node<E>[] table;            // Массив бакетов для хеш-таблицы
    private int size;                   // Количество элементов
    private final float loadFactor;     // Коэффициент загрузки для рехешинга

    // Ссылки для поддержания порядка добавления (двусвязный список)
    private Node<E> head;               // Первый добавленный элемент
    private Node<E> tail;               // Последний добавленный элемент

    /**
     * Внутренний класс узла с дополнительными ссылками для порядка добавления
     */
    private static class Node<E> {
        final E element;
        final int hash;
        Node<E> next;           // Следующий узел в цепочке коллизий
        Node<E> after;          // Следующий узел в порядке добавления
        Node<E> before;         // Предыдущий узел в порядке добавления

        Node(E element, int hash, Node<E> next) {
            this.element = element;
            this.hash = hash;
            this.next = next;
        }
    }

    // Конструкторы
    public MyLinkedHashSet() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyLinkedHashSet(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Основной конструктор - создает пустое множество с сохранением порядка
     */
    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);

        this.loadFactor = loadFactor;
        this.table = (Node<E>[]) new Node[initialCapacity > 0 ? initialCapacity : DEFAULT_CAPACITY];
        this.size = 0;
        this.head = null;
        this.tail = null;
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
    @SuppressWarnings("unchecked")
    public void clear() {
        // Очищаем хеш-таблицу
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        // Очищаем ссылки порядка добавления
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Добавляет элемент в множество, сохраняя порядок добавления
     */
    @Override
    public boolean add(E element) {
        int hash = hash(element);
        int index = indexFor(hash, table.length);

        // Проверяем, нет ли уже такого элемента
        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash &&
                    (element == current.element ||
                            (element != null && element.equals(current.element)))) {
                return false; // Элемент уже существует
            }
            current = current.next;
        }

        // Создаем новый узел и добавляем в хеш-таблицу
        Node<E> newNode = new Node<>(element, hash, table[index]);
        table[index] = newNode;

        // Добавляем узел в конец списка порядка добавления
        linkNodeLast(newNode);
        size++;

        // Проверяем необходимость рехешинга
        if (size > table.length * loadFactor) {
            resize();
        }

        return true;
    }

    /**
     * Удаляет элемент из множества с сохранением целостности порядка
     */
    @Override
    public boolean remove(Object object) {
        int hash = hash(object);
        int index = indexFor(hash, table.length);

        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if (current.hash == hash &&
                    (object == current.element ||
                            (object != null && object.equals(current.element)))) {

                // Удаляем из хеш-таблицы
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }

                // Удаляем из списка порядка добавления
                unlinkNode(current);
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }

        return false;
    }

    @Override
    public boolean contains(Object object) {
        int hash = hash(object);
        int index = indexFor(hash, table.length);

        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash &&
                    (object == current.element ||
                            (object != null && object.equals(current.element)))) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    /**
     * Возвращает строковое представление в порядке добавления элементов
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        boolean first = true;

        // Проходим по всем элементам в порядке добавления
        while (current != null) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(current.element);
            first = false;
            current = current.after;
        }

        sb.append("]");
        return sb.toString();
    }

    // Вспомогательные методы для поддержания порядка

    /**
     * Добавляет узел в конец списка порядка добавления
     */
    private void linkNodeLast(Node<E> node) {
        if (tail == null) {
            // Если список пуст, новый узел становится и головой и хвостом
            head = node;
            tail = node;
        } else {
            // Добавляем в конец списка
            tail.after = node;
            node.before = tail;
            tail = node;
        }
    }

    /**
     * Удаляет узел из списка порядка добавления
     */
    private void unlinkNode(Node<E> node) {
        Node<E> before = node.before;
        Node<E> after = node.after;

        if (before == null) {
            // Удаляем первый элемент
            head = after;
        } else {
            before.after = after;
            node.before = null;
        }

        if (after == null) {
            // Удаляем последний элемент
            tail = before;
        } else {
            after.before = before;
            node.after = null;
        }
    }

    // Остальные вспомогательные методы и методы Collection аналогично MyHashSet
    // (hash, indexFor, resize, и нереализованные методы)

    private int hash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * 2;
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];

        for (Node<E> node : table) {
            Node<E> current = node;
            while (current != null) {
                Node<E> next = current.next;
                int newIndex = indexFor(current.hash, newCapacity);
                current.next = newTable[newIndex];
                newTable[newIndex] = current;
                current = next;
            }
        }

        table = newTable;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Iterator not implemented");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("toArray not implemented");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("toArray not implemented");
    }

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
        Object[] temp = new Object[size];
        int tempSize = 0;

        // Собираем элементы, которые есть в коллекции c
        Node<E> current = head;
        while (current != null) {
            if (c.contains(current.element)) {
                temp[tempSize++] = current.element;
            } else {
                modified = true;
            }
            current = current.after;
        }

        // Очищаем и перестраиваем
        if (modified) {
            clear();
            for (int i = 0; i < tempSize; i++) {
                add((E) temp[i]);
            }
        }

        return modified;
    }
}