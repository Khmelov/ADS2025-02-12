package by.it.group410902.gavlev.lesson11;

import by.it.group410902.gavlev.lesson09.ListA;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private class Node<E> {
        public E data;
        public Node<E> bucketNext;
        public Node<E> prev;
        public Node<E> next;

        Node(E data) {
            this.data = data;
        }

        Node() {
            this(null);
        }
    }

    Node<E>[] data;
    Node<E> root;
    int size = 0;

    public MyLinkedHashSet(int capacity) {
        data = new Node[capacity];
        root = new Node<E>();
    }

    public MyLinkedHashSet() {
        this(20);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<E> temp = root.next;
        while (temp != null) {
            sb.append(temp.data.toString()).append(", ");
            temp = temp.next;
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        return sb.toString();
    }

    private int hashIndex(Object o, int len) {
        return (o.hashCode() & 0x7fffffff) % len;
    }

    private int hashIndex(Object o) {
        return hashIndex(o, data.length);
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        int hashID = hashIndex(o);
        Node<E> temp = data[hashID];
        if (temp == null) return false;
        while (temp != null) {
            if (o.equals(temp.data)) return true;
            temp = temp.bucketNext;
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        if (size == 0) return new Object[0];
        Object[] answer = new Object[size];
        Node<E> temp = root.next;
        for (int i = 0; temp != null; i++) {
            answer[i] = temp.data;
            temp = temp.next;
        }
        return answer;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return a;
    }

    @Override
    public boolean add(Object o) {
        if (o == null) return false;
        int hashID = hashIndex(o);
        Node<E> temp = data[hashID];
        Node<E> prevLast = root.prev;
        Node<E> newNode = new Node<E>((E) o);
        if (temp == null) {
            temp = newNode;
            data[hashID] = newNode;
        }
        else {
            while (true) {
                if (o.equals(temp.data)) {
                    return false;
                }
                if (temp.bucketNext == null) break;
                temp = temp.bucketNext;
            }
            temp.bucketNext = newNode;
            temp = newNode;
        }
        if (isEmpty()) {
            root.next = temp;
        }
        else {
            prevLast.next = temp;
            temp.prev = prevLast;
        }
        root.prev = temp;
        size++;
        if ((double)size / data.length >= 0.75) {
            resize();
        }
        return true;
    }

    private void resize() {
        MyLinkedHashSet<E> newHashSet = new MyLinkedHashSet<>(data.length * 3 / 2);
        for (var el : this) {
            newHashSet.add(el);
        }
        this.data = newHashSet.data;
        this.root = newHashSet.root;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        int hashID = hashIndex(o);
        Node<E> temp = data[hashID];
        if (temp == null) return false;
        if (o.equals(temp.data)) {
            data[hashID] = temp.bucketNext;
            unlinkFromOrder(temp);
            size--;
            return true;
        }
        while (temp.bucketNext != null) {
            if (o.equals(temp.bucketNext.data)) {
                unlinkFromOrder(temp.bucketNext);
                temp.bucketNext = temp.bucketNext.bucketNext;
                size--;
                return true;
            }
            temp = temp.bucketNext;
        }
        return false;
    }

    private void unlinkFromOrder(Node<E> node) {
        Node<E> prevNode = node.prev;
        Node<E> nextNode = node.next;

        if (prevNode != null) prevNode.next = nextNode;
        else root.next = nextNode;

        if (nextNode != null) nextNode.prev = prevNode;
        else root.prev = prevNode;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (var el : collection) {
            if (!contains(el)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        boolean result = false;
        for (var el : collection) {
            if (!result) result = add(el);
            else add(el);
        }
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        ListA<E> toRemove = new ListA<>();
        for (var el : this) {
            if (!collection.contains(el)) {
                toRemove.add(el);
            }
        }
        return removeAll(toRemove);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean result = false;
        for (var el : collection) {
            if (!result) result = remove(el);
            else remove(el);
        }
        return result;
    }

    @Override
    public void clear() {
        for (int i = 0; i < data.length; i++) {
            data[i] = null;
        }
        size = 0;
        root.next = null;
        root.prev = null;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> temp = root;
            @Override
            public boolean hasNext() {
                if (temp.next == null) return false;
                return true;
            }

            @Override
            public E next() {
                temp = temp.next;
                return temp.data;
            }
        };
    }
}
