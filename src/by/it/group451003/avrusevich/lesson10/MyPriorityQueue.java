package by.it.group451003.avrusevich.lesson10;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyPriorityQueue<E> implements Deque<E> {

    private int size = 0;
    private E[] heap = null;

    private void siftDown(int i) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int j = left;
        if (right < size && ((Comparable<? super E>)heap[right]).compareTo(heap[j]) < 0)
            j = right;
        if (j < size && ((Comparable<? super E>)heap[j]).compareTo(heap[i]) < 0) {
            E temp = heap[i];
            heap[i] = heap[j];
            heap[j] = temp;
            siftDown(j);
        }
    }

    private void siftUp(int i) {
        if (((Comparable<? super E>)heap[i]).compareTo(heap[(i - 1) / 2]) < 0) {
            E temp = heap[i];
            heap[i] = heap[(i - 1) / 2];
            heap[(i - 1) / 2] = temp;
            siftUp((i - 1) / 2);
        }
    }
    public String toString() {
        String res = "[";
        for (int i = 0; i < size - 1; i++) {
            res += heap[i] + ", ";
        }
        if (size != 0) {
            res += heap[size - 1];
        }
        res += "]";
        return res;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
        heap = null;
    }

    @Override
    public boolean add(E e) {
        if (e == null) return false;
        size++;
        E[] newheap = (E[]) new Object[size];
        for (int i = 0; i < size - 1; i++) {
            newheap[i] = heap[i];
        }
        newheap[size - 1] = e;
        heap = newheap;
        siftUp(size - 1);
        return true;
    }

    @Override
    public E remove() {
        if (size == 0) return null;
        size--;
        E[] newheap = (E[]) new Object[size];
        newheap[0] = heap[size];
        for (int i = 1; i < size; i++) {
            newheap[i] = heap[i];
        }
        E res = heap[0];
        heap = newheap;
        siftDown(0);
        return res;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (heap[i] == o) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E poll() {
        return remove();
    }

    @Override
    public E peek() {
        if (size == 0) return null;
        return heap[0];
    }

    @Override
    public E element() {
        return peek();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o: c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean res = false;
        for (Object o: c) {
            res = add((E)o);
        }
        return res;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int newSize = 0;
        E[] temp = (E[]) new Object[size];
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                temp[newSize++] = heap[i];
            }
        }
        boolean changed = newSize != size;
        size = newSize;
        E[] newheap = (E[]) new Object[size];
        for (int i = 0; i < size; i++) {
            newheap[i] = temp[i];
        }
        heap = newheap;
        for (int i = size / 2; i >= 0; i--) {
            siftDown(i);
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int newSize = 0;
        E[] temp = (E[]) new Object[size];
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                temp[newSize++] = heap[i];
            }
        }
        boolean changed = newSize != size;
        size = newSize;
        E[] newheap = (E[]) new Object[size];
        for (int i = 0; i < size; i++) {
            newheap[i] = temp[i];
        }
        heap = newheap;
        for (int i = size / 2; i >= 0; i--) {
            siftDown(i);
        }
        return changed;
    }







    //////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////
    @Override
    public void addFirst(E e) {

    }

    @Override
    public void addLast(E e) {

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
    public E removeFirst() {
        return null;
    }

    @Override
    public E removeLast() {
        return null;
    }

    @Override
    public E pollFirst() {
        return null;
    }

    @Override
    public E pollLast() {
        return null;
    }

    @Override
    public E getFirst() {
        return null;
    }

    @Override
    public E getLast() {
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
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override
    public void push(E e) {

    }

    @Override
    public E pop() {
        return null;
    }

    @Override
    public boolean remove(Object o) {
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
    public Iterator<E> descendingIterator() {
        return null;
    }
}