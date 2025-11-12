package by.it.group410902.jalilova.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E extends Comparable<E>> implements Set<E> {

    // узел дерева, хранит значение и ссылки на левого и правого потомка
    class Node {
        E data; // значение элемента
        Node left; // левый потомок (меньше текущего)
        Node right; // правый потомок (больше текущего)

        Node(E data) {
            this.data = data;
        }
    }

    Node _root; // корень дерева
    int _count; // количество элементов в дереве

    // возвращает строковое представление множества в порядке возрастания
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        inOrderTraversal(_root, sb); // обход дерева слева-направо
        return sb.append("]").toString();
    }

    // обход дерева в порядке возрастания (in-order)
    void inOrderTraversal(Node node, StringBuilder sb) {
        if (node == null) return;
        inOrderTraversal(node.left, sb); // сначала левое поддерево
        if (sb.length() > 1) // если не первый элемент, добавляем запятую
            sb.append(", ");
        sb.append(node.data); // добавляем текущий элемент
        inOrderTraversal(node.right, sb); // потом правое поддерево
    }

    @Override
    public int size() {
        return _count; // возвращаем количество элементов
    }

    @Override
    public boolean isEmpty() {
        return _count == 0; // проверяем, пусто ли дерево
    }

    // рекурсивный поиск элемента в дереве
    boolean contains(Node node, E element) {
        if (node == null) return false; // достигли конца поддерева
        int compare = element.compareTo(node.data);
        if (compare < 0)
            return contains(node.left, element); // ищем в левом поддереве
        else if (compare > 0)
            return contains(node.right, element); // ищем в правом поддереве
        else
            return true; // нашли элемент
    }

    @Override
    public boolean contains(Object o) {
        return contains(_root, (E) o); // вызываем поиск от корня
    }

    // рекурсивная вставка элемента в дерево
    Node insert(Node node, E element) {
        if (node == null)
            return new Node(element); // создаем новый узел, если достигли null
        int compare = element.compareTo(node.data);
        if (compare < 0)
            node.left = insert(node.left, element); // вставка в левое поддерево
        else if (compare > 0)
            node.right = insert(node.right, element); // вставка в правое поддерево
        return node;
    }

    @Override
    public boolean add(E e) {
        if (!contains(e)) { // проверяем, что элемента еще нет
            _root = insert(_root, e); // вставляем элемент в дерево
            _count++;
            return true;
        }
        return false; // элемент уже существует
    }

    // находим минимальный элемент в поддереве (для удаления)
    Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // рекурсивное удаление элемента из дерева
    Node delete(Node node, E element) {
        if (node == null) return null;
        int compare = element.compareTo(node.data);
        if (compare < 0) {
            node.left = delete(node.left, element); // ищем в левом поддереве
        } else if (compare > 0) {
            node.right = delete(node.right, element); // ищем в правом поддереве
        } else { // нашли элемент для удаления
            if (node.left == null) { // только правое поддерево
                return node.right;
            } else if (node.right == null) { // только левое поддерево
                return node.left;
            }
            // если оба поддерева существуют, заменяем элемент на минимальный из правого поддерева
            node.data = findMin(node.right).data;
            node.right = delete(node.right, node.data); // удаляем этот минимальный элемент
        }
        return node;
    }

    @Override
    public boolean remove(Object o) {
        if (contains(o)) {
            _root = delete(_root, (E) o); // удаляем элемент
            _count--;
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object obj: c) {
            if (!contains(obj)) // если хотя бы одного нет
                return false;
        }
        return true; // все элементы есть
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean isModified = false;
        for (E element: c) {
            if (add(element)) // если элемент реально добавлен
                isModified = true;
        }
        return isModified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c.isEmpty()) { // если коллекция пустая, очищаем дерево
            this.clear();
            return true;
        }
        boolean isModified = false;
        MyTreeSet<E> retainSet = new MyTreeSet<>();
        for (Object obj : c) {
            if (contains(obj)) { // оставляем только элементы, которые есть в c
                retainSet.add((E) obj);
                isModified = true;
            }
        }
        _root = retainSet._root; // заменяем дерево на пересечение
        _count = retainSet._count;
        return isModified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean isModified = false;
        for (Object obj: c) {
            if (remove(obj)) // если элемент реально удален
                isModified = true;
        }
        return isModified;
    }

    @Override
    public void clear() {
        _root = null; // очищаем дерево
        _count = 0;
    }

    //////////////////////////////////////////////////////////////////////////////////////

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
