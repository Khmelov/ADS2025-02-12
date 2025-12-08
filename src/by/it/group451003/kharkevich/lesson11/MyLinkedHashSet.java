package by.it.group451003.kharkevich.lesson11;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    static class Node<E> {
        E data;
        Node<E> previous;
        Node<E> next;
        Node<E> nextInSet;

        public Node(E data) {
            this.data = data;
        }
    }

    static class List<E> {
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
    List<E> list;

    public MyLinkedHashSet() {
        this(INITIAL_SIZE);
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int size) {
        elements = new Node[size];
        list = new List<>();
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
        return Math.abs(element.hashCode()) % elements.length;
    }

    @Override
    public boolean add(E e) {
        int index = getHashCode(e);
        Node<E> current = elements[index];
        while (current != null) {
            if (current.data.equals(e)) {
                return false;
            }
            current = current.nextInSet;
        }
        Node<E> newNode = new Node<>(e);
        newNode.nextInSet = elements[index];
        elements[index] = newNode;
        size++;
        list.add(newNode);
        if (size > elements.length * 0.75) {
            resize();
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] newElements = new Node[elements.length * 2];
        Node<E> current = list.head;
        while (current != null) {
            int newIndex = Math.abs(current.data.hashCode()) % newElements.length;
            current.nextInSet = newElements[newIndex];
            newElements[newIndex] = current;
            current = current.next;
        }
        elements = newElements;
    }

    @Override
    public boolean remove(Object o) {
        E e = (E) o;
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
        E e = (E) o;
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
        for (Object object : c) {
            if (!contains(object)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;
        for (E element : c) {
            if (add(element)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = false;
        for (Object object : c) {
            if (remove(object)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean isModified = false;
        Iterator<Node<E>> iterator = new Iterator<Node<E>>() {
            private Node<E> current = list.head;
            private Node<E> lastReturned = null;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Node<E> next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                lastReturned = current;
                current = current.next;
                return lastReturned;
            }
        };

        while (iterator.hasNext()) {
            Node<E> node = iterator.next();
            if (!c.contains(node.data)) {
                remove(node.data);
                isModified = true;
            }
        }
        return isModified;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = list.head;
            private Node<E> lastReturned = null;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                lastReturned = current;
                E data = current.data;
                current = current.next;
                return data;
            }

            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }
                MyLinkedHashSet.this.remove(lastReturned.data);
                lastReturned = null;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        Node<E> current = list.head;
        for (int i = 0; i < size && current != null; i++) {
            array[i] = current.data;
            current = current.next;
        }
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }

        Node<E> current = list.head;
        for (int i = 0; i < size && current != null; i++) {
            a[i] = (T) current.data;
            current = current.next;
        }

        if (a.length > size) {
            a[size] = null;
        }

        return a;
    }
}