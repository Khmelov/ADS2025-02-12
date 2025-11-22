package by.it.group410901.evtuhovskaya.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

//двусвязный список, элементы будут идти упорядоченное (по добавлению)

public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private Node<E> head; // первый элемент в порядке добавления
    private Node<E> tail; // последний элемент в порядке добавления
    private int size;
    private final float loadFactor;

    // Узел для двусвязного списка (порядок добавления) и односвязного (коллизии)
    private static class Node<E> {
        E item;
        Node<E> next;     // следующий в цепочке коллизий
        Node<E> before;   // предыдущий в порядке добавления
        Node<E> after;    // следующий в порядке добавления
        int hash;

        Node(int hash, E item, Node<E> next) {
            this.hash = hash;
            this.item = item;
            this.next = next;
        }
    }

    public MyLinkedHashSet() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyLinkedHashSet(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }

        this.loadFactor = loadFactor;
        this.table = new Node[initialCapacity];
        this.size = 0;
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
        if (size > 0) {
            for (int i = 0; i < table.length; i++) {
                table[i] = null;
            }
            head = tail = null;
            size = 0;
        }
    }

    @Override
    public boolean contains(Object element) {
        if (element == null) {
            return containsNull();
        }

        int hash = hash(element);
        int index = indexFor(hash, table.length);

        Node<E> node = table[index];
        while (node != null) {
            if (node.hash == hash && (element == node.item || element.equals(node.item))) {
                return true;
            }
            node = node.next;
        }

        return false;
    }

    private boolean containsNull() {
        Node<E> node = table[0];
        while (node != null) {
            if (node.item == null) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean add(E element) {
        if (element == null) {
            return addNull();
        }

        int hash = hash(element);
        int index = indexFor(hash, table.length);

        // Проверяем, есть ли уже такой элемент
        Node<E> node = table[index];
        while (node != null) {
            if (node.hash == hash && (element == node.item || element.equals(node.item))) {
                return false; // Элемент уже существует
            }
            node = node.next;
        }

        // Создаем новый узел
        Node<E> newNode = new Node<>(hash, element, table[index]);
        table[index] = newNode;

        // Добавляем в двусвязный список для сохранения порядка
        linkNodeLast(newNode);
        size++;

        // Проверяем необходимость расширения таблицы
        if (size > table.length * loadFactor) {
            resize();
        }

        return true;
    }

    private boolean addNull() {
        // Проверяем, есть ли уже null
        Node<E> node = table[0];
        while (node != null) {
            if (node.item == null) {
                return false;
            }
            node = node.next;
        }

        // Добавляем null
        Node<E> newNode = new Node<>(0, null, table[0]);
        table[0] = newNode;
        linkNodeLast(newNode);
        size++;

        if (size > table.length * loadFactor) {
            resize();
        }

        return true;
    }

    @Override
    public boolean remove(Object element) {
        if (element == null) {
            return removeNull();
        }

        int hash = hash(element);
        int index = indexFor(hash, table.length);

        Node<E> prev = null;
        Node<E> current = table[index];

        while (current != null) {
            if (current.hash == hash && (element == current.item || element.equals(current.item))) {
                // Удаляем из цепочки коллизий
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                // Удаляем из двусвязного списка
                unlinkNode(current);
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }

        return false;
    }

    private boolean removeNull() {
        Node<E> prev = null;
        Node<E> current = table[0];

        while (current != null) {
            if (current.item == null) {
                if (prev == null) {
                    table[0] = current.next;
                } else {
                    prev.next = current.next;
                }
                unlinkNode(current);
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }

        return false;
    }

    // Методы для работы с двусвязным списком (порядок добавления)

    private void linkNodeLast(Node<E> node) {
        if (tail == null) {
            // Первый элемент
            head = tail = node;
        } else {
            // Добавляем в конец
            tail.after = node;
            node.before = tail;
            tail = node;
        }
    }

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

    // Вспомогательные методы

    private int hash(Object key) {
        if (key == null) return 0;
        int h = key.hashCode();
        return h ^ (h >>> 16);
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * 2;
        Node<E>[] newTable = new Node[newCapacity];

        // Перехеширование всех элементов
        for (int i = 0; i < table.length; i++) {
            Node<E> node = table[i];
            while (node != null) {
                Node<E> next = node.next;
                int newIndex = indexFor(node.hash, newCapacity);
                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = next;
            }
        }

        table = newTable;
    }

    // Методы для работы с коллекциями

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

    // Метод toString() выводит элементы в порядке добавления

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        while (current != null) {
            sb.append(current.item);
            if (current.after != null) {
                sb.append(", ");
            }
            current = current.after;
        }
        sb.append("]");
        return sb.toString();
    }

    // Остальные методы интерфейса Set (базовые реализации)

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = head;
            private Node<E> lastReturned = null;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (!hasNext()) return null;
                lastReturned = current;
                current = current.after;
                return lastReturned.item;
            }

            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }
                MyLinkedHashSet.this.remove(lastReturned.item);
                lastReturned = null;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int index = 0;
        Node<E> current = head;
        while (current != null) {
            array[index++] = current.item;
            current = current.after;
        }
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }

        int index = 0;
        Node<E> current = head;
        while (current != null) {
            a[index++] = (T) current.item;
            current = current.after;
        }

        if (a.length > size) {
            a[size] = null;
        }

        return a;
    }
}