package by.it.group410901.kvitchenko.lesson10;

public class MyLinkedList<E> implements java.util.Deque<E> {
    private E[] array;
    private int size;

    @SuppressWarnings("unchecked")
    public MyLinkedList() {
        array = (E[]) new Object[16];
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == array.length) {
            E[] newArray = (E[]) new Object[array.length * 2];
            System.arraycopy(array, 0, newArray, 0, size);
            array = newArray;
        }
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(array[i]);
            if (i < size - 1) sb.append(", ");
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
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index);
        E removed = array[index];
        for (int i = index; i < size - 1; i++) {
            array[i] = array[i + 1];
        }
        array[--size] = null;
        return removed;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && array[i] == null) ||
                    (o != null && o.equals(array[i]))) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E element) {
        ensureCapacity();
        for (int i = size; i > 0; i--) {
            array[i] = array[i - 1];
        }
        array[0] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        ensureCapacity();
        array[size++] = element;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new java.util.NoSuchElementException("Empty");
        return array[0];
    }

    @Override
    public E getLast() {
        if (size == 0) throw new java.util.NoSuchElementException("Empty");
        return array[size - 1];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        return remove(0);
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        E e = array[size - 1];
        array[--size] = null;
        return e;
    }

    // Остальные методы Deque оставляем с UnsupportedOperationException
    @Override public boolean isEmpty() { return size == 0; }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() { throw new UnsupportedOperationException(); }
    @Override public boolean offer(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean offerFirst(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean offerLast(E e) { throw new UnsupportedOperationException(); }
    @Override public E remove() { throw new UnsupportedOperationException(); }
    @Override public E removeFirst() { throw new UnsupportedOperationException(); }
    @Override public E removeLast() { throw new UnsupportedOperationException(); }
    @Override public E peek() { throw new UnsupportedOperationException(); }
    @Override public E peekFirst() { throw new UnsupportedOperationException(); }
    @Override public E peekLast() { throw new UnsupportedOperationException(); }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public void push(E e) { throw new UnsupportedOperationException(); }
    @Override public E pop() { throw new UnsupportedOperationException(); }
    @Override public java.util.Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
}