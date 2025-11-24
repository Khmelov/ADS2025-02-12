package by.it.group451001.klevko.lesson12;

import java.util.*;

public class MyAvlMap implements Map<Integer, String> {

    private Node root;
    private int size;

    private class Node {
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

    private int getHeight(Node node) {
        if (node == null) return 0;
        return node.height;
    }

    private int getBalance(Node node) {
        if (node == null) return 0;
        return getHeight(node.left) - getHeight(node.right);
    }

    private void updateHeight(Node node) {
        if (node != null) node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
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

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        String oldValue = get(key);
        root = putHelper(root, key, value);
        if (oldValue == null) size++;
        return oldValue;
    }

    private Node putHelper(Node node, Integer key, String value) {
        if (node == null) return new Node(key, value);
        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = putHelper(node.left, key, value);
        else if (cmp > 0) node.right = putHelper(node.right, key, value);
        else {
            node.value = value;
            return node;
        }
        updateHeight(node);
        int balance = getBalance(node);

        if (balance > 1 && key.compareTo(node.left.key) < 0) return rotateRight(node);
        if (balance < -1 && key.compareTo(node.right.key) > 0) return rotateLeft(node);

        if (balance > 1 && key.compareTo(node.left.key) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balance < -1 && key.compareTo(node.right.key) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
    }

    @Override
    public String remove(Object key) {
        if (key == null || !(key instanceof Integer)) return null;
        Integer intKey = (Integer) key;
        String oldValue = get(intKey);
        if (oldValue != null) {
            root = removeHelper(root, intKey);
            size--;
        }
        return oldValue;
    }

    private Node removeHelper(Node node, Integer key) {
        if (node == null) return node;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = removeHelper(node.left, key);
        else if (cmp > 0) node.right = removeHelper(node.right, key);
        else {
            if (node.left == null || node.right == null) {
                Node temp = null;
                if (temp == node.left) temp = node.right;
                else temp = node.left;

                if (temp == null) {
                    temp = node;
                    node = null;
                } else node = temp;
            } else {
                Node temp = getMinValueNode(node.right);
                node.key = temp.key;
                node.value = temp.value;
                node.right = removeHelper(node.right, temp.key);
            }
        }

        if (node == null) return node;
        updateHeight(node);
        int balance = getBalance(node);

        if (balance > 1 && getBalance(node.left) >= 0) return rotateRight(node);
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balance < -1 && getBalance(node.right) <= 0) return rotateLeft(node);
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
    }

    private Node getMinValueNode(Node node) {
        Node curr = node;
        while (curr.left != null) curr = curr.left;
        return curr;
    }

    @Override
    public String get(Object key) {
        if (key == null || !(key instanceof Integer)) return null;
        Integer intKey = (Integer) key;
        return getHelper(root, intKey);
    }

    private String getHelper(Node node, Integer key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) return getHelper(node.left, key);
        else if (cmp > 0) return getHelper(node.right, key);
        else return node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null || !(key instanceof Integer)) return false;
        return get(key) != null;
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
    public String toString() {
        if (isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderTraversal(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    //Нереализованные методы Map
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
