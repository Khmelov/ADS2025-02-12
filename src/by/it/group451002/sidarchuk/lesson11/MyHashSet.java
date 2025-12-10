package by.it.group451002.sidarchuk.lesson11;

import java.util.*;

public class MyHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size;
    private final float loadFactor;

    // Узел односвязного списка для разрешения коллизий
    private static class Node<E> {
        final E element;
        final int hash;
        Node<E> next;

        Node(E element, int hash, Node<E> next) {
            this.element = element;
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
    public MyHashSet(Collection<? extends E> c) {
        this();
        addAll(c);
    }

    // Обязательные методы
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
            size = 0;
        }
    }

    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        int hash = hash(element);
        int index = indexFor(hash, table.length);

        // Проверяем, есть ли уже такой элемент
        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash && (element == current.element || element.equals(current.element))) {
                return false; // Элемент уже существует
            }
            current = current.next;
        }

        // Добавляем новый элемент в начало списка
        table[index] = new Node<>(element, hash, table[index]);
        size++;

        // Проверяем необходимость расширения таблицы
        if (size > table.length * loadFactor) {
            resize();
        }

        return true;
    }

    @Override
    public boolean remove(Object element) {
        if (element == null) {
            return false;
        }

        int hash = hash(element);
        int index = indexFor(hash, table.length);

        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if (current.hash == hash && (element == current.element || element.equals(current.element))) {
                // Удаляем элемент из списка
                if (prev == null) {
                    table[index] = current.next;
                } else {
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
    public boolean contains(Object element) {
        if (element == null) {
            return false;
        }

        int hash = hash(element);
        int index = indexFor(hash, table.length);

        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash && (element == current.element || element.equals(current.element))) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        boolean first = true;
        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
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

    // Остальные методы интерфейса Set (минимальные реализации)
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;
            private Node<E> currentNode = null;
            private Node<E> nextNode = findNextNode();

            private Node<E> findNextNode() {
                // Продолжаем с текущего узла
                if (currentNode != null && currentNode.next != null) {
                    currentNode = currentNode.next;
                    return currentNode;
                }

                // Ищем следующий непустой бакет
                while (currentIndex < table.length) {
                    if (table[currentIndex] != null) {
                        currentNode = table[currentIndex++];
                        return currentNode;
                    }
                    currentIndex++;
                }
                return null;
            }

            @Override
            public boolean hasNext() {
                return nextNode != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                E element = nextNode.element;
                nextNode = findNextNode();
                return element;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int index = 0;
        for (E element : this) {
            array[index++] = element;
        }
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (E element : this) {
            a[i++] = (T) element;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

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

    // Вспомогательные методы

    /**
     * Вычисляет хеш-код объекта
     */
    private int hash(Object element) {
        int h = element.hashCode();
        return h ^ (h >>> 16);
    }

    /**
     * Вычисляет индекс в массиве на основе хеш-кода
     */
    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    /**
     * Увеличивает размер таблицы при необходимости
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * 2;
        Node<E>[] newTable = new Node[newCapacity];

        // Перехеширование всех элементов
        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            while (current != null) {
                Node<E> next = current.next;
                int newIndex = indexFor(current.hash, newCapacity);
                current.next = newTable[newIndex];
                newTable[newIndex] = current;
                current = next;
            }
        }

        table = newTable;
    }
}