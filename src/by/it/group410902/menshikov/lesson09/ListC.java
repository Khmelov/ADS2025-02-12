package by.it.group410902.menshikov.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    private class Node {
        E value;
        Node next;
        Node prev;
        Node(E value) {
            this.value = value;
        }
    }
    private Node head;
    private Node tail;
    private int size = 0;
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Node current = head;
        while (current != null) {
            sb.append(current.value);
            if (current.next != null) sb.append(", ");
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        Node newNode = new Node(e);
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
        return true;
    }

    private Node getNode(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index);
        Node current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) current = current.next;
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) current = current.prev;
        }
        return current;
    }
    private void removeNode(Node node) {
        Node prevNode = node.prev;
        Node nextNode = node.next;
        if (prevNode != null) {
            prevNode.next = nextNode;
        } else {
            head = nextNode;
        }
        if (nextNode != null) {
            nextNode.prev = prevNode;
        } else {
            tail = prevNode;
        }
        size--;
    }

    @Override
    public E remove(int index) {
        Node toRemove = getNode(index);
        E val = toRemove.value;
        Node prevNode = toRemove.prev;
        Node nextNode = toRemove.next;

        if (prevNode != null) prevNode.next = nextNode;
        else head = nextNode;

        if (nextNode != null) nextNode.prev = prevNode;
        else tail = prevNode;
        size--;
        return val;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index);
        if (index == size) {
            add(element);
            return;
        }
        Node nextNode = getNode(index);
        Node prevNode = nextNode.prev;
        Node newNode = new Node(element);
        newNode.next = nextNode;
        newNode.prev = prevNode;
        nextNode.prev = newNode;
        if (prevNode != null) prevNode.next = newNode;
        else head = newNode;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        Node current = head;
        while (current != null) {
            if ((o == null && current.value == null) || (o != null && o.equals(current.value))) {
                Node prevNode = current.prev;
                Node nextNode = current.next;
                if (prevNode != null) prevNode.next = nextNode;
                else head = nextNode;
                if (nextNode != null) nextNode.prev = prevNode;
                else tail = prevNode;
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        Node node = getNode(index);
        E oldVal = node.value;
        node.value = element;
        return oldVal;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        head = tail = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        int index = 0;
        Node current = head;
        while (current != null) {
            if ((o == null && current.value == null) || (o != null && o.equals(current.value))) return index;
            current = current.next;
            index++;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        return getNode(index).value;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int index = size - 1;
        Node current = tail;
        while (current != null) {
            if ((o == null && current.value == null) || (o != null && o.equals(current.value))) return index;
            current = current.prev;
            index--;
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index);
        if (c.isEmpty()) return false;
        boolean modified = false;
        int currentIndex = index;
        for (E e : c) {
            add(currentIndex++, e);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        Node current = head;
        while (current != null) {
            Node next = current.next;
            if (c.contains(current.value)) {
                removeNode(current);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node current = head;
        while (current != null) {
            Node next = current.next;
            if (!c.contains(current.value)) {
                removeNode(current);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return null;
    }

}
