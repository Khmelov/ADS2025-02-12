package by.it.group451002.mishchenko.lesson11;

import java.util.Set;
import java.util.Iterator;

// Реализация собственного HashSet на массиве и односвязных списках
public class MyHashSet<E> implements Set<E> {

    // Узел для хранения элементов в случае коллизий
    private static class Node<E> {
        E value;
        Node<E> next;

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] table;   // массив "корзин"
    private int capacity;      // размер массива
    private int size;          // количество элементов

    public MyHashSet() {
        this.capacity = 16; // начальный размер массива
        this.table = new Node[capacity];
        this.size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        table = new Node[capacity]; // заново создаём пустой массив
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        int index = getIndex(e);

        Node<E> current = table[index];
        // Проверяем, есть ли уже такой элемент
        while (current != null) {
            if (current.value.equals(e)) {
                return false; // элемент уже существует
            }
            current = current.next;
        }

        // Добавляем новый узел в начало списка
        Node<E> newNode = new Node<>(e, table[index]);
        table[index] = newNode;
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

    @Override
    public boolean contains(Object o) {
        int index = getIndex(o);

        Node<E> current = table[index];
        while (current != null) {
            if (current.value.equals(o)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // Метод для вычисления индекса по хеш-коду
    private int getIndex(Object o) {
        return (o == null ? 0 : Math.abs(o.hashCode())) % capacity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;

        for (int i = 0; i < capacity; i++) {
            Node<E> current = table[i];
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(current.value);
                first = false;
                current = current.next;
            }
        }

        sb.append("]");
        return sb.toString();
    }

    // Методы интерфейса Set, которые можно оставить пустыми или реализовать позже
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
    public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException("containsAll not implemented");
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException("addAll not implemented");
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException("retainAll not implemented");
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException("removeAll not implemented");
    }
}

