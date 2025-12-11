package by.it.group410902.derzhavskaya_e.lesson11;

// Реализация множества на основе хеш-таблицы со списками
public class MyHashSet<E> implements java.util.Set<E> {

    // Узел односвязного списка для хранения элементов
    private static class Node<E> {
        E value;
        Node<E> next;
        Node(E value) {
            this.value = value;
        }
    }

    private Node<E>[] table; // массив списков
    private int size;        // количество элементов в множестве

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[16]; // начальный размер хеш-таблицы
        size = 0;
    }

    // Возвращает количество элементов
    @Override
    public int size() {
        return size;
    }

    // Проверка на пустоту
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Очистка множества
    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    // Добавление элемента (если такого ещё нет)
    @Override
    public boolean add(E value) {
        int index = getIndex(value);
        Node<E> current = table[index];

        // проверка на дубликат
        while (current != null) {
            if (equalsValue(current.value, value))
                return false;
            current = current.next;
        }

        // вставка нового элемента в начало цепочки
        Node<E> newNode = new Node<>(value);
        newNode.next = table[index];
        table[index] = newNode;
        size++;

        // при необходимости — можно добавить resize()
        return true;
    }

    // Проверка, содержится ли элемент
    @Override
    public boolean contains(Object value) {
        int index = getIndex(value);
        Node<E> current = table[index];

        while (current != null) {
            if (equalsValue(current.value, value))
                return true;
            current = current.next;
        }
        return false;
    }

    // Удаление элемента, если он есть
    @Override
    public boolean remove(Object value) {
        int index = getIndex(value);
        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if (equalsValue(current.value, value)) {
                if (prev == null)
                    table[index] = current.next;
                else
                    prev.next = current.next;
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    // Преобразование множества в строку (формат: [a, b, c])
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;

        for (Node<E> bucket : table) {
            Node<E> current = bucket;
            while (current != null) {
                if (!first) sb.append(", ");
                sb.append(current.value);
                first = false;
                current = current.next;
            }
        }
        return sb.append("]").toString();
    }

    // Вычисление индекса в массиве по хеш-коду
    private int getIndex(Object value) {
        if (value == null) return 0;
        return (value.hashCode() & 0x7fffffff) % table.length;
    }

    // Проверка эквивалентности элементов (учёт null)
    private boolean equalsValue(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    @Override public java.util.Iterator<E> iterator() { return null; }
    @Override public Object[] toArray() { return null; }
    @Override public <T> T[] toArray(T[] a) { return null; }
    @Override public boolean containsAll(java.util.Collection<?> c) { return false; }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { return false; }
    @Override public boolean retainAll(java.util.Collection<?> c) { return false; }
    @Override public boolean removeAll(java.util.Collection<?> c) { return false; }
}
