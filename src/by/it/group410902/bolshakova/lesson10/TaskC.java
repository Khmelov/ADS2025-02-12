package by.it.group410902.bolshakova.lesson10;

import java.util.*;

public class TaskC {

    public class MyPriorityQueue<E> implements Queue<E> {
        private E[] elements;
        private int size;
        private final Comparator<? super E> comparator;

        @SuppressWarnings("unchecked")
        public MyPriorityQueue() {
            elements = (E[]) new Object[16];
            size = 0;
            comparator = null;
        }

        @SuppressWarnings("unchecked")
        public MyPriorityQueue(Comparator<? super E> comparator) {
            elements = (E[]) new Object[16];
            size = 0;
            this.comparator = comparator;
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

        @Override
        public void clear() {
            for (int i = 0; i < size; i++) {
                elements[i] = null;
            }
            size = 0;
        }

        @Override
        public boolean add(E element) {
            return offer(element);
        }

        @Override
        public E remove() {
            if (size == 0) {
                throw new NoSuchElementException("Queue is empty");
            }
            return poll();
        }

        @Override
        public boolean contains(Object o) {
            for (int i = 0; i < size; i++) {
                if ((o == null && elements[i] == null) ||
                        (o != null && o.equals(elements[i]))) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean offer(E element) {
            if (element == null) {
                throw new NullPointerException();
            }
            ensureCapacity();
            elements[size] = element;
            siftUp(size);
            size++;
            return true;
        }

        @Override
        public E poll() {
            if (size == 0) {
                return null;
            }
            E result = elements[0];
            elements[0] = elements[size - 1];
            elements[size - 1] = null;
            size--;
            if (size > 0) {
                siftDown(0);
            }
            return result;
        }

        @Override
        public E peek() {
            return (size == 0) ? null : elements[0];
        }

        @Override
        public E element() {
            if (size == 0) {
                throw new NoSuchElementException("Queue is empty");
            }
            return elements[0];
        }

        @Override
        public boolean isEmpty() {
            return size == 0;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            for (Object element : c) {
                if (!contains(element)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            boolean modified = false;
            for (E element : c) {
                if (add(element)) {
                    modified = true;
                }
            }
            return modified;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            boolean modified = false;
            for (int i = size - 1; i >= 0; i--) {
                if (c.contains(elements[i])) {
                    removeAt(i);
                    modified = true;
                }
            }
            return modified;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            boolean modified = false;
            for (int i = size - 1; i >= 0; i--) {
                if (!c.contains(elements[i])) {
                    removeAt(i);
                    modified = true;
                }
            }
            return modified;
        }

        // Вспомогательные методы для кучи
        @SuppressWarnings("unchecked")
        private void ensureCapacity() {
            if (size == elements.length) {
                E[] newElements = (E[]) new Object[elements.length * 2];
                System.arraycopy(elements, 0, newElements, 0, size);
                elements = newElements;
            }
        }

        @SuppressWarnings("unchecked")
        private int compare(E a, E b) {
            if (comparator != null) {
                return comparator.compare(a, b);
            } else {
                return ((Comparable<? super E>) a).compareTo(b);
            }
        }

        private void siftUp(int index) {
            E element = elements[index];
            while (index > 0) {
                int parent = (index - 1) >>> 1;
                E parentElement = elements[parent];
                if (compare(element, parentElement) >= 0) {
                    break;
                }
                elements[index] = parentElement;
                index = parent;
            }
            elements[index] = element;
        }

        private void siftDown(int index) {
            E element = elements[index];
            int half = size >>> 1;
            while (index < half) {
                int child = (index << 1) + 1;
                E childElement = elements[child];
                int right = child + 1;
                if (right < size && compare(childElement, elements[right]) > 0) {
                    child = right;
                    childElement = elements[right];
                }
                if (compare(element, childElement) <= 0) {
                    break;
                }
                elements[index] = childElement;
                index = child;
            }
            elements[index] = element;
        }

        private void removeAt(int index) {
            elements[index] = elements[size - 1];
            elements[size - 1] = null;
            size--;
            if (index < size) {
                siftDown(index);
                if (elements[index] == elements[size]) {
                    siftUp(index);
                }
            }
        }

        // Остальные методы интерфейса (заглушки)
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
        public boolean remove(Object o) {
            return false;
        }
    }
}