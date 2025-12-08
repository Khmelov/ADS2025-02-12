package by.it.group451001.klevko.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private Node root;
    private int size;

    private class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        boolean color;

        Node(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private boolean isRed(Node node) {
        if (node == null) return false;
        return node.color == RED;
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
        if (h != null) {
            h.color = !h.color;
            if (h.left != null) h.left.color = !h.left.color;
            if (h.right != null) h.right.color = !h.right.color;
        }
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        String oldValue = get(key);
        root = putHelper(root, key, value);
        root.color = BLACK;
        if (oldValue == null) size++;
        return oldValue;
    }

    private Node putHelper(Node h, Integer key, String value) {
        if (h == null) return new Node(key, value, RED);
        int cmp = key.compareTo(h.key);
        if (cmp < 0) h.left = putHelper(h.left, key, value);
        else if (cmp > 0) h.right = putHelper(h.right, key, value);
        else {
            h.value = value;
            return h;
        }

        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left != null ? h.left.left : null)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);
        return h;
    }

    @Override
    public String remove(Object key) {
        if (key == null || !(key instanceof Integer)) return null;
        Integer intKey = (Integer) key;
        if (!containsKey(intKey)) return null;

        String oldValue = get(intKey);
        if (!isRed(root.left) && !isRed(root.right)) root.color = RED;
        root = removeHelper(root, intKey);
        if (root != null) root.color = BLACK;
        size--;
        return oldValue;
    }

    private Node removeHelper(Node h, Integer key) {
        if (h == null) return null;
        if (key.compareTo(h.key) < 0) {
            if (h.left != null && !isRed(h.left) && !isRed(h.left.left)) h = moveRedLeft(h);
            if (h.left != null) h.left = removeHelper(h.left, key);
        } else {
            if (isRed(h.left)) h = rotateRight(h);
            if (key.compareTo(h.key) == 0 && h.right == null) return null;
            if (h.right != null && !isRed(h.right) && !isRed(h.right.left)) h = moveRedRight(h);
            if (key.compareTo(h.key) == 0) {
                Node x = getMin(h.right);
                h.key = x.key;
                h.value = x.value;
                h.right = removeMin(h.right);
            } else {
                if (h.right != null) h.right = removeHelper(h.right, key);
            }
        }
        return balance(h);
    }

    private Node moveRedLeft(Node h) {
        flipColors(h);
        if (h.right != null && isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    private Node moveRedRight(Node h) {
        flipColors(h);
        if (h.left != null && isRed(h.left.left)) {
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    private Node balance(Node h) {
        if (h == null) return null;
        if (isRed(h.right)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left != null ? h.left.left : null)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);
        return h;
    }

    private Node removeMin(Node h) {
        if (h == null) return null;
        if (h.left == null) return null;
        if (!isRed(h.left) && !isRed(h.left.left)) h = moveRedLeft(h);
        h.left = removeMin(h.left);
        return balance(h);
    }

    private Node getMin(Node h) {
        if (h == null) return null;
        while (h.left != null) h = h.left;
        return h;
    }

    private Node getMax(Node h) {
        if (h == null) return null;
        while (h.right != null) h = h.right;
        return h;
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
    public boolean containsValue(Object value) {
        return containsValueHelper(root, value);
    }

    private boolean containsValueHelper(Node node, Object value) {
        if (node == null) return false;

        if (value == null) {
            if (node.value == null) return true;
        } else {
            if (value.equals(node.value)) return true;
        }
        return containsValueHelper(node.left, value) || containsValueHelper(node.right, value);
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
    public Integer firstKey() {
        if (isEmpty()) throw new NoSuchElementException();
        return getMin(root).key;
    }

    @Override
    public Integer lastKey() {
        if (isEmpty()) throw new NoSuchElementException();
        return getMax(root).key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) throw new NullPointerException();
        MyRbMap result = new MyRbMap();
        headMapHelper(root, toKey, result);
        return result;
    }

    private void headMapHelper(Node node, Integer toKey, MyRbMap result) {
        if (node == null) return;
        int cmp = node.key.compareTo(toKey);
        if (cmp < 0) {
            result.put(node.key, node.value);
            headMapHelper(node.left, toKey, result);
            headMapHelper(node.right, toKey, result);
        } else headMapHelper(node.left, toKey, result);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) throw new NullPointerException();
        MyRbMap result = new MyRbMap();
        tailMapHelper(root, fromKey, result);
        return result;
    }

    private void tailMapHelper(Node node, Integer fromKey, MyRbMap result) {
        if (node == null) return;
        int cmp = node.key.compareTo(fromKey);
        if (cmp >= 0) {
            result.put(node.key, node.value);
            tailMapHelper(node.left, fromKey, result);
            tailMapHelper(node.right, fromKey, result);
        } else tailMapHelper(node.right, fromKey, result);
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

    // Нереализованные методы SortedMap интерфейса (не требуются по заданию)
    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
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
