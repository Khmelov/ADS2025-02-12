package by.it.group451003.burshtyn.lesson12;

import java.util.SortedMap;
import java.util.NoSuchElementException;

public class MyRbMap implements SortedMap<Integer, String> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean color;

        Node(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private Node root;
    private int size;

    public MyRbMap() {
        root = null;
        size = 0;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("Key cannot be null");

        String[] oldValue = new String[1];
        root = putRecursive(root, key, value, oldValue);
        if (oldValue[0] == null) {
            size++;
        }
        if (root != null) root.color = BLACK;
        return oldValue[0];
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer intKey = (Integer) key;

        String[] removedValue = new String[1];
        root = removeRecursive(root, intKey, removedValue);
        if (removedValue[0] != null) {
            size--;
        }
        if (root != null) root.color = BLACK;
        return removedValue[0];
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer intKey = (Integer) key;

        Node node = findNode(root, intKey);
        return node != null ? node.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRecursive(root, value);
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

    @Override
    public Integer firstKey() {
        if (isEmpty()) throw new NoSuchElementException("Map is empty");
        return findMin(root).key;
    }

    @Override
    public Integer lastKey() {
        if (isEmpty()) throw new NoSuchElementException("Map is empty");
        return findMax(root).key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        headMapRecursive(root, toKey, result);
        return result;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        tailMapRecursive(root, fromKey, result);
        return result;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "{}";

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderTraversal(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    private boolean isRed(Node node) {
        return node != null && node.color == RED;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private void flipColors(Node h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    private Node putRecursive(Node h, Integer key, String value, String[] oldValue) {
        if (h == null) {
            return new Node(key, value, RED);
        }

        int cmp = key.compareTo(h.key);
        if (cmp < 0) {
            h.left = putRecursive(h.left, key, value, oldValue);
        } else if (cmp > 0) {
            h.right = putRecursive(h.right, key, value, oldValue);
        } else {
            oldValue[0] = h.value;
            h.value = value;
            return h;
        }

        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        return h;
    }

    private Node removeRecursive(Node h, Integer key, String[] removedValue) {
        if (h == null) return null;

        int cmp = key.compareTo(h.key);
        if (cmp < 0) {
            h.left = removeRecursive(h.left, key, removedValue);
        } else if (cmp > 0) {
            h.right = removeRecursive(h.right, key, removedValue);
        } else {
            removedValue[0] = h.value;

            if (h.left == null) return h.right;
            if (h.right == null) return h.left;

            Node temp = h;
            h = findMin(temp.right);
            h.right = deleteMin(temp.right);
            h.left = temp.left;
        }

        return h;
    }

    private Node deleteMin(Node h) {
        if (h.left == null) return null;

        if (!isRed(h.left) && !isRed(h.left.left)) {
            h = moveRedLeft(h);
        }

        h.left = deleteMin(h.left);
        return balance(h);
    }

    private Node moveRedLeft(Node h) {
        flipColors(h);
        if (isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    private Node balance(Node h) {
        if (isRed(h.right)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        return h;
    }

    private Node findMin(Node node) {
        while (node != null && node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node findMax(Node node) {
        while (node != null && node.right != null) {
            node = node.right;
        }
        return node;
    }

    private Node findNode(Node node, Integer key) {
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) node = node.left;
            else if (cmp > 0) node = node.right;
            else return node;
        }
        return null;
    }

    private boolean containsValueRecursive(Node node, Object value) {
        if (node == null) return false;
        if (value == null ? node.value == null : value.equals(node.value)) return true;
        return containsValueRecursive(node.left, value) || containsValueRecursive(node.right, value);
    }

    private void headMapRecursive(Node node, Integer toKey, MyRbMap result) {
        if (node == null) return;

        headMapRecursive(node.left, toKey, result);
        if (node.key.compareTo(toKey) < 0) {
            result.put(node.key, node.value);
        }
        headMapRecursive(node.right, toKey, result);
    }

    private void tailMapRecursive(Node node, Integer fromKey, MyRbMap result) {
        if (node == null) return;

        tailMapRecursive(node.left, fromKey, result);
        if (node.key.compareTo(fromKey) >= 0) {
            result.put(node.key, node.value);
        }
        tailMapRecursive(node.right, fromKey, result);
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    @Override
    public java.util.Comparator<? super Integer> comparator() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void putAll(java.util.Map<? extends Integer, ? extends String> m) {
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
    public java.util.Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}