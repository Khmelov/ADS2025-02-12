package by.it.group451002.gorbach.lesson12;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.Comparator;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Integer key;
        String value;
        Node left, right;
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

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException();
        }

        String[] oldValue = new String[1];
        root = put(root, key, value, oldValue);
        root.color = BLACK;
        if (oldValue[0] == null) {
            size++;
        }
        return oldValue[0];
    }

    private Node put(Node h, Integer key, String value, String[] oldValue) {
        if (h == null) {
            return new Node(key, value, RED);
        }

        int cmp = key.compareTo(h.key);
        if (cmp < 0) {
            h.left = put(h.left, key, value, oldValue);
        } else if (cmp > 0) {
            h.right = put(h.right, key, value, oldValue);
        } else {
            oldValue[0] = h.value;
            h.value = value;
            return h;
        }

        // Балансировка
        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        return h;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        String[] result = new String[1];
        root = remove(root, (Integer) key, result);
        if (root != null) {
            root.color = BLACK;
        }
        if (result[0] != null) {
            size--;
        }
        return result[0];
    }

    private Node remove(Node node, Integer key, String[] result) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = remove(node.left, key, result);
        } else if (cmp > 0) {
            node.right = remove(node.right, key, result);
        } else {
            result[0] = node.value; // Сохраняем значение для возврата

            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            } else {
                // У узла есть оба потомка
                Node temp = node;
                node = min(temp.right);
                node.right = deleteMin(temp.right);
                node.left = temp.left;
            }
        }

        return balance(node);
    }

    private Node min(Node node) {
        if (node == null) return null;
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node deleteMin(Node node) {
        if (node.left == null) {
            return node.right;
        }
        node.left = deleteMin(node.left);
        return balance(node);
    }

    private Node balance(Node h) {
        if (isRed(h.right)) h = rotateLeft(h);
        if (isRed(h.left) && h.left != null && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);
        return h;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        return get(root, (Integer) key);
    }

    private String get(Node node, Integer key) {
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) {
                node = node.left;
            } else if (cmp > 0) {
                node = node.right;
            } else {
                return node.value;
            }
        }
        return null;
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
        if (node == null) {
            return false;
        }
        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
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
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        headMap(root, toKey, result);
        return result;
    }

    private void headMap(Node node, Integer toKey, MyRbMap result) {
        if (node == null) {
            return;
        }
        headMap(node.left, toKey, result);
        if (node.key.compareTo(toKey) < 0) {
            result.put(node.key, node.value);
        }
        headMap(node.right, toKey, result);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        tailMap(root, fromKey, result);
        return result;
    }

    private void tailMap(Node node, Integer fromKey, MyRbMap result) {
        if (node == null) {
            return;
        }
        tailMap(node.left, fromKey, result);
        if (node.key.compareTo(fromKey) >= 0) {
            result.put(node.key, node.value);
        }
        tailMap(node.right, fromKey, result);
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node node = root;
        while (node.left != null) {
            node = node.left;
        }
        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node node = root;
        while (node.right != null) {
            node = node.right;
        }
        return node.key;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }

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
    public java.util.Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}