package by.it.group410901.volkov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Реализация HashSet на основе хеш-таблицы с односвязными списками для разрешения коллизий
 * БЕЗ использования других классов стандартной библиотеки
 * Элементы хранятся в массиве с адресацией по хеш-коду
 */
public class MyHashSet<E> implements Set<E> {
    // Начальная емкость хеш-таблицы (должна быть степенью двойки для эффективного индексирования)
    private static final int DEFAULT_CAPACITY = 16;
    // Коэффициент загрузки: когда количество элементов превышает table.length * loadFactor,
    // таблица расширяется для поддержания производительности
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    // Массив buckets (корзин) для хранения элементов
    // Каждый bucket - это голова односвязного списка для разрешения коллизий
    private Node<E>[] table;
    // Текущее количество элементов в множестве
    private int size;
    // Коэффициент загрузки для определения момента расширения таблицы
    private final float loadFactor;

    // Внутренний класс для узлов односвязного списка
    // Используется для разрешения коллизий хеш-кодов (chaining)
    private static class Node<E> {
        E data;              // Хранимое значение элемента
        int hash;            // Хеш-код элемента (кэшируется для быстрого сравнения)
        Node<E> next;        // Ссылка на следующий узел в цепочке коллизий

        Node(E data, int hash, Node<E> next) {
            this.data = data;
            this.hash = hash;
            this.next = next;
        }
    }

    // Конструкторы
    @SuppressWarnings("unchecked")
    public MyHashSet() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.table = new Node[DEFAULT_CAPACITY];
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    public MyHashSet(int initialCapacity) {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.table = new Node[initialCapacity];
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    public MyHashSet(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);

        this.loadFactor = loadFactor;
        this.table = new Node[initialCapacity];
        this.size = 0;
    }

