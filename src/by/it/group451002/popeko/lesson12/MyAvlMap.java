package by.it.group451002.popeko.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right;
        int height;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size;

    public MyAvlMap() {
        root = null;
        size = 0;
    }

    @Override
    public String put(Integer key, String value) {
        String[] oldValue = new String[1];
        root = put(root, key, value, oldValue);
        return oldValue[0];
    }

    private Node put(Node node, Integer key, String value, String[] oldValue) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value, oldValue);
        } else if (cmp > 0) {
            node.right = put(node.right, key, value, oldValue);
        } else {
            oldValue[0] = node.value;
            node.value = value;
            return node;
        }

        return balance(node);
    }

    @Override
    public String remove(Object key) {
        String[] removedValue = new String[1];
        root = remove(root, (Integer) key, removedValue);
        if (removedValue[0] != null) {
            size--;
        }
        return removedValue[0];
    }

    private Node remove(Node node, Integer key, String[] removedValue) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = remove(node.left, key, removedValue);
        } else if (cmp > 0) {
            node.right = remove(node.right, key, removedValue);
        } else {
            removedValue[0] = node.value;

            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            Node minNode = findMin(node.right);
            node.key = minNode.key;
            node.value = minNode.value;
            node.right = remove(node.right, minNode.key, new String[1]);
        }

        return balance(node);
    }

    @Override
    public String get(Object key) {
        Node node = get(root, (Integer) key);
        return node != null ? node.value : null;
    }

    private Node get(Node node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) return get(node.left, key);
        if (cmp > 0) return get(node.right, key);
        return node;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderToString(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    private void inOrderToString(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderToString(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderToString(node.right, sb);
        }
    }

    // Вспомогательные методы для АВЛ-дерева
    private int height(Node node) {
        return node != null ? node.height : 0;
    }

    private int balanceFactor(Node node) {
        return node != null ? height(node.left) - height(node.right) : 0;
    }

    private void updateHeight(Node node) {
        if (node != null) {
            node.height = Math.max(height(node.left), height(node.right)) + 1;
        }
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T = x.right;

        x.right = y;
        y.left = T;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T = y.left;

        y.left = x;
        x.right = T;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    private Node balance(Node node) {
        if (node == null) return null;

        updateHeight(node);
        int bf = balanceFactor(node);

        // Left Left Case
        if (bf > 1 && balanceFactor(node.left) >= 0) {
            return rotateRight(node);
        }

        // Left Right Case
        if (bf > 1 && balanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right Right Case
        if (bf < -1 && balanceFactor(node.right) <= 0) {
            return rotateLeft(node);
        }

        // Right Left Case
        if (bf < -1 && balanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Простые методы
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

    // Не реализованные методы
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