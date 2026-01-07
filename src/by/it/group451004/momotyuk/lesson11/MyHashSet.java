package by.it.group451004.momotyuk.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    
    private Node<E>[] table;
    private int size;
    
    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }
    
    // Внутренний класс для узла связного списка
    private static class Node<E> {
        E data;
        Node<E> next;
        
        Node(E data) {
            this.data = data;
        }
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
    @SuppressWarnings("unchecked")
    public void clear() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }
    
    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        
        int index = getIndex(o);
        Node<E> current = table[index];
        
        while (current != null) {
            if (o.equals(current.data)) {
                return true;
            }
            current = current.next;
        }
        
        return false;
    }
    
    @Override
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException("Null elements are not allowed");
        }
        
        if (contains(e)) {
            return false;
        }
        
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
        
        int index = getIndex(e);
        Node<E> newNode = new Node<>(e);
        
        // Добавляем в начало списка
        newNode.next = table[index];
        table[index] = newNode;
        size++;
        
        return true;
    }
    
    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        
        int index = getIndex(o);
        Node<E> current = table[index];
        Node<E> prev = null;
        
        while (current != null) {
            if (o.equals(current.data)) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        
        return false;
    }
    
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        
        StringBuilder sb = new StringBuilder("[");
        int count = 0;
        
        for (Node<E> node : table) {
            Node<E> current = node;
            while (current != null) {
                sb.append(current.data);
                count++;
                if (count < size) {
                    sb.append(", ");
                }
                current = current.next;
            }
        }
        
        sb.append("]");
        return sb.toString();
    }
    
    // Вспомогательные методы
    
    private int getIndex(Object o) {
        return Math.abs(o.hashCode()) % table.length;
    }
    
    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        size = 0;
        
        for (Node<E> node : oldTable) {
            Node<E> current = node;
            while (current != null) {
                add(current.data);
                current = current.next;
            }
        }
    }
    
    // Методы интерфейса Set, которые не требуются по заданию, но должны быть реализованы
    
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }
    
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            if (!c.contains(iterator.next())) {
                iterator.remove();
                modified = true;
            }
        }
        return modified;
    }
    
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object element : c) {
            if (remove(element)) {
                modified = true;
            }
        }
        return modified;
    }
}