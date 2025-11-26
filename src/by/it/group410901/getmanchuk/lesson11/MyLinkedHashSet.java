package by.it.group410901.getmanchuk.lesson11;

import java.util.*;

// Данные хранятся в массиве бакетов, где каждый бакет — это связанный список элементов с одинаковым хеш-кодом

// создаём шаблонный класс, реализующий интерфейс Set

public class MyLinkedHashSet<E> implements Set<E> {

    // Узел для хранения элемента + связей по бакетам и по порядку вставки
    private static class Node<E> {
        E value;
        Node<E> next;       // следующая в бакете
        Node<E> prevOrder;  // предыдущая в порядке вставки
        Node<E> nextOrder;  // следующая в порядке вставки
        Node(E value) { this.value = value; }
    }

    private final int capacity = 16; // размер хеш-таблицы
    private Node<E>[] table = new Node[capacity];
    private int size = 0;
    private Node<E> head; // первый элемент по порядку вставки
    private Node<E> tail; // последний элемент по порядку вставки

    // вычисляем номер бакета по хеш-коду
    private int index(Object o) {
        return (o == null) ? 0 : Math.abs(o.hashCode() % capacity);
    }

    // Добавление элемента
    @Override
    public boolean add(E e) {
        int i = index(e);
        Node<E> cur = table[i];
        while (cur != null) {
            if (Objects.equals(cur.value, e)) return false;
            cur = cur.next;
        }
        Node<E> node = new Node<>(e);
        node.next = table[i];
        table[i] = node;
        if (tail == null) head = tail = node;
        else {
            tail.nextOrder = node;
            node.prevOrder = tail;
            tail = node;
        }
        size++;
        return true;
    }

    // Проверка наличия элемента
    @Override
    public boolean contains(Object o) {
        int i = index(o);
        Node<E> cur = table[i];
        while (cur != null) {
            if (Objects.equals(cur.value, o)) return true;
            cur = cur.next;
        }
        return false;
    }

    // Удаление элемента
    @Override
    public boolean remove(Object o) {
        int i = index(o);
        Node<E> cur = table[i];
        Node<E> prev = null;
        while (cur != null) {
            if (Objects.equals(cur.value, o)) {
                if (prev == null) table[i] = cur.next;
                else prev.next = cur.next;
                if (cur.prevOrder != null) cur.prevOrder.nextOrder = cur.nextOrder;
                else head = cur.nextOrder;
                if (cur.nextOrder != null) cur.nextOrder.prevOrder = cur.prevOrder;
                else tail = cur.prevOrder;
                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }

    // Очистка множества
    @Override
    public void clear() {
        Arrays.fill(table, null);
        head = tail = null;
        size = 0;
    }

    // Возврат количества элементов
    @Override
    public int size() { return size; }

    // Проверка на пустоту
    @Override
    public boolean isEmpty() { return size == 0; }

    // Итератор по порядку вставки
    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            Node<E> cur = head;
            public boolean hasNext() { return cur != null; }
            public E next() {
                if (cur == null) throw new NoSuchElementException();
                E v = cur.value;
                cur = cur.nextOrder;
                return v;
            }
        };
    }

    // Преобразование в строку (в порядке вставки)
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = head;
        while (cur != null) {
            sb.append(cur.value);
            cur = cur.nextOrder;
            if (cur != null) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    // Остальные методы Set (минимальные реализации)
    @Override public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;
        for (E e : this) arr[i++] = e;
        return arr;
    }

    @Override public <T> T[] toArray(T[] a) {
        int i = 0;
        for (E e : this) a[i++] = (T)e;
        return a;
    }

    @Override public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) changed |= add(e);
        return changed;
    }

    @Override public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) changed |= remove(o);
        return changed;
    }

    @Override public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E e = it.next();
            if (!c.contains(e)) { remove(e); changed = true; }
        }
        return changed;
    }
}