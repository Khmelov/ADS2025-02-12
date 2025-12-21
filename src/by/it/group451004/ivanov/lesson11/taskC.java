package by.it.group451004.ivanov.lesson11;

import java.util.*;

class MyTreeSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 10;

    private Object[] elements;
    private int size;
    private final Comparator<? super E> comparator;

    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.comparator = null;
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    public MyTreeSet(Comparator<? super E> comparator) {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.comparator = comparator;
        this.size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        } else {
            return ((Comparable<? super E>) a).compareTo(b);
        }
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            elements = Arrays.copyOf(elements, elements.length * 2);
        }
    }

    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException();
        }

        if (contains(element)) {
            return false;
        }

        ensureCapacity();
        elements[size] = element;
        size++;

        sort();

        return true;
    }

    @Override
    public boolean remove(Object element) {
        if (element == null) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (Objects.equals(elements[i], element)) {
                System.arraycopy(elements, i + 1, elements, i, size - i - 1);
                elements[size - 1] = null;
                size--;
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean contains(Object element) {
        if (element == null) {
            return false;
        }

        return binarySearch(element) >= 0;
    }

    @SuppressWarnings("unchecked")
    private int binarySearch(Object element) {
        int left = 0;
        int right = size - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int cmp = compare((E) element, (E) elements[mid]);

            if (cmp == 0) {
                return mid;
            } else if (cmp < 0) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return -1;
    }

    @SuppressWarnings("unchecked")
    private void sort() {
        for (int i = 1; i < size; i++) {
            E key = (E) elements[i];
            int j = i - 1;

            while (j >= 0 && compare(key, (E) elements[j]) < 0) {
                elements[j + 1] = elements[j];
                j--;
            }
            elements[j + 1] = key;
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) {
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
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int current = 0;
            private int lastReturned = -1;
            private int expectedModCount = size;

            @Override
            public boolean hasNext() {
                if (expectedModCount != size) {
                    throw new ConcurrentModificationException();
                }
                return current < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                lastReturned = current;
                return (E) elements[current++];
            }

            @Override
            public void remove() {
                if (lastReturned == -1) {
                    throw new IllegalStateException();
                }
                MyTreeSet.this.remove(elements[lastReturned]);
                current--;
                lastReturned = -1;
                expectedModCount = size;
            }
        };
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) Arrays.copyOf(elements, size, a.getClass());
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
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
        for (Object element : c) {
            if (remove(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Set)) return false;
        Set<?> that = (Set<?>) o;
        if (this.size != that.size()) return false;

        return containsAll(that);
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (int i = 0; i < size; i++) {
            result += elements[i].hashCode();
        }
        return result;
    }
}
