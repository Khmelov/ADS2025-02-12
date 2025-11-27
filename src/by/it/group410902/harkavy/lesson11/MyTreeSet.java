package by.it.group410902.harkavy.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    @SuppressWarnings("unchecked")
    private E[] a = (E[]) new Object[8]; // отсортированный массив
    private int size = 0;

    // сравнение по natural order (элементы должны быть Comparable)
    @SuppressWarnings("unchecked")
    private int cmp(E x, E y) { return ((Comparable<? super E>) x).compareTo(y); }

    @SuppressWarnings("unchecked")
    private void growIfNeeded() {
        if (size < a.length) return;
        E[] b = (E[]) new Object[a.length << 1];
        for (int i = 0; i < size; i++) b[i] = a[i];
        a = b;
    }

    // двоичный поиск по отсортированному массиву
    // возвращает индекс, если найден; иначе -(позиция_вставки + 1)
    private int binarySearch(Object key) {
        @SuppressWarnings("unchecked") E k = (E) key;
        int lo = 0, hi = size - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int c = cmp(a[mid], k);
            if (c < 0) lo = mid + 1;
            else if (c > 0) hi = mid - 1;
            else return mid;
        }
        return -(lo + 1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(a[i]);
            if (i + 1 < size) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) a[i] = null; // освобождаем ссылки
        size = 0;
    }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException(); // сортировка требует Comparable
        int idx = binarySearch(e);
        if (idx >= 0) return false; // уже есть
        int ins = -idx - 1;         // позиция вставки
        growIfNeeded();
        // сдвигаем вправо и вставляем
        for (int i = size; i > ins; i--) a[i] = a[i - 1];
        a[ins] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        int idx = binarySearch(o);
        if (idx < 0) return false;
        // сдвигаем влево, затирая удаляемый элемент
        for (int i = idx; i < size - 1; i++) a[i] = a[i + 1];
        a[--size] = null;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        int idx = binarySearch(o);
        return idx >= 0;
    }

    // -------- массовые операции --------

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object x : c) if (!contains(x)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E x : c) if (add(x)) changed = true;
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object x : c) if (remove(x)) changed = true;
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        // идём по нашему массиву слева направо и удаляем то, чего нет в c
        for (int i = 0; i < size; i++) {
            if (!c.contains(a[i])) {
                remove(a[i]);
                i--;        // остаёмся на той же позиции, т.к. элементы сдвинулись
                changed = true;
            }
        }
        return changed;
    }

    // -------- остальное не требуется заданием --------
    private void unsupported() { throw new UnsupportedOperationException("Not required by the assignment"); }
    @Override public Iterator<E> iterator() { unsupported(); return null; }
    @Override public Object[] toArray() { unsupported(); return null; }
    @Override public <T> T[] toArray(T[] arr) { unsupported(); return null; }
}
