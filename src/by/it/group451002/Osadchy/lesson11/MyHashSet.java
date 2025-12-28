package by.it.group451002.Osadchy.lesson11;

import java.util.*;

public class MyHashSet<E> implements java.util.Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size;
    private final float loadFactor;

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

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.table = new Node[DEFAULT_CAPACITY];
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
    public boolean contains(Object o) {
        int hash = hash(o);
        int index = indexFor(hash, table.length);

        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash &&
                    Objects.equals(o, current.element)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int tableIndex = 0;
            private Node<E> current = null;
            private Node<E> next = findNext();

            private Node<E> findNext() {
                // Продолжаем с текущего узла
                if (current != null && current.next != null) {
                    current = current.next;
                    return current;
                }

                // Ищем следующий непустой bucket
                while (tableIndex < table.length) {
                    if (table[tableIndex] != null) {
                        current = table[tableIndex++];
                        return current;
                    }
                    tableIndex++;
                }
                return null;
            }

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                E element = next.element;
                next = findNext();
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
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
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
    public boolean add(E e) {
        int hash = hash(e);
        int index = indexFor(hash, table.length);

        // Проверяем, есть ли уже такой элемент
        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash &&
                    Objects.equals(e, current.element)) {
                return false;
            }
            current = current.next;
        }

        // Добавляем новый элемент в начало списка
        table[index] = new Node<>(e, hash, table[index]);
        size++;

        // Проверяем необходимость рехеширования
        if (size > table.length * loadFactor) {
            resize();
        }

        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hash = hash(o);
        int index = indexFor(hash, table.length);

        Node<E> current = table[index];
        Node<E> previous = null;

        while (current != null) {
            if (current.hash == hash &&
                    Objects.equals(o, current.element)) {

                if (previous == null) {
                    table[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
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

    @Override
    public void clear() {
        Arrays.fill(table, null);
        size = 0;
    }

    @Override
    public String toString() {
        Iterator<E> it = iterator();
        if (!it.hasNext()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        while (true) {
            E element = it.next();
            sb.append(element);
            if (!it.hasNext()) {
                return sb.append("]").toString();
            }
            sb.append(", ");
        }
    }

    // Вспомогательные методы
    private int hash(Object element) {
        return element == null ? 0 : element.hashCode();
    }

    private int indexFor(int hash, int length) {
        return Math.abs(hash % length);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * 2;
        Node<E>[] newTable = new Node[newCapacity];

        for (Node<E> node : table) {
            Node<E> current = node;
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