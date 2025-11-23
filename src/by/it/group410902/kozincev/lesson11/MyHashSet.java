package by.it.group410902.kozincev.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

// Вспомогательный класс для односвязного списка
class Node<E> {
    final int hash;
    final E value;
    Node<E> next;

    Node(int hash, E value, Node<E> next) {
        this.hash = hash;
        this.value = value;
        this.next = next;
    }
}

public class MyHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private Node<E>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        // Инициализация массива "корзин"
        this.table = new Node[DEFAULT_CAPACITY];
        this.size = 0;
    }

    // Вспомогательный метод для получения индекса корзины
    // Использует хеш-код объекта и размер массива (length - 1)
    private int getIndex(int hash) {
        return hash & (table.length - 1);
    }

    // Вспомогательный метод для получения хеш-кода (защита от null)
    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    // ================== Обязательные методы ==================

    @Override
    public boolean add(E e) {
        int h = hash(e);
        int index = getIndex(h);
        Node<E> current = table[index];

        // 1. Проверка на дубликат в текущем списке
        while (current != null) {
            // Сначала сравниваем хеши для быстрого отсева, затем сами объекты
            if (current.hash == h && (current.value == e || (e != null && e.equals(current.value)))) {
                return false; // Элемент уже существует
            }
            current = current.next;
        }

        // 2. Добавление нового элемента в начало списка
        Node<E> newNode = new Node<>(h, e, table[index]);
        table[index] = newNode; // Новый элемент становится головой списка
        size++;

        return true;
    }

    @Override
    public boolean remove(Object o) {
        int h = hash(o);
        int index = getIndex(h);
        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            // Проверяем, совпадает ли текущий узел с удаляемым объектом
            if (current.hash == h && (current.value == o || (o != null && o.equals(current.value)))) {
                // Найден! Удаляем узел
                if (prev == null) {
                    // Узел является головой списка
                    table[index] = current.next;
                } else {
                    // Узел где-то в середине/конце
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

    @Override
    public boolean contains(Object o) {
        int h = hash(o);
        int index = getIndex(h);
        Node<E> current = table[index];

        while (current != null) {
            // Проверка на совпадение
            if (current.hash == h && (current.value == o || (o != null && o.equals(current.value)))) {
                return true; // Элемент найден
            }
            current = current.next;
        }
        return false; // Элемент не найден
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        // Очищаем все ссылки в массиве
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        int count = 0;

        // Итерация по всем корзинам
        for (Node<E> head : table) {
            Node<E> current = head;
            // Итерация по односвязному списку в корзине
            while (current != null) {
                sb.append(current.value);
                count++;
                if (count < size) {
                    sb.append(", "); // Разделитель: запятая с пробелом
                }
                current = current.next;
            }
        }

        sb.append(']');
        return sb.toString();
    }


    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
}