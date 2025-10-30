package by.it.group451001.yarkovich.lesson11;

import java.util.Collection;
import java.util.Iterator;

/**
 * Реализация множества на основе хеш-таблицы с методом цепочек для разрешения коллизий.
 * Элементы хранятся в бакетах, каждый бакет представляет собой односвязный список.
 */
public class MyHashSet<E> implements Collection<E> {
    // Начальные константы
    private static final int DEFAULT_CAPACITY = 16;      // Начальный размер хеш-таблицы
    private static final float DEFAULT_LOAD_FACTOR = 0.75f; // Коэффициент заполнения для рехешинга

    // Основные поля
    private Node<E>[] table;    // Массив бакетов (корзин) для хранения элементов
    private int size;           // Количество элементов в множестве
    private final float loadFactor; // Коэффициент загрузки

    /**
     * Внутренний класс для представления узла односвязного списка.
     * Используется для разрешения коллизий хеш-кодов.
     */
    private static class Node<E> {
        final E element;    // Хранимый элемент
        final int hash;     // Сохраненный хеш-код элемента (для быстрого доступа)
        Node<E> next;       // Ссылка на следующий узел в цепочке коллизий

        Node(E element, int hash, Node<E> next) {
            this.element = element;
            this.hash = hash;
            this.next = next;
        }
    }

    // Конструкторы
    public MyHashSet() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashSet(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Основной конструктор - создает пустое множество с указанными параметрами
     */
    @SuppressWarnings("unchecked")
    public MyHashSet(int initialCapacity, float loadFactor) {
        // Проверка корректности входных параметров
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);

        this.loadFactor = loadFactor;
        // Создаем массив бакетов. Если capacity = 0, используем значение по умолчанию
        this.table = (Node<E>[]) new Node[initialCapacity > 0 ? initialCapacity : DEFAULT_CAPACITY];
        this.size = 0;
    }

    /**
     * Возвращает количество элементов в множестве
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Проверяет, пусто ли множество
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Удаляет все элементы из множества
     */
    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        // Проходим по всем бакетам и обнуляем их
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    /**
     * Добавляет элемент в множество, если его еще нет
     * @return true если элемент был добавлен, false если элемент уже существовал
     */
    @Override
    public boolean add(E element) {
        // Вычисляем хеш-код элемента и определяем бакет
        int hash = hash(element);
        int index = indexFor(hash, table.length);

        // Проверяем, нет ли уже такого элемента в цепочке коллизий
        Node<E> current = table[index];
        while (current != null) {
            // Сравниваем сначала по хеш-коду, потом по equals (для оптимизации)
            if (current.hash == hash &&
                    (element == current.element ||
                            (element != null && element.equals(current.element)))) {
                return false; // Элемент уже существует
            }
            current = current.next;
        }

        // Добавляем новый элемент в начало цепочки (самый эффективный способ)
        table[index] = new Node<>(element, hash, table[index]);
        size++;

        // Проверяем, не нужно ли увеличить таблицу (рехеширование)
        if (size > table.length * loadFactor) {
            resize();
        }

        return true;
    }

    /**
     * Удаляет элемент из множества, если он присутствует
     * @return true если элемент был удален, false если элемент не найден
     */
    @Override
    public boolean remove(Object object) {
        int hash = hash(object);
        int index = indexFor(hash, table.length);

        Node<E> current = table[index];
        Node<E> prev = null;

        // Ищем элемент в цепочке
        while (current != null) {
            if (current.hash == hash &&
                    (object == current.element ||
                            (object != null && object.equals(current.element)))) {
                // Нашли элемент для удаления
                if (prev == null) {
                    // Удаляем первый элемент цепочки
                    table[index] = current.next;
                } else {
                    // Удаляем элемент из середины или конца цепочки
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }

        return false; // Элемент не найден
    }

    /**
     * Проверяет, содержится ли элемент в множестве
     */
    @Override
    public boolean contains(Object object) {
        int hash = hash(object);
        int index = indexFor(hash, table.length);

        // Ищем элемент в цепочке коллизий для данного бакета
        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash &&
                    (object == current.element ||
                            (object != null && object.equals(current.element)))) {
                return true; // Элемент найден
            }
            current = current.next;
        }

        return false; // Элемент не найден
    }

    /**
     * Возвращает строковое представление множества
     * Элементы выводятся в произвольном порядке
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        boolean first = true;

        // Обходим все бакеты и все элементы в цепочках коллизий
        for (Node<E> node : table) {
            Node<E> current = node;
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(current.element);
                first = false;
                current = current.next;
            }
        }

        sb.append("]");
        return sb.toString();
    }

    // Вспомогательные методы

    /**
     * Вычисляет хеш-код с обработкой null (null имеет хеш-код 0)
     */
    private int hash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    /**
     * Определяет индекс бакета для данного хеш-кода
     * Используется побитовое И вместо деления по модулю для эффективности
     */
    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    /**
     * Увеличивает размер хеш-таблицы в 2 раза и перераспределяет элементы
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * 2;
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];

        // Перебираем все элементы старой таблицы
        for (Node<E> node : table) {
            Node<E> current = node;
            while (current != null) {
                Node<E> next = current.next; // Сохраняем ссылку на следующий элемент

                // Вычисляем новый индекс для текущего элемента
                int newIndex = indexFor(current.hash, newCapacity);

                // Вставляем элемент в начало цепочки новой таблицы
                current.next = newTable[newIndex];
                newTable[newIndex] = current;

                current = next; // Переходим к следующему элементу
            }
        }

        table = newTable; // Заменяем старую таблицу новой
    }

    // Методы интерфейса Collection, которые не реализуются в данной версии

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
        // Простая реализация - проверяем каждый элемент коллекции
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
        // Добавляем каждый элемент коллекции
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
        // Удаляем каждый элемент коллекции
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
        // Создаем временный массив для элементов, которые нужно сохранить
        Object[] temp = new Object[size];
        int tempSize = 0;

        // Собираем элементы, которые есть в коллекции c
        for (Node<E> node : table) {
            Node<E> current = node;
            while (current != null) {
                if (c.contains(current.element)) {
                    temp[tempSize++] = current.element;
                } else {
                    modified = true;
                }
                current = current.next;
            }
        }

        // Очищаем множество и добавляем только сохраненные элементы
        if (modified) {
            clear();
            for (int i = 0; i < tempSize; i++) {
                add((E) temp[i]);
            }
        }

        return modified;
    }
}