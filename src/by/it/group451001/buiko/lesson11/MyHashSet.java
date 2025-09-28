package by.it.group451001.buiko.lesson11;

import java.util.Collection;
import java.util.Set;
import java.util.Iterator;

public class MyHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    // Внутренний класс для узла односвязного списка
    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
        }
    }

    private Node<E>[] table;
    private int size;

    // Конструктор
    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    // Вспомогательный метод для вычисления индекса в таблице
    private int getIndex(Object key) {
        if (key == null) return 0;
        int hash = key.hashCode();
        // Приведение хеш-кода к неотрицательному значению
        return (hash & 0x7FFFFFFF) % table.length;
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
    public boolean contains(Object o) {
        int index = getIndex(o);
        Node<E> current = table[index];

        // Поиск элемента в цепочке коллизий
        while (current != null) {
            if (objectsEqual(o, current.data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        int index = getIndex(e);
        Node<E> current = table[index];

        // Проверка на существование элемента
        while (current != null) {
            if (objectsEqual(e, current.data)) {
                return false; // Элемент уже существует
            }
            current = current.next;
        }

        // Добавление нового элемента в начало цепочки
        Node<E> newNode = new Node<>(e);
        newNode.next = table[index];
        table[index] = newNode;
        size++;

        // Проверка необходимости рехеширования
        if ((float) size / table.length > LOAD_FACTOR) {
            resize();
        }

        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = getIndex(o);
        Node<E> current = table[index];
        Node<E> prev = null;

        // Поиск и удаление элемента из цепочки
        while (current != null) {
            if (objectsEqual(o, current.data)) {
                if (prev == null) {
                    // Удаление первого элемента цепочки
                    table[index] = current.next;
                } else {
                    // Удаление из середины или конца
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    // Вспомогательный метод для сравнения объектов с учетом null
    private boolean objectsEqual(Object a, Object b) {
        if (a == null) return b == null;
        return a.equals(b);
    }

    // Метод для увеличения размера таблицы при переполнении
    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        int newCapacity = oldTable.length * 2;
        table = (Node<E>[]) new Node[newCapacity];

        // Перехеширование всех элементов
        for (Node<E> node : oldTable) {
            Node<E> current = node;
            while (current != null) {
                Node<E> next = current.next;
                int newIndex = getIndex(current.data);

                // Вставка в новую таблицу
                current.next = table[newIndex];
                table[newIndex] = current;

                current = next;
            }
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        boolean firstElement = true;

        // Обход всех бакетов хеш-таблицы
        for (Node<E> node : table) {
            Node<E> current = node;
            while (current != null) {
                if (!firstElement) {
                    sb.append(", ");
                }
                sb.append(current.data);
                firstElement = false;
                current = current.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // Необязательные методы интерфейса Set (заглушки)

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
        throw new UnsupportedOperationException("containsAll not implemented");
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("addAll not implemented");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("retainAll not implemented");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("removeAll not implemented");
    }
}