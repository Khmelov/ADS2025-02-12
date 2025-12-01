package by.it.group451002.koltsov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    final int arrSize = 100;
    int size = 0;
    Object elems[] = new Object[arrSize];
    OrderList<E> orderList;

    public MyLinkedHashSet() {
        for (int i = 0; i < arrSize; i++) {
            elems[i] = new LHSList<E>();
        }
        orderList = new OrderList<E>();
    }

    // Методы, необходимые для реализации

    @Override
    public String toString() {
        boolean isElemAdded = false;
        StringBuilder resStr = new StringBuilder("[");
        Node<E> tempNode = orderList.head;
        for (int i = 0; i < orderList.size; i++) {
            isElemAdded = true;
            tempNode = tempNode.next;
            resStr.append(tempNode.orderListElem.value.toString());
            resStr.append(", ");
        }

        if (isElemAdded) {
            resStr.deleteCharAt(resStr.length() - 1);
            resStr.deleteCharAt(resStr.length() - 1);
        }
        resStr.append("]");

        return resStr.toString();
    }

    @Override
    public int size() {
        return size;
    }

    // + очистить список из всех элементов
    @Override
    public void clear() {
        for (int i = 0; i < elems.length; i++) {
            ((LHSList<E>)elems[i]).clear();
        }
        size = 0;
        orderList.clear();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // + добавить информацию в список элементов
    @Override
    public boolean add(E e) {
        if (((LHSList<E>)elems[hash(e)]).add(e, orderList))
        {
            size++;
            return true;
        }
        return false;
    }

    // + удалить элемент из списка всех элементов
    @Override
    public boolean remove(Object o) {
        if (((LHSList<E>)elems[hash((E)o)]).remove((E)o, orderList))
        {
            size--;
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return ((LHSList<E>)elems[hash((E)o)]).contains((E)o);
    }

    public int hash(E value) {
        int hash = value.hashCode() % arrSize;
        return hash;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (!((LHSList<E>) elems[hash((E)o)]).contains((E)o))
                return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean isElemAdded = false;
        for (Object o : c)
            if (add((E)o))
                isElemAdded = true;
        return isElemAdded;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean isElemDeleted = false;
        MyList<E> deleteList = new MyList<E>();
        for (int i = 0; i < arrSize; i++) {
            Node<E> tempNode = ((LHSList<E>) elems[i]).head;
            for (int j = 0; j < ((LHSList<E>) elems[i]).size; j++) {
                tempNode = tempNode.next;
                if (!c.contains(tempNode.value))
                    deleteList.add(tempNode.value);
            }
        }

        int size = deleteList.size;
        for (int i = 0; i < size; i++) {
            if (remove(deleteList.pop()))
                isElemDeleted = true;
        }
        return isElemDeleted;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean isElemRemoved = false;
        for (Object o : c)
            if (remove((E)o))
                isElemRemoved = true;
        return isElemRemoved;
    }

    // Методы дальше можно не реализовывать

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

}
