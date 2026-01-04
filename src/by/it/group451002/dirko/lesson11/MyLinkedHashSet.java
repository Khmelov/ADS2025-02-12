package by.it.group451002.dirko.lesson11;

import java.util.*;

public class MyLinkedHashSet<E> implements Set<E> {
    private Node MyList;
    private Node[] elements = new Node[10000];
    private int size;

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node tempNode = MyList;
        while (tempNode != null) {
            sb.append(tempNode.item).append(", ");
            tempNode = tempNode.next;
        }
        if (sb.length() > 1) sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        return sb.toString();
    }

    private int getHash(Object elem){
        return (int) elem % elements.length;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean contains(Object o) {
        int ind = getHash(o);
        Node elem = elements[ind];
        while (elem != null) {
            if (Objects.equals(elem.item, o)) return true;
            elem = elem.next;
        }
        return false;
    }

    private void addToList(E e) {
        if (MyList == null) {
            MyList = new Node(e, null);
            return;
        }
        Node tempNode = MyList;
        while (tempNode.next != null) tempNode = tempNode.next;
        tempNode.next = new Node(e, null);
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) return false;
        size++;
        addToList(e);
        int ind = getHash(e);
        Node elem = elements[ind];
        if (elem == null) {
            elements[ind] = new Node(e, null);
            return true;
        }
        while (elem.next != null) { elem = elem.next; }
        elem.next = new Node(e, null);
        return true;
    }

    private void removeFromList(Object o)
    {
        if (Objects.equals(MyList.item, o)) {
            MyList = MyList.next;
            return;
        }
        Node tempNode = MyList;
        while (!Objects.equals(tempNode.next.item, o))
            tempNode = tempNode.next;
        tempNode.next = tempNode.next.next;
    }

    @Override
    public boolean remove(Object o) {
        if (!contains(o)) return false;
        size--;
        removeFromList(o);
        int ind = getHash(o);
        Node elem = elements[ind];
        if (Objects.equals(elem.item, o)) {
            elements[ind] = elements[ind].next;
            return true;
        }
        while (!Objects.equals(elem.next.item, o)) elem = elem.next;
        elem.next = elem.next.next;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        int oldSize = size;
        for (E e : c) add(e);
        return oldSize != size;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int oldSize = size;
        Node tempNode = MyList;
        while (tempNode != null) {
            if (!c.contains(tempNode.item))
            {
                Node nextNode = tempNode.next;
                remove(tempNode.item);
                tempNode = nextNode;
            }
            else tempNode = tempNode.next;
        }
        return oldSize != size;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int oldSize = size;
        for (Object o: c) remove(o);
        return oldSize != size;
    }

    @Override
    public void clear() {
        MyList = null;
        elements = new Node[10000];
        size = 0;
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
