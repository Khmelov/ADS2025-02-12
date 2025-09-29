package by.it.group451001.klevko.lesson11;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/*              toString()
                size()
                clear()
                isEmpty()
                add(Object)
                remove(Object)
                contains(Object)

                containsAll(Collection)
                addAll(Collection)
                removeAll(Collection)
                retainAll(Collection)*/

public class MyLinkedHashSet<E> implements Set<E> {
    private Node<E>[] backet = new Node[16];
    private int size = 0;
    class Node<E>{
        E data;
        Node<E> next;
        Node<E> nextAdd;
        Node<E> lastAdd;
        Node(){
            this.data = null;
            this.next = null;
            this.nextAdd = null;
            this.lastAdd = null;
        }
        Node(E e){
            this.data = e;
            this.next = null;
            this.nextAdd = null;
            this.lastAdd = null;
        }
        /*Node(Node<E> nextAdd, Node<E> lastAdd){
            this.data = null;
            this.next = null;
            this.nextAdd = nextAdd;
            this.lastAdd = lastAdd;
        }*/
    }
    Node<E> head = new Node<>(), last = new Node<>();

    MyLinkedHashSet() {
        head.nextAdd = last;
        last.lastAdd = head;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder ans = new StringBuilder("[");
        Node<E> temp = head.nextAdd;
        while (temp != last){
            ans.append(temp.data).append(", ");
            temp = temp.nextAdd;
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
            for (int i = 0; i < lastBacket.length; i++){
                Node<E> temp = lastBacket[i], nextE;
                while (temp != null) {
                    nextE = temp.next;
                    int hashCode = Math.abs(temp.data.hashCode()) % backet.length;
                    Node<E> temp2 = backet[hashCode];
                    if (temp2 == null) {
                        backet[hashCode] = temp;
                        temp.next = null;
                    } else {
                        while (temp2.next != null) temp2 = temp2.next;
                        temp2.next = temp;
                        temp.next = null;
                    }
                    temp = nextE;
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
        int hashCode = Math.abs(e.hashCode()) % backet.length;
        Node<E> temp = backet[hashCode];
        if (temp == null) {
            backet[hashCode] = new Node<>(e);
            temp = backet[hashCode];
        } else {
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = new Node<>(e);
            temp = temp.next;
        }
        size++;
        last.lastAdd.nextAdd = temp;
        temp.lastAdd = last.lastAdd;
        temp.nextAdd = last;
        last.lastAdd = temp;
        rehash();
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            changed |= add(e);
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean ans = false;
        for (int i = 0; i < backet.length; i++){
            Node<E> temp = backet[i], nextE;
            while (temp != null) {
                nextE = temp.next;
                if (!c.contains(temp.data)) {
                    remove(temp.data);
                    ans = true;
                }
                temp = nextE;
            }
        }
        return ans;
    }

    @Override
    public boolean remove(Object o) {
        int hashCode = Math.abs(o.hashCode()) % backet.length;
        Node<E> lastTemp, temp = backet[hashCode];
        if (temp != null) {
            if (temp.data.equals(o)){
                backet[hashCode] = temp.next;
                temp.lastAdd.nextAdd = temp.nextAdd;
                temp.nextAdd.lastAdd = temp.lastAdd;
                size--;
                return true;
            } else {
                lastTemp = temp;
                temp = temp.next;
                while (temp != null) {
                    if (temp.data.equals(o)) {
                        lastTemp.next = temp.next;
                        temp.lastAdd.nextAdd = temp.nextAdd;
                        temp.nextAdd.lastAdd = temp.lastAdd;
                        size--;
                        return true;
                    }
                    lastTemp = temp;
                    temp = temp.next;
                }
            }
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean ans = false;
        for (int i = 0; i < backet.length; i++){
            Node<E> temp = backet[i], nextE;
            while (temp != null) {
                nextE = temp.next;
                if (c.contains(temp.data)) {
                    remove(temp.data);
                    ans = true;
                }
                temp = nextE;
            }
        }
        return ans;
    }

    @Override
    public void clear() {
        size = 0;
        head.nextAdd = last;
        last.lastAdd = head;
        Arrays.fill(backet, null);
    }
}