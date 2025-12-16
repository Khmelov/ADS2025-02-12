package by.it.group451003.halubionak.lesson12;

import java.util.*;

@SuppressWarnings("unchecked")
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

    private boolean isRed(Node n) {
        return n != null && n.color == RED;
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
        h.color = RED;
        if (h.left != null) h.left.color = BLACK;
        if (h.right != null) h.right.color = BLACK;
    }

    private Node put(Node h, Integer key, String value) {
        if (h == null) {
            size++;
            return new Node(key, value, RED);
        }
        int cmp = key.compareTo(h.key);
        if (cmp < 0) h.left = put(h.left, key, value);
        else if (cmp > 0) h.right = put(h.right, key, value);
        else h.value = value;

        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        return h;
    }

    private String get(Node h, Integer key) {
        while (h != null) {
            int cmp = key.compareTo(h.key);
            if (cmp < 0) h = h.left;
            else if (cmp > 0) h = h.right;
            else return h.value;
        }
        return null;
    }

    private boolean containsKey(Node h, Integer key) {
        return get(h, key) != null;
    }

    private boolean containsValue(Node node, String value) {
        if (node == null) return false;
        if (Objects.equals(node.value, value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    private void inOrder(Node node, StringBuilder sb) {
        if (node != null) {
            inOrder(node.left, sb);
            if (sb.length() > 1) sb.append(", ");
            sb.append(node.key).append("=").append(node.value);
            inOrder(node.right, sb);
        }
    }

    private Node min(Node h) {
        if (h.left == null) return h;
        return min(h.left);
    }

    private Node max(Node h) {
        if (h.right == null) return h;
        return max(h.right);
    }

    // ================== MAP INTERFACE ==================
    @Override
    public String put(Integer key, String value) {
        String oldValue = get(root, key);
        root = put(root, key, value);
        root.color = BLACK;
        return oldValue;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        return get(root, (Integer) key);
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return containsKey(root, (Integer) key);
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) return false;
        return containsValue(root, (String) value);
    }



    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public void clear() {
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
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        sb.append("}");
        return sb.toString();
    }

    // ================== SORTEDMAP METHODS ==================
    @Override
    public Comparator<? super Integer> comparator() {
        return null; // Натуральный порядок
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        return min(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        return max(root).key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap map = new MyRbMap();
        headMap(root, toKey, map);
        return map;
    }

    private void headMap(Node node, Integer toKey, MyRbMap map) {
        if (node != null) {
            if (node.key.compareTo(toKey) < 0) map.put(node.key, node.value);
            headMap(node.left, toKey, map);
            headMap(node.right, toKey, map);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap map = new MyRbMap();
        tailMap(root, fromKey, map);
        return map;
    }

    private void tailMap(Node node, Integer fromKey, MyRbMap map) {
        if (node != null) {
            if (node.key.compareTo(fromKey) >= 0) map.put(node.key, node.value);
            tailMap(node.left, fromKey, map);
            tailMap(node.right, fromKey, map);
        }
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MyRbMap map = new MyRbMap();
        subMap(root, fromKey, toKey, map);
        return map;
    }

    private void subMap(Node node, Integer fromKey, Integer toKey, MyRbMap map) {
        if (node != null) {
            if (node.key.compareTo(fromKey) >= 0 && node.key.compareTo(toKey) < 0)
                map.put(node.key, node.value);
            subMap(node.left, fromKey, toKey, map);
            subMap(node.right, fromKey, toKey, map);
        }
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        if (!containsKey(key)) return null;

        String oldValue = get(key);
        root = remove(root, (Integer) key);
        if (root != null) root.color = BLACK;
        size--;
        return oldValue;
    }

    // Удаление из красно-черного дерева
    private Node remove(Node h, Integer key) {
        if (key.compareTo(h.key) < 0) {
            if (h.left != null) h.left = remove(h.left, key);
        } else if (key.compareTo(h.key) > 0) {
            if (h.right != null) h.right = remove(h.right, key);
        } else {
            // Удаляем узел h
            if (h.right == null) return h.left;
            if (h.left == null) return h.right;

            Node t = h;
            h = min(t.right);
            h.right = deleteMin(t.right);
            h.left = t.left;
        }
        return balance(h);
    }

    private Node balance(Node h) {
        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);
        return h;
    }

    private Node deleteMin(Node h) {
        if (h.left == null) return h.right;
        h.left = deleteMin(h.left);
        return balance(h);
    }


    @Override
    public Set<Integer> keySet() { throw new UnsupportedOperationException(); }

    @Override
    public Collection<String> values() { throw new UnsupportedOperationException(); }

    @Override
    public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}
