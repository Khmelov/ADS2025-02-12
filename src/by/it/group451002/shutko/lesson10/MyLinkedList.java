package by.it.group451002.shutko.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;


// Создать собственную реализацию двусторонней очереди (Deque) на основе двусвязного
// списка без использования стандартных библиотек Java.
public class MyLinkedList<E> implements Deque<E> {
    int size = 0;

    private static class Node<E> {
        // data - хранимые данные
        E data;
        // previous - ссылка на предыдущий узел
        Node<E> previous;
        // next - ссылка на следующий узел
        Node<E> next;

        private Node(E data) {
            this.data = data;
            this.next = null;
            this.previous = null;
        }
    }
    // указатель на первый элемент
    Node<E> first;
    // указатель на последний элемент
    Node<E> last;

    // формирует строковое представление списка
    public String toString() {
        StringBuilder res = new StringBuilder("[");

        for (Node<E> tmpNode = first; tmpNode != null; tmpNode = tmpNode.next) {
            res.append(tmpNode.data.toString()).append(", ");
        }

        if (first != null) {
            res.delete(res.length() - 2, res.length());
        }

        return res.append(']').toString();
    }

    @Override
    // добавление элемента в начало
    public void addFirst(E e) {
        // Создается новый узел
        // Если список пуст: новый_узел
        // Если не пуст: новый узел становится перед первым, обновляются указатели
        Node<E> tmpNode = new Node<E>(e);
        if (first == null) {
            first = tmpNode;
            last = tmpNode;
        } else {
            tmpNode.next = first;
            first.previous = tmpNode;
            first = tmpNode;
        }
        size++;
    }

    @Override
    // добавление элемента в конец
    public void addLast(E e) {
        // Создается новый узел
        // Если список пуст: новый_узел
        // Если не пуст: новый узел становится после последнего, обновляются указатели
        Node<E> tmpNode = new Node<E>(e);
        if (first == null) {
            first = tmpNode;
            last = tmpNode;
        } else {
            tmpNode.previous = last;
            last.next = tmpNode;
            last = tmpNode;
        }
        size++;
    }

    @Override
    // просто вызывает addLast(e)
    public boolean add(E e) {
        addLast(e);
        return true;
    }


    @Override
    // удаление первого элемента
    public E removeFirst() {
        // Сохраняем данные первого элемента
        // Перенаправляем first на второй элемент
        // Обнуляем ссылку previous у нового первого элемента
        // возвращаем информацию о 1 элементе
        Node<E> tmpNode = first;
        first = first.next;
        first.previous = null;
        size--;
        return tmpNode.data;
    }

    @Override
    // удаление последнего элемента
    public E removeLast() {
        // Сохраняем данные последнего элемента
        // Перенаправляем last на предпоследний элемент
        // Обнуляем ссылку next у нового последнего элемента
        // возвращаем информацию о последнем элементе
        Node<E> tmpNode = last;
        last = last.previous;
        last.next = null;
        size--;
        return tmpNode.data;
    }

    @Override
    // удаление первого элемента (removeFirst() )
    public E remove() {
        return removeFirst();
    }

    // удаление по индексу
    public E remove(int index) {
        // Находим узел по индексу
        // "Сшиваем" предыдущий и следующий узлы между собой (меняем их указатели)
        // Обходимся без сдвига элементов как в массиве
        Node<E> tmpNode = first;
        for (int i = 0; i < index && tmpNode != null; i++)
            tmpNode = tmpNode.next;
        if (tmpNode != null) {
            Node<E> tmpP = tmpNode.previous;
            Node<E> tmpN = tmpNode.next;
            if (tmpP != null)
                tmpP.next = tmpN;
            if (tmpN != null)
                tmpN.previous = tmpP;
            size--;
            return tmpNode.data;
        }
        return null;
    }

    @Override
    public E element() {
        return getFirst();
    }


    @Override
    // возвращает данные первого узла
    public E getFirst() {
        return first.data;
    }

    @Override
    // возвращает данные последнего узла
    public E getLast() {
        return last.data;
    }


    @Override
    // удаляют и возвращают первый элемент (идентичен poll() )
    public E pollFirst() {
        return removeFirst();
    }

    @Override
    // удаляет и возвращает последний элемент
    public E pollLast() {
        return removeLast();
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    // возвращает количество элементов
    public int size() {
        return size;
    }

    @Override
    // удаление по значению
    public boolean remove(Object o) {
        // Линейный поиск узла с нужным значением
        // "Сшиваем" предыдущий и следующий узлы (меняя указатели)
        Node<E> tmpNode = first;
        while (tmpNode != null && !o.equals(tmpNode.data))
            tmpNode = tmpNode.next;
        if (tmpNode != null) {
            Node<E> tmpP = tmpNode.previous;
            Node<E> tmpN = tmpNode.next;
            if (tmpP != null)
                tmpP.next = tmpN;
            if (tmpN != null)
                tmpN.previous = tmpP;
            size--;
            return true;
        }
        return false;
    }

    @Override
    public boolean offerFirst(E e) {
        return false;
    }

    @Override
    public boolean offerLast(E e) {
        return false;
    }

    @Override
    public E peekFirst() {
        return null;
    }

    @Override
    public E peekLast() {
        return null;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E peek() {
        return null;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public void push(E e) {

    }

    @Override
    public E pop() {
        return null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public boolean isEmpty() {
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
    public Iterator<E> descendingIterator() {
        return null;
    }
}

