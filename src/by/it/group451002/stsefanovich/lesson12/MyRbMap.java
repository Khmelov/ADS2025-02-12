package by.it.group451002.stsefanovich.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {
    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean isRed;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.isRed = true;
        }
    }

    private Node root;
    private int size;

    private boolean isRed(Node node) {
        return node != null && node.isRed;
    }

    private Node rotateLeft(Node h) {
        if (h == null || h.right == null) return h;
        Node x = h.right;
        h.right = x.left;
        if (x.left != null) x.left.parent = h;
        x.left = h;
        x.parent = h.parent;
        h.parent = x;
        x.isRed = h.isRed;
        h.isRed = true;
        if (h == root) root = x;
        return x;
    }

    private Node rotateRight(Node h) {
        if (h == null || h.left == null) return h;
        Node x = h.left;
        h.left = x.right;
        if (x.right != null) x.right.parent = h;
        x.right = h;
        x.parent = h.parent;
        h.parent = x;
        x.isRed = h.isRed;
        h.isRed = true;
        if (h == root) root = x;
        return x;
    }

    private void flipColors(Node h) {
        if (h == null || h.left == null || h.right == null) return;
        h.isRed = !h.isRed;
        h.left.isRed = !h.left.isRed;
        h.right.isRed = !h.right.isRed;
    }

    private Node fixUp(Node node) {
        if (node == null) return node;
        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }
        return node;
    }

    private Node put(Node node, Integer key, String value, Node parent) {
        if (node == null) {
            size++;
            Node newNode = new Node(key, value);
            newNode.parent = parent;
            return newNode;
        }

        if (key.compareTo(node.key) < 0) {
            node.left = put(node.left, key, value, node);
        } else if (key.compareTo(node.key) > 0) {
            node.right = put(node.right, key, value, node);
        } else {
            node.value = value;
        }

        return fixUp(node);
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) return null;
        String oldValue = get(key);
        root = put(root, key, value, null);
        if (root != null) root.isRed = false;
        return oldValue;
    }

    private Node min(Node node) {
        while (node != null && node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node moveRedLeft(Node h) {
        if (h == null) return h;
        flipColors(h);
        if (h.right != null && isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    private Node moveRedRight(Node h) {
        if (h == null) return h;
        flipColors(h);
        if (h.left != null && isRed(h.left.left)) {
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    private Node fixUpAfterDelete(Node node) {
        if (node == null) return null;
        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && node.left.left != null && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }
        return node;
    }

    private Node deleteMin(Node node) {
        if (node == null) return null;
        if (node.left == null) {
            size--;
            return null;
        }
        if (!isRed(node.left) && (node.left.left == null || !isRed(node.left.left))) {
            node = moveRedLeft(node);
        }
        node.left = deleteMin(node.left);
        return fixUpAfterDelete(node);
    }

    private Node remove(Node node, Integer key) {
        if (node == null) return null;

        if (key.compareTo(node.key) < 0) {
            if (node.left != null && !isRed(node.left) && (node.left.left == null || !isRed(node.left.left))) {
                node = moveRedLeft(node);
            }
            node.left = remove(node.left, key);
        } else {
            if (isRed(node.left)) {
                node = rotateRight(node);
            }
            if (key.compareTo(node.key) == 0 && node.right == null) {
                size--;
                return null;
            }
            if (node.right != null && !isRed(node.right) && (node.right.left == null || !isRed(node.right.left))) {
                node = moveRedRight(node);
            }
            if (key.compareTo(node.key) == 0) {
                Node minNode = min(node.right);
                node.key = minNode.key;
                node.value = minNode.value;
                node.right = deleteMin(node.right);
            } else {
                node.right = remove(node.right, key);
            }
        }
        return fixUpAfterDelete(node);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        String oldValue = get((Integer) key);
        if (oldValue != null) {
            root = remove(root, (Integer) key);
            if (root != null) root.isRed = false;
        }
        return oldValue;
    }

    private String get(Node node, Integer key) {
        if (node == null) return null;
        if (key.compareTo(node.key) < 0) {
            return get(node.left, key);
        } else if (key.compareTo(node.key) > 0) {
            return get(node.right, key);
        } else {
            return node.value;
        }
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        return get(root, (Integer) key);
    }

    private boolean containsKey(Node node, Integer key) {
        if (node == null) return false;
        if (key.compareTo(node.key) < 0) {
            return containsKey(node.left, key);
        } else if (key.compareTo(node.key) > 0) {
            return containsKey(node.right, key);
        } else {
            return true;
        }
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return containsKey(root, (Integer) key);
    }

    private boolean containsValue(Node node, String value) {
        if (node == null) return false;
        if (node.value == null ? value == null : node.value.equals(value)) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) return false;
        return containsValue(root, (String) value);
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

    private void toString(Node node, StringBuilder sb) {
        if (node == null) return;
        toString(node.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(node.key).append("=").append(node.value);
        toString(node.right, sb);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        toString(root, sb);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        headMap(root, toKey, result);
        return result;
    }

    private void headMap(Node node, Integer toKey, MyRbMap result) {
        if (node == null) return;
        if (node.key.compareTo(toKey) < 0) {
            result.put(node.key, node.value);
            headMap(node.left, toKey, result);
            headMap(node.right, toKey, result);
        } else {
            headMap(node.left, toKey, result);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        tailMap(root, fromKey, result);
        return result;
    }

    private void tailMap(Node node, Integer fromKey, MyRbMap result) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0) {
            result.put(node.key, node.value);
            tailMap(node.left, fromKey, result);
            tailMap(node.right, fromKey, result);
        } else {
            tailMap(node.right, fromKey, result);
        }
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = min(root);
        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = root;
        while (node.right != null) {
            node = node.right;
        }//
        return node.key;
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
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}