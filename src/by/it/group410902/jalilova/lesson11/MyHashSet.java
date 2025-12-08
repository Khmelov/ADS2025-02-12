package by.it.group410902.jalilova.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    // узел списка в бакете
    private static class Node<E> {
        E data;
        Node<E> next;
        Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    // массив бакетов
    private Node<E>[] table;
    private int size;
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    // создаём пустое множество
    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[INITIAL_CAPACITY];
    }
    // индекс для элемента по хеш-коду
    private int getIndex(Object o, int length) {
        if (o == null) return 0;
        return Math.abs(o.hashCode()) % length;
    }
    // добавить элемент
    @Override
    public boolean add(E e) {
        if (contains(e)) return false;
        if (size >= table.length * LOAD_FACTOR) resize();

        int index = getIndex(e, table.length);
        table[index] = new Node<>(e, table[index]); // вставляем в начало цепочки
        size++;
        return true;
    }
    // увеличить таблицу в 2 раза
    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        table = (Node<E>[]) new Node[oldTable.length * 2];
        size = 0;
        for (Node<E> node : oldTable) {
            while (node != null) {
                add(node.data);
                node = node.next;
            }
        }
    }
    // проверить наличие
    @Override
    public boolean contains(Object o) {
        int index = getIndex(o, table.length);
        Node<E> current = table[index];
        while (current != null) {
            if (o == null ? current.data == null : o.equals(current.data))
                return true;
            current = current.next;
        }
        return false;
    }
    // удалить элемент
    @Override
    public boolean remove(Object o) {
        int index = getIndex(o, table.length);
        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if (o == null ? current.data == null : o.equals(current.data)) {
                if (prev == null) table[index] = current.next;
                else prev.next = current.next;
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }
    // очистить множество
    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++)
            table[i] = null;
        size = 0;
    }
    // вернуть размер
    @Override
    public int size() {
        return size;
    }
    // проверить, пусто ли множество
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // --- методы коллекции (необязательно к реализации) ---

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            if (remove(o)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < table.length; i++) {
            Node<E> prev = null;
            Node<E> curr = table[i];
            while (curr != null) {
                if (!c.contains(curr.data)) {
                    if (prev == null) table[i] = curr.next;
                    else prev.next = curr.next;
                    size--;
                    modified = true;
                } else {
                    prev = curr;
                }
                curr = curr.next;
            }
        }
        return modified;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int bucket = 0;
            Node<E> current = advance();

            private Node<E> advance() {
                while (bucket < table.length) {
                    if (table[bucket] != null)
                        return table[bucket];
                    bucket++;
                }
                return null;
            }

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                E data = current.data;
                current = (current.next != null) ? current.next : (++bucket < table.length ? advance() : null);
                return data;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;
        for (E e : this) arr[i++] = e;
        return arr;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        int i = 0;
        if (a.length < size)
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        for (Node<E> node : table) {
            while (node != null) {
                a[i++] = (T) node.data;
                node = node.next;
            }
        }
        if (a.length > size)
            a[size] = null;
        return a;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (E e : this) {
            if (!first) sb.append(", ");
            sb.append(e);
            first = false;
        }
        sb.append("]");
        return sb.toString();
    }
}
