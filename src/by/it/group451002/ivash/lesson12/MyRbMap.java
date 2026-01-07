package by.it.group451002.ivash.lesson12;
import java.util.Map;
import java.util.SortedMap;
import java.util.Set;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean color;

        Node(Integer k, String v, boolean color, Node parent) {
            this.key = k;
            this.value = v;
            this.color = color;
            this.parent = parent;
        }
    }

    private Node root;
    private int size = 0;

    public MyRbMap() {
        root = null;
        size = 0;
    }

    private int compare(Integer a, Integer b) {
        return a.compareTo(b);
    }

    private Node treeSearch(Integer key) {
        Node x = root;
        while (x != null) {
            int cmp = compare(key, x.key);
            if (cmp == 0) return x;
            x = (cmp < 0) ? x.left : x.right;
        }
        return null;
    }

    private Node minimum(Node x) {
        if (x == null) return null;
        while (x.left != null) x = x.left;
        return x;
    }

    private Node maximum(Node x) {
        if (x == null) return null;
        while (x.right != null) x = x.right;
        return x;
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

    private void rotateRight(Node x) {
        Node y = x.left;
        if (y == null) return;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;
        y.right = x;
        x.parent = y;
    }

    private void fixAfterInsert(Node z) {
        z.color = RED;
        while (z != null && z != root && z.parent.color == RED) {
            if (z.parent == z.parent.parent.left) {
                Node y = z.parent.parent.right;
                if (y != null && y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        z = z.parent;
                        rotateLeft(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateRight(z.parent.parent);
                }
            } else {
                Node y = z.parent.parent.left;
                if (y != null && y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rotateRight(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateLeft(z.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    @Override
    public String put(Integer key, String value) {
        Objects.requireNonNull(key, "Null keys are not supported");
        Node y = null;
        Node x = root;
        while (x != null) {
            y = x;
            int cmp = compare(key, x.key);
            if (cmp == 0) {
                String old = x.value;
                x.value = value;
                return old;
            }
            x = (cmp < 0) ? x.left : x.right;
        }
        Node z = new Node(key, value, RED, y);
        if (y == null) root = z;
        else if (compare(z.key, y.key) < 0) y.left = z;
        else y.right = z;
        fixAfterInsert(z);
        size++;
        return null;
    }

    @Override
    public String remove(Object keyObj) {
        if (keyObj == null || !(keyObj instanceof Integer)) return null;
        Integer key = (Integer) keyObj;
        Node found = treeSearch(key);
        if (found == null) return null;
        String oldValue = found.value;

        MyRbMap newTree = new MyRbMap();
        inorderCopyExcept(root, newTree, key);
        this.root = newTree.root;
        this.size = newTree.size;
        return oldValue;
    }

    private void inorderCopyExcept(Node node, MyRbMap target, Integer excludedKey) {
        if (node == null) return;
        inorderCopyExcept(node.left, target, excludedKey);
        if (!node.key.equals(excludedKey)) target.put(node.key, node.value);
        inorderCopyExcept(node.right, target, excludedKey);
    }

    @Override
    public String get(Object keyObj) {
        if (keyObj == null || !(keyObj instanceof Integer)) return null;
        Integer key = (Integer) keyObj;
        Node n = treeSearch(key);
        return (n == null) ? null : n.value;
    }

    @Override
    public boolean containsKey(Object keyObj) {
        if (keyObj == null || !(keyObj instanceof Integer)) return false;
        return treeSearch((Integer) keyObj) != null;
    }

    @Override
    public boolean containsValue(Object valueObj) {
        return containsValueInSubtree(root, valueObj);
    }

    private boolean containsValueInSubtree(Node node, Object v) {
        if (node == null) return false;
        if (Objects.equals(node.value, v)) return true;
        return containsValueInSubtree(node.left, v) || containsValueInSubtree(node.right, v);
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
        Objects.requireNonNull(toKey);
        MyRbMap m = new MyRbMap();
        headTailCopy(root, m, null, toKey);
        return m;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        Objects.requireNonNull(fromKey);
        MyRbMap m = new MyRbMap();
        headTailCopy(root, m, fromKey, null);
        return m;
    }

    private void headTailCopy(Node node, MyRbMap target, Integer fromInclusive, Integer toExclusive) {
        if (node == null) return;
        headTailCopy(node.left, target, fromInclusive, toExclusive);
        boolean okLower = fromInclusive == null || compare(node.key, fromInclusive) >= 0;
        boolean okUpper = toExclusive == null || compare(node.key, toExclusive) < 0;
        if (okLower && okUpper) target.put(node.key, node.value);
        headTailCopy(node.right, target, fromInclusive, toExclusive);
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        return minimum(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        return maximum(root).key;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        ToStringHelper helper = new ToStringHelper();
        inorderToString(root, helper);
        sb.append(helper.sb);
        sb.append("}");
        return sb.toString();
    }

    private static class ToStringHelper {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
    }

    private void inorderToString(Node node, ToStringHelper helper) {
        if (node == null) return;
        inorderToString(node.left, helper);
        if (!helper.first) helper.sb.append(", ");
        helper.first = false;
        helper.sb.append(node.key).append("=").append(node.value);
        inorderToString(node.right, helper);
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        Objects.requireNonNull(fromKey);
        Objects.requireNonNull(toKey);
        if (compare(fromKey, toKey) > 0) throw new IllegalArgumentException("fromKey > toKey");
        MyRbMap m = new MyRbMap();
        headTailCopy(root, m, fromKey, toKey);
        return m;
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("entrySet() not implemented");
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException("keySet() not implemented");
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException("values() not implemented");
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public boolean equals(Object o) { return super.equals(o); }
    @Override
    public int hashCode() { return super.hashCode(); }
}
