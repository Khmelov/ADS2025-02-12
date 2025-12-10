package by.it.group451003.sorokin.lesson11;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size;
    private final float loadFactor;

    // Узел односвязного списка
    private static class Node<E> {
        E item;
        Node<E> next;
        int hash;

        Node(int hash, E item, Node<E> next) {
            this.hash = hash;
            this.item = item;
            this.next = next;
        }
    }

    // Конструкторы
    public MyHashSet() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashSet(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public MyHashSet(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }

        this.loadFactor = loadFactor;
        this.table = (Node<E>[]) new Node[initialCapacity];
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
    public void clear() {
        if (size > 0) {
            Arrays.fill(table, null);
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

        // Проверяем, существует ли уже элемент
        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash && (element == current.item || element.equals(current.item))) {
                return false; // Элемент уже существует
            }
            current = current.next;
        }

        // Добавляем новый элемент в начало списка
        table[index] = new Node<>(hash, element, table[index]);
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
            if (current.hash == hash && (element == current.item || element.equals(current.item))) {
                // Удаляем узел
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
            if (current.hash == hash && (element == current.item || element.equals(current.item))) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    // Вспомогательные методы

    private int hash(Object key) {
        int h = key.hashCode();
        return h ^ (h >>> 16); // Распределение хешей
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * 2;
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];

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

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        boolean first = true;

        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(current.item);
                first = false;
                current = current.next;
            }
        }

        sb.append("]");
        return sb.toString();
    }

    // Обязательные методы интерфейса Set, которые могут потребоваться тестами

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;
            private Node<E> currentNode = null;
            private Node<E> lastReturned = null;
            private Node<E> nextNode = null;
            private int count = 0;

            {
                findNext();
            }

            private void findNext() {
                while (currentIndex < table.length) {
                    if (table[currentIndex] != null) {
                        if (nextNode == null) {
                            nextNode = table[currentIndex];
                        } else {
                            nextNode = nextNode.next;
                        }

                        if (nextNode != null) {
                            return;
                        }
                    }
                    currentIndex++;
                    nextNode = null;
                }
            }

            @Override
            public boolean hasNext() {
                return nextNode != null && count < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                lastReturned = nextNode;
                E item = nextNode.item;
                count++;
                findNext();
                return item;
            }

            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }
                MyHashSet.this.remove(lastReturned.item);
                lastReturned = null;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int index = 0;
        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            while (current != null) {
                array[index++] = current.item;
                current = current.next;
            }
        }
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) Arrays.copyOf(toArray(), size, a.getClass());
        }
        System.arraycopy(toArray(), 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    // Остальные методы интерфейса Set - минимальные реализации

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E item : c) {
            if (add(item)) {
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
            E element = it.next();
            if (!c.contains(element)) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object item : c) {
            if (remove(item)) {
                modified = true;
            }
        }
        return modified;
    }

    // Обязательные методы для реализации Set

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Set)) return false;

        Collection<?> c = (Collection<?>) o;
        if (c.size() != size()) return false;

        try {
            return containsAll(c);
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (E element : this) {
            if (element != null) {
                h += element.hashCode();
            }
        }
        return h;
    }
}