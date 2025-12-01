package by.it.group451003.khmilevskiy.lesson12;

import java.util.*;

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

    private void rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        if (x.right != null) x.right.parent = y;
        x.parent = y.parent;
        if (y.parent == null) root = x;
        else if (y == y.parent.right) y.parent.right = x;
        else y.parent.left = x;
        x.right = y;
        y.parent = x;
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
            } else if (x == x.parent.left && x.parent == x.parent.parent.right) {
                rotateRight(x.parent);
                rotateLeft(x.parent);
            } else {
                rotateLeft(x.parent);
                rotateRight(x.parent);
            }
        }
    }

    private Node find(Integer key) {
        Node node = root;
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
        if (root == null) {
            root = new Node(key, value, null);
            size++;
            return null;
        }
        Node node = root;
        Node parent = null;
        String old = null;
        while (node != null) {
            parent = node;
            int cmp = key.compareTo(node.key);
            if (cmp < 0) node = node.left;
            else if (cmp > 0) node = node.right;
            else {
                old = node.value;
                node.value = value;
                splay(node);
                return old;
            }
        }
        Node newNode = new Node(key, value, parent);
        if (key.compareTo(parent.key) < 0) parent.left = newNode;
        else parent.right = newNode;
        size++;
        splay(newNode);
        return null;
    }

    @Override
    public String remove(Object o) {
        if (!(o instanceof Integer)) return null;
        Integer key = (Integer) o;
        Node node = find(key);
        if (node == null) return null;
        String old = node.value;
        splay(node);
        Node leftTree = root.left;
        if (leftTree != null) leftTree.parent = null;
        Node rightTree = root.right;
        if (rightTree != null) rightTree.parent = null;
        if (leftTree == null) {
            root = rightTree;
        } else if (rightTree == null) {
            root = leftTree;
        } else {
            Node max = leftTree;
            while (max.right != null) max = max.right;
            root = leftTree;
            splay(max);
            root.right = rightTree;
            if (rightTree != null) rightTree.parent = root;
        }
        size--;
        return old;
    }

    @Override
    public String get(Object o) {
        if (!(o instanceof Integer)) return null;
        Integer key = (Integer) o;
        Node node = find(key);
        if (node == null) return null;
        splay(node);
        return node.value;
    }

    @Override
    public boolean containsKey(Object o) {
        if (!(o instanceof Integer)) return false;
        Integer key = (Integer) o;
        Node node = find(key);
        if (node == null) return false;
        splay(node);
        return true;
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) return false;
        if (node.value.equals(value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public boolean containsValue(Object o) {
        return containsValue(root, o);
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

    private void buildString(Node node, StringBuilder sb) {
        if (node == null) return;
        buildString(node.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(node.key).append("=").append(node.value);
        buildString(node.right, sb);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        buildString(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void addToHeadMap(Node node, MySplayMap sub, Integer toKey) {
        if (node == null) return;
        addToHeadMap(node.left, sub, toKey);
        if (node.key.compareTo(toKey) < 0) sub.put(node.key, node.value);
        addToHeadMap(node.right, sub, toKey);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap sub = new MySplayMap();
        addToHeadMap(root, sub, toKey);
        if (inclusive && containsKey(toKey)) sub.put(toKey, get(toKey));
        return sub;
    }

    private void addToTailMap(Node node, MySplayMap sub, Integer fromKey) {
        if (node == null) return;
        addToTailMap(node.left, sub, fromKey);
        if (node.key.compareTo(fromKey) >= 0) sub.put(node.key, node.value);
        addToTailMap(node.right, sub, fromKey);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap sub = new MySplayMap();
        addToTailMap(root, sub, fromKey);
        if (!inclusive && containsKey(fromKey)) sub.remove(fromKey);
        return sub;
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = root;
        while (node.left != null) node = node.left;
        splay(node);
        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = root;
        while (node.right != null) node = node.right;
        splay(node);
        return node.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node node = root;
        Node res = null;
        while (node != null) {
            if (node.key.compareTo(key) < 0) {
                res = node;
                node = node.right;
            } else {
                node = node.left;
            }
        }
        if (res != null) splay(res);
        return res == null ? null : res.key;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node node = root;
        Node res = null;
        while (node != null) {
            int cmp = node.key.compareTo(key);
            if (cmp == 0) {
                splay(node);
                return node.key;
            } else if (cmp < 0) {
                res = node;
                node = node.right;
            } else {
                node = node.left;
            }
        }
        if (res != null) splay(res);
        return res == null ? null : res.key;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node node = root;
        Node res = null;
        while (node != null) {
            int cmp = node.key.compareTo(key);
            if (cmp == 0) {
                splay(node);
                return node.key;
            } else if (cmp > 0) {
                res = node;
                node = node.left;
            } else {
                node = node.right;
            }
        }
        if (res != null) splay(res);
        return res == null ? null : res.key;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node node = root;
        Node res = null;
        while (node != null) {
            if (node.key.compareTo(key) > 0) {
                res = node;
                node = node.left;
            } else {
                node = node.right;
            }
        }
        if (res != null) splay(res);
        return res == null ? null : res.key;
    }

    private boolean checkEntries(Node node, Map<?, ?> m) {
        if (node == null) return true;
        if (!checkEntries(node.left, m)) return false;
        Object val = m.get(node.key);
        if (node.value == null ? val != null : !node.value.equals(val)) return false;
        return checkEntries(node.right, m);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Map)) return false;
        Map<?, ?> m = (Map<?, ?>) o;
        if (m.size() != size()) return false;
        return checkEntries(root, m);
    }

    @Override
    public int hashCode() {
        return calculateHash(root);
    }

    private int calculateHash(Node node) {
        if (node == null) return 0;
        int h = (node.key == null ? 0 : node.key.hashCode()) ^ (node.value == null ? 0 : node.value.hashCode());
        return calculateHash(node.left) + h + calculateHash(node.right) * 31;
    }

    // Unsupported or partial
    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
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
    public Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
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