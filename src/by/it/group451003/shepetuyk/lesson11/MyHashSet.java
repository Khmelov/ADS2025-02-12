package by.it.group451003.shepetuyk.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static class Node<Object> {
        public Object o;
        public Node next;

        Node(Object o) {
            this.o = o;
        }
    }

    private int getHashCode(Object o) {
        int hashcode = o == null ? 0 : o.hashCode();
        hashcode = hashcode < 0 ? -hashcode : hashcode;
        return hashcode % buckets.length;
    }

    private void resize() {
        length = 0;
        Node[] tempBuckets = buckets;
        buckets = new Node[capacity *= 2];
        for (Node bucket : tempBuckets) {
            Node tempNode = bucket;
            while (tempNode != null) {
                add((E)tempNode.o);
                tempNode = tempNode.next;
            }
        }
    }
    private Node[] buckets;
    private static int capacity = 16;
    private int length = 0;
    private final float DEFAULT_LOAD_FACTOR = 0.75f;

    MyHashSet() {
        buckets = new Node[capacity];
    }
    public String toString() {
        StringBuilder strbldr = new StringBuilder();
        strbldr.append("[");
        for (Node bucket : buckets) {
            while (bucket != null) {
                strbldr.append(bucket.o + ", ");
                bucket = bucket.next;
            }
        }

        int sbLength = strbldr.length();
        if (sbLength > 1)
            strbldr.delete(sbLength - 2, sbLength);

        strbldr.append("]");

        return strbldr.toString();
    }

    @Override
    public int size() {
        return length;
    }

    @Override
    public void clear() {
        capacity = 16;
        length = 0;
        buckets = new Node[capacity];
    }

    @Override
    public boolean isEmpty() {
        return size() == 0 ;
    }

    @Override
    public boolean add(E e) {
        int h = getHashCode(e);
        Node prevNode = null, tempNode = buckets[h];
        while (tempNode != null) {
            if (tempNode.o.equals(e))
                return false;
            prevNode = tempNode;
            tempNode = tempNode.next;
        }

        if (prevNode != null) {
            tempNode = new Node(e);
            prevNode.next = tempNode;
        }
        else buckets[h] = new Node(e);

        length++;
        if (length > buckets.length * DEFAULT_LOAD_FACTOR)
            resize();

        return true;
    }

    @Override
    public boolean remove(Object o) {
        int h = getHashCode(o);
        Node prevNode = null, tempNode = buckets[h];

        while (tempNode != null) {
            if (o.equals(tempNode.o)) {
                if (prevNode == null)
                    buckets[h] = tempNode.next;
                else
                    prevNode.next = tempNode.next;
                length--;
                return true;
            }
            prevNode = tempNode;
            tempNode = tempNode.next;
        }

        return false;
    }

    @Override
    public boolean contains(Object o) {
        int h = getHashCode(o);
        Node tempNode = buckets[h];

        while (tempNode != null) {
            if (o.equals(tempNode.o))
                return true;
            tempNode = tempNode.next;
        }
        return false;
    }

    ///////////////////
    //// необязательные
    ///////////////////

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }
}
