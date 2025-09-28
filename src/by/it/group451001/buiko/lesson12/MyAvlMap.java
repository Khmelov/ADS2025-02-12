package by.it.group451001.buiko.lesson12;

import java.util.*;

public class MyAvlMap implements Map<Integer, String> {

    // Внутренний класс для узлов АВЛ-дерева
    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        int height;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size = 0;

    // Возвращает высоту узла (для null возвращает 0)
    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    // Обновляет высоту узла на основе высот потомков
    private void updateHeight(Node node) {
        node.height = Math.max(height(node.left), height(node.right)) + 1;
    }

    // Возвращает баланс-фактор узла
    private int getBalance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    // Правый поворот вокруг узла y
    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    // Левый поворот вокруг узла x
    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    // Балансировка узла
    private Node balance(Node node) {
        if (node == null) return null;

        updateHeight(node);
        int balance = getBalance(node);

        // Лево-левый случай
        if (balance > 1 && getBalance(node.left) >= 0)
            return rotateRight(node);

        // Право-правый случай
        if (balance < -1 && getBalance(node.right) <= 0)
            return rotateLeft(node);

        // Лево-правый случай
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Право-левый случай
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    // Вспомогательный метод для вставки узла в дерево
    private Node put(Node node, Integer key, String value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0)
            node.left = put(node.left, key, value);
        else if (cmp > 0)
            node.right = put(node.right, key, value);
        else
            node.value = value;

        return balance(node);
    }

    @Override
    public String put(Integer key, String value) {
        String oldValue = get(key);
        root = put(root, key, value);
        return oldValue;
    }

    // Поиск узла с минимальным ключом в поддереве
    private Node findMin(Node node) {
        return node.left == null ? node : findMin(node.left);
    }

    // Удаление узла с минимальным ключом из поддерева
    private Node removeMin(Node node) {
        if (node.left == null) {
            size--;
            return node.right;
        }
        node.left = removeMin(node.left);
        return balance(node);
    }

    // Вспомогательный метод для удаления узла
    private Node remove(Node node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = remove(node.left, key);
        } else if (cmp > 0) {
            node.right = remove(node.right, key);
        } else {
            // Узел для удаления найден
            if (node.left == null || node.right == null) {
                // Случай 1: нет левого или правого потомка
                size--;
                return (node.left != null) ? node.left : node.right;
            } else {
                // Случай 2: есть оба потомка
                Node minNode = findMin(node.right);
                node.key = minNode.key;
                node.value = minNode.value;
                node.right = removeMin(node.right);
            }
        }
        return balance(node);
    }

    @Override
    public String remove(Object key) {
        String oldValue = get(key);
        if (oldValue != null) {
            root = remove(root, (Integer) key);
        }
        return oldValue;
    }

    // Вспомогательный метод для поиска значения по ключу
    private String get(Node node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp < 0)
            return get(node.left, key);
        else if (cmp > 0)
            return get(node.right, key);
        else
            return node.value;
    }

    @Override
    public String get(Object key) {
        if (key == null) return null;
        return get(root, (Integer) key);
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Вспомогательный метод для построения строки (ин-порядок обхода)
    private void toString(Node node, StringBuilder sb) {
        if (node == null) return;

        toString(node.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(node.key).append('=').append(node.value);
        toString(node.right, sb);
    }

    @Override
    public String toString() {
        if (isEmpty()) return "{}";

        StringBuilder sb = new StringBuilder("{");
        toString(root, sb);
        return sb.append("}").toString();
    }

    // Остальные методы интерфейса Map не реализованы
    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}