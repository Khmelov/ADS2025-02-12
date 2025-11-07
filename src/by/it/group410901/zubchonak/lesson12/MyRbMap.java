package by.it.group410901.zubchonak.lesson12;

import java.util.*;

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
    private int size = 0;

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

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
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
        if (Objects.equals(node.value, value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = get(root, (Integer) key);
        return node == null ? null : node.value;
    }

    private Node get(Node node, Integer key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) return get(node.left, key);
        else if (cmp > 0) return get(node.right, key);
        else return node;
    }

    @Override
    public String put(Integer key, String value) {
        String oldValue = get(key);
        root = put(root, key, value);
        root.color = BLACK;
        if (oldValue == null) size++;
        return oldValue;
    }

    private Node put(Node node, Integer key, String value) {
        if (node == null) return new Node(key, value, RED);

        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = put(node.left, key, value);
        else if (cmp > 0) node.right = put(node.right, key, value);
        else node.value = value;

        if (isRed(node.right) && !isRed(node.left)) node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right)) flipColors(node);

        return node;
    }

    @Override
    public String remove(Object key) {
        if (!containsKey(key)) return null;

        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        String oldValue = get(key);
        root = remove(root, (Integer) key);
        if (root != null) root.color = BLACK;
        size--;
        return oldValue;
    }

    private Node remove(Node node, Integer key) {
        if (key.compareTo(node.key) < 0) {
            if (!isRed(node.left) && !isRed(node.left.left))
                node = moveRedLeft(node);
            node.left = remove(node.left, key);
        } else {
            if (isRed(node.left))
                node = rotateRight(node);
            if (key.compareTo(node.key) == 0 && (node.right == null))
                return null;
            if (!isRed(node.right) && !isRed(node.right.left))
                node = moveRedRight(node);
            if (key.compareTo(node.key) == 0) {
                Node x = min(node.right);
                node.key = x.key;
                node.value = x.value;
                node.right = deleteMin(node.right);
            } else {
                node.right = remove(node.right, key);
            }
        }
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

    private Node moveRedRight(Node node) {
        flipColors(node);
        if (isRed(node.left.left)) {
            node = rotateRight(node);
            flipColors(node);
        }
        return node;
    }

    private Node deleteMin(Node node) {
        if (node.left == null) return null;

        if (!isRed(node.left) && !isRed(node.left.left))
            node = moveRedLeft(node);

        node.left = deleteMin(node.left);
        return balance(node);
    }

    private Node min(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private Node balance(Node node) {
        if (isRed(node.right)) node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right)) flipColors(node);

        return node;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> keys = new TreeSet<>();
        inOrderKeys(root, keys);
        return keys;
    }

    private void inOrderKeys(Node node, Set<Integer> keys) {
        if (node != null) {
            inOrderKeys(node.left, keys);
            keys.add(node.key);
            inOrderKeys(node.right, keys);
        }
    }

    @Override
    public Collection<String> values() {
        List<String> values = new ArrayList<>();
        inOrderValues(root, values);
        return values;
    }

    private void inOrderValues(Node node, List<String> values) {
        if (node != null) {
            inOrderValues(node.left, values);
            values.add(node.value);
            inOrderValues(node.right, values);
        }
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        Set<Entry<Integer, String>> entries = new TreeSet<>(Comparator.comparingInt(Entry::getKey));
        inOrderEntries(root, entries);
        return entries;
    }

    private void inOrderEntries(Node node, Set<Entry<Integer, String>> entries) {
        if (node != null) {
            inOrderEntries(node.left, entries);
            entries.add(new AbstractMap.SimpleEntry<>(node.key, node.value));
            inOrderEntries(node.right, entries);
        }
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MyRbMap subMap = new MyRbMap();
        addInRange(root, fromKey, toKey, subMap);
        return subMap;
    }

    private void addInRange(Node node, Integer fromKey, Integer toKey, MyRbMap subMap) {
        if (node == null) return;

        if (node.key.compareTo(fromKey) > 0) {
            addInRange(node.left, fromKey, toKey, subMap);
        }
        if (node.key.compareTo(fromKey) >= 0 && node.key.compareTo(toKey) < 0) {
            subMap.put(node.key, node.value);
        }
        if (node.key.compareTo(toKey) < 0) {
            addInRange(node.right, fromKey, toKey, subMap);
        }
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return subMap(firstKey(), toKey);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap tailMap = new MyRbMap();
        addFromKey(root, fromKey, tailMap);
        return tailMap;
    }

    private void addFromKey(Node node, Integer fromKey, MyRbMap tailMap) {
        if (node == null) return;

        if (node.key.compareTo(fromKey) >= 0) {
            addFromKey(node.left, fromKey, tailMap);
            tailMap.put(node.key, node.value);
        }
        addFromKey(node.right, fromKey, tailMap);
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = root;
        while (node.left != null) node = node.left;
        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = root;
        while (node.right != null) node = node.right;
        return node.key;
    }

    @Override
    public String toString() {
        if (root == null) return "{}";

        StringBuilder sb = new StringBuilder("{");
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


}