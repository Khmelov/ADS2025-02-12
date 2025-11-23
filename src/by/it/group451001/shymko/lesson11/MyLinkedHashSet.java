package by.it.group451001.shymko.lesson11;

import java.util.*;

public class MyLinkedHashSet<E> implements Set<E> {

    static class Node<E> {
        E data;
        Node next;
        Node before;
        Node after;
        public Node(E data) {
            this.data = data;
        }
    }

    Node[] elements;
    int size;
    final int initialCapasity = 10;
    int capacity;
    Node head;
    Node tail;

    public MyLinkedHashSet() {
        size = 0;
        elements = new Node[initialCapasity];
        capacity = initialCapasity;
        for (int i = 0; i < elements.length; i++) {
            elements[i] = null;
        }
        head = tail = null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (head != null) {
            Node<E> temp = head;
            while (temp.after != null) {
                sb.append(temp.data);
                sb.append(", ");
                temp = temp.after;
            }
            sb.append(temp.data);
            sb.append("]");
        }
        else{
            return "[]";
        }
        return sb.toString();
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(Object o) {
        if(o.hashCode() >= capacity){
            return false;
        }
        Node<E> temp = elements[o.hashCode()];
        while(temp != null){
            if(temp.data.equals(o)){
                return true;
            }
            temp = temp.after;
        }
        return false;
    }

    public Iterator iterator() {
        return null;
    }

    public Object[] toArray() {
        return new Object[0];
    }

    public boolean add(Object o) {
        if(o.hashCode() >= capacity) {
            Node<E>[] temp = new Node[o.hashCode() + 1];
            System.arraycopy(elements, 0, temp, 0, elements.length);
            elements = temp;
            for(int i = capacity; i < o.hashCode() + 1; i++) {
                elements[i] = null;
            }
            capacity = o.hashCode() + 1;
        }
        Node<E> temp = elements[o.hashCode()];
        if(temp == null) {
            Node<E> newNode = new Node<E>((E)o);
            newNode.before = tail;
            newNode.after = null;
            if(head == null) {
                head = newNode;
                tail = newNode;
            }
            else {
                tail.after = newNode;
                tail = newNode;
            }
            elements[o.hashCode()] = newNode;
            size++;
            return true;
        }
        while(temp.next != null) {
            if(temp.data.equals(o)) {
                return false;
            }
            temp = temp.next;
        }
        if(temp.data.equals(o)) {
            return false;
        }
        Node<E> newNode = new Node<E>((E)o);
        newNode.before = tail;
        newNode.after = null;
        tail.after = newNode;
        tail = newNode;
        temp.next = newNode;
        size++;
        return true;
    }

    public boolean remove(Object o) {
        if(o.hashCode() >= capacity) {
            return false;
        }
        Node<E> temp = elements[o.hashCode()];
        if(temp == null) {
            return false;
        }
        if(temp.data.equals(o)) {
            if(temp == tail) {
                tail = tail.before;
                tail.after = null;
                elements[o.hashCode()] = temp.next;
                temp = null;
                size--;
                if(size == 0) {
                    head = null;
                }
                return true;
            }
            if(temp == head) {
                head = head.after;
                elements[o.hashCode()] = temp.next;
                temp = null;
                size--;
                if(size == 0) {
                    tail = null;
                }
                return true;
            }
            temp.before.after = temp.after;
            temp.after.before = temp.before;
            elements[o.hashCode()] = temp.next;
            temp = null;
            size--;
            return true;
        }
        while(temp.next != null) {
            if(temp.next.data.equals(o)) {
                if(temp.next == tail) {
                    tail = tail.before;
                    tail.after = null;
                    temp.next = temp.next.next;
                    size--;
                    if(size == 0) {
                        head = null;
                    }
                    return true;
                }
                if(temp.next == head) {
                    head = head.after;
                    temp.next = temp.next.next;
                    size--;
                    if(size == 0) {
                        tail = null;
                    }
                    return true;
                }
                temp.next.before.after = temp.next.after;
                temp.next.after.before = temp.next.before;
                temp.next = temp.next.next;
                size--;
                return true;
            }
        }
        return false;
    }

    public boolean addAll(Collection c) {
        boolean changed = false;
        for(Object o : c) {
            changed |= add(o);
        }
        return changed;
    }

    public void clear() {
        size = 0;
        elements = new Node[initialCapasity];
        capacity = initialCapasity;
        for (int i = 0; i < elements.length; i++) {
            elements[i] = null;
        }
        head = tail = null;
    }

    public boolean removeAll(Collection c) {
        boolean changed = false;
        for(Object o : c) {
            if(remove(o)) {
                changed = true;
            }
        }
        return changed;
    }

    public boolean retainAll(Collection c) {
        Node<E> temp = head;
        boolean changed = false;
        while(temp != null) {
            if(!c.contains(temp.data)) {
                E e = (E)temp.data;
                temp = temp.after;
                changed |= remove(e);
            }
            else{
                temp = temp.after;
            }
        }
        return changed;
    }

    public boolean containsAll(Collection c) {
        for(Object o : c) {
            if(!contains(o)) {
                return false;
            }
        }
        return true;
    }

    public Object[] toArray(Object[] a) {
        return new Object[0];
    }
}
