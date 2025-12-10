package by.it.group451001.volynets.lesson12;

import java.util.Map;
import java.util.Collection;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private static final class Node {
        int key;
        String value;
        int height;
        Node left;
        Node right;
        Node(int key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size;

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    private int balanceFactor(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
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

    private Node rebalance(Node node) {
        if (node == null) return null;
        updateHeight(node);
        int bf = balanceFactor(node);
        if (bf > 1 && balanceFactor(node.left) >= 0) return rotateRight(node);
        if (bf > 1 && balanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (bf < -1 && balanceFactor(node.right) <= 0) return rotateLeft(node);
        if (bf < -1 && balanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
    }

    private static final class Holder<T> { T value; }

    private Node putNode(Node node, int key, String value, Holder<String> oldVal) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }
        if (key < node.key) {
            node.left = putNode(node.left, key, value, oldVal);
        } else if (key > node.key) {
            node.right = putNode(node.right, key, value, oldVal);
        } else {
            oldVal.value = node.value;
            node.value = value;
            return node;
        }
        return rebalance(node);
    }

    private Node minNode(Node node) {
        Node cur = node;
        while (cur != null && cur.left != null) cur = cur.left;
        return cur;
    }

    private Node removeNode(Node node, int key, Holder<String> oldVal) {
        if (node == null) return null;
        if (key < node.key) {
            node.left = removeNode(node.left, key, oldVal);
        } else if (key > node.key) {
            node.right = removeNode(node.right, key, oldVal);
        } else {
            oldVal.value = node.value;
            if (node.left == null || node.right == null) {
                Node child = (node.left != null) ? node.left : node.right;
                node = child;
                size--;
            } else {
                Node succ = minNode(node.right);
                node.key = succ.key;
                node.value = succ.value;
                node.right = removeNode(node.right, succ.key, new Holder<String>());
            }
        }
        if (node == null) return null;
        return rebalance(node);
    }

    private String getNode(Node node, int key) {
        Node cur = node;
        while (cur != null) {
            if (key < cur.key) cur = cur.left;
            else if (key > cur.key) cur = cur.right;
            else return cur.value;
        }
        return null;
    }

    private boolean containsKeyNode(Node node, int key) {
        Node cur = node;
        while (cur != null) {
            if (key < cur.key) cur = cur.left;
            else if (key > cur.key) cur = cur.right;
            else return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        appendInOrder(root, sb, new boolean[]{false});
        sb.append('}');
        return sb.toString();
    }

    private void appendInOrder(Node node, StringBuilder sb, boolean[] first) {
        if (node == null) return;
        appendInOrder(node.left, sb, first);
        if (first[0]) sb.append(", ");
        sb.append(node.key).append('=').append(node.value);
        first[0] = true;
        appendInOrder(node.right, sb, first);
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("key is null");
        Holder<String> oldVal = new Holder<>();
        root = putNode(root, key, value, oldVal);
        return oldVal.value;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Holder<String> oldVal = new Holder<>();
        root = removeNode(root, (Integer) key, oldVal);
        return oldVal.value;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        return getNode(root, (Integer) key);
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return containsKeyNode(root, (Integer) key);
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("containsValue() is not required");
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException("putAll() is not required");
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException("keySet() is not required");
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException("values() is not required");
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("entrySet() is not required");
    }
}
