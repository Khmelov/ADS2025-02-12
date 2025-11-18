package by.it.group451001.kolosun.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {
    public boolean isBalanced() {
        return isBalanced(root);
    }

    private boolean isBalanced(Node node) {
        if (node == null)
            return true;

        int balance = getBalance(node);

        if (Math.abs(balance) > 1)
            return false;

        return isBalanced(node.left) && isBalanced(node.right);
    }

    private void checkBalance() {
        if (!isBalanced())
            throw new IllegalStateException("The tree is unbalanced");
    }

    private static class Node {
        public int key;
        public String data;
        public Node left = null;
        public Node right = null;
        public int height = 1;
        public Node(int key, String data) {
            this.key = key;
            this.data = data;
        }
    }

    private Node root = null;
    private int size = 0;

    private int getHeight(Node node) {
        return node == null ? 0 : node.height;
    }

    private void setHeight(Node node) {
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }

    private int getBalance(Node node) {
        return getHeight(node.left) - getHeight(node.right);
    }

    private Node rightRotate(Node node) {
        Node left = node.left;
        node.left = left.right;
        left.right = node;

        setHeight(node);
        setHeight(left);
        return left;
    }

    private Node leftRotate(Node node) {
        Node right = node.right;
        node.right = right.left;
        right.left = node;

        setHeight(node);
        setHeight(right);
        return right;
    }

    private Node balance(Node node) {
        int balance = getBalance(node);

        if (balance > 1) {
            if (getBalance(node.left) < 0)
                node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        else if (balance < -1) {
            if (getBalance(node.right) > 0)
                node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    private boolean isInvalidKeyType(Object o) {
        return !(o instanceof Integer);
    }

    private Node getMin(Node node) {
        while (node.left != null)
            node = node.left;
        return node;
    }

    private Node removeMin(Node node) {
        if (node.left == null)
            return node.right;

        node.left = removeMin(node.left);
        setHeight(node);

        return balance(node);
    }

    @Override
    public String toString() {
        checkBalance();
        if (root == null)
            return "{}";

        StringBuilder sb = new StringBuilder("{");
        toString(root, sb);
        sb.replace(sb.length() - 2, sb.length(), "}");
        return sb.toString();
    }

    private void toString(Node node, StringBuilder sb) {
        if (node != null) {
            toString(node.left, sb);
            sb.append(node.key).append("=").append(node.data).append(", ");
            toString(node.right, sb);
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

    private Node search(int key) {
        Node node = root;
        while (node != null) {
            if (key < node.key)
                node = node.left;
            else if (key > node.key)
                node = node.right;
            else
                return node;
        }
        return null;
    }
    @Override
    public boolean containsKey(Object key) {
        if (isInvalidKeyType(key))
            throw new ClassCastException("The key is not of type Integer");
        return search((int)key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public String get(Object key) {
        if (isInvalidKeyType(key))
            throw new ClassCastException("The key is not of type Integer");
        Node result = search((int)key);
        return result == null ? null : result.data;
    }

    @Override
    public String put(Integer key, String value) {
        var node = search(key);
        if (node == null) {
            size++;
            root = put(root, key, value);
            return null;
        }
        else {
            var oldValue = node.data;
            node.data = value;
            return oldValue;
        }
    }

    private Node put(Node node, int key, String value) {
        if (node == null)
            return new Node(key, value);

        if (key < node.key)
            node.left = put(node.left, key, value);
        else if (key > node.key)
            node.right = put(node.right, key, value);
        else
            node.data = value;
        setHeight(node);

        return balance(node);
    }

    @Override
    public String remove(Object key) {
        var oldValue = get(key);
        if (oldValue != null) {
            size--;
            root = remove(root, (int) key);
        }
        return oldValue;
    }

    private Node remove(Node node, int key) {
        if (node == null)
            return null;

        if (key < node.key)
            node.left = remove(node.left, key);
        else if (key > node.key)
            node.right = remove(node.right, key);
        else {
            if (node.left == null && node.right == null)
                return null;
            else if (node.left == null)
                return node.right;
            else if (node.right == null)
                return node.left;
            else {
                var min = getMin(node.right);

                node.key = min.key;
                node.data = min.data;

                node.right = removeMin(node.right);
            }
        }
        setHeight(node);

        return balance(node);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public Collection<String> values() {
        return null;
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return null;
    }
}