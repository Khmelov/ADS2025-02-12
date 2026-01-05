package by.it.group410902.varava.lesson11;

import java.util.*;

/**
 * Кастомная реализация LinkedHashSet - хеш-таблицы с сохранением порядка добавления элементов.
 * Сочетает преимущества хеш-таблицы (быстрый доступ) и связанного списка (сохранение порядка).
 */
public class MyLinkedHashSet<E> implements Set<E> {
    // Начальная емкость хеш-таблицы по умолчанию
    private static final int DEFAULT_CAPACITY = 16;

    // Коэффициент загрузки по умолчанию (когда нужно увеличивать таблицу)
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    // Хеш-таблица (массив бакетов)
    private Node<E>[] table;

    // Указатели на начало и конец двусвязного списка для сохранения порядка
    private Node<E> head; // первый добавленный элемент
    private Node<E> tail; // последний добавленный элемент

    // Количество элементов в множестве
    private int size;

    // Коэффициент загрузки для определения когда выполнять рехеширование
    private final float loadFactor;

    /**
     * Узел для хранения элементов. Содержит ссылки для двух структур:
     * - next: для цепочек коллизий в хеш-таблице
     * - before/after: для двусвязного списка порядка добавления
     */
    private static class Node<E> {
        final E element;    // хранимый элемент
        final int hash;     // хеш-код элемента (кешируется для производительности)
        Node<E> next;      // следующий узел в цепочке коллизий хеш-таблицы
        Node<E> before;    // предыдущий узел в порядке добавления
        Node<E> after;     // следующий узел в порядке добавления

        Node(E element, int hash, Node<E> next) {
            this.element = element;
            this.hash = hash;
            this.next = next;
        }
    }

    // Конструкторы