    public MyHashSet(Collection<? extends E> c) {
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
        }
    }

    /**
     * Проверяет наличие элемента в множестве
     * Время выполнения: O(1) в среднем, O(n) в худшем случае (все элементы в одной корзине)
     * @param o элемент для поиска
     * @return true если элемент найден, false иначе
     */
    @Override
    public boolean contains(Object o) {
        // null элементы обрабатываются отдельно (хранятся в bucket[0])
        if (o == null) {
            return containsNull();
        }

        // Вычисляем хеш-код и индекс корзины
        int hash = hash(o);
        int index = indexFor(hash, table.length);

        // Проходим по цепочке коллизий в найденной корзине
        Node<E> node = table[index];
        while (node != null) {
            // Сначала проверяем хеш-код (быстрое сравнение),
            // затем проверяем равенство объектов (== для ссылок, equals для значений)
            if (node.hash == hash && (o == node.data || o.equals(node.data))) {
                return true;
            }
            node = node.next;
        }

        return false;
    }

    /**
     * Проверяет наличие null элемента
     * null элементы всегда хранятся в первой корзине (index 0)
     * @return true если null найден, false иначе
     */
    private boolean containsNull() {
        Node<E> node = table[0];
        while (node != null) {
            // Для null используем только сравнение по ссылке (==)
            if (node.data == null) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    /**
     * Добавляет элемент в множество
     * Время выполнения: O(1) в среднем, O(n) в худшем случае
     * @param e элемент для добавления
     * @return true если элемент был добавлен, false если уже существовал
     */
    @Override
    public boolean add(E e) {
        // null элементы обрабатываются отдельно
        if (e == null) {
            return addNull();
        }

        // Вычисляем хеш-код и индекс корзины
        int hash = hash(e);
        int index = indexFor(hash, table.length);

        // Проверяем, существует ли уже элемент (множество не содержит дубликатов)
        Node<E> node = table[index];
        while (node != null) {
            if (node.hash == hash && (e == node.data || e.equals(node.data))) {
                return false; // элемент уже существует, добавление не требуется
            }
            node = node.next;
        }

        // Элемент не найден, добавляем новый
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

    /**
     * Внутренний метод для добавления узла в хеш-таблицу
     * Новый узел добавляется в начало цепочки коллизий для O(1) вставки
     * @param e добавляемый элемент
     * @param hash хеш-код элемента
     * @param index индекс корзины в таблице
     */
    @SuppressWarnings("unchecked")
    private void addNode(E e, int hash, int index) {
        // Создаем новый узел и добавляем в начало списка (O(1) операция)
        // Старая голова списка становится следующим узлом нового узла
        Node<E> newNode = new Node<>(e, hash, table[index]);
        table[index] = newNode;

        size++;

        // Проверяем необходимость расширения таблицы для поддержания производительности
        // Когда коэффициент загрузки превышает порог, таблица удваивается
        if (size >= table.length * loadFactor) {
            resize(table.length * 2);
        }
    }

    /**
     * Удаляет элемент из множества
     * Время выполнения: O(1) в среднем, O(n) в худшем случае
     * @param o элемент для удаления
     * @return true если элемент был удален, false если не найден
     */
    @Override
    public boolean remove(Object o) {
        // null элементы обрабатываются отдельно
        if (o == null) {
            return removeNull();
        }

        // Вычисляем хеш-код и индекс корзины
        int hash = hash(o);
        int index = indexFor(hash, table.length);

        // Ищем элемент в цепочке коллизий, сохраняя ссылку на предыдущий узел
        // для корректного удаления из односвязного списка
        Node<E> prev = null;
        Node<E> node = table[index];

        while (node != null) {
            if (node.hash == hash && (o == node.data || o.equals(node.data))) {
                // Элемент найден, удаляем его из цепочки
                removeNode(node, prev, index);
                return true;
            }
            prev = node;
            node = node.next;
        }

        return false; // элемент не найден
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

    /**
     * Внутренний метод для удаления узла из цепочки коллизий
     * @param node узел для удаления
     * @param prev предыдущий узел в цепочке (null если node - голова списка)
     * @param index индекс корзины в таблице
     */
    private void removeNode(Node<E> node, Node<E> prev, int index) {
        // Удаляем узел из односвязного списка
        if (prev == null) {
            // Удаляемый узел - голова списка, обновляем ссылку в таблице
            table[index] = node.next;
        } else {
            // Пропускаем удаляемый узел в цепочке
            prev.next = node.next;
        }

        size--;
    }

    /**
     * Вычисляет хеш-код объекта с улучшением распределения
     * XOR с правым сдвигом на 16 бит улучшает распределение для таблиц с размером степени двойки
     * @param key объект для хеширования
     * @return улучшенный хеш-код
     */
    private int hash(Object key) {
        if (key == null) return 0;
        int h = key.hashCode();
        // Улучшение распределения: XOR старших и младших битов
        // Это уменьшает коллизии для таблиц с размером степени двойки
        return h ^ (h >>> 16);
    }

    /**
     * Вычисляет индекс корзины для хеш-кода
     * Использует побитовую операцию AND вместо деления по модулю для производительности
     * Работает корректно только если length - степень двойки
     * @param hash хеш-код
     * @param length размер таблицы (должен быть степенью двойки)
     * @return индекс корзины в диапазоне [0, length-1]
     */
    private int indexFor(int hash, int length) {
        // Побитовая операция AND быстрее деления по модулю
        // hash & (length - 1) эквивалентно hash % length для степени двойки
        return hash & (length - 1);
    }

    /**
     * Расширяет хеш-таблицу до нового размера и перехеширует все элементы
     * Это дорогая операция O(n), но выполняется редко благодаря коэффициенту загрузки
     * @param newCapacity новый размер таблицы (должен быть степенью двойки)
     */
    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        Node<E>[] oldTable = table;
        table = new Node[newCapacity];
        size = 0;

        // Перехеширование всех элементов: каждый элемент получает новый индекс
        // из-за изменения размера таблицы
        for (Node<E> head : oldTable) {
            Node<E> current = head;
            while (current != null) {
                int hash = current.hash;
                // Вычисляем новый индекс для элемента в расширенной таблице
                int index = indexFor(hash, newCapacity);

                // Создаем новый узел и добавляем в начало цепочки новой корзины
                Node<E> newNode = new Node<>(current.data, hash, table[index]);
                table[index] = newNode;

                size++;
                current = current.next;
            }
        }
    }

    // Метод toString() для вывода элементов
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        boolean first = true;
        for (Node<E> head : table) {
            Node<E> current = head;
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(current.data);
                first = false;
                current = current.next;
            }
        }

        sb.append("]");
        return sb.toString();
    }

    // Методы для работы с коллекциями (необязательные, но могут быть полезны)
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

    // Итератор
    @Override
    public Iterator<E> iterator() {
        return new HashSetIterator();
    }

    /**
     * Итератор для обхода всех элементов множества
     * Порядок обхода не гарантирован (зависит от хеш-кодов)
     */
    private class HashSetIterator implements Iterator<E> {
        private int currentIndex = 0;        // Текущий индекс корзины в таблице
        private Node<E> currentNode = null; // Текущий узел в цепочке коллизий
        private Node<E> lastReturned = null; // Последний возвращенный узел (для remove)

        public HashSetIterator() {
            // Находим первый элемент при создании итератора
            findNext();
        }

        /**
         * Находит следующий элемент для итерации
         * Сначала проверяет оставшиеся узлы в текущей цепочке,
         * затем переходит к следующей непустой корзине
         */
        private void findNext() {
            // Если есть текущий узел, переходим к следующему в списке
            if (currentNode != null && currentNode.next != null) {
                currentNode = currentNode.next;
                return;
            }

            // Ищем следующий непустой bucket (корзину)
            currentNode = null;
            while (currentIndex < table.length) {
                if (table[currentIndex] != null) {
                    // Нашли непустую корзину, начинаем обход её цепочки
                    currentNode = table[currentIndex];
                    currentIndex++;
                    return;
                }
                currentIndex++;
            }
        }

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            lastReturned = currentNode;
            E data = currentNode.data;
            findNext();
            return data;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            MyHashSet.this.remove(lastReturned.data);
            lastReturned = null;
        }
    }

    // Остальные методы интерфейса Set
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int index = 0;
        for (Node<E> head : table) {
            Node<E> current = head;
            while (current != null) {
                array[index++] = current.data;
                current = current.next;
            }
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

        int index = 0;
        for (Node<E> head : table) {
            Node<E> current = head;
            while (current != null) {
                a[index++] = (T) current.data;
                current = current.next;
            }
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
        for (Node<E> head : table) {
            Node<E> current = head;
            while (current != null) {
                if (current.data != null) {
                    hashCode += current.data.hashCode();
                }
                current = current.next;
            }
        }
        return hashCode;
    }
}

