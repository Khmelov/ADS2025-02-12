package by.it.group451004.zarivniak.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class RbNode {
        Integer key;
        String value;
        RbNode left;
        RbNode right;
        boolean color;

        RbNode(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private RbNode root;
    private int size;

    public MyRbMap() {
        root = null;
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderTraversal(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        String[] oldValue = new String[1];
        root = put(root, key, value, oldValue);
        root.color = BLACK;
        return oldValue[0];
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        if (m == null) {
            throw new NullPointerException("Map cannot be null");
        }
        for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        if (!containsKey(key)) {
            return null;
        }

        String[] result = new String[1];
        root = remove(root, (Integer) key, result);
        if (root != null) {
            root.color = BLACK;
        }
        return result[0];
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        return get(root, (Integer) key);
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }
        return get(root, (Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
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
        if (root == null) {
            return null;
        }
        RbNode min = root;
        while (min.left != null) {
            min = min.left;
        }
        return min.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            return null;
        }
        RbNode max = root;
        while (max.right != null) {
            max = max.right;
        }
        return max.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        headMap(root, toKey, result);
        return result;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        tailMap(root, fromKey, result);
        return result;
    }

    private boolean isRed(RbNode node) {
        return node != null && node.color == RED;
    }

    private RbNode rotateLeft(RbNode h) {
        RbNode x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private RbNode rotateRight(RbNode h) {
        RbNode x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private void flipColors(RbNode h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    private RbNode put(RbNode h, Integer key, String value, String[] oldValue) {
        if (h == null) {
            size++;
            return new RbNode(key, value, RED);
        }

        int cmp = key.compareTo(h.key);
        if (cmp < 0) {
            h.left = put(h.left, key, value, oldValue);
        } else if (cmp > 0) {
            h.right = put(h.right, key, value, oldValue);
        } else {
            oldValue[0] = h.value;
            h.value = value;
        }

        // Балансировка
        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        return h;
    }

    private String get(RbNode node, Integer key) {
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) node = node.left;
            else if (cmp > 0) node = node.right;
            else return node.value;
        }
        return null;
    }

    private boolean containsValue(RbNode node, Object value) {
        if (node == null) return false;
        if (Objects.equals(value, node.value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    private RbNode remove(RbNode node, Integer key, String[] result) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = remove(node.left, key, result);
        } else if (cmp > 0) {
            node.right = remove(node.right, key, result);
        } else {
            result[0] = node.value;
            size--;

            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            // Узел с двумя потомками
            RbNode temp = min(node.right);
            node.key = temp.key;
            node.value = temp.value;
            node.right = deleteMin(node.right);
        }
        return balance(node);
    }

    private RbNode deleteMin(RbNode node) {
        if (node.left == null) return node.right;
        node.left = deleteMin(node.left);
        return balance(node);
    }

    private RbNode min(RbNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private RbNode balance(RbNode node) {
        if (node == null) return null;

        if (isRed(node.right)) node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right)) flipColors(node);

        return node;
    }

    private void inOrderTraversal(RbNode node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    private void headMap(RbNode node, Integer toKey, MyRbMap result) {
        if (node != null) {
            headMap(node.left, toKey, result);
            if (node.key.compareTo(toKey) < 0) {
                result.put(node.key, node.value);
            }
            headMap(node.right, toKey, result);
        }
    }

    private void tailMap(RbNode node, Integer fromKey, MyRbMap result) {
        if (node != null) {
            tailMap(node.left, fromKey, result);
            if (node.key.compareTo(fromKey) >= 0) {
                result.put(node.key, node.value);
            }
            tailMap(node.right, fromKey, result);
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