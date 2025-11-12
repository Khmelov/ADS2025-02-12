//Создайте class MyLinkedList<E>, который реализует интерфейс Deque<E>
//    и работает на основе двунаправленного связного списка
//    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

package by.it.group410902.siomchena.lesson10;

import java.util.*;

public class MyLinkedList<E> implements Deque<E>{
    private static class Node<E>{
        E data;
        Node<E> next;
        Node<E> prev;

        Node(E data, Node<E> next, Node<E> prev) {
            this.data=data;
            this.next=next;
            this.prev=prev;
        }
    }

    private int size;
    private Node<E> first;
    private Node<E> last;

    public MyLinkedList() {
        size = 0;
        first = null;
        last = null;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node<E> current = first;
        while (current!=null) {
            sb.append(current.data);
            if (current.next!=null) {
                sb.append(", ");
            }
            current=current.next;
        }
        sb.append(']');
        return sb.toString();
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
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void addFirst(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        Node<E> newNode = new Node<>(e, first, null);
        if (first!=null) first.prev=newNode;
        else last=newNode;
        first=newNode;
        size++;
    }

    @Override
    public void addLast(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        Node<E> newNode = new Node<>(e, null, last);
        if (last!=null) last.next=newNode;
        else first=newNode;
        last=newNode;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        return first.data;
    }

    @Override
    public E getLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }
        return last.data;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }
        E element = first.data;
        removeNode(first);
        return element;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        E element = last.data;
        removeNode(last);
        return element;
    }

    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return pollFirst();
    }

    @Override
    public E removeLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return pollLast();
    }

    @Override
    public E peekFirst() {
        if (size == 0) {
            return null;
        }
        return first.data;
    }

    @Override
    public E peekLast() {
        if (size == 0) {
            return null;
        }
        return last.data;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node<E> x = first;
        for (int i = 0; i < index; i++) {
            x = x.next;
        }

        E result = x.data;
        removeNode(x);
        return result;
    }

    @Override
    public E remove(){
        return removeFirst();
    }

    @Override
    public boolean remove(Object e) {
        if (e == null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.data == null) {
                    removeNode(x);
                    return true;
                }
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (e.equals(x.data)) {
                    removeNode(x);
                    return true;
                }
            }
        }
        return false;
    }

    private void removeNode(Node<E> x) {
        Node<E> prev = x.prev;
        Node<E> next = x.next;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
        }
        x.next = null;
        x.prev = null;
        size--;
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public boolean contains(Object o) {
        Node<E> current = first;
        while (current!=null) {
            if (current.data==o) {
                return true;
            }
            current=current.next;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}
