package by.it.group451004.rublevskaya.lesson11;

import java.util.Set;
import java.util.Iterator;

public class MyHashSet<E> implements Set<E> {
    private static final int CAPACITY = 10;

    private Node<E>[] nodeLists;
    private int currentSize;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        nodeLists = (Node<E>[]) new Node[CAPACITY];
        currentSize = 0;
    }

    private static class Node<E> {
        E info;
        Node<E> next;

        Node(E value, Node<E> next) {
            this.info = value;
            this.next = next;
        }
    }

    private int getIndex(Object obj) {
        int hash = obj == null ? 0 : obj.hashCode();
        return (hash & 0x7FFFFFFF) % nodeLists.length;
    }

    @Override
    public boolean add(Object obj) {
        E e = (E) obj;
        int index = getIndex(e);
        Node<E> node = nodeLists[index];

        while (node != null) {
            if (node.info == null && e == null ||
                    node.info != null && node.info.equals(e)) {
                return false;
            }
            node = node.next;
        }

        nodeLists[index] = new Node<>(e, nodeLists[index]);
        currentSize++;
        return true;
    }

    @Override
    public boolean contains(Object obj) {
        int ind = getIndex(obj);
        Node<E> node = nodeLists[ind];

        while (node != null) {
            if (node.info == null && obj == null ||
                    node.info != null && node.info.equals(obj)) {
                return true;
            }
            node = node.next;
        }

        return false;
    }

    @Override
    public boolean remove(Object obj) {
        int index = getIndex(obj);
        Node<E> node = nodeLists[index];
        Node<E> nodePrev = null;

        while (node != null) {
            if (node.info == null && obj == null ||
                    node.info != null && node.info.equals(obj)) {
                if (nodePrev == null) {
                    nodeLists[index] = node.next;
                } else {
                    nodePrev.next = node.next;
                }
                currentSize--;
                return true;
            }
            nodePrev = node;
            node = node.next;
        }

        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < nodeLists.length; i++) {
            nodeLists[i] = null;
        }
        currentSize = 0;
    }

    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public boolean isEmpty() {
        return currentSize == 0;
    }

    // Просто для красивого вывода — порядок не гарантирован
    @Override
    public String toString() {
        StringBuilder resultStr = new StringBuilder();
        resultStr.append("[");
        boolean isCorrect = true;

        for (Node<E> list : nodeLists) {
            Node<E> node = list;
            while (node != null) {
                if (!isCorrect) {
                    resultStr.append(", ");
                }
                resultStr.append(node.info);
                isCorrect = false;
                node = node.next;
            }
        }

        resultStr.append("]");
        return resultStr.toString();
    }

    // Остальные методы Set<E> можно реализовать при необходимости
    // Но по заданию они не требуются:
    @Override public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }
    @Override public Object[] toArray() {
        throw new UnsupportedOperationException();
    }
    @Override public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
    @Override public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    @Override public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }
    @Override public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    @Override public boolean removeAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }
}
