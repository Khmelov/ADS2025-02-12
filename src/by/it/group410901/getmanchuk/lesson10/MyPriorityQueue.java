package by.it.group410901.getmanchuk.lesson10;

import java.util.*;

public class MyPriorityQueue<E> implements Queue<E> {

    private Object[] data;
    private int size = 0;
    private final Comparator<? super E> comparator;
    private static final int DEFAULT_CAPACITY = 16;

    // ================== КОНСТРУКТОРЫ ==================

    public MyPriorityQueue() {
        this(null);
    }

    public MyPriorityQueue(Comparator<? super E> comparator) {
        this.data = new Object[DEFAULT_CAPACITY];
        this.comparator = comparator;
    }

    // ================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ==================

    private void ensureCapacity(int minCapacity) {
        if (minCapacity <= data.length) return;
        int newCap = Math.max(data.length << 1, minCapacity);
        data = Arrays.copyOf(data, newCap);
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) return comparator.compare(a, b);
        return ((Comparable<? super E>) a).compareTo(b);
    }

    // ================== ДОБАВЛЕНИЕ ==================

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();
        ensureCapacity(size + 1);
        data[size] = e;
        siftUp(size++);
        return true;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    // ================== УДАЛЕНИЕ / ДОСТУП ==================

    @Override
    @SuppressWarnings("unchecked")
    public E poll() {
        if (size == 0) return null;
        E result = (E) data[0];
        data[0] = data[--size];
        data[size] = null;
        if (size > 0) siftDown(0);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E peek() {
        return size == 0 ? null : (E) data[0];
    }

    @Override
    public E remove() {
        E v = poll();
        if (v == null) throw new NoSuchElementException();
        return v;
    }

    @Override
    public E element() {
        E v = peek();
        if (v == null) throw new NoSuchElementException();
        return v;
    }

    // ================== СОРТИРОВКА ВНУТРИ КУЧИ ==================

    @SuppressWarnings("unchecked")
    private void siftUp(int index) {
        E val = (E) data[index];
        while (index > 0) {
            int parent = (index - 1) >>> 1;
            E p = (E) data[parent];
            if (compare(val, p) >= 0) break;
            data[index] = p;
            index = parent;
        }
        data[index] = val;
    }

    @SuppressWarnings("unchecked")
    private void siftDown(int index) {
        E val = (E) data[index];
        int half = size >>> 1;
        while (index < half) {
            int left = (index << 1) + 1;
            int right = left + 1;
            int smallest = left;
            if (right < size && compare((E) data[right], (E) data[left]) < 0)
                smallest = right;
            if (compare((E) data[smallest], val) >= 0)
                break;
            data[index] = data[smallest];
            index = smallest;
        }
        data[index] = val;
    }

    // ================== КОЛЛЕКЦИОННЫЕ МЕТОДЫ ==================

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        Arrays.fill(data, 0, size, null);
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++)
            if (Objects.equals(data[i], o)) return true;
        return false;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(data[i], o)) {
                data[i] = data[--size];
                data[size] = null;
                if (i < size) siftDown(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
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
    public boolean removeAll(Collection<?> c) {
        Set<?> removeSet = new HashSet<>(c);
        int write = 0;
        for (int i = 0; i < size; i++)
            if (!removeSet.contains(data[i])) data[write++] = data[i];
        Arrays.fill(data, write, size, null);
        if (write != size) {
            size = write;
            heapify();
            return true;
        }
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Set<?> keep = new HashSet<>(c);
        int write = 0;
        for (int i = 0; i < size; i++)
            if (keep.contains(data[i])) data[write++] = data[i];
        Arrays.fill(data, write, size, null);
        if (write != size) {
            size = write;
            heapify();
            return true;
        }
        return false;
    }

    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) siftDown(i);
    }

    // ================== ИТЕРАТОРЫ / ВСПОМОГАТЕЛЬНОЕ ==================

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            int cursor = 0;
            @Override public boolean hasNext() { return cursor < size; }
            @Override @SuppressWarnings("unchecked")
            public E next() {
                if (cursor >= size) throw new NoSuchElementException();
                return (E) data[cursor++];
            }
        };
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(data, size);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            return (T[]) Arrays.copyOf(data, size, a.getClass());
        System.arraycopy(data, 0, a, 0, size);
        if (a.length > size) a[size] = null;
        return a;
    }

    // ================== toString / equals / hashCode ==================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(data[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Queue)) return false;
        Queue<?> that = (Queue<?>) o;
        if (that.size() != this.size) return false;
        Iterator<?> it = that.iterator();
        for (E e : this)
            if (!Objects.equals(e, it.next())) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int h = 1;
        for (E e : this)
            h = 31 * h + (e == null ? 0 : e.hashCode());
        return h;
    }
}