    /**
     * Конструктор по умолчанию - использует стандартные емкость и коэффициент загрузки
     */
    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Конструктор с заданной начальной емкостью
     * @param initialCapacity начальная емкость хеш-таблицы
     */
    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Основной конструктор с заданными емкостью и коэффициентом загрузки
     * @param initialCapacity начальная емкость хеш-таблицы
     * @param loadFactor коэффициент загрузки для рехеширования
     */
    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int initialCapacity, float loadFactor) {
        // Проверка валидности параметров
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }

        this.loadFactor = loadFactor;
        // Создаем хеш-таблицу с заданной емкостью или по умолчанию
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

    /**
     * Проверяет наличие элемента в множестве
     * Использует хеш-таблицу для быстрого доступа O(1) в среднем случае
     */
    @Override
    public boolean contains(Object o) {
        // Вычисляем хеш и индекс в таблице
        int hash = hash(o);
        int index = getIndex(hash, table.length);

        // Проходим по цепочке коллизий в найденном бакете
        Node<E> current = table[index];
        while (current != null) {
            // Сравниваем сначала хеши (быстро), потом элементы (медленно)
            if (current.hash == hash && objectsEqual(current.element, o)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Добавляет элемент в множество
     * Сохраняет порядок добавления через двусвязный список
     * @return true если элемент был добавлен, false если уже существовал
     */
    @Override
    public boolean add(E e) {
        int hash = hash(e);
        int index = getIndex(hash, table.length);

        // Проверяем, существует ли элемент уже в цепочке коллизий
        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash && objectsEqual(current.element, e)) {
                return false; // Элемент уже существует
            }
            current = current.next;
        }

        // Создаем новый узел и добавляем его в начало цепочки коллизий
        Node<E> newNode = new Node<>(e, hash, table[index]);

        // Добавляем узел в хеш-таблицу (в начало цепочки)
        table[index] = newNode;

        // Добавляем узел в конец двусвязного списка для сохранения порядка
        linkNodeLast(newNode);
        size++;

        // Проверяем необходимость рехеширования (увеличения таблицы)
        if (size > table.length * loadFactor) {
            resize();
        }

        return true;
    }

    /**
     * Удаляет элемент из множества
     * Удаляет как из хеш-таблицы, так и из списка порядка
     * @return true если элемент был удален, false если не найден
     */
    @Override
    public boolean remove(Object o) {
        int hash = hash(o);
        int index = getIndex(hash, table.length);

        Node<E> current = table[index];
        Node<E> prev = null;

        // Ищем элемент в цепочке коллизий
        while (current != null) {
            if (current.hash == hash && objectsEqual(current.element, o)) {
                // Удаляем из хеш-таблицы (цепочек коллизий)
                if (prev == null) {
                    // Удаляем первый элемент цепочки
                    table[index] = current.next;
                } else {
                    // Удаляем из середины/конца цепочки
                    prev.next = current.next;
                }

                // Удаляем из двусвязного списка порядка
                unlinkNode(current);
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    /**
     * Очищает множество - удаляет все элементы
     */
    @Override
    public void clear() {
        // Очищаем все бакеты хеш-таблицы
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        // Сбрасываем указатели списка порядка
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Возвращает строковое представление множества
     * Элементы выводятся в порядке их добавления (через двусвязный список)
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        boolean first = true;

        // Проходим по списку порядка от первого к последнему элементу
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

    // Реализация остальных методов интерфейса Set

    /**
     * Возвращает итератор для обхода элементов в порядке добавления
     */
    @Override
    public Iterator<E> iterator() {
        return new LinkedHashSetIterator();
    }

    /**
     * Итератор для обхода элементов в порядке их добавления
     * Использует двусвязный список для гарантии порядка
     */
    private class LinkedHashSetIterator implements Iterator<E> {
        private Node<E> next = head;    // Следующий элемент для возврата
        private Node<E> current = null; // Текущий элемент (для операции remove)

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            current = next; // Запоминаем текущий элемент
            next = next.after; // Переходим к следующему
            return current.element;
        }

        @Override
        public void remove() {
            if (current == null) {
                throw new IllegalStateException();
            }
            // Удаляем текущий элемент через метод внешнего класса
            MyLinkedHashSet.this.remove(current.element);
            current = null; // Запрещаем повторный remove
        }
    }

    /**
     * Возвращает массив содержащий все элементы множества в порядке добавления
     */
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int index = 0;
        // Используем итератор который гарантирует порядок добавления
        for (E element : this) {
            array[index++] = element;
        }
        return array;
    }

    /**
     * Возвращает массив содержащий все элементы множества
     * @param a массив в который будут помещены элементы (если достаточно большой)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            // Создаем новый массив нужного типа и размера
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }

        int index = 0;
        for (E element : this) {
            a[index++] = (T) element;
        }

        if (a.length > size) {
            a[size] = null; // Помечаем конец согласно контракту Collection
        }

        return a;
    }

    /**
     * Сравнивает это множество с другим объектом
     * Два множества равны если содержат одинаковые элементы (порядок не важен для equals)
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
     * Хеш-код равен сумме хеш-кодов всех элементов (как в HashSet)
     */
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

    // Вспомогательные методы для поддержания порядка (двусвязный список)

    /**
     * Добавляет узел в конец двусвязного списка порядка
     */
    private void linkNodeLast(Node<E> node) {
        if (tail == null) {
            // Если список пуст, новый узел становится и головой и хвостом
            head = node;
            tail = node;
        } else {
            // Добавляем после текущего хвоста
            tail.after = node;
            node.before = tail;
            tail = node; // Новый узел становится новым хвостом
        }
    }

    /**
     * Удаляет узел из двусвязного списка порядка
     */
    private void unlinkNode(Node<E> node) {
        Node<E> before = node.before;
        Node<E> after = node.after;

        // Обновляем ссылки предыдущего узла
        if (before == null) {
            // Удаляем голову списка
            head = after;
        } else {
            before.after = after;
        }

        // Обновляем ссылки следующего узла
        if (after == null) {
            // Удаляем хвост списка
            tail = before;
        } else {
            after.before = before;
        }

        // Очищаем ссылки удаляемого узла
        node.before = null;
        node.after = null;
    }

    // Вспомогательные методы для хеширования

    /**
     * Вычисляет хеш-код объекта (обрабатывает null)
     */
    private int hash(Object o) {
        return o == null ? 0 : o.hashCode();
    }

    /**
     * Вычисляет индекс в хеш-таблице по хеш-коду
     * Использует маску для обеспечения неотрицательного индекса
     */
    private int getIndex(int hash, int tableLength) {
        // 0x7FFFFFFF - это Integer.MAX_VALUE, убирает знаковый бит
        return (hash & 0x7FFFFFFF) % tableLength;
    }

    /**
     * Безопасное сравнение двух объектов (обрабатывает null)
     */
    private boolean objectsEqual(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    /**
     * Увеличивает размер хеш-таблицы и перераспределяет все элементы
     * Вызывается когда количество элементов превышает порог (емкость * loadFactor)
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * 2; // Увеличиваем емкость в 2 раза
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];

        // Перехеширование всех элементов в новую таблицу
        for (Node<E> node : table) {
            Node<E> current = node;
            while (current != null) {
                Node<E> next = current.next; // Сохраняем ссылку на следующий узел

                // Вычисляем новый индекс для текущего узла
                int newIndex = getIndex(current.hash, newCapacity);

                // Вставляем узел в начало цепочки нового бакета
                current.next = newTable[newIndex];
                newTable[newIndex] = current;

                current = next; // Переходим к следующему узлу старой цепочки
            }
        }

        table = newTable; // Заменяем старую таблицу новой
    }
}