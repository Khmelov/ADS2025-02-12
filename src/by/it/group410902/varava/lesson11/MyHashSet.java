package by.it.group410902.varava.lesson11;

import java.util.*;

// Реализация интерфейса Set с использованием хэш-таблицы
public class MyHashSet<E> implements Set<E> {
    // Константы по умолчанию
    private static final int DEFAULT_CAPACITY = 16;          // Начальная вместимость
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;  // Коэффициент загрузки

    // Основные поля класса
    private Node<E>[] table;      // Массив бакетов (корзин) для хранения элементов
    private int size;             // Количество элементов в множестве
    private final float loadFactor; // Коэффициент загрузки для определения момента рехеширования

    // Внутренний класс для представления узла односвязного списка (разрешение коллизий)
    private static class Node<E> {
        final E element;    // Хранимый элемент
        final int hash;     // Хэш-код элемента
        Node<E> next;       // Ссылка на следующий узел в цепочке

        // Конструктор узла
        Node(E element, int hash, Node<E> next) {
            this.element = element;
            this.hash = hash;
            this.next = next;
        }
    }

    // Конструкторы

    // Конструктор по умолчанию
    @SuppressWarnings("unchecked")
    public MyHashSet() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    // Конструктор с заданной начальной вместимостью
    @SuppressWarnings("unchecked")
    public MyHashSet(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    // Основной конструктор
    @SuppressWarnings("unchecked")
    public MyHashSet(int initialCapacity, float loadFactor) {
        // Проверка корректности параметров
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }

        this.loadFactor = loadFactor;
        // Создаем массив бакетов, используя переданную вместимость или значение по умолчанию
        this.table = (Node<E>[]) new Node[initialCapacity > 0 ? initialCapacity : DEFAULT_CAPACITY];
        this.size = 0;
    }

    // Основные методы интерфейса Set

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        int hash = hash(o);  // Вычисляем хэш-код объекта
        int index = getIndex(hash, table.length);  // Определяем индекс бакета

