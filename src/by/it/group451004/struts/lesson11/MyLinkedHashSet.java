package by.it.group451004.struts.lesson11;

import java.util.*;

public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;

    private int size = 0;
    private Item<E>[] storage;
    private int threshold;

    private Item<E> head = null;
    private Item<E> tail = null;

    public static class Item<E> {
        E value;

        Item<E> next;

        Item<E> after;
        Item<E> before;

        public Item(E value) {
            this.value = value;
            this.next = null;
            this.after = null;
            this.before = null;
        }
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        this.storage = (Item<E>[]) new Item[DEFAULT_CAPACITY];
        this.threshold = DEFAULT_CAPACITY * 2;
    }

    private int hash(Object o, int length) {
        if (length == 0) return 0;

        return (o.hashCode() & 0x7fffffff) % length;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = storage.length * 2;
        Item<E>[] newArr = (Item<E>[]) new Item[newCapacity];
        this.threshold = newCapacity * 2;

        Item<E>[] oldStorage = this.storage;
        this.storage = newArr;

        for (Item<E> current : oldStorage) {
            while (current != null) {
                Item<E> nextInBucket = current.next;

                int hash = hash(current.value, newArr.length);

                current.next = newArr[hash];
                newArr[hash] = current;

                current = nextInBucket;
            }
        }
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
        Arrays.fill(storage, null);
        size = 0;
        head = tail = null;
    }

    @Override
    public boolean contains(Object o) {
        int hash = hash(o, storage.length);
        Item<E> current = storage[hash];

        while (current != null) {
            if (Objects.equals(o, current.value)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if (size >= threshold) {
            resize();
        }

        int hash = hash(e, storage.length);
        Item<E> current = storage[hash];
        Item<E> tailInBucket = null;

        while (current != null) {
            if (Objects.equals(e, current.value)) {
                return false;
            }
            tailInBucket = current;
            current = current.next;
        }

        Item<E> newNode = new Item<>(e);
        if (tailInBucket == null) {
            storage[hash] = newNode;
        } else {
            tailInBucket.next = newNode;
        }

        if (head == null) {
            head = newNode;
        } else {
            tail.after = newNode;
            newNode.before = tail;
        }
        tail = newNode;

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hash = hash(o, storage.length);
        Item<E> prevInBucket = null;
        Item<E> current = storage[hash];

        while (current != null) {
            boolean isMatch = (Objects.equals(o, current.value));

            if (isMatch) {
                if (prevInBucket == null) {
                    storage[hash] = current.next;
                } else {
                    prevInBucket.next = current.next;
                }

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

                size--;
                return true;
            }
            prevInBucket = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!this.contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (this.add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            if (this.remove(o)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;

        Item<E> current = head;
        Item<E> next;

        while (current != null) {
            next = current.after;

            if (!c.contains(current.value)) {
                this.remove(current.value);
                modified = true;
            }

            current = next;
        }
        return modified;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        Item<E> current = head;

        while (current != null) {
            result.append(current.value);
            current = current.after;
            if (current != null) {
                result.append(", ");
            }
        }
        result.append("]");
        return result.toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private Item<E> current = head;
            private Item<E> lastReturned = null;

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
                E value = current.value;
                current = current.after;
                return value;
            }

            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }

                E valueToRemove = lastReturned.value;
                lastReturned = null;
                MyLinkedHashSet.this.remove(valueToRemove);
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int i = 0;
        Item<E> current = head;
        while (current != null) {
            array[i++] = current.value;
            current = current.after;
        }
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        return (T[]) this.toArray();
    }
}