package by.it.group410901.zdanovich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E extends Comparable<E>> implements Set<E> {

    // Узел бинарного дерева поиска
    class Node {
        E data;      // значение
        Node left;   // левый потомок
        Node right;  // правый потомок

        Node(E data) {
            this.data = data;
        }
    }

    // Печать множества в отсортированном порядке (in-order обход дерева)
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        inOrderTraversal(_root, sb);
        return sb.append("]").toString();
    }

    // Симметричный обход (лево → корень → право)
    void inOrderTraversal(Node node, StringBuilder sb) {
        if (node == null) return;
        inOrderTraversal(node.left, sb);
        if (sb.length() > 1)
            sb.append(", ");
        sb.append(node.data);
        inOrderTraversal(node.right, sb);
    }

    Node _root;   // корень дерева
    int _count;   // количество элементов

    @Override
    public int size() {
        return _count;
    }

    @Override
    public boolean isEmpty() {
        return _count == 0;
    }

    // Проверка, содержится ли элемент в поддереве
    boolean contains(Node node, E element) {
        if (node == null) return false;
        int compare = element.compareTo(node.data);
        if (compare < 0)
            return contains(node.left, element);
        else if (compare > 0)
            return contains(node.right, element);
        else
            return true; // совпадение
    }

    @Override
    public boolean contains(Object o) {
        return contains(_root, (E) o);
    }

    // Вставка элемента в дерево (рекурсивно)
    Node insert(Node node, E element) {
        if (node == null)
            return new Node(element); // создаём новый узел
        int compare = element.compareTo(node.data);
        if (compare < 0)
            node.left = insert(node.left, element);
        else if (compare > 0)
            node.right = insert(node.right, element);
        return node;
    }

    @Override
    public boolean add(E e) {
        if (!contains(e)) { // уникальность
            _root = insert(_root, e);
            _count++;
            return true;
        }
        return false;
    }

    // Поиск минимального узла (самый левый)
    Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Удаление узла из дерева
    Node delete(Node node, E element) {
        if (node == null) return null;
        int compare = element.compareTo(node.data);
        if (compare < 0) {
            node.left = delete(node.left, element);
        } else if (compare > 0) {
            node.right = delete(node.right, element);
        } else {
            // Узел найден
            if (node.left == null) {
                return node.right; // если нет левого поддерева
            } else if (node.right == null) {
                return node.left;  // если нет правого поддерева
            }
            // узел с двумя потомками — заменяем минимальным справа
            node.data = findMin(node.right).data;
            node.right = delete(node.right, node.data);
        }
        return node;
    }

    @Override
    public boolean remove(Object o) {
        if (contains(o)) {
            _root = delete(_root, (E) o);
            _count--;
            return true;
        }
        return false;
    }

    // Проверка: содержатся ли все элементы коллекции
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object obj: c) {
            if (!contains(obj))
                return false;
        }
        return true;
    }

    // Добавление всех элементов коллекции
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean isModified = false;
        for (E element: c) {
            if (add(element))
                isModified = true;
        }
        return isModified;
    }

//Оставляем только элементы, которые есть в другой коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        if (c.isEmpty()) {
            this.clear();
            return true;
        }
        boolean isModified = false;
        MyTreeSet<E> retainSet = new MyTreeSet<>();
        for (Object obj : c) {
            if (contains(obj)) {
                retainSet.add((E) obj);
                isModified = true;
            }
        }
        // Переприсваиваем дерево
        _root = retainSet._root;
        _count = retainSet._count;

        return isModified;
    }

    // Удаление всех элементов коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean isModified = false;
        for (Object obj: c) {
            if (remove(obj))
                isModified = true;
        }
        return isModified;
    }

    @Override
    public void clear() {
        _root = null;
        _count = 0;
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Методы не реализованы, оставлены заглушки
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
