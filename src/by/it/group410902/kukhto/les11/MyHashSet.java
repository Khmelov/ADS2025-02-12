package by.it.group410902.kukhto.les11;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
public class MyHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private Node<E>[] table;
    private int size;

    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
        }
    }

    public MyHashSet() {
        table = new Node[DEFAULT_CAPACITY];
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
        size = 0;
    }

    @Override
    public boolean add(E element) {
        int index = getIndex(element);
        Node<E> current = table[index];

        //  нет ли уже такого элемента
        while (current != null) {
            if (Objects.equals(current.data, element)) {
                return false; //уже существует
            }
            current = current.next;
        }

        //  в начало списка
        Node<E> newNode = new Node<>(element);
        newNode.next = table[index];
        table[index] = newNode;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object element) {
        int index = getIndex(element);
        Node<E> current = table[index];
        Node<E> prev = null;

        while (current != null) {
            if (Objects.equals(current.data, element)) {
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

    @Override
    public boolean contains(Object element) {
        int index = getIndex(element);
        Node<E> current = table[index];

        while (current != null) {
            if (Objects.equals(current.data, element)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    private int getIndex(Object element) {
        if (element == null) {
            return 0;
        }
        int hashCode = element.hashCode();
        return Math.abs(hashCode) % table.length;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        int count = 0;

        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            while (current != null) {
                sb.append(current.data);
                count++;
                if (count < size) {
                    sb.append(", ");
                }
                current = current.next;
            }
        }

        sb.append("]");
        return sb.toString();
    }

    // Остальные методы интерфейса Set не реализованы
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
