package by.it.group410901.evtuhovskaya.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

//реализация структуры данных "множество" на основе хэш-таблицы//

public class  MyHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    //структура массива
    private Node<E>[] table;
    private int size;
    private final float loadFactor;

    // узел односвязного списка для разрешения коллизий, структура
    private static class Node<E> {
        E item;
        Node<E> next;
        int hash; //предвычисленный хэш элемента

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

        // Добавляем новый элемент в начало списка
        table[index] = new Node<>(hash, element, table[index]);
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
                return false; // null уже существует
            }
            node = node.next;
        }

        // Добавляем null
        table[0] = new Node<>(0, null, table[0]);
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
            return removeNull();
        }

        int hash = hash(element);
        int index = indexFor(hash, table.length);

        Node<E> prev = null;
        Node<E> current = table[index];

        while (current != null) {
            if (current.hash == hash && (element == current.item || element.equals(current.item))) {
                // Найден элемент для удаления
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
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }

        return false;
    }

    // Вспомогательные методы
   // Хэширование и определение индекса
    private int hash(Object key) {
        if (key == null) return 0;
        int h = key.hashCode();
        return h ^ (h >>> 16); // Распределение битов для лучшего распределения
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

    // Обязательные методы интерфейса Set, которые можно оставить пустыми или с базовой реализацией

    @Override
    public Iterator<E> iterator() {
        // Базовая реализация - возвращаем пустой итератор
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public E next() {
                return null;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int index = 0;
        for (int i = 0; i < table.length; i++) {
            Node<E> node = table[i];
            while (node != null) {
                array[index++] = node.item;
                node = node.next;
            }
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
        for (int i = 0; i < table.length; i++) {
            Node<E> node = table[i];
            while (node != null) {
                a[index++] = (T) node.item;
                node = node.next;
            }
        }

        if (a.length > size) {
            a[size] = null;
        }

        return a;
    }

    // Остальные методы интерфейса Collection/Set

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

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        boolean first = true;

        for (int i = 0; i < table.length; i++) {
            Node<E> node = table[i];
            while (node != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(node.item);
                first = false;
                node = node.next;
            }
        }

        sb.append("]");
        return sb.toString();
    }
}