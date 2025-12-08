package by.it.group451004.levkovich.lesson10;
import java.util.*;

public class MyPriorityQueue<E> implements Queue<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] heap;
    private int size;
    private final Comparator<? super E> comparator;

    //конструкторы
    public MyPriorityQueue() {
        this(DEFAULT_CAPACITY, null);
    }

    public MyPriorityQueue(int initialCapacity) {
        this(initialCapacity, null);
    }

    public MyPriorityQueue(Comparator<? super E> comparator) {
        this(DEFAULT_CAPACITY, comparator);
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
        if (initialCapacity < 1)
            throw new IllegalArgumentException();
        this.heap = new Object[initialCapacity];
        this.size = 0;
        this.comparator = comparator;
    }

    //вспомогательные для работы с кучей
    private int parent(int index) { return (index - 1) / 2; }
    private int leftChild(int index) { return 2 * index + 1; }
    private int rightChild(int index) { return 2 * index + 2; }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        } else {
            return ((Comparable<? super E>) a).compareTo(b);
        }
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parent = parent(index);
            if (compare((E) heap[index], (E) heap[parent]) >= 0) {
                break;
            }
            swap(index, parent);
            index = parent;
        }
    }

    private void siftDown(int index) {
        int smallest = index;
        int left = leftChild(index);
        int right = rightChild(index);

        if (left < size && compare((E) heap[left], (E) heap[smallest]) < 0) {
            smallest = left;
        }
        if (right < size && compare((E) heap[right], (E) heap[smallest]) < 0) {
            smallest = right;
        }
        if (smallest != index) {
            swap(index, smallest);
            siftDown(smallest);
        }
    }

    private void swap(int i, int j) {
        Object temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    private void ensureCapacity() {
        if (size == heap.length) {
            heap = Arrays.copyOf(heap, heap.length * 2);
        }
    }

    //методы по условию
    @Override
    public String toString() {
        if (size == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) sb.append(", ");
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
            heap[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public boolean remove(Object element) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(heap[i], element)) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E element) {
        if (element == null)
            throw new NullPointerException();

        ensureCapacity();
        heap[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E poll() {
        if (size == 0) return null;

        E result = (E) heap[0]; // сохранить корень
        heap[0] = heap[size - 1]; // заменить на последний
        heap[size - 1] = null; //
        size--;
        if (size > 0) {
            siftDown(0); //
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E peek() {
        return (size == 0) ? null : (E) heap[0];
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return peek();
    }

    @Override
    public E remove() {
        if (size == 0) throw new NoSuchElementException();
        return poll();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object element) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(heap[i], element)) {
                return true;
            }
        }
        return false;
    }

    //
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
            if (offer(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean removeAll(Collection<?> c) {
        if (c.isEmpty() || size == 0) {
            return false;
        }

        //
        Object[] newHeap = new Object[heap.length];
        int newSize = 0;

        // которых нет в коллекции
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                newHeap[newSize++] = heap[i];
            }
        }

        if (newSize == size) {
            return false;
        }

        this.heap = newHeap;
        this.size = newSize;

        buildHeap();
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean retainAll(Collection<?> c) {
        if (size == 0) {
            return false;
        }

        Object[] newHeap = new Object[heap.length];
        int newSize = 0;

        // те которые есть в коллекции
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                newHeap[newSize++] = heap[i];
            }
        }

        if (newSize == size) {
            return false;
        }

        this.heap = newHeap;
        this.size = newSize;

        buildHeap();
        return true;
    }

    private void buildHeap() {
        for (int i = parent(size - 1); i >= 0; i--) {
            siftDown(i);
        }
    }

    private void removeAt(int index) {
        if (index < 0 || index >= size) return;

        heap[index] = heap[size - 1]; // замена на последний
        heap[size - 1] = null;
        size--;

        if (index < size) {
            siftDown(index);
            siftUp(index);
        }
    }

    // нереализованные
    @Override
    public Iterator<E> iterator() {
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