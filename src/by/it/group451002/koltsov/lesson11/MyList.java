package by.it.group451002.koltsov.lesson11;

public class MyList<E> {
    int size = 0;
    Node<E> head = null;

    public MyList() {
        head = new Node<>();
        head.next = null;
    }

    // Добавление элемента в начало списка
    public boolean add(E value) {
        if (contains(value))
            return false;
        Node<E> tempNode = new Node<E>();
        tempNode.value = value;
        tempNode.next = head.next;
        head.next = tempNode;
        size++;
        return true;
    }

    // Удаляет элемент из списка и возвращает true, если состав элементов был изменён, иначе false
    public boolean remove(E value) {
        Node<E> tempNode = head;
        Node<E> prevNode = head;

        for (int i = 0; i < size; i++) {
            tempNode = tempNode.next;
            if (tempNode.value.equals(value)) {
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

    public E pop() {
        E temp = head.next.value;
        head.next = head.next.next;
        size--;
        return temp;
    }
}
