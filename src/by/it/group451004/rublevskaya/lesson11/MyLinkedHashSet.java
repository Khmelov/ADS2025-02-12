package by.it.group451004.rublevskaya.lesson11;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    private static final int CAPACITY = 10;
    private Node<E>[] hashTable;
    private Node<E> head;
    private Node<E> tail;
    private int currentSize;

    public MyLinkedHashSet() {
        hashTable = new Node[CAPACITY];
        currentSize = 0;
        head = tail = null;
    }

    private static class Node<E> {
        E info;
        Node<E> next;
        Node<E> prev;

        Node(E info) {
            this.info = info;
            this.next = this.prev = null;
        }
    }

    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public void clear() {
        hashTable = new Node[CAPACITY];
        head = tail = null;
        currentSize = 0;
    }

    @Override
    public boolean isEmpty() {
        return currentSize == 0;
    }

    @Override
    public boolean contains(Object o) {
        int index = getIndex(o);
        Node<E> currentHashTable = hashTable[index];

        while (currentHashTable != null) {
            if (Objects.equals(currentHashTable.info, o)) {
                return true;
            }
            currentHashTable = currentHashTable.next;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> currentHashTable = head;

            @Override
            public boolean hasNext() {
                return currentHashTable != null;
            }

            @Override
            public E next() {
                E info = currentHashTable.info;
                currentHashTable = currentHashTable.next;
                return info;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[currentSize];
        Node<E> currentHashTable = head;
        int index = 0;
        while (currentHashTable != null) {
            arr[index] = currentHashTable.info;
            index++;
            currentHashTable = currentHashTable.next;
        }
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] t) {
        if (t.length < currentSize) {
            t = (T[]) Array.newInstance(t.getClass().getComponentType(), currentSize);
        }
        Node<E>  currentHashTable = head;
        int index = 0;
        while ( currentHashTable != null) {
            t[index++] = (T)  currentHashTable.info;
            currentHashTable =  currentHashTable.next;
        }
        return t;
    }

    @Override
    public boolean add(E info) {
        if (contains(info)) {
            return false;
        }

        int index = getIndex(info);
        Node<E>  newHashTable = new Node<>(info);


        if (hashTable[index] == null) {
            hashTable[index] = newHashTable;
        } else {
            Node<E> current = hashTable[index];
            while (current.next != null) {
                current = current.next;
            }
            current.next = newHashTable;
        }
        if (tail == null) {
            head = tail = newHashTable;
        } else {
            tail.next = newHashTable;
            newHashTable.prev = tail;
            tail = newHashTable;
        }

        currentSize++;
        return true;
    }

    @Override
    public boolean remove(Object obj) {
        int index = getIndex(obj);
        Node<E> currentHashTable = hashTable[index];
        Node<E> prevHashTable = null;

        while (currentHashTable != null) {
            if (Objects.equals(currentHashTable.info, obj)) {
                if (prevHashTable == null) {
                    hashTable[index] = currentHashTable.next;
                } else {
                    prevHashTable.next = currentHashTable.next;
                }
                if (currentHashTable.prev != null) {
                    currentHashTable.prev.next = currentHashTable.next;
                } else {
                    head = currentHashTable.next;
                }

                if (currentHashTable.next != null) {
                    currentHashTable.next.prev = currentHashTable.prev;
                } else {
                    tail = currentHashTable.prev;
                }

                currentSize--;
                return true;
            }
            prevHashTable = currentHashTable;
            currentHashTable = currentHashTable.next;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object o : collection) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        boolean isCorrect = false;
        for (E value : collection) {
            if (add(value)) {
                isCorrect = true;
            }
        }
        return isCorrect;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean isCorrect = false;
        Node<E> currentHashTable = head;

        while (currentHashTable != null) {
            if (!collection.contains(currentHashTable.info)) {
                Node<E> removeHashTable = currentHashTable;
                currentHashTable = currentHashTable.next;

                remove(removeHashTable.info);
                isCorrect = true;
            } else {
                currentHashTable = currentHashTable.next;
            }
        }
        return isCorrect;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean isCorrect = false;
        for (Object o : c) {
            if (remove(o)) {
                isCorrect = true;
            }
        }
        return isCorrect;
    }

    private int getIndex(Object info) {
        return Math.abs(Objects.hashCode(info)) % hashTable.length;
    }

    @Override
    public String toString() {
        StringBuilder resultStr = new StringBuilder("[");
        Node<E> currentHashTable = head;
        while (currentHashTable != null) {
            resultStr.append(currentHashTable.info).append(", ");
            currentHashTable = currentHashTable.next;
        }
        if (resultStr.length() > 1) {
            resultStr.setLength(resultStr.length() - 2);
        }
        resultStr.append("]");
        return resultStr.toString();
    }
}
