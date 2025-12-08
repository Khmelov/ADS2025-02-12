package by.it.group451003.halubionak.lesson12;

import java.util.*;

@SuppressWarnings("unchecked")
public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size = 0;

    private Node splay(Node root, Integer key) {
        if (root == null) return null;
        int cmp1 = key.compareTo(root.key);
        if (cmp1 < 0) {
            if (root.left == null) return root;
            int cmp2 = key.compareTo(root.left.key);
            if (cmp2 < 0) {
                root.left.left = splay(root.left.left, key);
                root = rotateRight(root);
            } else if (cmp2 > 0) {
                root.left.right = splay(root.left.right, key);
                if (root.left.right != null) root.left = rotateLeft(root.left);
            }
            return root.left == null ? root : rotateRight(root);
        } else if (cmp1 > 0) {
            if (root.right == null) return root;
            int cmp2 = key.compareTo(root.right.key);
            if (cmp2 < 0) {
                root.right.left = splay(root.right.left, key);
                if (root.right.left != null) root.right = rotateRight(root.right);
            } else if (cmp2 > 0) {
                root.right.right = splay(root.right.right, key);
                root = rotateLeft(root);
            }
            return root.right == null ? root : rotateLeft(root);
        } else return root;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        return x;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        return x;
    }

    // ========== MAP OPERATIONS ==========
    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }
        root = splay(root, key);
        int cmp = key.compareTo(root.key);
        if (cmp < 0) {
            Node node = new Node(key, value);
            node.left = root.left;
            node.right = root;
            root.left = null;
            root = node;
            size++;
            return null;
        } else if (cmp > 0) {
            Node node = new Node(key, value);
            node.right = root.right;
            node.left = root;
            root.right = null;
            root = node;
            size++;
            return null;
        } else {
            String oldValue = root.value;
            root.value = value;
            return oldValue;
        }
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        root = splay(root, (Integer) key);
        return root != null && root.key.equals(key) ? root.value : null;
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
        if (node.value.equals(value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer) || root == null) return null;
        root = splay(root, (Integer) key);
        if (!root.key.equals(key)) return null;
        String removedValue = root.value;
        if (root.left == null) root = root.right;
        else {
            Node temp = root.right;
            root = root.left;
            root = splay(root, (Integer) key);
            root.right = temp;
        }
        size--;
        return removedValue;
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() { root = null; size = 0; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public Integer firstKey() {
        if (root == null) return null;
        Node current = root;
        while (current.left != null) current = current.left;
        root = splay(root, current.key);
        return current.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) return null;
        Node current = root;
        while (current.right != null) current = current.right;
        root = splay(root, current.key);
        return current.key;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void inOrder(Node node, StringBuilder sb) {
        if (node != null) {
            inOrder(node.left, sb);
            if (sb.length() > 1) sb.append(", ");
            sb.append(node.key).append("=").append(node.value);
            inOrder(node.right, sb);
        }
    }

    // ========== NAVIGABLE METHODS ==========
    private Node findLower(Node node, Integer key) {
        Node result = null;
        while (node != null) {
            if (node.key.compareTo(key) < 0) {
                result = node;
                node = node.right;
            } else node = node.left;
        }
        return result;
    }

    private Node findFloor(Node node, Integer key) {
        Node result = null;
        while (node != null) {
            int cmp = node.key.compareTo(key);
            if (cmp == 0) return node;
            if (cmp < 0) {
                result = node;
                node = node.right;
            } else node = node.left;
        }
        return result;
    }

    private Node findCeiling(Node node, Integer key) {
        Node result = null;
        while (node != null) {
            int cmp = node.key.compareTo(key);
            if (cmp == 0) return node;
            if (cmp > 0) {
                result = node;
                node = node.left;
            } else node = node.right;
        }
        return result;
    }

    private Node findHigher(Node node, Integer key) {
        Node result = null;
        while (node != null) {
            if (node.key.compareTo(key) > 0) {
                result = node;
                node = node.left;
            } else node = node.right;
        }
        return result;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node node = findLower(root, key);
        return node != null ? node.key : null;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node node = findFloor(root, key);
        return node != null ? node.key : null;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node node = findCeiling(root, key);
        return node != null ? node.key : null;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node node = findHigher(root, key);
        return node != null ? node.key : null;
    }

    // ========== HEAD / TAIL MAPS ==========
    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap map = new MySplayMap();
        headMap(root, toKey, inclusive, map);
        return map;
    }

    private void headMap(Node node, Integer toKey, boolean inclusive, MySplayMap map) {
        if (node != null) {
            int cmp = node.key.compareTo(toKey);
            if (cmp < 0 || (inclusive && cmp == 0)) map.put(node.key, node.value);
            headMap(node.left, toKey, inclusive, map);
            headMap(node.right, toKey, inclusive, map);
        }
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap map = new MySplayMap();
        tailMap(root, fromKey, inclusive, map);
        return map;
    }

    private void tailMap(Node node, Integer fromKey, boolean inclusive, MySplayMap map) {
        if (node != null) {
            int cmp = node.key.compareTo(fromKey);
            if (cmp > 0 || (inclusive && cmp == 0)) map.put(node.key, node.value);
            tailMap(node.left, fromKey, inclusive, map);
            tailMap(node.right, fromKey, inclusive, map);
        }
    }

    // ========== UNUSED / DEFAULT METHODS ==========
    @Override
    public Comparator<? super Integer> comparator() { return null; }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() { throw new UnsupportedOperationException(); }
    @Override
    public NavigableSet<Integer> navigableKeySet() { throw new UnsupportedOperationException(); }
    @Override
    public NavigableSet<Integer> descendingKeySet() { throw new UnsupportedOperationException(); }
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) { for (Entry<? extends Integer, ? extends String> e : m.entrySet()) put(e.getKey(), e.getValue()); }
    @Override
    public boolean equals(Object o) { throw new UnsupportedOperationException(); }
    @Override
    public int hashCode() { throw new UnsupportedOperationException(); }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override
    public Entry<Integer, String> floorEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override
    public Entry<Integer, String> higherEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override
    public Entry<Integer, String> firstEntry() { throw new UnsupportedOperationException(); }
    @Override
    public Entry<Integer, String> lastEntry() { throw new UnsupportedOperationException(); }
    @Override
    public Entry<Integer, String> pollFirstEntry() { throw new UnsupportedOperationException(); }
    @Override
    public Entry<Integer, String> pollLastEntry() { throw new UnsupportedOperationException(); }
    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) { return headMap(toKey, false); }
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) { return tailMap(fromKey, true); }

    @Override
    public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override
    public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override
    public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}
