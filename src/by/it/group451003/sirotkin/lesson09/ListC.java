package by.it.group451003.sirotkin.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private class Node {
        E data;
        Node next;
        Node prev;

        Node(E data) {
            this.data = data;
        }
    }

    private Node head; // Голова
    private Node tail; // Хвост
    private int size = 0; // Размер

    private Node getNode(int index) {
        if (index < (size / 2)) {
            Node x = head;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            Node x = tail;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }

    private E unlink(Node x) {
        final E element = x.data;
        final Node next = x.next;
        final Node prev = x.prev;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.data = null;
        size--;
        return element;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        Node newNode = new Node(e);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) return null;
        return unlink(getNode(index));
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) return;

        if (index == size) {
            add(element);
        } else {
            Node succ = getNode(index);
            Node pred = succ.prev;
            Node newNode = new Node(element);

            newNode.next = succ;
            succ.prev = newNode;
            newNode.prev = pred;

            if (pred == null)
                head = newNode;
            else
                pred.next = newNode;

            size++;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (Node x = head; x != null; x = x.next) {
                if (x.data == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            for (Node x = head; x != null; x = x.next) {
                if (o.equals(x.data)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) return null;
        Node x = getNode(index);
        E oldVal = x.data;
        x.data = element;
        return oldVal;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        for (Node x = head; x != null; ) {
            Node next = x.next;
            x.data = null;
            x.next = null;
            x.prev = null;
            x = next;
        }
        head = tail = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        int index = 0;
        if (o == null) {
            for (Node x = head; x != null; x = x.next) {
                if (x.data == null) return index;
                index++;
            }
        } else {
            for (Node x = head; x != null; x = x.next) {
                if (o.equals(x.data)) return index;
                index++;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) return null;
        return getNode(index).data;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int index = size - 1;
        if (o == null) {
            for (Node x = tail; x != null; x = x.prev) {
                if (x.data == null) return index;
                index--;
            }
        } else {
            for (Node x = tail; x != null; x = x.prev) {
                if (o.equals(x.data)) return index;
                index--;
            }
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null || c.isEmpty()) return false;
        for (E element : c) {
            add(element);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (c == null || c.isEmpty()) return false;
        if (index < 0 || index > size) return false;

        boolean modified = false;

        for (E element : c) {
            add(index++, element);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null || c.isEmpty()) return false;
        boolean modified = false;

        Node current = head;
        while (current != null) {
            Node next = current.next;
            if (c.contains(current.data)) {
                unlink(current);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) return false;
        boolean modified = false;

        Node current = head;
        while (current != null) {
            Node next = current.next;
            if (!c.contains(current.data)) {
                unlink(current);
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
