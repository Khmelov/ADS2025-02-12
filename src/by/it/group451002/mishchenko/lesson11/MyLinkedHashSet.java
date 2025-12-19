package by.it.group451002.mishchenko.lesson11;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

// Реализация LinkedHashSet без стандартных коллекций
public class MyLinkedHashSet<E> implements Set<E> {

    // Узел для хранения элементов в корзинах (коллизии)
    private static class Node<E> {
        E value;
        Node<E> nextInBucket;   // для коллизий
        Node<E> nextOrder;      // для порядка вставки
        Node<E> prevOrder;      // для порядка вставки

        Node(E value) {
            this.value = value;
        }
    }

    private Node<E>[] table;   // массив корзин
    private int capacity;      // размер массива
    private int size;          // количество элементов

    // Дополнительные ссылки для порядка вставки
    private Node<E> head;      // первый элемент
    private Node<E> tail;      // последний элемент

    public MyLinkedHashSet() {
        this.capacity = 16;
        this.table = new Node[capacity];
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        table = new Node[capacity];
        size = 0;
        head = null;
        tail = null;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        int index = getIndex(e);

        Node<E> current = table[index];
        while (current != null) {
            if (current.value.equals(e)) {
                return false; // элемент уже есть
            }
            current = current.nextInBucket;
        }

        // создаём новый узел
        Node<E> newNode = new Node<>(e);

        // вставляем в корзину
        newNode.nextInBucket = table[index];
        table[index] = newNode;

        // вставляем в конец списка порядка
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.nextOrder = newNode;
            newNode.prevOrder = tail;
            tail = newNode;
        }

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = getIndex(o);

        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if (current.value.equals(o)) {
                // удаляем из корзины
                if (prev == null) {
                    table[index] = current.nextInBucket;
                } else {
                    prev.nextInBucket = current.nextInBucket;
                }

                // удаляем из списка порядка
                if (current.prevOrder != null) {
                    current.prevOrder.nextOrder = current.nextOrder;
                } else {
                    head = current.nextOrder;
                }

                if (current.nextOrder != null) {
                    current.nextOrder.prevOrder = current.prevOrder;
                } else {
                    tail = current.prevOrder;
                }

                size--;
                return true;
            }
            prev = current;
            current = current.nextInBucket;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        int index = getIndex(o);

        Node<E> current = table[index];
        while (current != null) {
            if (current.value.equals(o)) {
                return true;
            }
            current = current.nextInBucket;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;

        Node<E> current = head;
        while (current != null) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(current.value);
            first = false;
            current = current.nextOrder;
        }

        sb.append("]");
        return sb.toString();
    }

    // Методы работы с коллекциями
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
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
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.nextOrder; // сохраняем ссылку, т.к. current может удалиться
            if (!c.contains(current.value)) {
                remove(current.value);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    // Вспомогательный метод для индекса
    private int getIndex(Object o) {
        return (o == null ? 0 : Math.abs(o.hashCode())) % capacity;
    }

    // Методы интерфейса Set, которые можно реализовать позже
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

