package lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private MyNode<E>[] elements = new MyNode[20];
    int size = 0;
    int capacity = 20;
    private MyNode<E> orderHead = null;
    private MyNode<E> orderTail = null;

    private class MyNode<E>{
        MyNode<E> next = null;
        E element;
        int hash;
        MyNode<E> orderNext = null;
        MyNode<E> orderPrev = null;
        public MyNode(E element) {
            this.element = element;
            this.hash = element.hashCode();
        }
    }

    private int getIndex(int hash)
    {
        return hash & capacity - 1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        MyNode<E> pointer = orderHead;
        if (pointer != null) {
            sb.append(pointer.element);
            pointer = pointer.orderNext;
        }
        while (pointer != null) {
            sb.append(", ");
            sb.append(pointer.element);
            pointer = pointer.orderNext;
        }
        return sb.append("]").toString();
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

    private class MyIterator implements Iterator<E> {

        MyNode<E> cursor = orderHead;

        @Override
        public boolean hasNext() {
            return cursor != null;
        }

        @Override
        public E next() {
            MyNode<E> pointer = cursor;
            cursor = cursor.orderNext;
            return pointer.element;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new MyIterator();
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    public boolean add(E e) {
        MyNode<E> newNode = new MyNode<>(e);
        MyNode<E> pointer;
        int index = getIndex(newNode.hash);
        if(elements[index] == null) {
            elements[index] = newNode;
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
        }
        size++;
        if(orderHead == null) {
            orderHead = newNode;
        }
        else {
            newNode.orderPrev = orderTail;
            orderTail.orderNext = newNode;;
        }
        orderTail = newNode;
        return true;
    }


    @Override
    public boolean remove(Object o) {
        int index = getIndex(o.hashCode());
        MyNode<E> pointer = elements[index];
        if(pointer != null) {
            if(Objects.equals(elements[index].element, o)) {
                if(pointer.orderNext != null)
                    pointer.orderNext.orderPrev = pointer.orderPrev;
                else
                    orderTail = pointer.orderPrev;
                if(pointer.orderPrev != null)
                    pointer.orderPrev.orderNext = pointer.orderNext;
                else
                    orderHead = pointer.orderNext;
                elements[index] = elements[index].next;
                size--;
                return true;
            }
            while(pointer.next != null && !Objects.equals(pointer.next.element, o))
            {
                pointer = pointer.next;
            }
            if(pointer.next != null) {
                if(pointer.next.orderNext != null)
                    pointer.next.orderNext.orderPrev = pointer.next.orderPrev;
                else
                    orderTail = pointer.next.orderPrev;
                if(pointer.next.orderPrev != null)
                    pointer.next.orderPrev.orderNext = pointer.next.orderNext;
                else
                    orderHead = pointer.next.orderNext;
                pointer.next = pointer.next.next;
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for(Object o : c) {
            if(!contains(o))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for(E e : c) {
           changed |= add(e);
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (E e : this) {
            if (!c.contains(e)) {
                changed |= remove(e);
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for(Object o : c) {
            changed |= remove(o);
        }
        return changed;
    }

    @Override
    public void clear() {
        elements = new MyNode[20];
        size = 0;
        orderHead = null;
        orderTail = null;
    }
}
