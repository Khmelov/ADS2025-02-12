package by.it.group451002.buyel.lesson10;

import java.util.*;

public class MyLinkedList<E> implements List<E> {

    transient int size;
    transient Node<E> start;
    transient Node<E> end;

    public MyLinkedList() {
        start = end = null;
        size = 0;
    }

//    ===========================
//    toString() -
//    add(E element) -
//    remove(int) -
//    remove(E element) -
//    size() -
//
//    addFirst(E element) -
//    addLast(E element) -
//
//    element() -
//    getFirst() -
//    getLast() -
//
//    poll() -
//    pollFirst() -
//    pollLast() -
//    ===========================

    @Override
    public void addFirst(E e) {
        size++;
        if (null == start) {
            start = end = new Node<>(e);
        } else {
            start.prev = new Node<>(e);
            Node<E> tempNode = start;
            start = start.prev;
            start.next = tempNode;
        }
    }

    @Override
    public void addLast(E e) {
        size++;
        if (null == end) {
            start = end = new Node<>(e);
        } else {
            Node <E> tempNode = end.next = new Node<>(e);
            tempNode.prev = end;
            end = end.next;
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        Node<E> tempNode = start;

        while (null != tempNode) {
            result.append(tempNode.data);
            if (null != tempNode.next) {
                result.append(", ");
            }
            tempNode = tempNode.next;
        }

        result.append("]");
        return result.toString();
    }

    public E element() {return start.data;}

    public E getFirst() {return element();}

    public E getLast() {return end.data;}

    public E poll() {
        if (0 == size) {
            return null;
        }

        Node<E> tempNode = start;
        start = start.next;
        size--;

        if (null == start) {
            end = start;
            return tempNode.data;
        }

        start.prev = null;
        tempNode.next = null;
        return tempNode.data;
    }

    public E pollFirst() {return poll();}

    public E pollLast() {
        if (0 == size) {
            return null;
        }

        Node<E> tempNode = end;
        end = end.prev;
        size--;

        if (null == end) {
            start = end;
            return tempNode.data;
        }

        end.next = null;
        tempNode.prev = null;
        return tempNode.data;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
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
        if (0 == size) {
            start = end = new Node<>(e);
            size++;
            return true;
        }
        size++;
        end.next = new Node<>(e);
        end.next.prev = end;
        end = end.next;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        Node<E> nodeFromStart = start;
        Node<E> nodeFromEnd = end;
        boolean found = false;

        if (null == o) {
            return found;
        }

//        while (!o.equals(nodeFromStart.data) && !o.equals(nodeFromEnd.data)
//                && nodeFromStart != end && nodeFromEnd != start) {
        while (!o.equals(nodeFromStart.data) && nodeFromStart != end) {
            nodeFromStart = nodeFromStart.next;
        }

        if (o.equals(nodeFromStart.data)) {
            found = true;
            size--;
            Node<E> tempNode = nodeFromStart;
            nodeFromStart = nodeFromStart.next;

            nodeFromStart.prev = tempNode.prev;
            if (null != tempNode.prev) {
                tempNode.prev.next = nodeFromStart;
            } else {
                start = nodeFromStart;
            }

            tempNode.next = null;
            tempNode.prev = null;
        }
//        else if (o.equals(nodeFromEnd.data)) {
//            found = true;
//            size--;
//            Node<E> tempNode = nodeFromEnd;
//            nodeFromEnd = nodeFromEnd.prev;
//
//            nodeFromEnd.next = tempNode.next;
//            if (null != tempNode.next) {
//                tempNode.next.prev = nodeFromEnd;
//            } else {
//                end = nodeFromEnd;
//            }
//
//            tempNode.next = null;
//            tempNode.prev = null;
//        }

        return found;
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
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public E set(int index, E element) {
        return null;
    }

    @Override
    public void add(int index, E element) {

    }

    @Override
    public E remove(int index) {
        Node<E> tempNode;

        if (index > size || 0 == size) {
            return null;
        }

        if (size / 2 > index) {
            tempNode = start;
            for (int i = 0; i < index; i++) {
                tempNode = tempNode.next;
            }

            Node<E> tempNodePrev = tempNode.prev;
            Node<E> tempNodeNext = tempNode.next;

            if (start == tempNode) {
                start = start.next;
                if (null == start) {
                    end = start;
                }
            }

            if (null != tempNodePrev) {
                tempNodePrev.next = tempNodeNext;
            }
            if (null != tempNodeNext) {
                tempNodeNext.prev = tempNodePrev;
            }
            tempNode.next = null;
            tempNode.prev = null;
            size--;
        } else {
            tempNode = end;
            for (int i = size-1; i > index; i--) {
                tempNode = tempNode.prev;
            }

            Node<E> tempNodePrev = tempNode.prev;
            Node<E> tempNodeNext = tempNode.next;

            if (end == tempNode) {
                end = end.prev;
                if (null == end) {
                    start = end;
                }
            }

            if (null != tempNodePrev) {
                tempNodePrev.next = tempNodeNext;
            }
            if (null != tempNodeNext) {
                tempNodeNext.prev = tempNodePrev;
            }
            tempNode.next = null;
            tempNode.prev = null;
            size--;
        }
        return tempNode.data;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return List.of();
    }
}
