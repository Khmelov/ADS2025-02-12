package by.it.group410902.bolshakova.lesson10;

import java.util.*;

public class TaskB {

    public class MyLinkedList<E> implements Deque<E> {
        private static class Node<E> {
            E data;
            Node<E> next;
            Node<E> prev;

            Node(E data) {
                this.data = data;
            }
        }

        private Node<E> first;
        private Node<E> last;
        private int size;

        public MyLinkedList() {
            first = null;
            last = null;
            size = 0;
        }

        @Override
        public String toString() {
            if (size == 0) {
                return "[]";
            }
            StringBuilder sb = new StringBuilder("[");
            Node<E> current = first;
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
        public boolean add(E element) {
            addLast(element);
            return true;
        }

        public E remove(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }

            Node<E> current = getNode(index);
            E removedData = current.data;

            if (current.prev != null) {
                current.prev.next = current.next;
            } else {
                first = current.next;
            }

            if (current.next != null) {
                current.next.prev = current.prev;
            } else {
                last = current.prev;
            }

            size--;
            return removedData;
        }

        @Override
        public boolean remove(Object o) {
            Node<E> current = first;
            while (current != null) {
                if ((o == null && current.data == null) ||
                        (o != null && o.equals(current.data))) {
                    if (current.prev != null) {
                        current.prev.next = current.next;
                    } else {
                        first = current.next;
                    }

                    if (current.next != null) {
                        current.next.prev = current.prev;
                    } else {
                        last = current.prev;
                    }

                    size--;
                    return true;
                }
                current = current.next;
            }
            return false;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public void addFirst(E element) {
            Node<E> newNode = new Node<>(element);
            if (first == null) {
                first = newNode;
                last = newNode;
            } else {
                newNode.next = first;
                first.prev = newNode;
                first = newNode;
            }
            size++;
        }

        @Override
        public void addLast(E element) {
            Node<E> newNode = new Node<>(element);
            if (last == null) {
                first = newNode;
                last = newNode;
            } else {
                newNode.prev = last;
                last.next = newNode;
                last = newNode;
            }
            size++;
        }

        @Override
        public E element() {
            return getFirst();
        }

        @Override
        public E getFirst() {
            if (first == null) {
                throw new NoSuchElementException("Deque is empty");
            }
            return first.data;
        }

        @Override
        public E getLast() {
            if (last == null) {
                throw new NoSuchElementException("Deque is empty");
            }
            return last.data;
        }

        @Override
        public E poll() {
            return pollFirst();
        }

        @Override
        public E pollFirst() {
            if (first == null) {
                return null;
            }
            E data = first.data;
            first = first.next;
            if (first != null) {
                first.prev = null;
            } else {
                last = null;
            }
            size--;
            return data;
        }

        @Override
        public E pollLast() {
            if (last == null) {
                return null;
            }
            E data = last.data;
            last = last.prev;
            if (last != null) {
                last.next = null;
            } else {
                first = null;
            }
            size--;
            return data;
        }

        private Node<E> getNode(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }

            Node<E> current;
            if (index < size / 2) {
                current = first;
                for (int i = 0; i < index; i++) {
                    current = current.next;
                }
            } else {
                current = last;
                for (int i = size - 1; i > index; i--) {
                    current = current.prev;
                }
            }
            return current;
        }

        // Остальные методы интерфейса (заглушки)
        @Override public boolean isEmpty() { return size == 0; }
        @Override public boolean offer(E e) { return false; }
        @Override public boolean offerFirst(E e) { return false; }
        @Override public boolean offerLast(E e) { return false; }
        @Override public E remove() { return null; }
        @Override public E removeFirst() { return null; }
        @Override public E removeLast() { return null; }
        @Override public E peek() { return null; }
        @Override public E peekFirst() { return null; }
        @Override public E peekLast() { return null; }
        @Override public boolean contains(Object o) { return false; }
        @Override public Iterator<E> iterator() { return null; }
        @Override public Iterator<E> descendingIterator() { return null; }
        @Override public boolean addAll(Collection<? extends E> c) { return false; }
        @Override public void clear() { }
        @Override public boolean retainAll(Collection<?> c) { return false; }
        @Override public boolean removeAll(Collection<?> c) { return false; }
        @Override public boolean containsAll(Collection<?> c) { return false; }
        @Override public Object[] toArray() { return new Object[0]; }
        @Override public <T> T[] toArray(T[] a) { return null; }
        @Override public void push(E e) { }
        @Override public E pop() { return null; }
        @Override public boolean removeFirstOccurrence(Object o) { return false; }
        @Override public boolean removeLastOccurrence(Object o) { return false; }
    }
}