package by.it.group410902.plekhova.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;


public class MyHashSet<E> implements Set<E> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size;
    private int threshold; //число элементов, при достижении которого хэш-таблица увеличивает размер.
    private final float loadFactor; // коэффициент загрузки

    @SuppressWarnings("unchecked")
    public MyHashSet(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) initialCapacity = DEFAULT_INITIAL_CAPACITY;
        this.loadFactor = loadFactor > 0 ? loadFactor : DEFAULT_LOAD_FACTOR;
        table = (Node<E>[]) new Node[initialCapacity];
        this.size = 0;
        this.threshold = (int) (initialCapacity * this.loadFactor);
    }

    public MyHashSet(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public MyHashSet() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    // Внутренний узел односвязного списка в бакете
    private static class Node<E> {
        final E key;
        final int hash;
        Node<E> next;

        Node(E key, int hash, Node<E> next) {
            this.key = key;
            this.hash = hash;
            this.next = next;
        }
    }



    private int hash(Object o) {
        return (o == null) ? 0 : (o.hashCode() & 0x7fffffff);
    }

    private int indexForHash(int hash, int length) {
        return hash % length;
    }

    @SuppressWarnings("unchecked")
    private void resizeIfNeeded() {
        if (size < threshold) return;
        int newCap = table.length * 2;
        Node<E>[] newTable = (Node<E>[]) new Node[newCap];

        // ре-хеш всех элементов (перенос в новые бакеты)
        for (int i = 0; i < table.length; i++) {
            Node<E> node = table[i];
            while (node != null) {
                Node<E> next = node.next;
                int idx = indexForHash(node.hash, newCap);
                node.next = newTable[idx];
                newTable[idx] = node;
                node = next;
            }
        }
        table = newTable;
        threshold = (int) (newCap * loadFactor);
    }



    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) table[i] = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;
        for (int i = 0; i < table.length; i++) {
            for (Node<E> n = table[i]; n != null; n = n.next) {
                if (!first) sb.append(", ");
                sb.append(n.key);
                first = false;
            }
        }
        sb.append(']');
        return sb.toString();
    }

    /**
     * Добавляет элемент, если его ещё нет. Возвращает true, если добавили.
     */
    @Override
    public boolean add(E e) {
        int h = hash(e);
        int idx = indexForHash(h, table.length);

        // проверка на существование
        for (Node<E> n = table[idx]; n != null; n = n.next) {
            if (n.hash == h) {
                if (e == n.key || (e != null && e.equals(n.key))) {
                    return false; // уже есть
                }
            }
        }

        // вставка в голову списка бакета
        Node<E> newNode = new Node<>(e, h, table[idx]);
        table[idx] = newNode;
        size++;
        resizeIfNeeded();
        return true;
    }

    /*
      Удаляет элемент, если он есть. Возвращает true, если удалили.
     */
    @Override
    public boolean remove(Object o) {
        int h = hash(o);
        int idx = indexForHash(h, table.length);

        Node<E> prev = null;
        Node<E> cur = table[idx];
        while (cur != null) {
            if (cur.hash == h && (o == cur.key || (o != null && o.equals(cur.key)))) {
                if (prev == null) {
                    table[idx] = cur.next;
                } else {
                    prev.next = cur.next;
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
    public boolean contains(Object o) {
        int h = hash(o);
        int idx = indexForHash(h, table.length);
        for (Node<E> n = table[idx]; n != null; n = n.next) {
            if (n.hash == h && (o == n.key || (o != null && o.equals(n.key)))) return true;
        }
        return false;
    }


    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        for (Object e : c) if (!contains(e)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;
        for (E e : c) {
            if (add(e)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;
        for (Object e : c) {
            while (remove(e)) { // удаляем все вхождения (по контракту Set их не может быть несколько, но на всякий случай)
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        boolean changed = false;
        for (int i = 0; i < table.length; i++) {
            Node<E> prev = null;
            Node<E> cur = table[i];
            while (cur != null) {
                if (!c.contains(cur.key)) {
                    // удалить cur
                    if (prev == null) {
                        table[i] = cur.next;
                    } else {
                        prev.next = cur.next;
                    }
                    size--;
                    changed = true;
                    cur = (prev == null) ? table[i] : prev.next;
                } else {
                    prev = cur;
                    cur = cur.next;
                }
            }
        }
        return changed;
    }


    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int bucket = 0;
            Node<E> current = null;
            Node<E> lastReturned = null;

            {
                // поставить current на первый непустой узел
                advanceToNext();
            }

            private void advanceToNext() {
                if (current != null && current.next != null) {
                    current = current.next;
                    return;
                }
                current = null;
                while (bucket < table.length) {
                    if (table[bucket] != null) {
                        current = table[bucket++];
                        return;
                    }
                    bucket++;
                }
                // если дошли до конца, current останется null
            }

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null) throw new NoSuchElementException();
                E res = current.key;
                lastReturned = current;
                advanceToNext();
                return res;
            }

            @Override
            public void remove() {
                if (lastReturned == null) throw new IllegalStateException();
                MyHashSet.this.remove(lastReturned.key);
                lastReturned = null;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int pos = 0;
        for (int i = 0; i < table.length; i++) {
            for (Node<E> n = table[i]; n != null; n = n.next) {
                arr[pos++] = n.key;
            }
        }
        return arr;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            // создать новый массив нужного типа
            T[] newArr = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
            int i = 0;
            for (int b = 0; b < table.length; b++) {
                for (Node<E> n = table[b]; n != null; n = n.next) {
                    newArr[i++] = (T) n.key;
                }
            }
            return newArr;
        } else {
            int i = 0;
            for (int b = 0; b < table.length; b++) {
                for (Node<E> n = table[b]; n != null; n = n.next) {
                    a[i++] = (T) n.key;
                }
            }
            if (a.length > size) a[size] = null;
            return a;
        }
    }



}
