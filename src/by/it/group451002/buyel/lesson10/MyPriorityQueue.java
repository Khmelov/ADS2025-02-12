package by.it.group451002.buyel.lesson10;

import java.lang.reflect.Array;
import java.util.*;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    transient E[] queue;
    int size;

//    -------------------------------------------
//    toString() -
//    size() -
//    clear() -
//    add(E element) -
//    remove() -
//    contains(E element) -
//
//    offer(E element) -
//    poll() -
//    peek() -
//    element() -
//    isEmpty() -
//
//    containsAll(Collection<E> c) -
//    addAll(Collection<E> c) -
//    removeAll(Collection<E> c) -
//    retainAll(Collection<E> c) -
//    -------------------------------------------

    private int myHashCode(E elem, int size) {
        String str = elem.toString();
        int code = str.hashCode();
        return Math.abs(31 * code) % size;
    }

    private Node[] getHashArray(Collection<?> c) {
        Node[] tempHashArray = new Node[c.size()];
        for (Object elem : c){
            int index = myHashCode((E) elem, c.size());

            Node tempNode = tempHashArray[index];
            while (tempNode != null && tempNode.next != null) {
                tempNode = tempNode.next;
            }
            if (tempNode == null) {
                tempHashArray[index] = new Node(elem);
            } else {
                tempNode.next = new Node<>(elem);
                tempNode.next.prev = tempNode;
            }
        }
        return tempHashArray;
    }

    private E[] appendElem(E[] array, E elem) {
        E[] tempArray = (E[]) Array.newInstance(elem.getClass(), array.length+1);
        for (int i = 0; i < array.length; i++) {
            tempArray[i] = array[i];
        }
        tempArray[array.length] = elem;
        return tempArray;
    }

    private static <E> void swap(E[] array, int i, int j) {
        E temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private <E extends Comparable<E>> void heapMin(E[] array, int ind) {
        while (ind > 0 && array[(ind - 1)/2].compareTo(array[ind]) > 0) {
            swap(array, ind, (ind - 1)/2);
            ind = (ind - 1)/2;
        }
    }

    private <E extends Comparable<E>> void heapifyDown(E[] array, int ind, int size) {
        int left, right, smallest;
        while (true) {
            left = 2 * ind + 1;
            right = 2 * ind + 2;
            smallest = ind;

            if (left < size && array[left].compareTo(array[smallest]) < 0) {
                smallest = left;
            }
            if (right < size && array[right].compareTo(array[smallest]) < 0) {
                smallest = right;
            }
            if (smallest != ind) {
                swap(array, ind, smallest);
                ind = smallest;
            } else {
                break;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        E[] tempNode = queue;

        for (int i = 0; i < size; i++) {
            result.append(tempNode[i]);
            if (i < size-1) {
                result.append(", ");
            }
        }

        result.append("]");
        return result.toString();
    }

    @Override
    public int size() {return size;}

    @Override
    public boolean isEmpty() {
        return (0 == size);
    }

    @Override
    public boolean contains(Object o) {
        int i = 0;
        while (i < size && !o.equals(queue[i])) {i++;}
        return o.equals(queue[i - ((i == size) ? 1 : 0)]);
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
        size++;
        E[] tempQueue = (E[]) Array.newInstance(e.getClass(), size);

        for (int i = 0; i < size - 1; i++) {
            tempQueue[i] = queue[i];
        }

        tempQueue[size - 1] = e;
        queue = tempQueue;

        heapMin(queue, size-1);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        Node[] tempHashArray = getHashArray(Arrays.asList(queue));
        int count = 0;

        for (Object elem : c) {
            int elemCode = myHashCode((E) elem, queue.length);

            Node tempNode = tempHashArray[elemCode];
            while (tempNode != null) {
                if (tempNode.data == elem) {
                    count++;
                    break;
                }
                tempNode = tempNode.next;
            }
        }

        return (c.size() == count);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        }
        for (E elem : c) {
            add(elem);
        }
        return true;
    }

    private boolean containsElem(Object[] array, Object o) {
        int i = 0;
        while (i < array.length && !o.equals(array[i])) {i++;}
        return o.equals(array[i - ((i == array.length) ? 1 : 0)]);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c.isEmpty() || size == 0) {
            return false;
        }
        boolean modified = false;

        E[] tempArray = (E[]) Array.newInstance(queue.getClass().getComponentType(), 0);

        for (Object elem : queue) {
            if (!containsElem(c.toArray(), elem)) {
                tempArray = appendElem(tempArray, (E) elem);
            } else {
                modified = true;
            }
        }

        queue = tempArray;
        size = queue.length;

        for (int i = (size / 2) - 1; i >= 0; i--) {
            heapifyDown(queue, i, size);
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c.isEmpty()) {
            clear();
            return true;
        }

        E[] tempArray = (E[]) Array.newInstance(queue.getClass().getComponentType(), 0);

        for (E elem : queue) {
            if (containsElem(c.toArray(), elem)) {
                tempArray = appendElem(tempArray, elem);
            }
        }

        queue = tempArray;
        size = queue.length;

        for (int i = (size / 2) - 1; i >= 0; i--) {
            heapifyDown(queue, i, size);
        }

        return true;
    }

    @Override
    public void clear() {
        size = 0;
        queue = (E[]) Array.newInstance(queue.getClass().getComponentType(), 0);
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E remove() {
        if (size == 0) {
            throw new IllegalStateException();
        }

        E removedElem = queue[0];
        size--;

        if (size == 0) {
            queue = (E[]) Array.newInstance(queue.getClass().getComponentType(), 0);
            return removedElem;
        }

        queue[0] = queue[size];

        // Shrink array by one
        E[] tempQueue = (E[]) Array.newInstance(queue.getClass().getComponentType(), size);
        System.arraycopy(queue, 0, tempQueue, 0, size);   // O(n), n = size

        queue = tempQueue;

        heapifyDown(queue, 0, size);

        return removedElem;
    }

    @Override
    public E poll() {
        if (0 == size) {
            return null;
        }
        return remove();
    }

    @Override
    public E element() {
        if (0 == size) {
            throw new NoSuchElementException();
        }
        return queue[0];
    }

    @Override
    public E peek() {
        if (0 == size) {
            return null;
        }
        return queue[0];
    }
}
