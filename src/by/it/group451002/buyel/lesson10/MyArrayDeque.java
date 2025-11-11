package by.it.group451002.buyel.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.Arrays;

public class MyArrayDeque<E> implements Deque<E> {
    transient E[] elements;
    transient int head;
    transient int tail;
    final int MAX_CAPACITY_VALUE = Integer.MAX_VALUE - 8;

    public MyArrayDeque() {elements = (E[]) new Object[16 + 1];}

//    ------------------------------
//    toString()
//    size() -
//
//    add(E element) -
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

    private int toMaxCapacity(int needed, int jump) {
        int minCapacity = elements.length + needed;
        if (minCapacity - MAX_CAPACITY_VALUE > 0) {
            if (minCapacity < 0) {
                throw new IllegalStateException("Ooops, something went wrong");
            }
            return MAX_CAPACITY_VALUE;
        }
        if (needed > jump) {
            return minCapacity;
        }
        return (elements.length + jump - MAX_CAPACITY_VALUE < 0)
                ? (elements.length + jump)
                : MAX_CAPACITY_VALUE;
    }

    // Output:
    // | null ... null | head | e | e ... e | e | tail | null ... null |
    // ... or
    // | e | e | e | tail | null ... null | head | e | e | e | e |
    private void grow(int needed) {
        final int oldCapacity = elements.length;

        // Change the capacity
        int jump = (oldCapacity < 64) ? (oldCapacity + 2) : (oldCapacity >> 1);
        int newCapacity = oldCapacity + jump;
        if (needed > jump
                || newCapacity - MAX_CAPACITY_VALUE > 0) {
            // Choosing the best capacity (which allow VM)
            newCapacity = toMaxCapacity(needed, jump);
        }

        // If we have this situation:
        // | e | e | e | tail | null ... null | head | e | e | e | e | null ... null |
        // ... or this:
        // | e | e | e | tail/head | e | e | e | null ... null |
        final E[] es = elements = Arrays.copyOf(elements, newCapacity);
        if (tail < head || (tail == head && es[head] != null)) {
            int freeCapacity = newCapacity - oldCapacity;

            System.arraycopy(es, head,
                             es, head + freeCapacity,
                             oldCapacity - head);

            head += freeCapacity;
            for (int i = head-freeCapacity; i < head; i++) {
                es[i] = null;
            }
        }
    }

    private int add(int index, int jump) {
        return (index + jump) % elements.length;
    }

    private int sub(int index, int jump) {
        return (index - jump + elements.length) % elements.length;
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");

        if (tail < head) {
            for (int i = head; i < elements.length; i++) {
                result.append(elements[i]);
                if (i < elements.length - ((tail == 0) ? 1 : 0)) {
                    result.append(", ");
                }
            }
            for (int i = 0; i < tail; i++) {
                result.append(elements[i]);
                if (i < tail-1) result.append(", ");
            }
        } else {
            for (int i = head; i < tail; i++) {
                result.append(elements[i]);
                if (i < tail-1) result.append(", ");
            }
        }

        result.append("]");
        return result.toString();
    }

    @Override
    public int size() {
        int res = tail - head;
        if (res < 0) {
            res += elements.length;
        }
        return res;
    }

    @Override
    public boolean isEmpty() {
        return (head == tail);
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

    @Override
    public boolean add(E element) {
        if (element == null)
            throw new NullPointerException();

        int next = add(tail, 1);
        if (next == head) {
            grow(1);
            next = add(tail, 1);
        }

        elements[tail] = element;
        tail = next;
        return true;
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
        return null;
    }

    @Override
    public void addFirst(E element) {
        if (element == null)
            throw new NullPointerException();

        if (tail == head) {
            grow(1);
        }
        head = sub(head, 1);
        elements[head] = element;
    }

    @Override
    public void addLast(E element) {add(element);}

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
    public E element() {
        return elements[head];
    }

    @Override
    public E peek() {
        return null;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
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
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public E getFirst() {return element();}

    @Override
    public E getLast() {
        return elements[sub(tail, 1)];
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
    public E poll() {
        E result = elements[head];
        elements[head] = null;
        head = add(head, 1);
        return result;
    }

    @Override
    public E pollFirst() {return poll();}

    @Override
    public E pollLast() {
        tail = sub(tail, 1);
        E result = elements[tail];
        elements[tail] = null;
        return result;
    }
}
