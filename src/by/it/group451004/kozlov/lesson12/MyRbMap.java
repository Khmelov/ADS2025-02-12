package by.it.group451004.kozlov.lesson12;

import java.util.*;

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
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("Key cannot be null");

        String oldValue = get(key);
        root = put(root, key, value);
        root.color = BLACK;
        return oldValue;
    }

    private Node put(Node node, Integer key, String value) {
        if (node == null) {
            size++;
            return new Node(key, value, RED);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value);
            if (node.left != null) node.left.parent = node;
        } else if (cmp > 0) {
            node.right = put(node.right, key, value);
            if (node.right != null) node.right.parent = node;
        } else {
            node.value = value;
            return node;
        }

        return balance(node);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;

        String oldValue = get(key);
        if (oldValue != null) {
            root = remove(root, (Integer) key);
            if (root != null) root.color = BLACK;
            size--;
        }
        return oldValue;
    }

    private Node remove(Node node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = remove(node.left, key);
        } else if (cmp > 0) {
            node.right = remove(node.right, key);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            Node minNode = findMin(node.right);
            node.key = minNode.key;
            node.value = minNode.value;
            node.right = remove(node.right, minNode.key);
        }

        return balance(node);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        return get(root, (Integer) key);
    }

    private String get(Node node, Integer key) {
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) node = node.left;
            else if (cmp > 0) node = node.right;
            else return node.value;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) return false;
        return containsValue(root, (String) value);
    }

    private boolean containsValue(Node node, String value) {
        if (node == null) return false;
        if (value.equals(node.value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public Integer firstKey() {
        if (root == null) return null;
        return findMin(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) return null;
        return findMax(root).key;
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
            headMap(node.right, toKey, result);
        }
        headMap(node.left, toKey, result);
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
        }
        tailMap(node.right, fromKey, result);
    }

    @Override
    public String toString() {
        if (isEmpty()) return "{}";

        StringBuilder sb = new StringBuilder("{");
        inOrderToString(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
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

    // Красно-черное дерево балансировка
    private Node balance(Node node) {
        if (isRed(node.right) && !isRed(node.left)) node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right)) flipColors(node);
        return node;
    }

    private boolean isRed(Node node) {
        return node != null && node.color == RED;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        if (x.left != null) x.left.parent = h;
        x.left = h;
        x.parent = h.parent;
        h.parent = x;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        if (x.right != null) x.right.parent = h;
        x.right = h;
        x.parent = h.parent;
        h.parent = x;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private void flipColors(Node h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    private Node findMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private Node findMax(Node node) {
        while (node.right != null) node = node.right;
        return node;
    }

    // Реализация остальных обязательных методов SortedMap
    @Override
    public Comparator<? super Integer> comparator() {
        return null; // используем естественный порядок
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MyRbMap result = new MyRbMap();
        subMap(root, fromKey, toKey, result);
        return result;
    }

    private void subMap(Node node, Integer fromKey, Integer toKey, MyRbMap result) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0 && node.key.compareTo(toKey) < 0) {
            result.put(node.key, node.value);
        }
        if (node.key.compareTo(fromKey) > 0) {
            subMap(node.left, fromKey, toKey, result);
        }
        if (node.key.compareTo(toKey) < 0) {
            subMap(node.right, fromKey, toKey, result);
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
        Set<Integer> keys = new TreeSet<>();
        keySet(root, keys);
        return keys;
    }

    private void keySet(Node node, Set<Integer> keys) {
        if (node != null) {
            keySet(node.left, keys);
            keys.add(node.key);
            keySet(node.right, keys);
        }
    }

    @Override
    public Collection<String> values() {
        Collection<String> values = new ArrayList<>();
        values(root, values);
        return values;
    }

    private void values(Node node, Collection<String> values) {
        if (node != null) {
            values(node.left, values);
            values.add(node.value);
            values(node.right, values);
        }
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        Set<Entry<Integer, String>> entries = new TreeSet<>(Comparator.comparing(Entry::getKey));
        entrySet(root, entries);
        return entries;
    }

    private void entrySet(Node node, Set<Entry<Integer, String>> entries) {
        if (node != null) {
            entrySet(node.left, entries);
            entries.add(new SimpleEntry(node.key, node.value));
            entrySet(node.right, entries);
        }
    }

    // Простая реализация Entry
    private static class SimpleEntry implements Entry<Integer, String> {
        private final Integer key;
        private String value;

        SimpleEntry(Integer key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public Integer getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String setValue(String value) {
            String oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Entry)) return false;
            Entry<?, ?> entry = (Entry<?, ?>) o;
            return Objects.equals(key, entry.getKey()) && Objects.equals(value, entry.getValue());
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }
}