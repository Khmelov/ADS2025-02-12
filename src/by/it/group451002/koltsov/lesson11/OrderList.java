package by.it.group451002.koltsov.lesson11;

public class OrderList<E> {
    int size = 0;
    Node<E> head = null;
    Node<E> tail = null;

    public OrderList() {
        head = new Node<E>();
        tail = new Node<E>();
        head.next = tail;
        tail.prev = head;
    }

    // Вставка узла в конец порядкового списка (без доп проверок)
    public void add(Node<E> node) {
        Node<E> tempNode = new Node<E>();
        tempNode.orderListElem = node;
        node.orderListElem = tempNode;

        tempNode.next = tail;
        tempNode.prev = tail.prev;
        tail.prev.next = tempNode;
        tail.prev = tempNode;
        size++;
    }

    // Удаление элемента из порядкоговго списка
    public void remove(Node<E> node) {
        Node<E> tempNode = node.orderListElem;
        tempNode.prev.next = tempNode.next;
        tempNode.next.prev = tempNode.prev;
        size--;
    }

    public void clear() {
        head.next = tail;
        tail.prev = head;
        size = 0;
    }
}
