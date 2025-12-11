package by.it.group451001.puzik.lesson11;

import java.util.*;

@SuppressWarnings("unchecked")
public class MyTreeSet<E> implements Set<E> {

    private Object[] elements = new Object[8];
    private int size = 0;

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append('[');
        for (int i = 0; i < size; i++) {
            if (i > 0) out.append(", ");
            out.append(elements[i]);
        }
        out.append(']');
        return out.toString();
    }

    @Override public int size() { return size; }
    @Override public void clear() { Arrays.fill(elements, 0, size, null); size = 0; }
    @Override public boolean isEmpty() { return size == 0; }

    @Override
    public boolean add(E e) {
        int i = findIndex(e);
        if (i >= 0) return false; // exists
        int insertPos = -i - 1;
        ensureCapacity(size + 1);
        System.arraycopy(elements, insertPos, elements, insertPos + 1, size - insertPos);
        elements[insertPos] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int i = findIndex((E) o);
        if (i < 0) return false;
        int numMoved = size - i - 1;
        if (numMoved > 0) System.arraycopy(elements, i + 1, elements, i, numMoved);
        elements[--size] = null;
        return true;
    }

    @Override
    public boolean contains(Object o) { return findIndex((E) o) >= 0; }

    // ---------- helpers ----------
    private int findIndex(E key) {
        int lo = 0, hi = size - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int cmp = compare((E) elements[mid], key);
            if (cmp < 0) lo = mid + 1; else if (cmp > 0) hi = mid - 1; else return mid;
        }
        return -(lo + 1);
    }

    private int compare(E a, E b) { return ((Comparable<? super E>) a).compareTo(b); }

    private void ensureCapacity(int min) { if (elements.length < min) elements = Arrays.copyOf(elements, Math.max(min, elements.length * 2)); }

    // ---- bulk ops ----
    @Override public boolean containsAll(Collection<?> c) { for(Object o:c) if(!contains(o)) return false; return true; }
    @Override public boolean addAll(Collection<? extends E> c) { boolean ch=false; for(E e:c) if(add(e)) ch=true; return ch; }
    @Override public boolean removeAll(Collection<?> c) { boolean ch=false; for(Object o:c) if(remove(o)) ch=true; return ch; }
    @Override public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        int w = 0;
        for (int r = 0; r < size; r++) {
            if (c.contains(elements[r])) elements[w++] = elements[r]; else changed = true;
        }
        Arrays.fill(elements, w, size, null);
        size = w;
        return changed;
    }

    // ---- boilerplate unsupported ----
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { return Arrays.copyOf(elements, size); }
    @Override public <T> T[] toArray(T[] a) { if(a.length<size) a=Arrays.copyOf(a,size); for(int i=0;i<size;i++){ a[i]=(T)elements[i]; } if(a.length>size) a[size]=null; return a; }
}


