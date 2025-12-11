package by.it.group451001.demidovich.lesson01.lesson09;

import java.lang.reflect.Array;
import java.util.*;

public class ListB<E> implements List<E> {


    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private static class Node<E> {
        E value;
        ListA.Node<E> next;
        Node(E val) { value = val; }
    }
    private ListA.Node<E> head;
    private ListA.Node<E> tail;
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
        ListA.Node<E> cur = head;
        while (cur != null) {
            sb.append(cur.value);
            if (cur.next != null) sb.append(", ");
            cur = cur.next;
        }
        sb.append(']');
        return sb.toString();
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    @Override
    public boolean add(E e) {//добавление в конец
        ListA.Node<E> node = new ListA.Node<>(e);
        if (head == null) {
            head = tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
        return true;
    }

    @Override
    public E remove(int index) {//удаление по индексу
        checkIndex(index);
        ListA.Node<E> cur = head;
        ListA.Node<E> prev = null;
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
    public int size(){
        return size;
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    @Override
    public void add(int index, E element) {//вставка по индексу
        checkIndexForAdd(index);
        ListA.Node<E> newNode = new ListA.Node<>(element);
        if (index == 0) {//вставка в начало
            newNode.next = head;
            head = newNode;
            if (tail == null) tail = newNode;
        } else if (index == size) {//вставка в конец
            add(element);
            return;
        } else {//вставка в середину
            ListA.Node<E> cur = head;
            for (int i = 0; i < index - 1; i++) cur = cur.next;
            newNode.next = cur.next;
            cur.next = newNode;
        }
        size++;
    }

    @Override
    public boolean remove(Object o) {

        ListA.Node<E> cur = head;
        ListA.Node<E> prev = null;
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
        ListA.Node<E> cur = head;
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
    public int indexOf(Object o) {//Линейный поиск от головы к хвосту.

        ListA.Node<E> cur = head;
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
        ListA.Node<E> cur = head;
        for (int i = 0; i < index; i++) cur = cur.next;
        return cur.value;
    }

    @Override
    public boolean contains(Object o){
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {//Проходит весь список, запоминая последнее совпадение.
        ListA.Node<E> cur = head;
        int idx = 0;
        int last = -1;
        while (cur != null) {
            if (o == null ? cur.value == null : o.equals(cur.value)) last = idx;
            cur = cur.next;
            idx++;
        }
        return last;
    }


    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////


    @Override
    public boolean containsAll(Collection<?> c) {//Проверяет, что все элементы коллекции c содержатся в списке.
        for (Object item : c) {
            if (!contains(item)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {//Последовательно добавляет все элементы коллекции в конец.
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
        ListA.Node<E> cur = head;
        ListA.Node<E> prev = null;
        for (int i = 0; i < index; i++) {
            prev = cur;
            cur = cur.next;
        }
        ListA.Node<E> firstNew = null;
        ListA.Node<E> lastNew = null;
        for (E e : c) {
            ListA.Node<E> n = new ListA.Node<>(e);
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
    public boolean removeAll(Collection<?> c) {//Удаляет все элементы, содержащиеся в коллекции c
        boolean changed = false;
        ListA.Node<E> cur = head;
        ListA.Node<E> prev = null;
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
    public boolean retainAll(Collection<?> c) {//Удаляет все элементы, НЕ содержащиеся в коллекции c (оставляет только пересечение).
        boolean changed = false;
        ListA.Node<E> cur = head;
        ListA.Node<E> prev = null;
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
    public List<E> subList(int fromIndex, int toIndex) {//Создает новый ListA<E> (не ListB<E>) с элементами из заданного диапазона.
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
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {//Стандартная реализация преобразования списка в массив.
        @SuppressWarnings("unchecked")
        T[] arr = (T[]) (a.length >= size
                ? a
                : (T[]) Array.newInstance(a.getClass().getComponentType(), size));
        ListA.Node<E> cur = head;
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
        ListA.Node<E> cur = head;
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
