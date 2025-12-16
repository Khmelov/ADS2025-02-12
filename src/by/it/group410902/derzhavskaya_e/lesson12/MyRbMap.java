package by.it.group410902.derzhavskaya_e.lesson12;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.Set;
import java.util.Collection;


public class MyRbMap implements SortedMap<Integer, String>, Map<Integer,String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean color; // RED = true, BLACK = false

        Node(Integer k, String v, boolean color, Node parent) {
            this.key = k;
            this.value = v;
            this.color = color;
            this.parent = parent;
        }
    }

    private Node root;
    private int size = 0;
    private final Comparator<? super Integer> comparator;

    public MyRbMap() {
        this.comparator = null;
    }

    public MyRbMap(Comparator<? super Integer> comp) {
        this.comparator = comp;
    }

    private int cmp(Integer a, Integer b) {
        if (comparator != null) return comparator.compare(a, b);
        return a.compareTo(b);
    }


    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("Key is null");
        Node y = null;
        Node x = root;

        while (x != null) {
            y = x;
            int c = cmp(key, x.key);
            if (c == 0) {
                String old = x.value;
                x.value = value;
                return old;
            } else if (c < 0) x = x.left;
            else x = x.right;
        }

        Node z = new Node(key, value, RED, y);
        if (y == null) root = z;
        else if (cmp(key, y.key) < 0) y.left = z;
        else y.right = z;

        insertFixup(z);
        size++;
        return null;
    }

    private void insertFixup(Node z) {
        while (z.parent != null && z.parent.color == RED) {
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

    private void rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;
        y.right = x;
        x.parent = y;
    }

    @Override
    public String get(Object keyObj) {
        if (keyObj == null) return null;
        Integer key = (Integer) keyObj;
        Node n = getNode(key);
        return n == null ? null : n.value;
    }

    private Node getNode(Integer key) {
        Node x = root;
        while (x != null) {
            int c = cmp(key, x.key);
            if (c == 0) return x;
            else if (c < 0) x = x.left;
            else x = x.right;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object keyObj) {
        if (keyObj == null) return false;
        return getNode((Integer) keyObj) != null;
    }

    @Override
    public boolean containsValue(Object valueObj) {
        return containsValueRec(root, valueObj);
    }

    private boolean containsValueRec(Node node, Object val) {
        if (node == null) return false;
        if (java.util.Objects.equals(node.value, val)) return true;
        return containsValueRec(node.left, val) || containsValueRec(node.right, val);
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
    public String remove(Object keyObj) {
        if (keyObj == null) return null;
        Integer key = (Integer) keyObj;
        Node z = getNode(key);
        if (z == null) return null;
        String oldValue = z.value;
        deleteNode(z);
        size--;
        return oldValue;
    }

    private void deleteNode(Node z) {
        Node y = z;
        boolean yOriginalColor = y.color;
        Node x;

        if (z.left == null) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == null) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                if (x != null) x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = z.right;
                if (y.right != null) y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            if (y.left != null) y.left.parent = y;
            y.color = z.color;
        }

        if (yOriginalColor == BLACK) {
            deleteFixup(x, y.parent);
        }
    }

    private void transplant(Node u, Node v) {
        if (u.parent == null) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        if (v != null) v.parent = u.parent;
    }

    private Node minimum(Node x) {
        while (x.left != null) x = x.left;
        return x;
    }

    private Node maximum(Node x) {
        while (x.right != null) x = x.right;
        return x;
    }

    private void deleteFixup(Node x, Node xParent) {
        while ((x != root) && (x == null || colorOf(x) == BLACK)) {
            if (xParent == null) break;
            if (x == xParent.left) {
                Node w = xParent.right;
                if (w != null && colorOf(w) == RED) {
                    w.color = BLACK;
                    xParent.color = RED;
                    rotateLeft(xParent);
                    w = xParent.right;
                }
                if ( (w == null) ||
                        (colorOf(w.left) == BLACK && colorOf(w.right) == BLACK) ) {
                    if (w != null) w.color = RED;
                    x = xParent;
                    xParent = x.parent;
                } else {
                    if (colorOf(w.right) == BLACK) {
                        if (w.left != null) w.left.color = BLACK;
                        w.color = RED;
                        rotateRight(w);
                        w = xParent.right;
                    }
                    if (w != null) w.color = xParent.color;
                    xParent.color = BLACK;
                    if (w != null && w.right != null) w.right.color = BLACK;
                    rotateLeft(xParent);
                    x = root;
                    xParent = null;
                }
            } else {
                Node w = xParent.left;
                if (w != null && colorOf(w) == RED) {
                    w.color = BLACK;
                    xParent.color = RED;
                    rotateRight(xParent);
                    w = xParent.left;
                }
                if ( (w == null) ||
                        (colorOf(w.right) == BLACK && colorOf(w.left) == BLACK) ) {
                    if (w != null) w.color = RED;
                    x = xParent;
                    xParent = x.parent;
                } else {
                    if (colorOf(w.left) == BLACK) {
                        if (w.right != null) w.right.color = BLACK;
                        w.color = RED;
                        rotateLeft(w);
                        w = xParent.left;
                    }
                    if (w != null) w.color = xParent.color;
                    xParent.color = BLACK;
                    if (w != null && w.left != null) w.left.color = BLACK;
                    rotateRight(xParent);
                    x = root;
                    xParent = null;
                }
            }
        }
        if (x != null) x.color = BLACK;
    }

    private boolean colorOf(Node n) {
        return n == null ? BLACK : n.color;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) throw new NullPointerException();
        MyRbMap res = new MyRbMap(this.comparator);
        copyRange(root, res, null, toKey, true);
        return res;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) throw new NullPointerException();
        MyRbMap res = new MyRbMap(this.comparator);
        copyRange(root, res, fromKey, null, false);
        return res;
    }

    private void copyRange(Node node, MyRbMap target, Integer low, Integer high, boolean isHead) {
        if (node == null) return;
        // left
        copyRange(node.left, target, low, high, isHead);
        boolean accept = true;
        if (low != null) {
            // tailMap uses low as fromKey and should include keys >= low
            if (cmp(node.key, low) < 0) accept = false;
        }
        if (high != null) {
            // headMap uses high as toKey and should include keys < high
            if (cmp(node.key, high) >= 0) accept = false;
        }
        if (accept) target.put(node.key, node.value);
        // right
        copyRange(node.right, target, low, high, isHead);
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
        boolean[] first = new boolean[]{true};
        toStringRec(root, sb, first);
        sb.append("}");
        return sb.toString();
    }

    private void toStringRec(Node node, StringBuilder sb, boolean[] first) {
        if (node == null) return;
        toStringRec(node.left, sb, first);
        if (!first[0]) {
            sb.append(", ");
        }
        sb.append(node.key).append("=").append(node.value);
        first[0] = false;
        toStringRec(node.right, sb, first);
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return comparator;
    }

    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("entrySet not implemented");
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException("keySet not implemented");
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException("values not implemented");
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        if (fromKey == null || toKey == null) throw new NullPointerException();
        if (cmp(fromKey, toKey) > 0) throw new IllegalArgumentException("fromKey > toKey");
        MyRbMap res = new MyRbMap(this.comparator);
        copyRange(root, res, fromKey, toKey, false);
        return res;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public boolean equals(Object o) {
        // Default: fallback to Map implementation is complex — mark unsupported for safety
        throw new UnsupportedOperationException("equals not implemented");
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("hashCode not implemented");
    }

}
