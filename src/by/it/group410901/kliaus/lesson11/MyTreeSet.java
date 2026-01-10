package by.it.group410901.kliaus.lesson11;

import java.util.*;

/**
 * Простейшая реализация TreeSet без использования встроенных коллекций.
 * Сохраняет элементы в отсортированном порядке (по возрастанию).
 */
public class MyTreeSet implements Collection<Number> {

    private Node root;
    private int size = 0;

    public MyTreeSet() {
    }

    private static class Node {
        Number value;
        Node left;
        Node right;

        Node(Number value) {
            this.value = value;
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

    private int compare(Number a, Number b) {
        double diff = a.doubleValue() - b.doubleValue();
        if (diff < 0) return -1;
        if (diff > 0) return 1;
        return 0;
    }

    @Override
    public boolean contains(Object o) {
        if (!(o instanceof Number n)) return false;
        Node node = root;
        while (node != null) {
            int cmp = compare(n, node.value);
            if (cmp == 0) return true;
            node = (cmp < 0) ? node.left : node.right;
        }
        return false;
    }

    @Override
    public boolean add(Number number) {
        if (root == null) {
            root = new Node(number);
            size++;
            return true;
        }
        Node current = root;
        while (true) {
            int cmp = compare(number, current.value);
            if (cmp == 0) return false; // уже есть
            else if (cmp < 0) {
                if (current.left == null) {
                    current.left = new Node(number);
                    size++;
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new Node(number);
                    size++;
                    return true;
                }
                current = current.right;
            }
        }
    }

    @Override
    public boolean remove(Object o) {
        if (!(o instanceof Number n)) return false;
        if (root == null) return false;

        Node parent = null;
        Node current = root;
        boolean isLeftChild = false;

        while (current != null && compare(n, current.value) != 0) {
            parent = current;
            if (compare(n, current.value) < 0) {
                isLeftChild = true;
                current = current.left;
            } else {
                isLeftChild = false;
                current = current.right;
            }
        }
        if (current == null) return false; // не найден

        // Случай 1: нет потомков
        if (current.left == null && current.right == null) {
            if (current == root) root = null;
            else if (isLeftChild) parent.left = null;
            else parent.right = null;
        }
        // Случай 2: один потомок
        else if (current.left == null) {
            if (current == root) root = current.right;
            else if (isLeftChild) parent.left = current.right;
            else parent.right = current.right;
        } else if (current.right == null) {
            if (current == root) root = current.left;
            else if (isLeftChild) parent.left = current.left;
            else parent.right = current.left;
        }
        // Случай 3: два потомка
        else {
            Node successorParent = current;
            Node successor = current.right;
            while (successor.left != null) {
                successorParent = successor;
                successor = successor.left;
            }
            current.value = successor.value;
            if (successorParent.left == successor)
                successorParent.left = successor.right;
            else
                successorParent.right = successor.right;
        }

        size--;
        return true;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    // ---------- Вспомогательные методы обхода дерева ----------
    private void inorder(Node node, List<Number> list) {
        if (node == null) return;
        inorder(node.left, list);
        list.add(node.value);
        inorder(node.right, list);
    }

    private List<Number> toList() {
        List<Number> list = new ArrayList<>(size);
        inorder(root, list);
        return list;
    }

    // ---------- Методы Collection ----------
    @Override
    public Iterator<Number> iterator() {
        List<Number> list = toList();
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return toList().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        List<Number> list = toList();
        return list.toArray(a);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Number> c) {
        boolean changed = false;
        for (Number n : c)
            if (add(n)) changed = true;
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c)
            if (remove(o)) changed = true;
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        List<Number> all = toList();
        boolean changed = false;
        for (Number n : all) {
            if (!c.contains(n)) {
                remove(n);
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public String toString() {
        List<Number> list = toList();
        return list.toString();
    }
}
