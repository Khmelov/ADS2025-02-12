package by.it.group410902.bobrovskaya.lesson11;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    class Node<E> {
        E data;
        Node<E> previous;
        Node<E> next;
        Node<E> nextInSet;

        public Node(E data) {
            this.data = data;
        }
    }
    class List<E> {
        Node<E> head;
        Node<E> tail;

        void add(Node<E> node) {
            if (head == null) {
                head = node;
            } else {
                tail.next = node;
                node.previous = tail;
            }
            tail = node;
        }
        void remove(Node<E> node) {
            if (node.previous != null) {
                node.previous.next = node.next;
            } else {
                head = head.next;
            }

            if (node.next != null) {
                node.next.previous = node.previous;
            } else {
                tail = tail.previous;
            }
        }
        void clear() {
            head = null;
            tail = null;
        }
    }

    Node<E>[] elements;
    static final int INITIAL_SIZE = 16;
    int size;
    List<E> list = new List<E>();


    public MyLinkedHashSet() {
        this(INITIAL_SIZE);
    }
    public MyLinkedHashSet(int size) {
        elements = new Node[size];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = list.head;
        if (current != null) {
            sb.append(current.data);
            current = current.next;
        }
        while (current != null) {
            sb.append(", ");
            sb.append(current.data);
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
    public void clear() {
        Arrays.fill(elements, null);
        size = 0;
        list.clear();
    }
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    private int getHashCode(E element) {
        return element.hashCode() % elements.length;
    }
    @Override
    public boolean add(E e) {
        int index = getHashCode(e);
        Node<E> current = elements[index];
        while (current != null) { // проверка на дубликаты (избежание коллизии)
            if (current.data.equals(e)) {
                return false;
            }
            current = current.nextInSet;
        }
        Node<E> newNode = new Node<E>(e);
        newNode.nextInSet = elements[index];
        elements[index] = newNode;
        size++;
        list.add(newNode);
        if (size > elements.length * 0.75) { // расширение хэш-таблицы
            Node<E>[] newElements = new Node[elements.length * 2];
            current = list.head;
            while (current != null) {
                int newIndex = current.data.hashCode() % newElements.length;
                current.nextInSet = newElements[newIndex];
                newElements[newIndex] = current;
                current = current.next;
            }
            elements = newElements;
        }
        return true;
    }
    @Override
    public boolean remove(Object o) {
        E e = (E)o;
        int index = getHashCode(e);
        Node<E> previous = null;
        Node<E> current = elements[index];
        while (current != null) {
            if (current.data.equals(e)) {
                if (previous == null) {
                    elements[index] = current.nextInSet;
                } else {
                    previous.nextInSet = current.nextInSet;
                }
                size--;
                list.remove(current);
                return true;
            }
            previous = current;
            current = current.nextInSet;
        }
        return false;
    }
    @Override
    public boolean contains(Object o) {
        E e = (E)o;
        int index = getHashCode(e);
        Node<E> current = elements[index];
        while (current != null) {
            if (current.data.equals(e)) {
                return true;
            }
            current = current.nextInSet;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (var object : c) {
            if (!contains(object)) {
                return false;
            }
        }
        return true;
    }
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;
        for (var element : c) {
            if (add(element)) {
                result = true;
            }
        }
        return result;
    }
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = false;
        for (var object : c) {
            if (remove(object)) {
                result = true;
            }
        }
        return result;
    }
    @Override
    public boolean retainAll(Collection<?> c) {
        if (c.isEmpty()) {
            this.clear();
            return true;
        }
        boolean isModified = false;
        MyLinkedHashSet<E> tempSet = new MyLinkedHashSet<>(elements.length);
        Node<E> current = list.head;
        while (current != null) {
            if (c.contains(current.data)) {
                tempSet.add(current.data);
                isModified = true;
            }
            current = current.next;
        }
        elements = tempSet.elements;
        list.head = tempSet.list.head;
        list.tail = tempSet.list.tail;
        size = tempSet.size;

        return isModified;
    }

    @Override
    public Iterator<E> iterator() {return null;}
    @Override
    public Object[] toArray() {return new Object[0];}
    @Override
    public <T> T[] toArray(T[] a) {return null;}
}