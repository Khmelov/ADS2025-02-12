package by.it.group451001.suprunovich.lesson12;

import java.util.Map;
import java.util.Objects;

public class MyAvlMap implements Map<Integer, String> {
    private class Node {
        Integer key;
        String info;
        Node left, right;
        int height;

        Node(Integer key, String info) {
            this.key = key;
            this.info = info;
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
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            throw new ClassCastException("Key must be an Integer");
        }
        Node node = getNode(root, (Integer) key);
        return node == null ? null : node.info;
    }

    private Node getNode(Node node, Integer key) {
        if (node == null) {
            return null;
        }
        if (key.equals(node.key)) {
            return node;
        }
        return key < node.key ? getNode(node.left, key) : getNode(node.right, key);
    }

    @Override
    public String put(Integer key, String info) {
        String previousValue = get(key);
        root = putNode(root, key, info);
        return previousValue;
    }

    private Node putNode(Node node, Integer key, String info) {
        if (node == null) {
            size++;
            return new Node(key, info);
        }
        if (key.equals(node.key)) {
            node.info = info;
            return node;
        } else if (key < node.key) {
            node.left = putNode(node.left, key, info);
        } else {
            node.right = putNode(node.right, key, info);
        }

        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        return balance(node);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            throw new ClassCastException("Key must be an Integer");
        }
        String info = get((Integer) key);
        if (info != null) {
            root = removeNode(root, (Integer) key);
            return info;
        }
        return null;
    }

    private Node removeNode(Node node, Integer key) {
        if (node == null) {
            return null;
        }

        if (key < node.key) {
            node.left = removeNode(node.left, key);
        } else if (key > node.key) {
            node.right = removeNode(node.right, key);
        } else {
            if (node.left == null) {
                size--;
                return node.right;
            } else if (node.right == null) {
                size--;
                return node.left;
            }

            Node min = getMin(node.right);
            node.key = min.key;
            node.info = min.info;
            node.right = removeNode(node.right, min.key);
        }

        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        return balance(node);
    }

    private Node getMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    @Override
    public boolean containsKey(Object obj) {
        if (!(obj instanceof Integer)) {
            return false;
        }
        return getNode(root, (Integer) obj) != null;
    }

    @Override
    public String toString() {
        StringBuilder resultStr = new StringBuilder("{");
        UpdateSB(root, resultStr);
        if (resultStr.length() > 1) {
            resultStr.setLength(resultStr.length() - 2);
        }
        resultStr.append("}");
        return resultStr.toString();
    }

    private void UpdateSB(Node node, StringBuilder result) {
        if (node != null) {
            UpdateSB(node.left, result);
            result.append(node.key).append("=").append(node.info).append(", ");
            UpdateSB(node.right, result);
        }
    }

    private int getHeight(Node node) {
        return node == null ? 0 : node.height;
    }

    private int getBalance(Node node) {
        return node == null ? 0 : getHeight(node.left) - getHeight(node.right);
    }

    private Node balance(Node node) {
        int balance = getBalance(node);

        if (balance > 1) {
            if (getBalance(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }

        if (balance < -1) {
            if (getBalance(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }

        return node;
    }

    private Node rotateRight(Node node) {
        Node root = node.left;
        node.left = root.right;
        root.right = node;
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));
        return root;
    }

    private Node rotateLeft(Node node) {
        Node root = node.right;
        node.right = root.left;
        root.left = node;
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));
        return root;
    }

    @Override
    public boolean containsValue(Object obj) {
        return containsValue(root, obj);
    }

    private boolean containsValue(Node node, Object obj) {
        if (node == null) {
            return false;
        }
        if (Objects.equals(node.info, obj)) {
            return true;
        }
        return containsValue(node.left, obj) || containsValue(node.right, obj);
    }

    @Override
    public java.util.Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public java.util.Set<Integer> keySet() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public java.util.Collection<String> values() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
}
