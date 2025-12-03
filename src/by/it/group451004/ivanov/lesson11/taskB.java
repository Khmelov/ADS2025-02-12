package by.it.group451004.ivanov.lesson11;

import java.util.*;

class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<E>[] buckets;
    private Node<E> head;
    private Node<E> tail;
    private int size;
    private int capacity;

    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> prev;
        Node<E> nextOrder;

        Node(E data) {
            this.data = data;
        }
    }

    public MyLinkedHashSet() {
        this.capacity = DEFAULT_CAPACITY;
        this.buckets = new Node[capacity];
        this.size = 0;
        this.head = null;
        this.tail = null;
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
        for (int i = 0; i < capacity; i++) {
            buckets[i] = null;
        }
        head = null;
        tail = null;
        size = 0;
    }

    private int hash(Object key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException();
        }

        int index = hash(element);
        Node<E> current = buckets[index];

        while (current != null) {
            if (Objects.equals(current.data, element)) {
                return false;
            }
            current = current.next;
        }

        Node<E> newNode = new Node<>(element);
        newNode.next = buckets[index];
        buckets[index] = newNode;

        if (head == null) {
            head = tail = newNode;
        } else {
            tail.nextOrder = newNode;
            newNode.prev = tail;
            tail = newNode;
        }

        size++;

        if (size > capacity * LOAD_FACTOR) {
            resize();
        }

        return true;
    }

    @Override
    public boolean remove(Object element) {
        if (element == null) {
            return false;
        }

        int index = hash(element);
        Node<E> current = buckets[index];
        Node<E> prev = null;

        while (current != null) {
            if (Objects.equals(current.data, element)) {
                if (prev == null) {
                    buckets[index] = current.next;
                } else {
                    prev.next = current.next;
                }

                if (current.prev != null) {
                    current.prev.nextOrder = current.nextOrder;
                } else {
                    head = current.nextOrder;
                }

                if (current.nextOrder != null) {
                    current.nextOrder.prev = current.prev;
                } else {
                    tail = current.prev;
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
        if (element == null) {
            return false;
        }

        int index = hash(element);
        Node<E> current = buckets[index];

        while (current != null) {
            if (Objects.equals(current.data, element)) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
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
            current = current.nextOrder;
        }

        sb.append("]");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldBuckets = buckets;
        capacity *= 2;
        buckets = new Node[capacity];

        Node<E> current = head;
        head = tail = null;
        size = 0;

        while (current != null) {
            Node<E> next = current.nextOrder;
            add(current.data);
            current = next;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = head;
            private Node<E> lastReturned = null;
            private int expectedModCount = size;

            @Override
            public boolean hasNext() {
                if (expectedModCount != size) {
                    throw new ConcurrentModificationException();
                }
                return current != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                lastReturned = current;
                E data = current.data;
                current = current.nextOrder;
                return data;
            }

            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }
                MyLinkedHashSet.this.remove(lastReturned.data);
                lastReturned = null;
                expectedModCount = size;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int index = 0;
        for (E element : this) {
            result[index++] = element;
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) Arrays.copyOf(toArray(), size, a.getClass());
        }
        System.arraycopy(toArray(), 0, a, 0, size);
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
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Set)) return false;
        Set<?> that = (Set<?>) o;
        if (this.size != that.size()) return false;

        return containsAll(that);
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (E element : this) {
            result += element.hashCode();
        }
        return result;
    }
}