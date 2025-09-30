package by.it.group410901.borisdubinin.lesson09;

import java.util.*;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    private int size;
    private Node head;
    private Node tail;

    private class Node{
        private E elem;

        public Node prev;
        public Node next;

        public Node(E elem){
            this.elem = elem;
        }
        public Node(E elem, Node prev, Node next){
            this.elem = elem;
            this.next = next;
            this.prev = prev;
        }
    }

    private Node getNode(int index){
        Node ptr;
        if (index <= size /2) {
            ptr = head;
            for (; index > 0; index--)
                ptr = ptr.next;
        } else {
            ptr = tail;
            for (int tail_index = size - 1; tail_index > index; tail_index--)
                ptr = ptr.prev;
        }
        return ptr;
    }

    public ListC(){
        size = 0;
        head = null;
        tail = null;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(size * 16);
        sb.append("[");

        for(Node ptr = head; ptr != null; ptr = ptr.next){
            sb.append(ptr.elem);
            if(ptr.next != null)
                sb.append(", ");
        }
        sb.append("]");

        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        if (size == 0){
            head = new Node(e, null, null);
            tail = head;
        }
        else {
            tail = new Node(e, tail, null);
            tail.prev.next = tail;
        }
        size++;

        return true;
    }

    @Override
    public E remove(int index) {
        if(index < 0 || index >= size)
            return null;

        Node ptr;
        if(size == 1){
            ptr = head;
            head = tail = null;
        }
        else if(index == 0){
            ptr = head;
            head = head.next;
            head.prev = null;
        }
        else if(index == size -1){
            ptr = tail;
            tail = tail.prev;
            tail.next = null;
        }
        else {
            ptr = getNode(index);
            ptr.prev.next = ptr.next;
            ptr.next.prev = ptr.prev;
        }

        size--;
        return ptr.elem;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
	if(index < 0 || index > size)
            return;
        if(size == 0 || index == size){
            add(element);
            return;
        }

        if(index == 0){
            Node ptr = new Node(element, null, head);
            head.prev = ptr;
            head = ptr;
        }
        else {
            Node ptr = getNode(index);
            Node newNode = new Node(element, ptr.prev, ptr);
            newNode.next.prev = newNode;
            newNode.prev.next = newNode;
        }

        size++;
    }

    @Override
    public boolean remove(Object o) {
        Node ptr = head;

        while (ptr != null) {
            if (Objects.equals(o, ptr.elem)) {
                if (ptr == head) {
                    head = head.next;
                    if (head != null) head.prev = null;
                    else tail = null;
                }
                else if (ptr == tail) {
                    tail = tail.prev;
                    tail.next = null;
                }
                else {
                    ptr.prev.next = ptr.next;
                    ptr.next.prev = ptr.prev;
                }
                size--;
                return true;
            }
            ptr = ptr.next;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if(index < 0 || index >= size)
            return null;
        Node ptr = getNode(index);
        E oldElem = ptr.elem;
        ptr.elem = element;
        return oldElem;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        Node ptr = head;
        while (ptr != null) {
            Node next = ptr.next;
            ptr.prev = null;
            ptr.next = null;
            ptr.elem = null; // Опционально
            ptr = next;
        }
        head = tail = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        int index = 0;
        Node ptr = head;

        if (o == null) {
            while (ptr != null) {
                if (ptr.elem == null) {
                    return index;
                }
                ptr = ptr.next;
                index++;
            }
        } else {
            while (ptr != null) {
                if (o.equals(ptr.elem)) {
                    return index;
                }
                ptr = ptr.next;
                index++;
            }
        }

        return -1;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return getNode(index).elem;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int index = size-1;
        Node ptr = tail;

        if(o != null){
            while(ptr != null){
                if(o.equals(ptr.elem))
                    return index;
                ptr = ptr.prev;
                index--;
            }
        }
        else {
            while(ptr != null){
                if(ptr.elem == null)
                    return index;
                ptr = ptr.prev;
                index--;
            }
        }

        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for(Object item : c){
            if(!contains(item))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if(c == null || c.isEmpty())
            return false;

        for(E item : c){
            add(item);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (c == null) {
            return false;
        }
        if (index < 0 || index > size) {
            return false;
        }
        if (c.isEmpty()) {
            return false;
        }

        Node currentNode = (index == size) ? null : getNode(index);
        Node previousNode = (currentNode != null) ? currentNode.prev : tail;

        for (E item : c) {
            Node newNode = new Node(item, previousNode, null);

            if (previousNode == null) {
                head = newNode;
            } else {
                previousNode.next = newNode;
            }
            previousNode = newNode;
        }

        if (currentNode != null) {
            previousNode.next = currentNode;
            currentNode.prev = previousNode;
        } else {
            tail = previousNode;
        }

        size += c.size();
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) return false;
        if (c.isEmpty()) return false;

        boolean changed = false;
        Node current = head;

        while (current != null) {
            Node next = current.next;

            if (c.contains(current.elem)) {
                if (current == head) {
                    head = head.next;
                    if (head != null) head.prev = null;
                } else if (current == tail) {
                    tail = tail.prev;
                    tail.next = null;
                } else {
                    current.prev.next = current.next;
                    current.next.prev = current.prev;
                }
                size--;
                changed = true;
            }

            current = next;
        }

        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if(c == null) return false;

        boolean changed = false;
        Node current = head;
        Node previous = null;

        while (current != null) {
            Node next = current.next;

            if (!c.contains(current.elem)) {
                if (previous == null) {
                    head = current.next;
                    if (head != null) {
                        head.prev = null;
                    }
                } else {
                    previous.next = current.next;
                    if (current.next != null) {
                        current.next.prev = previous;
                    }
                }

                if (current == tail) {
                    tail = previous;
                }

                current.prev = null;
                current.next = null;

                size--;
                changed = true;
            } else {
                previous = current;
            }

            current = next;
        }

        return changed;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
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
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
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
