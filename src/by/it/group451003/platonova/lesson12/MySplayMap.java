package by.it.group451003.platonova.lesson12;

import java.util.*;

@SuppressWarnings("unchecked")
public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size = 0;

    // ======== Splay операции ========
    private void rotateRight(Node x) {
        Node y = x.left;
        if (y == null) return;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.right = x;
        x.parent = y;
    }

    private void rotateLeft(Node x) {
        Node y = x.right;
        if (y == null) return;
        x.right = y.left;
        if (y.left != null) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    private void splay(Node x) {
        if (x == null) return;
        while (x.parent != null) {
            if (x.parent.parent == null) { // Zig
                if (x == x.parent.left) rotateRight(x.parent);
                else rotateLeft(x.parent);
            } else if (x == x.parent.left && x.parent == x.parent.parent.left) { // Zig-Zig
                rotateRight(x.parent.parent);
                rotateRight(x.parent);
            } else if (x == x.parent.right && x.parent == x.parent.parent.right) { // Zig-Zig
                rotateLeft(x.parent.parent);
                rotateLeft(x.parent);
            } else if (x == x.parent.right && x.parent == x.parent.parent.left) { // Zig-Zag
                rotateLeft(x.parent);
                rotateRight(x.parent);
            } else { // Zig-Zag
                rotateRight(x.parent);
                rotateLeft(x.parent);
            }
        }
    }

    private Node findNode(Integer key) {
        Node n = root;
        Node last = root;
        while (n != null) {
            last = n;
            int cmp = key.compareTo(n.key);
            if (cmp < 0) n = n.left;
            else if (cmp > 0) n = n.right;
            else {
                splay(n);
                return n;
            }
        }
        if (last != null) splay(last);
        return null;
    }

    // ======== Map методы ========
    @Override
    public String get(Object key) {
        Node n = findNode((Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return findNode((Integer) key) != null;
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) return false;
        if (Objects.equals(node.value, value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }
        Node n = root;
        Node parent = null;
        int cmp = 0;
        while (n != null) {
            parent = n;
            cmp = key.compareTo(n.key);
            if (cmp < 0) n = n.left;
            else if (cmp > 0) n = n.right;
            else {
                splay(n);
                String old = n.value;
                n.value = value;
                return old;
            }
        }
        Node newNode = new Node(key, value);
        newNode.parent = parent;
        if (cmp < 0) parent.left = newNode;
        else parent.right = newNode;
        splay(newNode);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        Node n = findNode((Integer) key);
        if (n == null) return null;
        splay(n);
        String old = n.value;
        if (n.left == null) transplant(n, n.right);
        else if (n.right == null) transplant(n, n.left);
        else {
            Node min = n.right;
            while (min.left != null) min = min.left;
            if (min.parent != n) {
                transplant(min, min.right);
                min.right = n.right;
                min.right.parent = min;
            }
            transplant(n, min);
            min.left = n.left;
            min.left.parent = min;
        }
        size--;
        return old;
    }

    private void transplant(Node u, Node v) {
        if (u.parent == null) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        if (v != null) v.parent = u.parent;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void inOrder(Node node, StringBuilder sb) {
        if (node == null) return;
        inOrder(node.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(node.key).append("=").append(node.value);
        inOrder(node.right, sb);
    }

    // ===== NavigableMap методы =====
    @Override
    public Integer firstKey() {
        Node n = root;
        if (n == null) throw new NoSuchElementException();
        while (n.left != null) n = n.left;
        return n.key;
    }

    @Override
    public Integer lastKey() {
        Node n = root;
        if (n == null) throw new NoSuchElementException();
        while (n.right != null) n = n.right;
        return n.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap map = new MySplayMap();
        addHeadTailMap(root, map, null, toKey);
        return map;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap map = new MySplayMap();
        addHeadTailMap(root, map, fromKey, null);
        return map;
    }

    private void addHeadTailMap(Node node, MySplayMap map, Integer fromKey, Integer toKey) {
        if (node == null) return;
        addHeadTailMap(node.left, map, fromKey, toKey);
        if ((fromKey == null || node.key >= fromKey) && (toKey == null || node.key < toKey)) {
            map.put(node.key, node.value);
        }
        addHeadTailMap(node.right, map, fromKey, toKey);
    }

    private Node lowerCeilFloor(Node node, Integer key, boolean lower, boolean inclusive) {
        Node res = null;
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if ((lower && cmp > 0) || (!lower && cmp < 0) || (inclusive && cmp == 0)) {
                res = node;
                node = lower ? node.right : node.left;
            } else node = lower ? node.left : node.right;
        }
        return res;
    }

    @Override
    public Integer lowerKey(Integer key) { return lowerCeilFloor(root, key, true, false) == null ? null : lowerCeilFloor(root, key, true, false).key; }
    @Override
    public Integer floorKey(Integer key) { return lowerCeilFloor(root, key, true, true) == null ? null : lowerCeilFloor(root, key, true, true).key; }
    @Override
    public Integer ceilingKey(Integer key) { return lowerCeilFloor(root, key, false, true) == null ? null : lowerCeilFloor(root, key, false, true).key; }
    @Override
    public Integer higherKey(Integer key) { return lowerCeilFloor(root, key, false, false) == null ? null : lowerCeilFloor(root, key, false, false).key; }

    // ===== Заглушки NavigableMap =====
    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> lowerEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> floorEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> ceilingEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> higherEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> firstEntry() { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> lastEntry() { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> pollFirstEntry() { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer, String> pollLastEntry() { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> descendingMap() { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> navigableKeySet() { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> descendingKeySet() { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
}

