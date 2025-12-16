package by.it.group451003.chveikonstantcin.lesson10;

import java.util.*;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> prev;

        Node(E data) {
            this.data = data;
        }
    }

    private Node<E> first;
    private Node<E> last;
    private int size;

    public MyLinkedList() {
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<E> current = first;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    public E remove(int index) {
        checkIndex(index);
        Node<E> current = getNode(index);
        return unlink(current);
    }

    @Override
    public boolean remove(Object o) {
        for (Node<E> current = first; current != null; current = current.next) {
            if ((o == null && current.data == null) || (o != null && o.equals(current.data))) {
                unlink(current);
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E element) {
        Node<E> newNode = new Node<>(element);
        if (first == null) {
            first = last = newNode;
        } else {
            newNode.next = first;
            first.prev = newNode;
            first = newNode;
        }
        size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> newNode = new Node<>(element);
        if (last == null) {
            first = last = newNode;
        } else {
            last.next = newNode;
            newNode.prev = last;
            last = newNode;
        }
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        return first.data;
    }

    @Override
    public E getLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }
        return last.data;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (first == null) {
            return null;
        }
        return unlink(first);
    }

    @Override
    public E pollLast() {
        if (last == null) {
            return null;
        }
        return unlink(last);
    }

    private Node<E> getNode(int index) {
        Node<E> current;
        if (index < size / 2) {
            current = first;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = last;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    private E unlink(Node<E> node) {
        E data = node.data;
        Node<E> prev = node.prev;
        Node<E> next = node.next;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
        }

        node.data = null;
        node.next = null;
        node.prev = null;
        size--;
        return data;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E remove() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        return pollFirst();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E removeFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        return pollFirst();
    }

    @Override
    public E removeLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }
        return pollLast();
    }

    @Override
    public E peekFirst() {
        return first == null ? null : first.data;
    }

    @Override
    public E peekLast() {
        return last == null ? null : last.data;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        for (Node<E> current = last; current != null; current = current.prev) {
            if ((o == null && current.data == null) || (o != null && o.equals(current.data))) {
                unlink(current);
                return true;
            }
        }
        return false;
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private Node<E> current = last;

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
                current = current.prev;
                return data;
            }
        };
    }

    @Override
    public boolean contains(Object o) {
        for (Node<E> current = first; current != null; current = current.next) {
            if ((o == null && current.data == null) || (o != null && o.equals(current.data))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = first;

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

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int i = 0;
        for (Node<E> current = first; current != null; current = current.next) {
            array[i++] = current.data;
        }
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (Node<E> current = first; current != null; current = current.next) {
            a[i++] = (T) current.data;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
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
            addLast(element);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Node<E> current = first; current != null; ) {
            Node<E> next = current.next;
            if (c.contains(current.data)) {
                unlink(current);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (Node<E> current = first; current != null; ) {
            Node<E> next = current.next;
            if (!c.contains(current.data)) {
                unlink(current);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    @Override
    public void clear() {
        for (Node<E> current = first; current != null; ) {
            Node<E> next = current.next;
            current.data = null;
            current.next = null;
            current.prev = null;
            current = next;
        }
        first = last = null;
        size = 0;
    }
}