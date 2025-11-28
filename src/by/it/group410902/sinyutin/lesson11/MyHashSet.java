package by.it.group410902.sinyutin.lesson11;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Реализация Set<E> на основе хеш-таблицы (массива)
 * и односвязного списка для разрешения коллизий (Separate Chaining).
 * Запрещено использование классов стандартной библиотеки, кроме базовых.
 */
public class MyHashSet<E> implements Set<E> {

    // --- 1. Внутренний класс для узлов односвязного списка (Node) ---
    private static class Node<E> {
        E value;
        Node<E> next;

        public Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    // --- 2. Поля MyHashSet ---

    private static final int DEFAULT_CAPACITY = 16;

    // Массив бакетов, хранит головы односвязных списков.
    // Используем Object[] из-за ограничений Java на создание массивов дженериков,
    // и выполняем unchecked cast.
    private Node<E>[] buckets;

    private int size;

    // --- 3. Конструкторы ---

    public MyHashSet() {
        // Создание массива и кастинг (unchecked cast)
        this.buckets = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        this.size = 0;
    }

    // --- 4. Приватный метод для вычисления индекса ---

    /**
     * Вычисляет индекс бакета для объекта o.
     * Используется маскирование (0x7FFFFFFF) для обработки возможного отрицательного hashCode.
     */
    private int getIndex(Object o) {
        if (o == null) {
            // Null всегда хранится в бакете 0
            return 0;
        }
        // Используем побитовое И с 0x7FFFFFFF для получения абсолютного значения
        // (избегаем Math.abs, так как это библиотечный метод).
        // Затем берем остаток от деления на длину массива.
        return (o.hashCode() & 0x7FFFFFFF) % buckets.length;
    }

    // --- 5. Обязательные методы Set<E> ---

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        // Просто пересоздаем массив и сбрасываем размер.
        this.buckets = (Node<E>[]) new Node[buckets.length];
        this.size = 0;
    }

    @Override
    public boolean contains(Object o) {
        int index = getIndex(o);
        Node<E> current = buckets[index];

        while (current != null) {
            // Корректное сравнение с null-значением
            if (o == null) {
                if (current.value == null) return true;
            } else {
                if (o.equals(current.value)) return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        int index = getIndex(e);
        Node<E> current = buckets[index];

        // 1. Проверка на дубликаты
        while (current != null) {
            if ((e == null && current.value == null) || (e != null && e.equals(current.value))) {
                return false; // Элемент уже существует
            }
            current = current.next;
        }

        // 2. Добавление нового элемента в начало списка (голова)
        // Новый узел становится головой, указывая на старую голову.
        Node<E> newNode = new Node<>(e, buckets[index]);
        buckets[index] = newNode;

        this.size++;

        // В реальной реализации здесь должно быть расширение (resize), но для уровня А
        // и без использования стандартных библиотек это не требуется.

        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = getIndex(o);
        Node<E> current = buckets[index];
        Node<E> previous = null;

        while (current != null) {
            boolean found = (o == null && current.value == null) || (o != null && o.equals(current.value));

            if (found) {
                if (previous == null) {
                    // Удаляем голову списка
                    buckets[index] = current.next;
                } else {
                    // Удаляем узел в середине/конце списка
                    previous.next = current.next;
                }
                this.size--;
                return true;
            }

            previous = current;
            current = current.next;
        }

        return false; // Элемент не найден
    }

    // --- 6. Метод toString() для корректного вывода ---

    @Override
    public String toString() {
        // Используем StringBuilder (разрешено, так как это не коллекция)
        StringBuilder sb = new StringBuilder();
        sb.append('[');

        boolean firstElement = true;

        for (int i = 0; i < buckets.length; i++) {
            Node<E> current = buckets[i];
            while (current != null) {
                if (!firstElement) {
                    sb.append(", "); // Разделитель: запятая с пробелом
                }
                sb.append(current.value);
                firstElement = false;
                current = current.next;
            }
        }

        sb.append(']');
        return sb.toString();
    }


    // --- 7. Минимальная реализация остальных методов Set<E> (обязательно для интерфейса) ---
    // Поскольку эти методы не были указаны как обязательные и требуют использования
    // других коллекций/итераторов, мы бросаем исключение.

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Метод не реализован в соответствии с заданием");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Метод не реализован в соответствии с заданием");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Метод не реализован в соответствии с заданием");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Метод не реализован в соответствии с заданием");
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("Метод не реализован в соответствии с заданием");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Метод не реализован в соответствии с заданием");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Метод не реализован в соответствии с заданием");
    }
}
