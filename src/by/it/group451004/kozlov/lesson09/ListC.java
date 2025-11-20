package by.it.group451004.kozlov.lesson09;

import java.util.*;

public class ListC<E> implements List<E> {

    private Object[] arr;
    private int usedLength;
    final int startLength = 128;

    public ListC(E... elements) {
        int length = startLength;
        while (length < elements.length) {
            length = Integer.MAX_VALUE / 2 <= length ? Integer.MAX_VALUE : length * 2;
        }
        arr = Arrays.copyOf(elements, length);
        usedLength = elements.length;
    }

    public ListC() {
        arr = new Object[startLength];
        usedLength = 0;
    }

    @Override
    public String toString() {
        if (usedLength == 0) {
            return "[]";
        }

        StringBuilder result = new StringBuilder();
        result.append("[");
        for (int i = 0; i < usedLength; i++) {
            if (i > 0) result.append(", ");
            if (arr[i] != null) {
                result.append(arr[i]);
            } else {
                result.append("null");
            }
        }
        result.append("]");

        return result.toString();
    }

    @Override
    public boolean add(E e) {
        if (usedLength == Integer.MAX_VALUE)
            return false;

        while (usedLength >= arr.length) //расширяем - всегда удачно
        {
            int length = Integer.MAX_VALUE / 2 <= arr.length ? Integer.MAX_VALUE : arr.length * 2;
            arr = Arrays.copyOf(arr, length);
        }
        //на выходе usedLength < arr.Length, поэтому ошибки ниже не будет
        arr[usedLength++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= usedLength)
            throw new IndexOutOfBoundsException();

        E result = ((E) arr[index]);

        usedLength--;
        for (int i = index; i < usedLength; i++) {
            arr[i] = arr[i + 1];
        }
        arr[usedLength] = null;
        return result;
    }

    @Override
    public int size() {
        return usedLength;
    }

    @Override
    public void add(int index, E element) {
        if (usedLength == Integer.MAX_VALUE)
            throw new IllegalStateException("List has reached maximum capacity");

        if (index < 0 || index > usedLength) // исправлено: было >= usedLength
            throw new IndexOutOfBoundsException();

        while (usedLength >= arr.length) //расширяем - всегда удачно
        {
            int length = Integer.MAX_VALUE / 2 <= arr.length ? Integer.MAX_VALUE : arr.length * 2;
            arr = Arrays.copyOf(arr, length);
        }
        //на выходе usedLength < arr.Length, поэтому ошибки ниже не будет

        for (int i = usedLength; i > index; i--) {
            arr[i] = arr[i - 1];
        }
        arr[index] = element;
        usedLength++;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < usedLength; i++) {
            if (arr[i].equals(o)) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= usedLength)
            throw new IndexOutOfBoundsException();
        E result = (E) arr[index];
        arr[index] = element;
        return result;
    }

    @Override
    public boolean isEmpty() {
        return usedLength == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < usedLength; i++) {
            arr[i] = null;
        }
        usedLength = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < usedLength; i++) {
            if (arr[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= usedLength)
            throw new IndexOutOfBoundsException();
        return (E) arr[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = usedLength - 1; i >= 0; i--) {
            if (arr[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (var el : c) {
            if (!contains(el))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        }
        for (var el : c) {
            if (!add(el))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false; // исправлено: возвращаем false для пустой коллекции
        }

        if (index < 0 || index > usedLength) {
            throw new IndexOutOfBoundsException();
        }

        for (E element : c) {
            add(index++, element);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        Iterator<E> it = this.iterator();
        while (it.hasNext()) {
            if (c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        try {
            for (int i = usedLength - 1; i >= 0; i--) {
                if (!c.contains(arr[i]))
                    remove(i);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || fromIndex >= usedLength)
            throw new IndexOutOfBoundsException();
        if (toIndex < 0 || toIndex >= usedLength)
            throw new IndexOutOfBoundsException();
        return new ListC<E>((E) Arrays.copyOfRange(arr, fromIndex, toIndex));
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ArrayListIterator(index);
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ArrayListIterator(0);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < usedLength) {
            return (T[]) (Arrays.copyOf(arr, usedLength));
        } else {
            for (int i = 0; i < a.length; i++) {
                if (i < usedLength) {
                    a[i] = (T) arr[i];
                } else {
                    a[i] = null;
                }
            }
            return a;
        }
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(arr, usedLength);
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {
        int cursor = 0; // текущая позиция
        int lastRet = -1; // индекс последнего возвращённого элемента

        @Override
        public boolean hasNext() {
            return cursor < usedLength;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastRet = cursor;
            return (E)arr[cursor++];
        }

        @Override
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            ListC.this.remove(lastRet); // вызываем remove у твоего списка
            cursor = lastRet; // сдвигаем курсор назад
            lastRet = -1;
        }
    }

    private class ArrayListIterator implements ListIterator<E> {

        private int pointer;
        private int lastRet = -1;

        public ArrayListIterator(int index){
            pointer = index;

        }

        @Override
        public boolean hasNext() {
            return pointer < usedLength;
        }

        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            lastRet = pointer;
            return (E)arr[pointer++];
        }

        @Override
        public boolean hasPrevious() {
            return pointer > 0 && pointer < usedLength;
        }

        @Override
        public E previous() {
            if (!hasPrevious()) throw new NoSuchElementException();
            lastRet = --pointer;
            return (E)arr[pointer];
        }

        @Override
        public int nextIndex() {
            return pointer;
        }

        @Override
        public int previousIndex() {
            return pointer - 1;
        }

        @Override
        public void remove() {
            if (lastRet < 0) throw new IllegalStateException();
            ListC.this.remove(lastRet);
            if (lastRet < pointer) pointer--;
            lastRet = -1;
        }

        @Override
        public void set(E e) {
            if (lastRet < 0) throw new IllegalStateException();
            arr[lastRet] = e;
        }

        @Override
        public void add(E e) {
            ListC.this.add(pointer++, e);
            lastRet = -1;
        }
    }
}