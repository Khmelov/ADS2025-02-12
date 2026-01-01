package by.it.group410901.skachkova.lesson10;

import java.util.Deque;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> { //двусвязный
    private Node<E> first;
    private Node<E> last;
    private int size;

    private static class Node<E>
    {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    public MyLinkedList()
    {
        first = null;
        last = null;
        size = 0;
    }

    @Override
    public String toString()
    {
        if (size == 0)
        {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node<E> current = first;
        while (current != null)
        {
            sb.append(current.item);
            if (current.next != null)
            {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E element)
    {

        addLast(element);
        return true;
    }

    public E remove(int index)
    {
        if (index < 0 || index >= size)
        {
            throw new IndexOutOfBoundsException();
        }

        Node<E> current = first;
        for (int i = 0; i < index; i++)
        {
            current = current.next;
        }

        return unlink(current);
    }

    @Override
    public boolean remove(Object element) {
        if (element == null) {
            for (Node<E> current = first; current != null; current = current.next) {
                if (current.item == null) {
                    unlink(current);
                    return true;
                }
            }
        } else {
            for (Node<E> current = first; current != null; current = current.next) {
                if (element.equals(current.item)) {
                    unlink(current);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public void addFirst(E element)
    {
        if (element == null)
        {
            throw new NullPointerException();
        }

        Node<E> newNode = new Node<>(null, element, first);
        if (first != null) //если список не пустой
        {
            first.prev = newNode; //старый первый теперь второй
        }
        else
        {
            last = newNode;
        }
        first = newNode;
        size++;
    }

    @Override
    public void addLast(E element)
    {
        if (element == null)
        {
            throw new NullPointerException();
        }

        Node<E> newNode = new Node<>(last, element, null);
        if (last != null)
        {
            last.next = newNode; //последний теперь предпоследний
        }
        else
        {
            first = newNode;
        }
        last = newNode;
        size++;
    }

    @Override
    public E element()
    {
        return getFirst();
    }

    @Override
    public E getFirst()
    {
        if (first == null)
        {
            throw new NoSuchElementException();
        }
        return first.item;
    }

    @Override
    public E getLast()
    {
        if (last == null)
        {
            throw new NoSuchElementException();
        }
        return last.item;
    }

    @Override
    public E poll()
    {
        return pollFirst();
    }

    @Override
    public E pollFirst() //удаление первого элемента
    {
        if (first == null)
        {
            return null;
        }
        return unlink(first);
    }

    @Override
    public E pollLast() //удаление последнего
    {
        if (last == null)
        {
            return null;
        }
        return unlink(last);
    }

    private E unlink(Node<E> node) //удаление элемента
    {
        E element = node.item;
        Node<E> next = node.next;
        Node<E> prev = node.prev;

        //обновление ссылок на соседние
        if (prev == null)
        {
            first = next;
        }
        else
        {
            prev.next = next;
            node.prev = null;
        }

        if (next == null)
        {
            last = prev;
        }
        else
        {
            next.prev = prev;
            node.next = null;
        }

        node.item = null;
        size--;
        return element;
    }

    // Остальные методы интерфейса Deque
    @Override public boolean isEmpty() { return size == 0; }
    @Override public boolean offer(E e) { return add(e); }
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E remove() {
        if (first == null) throw new NoSuchElementException();
        return pollFirst();
    }
    @Override public E removeFirst() {
        if (first == null) throw new NoSuchElementException();
        return pollFirst();
    }
    @Override public E removeLast() {
        if (last == null) throw new NoSuchElementException();
        return pollLast();
    }
    @Override public E peek() { return peekFirst(); }
    @Override public E peekFirst() { return first == null ? null : first.item; }
    @Override public E peekLast() { return last == null ? null : last.item; }
    @Override public boolean removeFirstOccurrence(Object o) { return remove(o); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public void push(E e) { addFirst(e); }
    @Override public E pop() { return removeFirst(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}