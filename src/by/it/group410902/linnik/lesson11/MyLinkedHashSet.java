package by.it.group410902.linnik.lesson11;
import java.util.*;

public class MyLinkedHashSet<E> implements Set<E> {
    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        Node(E data) {
            this.data=data;
        }
    }

    private Node<E>[] elements;
    private Node<E> head;
    private Node<E> tail;
    private int size;

    public MyLinkedHashSet(int capacity) {
        elements = new Node[capacity];
        size = 0;
    }

    public MyLinkedHashSet() {
        this(16);
    }

    private int getElementIndex(Object key, int capacity) {
        if (key == null) {
            return 0;
        }
        int hashCode = key.hashCode();
        return (hashCode & 0x7FFFFFFF) % capacity; //хэшкод+убираем отрицательные числа+делим на массив
    }

    private int getElementIndex(Object key) {
        return getElementIndex(key, elements.length);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty(){
        return size==0;
    }

    @Override
    public boolean add(E e) {
        int index = getElementIndex(e);
        Node<E> current = elements[index];

        while (current != null) {
            if (Objects.equals(current.data, e)) {
                return false;
            }
            current = current.next;
        }

        Node<E> newNode = new Node<>(e);
        newNode.next = elements[index];
        elements[index] = newNode;

        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode;
        }

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = getElementIndex(o);
        Node<E> current = elements[index];
        Node<E> prev = null;

        while (current != null) {
            if (Objects.equals(current.data, o)) {
                if (current.before != null) {
                    current.before.after = current.after;
                } else {
                    head = current.after;
                }
                if (current.after != null) {
                    current.after.before = current.before;
                } else {
                    tail = current.before;
                }

                if (prev == null) {
                    elements[index] = current.next;
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
    public void clear() {
        for (int i = 0; i < elements.length; i++) {
            elements[i] = null;
        }
        head = tail = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        int index = getElementIndex(o);
        Node<E> current = elements[index];

        while (current != null) {
            if (Objects.equals(current.data, o)) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        boolean first = true;

        while (current != null) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(current.data);
            first = false;
            current = current.after;
        }

        sb.append("]");
        return sb.toString();
    }

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

        // Создаем временный список для элементов, которые нужно удалить
        List<E> toRemove = new ArrayList<>();

        // Проходим по всем элементам множества
        for (Node<E> head : elements) {
            Node<E> current = head;
            while (current != null) {
                // Если элемент не содержится в коллекции c, помечаем его для удаления
                if (!c.contains(current.data)) {
                    toRemove.add(current.data);
                }
                current = current.next;
            }
        }

        // Удаляем все помеченные элементы
        for (E element : toRemove) {
            if (remove(element)) {
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
}
