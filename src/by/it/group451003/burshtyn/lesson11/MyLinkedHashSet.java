package by.it.group451003.burshtyn.lesson11;

import java.util.Collection;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    // Узел для хеш-таблицы (цепочки коллизий)
    private static class HashNode<E> {
        E data;
        HashNode<E> next;

        HashNode(E data, HashNode<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    // Узел для поддержания порядка добавления
    private static class LinkedNode<E> {
        E data;
        LinkedNode<E> prev;
        LinkedNode<E> next;

        LinkedNode(E data) {
            this.data = data;
        }
    }

    private HashNode<E>[] table;
    private LinkedNode<E> head;  // Первый добавленный элемент
    private LinkedNode<E> tail;  // Последний добавленный элемент
    private int size;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (HashNode<E>[]) new HashNode[DEFAULT_CAPACITY];
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        LinkedNode<E> current = head;
        int count = 0;

        while (current != null) {
            sb.append(current.data);
            if (++count < size) {
                sb.append(", ");
            }
            current = current.next;
        }

        sb.append("]");
        return sb.toString();
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
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException("Null elements are not supported");
        }

        // Проверяем нужно ли увеличить таблицу
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }

        int index = getIndex(element);
        HashNode<E> current = table[index];

        // Проверяем нет ли уже такого элемента
        while (current != null) {
            if (element.equals(current.data)) {
                return false; // Элемент уже существует
            }
            current = current.next;
        }

        // Добавляем в хеш-таблицу
        table[index] = new HashNode<>(element, table[index]);

        // Добавляем в связный список для порядка
        LinkedNode<E> newNode = new LinkedNode<>(element);
        if (tail == null) {
            // Первый элемент
            head = newNode;
            tail = newNode;
        } else {
            // Добавляем в конец
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }

        size++;
        return true;
    }

    @Override
    public boolean remove(Object element) {
        if (element == null) {
            return false;
        }

        int index = getIndex(element);
        HashNode<E> current = table[index];
        HashNode<E> prev = null;

        // Ищем элемент в хеш-таблице
        while (current != null) {
            if (element.equals(current.data)) {
                // Удаляем из хеш-таблицы
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }

                // Удаляем из связного списка
                removeFromLinkedList(element);

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

        int index = getIndex(element);
        HashNode<E> current = table[index];

        while (current != null) {
            if (element.equals(current.data)) {
                return true;
            }
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
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;

        // Создаем временный набор для элементов которые нужно удалить
        LinkedNode<E> current = head;
        while (current != null) {
            if (!c.contains(current.data)) {
                remove(current.data);
                modified = true;
            }
            current = current.next;
        }

        return modified;
    }

    // Вспомогательные методы

    private int getIndex(Object element) {
        return Math.abs(element.hashCode()) % table.length;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        HashNode<E>[] oldTable = table;
        table = (HashNode<E>[]) new HashNode[oldTable.length * 2];

        // Перехешируем все элементы
        for (int i = 0; i < oldTable.length; i++) {
            HashNode<E> current = oldTable[i];
            while (current != null) {
                int newIndex = getIndex(current.data);
                table[newIndex] = new HashNode<>(current.data, table[newIndex]);
                current = current.next;
            }
        }
    }

    private void removeFromLinkedList(Object element) {
        LinkedNode<E> current = head;

        while (current != null) {
            if (element.equals(current.data)) {
                // Нашли узел для удаления
                if (current.prev == null) {
                    // Удаляем голову
                    head = current.next;
                    if (head != null) {
                        head.prev = null;
                    }
                } else {
                    current.prev.next = current.next;
                }

                if (current.next == null) {
                    // Удаляем хвост
                    tail = current.prev;
                    if (tail != null) {
                        tail.next = null;
                    }
                } else {
                    current.next.prev = current.prev;
                }

                break;
            }
            current = current.next;
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////    Остальные методы интерфейса Set (необязательные)       ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public java.util.Iterator<E> iterator() {
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
}