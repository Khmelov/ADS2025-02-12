package by.it.group451002.ivash.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Простая реализация Set на основе массива бакетов + односвязный список в бакете.
 * Не использует коллекции стандартной библиотеки для хранения элементов.
 *
 * Формат toString() соответствует формату стандартных коллекций: [e1, e2, ...]
 */
@SuppressWarnings("unchecked")
public class MyHashSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size;
    private int threshold;
    private final float loadFactor;

    // Внутренний класс узла списка в бакете
    private static class Node<E> {
        final E key;
        Node<E> next;

        Node(E key, Node<E> next) {
            this.key = key;
            this.next = next;
        }
    }

    public MyHashSet() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashSet(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public MyHashSet(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) initialCapacity = DEFAULT_CAPACITY;
        if (loadFactor <= 0) loadFactor = DEFAULT_LOAD_FACTOR;
        this.loadFactor = loadFactor;
        table = (Node<E>[]) new Node[tableSizeFor(initialCapacity)];
        threshold = (int) (table.length * loadFactor);
        size = 0;
    }

    // Приводим capacity к степени двойки (как в стандартной HashMap) для простоты
    private int tableSizeFor(int cap) {
        int n = 1;
        while (n < cap) n <<= 1;
        return n;
    }

    private int indexFor(Object key) {
        int h = (key == null) ? 0 : key.hashCode();
        // мешаем биты (аналогично HashMap)
        h ^= (h >>> 16);
        return h & (table.length - 1);
    }

    private void resize() {
        Node<E>[] old = table;
        int newCap = old.length << 1;
        Node<E>[] ntable = (Node<E>[]) new Node[newCap];

        for (int i = 0; i < old.length; i++) {
            Node<E> node = old[i];
            while (node != null) {
                Node<E> next = node.next;
                int idx;
                E k = node.key;
                int h = (k == null) ? 0 : k.hashCode();
                h ^= (h >>> 16);
                idx = h & (newCap - 1);
                // вставляем в голову соответствующего бакета в новом массиве
                node.next = ntable[idx];
                ntable[idx] = node;
                node = next;
            }
        }
        table = ntable;
        threshold = (int)(table.length * loadFactor);
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
        int idx = indexFor(o);
        Node<E> node = table[idx];
        while (node != null) {
            if (eq(node.key, o)) return true;
            node = node.next;
        }
        return false;
    }

    private boolean eq(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) return false;
        if (size + 1 > threshold) {
            resize();
        }
        int idx = indexFor(e);
        Node<E> newNode = new Node<>(e, table[idx]);
        table[idx] = newNode;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexFor(o);
        Node<E> node = table[idx];
        Node<E> prev = null;
        while (node != null) {
            if (eq(node.key, o)) {
                if (prev == null) {
                    table[idx] = node.next;
                } else {
                    prev.next = node.next;
                }
                size--;
                return true;
            }
            prev = node;
            node = node.next;
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) table[i] = null;
        size = 0;
    }

    // Итератор по множеству
    private class Itr implements Iterator<E> {
        int bucketIndex;
        Node<E> current;
        Node<E> lastReturned;
        int seen;

        Itr() {
            current = null;
            lastReturned = null;
            bucketIndex = 0;
            seen = 0;
            advanceToNext();
        }

        private void advanceToNext() {
            while (current == null && bucketIndex < table.length) {
                current = table[bucketIndex++];
            }
        }

        @Override
        public boolean hasNext() {
            return seen < size;
        }

        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            if (current == null) throw new NoSuchElementException();
            lastReturned = current;
            E res = current.key;
            current = current.next;
            if (current == null) advanceToNext();
            seen++;
            return res;
        }

        @Override
        public void remove() {
            if (lastReturned == null) throw new IllegalStateException();
            MyHashSet.this.remove(lastReturned.key);
            lastReturned = null;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;
        for (E e : this) arr[i++] = e;
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            // создать новый массив нужного типа и размера
            @SuppressWarnings("unchecked")
            T[] newArr = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
            a = newArr;
        }
        int i = 0;
        for (E e : this) {
            @SuppressWarnings("unchecked")
            T val = (T) e;
            a[i++] = val;
        }
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            if (add(e)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            if (remove(o)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // Удаляем все элементы, не содержащиеся в c
        boolean changed = false;
        for (Iterator<E> it = iterator(); it.hasNext(); ) {
            E e = it.next();
            if (!c.contains(e)) {
                it.remove();
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Iterator<E> it = iterator();
        boolean first = true;
        while (it.hasNext()) {
            if (!first) sb.append(", ");
            first = false;
            E e = it.next();
            sb.append(e == this ? "(this Set)" : String.valueOf(e));
        }
        sb.append(']');
        return sb.toString();
    }

    // equals и hashCode можно реализовать для полноты (по контракту Set),
    // но не обязательны для задания уровня A. Всё же реализуем простую версию.

    @Override
    public int hashCode() {
        int h = 0;
        for (E e : this) {
            h += (e == null ? 0 : e.hashCode());
        }
        return h;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Set)) return false;
        Set<?> that = (Set<?>) o;
        if (that.size() != this.size()) return false;
        return this.containsAll(that);
    }
}
