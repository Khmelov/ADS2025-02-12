package by.it.group410901.bukshta.lesson12;

import java.util.*;

@SuppressWarnings("unchecked")
public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private Node root;
    private int size;

    public MySplayMap() {
        root = null;
        size = 0;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
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
            if (x.parent.parent == null) {
                if (x == x.parent.left) rotateRight(x.parent);
                else rotateLeft(x.parent);
            } else if (x == x.parent.left && x.parent == x.parent.parent.left) {
                rotateRight(x.parent.parent);
                rotateRight(x.parent);
            } else if (x == x.parent.right && x.parent == x.parent.parent.right) {
                rotateLeft(x.parent.parent);
                rotateLeft(x.parent);
            } else if (x == x.parent.right && x.parent == x.parent.parent.left) {
                rotateLeft(x.parent);
                rotateRight(x.parent);
            } else {
                rotateRight(x.parent);
                rotateLeft(x.parent);
            }
        }
    }

    private Node getNode(Integer key) {
        Node t = root;
        Node last = null;
        while (t != null) {
            last = t;
            int cmp = key.compareTo(t.key);
            if (cmp < 0) t = t.left;
            else if (cmp > 0) t = t.right;
            else {
                splay(t);
                return t;
            }
        }
        if (last != null) splay(last);
        return null;
    }

    @Override
    public String get(Object key) {
        Node n = getNode((Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return getNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) return false;
        if ((value == null && node.value == null) || (value != null && value.equals(node.value)))
            return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value, null);
            size++;
            return null;
        }
        Node t = root;
        Node parent = null;
        int cmp = 0;
        while (t != null) {
            parent = t;
            cmp = key.compareTo(t.key);
            if (cmp < 0) t = t.left;
            else if (cmp > 0) t = t.right;
            else {
                String old = t.value;
                t.value = value;
                splay(t);
                return old;
            }
        }
        Node n = new Node(key, value, parent);
        if (cmp < 0) parent.left = n;
        else parent.right = n;
        splay(n);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        Node node = getNode((Integer) key);
        if (node == null) return null;
        String oldValue = node.value;
        if (node.left != null) {
            Node maxLeft = maximum(node.left);
            splay(maxLeft);
            maxLeft.right = node.right;
            if (node.right != null) node.right.parent = maxLeft;
            root = maxLeft;
            maxLeft.parent = null;
        } else if (node.right != null) {
            root = node.right;
            root.parent = null;
        } else {
            root = null;
        }
        size--;
        return oldValue;
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

    private void toStringHelper(Node node, StringBuilder sb) {
        if (node == null) return;
        toStringHelper(node.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(node.key).append("=").append(node.value);
        toStringHelper(node.right, sb);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        toStringHelper(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private Node minimum(Node node) {
        if (node == null) return null;
        while (node.left != null) node = node.left;
        return node;
    }

    private Node maximum(Node node) {
        if (node == null) return null;
        while (node.right != null) node = node.right;
        return node;
    }

    @Override
    public Integer firstKey() {
        return minimum(root).key;
    }

    @Override
    public Integer lastKey() {
        return maximum(root).key;
    }

    // ===== Навигационные методы =====
    private Integer lower(Node node, Integer key) {
        Integer res = null;
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp <= 0) node = node.left;
            else {
                res = node.key;
                node = node.right;
            }
        }
        return res;
    }

    @Override
    public Integer lowerKey(Integer key) {
        return lower(root, key);
    }

    private Integer floor(Node node, Integer key) {
        Integer res = null;
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) node = node.left;
            else {
                res = node.key;
                node = node.right;
            }
        }
        return res;
    }

    @Override
    public Integer floorKey(Integer key) {
        return floor(root, key);
    }

    private Integer ceiling(Node node, Integer key) {
        Integer res = null;
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp > 0) node = node.right;
            else {
                res = node.key;
                node = node.left;
            }
        }
        return res;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        return ceiling(root, key);
    }

    private Integer higher(Node node, Integer key) {
        Integer res = null;
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp >= 0) node = node.right;
            else {
                res = node.key;
                node = node.left;
            }
        }
        return res;
    }

    @Override
    public Integer higherKey(Integer key) {
        return higher(root, key);
    }


    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        MySplayMap map = new MySplayMap();
        fillHeadMap(root, toKey, map);
        return map;
    }

    private void fillHeadMap(Node node, Integer toKey, MySplayMap map) {
        if (node == null) return;
        if (node.key.compareTo(toKey) < 0) {
            map.put(node.key, node.value);
            fillHeadMap(node.left, toKey, map);
            fillHeadMap(node.right, toKey, map);
        } else {
            fillHeadMap(node.left, toKey, map);
        }
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap map = new MySplayMap();
        fillTailMap(root, fromKey, map);
        return map;
    }

    private void fillTailMap(Node node, Integer fromKey, MySplayMap map) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0) {
            map.put(node.key, node.value);
            fillTailMap(node.left, fromKey, map);
            fillTailMap(node.right, fromKey, map);
        } else {
            fillTailMap(node.right, fromKey, map);
        }
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return headMap(toKey);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return tailMap(fromKey);
    }

    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException();
    }

    // ===== Map интерфейс =====
    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
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
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }
}

