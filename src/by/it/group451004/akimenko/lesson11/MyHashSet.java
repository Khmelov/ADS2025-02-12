package lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {
    MyNode<E>[] elements = new MyNode[20];
    int size = 0;
    int capacity = 20;
    @Override
    public int size() {
        return size;
    }
    private int getIndex(int hash) {
        return hash & (capacity-1);
    }
    private class MyNode<E>{
        MyNode<E> next = null;
        E element;
        int hash;
        public MyNode(E element) {
            this.element = element;
            this.hash = element.hashCode();
        }
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        int index = getIndex(o.hashCode());
        MyNode<E> pointer;
        if(elements[index] != null) {
            if(Objects.equals(elements[index].element, o)) {
                return true;
            }
            pointer = elements[index];
            while(pointer.next != null && !Objects.equals(pointer.next.element, o))
            {
                pointer = pointer.next;
            }
            if(pointer.next != null) {
                return true;
            }
        }
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

    @Override
    public boolean add(E e) {
        MyNode<E> newNode = new MyNode<>(e);
        MyNode<E> pointer;
        int index = getIndex(newNode.hash);
        if(elements[index] == null) {
            elements[index] = newNode;
            size++;
            return true;
        }
        else
        {
            pointer = elements[index];
            while(pointer.next != null) {
                if(Objects.equals(e, pointer.element)) {
                    return false;
                }
                pointer = pointer.next;
            }
            if(Objects.equals(e, pointer.element)) {
                return false;
            }
            pointer.next = newNode;
            size++;
            return true;
        }
    }

    @Override
    public boolean remove(Object o) {
        int index = getIndex(o.hashCode());
        MyNode<E> pointer;
        if(elements[index] != null) {
            if(Objects.equals(elements[index].element, o)) {
                elements[index] = elements[index].next;
                size--;
                return true;
            }
            pointer = elements[index];
            while(pointer.next != null && !Objects.equals(pointer.next.element, o))
            {
                pointer = pointer.next;
            }
            if(pointer.next != null) {
                pointer.next = pointer.next.next;
                size--;
                return true;
            }
        }
        return false;
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
    public String toString()
    {
        StringBuilder str = new StringBuilder("[");
        MyNode<E> pointer = elements[0];
        int i = 0;
        while( i < capacity && pointer == null) {pointer = elements[i++];}
        if(i < capacity)
        {
            str.append(pointer.element.toString());
            while(pointer.next != null)
            {
                str.append(", ");
                pointer = pointer.next;
                str.append(pointer.element.toString());
            }
        }
        i++;
        while (i < capacity)
        {
            pointer = elements[i++];
            if(pointer != null) {
                str.append(", ");
                str.append(pointer.element.toString());
                while (pointer.next != null) {
                    str.append(", ");
                    pointer = pointer.next;
                    str.append(pointer.element.toString());
                }
            }
        }
        return str.append("]").toString();
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
    public void clear() {
        elements = new MyNode[capacity];
        size = 0;
    }
}
