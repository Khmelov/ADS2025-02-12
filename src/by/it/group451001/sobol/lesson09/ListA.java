package by.it.group451001.sobol.lesson09;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    static class Node<E> {
        E value;
        Node<E> next;
        Node(E val) { value = val; }
    }
    private Node<E> head;
    private Node<E> tail;
    private int size = 0;
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node<E> cur = head;
        while (cur != null) {
            sb.append(cur.value);
            if (cur.next != null) sb.append(", ");
            cur = cur.next;
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        Node<E> node = new Node<>(e);
        if (head == null) {
            head = tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
        return true;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        Node<E> cur = head;
        Node<E> prev = null;
        for (int i = 0; i < index; i++) {
            prev = cur;
            cur = cur.next;
        }
        E val = cur.value;
        if (prev == null) {
            head = cur.next;
            if (head == null) tail = null;
        } else {
            prev.next = cur.next;
            if (prev.next == null) tail = prev;
        }
        size--;
        return val;
    }

    @Override
    public int size() {
        return size;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }


    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index);
        Node<E> newNode = new Node<>(element);
        if (index == 0) {
            newNode.next = head;
            head = newNode;
            if (tail == null) tail = newNode;
        } else if (index == size) {
            add(element);
            return;
        } else {
            Node<E> cur = head;
            for (int i = 0; i < index - 1; i++) cur = cur.next;
            newNode.next = cur.next;
            cur.next = newNode;
        }
        size++;
    }

    @Override
    public boolean remove(Object o) {

        Node<E> cur = head;
        Node<E> prev = null;
        while (cur != null) {
            if (o == null ? cur.value == null : o.equals(cur.value)) {
                if (prev == null) {
                    head = cur.next;
                    if (head == null) tail = null;
                } else {
                    prev.next = cur.next;
                    if (prev.next == null) tail = prev;
                }
                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        Node<E> cur = head;
        for (int i = 0; i < index; i++) cur = cur.next;
        E old = cur.value;
        cur.value = element;
        return old;
    }


    @Override
    public boolean isEmpty() {

        return size == 0;
    }


    @Override
    public void clear() {
        head = tail = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {

        Node<E> cur = head;
        int idx = 0;
        while (cur != null) {
            if (o == null ? cur.value == null : o.equals(cur.value)) return idx;
            cur = cur.next;
            idx++;
        }
        return -1;
    }

    @Override
    public E get(int index) {

        checkIndex(index);
        Node<E> cur = head;
        for (int i = 0; i < index; i++) cur = cur.next;
        return cur.value;
    }

    @Override
    public boolean contains(Object o) {

        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        Node<E> cur = head;
        int idx = 0;
        int last = -1;
        while (cur != null) {
            if (o == null ? cur.value == null : o.equals(cur.value)) last = idx;
            cur = cur.next;
            idx++;
        }
        return last;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            add(e);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        checkIndexForAdd(index);
        if (c.isEmpty()) return false;
        Node<E> cur = head;
        Node<E> prev = null;
        for (int i = 0; i < index; i++) {
            prev = cur;
            cur = cur.next;
        }
        Node<E> firstNew = null;
        Node<E> lastNew = null;
        for (E e : c) {
            Node<E> n = new Node<>(e);
            if (firstNew == null) firstNew = lastNew = n;
            else {
                lastNew.next = n;
                lastNew = n;
            }
            size++;
        }
        if (prev == null) {
            lastNew.next = head;
            head = firstNew;
            if (tail == null) tail = lastNew;
        } else {
            lastNew.next = prev.next;
            prev.next = firstNew;
            if (lastNew.next == null) tail = lastNew;
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        Node<E> cur = head;
        Node<E> prev = null;
        while (cur != null) {
            if (c.contains(cur.value)) {
                if (prev == null) {
                    head = cur.next;
                    cur = head;
                    if (head == null) tail = null;
                } else {
                    prev.next = cur.next;
                    if (prev.next == null) tail = prev;
                    cur = prev.next;
                }
                size--;
                changed = true;
            } else {
                prev = cur;
                cur = cur.next;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> cur = head;
        Node<E> prev = null;
        while (cur != null) {
            if (!c.contains(cur.value)) {
                if (prev == null) {
                    head = cur.next;
                    cur = head;
                    if (head == null) tail = null;
                } else {
                    prev.next = cur.next;
                    if (prev.next == null) tail = prev;
                    cur = prev.next;
                }
                size--;
                changed = true;
            } else {
                prev = cur;
                cur = cur.next;
            }
        }
        return changed;
    }


    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex)
            throw new IndexOutOfBoundsException("fromIndex: " + fromIndex + ", toIndex: " + toIndex + ", Size: " + size);
        ListA<E> sub = new ListA<>();
        for (int i = fromIndex; i < toIndex; i++) {
            sub.add(this.get(i));
        }
        return sub;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        @SuppressWarnings("unchecked")
        T[] arr = (T[]) (a.length >= size
                ? a
                : (T[]) Array.newInstance(a.getClass().getComponentType(), size));
        Node<E> cur = head;
        int i = 0;
        while (cur != null) {
            arr[i++] = (T) cur.value;
            cur = cur.next;
        }
        if (arr.length > size) arr[size] = null;
        return arr;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        Node<E> cur = head;
        int i = 0;
        while (cur != null) {
            arr[i++] = cur.value;
            cur = cur.next;
        }
        return arr;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return null;
    }

}
