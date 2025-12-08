package lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    E[] elements = (E[]) (new Object[10]);
    int size = 0;
    @Override
    public int size() {
        return size;
    }

    private void grow(){
        E[] newElements = (E[]) (new Object[elements.length * 2]);
        System.arraycopy(elements, 0, newElements, 0, elements.length);
        elements = newElements;
    }
    private int find(E e){
        for (int i = 0; i < size; i++) {
            if(elements[i].equals(e)){
                return i;
            }
        }
        return -1;
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("[");
        if(size > 0){
            sb.append(elements[0]);
        }
        for(int i = 1; i < size; i++){
            sb.append(", ").append(elements[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return find((E) o) != -1;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(E e) {
        int index = find(e);
        if(index != -1)
            return false;
        if(size == elements.length)
            grow();
        while((++index < size) && (((Comparable<E>)(e)).compareTo(elements[index]) > 0));
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = find((E)(o));
        if(index == -1)
            return false;
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        size--;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        boolean result = true;
        for (Object o : c) {
            result &= contains(o);
        }
        return result;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
       boolean result = false;
       for (E e : c) {
           result |= add(e);
       }
       return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean result = false;
        for (int i = 0; i < size; i++){
            if(!c.contains(elements[i])){
                result |= remove(elements[i]);
                i--;
            }
        }
        return result;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = false;
        for (Object e : c) {
            result |= remove(e);
        }
        return result;
    }

    @Override
    public void clear() {
        elements = (E[]) (new Object[10]);
        size = 0;

    }
}
