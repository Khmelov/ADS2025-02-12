package by.it.group451001.klevko.lesson11;

import by.it.group451001.klevko.lesson10.MyLinkedList;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/*              size()
                clear()
                isEmpty()
                add(Object)
                remove(Object)
                contains(Object)*/

public class MyHashSet<E> implements Set<E> {
    private Node<E>[] backet = new Node[16];
    private int size = 0;
    class Node<E>{
        E data;
        Node<E> next;
        Node(){
            this.data = null;
            this.next = null;
        }
        Node(E e){
            this.data = e;
            this.next = null;
        }
        Node(E e, Node<E> next){
            this.data = e;
            this.next = next;
        }
    }

    private void addToHash(E e){
        int hashCode = Math.abs(e.hashCode()) % backet.length;
        Node<E> temp = backet[hashCode];
        if (temp == null) {
            backet[hashCode] = new Node<>(e);
        }
        else {
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = new Node<>(e);
        }
        size++;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder ans = new StringBuilder("[");
        for (int i = 0; i < backet.length; i++){
            Node<E> temp = backet[i];
            while (temp != null) {
                ans.append(temp.data).append(", ");
                temp = temp.next;
            }
        }
        ans.setLength(ans.length() - 2);
        ans.append("]");
        return ans.toString();
    }

    //вызывается после добавления элемента, проверяет надо ли rehash and do it if necessary
    private void rehash(){
        if ((double) size / backet.length > 0.75) {
            Node<E>[] lastBacket = backet;
            backet = new Node[lastBacket.length*2];
            size = 0;
            for (int i = 0; i < lastBacket.length; i++){
                Node<E> temp = lastBacket[i];
                while (temp != null) {
                    addToHash(temp.data);
                    temp = temp.next;
                }
            }
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
    public boolean contains(Object o) {
        int hashCode = Math.abs(o.hashCode()) % backet.length;
        Node<E> temp = backet[hashCode];
        while (temp != null){
            if (temp.data.equals(o)) return true;
            temp = temp.next;
        }
        return false;
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
        if (this.contains(e)) return false;
        addToHash(e);
        rehash();
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hashCode = Math.abs(o.hashCode()) % backet.length;
        Node<E> lastTemp, temp = backet[hashCode];
        if (temp != null) {
            if (temp.data.equals(o)){
                backet[hashCode] = temp.next;
                size--;
                return true;
            } else {
                lastTemp = temp;
                temp = temp.next;
                while (temp != null) {
                    if (temp.data.equals(o)) {
                        lastTemp.next = temp.next;
                        size--;
                        return true;
                    }
                    temp = temp.next;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        size = 0;
        Arrays.fill(backet, null);
    }
}
