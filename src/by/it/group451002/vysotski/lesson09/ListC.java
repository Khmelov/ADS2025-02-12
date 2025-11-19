package by.it.group451002.vysotski.lesson09;

import java.util.*;

public class ListC<E> implements List<E> {

    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> prev;

        Node(E data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }

        Node(E data, Node<E> prev, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public ListC() {
        head = null;
        tail = null;
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node<E> current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        Node<E> newNode = new Node<>(e);

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);

        Node<E> toRemove = getNode(index);
        E removedData = toRemove.data;

        if (toRemove.prev == null) {
            // Removing head
            head = toRemove.next;
        } else {
            toRemove.prev.next = toRemove.next;
        }

        if (toRemove.next == null) {
            // Removing tail
            tail = toRemove.prev;
        } else {
            toRemove.next.prev = toRemove.prev;
        }

        size--;
        return removedData;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index);

        if (index == size) {
            add(element);
            return;
        }

        Node<E> currentNode = getNode(index);
        Node<E> newNode = new Node<>(element, currentNode.prev, currentNode);

        if (currentNode.prev == null) {
            head = newNode;
        } else {
            currentNode.prev.next = newNode;
        }
        currentNode.prev = newNode;

        size++;
    }

    @Override
    public boolean remove(Object o) {
        Node<E> current = head;

        while (current != null) {
            if (Objects.equals(current.data, o)) {
                if (current.prev == null) {
                    head = current.next;
                } else {
                    current.prev.next = current.next;
                }

                if (current.next == null) {
                    tail = current.prev;
                } else {
                    current.next.prev = current.prev;
                }

                size--;
                return true;
            }
            current = current.next;
        }

        return false;
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);

        Node<E> node = getNode(index);
        E oldData = node.data;
        node.data = element;
        return oldData;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        Node<E> current = head;
        int index = 0;

        while (current != null) {
            if (Objects.equals(current.data, o)) {
                return index;
            }
            current = current.next;
            index++;
        }

        return -1;
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        return getNode(index).data;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        Node<E> current = tail;
        int index = size - 1;

        while (current != null) {
            if (Objects.equals(current.data, o)) {
                return index;
            }
            current = current.prev;
            index--;
        }

        return -1;
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
        if (c.isEmpty()) {
            return false;
        }

        for (E element : c) {
            add(element);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        checkIndexForAdd(index);

        if (c.isEmpty()) {
            return false;
        }

        int currentIndex = index;
        for (E element : c) {
            add(currentIndex++, element);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        Node<E> current = head;

        while (current != null) {
            Node<E> next = current.next;
            if (c.contains(current.data)) {
                removeNode(current);
                modified = true;
            }
            current = next;
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> current = head;

        while (current != null) {
            Node<E> next = current.next;
            if (!c.contains(current.data)) {
                removeNode(current);
                modified = true;
            }
            current = next;
        }

        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("subList operation is not supported");
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException("listIterator operation is not supported");
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException("listIterator operation is not supported");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("toArray operation is not supported");
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        Node<E> current = head;
        for (int i = 0; i < size; i++) {
            array[i] = current.data;
            current = current.next;
        }
        return array;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                E data = current.data;
                current = current.next;
                return data;
            }
        };
    }

    // Helper methods
    private Node<E> getNode(int index) {
        checkIndex(index);

        // Optimize traversal direction based on index position
        if (index < size / 2) {
            // Traverse from head
            Node<E> current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            return current;
        } else {
            // Traverse from tail
            Node<E> current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
            return current;
        }
    }

    private void removeNode(Node<E> node) {
        if (node.prev == null) {
            head = node.next;
        } else {
            node.prev.next = node.next;
        }

        if (node.next == null) {
            tail = node.prev;
        } else {
            node.next.prev = node.prev;
        }

        size--;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
}