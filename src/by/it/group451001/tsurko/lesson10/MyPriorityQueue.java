package by.it.group451001.tsurko.lesson10;

import java.util.Collection;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Queue;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E>, Iterable<E> {
    private E[] heap;
    private int currentSize;
    private static final int CAPACITY = 10;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = (E[]) new Comparable[CAPACITY];
        currentSize = 0;
    }

    private void ensureCapacity() {
        if (currentSize == heap.length) {
            heap = Arrays.copyOf(heap, heap.length * 2);
        }
    }

    private void siftUp(int index) {
        E x = heap[index];
        while (index > 0 && x.compareTo(heap[(index - 1) / 2]) < 0) {
            heap[index] = heap[(index - 1) / 2];
            index = (index - 1) / 2;
        }
        heap[index] = x;
    }

    private void siftDown(int index) {
        E x = heap[index];
        int midl = currentSize / 2;
        while (index < midl) {
            int child = 2 * index + 1;
            E minChild = heap[child];
            int right = child + 1;
            if (right < currentSize && minChild.compareTo(heap[right]) > 0) {
                child = right;
                minChild = heap[right];
            }
            if (x.compareTo(minChild) <= 0) {
                break;
            }
            heap[index] = minChild;
            index = child;
        }
        heap[index] = x;
    }

    public boolean add(E e) {
        ensureCapacity();
        heap[currentSize] = e;
        siftUp(currentSize);
        currentSize++;
        return true;
    }

    public E remove() {
        if (currentSize == 0) {
            throw new IllegalStateException("Queue is empty");
        }
        E info = heap[0];
        currentSize--;
        heap[0] = heap[currentSize];
        heap[currentSize] = null;
        siftDown(0);
        return info;
    }

    @Override
    public boolean contains(Object obj) {
        for (int i = 0; i < currentSize; i++) {
            if (heap[i].equals(obj)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOf(heap, currentSize));
    }

    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public void clear() {
        Arrays.fill(heap, null);
        currentSize = 0;
    }

    @Override
    public boolean isEmpty() {
        return currentSize == 0;
    }

    public boolean offer(E element) {
        return add(element);
    }

    public E poll() {
        return isEmpty() ? null : remove();
    }

    public E peek() {
        return isEmpty() ? null : heap[0];
    }

    public E element() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return heap[0];
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object obj : collection) {
            if (!contains(obj)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        boolean isCorrect = false;
        for (E element : collection) {
            isCorrect |= add(element);
        }
        return isCorrect;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean isCorrect = false;
        E[] temp = (E[]) new Comparable[currentSize];
        int newSize = 0;

        for (int i = 0; i < currentSize; i++) {
            if (!collection.contains(heap[i])) {
                temp[newSize] = heap[i];
                newSize++;
            } else {
                isCorrect = true;
            }
        }

        heap = temp;
        currentSize = newSize;

        for (int i = (currentSize / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }

        return isCorrect;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean isCorrect = false;
        E[] temp = (E[]) new Comparable[currentSize];
        int newSize = 0;

        for (int i = 0; i < currentSize; i++) {
            if (collection.contains(heap[i])) {
                temp[newSize] = heap[i];
                newSize++;
            } else {
                isCorrect = true;
            }
        }

        heap = temp;
        currentSize = newSize;

        for (int i = (currentSize / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }

        return isCorrect;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;
            private boolean isRemove = false;

            @Override
            public boolean hasNext() {
                return currentIndex < currentSize;
            }

            @Override
            public E next() {
                isRemove = true;
                return heap[currentIndex++];
            }

            @Override
            public void remove() {
                if (!isRemove) {
                    throw new IllegalStateException("You must call next() before calling remove()");
                }
                isRemove = false;
                MyPriorityQueue.this.remove(heap[currentIndex - 1]);
            }
        };
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(heap, currentSize);
    }

    @Override
    public <T> T[] toArray(T[] arr) {
        if (arr.length < currentSize) {
            return (T[]) Arrays.copyOf(heap, currentSize, arr.getClass());
        }
        System.arraycopy(heap, 0, arr, 0, currentSize);
        if (arr.length > currentSize) {
            arr[currentSize] = null;
        }
        return arr;
    }

    @Override
    public boolean remove(Object obj) {
        for (int i = 0; i < currentSize; i++) {
            if (obj.equals(heap[i])) {
                currentSize--;
                heap[i] = heap[currentSize];
                heap[currentSize] = null;
                siftDown(i);
                return true;
            }
        }
        return false;
    }
}