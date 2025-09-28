package by.it.group451001.buiko.lesson11;

import java.util.Collection;
import java.util.Set;
import java.util.Iterator;

public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    // Внутренний класс для узла с поддержкой порядка добавления
    private static class LinkedNode<E> {
        E data;
        LinkedNode<E> next; // следующий в цепочке коллизий
        LinkedNode<E> after; // следующий в порядке добавления
        LinkedNode<E> before; // предыдущий в порядке добавления

        LinkedNode(E data) {
            this.data = data;
        }
    }

    private LinkedNode<E>[] table;
    private LinkedNode<E> head; // первый добавленный элемент
    private LinkedNode<E> tail; // последний добавленный элемент
    private int size;

    // Конструктор
    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (LinkedNode<E>[]) new LinkedNode[DEFAULT_CAPACITY];
        head = null;
        tail = null;
        size = 0;
    }

    // Вычисление индекса в таблице
    private int getIndex(Object key) {
        if (key == null) return 0;
        int hash = key.hashCode();
        return (hash & 0x7FFFFFFF) % table.length;
    }

    // Сравнение объектов с учетом null
    private boolean objectsEqual(Object a, Object b) {
        if (a == null) return b == null;
        return a.equals(b);
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
        LinkedNode<E> current = table[index];

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
        LinkedNode<E> current = table[index];

        // Проверка на существование элемента
        while (current != null) {
            if (objectsEqual(e, current.data)) {
                return false;
            }
            current = current.next;
        }

        // Создание нового узла
        LinkedNode<E> newNode = new LinkedNode<>(e);

        // Добавление в цепочку коллизий
        newNode.next = table[index];
        table[index] = newNode;

        // Добавление в конец списка порядка
        if (tail == null) {
            // Первый элемент
            head = newNode;
            tail = newNode;
        } else {
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode;
        }

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
        LinkedNode<E> current = table[index];
        LinkedNode<E> prev = null;

        // Поиск элемента в цепочке коллизий
        while (current != null) {
            if (objectsEqual(o, current.data)) {
                // Удаление из цепочки коллизий
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }

                // Удаление из списка порядка
                if (current.before != null) {
                    current.before.after = current.after;
                } else {
                    head = current.after;
                }

                if (current.after != null) {
                    current.after.before = current.before;
                } else {
                    tail = current.before;
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
        head = null;
        tail = null;
        size = 0;
    }

    // Рехеширование таблицы
    @SuppressWarnings("unchecked")
    private void resize() {
        LinkedNode<E>[] oldTable = table;
        int newCapacity = oldTable.length * 2;
        table = (LinkedNode<E>[]) new LinkedNode[newCapacity];

        // Перехеширование элементов (порядок в LinkedNode сохраняется)
        for (LinkedNode<E> node : oldTable) {
            LinkedNode<E> current = node;
            while (current != null) {
                LinkedNode<E> next = current.next;
                int newIndex = getIndex(current.data);

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
        LinkedNode<E> current = head;
        boolean first = true;

        while (current != null) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(current.data);
            first = false;
            current = current.after;
        }
        sb.append("]");
        return sb.toString();
    }

    // Реализация дополнительных методов для уровня B

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
        LinkedNode<E> current = head;

        // Проход по всем элементам в порядке добавления
        while (current != null) {
            LinkedNode<E> next = current.after;
            if (!c.contains(current.data)) {
                remove(current.data);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    // Оставшиеся методы интерфейса Set (заглушки)

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
}