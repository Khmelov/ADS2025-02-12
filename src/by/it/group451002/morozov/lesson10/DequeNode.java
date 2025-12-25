package by.it.group451002.morozov.lesson10;

public class DequeNode<E> {
    public E value;
    public DequeNode<E> nextNode;
    public DequeNode<E> prevNode;

    public DequeNode (E value) {
        this.value = value;
    }
}