package by.it.group451003.sorokin.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {
    private Node root;
    private int size;

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;
        boolean color;

        Node(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    public MyRbMap() {
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
        root.color = BLACK; // Корень всегда черный
        return oldValue[0];
    }

    private Node put(Node node, Integer key, String value, String[] oldValue) {
        if (node == null) {
            size++;
            return new Node(key, value, RED);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value, oldValue);
            if (node.left != null) node.left.parent = node;
        } else if (cmp > 0) {
            node.right = put(node.right, key, value, oldValue);
            if (node.right != null) node.right.parent = node;
        } else {
            oldValue[0] = node.value;
            node.value = value;
            return node;
        }

        if (isRed(node.right) && !isRed(node.left)) node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right)) flipColors(node);

        return node;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer intKey = (Integer) key;

        if (!containsKey(intKey)) {
            return null;
        }

        String[] removedValue = new String[1];
        root = remove(root, intKey, removedValue);
        if (root != null) root.color = BLACK;
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
            node.right = deleteMin(node.right);
        }

        return balance(node);
    }

    private Node deleteMin(Node node) {
        if (node.left == null) return null;

        if (!isRed(node.left) && !isRed(node.left.left)) {
            node = moveRedLeft(node);
        }

        node.left = deleteMin(node.left);
        return balance(node);
    }

    private Node moveRedLeft(Node node) {
        flipColors(node);
        if (isRed(node.right.left)) {
            node.right = rotateRight(node.right);
            node = rotateLeft(node);
            flipColors(node);
        }
        return node;
    }

    private Node balance(Node node) {
        if (isRed(node.right)) node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right)) flipColors(node);

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
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) return get(node.left, key);
        else if (cmp > 0) return get(node.right, key);
        else return node;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) return false;
        if (value == null ? node.value == null : value.equals(node.value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
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
        if (h.left != null) h.left.color = !h.left.color;
        if (h.right != null) h.right.color = !h.right.color;
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node findMax(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    @Override
    public Integer firstKey() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        return findMin(root).key;
    }

    @Override
    public Integer lastKey() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        return findMax(root).key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) {
            throw new NullPointerException("toKey cannot be null");
        }
        MyRbMap result = new MyRbMap();
        headMap(root, toKey, result);
        return result;
    }

    private void headMap(Node node, Integer toKey, MyRbMap result) {
        if (node == null) return;

        if (node.key.compareTo(toKey) < 0) {
            result.put(node.key, node.value);
            headMap(node.right, toKey, result);
        }
        headMap(node.left, toKey, result);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) {
            throw new NullPointerException("fromKey cannot be null");
        }
        MyRbMap result = new MyRbMap();
        tailMap(root, fromKey, result);
        return result;
    }

    private void tailMap(Node node, Integer fromKey, MyRbMap result) {
        if (node == null) return;

        if (node.key.compareTo(fromKey) >= 0) {
            result.put(node.key, node.value);
            tailMap(node.left, fromKey, result);
        }
        tailMap(node.right, fromKey, result);
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

    @Override
    public Comparator<? super Integer> comparator() {
        return null; // естественный порядок
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException("subMap not implemented");
    }

    // Обязательные методы Object

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Map)) return false;

        Map<?,?> m = (Map<?,?>) o;
        if (m.size() != size()) return false;

        try {
            return containsAllEntries(m);
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }
    }

    private boolean containsAllEntries(Map<?,?> m) {
        for (Node node : getAllNodes()) {
            Object value = m.get(node.key);
            if (value == null ? node.value != null : !value.equals(node.value)) {
                return false;
            }
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
