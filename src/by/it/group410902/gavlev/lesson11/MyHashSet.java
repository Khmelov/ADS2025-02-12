package by.it.group410902.gavlev.lesson11;

import by.it.group410902.gavlev.lesson09.ListA;
import by.it.group410902.gavlev.lesson10.MyLinkedList;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    MyLinkedList<E>[] data;
    int size = 0;

    @SuppressWarnings("unchecked")
    public MyHashSet(int capacity) {
        data = new MyLinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            data[i] = new MyLinkedList<E>();
        }
    }

    public MyHashSet() {
        this(8);
    }

    private void resize() {
        int newdataLen = data.length * 3 / 2;
        MyLinkedList<E>[] newdata = new MyLinkedList[newdataLen];
        MyLinkedList<E>[] olddata = data;
        for (int i = 0; i < newdataLen; i++) {
            newdata[i] = new MyLinkedList<E>();
        }
        for (E el : this) {
            int hashID = (el.hashCode() & 0x7fffffff) % newdataLen;
            newdata[hashID].add(el);
        }
        data = newdata;
    }

    public int hashIndex(Object o) {
        return (o.hashCode() & 0x7fffffff) % data.length;
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
    public boolean contains(Object o) {
        int hashID = hashIndex(o);
        if (data[hashID].isEmpty()) {
            return false;
        }
        else if (data[hashID].size() == 1) {
            return o.equals(data[hashID].peek());
        }
        else {
            for (E el: data[hashID]) {
                if (o.equals(el)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int index = 0;
        for (E el : this) {
            array[index] = el;
            index++;
        }
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        Object[] array = new Object[size];
        int index = 0;
        for (E el : this) {
            array[index] = el;
            index++;
        }
        return (T[]) array;
    }

    @Override
    public boolean add(Object o) {
        int hashID = hashIndex(o);
        for (E el : data[hashID]) {
            if (o.equals(el)) {
                return false;
            }
        }
        data[hashID].add((E) o);
        size++;
        if (size >= data.length) this.resize();
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hashID = hashIndex(o);
        for (E el : data[hashID]) {
            if (o.equals(el)) {
                data[hashID].remove(el);
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        for (int i = 0; i < data.length; i++) {
            data[i] = new MyLinkedList<>();
        }
        size = 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int index = 0;
            Iterator<E> itLink = data[0].iterator();
            @Override
            public boolean hasNext() {
                while ((itLink == null || !itLink.hasNext()) && index < data.length - 1) {
                    index++;
                    itLink = data[index].iterator();
                }
                return itLink != null && itLink.hasNext();
            }

            @Override
            public E next() {
                while ((itLink == null || !itLink.hasNext()) && index < data.length - 1) {
                    index++;
                    itLink = data[index].iterator();
                }
                if (itLink == null || !itLink.hasNext()) {
                    throw new NoSuchElementException();
                }
                return itLink.next();
            }
        };
    }

    @Override
    public String toString() {
        if (isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (E el : this) {
            sb.append(el).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        return sb.toString();
    }
}
