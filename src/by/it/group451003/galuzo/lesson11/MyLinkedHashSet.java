package by.it.group451003.galuzo.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    private static class Node<Object> {
        private Object o;
        private Node next;
        private Node prev;

        Node(Node prev, Object o, Node next) {
            this.o = o;
            this.prev = prev;
            this.next = next;
        }
    }

    private int getHashCode(Object o) {
        int hashcode = o == null ? 0 : o.hashCode();
        hashcode = hashcode < 0 ? -hashcode : hashcode;
        return hashcode % buckets.length;
    }

    private void resize() {
        Node[] tempBuckets = buckets;
        buckets = new Node[capacity *= 2];
        for (Node bucket : tempBuckets) {
            Node tempNode = bucket;
            while (tempNode != null) {
                add((E)tempNode.o);
                tempNode = tempNode.next;
            }
        }
        needResize = false;
    }

    private Node first = null, last = null;
    private Node[] buckets;
    private static int capacity = 16;
    private int length = 0;
    private Boolean needResize = false;

    private final float DEFAULT_LOAD_FACTOR = 0.75f;

    MyLinkedHashSet() {
        buckets = new Node[capacity];
    }
    public String toString() {
        StringBuilder strbldr = new StringBuilder();
        strbldr.append("[");
        Node temp = first;
        while (temp != null) {
            strbldr.append(temp.o + ", ");
            temp = temp.next;
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
        last = first = null;
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
            if (e.equals(tempNode.o))
                return false;
            prevNode = tempNode;
            tempNode = tempNode.next;
        }

        if (prevNode != null)
            prevNode.next = new Node(prevNode, e, null);
        else
            buckets[h] = new Node(null, e, null);

        if (!needResize) {
            if (last == null) {
                last = new Node(null, e, null);
                first = last;
            } else {
                last.next = new Node(last, e, null);
                last = last.next;
            }
            length++;

            if (length > capacity * DEFAULT_LOAD_FACTOR) {
                needResize = true;
                resize();
            }
        }

        return true;
    }

    @Override
    public boolean remove(Object o) {
        int h = getHashCode(o);
        Node tempNode = buckets[h];

        while (tempNode != null) {
            if (tempNode.o.equals(o)) {
                if (tempNode.prev == null) {
                    buckets[h] = tempNode.next;
                    if (buckets[h] != null)
                        buckets[h].prev = null;
                }
                else {
                    tempNode.prev.next = tempNode.next;
                    if (tempNode.next != null)
                        tempNode.next.prev = tempNode.prev;
                }

                length--;

                Node n = first;
                while (n != null) {
                    if (n.o.equals(o)) {
                        if (first == last)
                            first = last = null;
                        else if (n.prev != null) {
                            n.prev.next = n.next;
                            if (n.next != null)
                                n.next.prev = n.prev;
                            else
                                last = n.prev;
                        }
                        else {
                            first = first.next;
                            if (first.next != null)
                                first.prev = null;
                        }

                        return true;
                    }
                    n = n.next;
                }
            }
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

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (!contains(o))
                return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (Object o : c) {
            if (o == null)
                return false;
            add((E)o);
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Boolean changed = false;
        Node n = first;
        while (n != null) {
            if (!c.contains(n.o)) {
                remove(n.o);
                changed = true;
            }
            n = n.next;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        for (Object o : c) {
            if (o == null)
                return false;
            remove(o);
        }
        return true;
    }

    ///////////////////
    //// необязательные
    ///////////////////

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
