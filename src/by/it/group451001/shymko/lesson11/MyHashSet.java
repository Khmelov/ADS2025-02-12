package by.it.group451001.shymko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;


public class MyHashSet<E> implements Set {

    static class List<E> {
        static class Node<E> {
            E data;
            Node<E> next;

            public Node(E data) {
                this.data = data;
                this.next = null;
            }
            public String toString(){
                return data.toString();
            }
        }

        public List(){
            head = null;
        }

        private Node<E> head;

        public void clear() {
            if (head == null) {
                return;
            }
            Node<E> temp = head.next;
            head = null;
            while (temp != null) {
                head = temp;
                temp = head.next;
                head = null;
            }
        }

        public boolean add(E data) {
            if (head == null) {
                head = new Node(data);
            }
            else {
                Node temp = head;
                while (temp.next != null) {
                    if (temp.data.equals(data)) {
                        return false;
                    }
                    temp = temp.next;
                }
                if (temp.data.equals(data)) {
                    return false;
                }
                temp.next = new Node(data);
            }
            return true;
        }

        public boolean removeNode(E data) {
            if (head == null) {
                return false;
            }
            if (data.equals(head.data)) {
                head = null;
                return true;
            }
            Node temp = head;
            while (temp.next != null) {
                if (temp.next.data.equals(data)) {
                    temp.next = temp.next.next;
                    return true;
                }
                temp = temp.next;
            }
            return false;
        }

        public String toString() {
            if (head == null) {
                return "";
            }
            Node<E> temp = head;
            String s = temp.toString();
            temp = temp.next;
            while (temp != null) {
                s += ", " + temp.data;
                temp = temp.next;
            }
            return s;
        }

        public boolean contains(E data) {
            if (head == null) {
                return false;
            }
            Node<E> temp = head;
            while (temp != null) {
                if (temp.data.equals(data)) {
                    return true;
                }
            }
            return false;
        }

        public boolean empty() {
            if (head == null) {
                return true;
            }
            return false;
        }
    }

    private List[] elements;


    private final int initalSize = 10;

    private int size;

    private int capacity;

    public MyHashSet() {
        elements = new List[initalSize];
        for(int i = 0; i < initalSize; i++) {
            elements[i] = new List();
        }
        size = 0;
        capacity = initalSize;
    }

    public String toString() {
        String s = "[";
        if(size == 0)
            return s + "]";
        for(int i = 0; i < capacity; ++i){
            List temp = elements[i];
            if(!temp.empty()) {
                if(s == "[") s += temp.toString();
                else s += ", " + temp.toString();
            }
        }
        return s + "]";
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
    public boolean contains(Object o) {
        if(o.hashCode() >= capacity) {
            return false;
        }
        return elements[o.hashCode()].contains((E)o);
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public boolean add(Object o) {
        int hash = o.hashCode();
        if(hash >= capacity) {
            List[] newElements = new List[hash + 1];
            System.arraycopy(elements, 0, newElements, 0, capacity);
            for(int i = capacity; i < newElements.length; i++) {
                newElements[i] = new List();
            }
            elements = newElements;
            capacity = hash + 1;
        }
        boolean f = elements[hash].add(o);
        size += f ? 1 : 0;
        return f;
    }

    @Override
    public boolean remove(Object o) {
        if(o.hashCode() >= capacity) {
            return false;
        }
        boolean res = elements[o.hashCode()].removeNode(o);
        if(res) size--;
        return res;
    }

    @Override
    public boolean addAll(Collection c) {
        return false;
    }


    @Override
    public void clear() {
        int i = 0;
        for (; i < capacity; i++) {
            elements[i].clear();
        }
        elements = new List[initalSize];
        for(i = 0; i < initalSize; i++) {
            elements[i] = new List();
        }
        capacity = initalSize;
        size = 0;
    }


    @Override
    public boolean removeAll(Collection c) {
        return false;
    }


    @Override
    public boolean retainAll(Collection c) {
        return false;
    }

    @Override
    public boolean containsAll(Collection c) {
        return false;
    }


    @Override
    public Object[] toArray(Object[] a) {
        return new Object[0];
    }
}