        // Проходим по цепочке в найденном бакете
        Node<E> current = table[index];
        while (current != null) {
            // Сравниваем хэш-коды и сами объекты
            if (current.hash == hash && objectsEqual(current.element, o)) {
                return true;  // Элемент найден
            }
            current = current.next;
        }
        return false;  // Элемент не найден
    }

    @Override
    public boolean add(E e) {
        int hash = hash(e);  // Вычисляем хэш-код элемента
        int index = getIndex(hash, table.length);  // Определяем индекс бакета

        // Проверяем, существует ли элемент уже в множестве
        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash && objectsEqual(current.element, e)) {
                return false; // Элемент уже существует, добавление не требуется
            }
            current = current.next;
        }

        // Добавляем новый элемент в начало цепочки (бакета)
        table[index] = new Node<>(e, hash, table[index]);
        size++;

        // Проверяем необходимость рехеширования (увеличения размера таблицы)
        if (size > table.length * loadFactor) {
            resize();
        }

        return true;  // Элемент успешно добавлен
    }

    @Override
    public boolean remove(Object o) {
        int hash = hash(o);  // Вычисляем хэш-код объекта
        int index = getIndex(hash, table.length);  // Определяем индекс бакета

        Node<E> current = table[index];
        Node<E> prev = null;

        // Проходим по цепочке в поисках элемента для удаления
        while (current != null) {
            if (current.hash == hash && objectsEqual(current.element, o)) {
                // Найден элемент для удаления
                if (prev == null) {
                    // Удаляем первый элемент в цепочке
                    table[index] = current.next;
                } else {
                    // Удаляем элемент из середины или конца цепочки
                    prev.next = current.next;
                }
                size--;
                return true;  // Элемент успешно удален
            }
            prev = current;
            current = current.next;
        }
        return false;  // Элемент не найден
    }

    @Override
    public void clear() {
        // Очищаем все бакеты, устанавливая ссылки в null
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;  // Сбрасываем счетчик элементов
    }

    // Строковое представление множества
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        boolean first = true;

        // Проходим по всем бакетам и всем элементам в цепочках
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

    // Реализация итератора для обхода элементов множества
    @Override
    public Iterator<E> iterator() {
        return new MyHashSetIterator();
    }

    // Внутренний класс итератора
    private class MyHashSetIterator implements Iterator<E> {
        private int currentIndex = 0;      // Текущий индекс в таблице
        private Node<E> currentNode = null; // Текущий обрабатываемый узел
        private Node<E> nextNode = findNextNode(); // Следующий узел для возврата

        @Override
        public boolean hasNext() {
            return nextNode != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            currentNode = nextNode;
            nextNode = findNextNode();
            return currentNode.element;
        }

        // Вспомогательный метод для поиска следующего узла
        private Node<E> findNextNode() {
            // Если есть следующий элемент в текущей цепочке
            if (currentNode != null && currentNode.next != null) {
                return currentNode.next;
            }

            // Ищем следующую непустую ячейку в таблице
            for (int i = currentIndex; i < table.length; i++) {
                if (table[i] != null) {
                    currentIndex = i + 1;  // Обновляем индекс для следующего поиска
                    return table[i];       // Возвращаем первый узел цепочки
                }
            }

            return null;  // Больше элементов нет
        }
    }

    // Методы преобразования в массив

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int index = 0;
        // Используем итератор для заполнения массива
        for (E element : this) {
            array[index++] = element;
        }
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        // Если переданный массив слишком мал, создаем новый
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }

        int index = 0;
        // Заполняем массив элементами множества
        for (E element : this) {
            a[index++] = (T) element;
        }

        // Устанавливаем маркер конца для массивов большего размера
        if (a.length > size) {
            a[size] = null;
        }

        return a;
    }

    // Методы для работы с коллекциями

    @Override
    public boolean containsAll(Collection<?> c) {
        // Проверяем, что все элементы коллекции содержатся в множестве
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // Добавляем все элементы коллекции
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true;  // Хотя бы один элемент был добавлен
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // Удаляем все элементы, не содержащиеся в переданной коллекции
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;  // Хотя бы один элемент был удален
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // Удаляем все элементы, содержащиеся в переданной коллекции
        boolean modified = false;
        for (Object element : c) {
            if (remove(element)) {
                modified = true;  // Хотя бы один элемент был удален
            }
        }
        return modified;
    }

    // Методы equals и hashCode

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;  // Сравнение с самим собой
        }
        if (!(o instanceof Set)) {
            return false;  // Объект не является множеством
        }

        Collection<?> c = (Collection<?>) o;
        if (c.size() != size()) {
            return false;  // Размеры различаются
        }

        // Проверяем, что все элементы совпадают
        try {
            return containsAll(c);
        } catch (ClassCastException | NullPointerException unused) {
            return false;  // Произошла ошибка при сравнении
        }
    }

    @Override
    public int hashCode() {
        // Вычисляем хэш-код как сумму хэш-кодов всех элементов
        int hashCode = 0;
        for (E element : this) {
            if (element != null) {
                hashCode += element.hashCode();
            }
        }
        return hashCode;
    }

    // Вспомогательные методы

    // Вычисление хэш-кода объекта (с обработкой null)
    private int hash(Object o) {
        return o == null ? 0 : o.hashCode();
    }

    // Определение индекса в таблице по хэш-коду
    private int getIndex(int hash, int tableLength) {
        // Используем побитовое И для получения положительного индекса и модуль для распределения
        return (hash & 0x7FFFFFFF) % tableLength;
    }

    // Безопасное сравнение объектов (с обработкой null)
    private boolean objectsEqual(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    // Увеличение размера таблицы (рехеширование)
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * 2;  // Удваиваем вместимость
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];

        // Перехеширование всех элементов в новую таблицу
        for (Node<E> node : table) {
            Node<E> current = node;
            while (current != null) {
                Node<E> next = current.next;  // Сохраняем ссылку на следующий узел
                // Вычисляем новый индекс для текущего элемента
                int newIndex = getIndex(current.hash, newCapacity);
                // Вставляем узел в начало цепочки нового бакета
                current.next = newTable[newIndex];
                newTable[newIndex] = current;
                current = next;  // Переходим к следующему узлу
            }
        }

        table = newTable;  // Заменяем старую таблицу новой
    }
}