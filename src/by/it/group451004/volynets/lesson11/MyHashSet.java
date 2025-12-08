package by.it.group451004.volynets.lesson11;

import java.util.Set;
import java.util.Iterator;
import java.util.Collection;

public class MyHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<E>[] table;
    private int size;

    // узел односвязного списка для разрешения коллизий
    private static class Node<E> {
        E data;
        Node<E> next;
        int hash; // сохраняем хеш для быстрого сравнения

        Node(int hash, E data, Node<E> next) {
            this.hash = hash;
            this.data = data;
            this.next = next;
        }
    }

    public MyHashSet() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    public MyHashSet(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        table = new Node[initialCapacity];
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    // количество элементов в множестве
    @Override
    public int size() {
        return size;
    }

    // очистка множества
    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    // проверка пустоты множества
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // добавление элемента в множество
    @Override
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException();
        }

        // проверяем необходимость расширения
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }

        int hash = hash(e);
        int index = indexFor(hash, table.length);

        // проверяем наличие элемента в цепочке
        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash && (e == current.data || e.equals(current.data))) {
                return false; // элемент уже существует
            }
            current = current.next;
        }

        // добавляем новый элемент в начало цепочки
        table[index] = new Node<>(hash, e, table[index]);
        size++;
        return true;
    }

    // удаление элемента из множества
    @Override
    public boolean remove(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }

        int hash = hash(o);
        int index = indexFor(hash, table.length);

        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if (current.hash == hash && (o == current.data || o.equals(current.data))) {
                // нашли элемент для удаления
                if (prev == null) {
                    table[index] = current.next; // удаляем первый элемент
                } else {
                    prev.next = current.next; // удаляем из середины/конца
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }

        return false; // элемент не найден
    }

    // проверка наличия элемента в множестве
    @Override
    public boolean contains(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }

        int hash = hash(o);
        int index = indexFor(hash, table.length);

        Node<E> current = table[index];
        while (current != null) {
            if (current.hash == hash && (o == current.data || o.equals(current.data))) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    // строковое представление множества
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int count = 0;

        // проходим по всем ячейкам таблицы
        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            while (current != null) {
                sb.append(current.data);
                if (++count < size) {
                    sb.append(", ");
                }
                current = current.next;
            }
        }

        sb.append("]");
        return sb.toString();
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы                     ///////
    /////////////////////////////////////////////////////////////////////////

    // вычисление хеш-кода объекта
    private int hash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    // вычисление индекса в таблице по хеш-коду
    private int indexFor(int hash, int length) {
        return (hash & 0x7FFFFFFF) % length; // убираем знак и берем по модулю
    }

    // увеличение размера таблицы при переполнении
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * 2;
        Node<E>[] newTable = new Node[newCapacity];

        // перераспределяем элементы в новую таблицу
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

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы Set - заглушки          ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
}