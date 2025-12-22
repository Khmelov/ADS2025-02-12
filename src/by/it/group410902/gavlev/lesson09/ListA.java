package by.it.group410902.gavlev.lesson09;

import java.util.*;

public class ListA<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private int size = 0;
    private Object[] data;

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    public ListA(int capaсity) {
        data = new Object[capaсity];
    }

    public ListA() {
        this(10);
    }

    private void updateCapacity() {
        Object[] newData = new Object[data.length * 3 / 2 + 1];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }

    public int getCapacity() {
        return data.length;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        for (int i = 0; i < size; i++) {
            stringBuilder.append(data[i]);
            if (i < size - 1) stringBuilder.append(", ");
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public boolean add(E e) {
        try {
            if (size + 1 == data.length) {
                updateCapacity();
            }
            data[size++] = e;
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Size: " + size + ", Index: " + index);
        E element = (E) data[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(data, index + 1, data, index, numMoved);
        }
        data[--size] = null;
        return element;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Size: " + size + ", Index: " + index);
        if (size + 1 == data.length) {
            updateCapacity();
        }
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (data[i] == null) {
                    int numMoved = size - i - 1;
                    if (numMoved > 0) {
                        System.arraycopy(data, i + 1, data, i, numMoved);
                    }
                    data[--size] = null;
                    return true;
                }
            }
        }
        else {
            for (int i = 0; i < size; i++) {
                if (o.equals(data[i])) {
                    int numMoved = size - i - 1;
                    if (numMoved > 0) {
                        System.arraycopy(data, i + 1, data, i, numMoved);
                    }
                    data[--size] = null;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Size: " + size + ", Index: " + index);
        E oldValue = (E) data[index];
        data[index] = element;
        return oldValue;
    }


    @Override
    public boolean isEmpty() {
        if (size == 0) return true;
        return false;
    }


    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (data[i] == null) return i;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(data[i])) return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Size: " + size + ", Index: " + index);
        return (E) data[index];
    }

    @Override
    public boolean contains(Object o) {
        if (indexOf(o) != -1) return true;
        else return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        int lastIndex = -1;
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (data[i] == null) lastIndex = i;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(data[i])) lastIndex = i;
            }
        }
        return lastIndex;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) if (!contains(e)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.size() == 0) return false;
        else {
            if (size + c.size() >= data.length) updateCapacity();
            for (E obj : c) this.add(obj);
            return true;
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Size: " + size + ", Index: " + index);
        if (c.size() == 0) return false;
        else {
            if (size + c.size() >= data.length) updateCapacity();
            for (E obj : c) this.add(index++, obj);
            return true;
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object obj : c) {
            for (int i = 0; i < size; i++) {
                if (obj.equals(data[i])) {
                    this.remove(i);
                    i--;
                    modified = true;
                }
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(data[i])) {
                this.remove(i);
                i--;
                modified = true;
            }
        }
        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (toIndex - fromIndex < 0) throw new IndexOutOfBoundsException("Size: " + size + ", fromIndex: " + fromIndex + ", toIndex: " + toIndex);
        ListC sub = new ListC(toIndex - fromIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            sub.add(data[i]);
        }
        return sub;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index);
        return new ListIterator<E>() {
            int cursor = index;
            int lastRet = -1;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                lastRet = cursor;
                return (E) data[cursor++];
            }

            @Override
            public boolean hasPrevious() {
                return cursor > 0;
            }

            @Override
            public E previous() {
                if (!hasPrevious()) throw new NoSuchElementException();
                lastRet = --cursor;
                return (E) data[cursor];
            }

            @Override
            public int nextIndex() {
                return cursor;
            }

            @Override
            public int previousIndex() {
                return cursor - 1;
            }

            @Override
            public void remove() {
                if (lastRet < 0) throw new IllegalStateException();
                ListA.this.remove(lastRet);
                if (cursor > lastRet) cursor--;
                lastRet = -1;
            }

            @Override
            public void set(E e) {
                if (lastRet < 0) throw new IllegalStateException();
                data[lastRet] = e;
            }

            @Override
            public void add(E e) {
                ListA.this.add(cursor++, e);
                lastRet = -1;
            }
        };
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) Arrays.copyOf(data, size, a.getClass());
        }
        System.arraycopy(data, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(data, size);
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int cursor = 0;
            int lastRet = -1;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                lastRet = cursor;
                return (E) data[cursor++];
            }

            @Override
            public void remove() {
                if (lastRet < 0) throw new IllegalStateException();
                ListA.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
            }
        };
    }


}
