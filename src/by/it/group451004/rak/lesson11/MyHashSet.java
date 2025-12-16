package by.it.group451004.rak.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {
    MyLinkedList<E>[] basket;
    int size = 0;

    static final int DEFAULT_CAPACITY = 256;

    public MyHashSet(){
        this(DEFAULT_CAPACITY);
    }

    public MyHashSet(int capacity){
        basket = (MyLinkedList<E>[]) new MyLinkedList[capacity];
        for (int i = 0; i < capacity; i++){
            basket[i] = new MyLinkedList<>();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean firstElement = true;
        for (MyLinkedList<E> list : basket) {
            MyLinkedList<E>.Node current = list.first;
            while (current != null) {
                if (!firstElement) {
                    sb.append(", ");
                }
                sb.append(current.value);
                firstElement = false;
                current = current.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private int getIndex(Object o) {
        int hash = (o == null) ? 0 : o.hashCode();
        int index = hash % basket.length;
        if (index < 0) index += basket.length;
        return index;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (MyLinkedList<E> list : basket) {
            list.removeAll();
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private void rehash() {
        int newCapacity = basket.length * 2;
        MyLinkedList<E>[] newBasket = (MyLinkedList<E>[]) new MyLinkedList[newCapacity];
        for (int i = 0; i < newCapacity; i++) {
            newBasket[i] = new MyLinkedList<>();
        }

        for (MyLinkedList<E> list : basket) {
            MyLinkedList<E>.Node current = list.first;
            while (current != null) {
                int index = (current.value == null ? 0 : current.value.hashCode()) % newCapacity;
                if (index < 0) index += newCapacity;
                newBasket[index].add(current.value);
                current = current.next;
            }
        }
        basket = newBasket;
    }

    @Override
    public boolean add(E e) {
        if (size >= basket.length) {
            rehash();
        }
        int index = getIndex(e);
        boolean added = basket[index].add(e);
        if (added) size++;
        return added;
    }

    @Override
    public boolean remove(Object o) {
        int index = getIndex(o);
        boolean removed = basket[index].remove(o);
        if (removed) size--;
        return removed;
    }

    @Override
    public boolean contains(Object o) {
        int index = getIndex(o);
        return basket[index].contains(o);
    }

    class MyLinkedList<E>{
        class Node{
            Node(E value) { this.value = value; }
            E value;
            Node next;
        }

        Node first = null;
        int size = 0;

        MyLinkedList () {}

        boolean contains(Object o) {
            Node current = first;
            while (current != null) {
                if (o == null ? current.value == null : o.equals(current.value)) {
                    return true;
                }
                current = current.next;
            }
            return false;
        }

        boolean add(E e){
            if (contains(e)) return false;
            Node newFirst = new Node(e);
            newFirst.next = first;
            first = newFirst;
            size++;
            return true;
        }

        boolean remove(Object o) {
            Node current = first;
            Node prev = null;
            while (current != null) {
                if (o == null ? current.value == null : o.equals(current.value)) {
                    if (prev == null) { // удаляем первый элемент
                        first = current.next;
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

        void removeAll() {
            Node current = first;
            while (current != null) {
                Node next = current.next;
                current.value = null;
                current.next = null;
                current = next;
            }
            first = null;
            size = 0;
        }

        public Object[] toArray() {
            Object[] array = new Object[size];
            Node current = first;
            int i = 0;
            while (current != null) {
                array[i++] = current.value;
                current = current.next;
            }
            return array;
        }
    }

    //ЗАГЛУШКИ


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
}
