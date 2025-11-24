package by.it.group451004.struts.lesson11;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E extends Comparable<E>> implements Set<E> {
    int size = 0;
    E[] storage;

    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        storage = (E[]) new Comparable[0];
    }

    @SuppressWarnings("unchecked")
    private void resize(int length) {
        E[] newStorage = (E[]) new Comparable[length];
        System.arraycopy(storage, 0, newStorage, 0, storage.length);
        storage = newStorage;
    }

    private void sort() {
        if (size < 2)
            return;

        int mid = size / 2;
        MyTreeSet<E> left = new MyTreeSet<>();
        MyTreeSet<E> right = new MyTreeSet<>();

        left.addAll(Arrays.asList(storage).subList(0, mid));
        right.addAll(Arrays.asList(storage).subList(mid, size));

        left.sort();
        right.sort();

        merge(left, right);
    }
    private void merge(MyTreeSet<E> left, MyTreeSet<E> right) {
        int i = 0, j = 0, k = 0;

        while (i < left.size && j < right.size) {
            if (left.storage[i].compareTo(right.storage[j]) <= 0) {
                storage[k++] = left.storage[i++];
            } else {
                storage[k++] = right.storage[j++];
            }
        }

        while (i < left.size) {
            storage[k++] = left.storage[i++];
        }

        while (j < right.size) {
            storage[k++] = right.storage[j++];
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("[");
        for (int i = 0; i < size; i++) {
            if (storage[i] != null)
                sb.append(storage[i]).append(", ");
        }
        if (sb.length() > 2)
            sb.replace(sb.length() - 2, sb.length(), "]");
        else
            sb.append("]");

        return sb.toString();
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
        size = 0;
    }

    @Override
    public boolean add(E e) {
        if (contains(e))
            return false;
        if (storage.length <= size + 1)
            resize(size + 1);

        storage[size++] = e;
        sort();

        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (storage[i].equals(o)) {
                index = i;
                break;
            }
        }
        if (index == -1)
            return false;

        for (int i = index; i < size - 1; i++) {
            storage[i] = storage[i + 1];
        }
        storage[size - 1] = null;
        size--;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (storage[i].equals(o)) {
                return true;
            }
        }
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
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty())
            return false;

        if (size + c.size() > storage.length)
            resize(size + c.size());

        for (E e : c)
            if (!contains(e))
                storage[size++] = e;

        sort();
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        final int saved = size;
        for (Object o : c) {
            remove(o);
        }
        return saved != size;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        final int prevSize = size;
        for (int i = 0; i < size; ) {
            if (!c.contains(storage[i])) {
                remove(storage[i]);
            } else {
                i++;
            }
        }
        return prevSize != size;
    }
}
