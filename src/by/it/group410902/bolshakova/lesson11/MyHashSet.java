package by.it.group410902.bolshakova.lesson11;

import java.util.*;

public class MyHashSet<E> implements Set<E> {

    // Один элемент списка
    private static class Node<E> {
        E value;
        Node<E> next;
        // Конструктор узла, инициализирует значение и устанавливает next в null
        Node(E value) {
            this.value = value;
        }
    }

    // cвязанный список для хранения элементов с одинаковым хешем
    private static class Bucket<E> {
        private Node<E> head;
        // Проверяет наличие элемента в корзине
        boolean contains(E value) {
            for (Node<E> n = head; n != null; n = n.next)
                if (n.value.equals(value)) return true;
            return false;
        }
        // Добавляет элемент в корзину
        boolean add(E value) {
            if (contains(value)) return false;
            Node<E> node = new Node<>(value); // создаем новый узел
            node.next = head;// новый узел указывает на текущее начало
            head = node;
            return true;
        }
        // Удаляет элемент из корзины
        boolean remove(E value) {
            if (head == null) return false;
            //удаляем первый элемент
            if (head.value.equals(value)) {
                head = head.next;
                return true;
            }
            // Ищем элемент перед тем, который нужно удалить
            Node<E> prev = head;
            while (prev.next != null && !prev.next.value.equals(value))
                prev = prev.next;
            if (prev.next == null) return false;
            // если пропускаем удаляемый элемент
            prev.next = prev.next.next;
            return true;
        }
        // Очищает корзину - удаляет все элементы
        void clear() {
            head = null;
        }
    }

    private static final int size = 100;
    private int count = 0;
    private final Bucket<E>[] table;
    // Конструктор - инициализирует хеш-таблицу
    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = new Bucket[size];
        for (int i = 0; i < size; i++)
            table[i] = new Bucket<>();
    }
    //индекс корзины для объекта
    private int index(Object o) {
        // Хеш-функция: берем хеш-код объекта, делим по модулю на размер таблицы
        return Math.abs(o.hashCode() % size);
    }
    // Добавляет элемент в множество
    @Override
    public boolean add(E e) {
        if (table[index(e)].add(e)) {
            count++;
            return true;
        }
        return false;
    }
    // Удаляет элемент из множества
    @Override
    public boolean remove(Object o) {
        if (table[index(o)].remove((E) o)) {
            count--;
            return true;
        }
        return false;
    }
    // Проверяет наличие элемента в множестве
    @Override
    public boolean contains(Object o) {
        return table[index(o)].contains((E) o);
    }
    //удаляет все элементы
    @Override
    public void clear() {
        for (Bucket<E> b : table)
            b.clear();
        count = 0;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }
    // Возвращает строковое представление множества
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        // Двойной цикл: по всем корзинам и по всем элементам в каждой корзине
        for (Bucket<E> b : table)
            for (Node<E> n = b.head; n != null; n = n.next)
                sb.append(n.value).append(", ");
        // Убираем лишнюю запятую и пробел в конце
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("]");
        return sb.toString();
    }

    // Остальные методы Set пока не реализованы
    public Iterator<E> iterator() { return null; }
    public Object[] toArray() { return new Object[0]; }
    public <T> T[] toArray(T[] a) { return null; }
    public boolean containsAll(Collection<?> c) { return false; }
    public boolean addAll(Collection<? extends E> c) { return false; }
    public boolean retainAll(Collection<?> c) { return false; }
    public boolean removeAll(Collection<?> c) { return false; }
}
