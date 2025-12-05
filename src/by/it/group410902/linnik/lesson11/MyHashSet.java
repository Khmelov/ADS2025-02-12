package by.it.group410902.linnik.lesson11;
import java.util.Arrays;
import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

public class MyHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size;
    private final float loadFactor;
    private int threshold;

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
        this.threshold = (int)(DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
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
            Arrays.fill(table, null);
            size = 0;
        }
    }

    @Override
    public boolean contains(Object element) {
        return getNode(element) != null;
    }

    @Override
    public boolean add(E element) {
        return addElement(element) == null;
    }

    @Override
    public boolean remove(Object element) {
        return removeElement(element) != null;
    }

    // Вспомогательные методы

    private int hash(Object key) {
        if (key == null) {
            return 0;
        }
        int h = key.hashCode();
        return h ^ (h >>> 16);
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private Node<E> getNode(Object element) {
        if (element == null) {
            Node<E> first = table[0];
            while (first != null) {
                if (first.element == null) {
                    return first;
                }
                first = first.next;
            }
            return null;
        }

        int hash = hash(element);
        int index = indexFor(hash, table.length);
        Node<E> node = table[index];

        while (node != null) {
            if (node.hash == hash &&
                    (element == node.element || element.equals(node.element))) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private E addElement(E element) {
        if (element == null) {
            return addNullElement();
        }

        int hash = hash(element);
        int index = indexFor(hash, table.length);

        // Проверяем, существует ли уже такой элемент
        Node<E> node = table[index];
        while (node != null) {
            if (node.hash == hash &&
                    (element == node.element || element.equals(node.element))) {
                return node.element; // Элемент уже существует
            }
            node = node.next;
        }

        // Добавляем новый элемент в начало цепочки
        table[index] = new Node<>(element, hash, table[index]);
        size++;

        // Проверяем необходимость расширения таблицы
        if (size > threshold) {
            resize();
        }

        return null; // Элемент был добавлен
    }

    private E addNullElement() {
        // Обрабатываем null элемент отдельно (всегда в bucket 0)
        Node<E> node = table[0];
        while (node != null) {
            if (node.element == null) {
                return node.element; // null уже существует
            }
            node = node.next;
        }

        table[0] = new Node<>(null, 0, table[0]);
        size++;

        if (size > threshold) {
            resize();
        }

        return null;
    }

    private E removeElement(Object element) {
        if (element == null) {
            return removeNullElement();
        }

        int hash = hash(element);
        int index = indexFor(hash, table.length);
        Node<E> node = table[index];
        Node<E> prev = null;

        while (node != null) {
            if (node.hash == hash &&
                    (element == node.element || element.equals(node.element))) {
                // Найден элемент для удаления
                if (prev == null) {
                    table[index] = node.next;
                } else {
                    prev.next = node.next;
                }
                size--;
                return node.element;
            }
            prev = node;
            node = node.next;
        }

        return null; // Элемент не найден
    }

    private E removeNullElement() {
        int index = 0;
        Node<E> node = table[index];
        Node<E> prev = null;

        while (node != null) {
            if (node.element == null) {
                if (prev == null) {
                    table[index] = node.next;
                } else {
                    prev.next = node.next;
                }
                size--;
                return node.element;
            }
            prev = node;
            node = node.next;
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        int newCapacity = oldTable.length * 2;
        table = new Node[newCapacity];
        threshold = (int)(newCapacity * loadFactor);

        for (int i = 0; i < oldTable.length; i++) {
            Node<E> node = oldTable[i];
            while (node != null) {
                Node<E> next = node.next;
                int newIndex = indexFor(node.hash, newCapacity);
                node.next = table[newIndex];
                table[newIndex] = node;
                node = next;
            }
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        boolean first = true;
        for (int i = 0; i < table.length; i++) {
            Node<E> node = table[i];
            while (node != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(node.element);
                first = false;
                node = node.next;
            }
        }

        sb.append("]");
        return sb.toString();
    }
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