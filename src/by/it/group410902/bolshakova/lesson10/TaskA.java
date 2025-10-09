package by.it.group410902.bolshakova.lesson10;

import java.util.*;

public class TaskA {

    public class MyArrayDeque<E> implements Deque<E> {
        private E[] elements;
        private int size;

        public MyArrayDeque() {
            elements = (E[]) new Object[8];
            size = 0;
        }

        @Override
        public String toString() {
            if (size == 0) {
                return "[]";
            }
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < size; i++) {
                sb.append(elements[i]);
                if (i < size - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]");
            return sb.toString();
        }

        @Override
        public int size() {
            return size;
        }

        private void ensureCapacity() {
            if (size == elements.length) {
                E[] newElements = (E[]) new Object[elements.length * 2];
                for (int i = 0; i < size; i++) {
                    newElements[i] = elements[i];
                }
                elements = newElements;
            }
        }

        @Override
        public boolean add(E element) {
            ensureCapacity();
            elements[size] = element;
            size++;
            return true;
        }

        @Override
        public void addFirst(E element) {
            ensureCapacity();
            for (int i = size; i > 0; i--) {
                elements[i] = elements[i - 1];
            }
            elements[0] = element;
            size++;
        }

        @Override
        public void addLast(E element) {
            add(element);
        }

        @Override
        public E element() {
            return getFirst();
        }

        @Override
        public E getFirst() {
            if (size == 0) {
                throw new NoSuchElementException("Deque is empty");
            }
            return elements[0];
        }

        @Override
        public E getLast() {
            if (size == 0) {
                throw new NoSuchElementException("Deque is empty");
            }
            return elements[size - 1];
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
            E firstElement = elements[0];
            for (int i = 0; i < size - 1; i++) {
                elements[i] = elements[i + 1];
            }
            elements[size - 1] = null;
            size--;
            return firstElement;
        }

        @Override
        public E pollLast() {
            if (size == 0) {
                return null;
            }
            E lastElement = elements[size - 1];
            elements[size - 1] = null;
            size--;
            return lastElement;
        }

        // Остальные методы интерфейса (заглушки)
        @Override
        public boolean isEmpty() {
            return size == 0;
        }

        @Override
        public boolean offer(E e) {
            return false;
        }

        @Override
        public boolean offerFirst(E e) {
            return false;
        }

        @Override
        public boolean offerLast(E e) {
            return false;
        }

        @Override
        public E remove() {
            return null;
        }

        @Override
        public E removeFirst() {
            return null;
        }

        @Override
        public E removeLast() {
            return null;
        }

        @Override
        public E peek() {
            return null;
        }

        @Override
        public E peekFirst() {
            return null;
        }

        @Override
        public E peekLast() {
            return null;
        }

        @Override
        public boolean remove(Object o) {
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
        public Iterator<E> descendingIterator() {
            return null;
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            return false;
        }

        @Override
        public void clear() {
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
        public boolean containsAll(Collection<?> c) {
            return false;
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
        public void push(E e) {
        }

        @Override
        public E pop() {
            return null;
        }

        @Override
        public boolean removeFirstOccurrence(Object o) {
            return false;
        }

        @Override
        public boolean removeLastOccurrence(Object o) {
            return false;
        }
    }
}