package by.it.group451003.fedorcov.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class RbNode {
        Integer key;
        String value;
        RbNode left, right;
        boolean color;
        int size;

        RbNode(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.size = 1;
        }
    }

    private RbNode root;
    private int size;

    public MyRbMap() {
        root = null;
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
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return get(root, (Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) return false;
        return containsValue(root, (String) value);
    }

    private boolean containsValue(RbNode node, String value) {
        if (node == null) return false;
        if (value.equals(node.value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        RbNode node = get(root, (Integer) key);
        return node == null ? null : node.value;
    }

    private RbNode get(RbNode node, Integer key) {
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) node = node.left;
            else if (cmp > 0) node = node.right;
            else return node;
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();

        String oldValue = get(key);
        root = put(root, key, value);
        root.color = BLACK;
        if (oldValue == null) size++;
        return oldValue;
    }

    private RbNode put(RbNode node, Integer key, String value) {
        if (node == null) return new RbNode(key, value, RED);

        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = put(node.left, key, value);
        else if (cmp > 0) node.right = put(node.right, key, value);
        else {
            node.value = value;
            return node;
        }

        if (isRed(node.right) && !isRed(node.left)) node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right)) flipColors(node);

        updateSize(node);
        return node;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        if (!containsKey(key)) return null;

        String oldValue = get(key);

        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = remove(root, (Integer) key);
        if (root != null) root.color = BLACK;
        size--;

        return oldValue;
    }

    private RbNode remove(RbNode node, Integer key) {
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
                RbNode min = min(node.right);
                node.key = min.key;
                node.value = min.value;
                node.right = deleteMin(node.right);
            } else {
                node.right = remove(node.right, key);
            }
        }
        return balance(node);
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

    private void inOrderKeys(RbNode node, Set<Integer> keys) {
        if (node == null) return;
        inOrderKeys(node.left, keys);
        keys.add(node.key);
        inOrderKeys(node.right, keys);
    }

    @Override
    public Collection<String> values() {
        List<String> values = new ArrayList<>();
        inOrderValues(root, values);
        return values;
    }

    private void inOrderValues(RbNode node, List<String> values) {
        if (node == null) return;
        inOrderValues(node.left, values);
        values.add(node.value);
        inOrderValues(node.right, values);
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        Set<Entry<Integer, String>> entries = new TreeSet<>(Comparator.comparingInt(Entry::getKey));
        inOrderEntries(root, entries);
        return entries;
    }

    private void inOrderEntries(RbNode node, Set<Entry<Integer, String>> entries) {
        if (node == null) return;
        inOrderEntries(node.left, entries);
        entries.add(new AbstractMap.SimpleEntry<>(node.key, node.value));
        inOrderEntries(node.right, entries);
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        if (fromKey.compareTo(toKey) >= 0) {
            throw new IllegalArgumentException("fromKey >= toKey");
        }
        MyRbMap result = new MyRbMap();
        subMap(root, fromKey, toKey, result);
        return result;
    }

    private void subMap(RbNode node, Integer fromKey, Integer toKey, MyRbMap result) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0) {
            subMap(node.left, fromKey, toKey, result);
        }
        if (node.key.compareTo(fromKey) >= 0 && node.key.compareTo(toKey) < 0) {
            result.put(node.key, node.value);
        }
        if (node.key.compareTo(toKey) < 0) {
            subMap(node.right, fromKey, toKey, result);
        }
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        headMap(root, toKey, result);
        return result;
    }

    private void headMap(RbNode node, Integer toKey, MyRbMap result) {
        if (node == null) return;
        if (node.key.compareTo(toKey) < 0) {
            headMap(node.left, toKey, result);
            result.put(node.key, node.value);
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

    private void tailMap(RbNode node, Integer fromKey, MyRbMap result) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0) {
            tailMap(node.left, fromKey, result);
            result.put(node.key, node.value);
            tailMap(node.right, fromKey, result);
        } else {
            tailMap(node.right, fromKey, result);
        }
    }

    @Override
    public Integer firstKey() {
        if (isEmpty()) throw new NoSuchElementException();
        return min(root).key;
    }

    @Override
    public Integer lastKey() {
        if (isEmpty()) throw new NoSuchElementException();
        return max(root).key;
    }

    private RbNode min(RbNode node) {
        if (node.left == null) return node;
        return min(node.left);
    }

    private RbNode max(RbNode node) {
        if (node.right == null) return node;
        return max(node.right);
    }

    @Override
    public String toString() {
        if (root == null) return "{}";

        List<String> entries = new ArrayList<>();
        inOrderTraversal(root, entries);

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < entries.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(entries.get(i));
        }
        sb.append("}");
        return sb.toString();
    }

    private void inOrderTraversal(RbNode node, List<String> entries) {
        if (node == null) return;
        inOrderTraversal(node.left, entries);
        entries.add(node.key + "=" + node.value);
        inOrderTraversal(node.right, entries);
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
        updateSize(h);
        updateSize(x);
        return x;
    }

    private RbNode rotateRight(RbNode h) {
        RbNode x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        updateSize(h);
        updateSize(x);
        return x;
    }

    private void flipColors(RbNode h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    private void updateSize(RbNode node) {
        if (node != null) {
            node.size = 1 + size(node.left) + size(node.right);
        }
    }

    private int size(RbNode node) {
        return node == null ? 0 : node.size;
    }

    private RbNode moveRedLeft(RbNode node) {
        flipColors(node);
        if (isRed(node.right.left)) {
            node.right = rotateRight(node.right);
            node = rotateLeft(node);
            flipColors(node);
        }
        return node;
    }

    private RbNode moveRedRight(RbNode node) {
        flipColors(node);
        if (isRed(node.left.left)) {
            node = rotateRight(node);
            flipColors(node);
        }
        return node;
    }

    private RbNode balance(RbNode node) {
        if (isRed(node.right)) node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right)) flipColors(node);

        updateSize(node);
        return node;
    }

    private RbNode deleteMin(RbNode node) {
        if (node.left == null) return null;

        if (!isRed(node.left) && !isRed(node.left.left))
            node = moveRedLeft(node);

        node.left = deleteMin(node.left);
        return balance(node);
    }
}