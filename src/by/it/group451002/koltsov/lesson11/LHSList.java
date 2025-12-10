package by.it.group451002.koltsov.lesson11;

import java.util.Collection;

public class LHSList<E> {
    int size = 0;
    Node<E> head = null;

    public LHSList() {
        head = new Node<>();
        head.next = null;
    }

    // Добавление элемента в начало списка
    public boolean add(E value, OrderList<E> orderList) {
        if (contains(value))
            return false;
        Node<E> tempNode = new Node<E>();
        tempNode.value = value;
        tempNode.next = head.next;
        head.next = tempNode;
        orderList.add(tempNode);
        size++;
        return true;
    }

    // Удаляет элемент из списка и возвращает true, если состав элементов был изменён, иначе false
    public boolean remove(E value, OrderList<E> orderList) {
        Node<E> tempNode = head;
        Node<E> prevNode = head;

        for (int i = 0; i < size; i++) {
            tempNode = tempNode.next;
            if (tempNode.value.equals(value)) {
                orderList.remove(tempNode);
                prevNode.next = tempNode.next;
                size--;
                return true;
            }
            prevNode = tempNode;
        }
        return false;
    }

    public boolean contains(E value) {
        Node<E> tempNode = head;

        for (int i = 0; i < size; i++) {
            tempNode = tempNode.next;
            if (tempNode.value.equals(value))
                return true;
        }
        return false;
    }

    public void clear() {
        size = 0;
        head.next = null;
    }
}

