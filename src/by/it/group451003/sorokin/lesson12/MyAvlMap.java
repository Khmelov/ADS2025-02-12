package by.it.group451003.sorokin.lesson12;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {
    private Node root;
    private int size;

    // Узел АВЛ-дерева
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

    public MyAvlMap() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

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
            // Ключ уже существует, обновляем значение
            oldValue[0] = node.value;
            node.value = value;
            return node;
        }

        // Обновляем высоту и балансируем дерево
        return balance(node);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer intKey = (Integer) key;

        String[] removedValue = new String[1];
        root = remove(root, intKey, removedValue);
        if (removedValue[0] != null) {
            size--;
        }
        return removedValue[0];
    }

    private Node remove(Node node, Integer key, String[] removedValue) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = remove(node.left, key, removedValue);
        } else if (cmp > 0) {
            node.right = remove(node.right, key, removedValue);
        } else {
            // Найден узел для удаления
            removedValue[0] = node.value;

            // Узел с одним или нулем потомков
            if (node.left == null || node.right == null) {
                return (node.left != null) ? node.left : node.right;
            } else {
                // Узел с двумя потомками
                Node successor = findMin(node.right);
                node.key = successor.key;
                node.value = successor.value;
                node.right = remove(node.right, successor.key, new String[1]);
            }
        }

        return balance(node);
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer intKey = (Integer) key;

        Node node = get(root, intKey);
        return (node != null) ? node.value : null;
    }

    private Node get(Node node, Integer key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return get(node.left, key);
        } else if (cmp > 0) {
            return get(node.right, key);
        } else {
            return node;
        }
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }
        return get(key) != null;
    }

    // АВЛ-балансировка

    private int height(Node node) {
        return (node != null) ? node.height : 0;
    }

    private int balanceFactor(Node node) {
        return (node != null) ? height(node.left) - height(node.right) : 0;
    }

    private void updateHeight(Node node) {
        if (node != null) {
            node.height = Math.max(height(node.left), height(node.right)) + 1;
        }
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    private Node balance(Node node) {
        if (node == null) {
            return null;
        }

        updateHeight(node);
        int balance = balanceFactor(node);

        // Left Left Case
        if (balance > 1 && balanceFactor(node.left) >= 0) {
            return rotateRight(node);
        }

        // Left Right Case
        if (balance > 1 && balanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right Right Case
        if (balance < -1 && balanceFactor(node.right) <= 0) {
            return rotateLeft(node);
        }

        // Right Left Case
        if (balance < -1 && balanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder("{");
        inOrderToString(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void inOrderToString(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderToString(node.left, sb);
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append(node.key).append("=").append(node.value);
            inOrderToString(node.right, sb);
        }
    }

    // Методы интерфейса Map, которые не требуются по заданию
    // Реализованы как заглушки или минимальные реализации

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) {
            return false;
        }
        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException("keySet not implemented");
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException("values not implemented");
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("entrySet not implemented");
    }

    // Обязательные методы Object

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Map)) return false;

        Map<?,?> m = (Map<?,?>) o;
        if (m.size() != size()) return false;

        try {
            for (Node node : getAllNodes()) {
                if (!m.get(node.key).equals(node.value)) {
                    return false;
                }
            }
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }
        return true;
    }

    private java.util.List<Node> getAllNodes() {
        java.util.List<Node> nodes = new java.util.ArrayList<>();
        inOrderCollect(root, nodes);
        return nodes;
    }

    private void inOrderCollect(Node node, java.util.List<Node> nodes) {
        if (node != null) {
            inOrderCollect(node.left, nodes);
            nodes.add(node);
            inOrderCollect(node.right, nodes);
        }
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (Node node : getAllNodes()) {
            h += node.key.hashCode() ^ (node.value == null ? 0 : node.value.hashCode());
        }
        return h;
    }
}
