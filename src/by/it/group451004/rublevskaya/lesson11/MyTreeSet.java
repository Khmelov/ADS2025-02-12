package by.it.group451004.rublevskaya.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E extends Comparable<E>> implements Set<E> {
    private E[] arr;
    private int currentSize;

    public MyTreeSet() {
        arr = (E[]) new Comparable[10];
        currentSize = 0;
    }

    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public void clear() {
        arr = (E[]) new Comparable[10];
        currentSize = 0;
    }

    @Override
    public boolean isEmpty() {
        return currentSize == 0;
    }

    @Override
    public boolean contains(Object obj) {
        return indexOf((E) obj) >= 0;
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
    public <T> T[] toArray(T[] arr) {
        return null;
    }

    private int indexOf(E info) {
        int left = 0, right = currentSize - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int res = arr[mid].compareTo(info);
            if (res == 0) {
                return mid;
            } else if (res < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    @Override
    public boolean add(E info) {
        if (contains(info)) {
            return false;
        }

        if (currentSize == arr.length) {
            resize();
        }
        int insertIndex = findInsertionIndex(info);
        for (int i = currentSize; i > insertIndex; i--) {
            arr[i] = arr[i - 1];
        }
        arr[insertIndex] = info;
        currentSize++;
        return true;
    }

    private int findInsertionIndex(E info) {
        int left = 0, right = currentSize - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (arr[mid].compareTo(info) < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }

    private void resize() {
        E[] copy = (E[]) new Comparable[arr.length * 2];
        for (int i = 0; i < arr.length; i++) {
            copy[i] = arr[i];
        }
        arr = copy;
    }

    @Override
    public boolean remove(Object obj) {
        int index = indexOf((E) obj);
        if (index >= 0) {
            for (int i = index; i < currentSize - 1; i++) {
                arr[i] = arr[i + 1];
            }
            arr[currentSize - 1] = null;
            currentSize--;
            return true;
        }
        return false;
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
        for (E info: collection) {
            if (add(info)) {
                isCorrect = true;
            }
        }
        return isCorrect;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean isCorrect = false;
        for (Object obj : collection) {
            if (remove(obj)) {
                isCorrect = true;
            }
        }
        return isCorrect;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean isCorrect = false;
        for (int i = currentSize - 1; i >= 0; i--) {
            if (!collection.contains(arr[i])) {
                remove(arr[i]);
                isCorrect = true;
            }
        }
        return isCorrect;
    }

    @Override
    public String toString() {
        StringBuilder resultStr = new StringBuilder("[");
        for (int i = 0; i < currentSize; i++) {
            resultStr.append(arr[i]);
            if (i < currentSize - 1) {
                resultStr.append(", ");
            }
        }
        resultStr.append("]");
        return resultStr.toString();
    }
}