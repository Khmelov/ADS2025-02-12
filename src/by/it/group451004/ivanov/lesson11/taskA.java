package by.it.group451004.ivanov.lesson11;

import java.util.*;

class MyHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<E>[] buckets;
    private int size;
    private int capacity;

    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
        }
    }

    public MyHashSet() {
        this.capacity = DEFAULT_CAPACITY;
        this.buckets = new Node[capacity];
        this.size = 0;
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

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldBuckets = buckets;
        capacity *= 2;
        buckets = new Node[capacity];
        size = 0;

        // Перехешируем все элементы
        for (Node<E> bucket : oldBuckets) {
            Node<E> current = bucket;
            while (current != null) {
                add(current.data);
                current = current.next;
            }
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        boolean first = true;

        for (Node<E> bucket : buckets) {
            Node<E> current = bucket;
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(current.data);
                first = false;
                current = current.next;
            }
        }

        sb.append("]");
        return sb.toString();
    }

    // Остальные методы интерфейса Set (необязательные для задания A)
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int bucketIndex = 0;
            private Node<E> current = null;
            private Node<E> lastReturned = null;
            private int expectedModCount = size;

            @Override
            public boolean hasNext() {
                if (expectedModCount != size) {
                    throw new ConcurrentModificationException();
                }

                // Находим следующий элемент
                while (current == null && bucketIndex < capacity) {
                    current = buckets[bucketIndex++];
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
                current = current.next;
                return data;
            }

            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }
                MyHashSet.this.remove(lastReturned.data);
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